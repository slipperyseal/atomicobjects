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
import java.io.OutputStream;

/**
 * An OutputStream which will buffer input until a specified threshold is reached, choosing one of two
 * streams to write to.
 * <p/>
 * <p>This class is useful for size sensitive operations where small amounts of data
 * can be processed in memory or large amounts need to be sent to disk.
 */
public class ThresholdOutputStream extends OutputStream {
    private byte[] buffer;
    private int pos;
    private boolean tripped;
    private boolean closed;
    private OutputStream smallStream;
    private OutputStream tripStream;

    public ThresholdOutputStream(OutputStream smallStream, OutputStream tripStream, int threshold) {
        this.smallStream = smallStream;
        this.tripStream = tripStream;
        this.buffer = new byte[threshold];
    }

    public void write(int b) throws IOException {
        if (tripped) {
            tripStream.write(b);
        } else if (pos < buffer.length) {
            buffer[pos++] = (byte) b;
        } else {
            // buffer full, so switch to tripStream
            tripped = true;
            // write all we have buffered
            tripStream.write(buffer);
            // write this request
            tripStream.write(b);
            buffer = null;
        }
    }

    public void write(byte b[]) throws IOException {
        this.write(b, 0, b.length);
    }

    public void write(byte b[], int off, int len) throws IOException {
        if (tripped) {
            tripStream.write(b, off, len);
        } else if ((pos + len) < buffer.length) {
            System.arraycopy(b, off, buffer, pos, len);
            pos += len;
        } else {
            // write buffer too big - switch to tripStream
            tripped = true;
            // write all we have buffered
            tripStream.write(buffer, 0, pos);
            // write this request
            tripStream.write(b, off, len);
            buffer = null;
        }
    }

    /**
     * Flush will not be effective unless the threshold is reached as the final stream to use
     * has not been determined.
     *
     * @throws IOException
     */
    public void flush() throws IOException {
        if (tripped) {
            tripStream.flush();
        }
    }

    public void close() throws IOException {
        if (closed) {
            return;
        }
        closed = true;

        if (tripped) {
            tripStream.close();
        } else {
            smallStream.write(buffer, 0, pos);
            smallStream.close();
        }
    }

    /**
     * Returns true if the stream has reached the threshold.  Useful for testing which stream was used
     * after writing is complete.
     */
    public boolean hasTripped() {
        return (tripped);
    }
}
