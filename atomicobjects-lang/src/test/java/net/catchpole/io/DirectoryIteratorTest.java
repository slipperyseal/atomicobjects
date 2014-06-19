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

import net.catchpole.trace.Core;

import java.io.File;
import java.io.IOException;
import java.util.Iterator;

public class DirectoryIteratorTest {
    //@Test
    public void testDirectoryIterator() throws IOException {
        print(new DirectoryIterator(new File("./src/main/java/net/catchpole/net")));
    }

    public void print(Iterator<File> di) {
        Core.getTrace().info(di);
        while (di.hasNext()) {
            Core.getTrace().info(di.next());
        }
    }
}
