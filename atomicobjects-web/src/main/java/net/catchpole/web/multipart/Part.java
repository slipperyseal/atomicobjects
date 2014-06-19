package net.catchpole.web.multipart;

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

import net.catchpole.io.Arrays;
import net.catchpole.io.InputOutputStream;
import net.catchpole.io.MemoryInputOutputStream;
import net.catchpole.lang.Disposable;

import java.io.IOException;
import java.io.InputStream;

public class Part implements Disposable {
    private final String name;
    private final String filename;
    private final InputOutputStream stream;

    public Part(String name, String filename, InputOutputStream stream) {
        this.name = name;
        this.filename = filename;
        this.stream = stream;
    }

    public String getName() {
        return name;
    }

    public String getFileName() {
        return filename;
    }

    public InputStream getInputStream() throws IOException {
        return stream.getInputStream();
    }

    public byte[] getBytes() throws IOException {
        if (stream instanceof MemoryInputOutputStream) {
            MemoryInputOutputStream mios = (MemoryInputOutputStream)stream;
            return ((MemoryInputOutputStream) stream).getBytes();
        } else {
            return Arrays.streamToByteArray(stream.getInputStream());
        }
    }

    public void dispose() {
        stream.dispose();
    }
}
