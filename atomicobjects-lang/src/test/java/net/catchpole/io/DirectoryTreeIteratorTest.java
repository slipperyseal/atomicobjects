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

import java.io.File;
import java.io.IOException;
import java.util.Iterator;

public class DirectoryTreeIteratorTest {
    //@Test
    public void testDirectoryIterator() throws IOException {
        int total = 0;

        Core.getTrace().info("Home is: " + new File(".").getAbsolutePath());
        Iterator<File> i = new DirectoryTreeIterator(new File("./src/main/java/net/catchpole/net"));

        while (i.hasNext()) {
            File file = i.next();
            Core.getTrace().info(file);
            total++;
        }
        TestCase.assertTrue(total > 2);
        Core.getTrace().info("total files (excluding dirs): " + total);
    }
}
