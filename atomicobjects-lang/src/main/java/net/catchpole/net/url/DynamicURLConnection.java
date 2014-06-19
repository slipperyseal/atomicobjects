package net.catchpole.net.url;

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
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;

public class DynamicURLConnection extends URLConnection {
    private final File file;
    private InputStream inputStream;

    public DynamicURLConnection(URL url, File source) {
        super(url);
        this.file = new File(source, url.getPath());
    }

    public void connect() throws IOException {
        if (this.connected) {
            throw new IOException("Already connected");
        }
        this.inputStream = new FileInputStream(file);
        this.connected = true;
    }

    public InputStream getInputStream() throws IOException {
        if (!this.connected) {
            connect();
        }
        return this.inputStream;
    }

    public int getContentLength() {
        return (int) file.length();
    }
}
