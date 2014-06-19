package net.catchpole.io;

//   Copyright 2014 catchpole.net
//
//   Licensed under the Apache License, Version 2.0 (the "License");
//   you may not use this file except in compliance with the License.
//   You may obtain a copy of the License at
//
//       http://www.apache.org/licenses/LICENSE-2.0
//
//   Unless required by applicable law or agreed to in writing, software
//   distributed under the License is distributed on an "AS IS" BASIS,
//   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
//   See the License for the specific language governing permissions and
//   limitations under the License.

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * An OutputStream which writes to a circular buffer, possibly looping around and overwriting
 * the oldest data.  This class in an OutputStreamSource so that InputStreams can be aquired
 * which read data from the circular buffer, blocking until data is available.
 * The InputStreams will throw IOExceptions if they have not read the available data before
 * the oldest data has been overwritten.
 * <p/>
 * <p>This class is suited to streaming applications whos data is fed on a timed basis.  Should large
 * amounts of data be fed into the stream in a short time, the InputStreams may not be expected to
 * read this data quickly enough.  An IOSynchronizationException will be throw in this instance.
 * <p/>
 * <p>The size of the buffer should be several times the size of the blocks written to the stream.
 * eg. for an audio streamer that writes a 10000 byte block every second, the buffer could be 40000 bytes,
 * allowing up to 3 seconds delay on any of the reading threads.
 */
public class CircularOutputStream extends OutputStream implements InputStreamSource {
    private final byte[] single = new byte[1];
    private byte[] buffer;
    private long position;
    private int end;
    private boolean closed = false;

    public CircularOutputStream(int size) {
        this.buffer = new byte[size];
    }

    public void write(int b) throws IOException {
        single[0] = (byte) b;
        write(single, 0, 1);
    }

    public void write(byte b[]) throws IOException {
        write(b, 0, b.length);
    }

    public void write(byte b[], int off, int len) throws IOException {
        while (len > 0) {
            int block = len > buffer.length ? buffer.length : len;
            writeBlock(b, off, block);
            off += block;
            len -= block;
        }
    }

    private synchronized void writeBlock(byte b[], int off, int len) {
        int remain = buffer.length - end;
        // wrapped write?
        if (len > remain) {
            System.arraycopy(b, off, buffer, end, remain);
            System.arraycopy(b, off + remain, buffer, 0, len - remain);
            end = len - remain;
        } else {
            System.arraycopy(b, off, buffer, end, len);
            end += len;
        }
        if (end == buffer.length) {
            end = 0;
        }
        position += len;
        // tell all listening input streams that data is available
        this.notifyAll();
    }

    /**
     * Returns the number of bytes written so far.
     *
     * @return the number of bytes written so far.
     */
    public long getPosition() {
        return position;
    }

    public synchronized void close() throws IOException {
        this.closed = true;
        this.notifyAll();
    }

    public String toString() {
        return this.getClass().getSimpleName() + ' ' + position + '/' + end;
    }

    public InputStream getInputStream() throws IOException {
        return new ListeningInputStream();
    }

    class ListeningInputStream extends InputStream {
        private byte[] single = new byte[1];
        private long localPosition;

        public ListeningInputStream() {
            localPosition = position;
        }

        public int read() throws IOException {
            return read(single) == -1 ? -1 : single[0] & 0xff;
        }

        public int read(byte b[]) throws IOException {
            return read(b, 0, b.length);
        }

        public int read(byte b[], int off, int len) throws IOException {
            try {
                synchronized (CircularOutputStream.this) {
                    long avail;
                    while ((avail = position - localPosition) <= 0 && !closed) {
                        CircularOutputStream.this.wait();
                    }
                    if (closed) {
                        return -1;
                    }
                    if (avail > buffer.length) {
                        throw new IOSynchronizationException("CircularOutputStream out of sync - data not consumed fast enough");
                    }
                    // shorten length to read if less is available
                    if (len > avail) {
                        len = (int) avail;
                    }
                    int start = end - len;
                    // wrap of flat copy?
                    if (start < 0) {
                        int endBit = 0 - start;
                        System.arraycopy(buffer, buffer.length - endBit, b, off, endBit);
                        System.arraycopy(buffer, 0, b, off + endBit, len - endBit);
                    } else {
                        System.arraycopy(buffer, start, b, off, len);
                    }
                    localPosition += len;
                }
                return len;
            } catch (InterruptedException ie) {
                throw new IOException("Interrupted while waiting for data");
            }
        }

        public long skip(long n) throws IOException {
            synchronized (CircularOutputStream.this) {
                long gap = position - localPosition;
                // can only skip a maximum of the current difference
                if (n > gap) {
                    n = gap;
                }
                localPosition += n;
            }
            return n;
        }

        public int available() throws IOException {
            synchronized (CircularOutputStream.this) {
                return position > end ? buffer.length : end;
            }
        }
    }
}
