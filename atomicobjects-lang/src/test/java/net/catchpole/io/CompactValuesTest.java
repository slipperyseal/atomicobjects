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

import junit.framework.TestCase;
import net.catchpole.trace.Core;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;

public class CompactValuesTest {
    @Test
    public void testCompactInteger() throws Exception {
        compactInteger(0x0);
        compactInteger(0x1);
        compactInteger(0xff00);
        compactInteger(0x1afebabe);

        compactLong(0x0);
        compactLong(0x1);
        compactLong(0xff00);
        compactLong(0x1afebabe);
        compactLong(0x1afebabe1afebabel);

        //todo: doesn't work with negative values
    }

    private void compactInteger(int value) throws Exception {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        CompactValues.writeCompactInteger(baos, value);

        Core.getTrace().info(Integer.toHexString(value));
        byte[] bytes = baos.toByteArray();
        for (byte b : bytes) {
            Core.getTrace().info(Integer.toString(b & 0xff, 2));
        }


        ByteArrayInputStream bais = new ByteArrayInputStream(bytes);
        TestCase.assertEquals(value, CompactValues.readCompactInteger(bais));
    }

    private void compactLong(long value) throws Exception {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        CompactValues.writeCompactLong(baos, value);

        Core.getTrace().info(Long.toHexString(value));
        byte[] bytes = baos.toByteArray();
        for (byte b : bytes) {
            Core.getTrace().info(Integer.toString(b & 0xff, 2));
        }


        ByteArrayInputStream bais = new ByteArrayInputStream(bytes);
        TestCase.assertEquals(value, CompactValues.readCompactLong(bais));
    }
}
