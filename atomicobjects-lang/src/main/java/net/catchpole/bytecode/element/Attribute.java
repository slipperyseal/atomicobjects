package net.catchpole.bytecode.element;

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

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.List;

public class Attribute {
    private final List<Constant> constants;
    private Constant name;
    private byte data[];

    public Attribute(DataInputStream dis, List<Constant> constants) throws IOException {
        this.constants = constants;

        this.name = constants.get(dis.readShort() & 0xffff);
        this.data = new byte[dis.readInt()];
        dis.readFully(data);
    }

    public Attribute(Constant name, byte[] data, List<Constant> constants) {
        this.name = name;
        this.data = data;
        this.constants = constants;
    }

    public void write(DataOutputStream dos) throws IOException {
        dos.writeShort(this.constants.indexOf(name));
        dos.writeInt(this.data.length);
        dos.write(this.data);
    }

    public Constant getName() {
        return name;
    }

    public void setName(Constant name) {
        this.name = name;
    }

    public byte[] getData() {
        return data;
    }

    public void setData(byte[] data) {
        this.data = data;
    }

    public String toString() {
        return this.getClass().getSimpleName() + ' ' + data.length + " bytes. name = " + name;
    }
}
