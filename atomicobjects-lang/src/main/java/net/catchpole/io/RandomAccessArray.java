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

public class RandomAccessArray implements RandomAccessStream {
    private byte[] bytes;
    private int length;
    private int pos;

    public RandomAccessArray(byte[] bytes) {
        this.bytes = bytes;
        this.length = bytes.length;
    }

    public long length() {
        return length;
    }

    public int read() throws IOException {
        if (pos >= length) {
            return -1;
        }
        return bytes[pos]++;
    }

    public int read(byte[] b, int off, int len) throws IOException {
        if (pos >= length) {
            return -1;
        }
        int remain = length - pos;
        if (len > remain) {
            len = remain;
        }
        System.arraycopy(bytes, pos, b, off, len);
        pos += len;
        return len;
    }

    public void write(byte[] b) throws IOException {
        if (pos >= length) {
            throw new IOException("Array expansion not supported");
        }
    }

    public void write(byte[] b, int off, int len) throws IOException {
        if (pos >= length) {
            throw new IOException("Array expansion not supported");
        }
        System.arraycopy(b, off, bytes, pos, len);
        pos += len;
    }

    public void seek(long location) throws IOException {
        if (location > length) {
            throw new IOException("Seek past EOF");
        }
        pos = (int) location;
    }

    public long currentPosition() throws IOException {
        return pos;
    }

    public void close() throws IOException {

    }

    public void flush() throws IOException {

    }

    public void setLength(long length) throws IOException {
        if (length > Integer.MAX_VALUE || this.length < length) {
            throw new IOException("Array expansion not supported");
        }
        this.length = (int) length;
    }

    public String toString() {
        return this.getClass().getSimpleName() + ' ' + length + " bytes";
    }
}
