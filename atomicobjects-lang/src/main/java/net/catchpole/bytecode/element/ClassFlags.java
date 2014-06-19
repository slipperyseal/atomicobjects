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

import net.catchpole.lang.Maths;

public class ClassFlags {
    private static final short ACC_PUBLIC = 0x0001; // Declared public; may be accessed from outside its package.
    private static final short ACC_FINAL = 0x0010; // Declared final; no subclasses allowed.
    private static final short ACC_SUPER = 0x0020; // Treat superclass methods specially when invoked by the invokespecial instruction.
    private static final short ACC_INTERFACE = 0x0200; // Is an interface, not a class.
    private static final short ACC_ABSTRACT = 0x0400; // Declared abstract; may not be instantiated.

    private short flags;

    public ClassFlags() {
    }

    public ClassFlags(short flags) {
        this.flags = flags;
    }

    public short getFlags() {
        return flags;
    }

    public void setPublic(boolean value) {
        flags = (short) Maths.changeBit(value, flags, ACC_PUBLIC);
    }

    public boolean isPublic() {
        return (flags & ACC_PUBLIC) != 0;
    }

    public void setFinal(boolean value) {
        flags = (short) Maths.changeBit(value, flags, ACC_FINAL);
    }

    public boolean isFinal() {
        return (flags & ACC_FINAL) != 0;
    }

    public void setSuper(boolean value) {
        flags = (short) Maths.changeBit(value, flags, ACC_SUPER);
    }

    public boolean isSuper() {
        return (flags & ACC_SUPER) != 0;
    }

    public void setInterface(boolean value) {
        flags = (short) Maths.changeBit(value, flags, ACC_INTERFACE);
    }

    public boolean isInterface() {
        return (flags & ACC_INTERFACE) != 0;
    }

    public void setAbstract(boolean value) {
        flags = (short) Maths.changeBit(value, flags, ACC_ABSTRACT);
    }

    public boolean isAbstract() {
        return (flags & ACC_ABSTRACT) != 0;
    }

    public String toString() {
        return this.getClass().getSimpleName() +
                (isPublic() ? " public" : "") +
                (isFinal() ? " final" : "") +
                (isSuper() ? " super" : "") +
                (isInterface() ? " interface" : "") +
                (isAbstract() ? " abstract" : "");
    }
}
