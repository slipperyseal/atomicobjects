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

/**
 * An InputStream which may never reach EOF by using an InputStreamSource to source new
 * InputStreams as each one completes.
 */
public class NeverEndingInputStream extends InputStream {
    private final InputStreamSource inputStreamSource;
    private InputStream is;

    public NeverEndingInputStream(InputStreamSource inputStreamSource) throws IOException {
        this.inputStreamSource = inputStreamSource;
        setStream();
    }

    public int read(byte b[], int off, int len) throws IOException {
        for (; ;) {
            try {
                int r = is.read(b, off, len);
                if (r == -1) {
                    setStream();
                    // loop back around and read from next stream
                } else {
                    return r;
                }
            } catch (EOFException eof) {
                setStream();
            }
        }
    }

    public int read(byte b[]) throws IOException {
        return this.read(b, 0, b.length);
    }

    public int read() throws IOException {
        for (; ;) {
            try {
                int v = is.read();
                if (v == -1) {
                    setStream();
                    // loop back around and read from next stream                    
                } else {
                    return v;
                }
            } catch (EOFException eof) {
                setStream();
            }
        }
    }

    private void setStream() throws IOException {
        if (is != null) {
            is.close();
        }
        is = inputStreamSource.getInputStream();
    }
}
