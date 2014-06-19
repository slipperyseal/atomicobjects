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

/**
 */
public class DirectoryTreeIterator implements Iterator<File> {
    private StackedDirectoryIterator currentIterator;
    private File next;

    public DirectoryTreeIterator(File currentIterator) throws IOException {
        this.currentIterator = new StackedDirectoryIterator(currentIterator, null);
        doNext();
    }

    public boolean hasNext() {
        return next != null;
    }

    public File next() {
        File next = this.next;
        if (next == null) {
            throw new IllegalStateException();
        }
        doNext();
        return next;
    }

    public void remove() {
        throw new IllegalStateException();
    }

    private void doNext() {
        this.next = null;

        // if interator has no next, search up the parent tree for one that hasNext
        // parent will be null if at end of tree
        while (this.currentIterator != null && !this.currentIterator.hasNext()) {
            this.currentIterator = this.currentIterator.unhook();
        }

        while (this.currentIterator != null && this.currentIterator.hasNext()) {
            this.next = this.currentIterator.next();
            if (!this.next.isDirectory()) {
                return;
            } else {
                try {
                    this.currentIterator = new StackedDirectoryIterator(this.next, this.currentIterator);
                } catch (IOException e) {
                    // catch not a directory exception - shouldn't happen
                    Throw.unchecked(e);
                }
            }
        }
    }

    class StackedDirectoryIterator extends DirectoryIterator {
        private StackedDirectoryIterator parent;

        public StackedDirectoryIterator(File file, StackedDirectoryIterator parent) throws IOException {
            super(file);
            this.parent = parent;
        }

        public StackedDirectoryIterator unhook() {
            StackedDirectoryIterator dir = parent;
            this.parent = null;
            return dir;
        }
    }
}
