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
import java.util.ArrayList;
import java.util.List;

public class Field {
    private final List<Constant> constants;
    private final short accessFlags;
    private final Constant name;
    private final Constant signature;
    private final List<Attribute> attributes = new ArrayList<Attribute>();

    public Field(DataInputStream dis, List<Constant> constants) throws IOException {
        this.constants = constants;

        this.accessFlags = dis.readShort();
        this.name = constants.get(dis.readShort() & 0xffff);
        this.signature = constants.get(dis.readShort() & 0xffff);

        for (int total = dis.readShort() & 0xffff; total-- > 0;) {
            this.attributes.add(new Attribute(dis, constants));
        }
    }

    public void write(DataOutputStream dos) throws IOException {
        dos.writeShort(this.accessFlags);
        dos.writeShort(this.constants.indexOf(this.name));
        dos.writeShort(this.constants.indexOf(this.signature));

        dos.writeShort(this.attributes.size());
        for (Attribute attribute : this.attributes) {
            attribute.write(dos);
        }
    }

    public String toString() {
        return this.getClass().getSimpleName() + ' ' + name + ' ' + signature + ' ' + attributes;
    }
}
