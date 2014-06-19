package net.catchpole.bytecode;

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

import net.catchpole.bytecode.element.Attribute;
import net.catchpole.bytecode.element.ClassFlags;
import net.catchpole.bytecode.element.Constant;
import net.catchpole.bytecode.element.Field;
import net.catchpole.bytecode.element.Method;
import net.catchpole.lang.Strings;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

public class ClassFile {
    private static short defaultMinorVersion;
    private static short defaultMajorVersion;

    static {
        // copy minor and major version from this class
        try {
            DataInputStream dis = new DataInputStream(ClassFile.class.getResourceAsStream(ClassFile.class.getSimpleName() + ".class"));
            if (dis.readInt() != 0xCAFEBABE) {
                throw new IOException("Not a class file");
            }
            defaultMinorVersion = dis.readShort();
            defaultMajorVersion = dis.readShort();
            dis.close();
        } catch (IOException ioe) {
            defaultMinorVersion = 0;
            defaultMajorVersion = 48;
        }
    }

    private String className;
    private final short minorVersion;
    private final short majorVersion;
    private final ClassFlags flags;
    private final Constant thisClass;
    private final Constant superClass;
    private final List<Constant> constants = new ArrayList<Constant>();
    private final List<Constant> interfaces = new ArrayList<Constant>();
    private final List<Field> fields = new ArrayList<Field>();
    private final List<Method> methods = new ArrayList<Method>();
    private final List<Attribute> attributes = new ArrayList<Attribute>();

    public ClassFile(String className) {
        this(className, "java.lang.Object");
    }

    public ClassFile(String className, String superClass) {
        this.className = className;

        this.minorVersion = defaultMinorVersion;
        this.majorVersion = defaultMajorVersion;

        this.constants.add(null);
        this.flags = new ClassFlags();
        this.flags.setPublic(true);

        this.superClass = new Constant(superClass.replace('.', '/'), constants);
        this.constants.add(this.superClass);
        this.thisClass = new Constant(className.replace('.', '/'), constants);
        this.constants.add(this.thisClass);
    }

    /**
     * Uses the class's resource loader to access its own byte code.
     * Will not work for classes which are themselves dynamic.
     *
     * @param clazz
     * @throws IOException
     */
    public ClassFile(Class clazz) throws IOException {
        this(clazz.getResourceAsStream(clazz.getSimpleName() + ".class"));
    }

    public ClassFile(byte[] classBytes) throws IOException {
        this(new ByteArrayInputStream(classBytes));
    }

    public ClassFile(InputStream in) throws IOException {
        DataInputStream dis = new DataInputStream(in);

        if (dis.readInt() != 0xCAFEBABE) {
            throw new IOException("Not a class file");
        }

        this.minorVersion = dis.readShort();
        this.majorVersion = dis.readShort();

        int totalConstants = dis.readShort() & 0xffff;
        constants.add(null);
        for (int x = 1; x < totalConstants; x++) {
            Constant constant = new Constant(dis, this.constants);
            constants.add(constant);
            // skip second entry
            if ((constant.getType() == Constant.TYPE_DOUBLE) || (constant.getType() == Constant.TYPE_LONG)) {
                constants.add(null);
            }
        }

        // set cross-references in the constant table
        for (int x = 1; x < totalConstants; x++) {
            Constant constant = constants.get(x);
            if (constant != null) {
                if (constant.getConstantIndexA() > 0) {
                    constant.setConstantA(constants.get(constant.getConstantIndexA()));
                }
                if (constant.getConstantIndexB() > 0) {
                    constant.setConstantB(constants.get(constant.getConstantIndexB()));
                }
            }
        }

        this.flags = new ClassFlags(dis.readShort());
        this.thisClass = constants.get(dis.readShort() & 0xffff);
        this.superClass = constants.get(dis.readShort() & 0xffff);

        for (int total = dis.readShort() & 0xffff; total-- > 0;) {
            this.interfaces.add(this.constants.get(dis.readShort()));
        }

        for (int total = dis.readShort() & 0xffff; total-- > 0;) {
            this.fields.add(new Field(dis, this.constants));
        }

        for (int total = dis.readShort() & 0xffff; total-- > 0;) {
            this.methods.add(new Method(dis, this.constants));
        }

        for (int total = dis.readShort() & 0xffff; total-- > 0;) {
            this.attributes.add(new Attribute(dis, this.constants));
        }

        this.className = this.thisClass.getConstantA().getValue().toString().replace('/', '.');
    }

    public void write(OutputStream out) throws IOException {
        DataOutputStream dos = new DataOutputStream(out);

        dos.writeInt(0xCAFEBABE);
        dos.writeShort(this.minorVersion);
        dos.writeShort(this.majorVersion);
        dos.writeShort(this.constants.size());
        for (Constant constant : this.constants) {
            // can be null because of extra entries for long and double.
            if (constant != null) {
                constant.write(dos);
            }
        }
        dos.writeShort(this.flags.getFlags());
        dos.writeShort(this.constants.indexOf(this.thisClass));
        dos.writeShort(this.constants.indexOf(this.superClass));

        dos.writeShort(this.interfaces.size());
        for (Constant constant : interfaces) {
            dos.writeShort(this.constants.indexOf(constant));
        }

        dos.writeShort(this.fields.size());
        for (Field field : this.fields) {
            field.write(dos);
        }

        dos.writeShort(this.methods.size());
        for (Method method : methods) {
            method.write(dos);
        }

        dos.writeShort(attributes.size());
        for (Attribute attribute : attributes) {
            attribute.write(dos);
        }
    }

    public byte[] build() throws Exception {
        ByteArrayOutputStream baos = new ByteArrayOutputStream(4096);
        this.write(baos);
        return baos.toByteArray();
    }

    public void setClassName(String name) {
        this.className = name.replace('/', '.');
        this.thisClass.getConstantA().setValue(name.replace('.', '/'));
    }

    public String getClassName() {
        return this.className;
    }

    public Constant addConstant(Object value) {
        for (Constant constant : constants) {
            if (constant != null) {
                Object constantValue = constant.getValue();
                if (constantValue != null && constantValue.equals(value)) {
                    return constant;
                }
            }
        }
        Constant constant = new Constant(value, constants);
        constants.add(constant);
        return constant;
    }

    public Attribute addAttribute(Constant name, byte[] data) {
        Attribute attribute = new Attribute(name, data, constants);
        attributes.add(attribute);
        return attribute;
    }

    public String toString() {
        return this.getClass().getSimpleName() + ' ' + this.className;
    }

    public String toDetailedString() {
        return this.toString() + ' ' + this.majorVersion + ':' + this.minorVersion + ' ' + '\r' + '\n' +
                Strings.toTabbedList(this.constants) +
                '\t' + flags + '\r' + '\n' +
                Strings.toTabbedList(this.interfaces) +
                Strings.toTabbedList(this.fields) +
                Strings.toTabbedList(this.methods) +
                Strings.toTabbedList(this.attributes);
    }

    public static String getTypeSignature(Class clazz) {
        return "()L" + clazz.getName().replace('.', '/') + ';';
    }
}
