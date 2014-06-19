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

public class DirectoryTree {
    private final File baseDir;

    public DirectoryTree(File baseDir) {
        if (!baseDir.isDirectory()) {
            throw new IllegalArgumentException(baseDir.getName());
        }
        this.baseDir = baseDir;
    }

    public void deleteEmptyDirs() {
        recurseDeleteEmptyDirs(baseDir);
    }

    public void deleteAll() {
        recurseDeleteAll(baseDir);
    }

    /**
     * Recurses all directories returning empty status on the way back.
     */
    private boolean recurseDeleteEmptyDirs(File dir) {
        File[] allFiles = dir.listFiles();

        // return true if dir is now empty
        if (allFiles.length == 0) {
            return true;
        }

        for (File file : allFiles) {
            if (file.isDirectory()) {
                if (recurseDeleteEmptyDirs(file)) {
                    file.delete();
                }
            }
        }

        // return true if dir is now empty
        return (dir.listFiles().length == 0);
    }

    private void recurseDeleteAll(File dir) {
        File[] allFiles = dir.listFiles();

        for (File file : allFiles) {
            if (file.isDirectory()) {
                recurseDeleteAll(file);
            }
            file.delete();
        }
    }
}
