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

import net.catchpole.lang.Throw;

import java.io.EOFException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Iterator;

/**
 */
public class StreamTokenIterator implements Iterator<String> {
    private final StringBuilder sb = new StringBuilder(128);
    private InputStream is;
    private String next;

    public StreamTokenIterator(InputStream is) {
        this.is = is;
        this.next = readNext();
    }

    public boolean hasNext() {
        return next != null;
    }

    public void remove() {
    }

    public String next() {
        String string = next;
        next = readNext();
        return string;
    }

    private String readNext() {
        sb.setLength(0);
        try {
            while (is != null) {
                int c = is.read();
                if (c == -1) {
                    is = null;
                    return (sb.length() > 0 ? sb.toString() : null);
                }
                if (c <= ' ') {
                    if (sb.length() == 0) {
                        continue;
                    }
                    return sb.toString();
                } else {
                    sb.append((char) c);
                }
            }
        } catch (EOFException eof) {
            is = null;
            if (sb.length() != 0) {
                return sb.toString();
            }
        } catch (IOException ioe) {
            is = null;
            throw Throw.unchecked(ioe);
        }
        return null;
    }
}
