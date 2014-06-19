package net.catchpole.scene.gl;

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

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.IntBuffer;
import java.nio.ShortBuffer;

public class GLTools {
    public GLTools() {
    }

    public IntBuffer allocInts(int total) {
        return ByteBuffer.allocateDirect(total * 4).order(ByteOrder.nativeOrder()).asIntBuffer();
    }

    public ShortBuffer allocShorts(int total) {
        return ByteBuffer.allocateDirect(total * 2).order(ByteOrder.nativeOrder()).asShortBuffer();
    }

    public ByteBuffer allocBytes(int total) {
        return ByteBuffer.allocateDirect(total);
    }

    public ByteBuffer allocBytes(byte[] bytes) {
        ByteBuffer byteBuffer = allocBytes(bytes.length);
        byteBuffer.put(bytes);
        byteBuffer.rewind();
        return byteBuffer;
    }

    public ShortBuffer allocShorts(short[] shorts) {
        ShortBuffer shortBuffer = allocShorts(shorts.length);
        shortBuffer.put(shorts);
        shortBuffer.rewind();
        return shortBuffer;
    }

    public IntBuffer allocInts(int[] ints) {
        IntBuffer intBuffer = allocInts(ints.length);
        intBuffer.put(ints);
        intBuffer.rewind();
        return intBuffer;
    }
}
