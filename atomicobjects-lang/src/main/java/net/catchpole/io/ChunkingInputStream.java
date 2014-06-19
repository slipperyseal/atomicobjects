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

/**
 * Creates an abstract InputStream that must implement <code>getNextChunk()</code> to source
 * byte array chunks.  The InputStream will read data from these chunks until
 * <code>getNextChunk()</code> return null or throws an IOException.
 */

public abstract class ChunkingInputStream extends InputStream {
    private byte[] bytes;
    private int position = 0;

    public int read(byte b[], int off, int len) throws IOException {
        // make sure bytes and position are ready for the read
        if (!resolveBytes()) {
            return -1;
        }

        // calculate the length we can read
        int remain = this.bytes.length - this.position;
        if (remain < len) {
            len = remain;
        }

        System.arraycopy(this.bytes, this.position, b, off, len);
        this.position += len;

        if (this.position >= this.bytes.length) {
            this.bytes = null;
        }

        return len;
    }

    public int read() throws IOException {
        if (!resolveBytes()) {
            return -1;
        }

        int b = this.bytes[position++];

        if (position >= this.bytes.length) {
            this.bytes = null;
        }
        return b;

    }

    /**
     * Returns true if data is available to the stream, calling the abstract
     * getNextChunk() method if needed.
     *
     * @return
     * @throws IOException
     */
    private boolean resolveBytes() throws IOException {
        if (this.bytes == null) {
            if ((this.bytes = getNextChunk()) == null) {
                return false;
            }
            this.position = 0;
        }
        return true;
    }

    abstract public byte[] getNextChunk() throws IOException;
}
