package net.catchpole.classpath;

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

import org.junit.Test;

import java.io.File;
import java.io.IOException;

public class ZipImportTest {
    @Test
    public void testZipImport() throws IOException {
//        Map<String, byte[]> map = new LinkedHashMap<String, byte[]>();
//        new ZipImport(new FileInputStream(findAZip()), map);
//        Core.getTrace().info(map);
    }

    private File findAZip() throws IOException {
        // test with a small file
        File servlet = new File("./lib/servlet-api-2.4.jar");
        if (servlet.exists()) {
            return servlet;
        }

        File dir = new File("./lib/");
        for (File file : dir.listFiles()) {
            if (file.getName().endsWith(".jar")) {
                return file;
            }
        }
        throw new IOException("Can't find a JAR to test with.");
    }
}
