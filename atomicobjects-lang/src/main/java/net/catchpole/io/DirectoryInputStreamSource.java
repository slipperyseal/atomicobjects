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
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 */
public class DirectoryInputStreamSource implements InputStreamSource {
    private final File directory;
    private final File[] files;
    private String name;
    private int next;
    private boolean loop;
    private boolean fail;

    /**
     * @param directory
     * @param recurse   Recursivly find all files in sub-directories
     * @param loop      Loop through the list of files when returning InputStreams
     * @throws IOException
     */

    public DirectoryInputStreamSource(File directory, boolean recurse, boolean loop) throws IOException {
        this.directory = directory;
        this.files = (recurse ? recurseDirs(directory) : directory.listFiles());
        this.loop = loop;
        Arrays.sort(this.files);
        if (this.files.length == 0) {
            fail = true;
        }
    }

    public InputStream getInputStream() throws IOException {
        return new FileInputStream(getNextFile());
    }

    public File getNextFile() throws IOException {
        if (fail) {
            throw new EOFException("No more files in: " + directory);
        }
        File file = this.files[next];
        this.name = this.files[next].getName();
        next++;
        if (next >= files.length) {
            if (!loop) {
                fail = true;
            }
            next = 0;
        }
        return file;
    }

    public String getCurrentFileName() {
        return name;
    }

    public String toString() {
        return this.getClass().getSimpleName() + ' ' + directory;
    }

    private File[] recurseDirs(File dir) {
        List<File> list = new ArrayList<File>();
        recurse(list, dir);
        return list.toArray(new File[list.size()]);
    }

    private void recurse(List<File> list, File dir) {
        File[] files = dir.listFiles();
        Arrays.sort(files);
        for (File file : files) {
            if (file.isDirectory()) {
                recurse(list, file);
            } else {
                list.add(file);
            }
        }
    }
}
