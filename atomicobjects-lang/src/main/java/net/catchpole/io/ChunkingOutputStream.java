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
 * A buffered OutputStream which only writes to the underlying OutputStream in chunks of the
 * given size, or on closing, a possibly short chunk size.
 * <p/>
 * To prevent chunks being written short before the end of the stream, the <code>flush()</code>
 * method has no effect.
 * <p/>
 * If the object is construted with <code>flushOnChunk</code> as true, each chunk write to the
 * underlying stream will be followed by a flush.  The default is false.
 */
public class ChunkingOutputStream extends OutputStream {
    private OutputStream os;
    private byte[] bytes;
    private int position;
    private boolean flushOnChunk;

    public ChunkingOutputStream(OutputStream os, int chunkSize) {
        this(os, chunkSize, false);
    }

    public ChunkingOutputStream(OutputStream os, int chunkSize, boolean flushOnChunk) {
        this.os = os;
        this.bytes = new byte[chunkSize];
        this.flushOnChunk = flushOnChunk;
    }

    public void write(byte b[], int off, int len) throws IOException {
        while (len > 0) {
            // how many bytes left in buffer?
            int remain = bytes.length - position;
            // will we write the remainder or the write len if its shorter?
            int writeLen = (len > remain ? remain : len);

            // copy bytes into buffer
            System.arraycopy(b, off, bytes, position, writeLen);
            position += writeLen;
            len -= writeLen;
            off += writeLen;

            // flush the buffer if full
            if (position == this.bytes.length) {
                flushBuffer();
            }
        }
    }

    public void write(int b) throws IOException {
        bytes[position++] = (byte) b;
        // flush the buffer if full
        if (position == this.bytes.length) {
            flushBuffer();
        }
    }

    public void flush() throws IOException {
    }

    public void close() throws IOException {
        flushBuffer();
        this.os.close();
    }

    private void flushBuffer() throws IOException {
        if (position != 0) {
            os.write(bytes, 0, position);
            if (flushOnChunk) {
                os.flush();
            }
            position = 0;
        }
    }
}
