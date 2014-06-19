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

import net.catchpole.lang.Disposable;

import java.io.*;

public class MemoryInputOutputStream implements InputOutputStream, Disposable, Externalizable {
    private ByteArrayOutputStream baos;
    private byte[] bytes;
    private Integer initialBuffer;

    public MemoryInputOutputStream() {
    }

    public MemoryInputOutputStream(int initialBuffer) {
        this.initialBuffer = initialBuffer;
    }

    public InputStream getInputStream() throws IOException {
        if (bytes == null) {
            bytes = baos != null ? baos.toByteArray() : new byte[0];
        }
        return new ByteArrayInputStream(bytes);
    }

    public OutputStream getOutputStream() throws IOException {
        bytes = null;
        baos = (initialBuffer == null ? new ByteArrayOutputStream() : new ByteArrayOutputStream(initialBuffer));
        return baos;
    }

    public byte[] getBytes() {
        return baos.toByteArray();
    }

    public void dispose() {
        baos = null;
        bytes = null;
    }

    public String toString() {
        return this.getClass().getSimpleName() + ' ' + (bytes != null ? bytes.length : 0);
    }

    public void writeExternal(ObjectOutput out) throws IOException {
        if (baos != null) {
            byte[] data = baos.toByteArray();
            out.writeInt(data.length);
            out.write(data);
        } else if (bytes != null) {
            out.writeInt(bytes.length);
            out.write(bytes);
        }
        out.writeInt(0);
    }

    public void readExternal(ObjectInput in) throws IOException, ClassNotFoundException {
        bytes = new byte[in.readInt()];
        in.readFully(bytes);
    }
}
