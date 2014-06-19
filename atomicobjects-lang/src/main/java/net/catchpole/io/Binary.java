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

import java.io.ByteArrayOutputStream;
import java.io.Externalizable;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInput;
import java.io.ObjectOutput;
import java.io.OutputStream;

public class Binary implements Externalizable {
    private static final long serialVersionUID = 42L;
    private static final int CHUNKSIZE = 1024 * 512;

    private String name;
    private File diskFile;
    private long length;
    private int chunkSize = CHUNKSIZE;

    private byte[] data;

    public Binary() {
    }

    public Binary(String name) {
        this.name = name;
    }

    public Binary(File diskFile) {
        this.diskFile = diskFile;
        this.name = diskFile.getName();
    }

    public void load(File copyFile, boolean useName) throws IOException {
        if (useName) {
            this.name = copyFile.getName();
        }

        load(new FileInputStream(copyFile));
    }

    public void load(InputStream is) throws IOException {
        try {
            ByteArrayOutputStream os = new ByteArrayOutputStream(1024);//this.getOutputStream();
            try {
                Arrays.spool(is, os);
            } finally {
                os.close();
            }
            this.data = os.toByteArray();
        } finally {
            is.close();
        }
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public long length() {
        return (this.diskFile != null ? this.diskFile.length() : this.length);
    }

    public InputStream getInputStream() throws IOException {
        return (this.diskFile != null ?
                (InputStream) new FileInputStream(this.diskFile) :
                (InputStream) new ChunkyInputStream());
    }

    public OutputStream getOutputStream() throws IOException {
        return (this.diskFile != null ?
                (OutputStream) new FileOutputStream(this.diskFile) :
                (OutputStream) new ChunkingOutputStream(new ChunkyOutputStream(), chunkSize));
    }

    public String toString() {
        return name;
    }

    class ChunkyOutputStream extends OutputStream {
        public void write(byte[] b, int off, int len) {
            if (len == 0) {
                return;
            }

            byte[] bytes = new byte[len - off];
            System.arraycopy(b, off, bytes, 0, len);
            length += len;
        }

        public void write(int b) throws IOException {
            // must be filtered through chunked writes
            throw new IOException();
        }
    }

    class ChunkyInputStream extends ChunkingInputStream {
        private int blockNumber;

        public byte[] getNextChunk() throws IOException {
            if (blockNumber == 0) {
                blockNumber++;
                return data;
            } else {
                return null;
            }
        }
    }

    public void writeExternal(ObjectOutput out) throws IOException {
        //super.writeExternal(out);
        out.writeUTF(this.name);
        out.writeObject(this.diskFile);
        out.writeLong(this.length);
        out.writeInt(this.chunkSize);
    }

    public void readExternal(ObjectInput in) throws IOException, ClassNotFoundException {
        //super.readExternal(in);
        this.name = in.readUTF();
        this.diskFile = (File) in.readObject();
        this.length = in.readLong();
        this.chunkSize = in.readInt();
    }
}
