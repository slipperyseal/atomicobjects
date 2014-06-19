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

import java.io.FilterInputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * A filter input stream which counts the number of bytes read.
 */
public class CountingInputStream extends FilterInputStream {
    private long total = 0;

    public CountingInputStream(InputStream in) {
        super(in);
    }

    public int read() throws IOException {
        int x = super.read();    //To change body of overridden methods use File | Settings | File Templates.
        if (x != -1) {
            total++;
        }
        return x;
    }

    public int read(byte b[]) throws IOException {
        int x = super.read(b);
        if (x != -1) {
            total += x;
        }
        return x;
    }

    public int read(byte b[], int off, int len) throws IOException {
        int x = super.read(b, off, len);    //To change body of overridden methods use File | Settings | File Templates.
        if (x != -1) {
            total += x;
        }
        return x;
    }

    public long skip(long n) throws IOException {
        long x = super.skip(n);
        if (x != -1) {
            total += x;
        }
        return x;
    }

    public long getTotalBytesRead() {
        return total;
    }

    public void resetTotalBytesRead() {
        total = 0;
    }
}
