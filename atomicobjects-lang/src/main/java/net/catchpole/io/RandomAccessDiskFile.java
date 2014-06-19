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

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;

public class RandomAccessDiskFile implements RandomAccessStream {
    private final RandomAccessFile randomAccessFile;

    public RandomAccessDiskFile(File file, boolean write) throws IOException {
        randomAccessFile = new RandomAccessFile(file, write ? "rw" : "r");
    }

    public long length() throws IOException {
        return randomAccessFile.length();
    }

    public int read() throws IOException {
        return randomAccessFile.read();
    }

    public int read(byte[] b, int off, int len) throws IOException {
        return randomAccessFile.read(b, off, len);
    }

    public void write(byte[] b) throws IOException {
        randomAccessFile.write(b);
    }

    public void write(byte[] b, int off, int len) throws IOException {
        randomAccessFile.write(b, off, len);
    }

    public void seek(long location) throws IOException {
        randomAccessFile.seek(location);
    }

    public long currentPosition() throws IOException {
        return randomAccessFile.getFilePointer();
    }

    public void close() throws IOException {
        randomAccessFile.close();
    }

    public void flush() throws IOException {

    }

    public void setLength(long length) throws IOException {
        randomAccessFile.setLength(length);
    }

    public String toString() {
        return this.getClass().getSimpleName() + ' ' + randomAccessFile;
    }
}
