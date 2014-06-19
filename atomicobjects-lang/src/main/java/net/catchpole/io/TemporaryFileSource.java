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

import java.io.File;
import java.io.IOException;

public class TemporaryFileSource implements InputOutputStreamSource {
    private static long id = System.currentTimeMillis();
    private File tempDir;

    public TemporaryFileSource() {
        this(new File(System.getProperty("java.io.tmpdir")));
    }

    public TemporaryFileSource(File tempDir) {
        this.tempDir = tempDir;
    }

    public InputOutputStream getInputOutputStream() throws IOException {
        return new FileInputOutputStream(getNextFile());
    }

    private File getNextFile() {
        File file = new File(tempDir, this.getClass().getSimpleName() + '-' + Long.toHexString(getNextId()));
        file.deleteOnExit();
        return file;
    }

    private static synchronized long getNextId() {
        return id++;
    }
}
