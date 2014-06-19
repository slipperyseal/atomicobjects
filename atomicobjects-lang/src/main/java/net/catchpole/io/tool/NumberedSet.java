package net.catchpole.io.tool;

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

import net.catchpole.io.Files;
import net.catchpole.lang.Arguments;
import net.catchpole.trace.Core;

import java.io.File;
import java.io.IOException;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class NumberedSet {
    private final List<File> fileList = new ArrayList<File>();

    public static void main(String[] args) {
        try {
            if (args.length < 2) {
                Core.getTrace().info(NumberedSet.class.getSimpleName() + " -dirs <dir1 dir2 ...> -prefix <prefix>");
                return;
            }
            Arguments arguments = new Arguments(args);
            File[] dirs = arguments.getFileArray("-dirs");
            NumberedSet fa = new NumberedSet(dirs);
            fa.rename(dirs[0], arguments.getArgument("-prefix", null), 1, 5);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public NumberedSet(File baseDir) throws IOException {
        recurse(baseDir);
    }

    public NumberedSet(File[] baseDirs) throws IOException {
        for (File baseDir : baseDirs) {
            recurse(baseDir);
        }
    }


    private void recurse(File dir) throws IOException {
        if (!dir.isDirectory()) {
            throw new IOException(dir + " is not a directory");
        }

        Core.getTrace().info("Reading", dir);
        File[] files = dir.listFiles();

        Arrays.sort(files);

        for (File f : files) {
            if (f.isDirectory()) {
                recurse(f);
            } else {
                // dont add "." files (.DS_STORE, .cvs etc)
                if (!f.getName().startsWith(".")) {
                    fileList.add(f);
                }
            }
        }
    }

    public void rename(File baseDir, String prefix, int start, int digits) throws IOException {
        NumberFormat nf = NumberFormat.getInstance();
        nf.setMinimumIntegerDigits(digits);
        nf.setGroupingUsed(false);

        for (File file : fileList) {
            String name = file.getName();
            String suffix = "";
            int d = name.lastIndexOf('.');
            if (d != -1) {
                suffix = name.substring(d).toLowerCase();
                // fix jpeg > jpg
                if (suffix.equals(".jpeg")) {
                    suffix = ".jpg";
                }
            }

            String num = nf.format(start++);
            String zone = num.substring(0, 2) + "000";

            File targetDir = new File(baseDir, zone);
            if (!targetDir.exists()) {
                if (!targetDir.mkdirs()) {
                    throw new IOException("Unable to create path: " + targetDir);
                }
            }

            File newName = new File(targetDir, prefix + num + suffix);
            if (!newName.equals(file)) {
                Core.getTrace().info("Rename:", file, newName);
                if (newName.exists()) {
                    throw new IOException("Cant rename: " + file + " to existing file " + newName);
                }
                // java rename will move a file if required
                file.renameTo(newName);
                //move(file, newName);
            }
        }
    }

    /**
     * Rename which does a heavy weight move and delete.
     */
    private void move(File source, File target) throws IOException {
        byte[] data = Files.loadFile(source);
        Files.saveFile(target, data);
        source.delete();
    }

    public String toString() {
        return this.getClass().getSimpleName() + ' ' + fileList + " files";
    }

}
