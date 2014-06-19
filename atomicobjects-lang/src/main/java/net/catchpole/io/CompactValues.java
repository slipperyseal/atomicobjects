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

import java.io.EOFException;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * Compact values are variable length numbers for writing to streams.  They are most efficient when the
 * values are small.  The basis of a compact value is that the most significant bit of each byte is set if
 * the value continues in the next byte.  Apart from compacting smaller values in the stream, it allows the
 * size of the value to be open ended.  Future readers and writers may support longer, larger numbers without
 * change to previous stream specification.
 */
public class CompactValues {
    // todo: compact values do not yet support negative values

    public static void writeCompactInteger(OutputStream os, int value) throws IOException {
        do {
            int b = value & 0x7f;
            value = (value & 0x7fffffff) >> 7;
            os.write(value == 0 ? b : (b | 0x80));
        } while (value != 0);
    }

    public static int readCompactInteger(InputStream is) throws IOException {
        int value = 0;
        for (int shift = 0; shift < 32; shift += 7) {
            int b = is.read();
            if (b == -1) {
                throw new EOFException();
            }
            value |= ((b & 0x7f) << shift);
            if ((b & 0x80) == 0) {
                return value;
            }
        }
        throw new IOException("Compact value exceeds 32 bits");
    }

    public static void writeCompactLong(OutputStream os, long value) throws IOException {
        do {
            int b = (int) value & 0x7f;
            value = (value & 0x7fffffffffffffffl) >> 7;
            os.write(value == 0 ? b : (b | 0x80));
        } while (value != 0);
    }

    public static long readCompactLong(InputStream is) throws IOException {
        long value = 0;
        for (int shift = 0; shift < 64; shift += 7) {
            int b = is.read();
            if (b == -1) {
                throw new EOFException();
            }
            value |= ((long) (b & 0x7f) << shift);
            if ((b & 0x80) == 0) {
                return value;
            }
        }
        throw new IOException("Compact value exceeds 32 bits");
    }
}
