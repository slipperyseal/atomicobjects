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

/**
 */

public class Constant {
    private static final String[] typeNames = {
            "NOTYPE", "ASCII", "UNICODE", "INTEGER", "FLOAT", "LONG", "DOUBLE", "CLASS",
            "STRING", "FIELDREF", "METHODREF", "INTERFACE", "NAMEANDTYPE"
    };
    private static final Class[] typeMapping = {
            String.class, String.class, Integer.class, Float.class, Long.class, Double.class, Class.class
    };
    public static final int TYPE_ASCII = 1;
    public static final int TYPE_UNICODE = 2;
    public static final int TYPE_INTEGER = 3;
    public static final int TYPE_FLOAT = 4;
    public static final int TYPE_LONG = 5;
    public static final int TYPE_DOUBLE = 6;
    public static final int TYPE_CLASS = 7;
    public static final int TYPE_STRING = 8;
    public static final int TYPE_FIELDREF = 9;
    public static final int TYPE_METHODREF = 10;
    public static final int TYPE_INTERFACE = 11;
    public static final int TYPE_NAMEANDTYPE = 12;

    private final List<Constant> peerConstants;
    private int type;
    private int constantIndexA = -1;
    private int constantIndexB = -1;
    private Constant constantA;
    private Constant constantB;
    private Object value;

    public Constant(Object value, List<Constant> peerConstants) {
        this.peerConstants = peerConstants;
        int x = 1;
        for (Class clazz : typeMapping) {
            if (value.getClass().isAssignableFrom(clazz)) {
                this.type = x;
                break;
            }
            x++;
        }
        this.value = value;
    }

    public Constant(DataInputStream dis, List<Constant> peerConstants) throws IOException {
        this.peerConstants = peerConstants;

        type = dis.readByte();
        switch (type) {
            case TYPE_CLASS:
            case TYPE_STRING:
                constantIndexA = dis.readShort() & 0xffff;
                break;
            case TYPE_FIELDREF:
            case TYPE_METHODREF:
            case TYPE_INTERFACE:
            case TYPE_NAMEANDTYPE:
                constantIndexA = dis.readShort() & 0xffff;
                constantIndexB = dis.readShort() & 0xffff;
                break;
            case TYPE_INTEGER:
                this.value = dis.readInt();
                break;
            case TYPE_FLOAT:
                this.value = dis.readFloat();
                break;
            case TYPE_LONG:
                this.value = dis.readLong();
                break;
            case TYPE_DOUBLE:
                this.value = dis.readDouble();
                break;
            case TYPE_ASCII:
            case TYPE_UNICODE:
                byte[] bytes = new byte[dis.readShort() & 0xffff];
                dis.readFully(bytes);
                this.value = new String(bytes);
                break;
            default:
                throw new IllegalArgumentException("Unknown type: " + type);
        }
    }

    public void write(DataOutputStream dos) throws IOException {
        dos.write(type);
        switch (type) {
            case TYPE_CLASS:
            case TYPE_STRING:
                dos.writeShort(peerConstants.indexOf(constantA));
                break;
            case TYPE_FIELDREF:
            case TYPE_METHODREF:
            case TYPE_INTERFACE:
            case TYPE_NAMEANDTYPE:
                dos.writeShort(peerConstants.indexOf(constantA));
                dos.writeShort(peerConstants.indexOf(constantB));
                break;
            case TYPE_INTEGER:
                dos.writeInt(((Integer) this.value));
                break;
            case TYPE_FLOAT:
                dos.writeFloat(((Float) this.value));
                break;
            case TYPE_LONG:
                dos.writeLong(((Long) this.value));
                break;
            case TYPE_DOUBLE:
                dos.writeDouble(((Double) this.value));
                break;
            case TYPE_ASCII:
            case TYPE_UNICODE:
                dos.writeShort(value.toString().length());
                dos.writeBytes(value.toString());
                break;
            default:
                throw new IllegalArgumentException("Unknown type: " + type);
        }
    }

    public int getType() {
        return type;
    }

    public Constant getConstantA() {
        return constantA;
    }

    public void setConstantA(Constant constantA) {
        this.constantA = constantA;
    }

    public Constant getConstantB() {
        return constantB;
    }

    public void setConstantB(Constant constantB) {
        this.constantB = constantB;
    }

    public int getConstantIndexA() {
        return constantIndexA;
    }

    public void setConstantIndexA(short constantIndexA) {
        this.constantIndexA = constantIndexA;
    }

    public int getConstantIndexB() {
        return constantIndexB;
    }

    public void setConstantIndexB(short constantIndexB) {
        this.constantIndexB = constantIndexB;
    }

    public Object getValue() {
        return value;
    }

    public void setValue(Object value) {
        this.value = value;
    }

    public String toString() {
        return this.getClass().getSimpleName() + ' ' +
                peerConstants.indexOf(this) + ' ' +
                typeNames[this.type] + ' ' +
                this.constantIndexA + ':' + this.constantIndexB + ' ' +
                (this.value == null ? null :
                        this.value.getClass().getSimpleName() + ' ' +
                                (this.value instanceof String ? "\"" + this.value + '\"' : this.value));
    }
}
