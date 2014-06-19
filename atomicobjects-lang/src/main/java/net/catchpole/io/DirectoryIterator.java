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

import java.io.File;
import java.io.IOException;
import java.util.Iterator;

public class DirectoryIterator implements Iterator<File> {
    private File[] files;
    private File last;
    private int index = 0;

    public DirectoryIterator() {
    }

    public DirectoryIterator(File directory) throws IOException {
        if (!directory.isDirectory()) {
            throw new IOException("Not a directory: " + directory);
        }
        this.files = directory.listFiles();
        java.util.Arrays.sort(this.files);
        if (this.files == null || this.files.length == 0) {
            this.files = null;
        }
    }

    public boolean hasNext() {
        return files != null && index < files.length;
    }

    public File next() {
        if (hasNext()) {
            return last = files[index++];
        }
        throw new IllegalStateException();
    }

    public void remove() {

    }

    public Iterator<File> iterator() {
        if (last == null) {
            throw new IllegalStateException("element not selected");
        }
        try {
            return last.isDirectory() ? new DirectoryIterator(last) : new DirectoryIterator();
        } catch (IOException ioe) {
            throw Throw.unchecked(ioe);
        }
    }

    public void dispose() {
    }

    public String toString() {
        return this.getClass().getSimpleName();
    }
}
