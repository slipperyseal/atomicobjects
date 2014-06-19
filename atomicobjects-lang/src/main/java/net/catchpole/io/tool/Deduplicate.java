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

import net.catchpole.io.Arrays;
import net.catchpole.io.DirectoryTree;
import net.catchpole.io.Files;
import net.catchpole.lang.Arguments;
import net.catchpole.trace.Core;

import javax.swing.*;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.zip.CRC32;

public class Deduplicate {
    private final List<File> repeats = new ArrayList<File>();

    public static void main(String[] args) {
        try {
            if (args.length == 0) {
                Core.getTrace().info(Deduplicate.class.getSimpleName() + " -dirs <dir1 dir2 ...> -delete -confirm -high -matchnames -trimdirs");
                return;
            }

            Arguments arguments = new Arguments(args);
            File[] dirList = arguments.getFileArray("-dirs");

            List<File> files = Files.getRecursiveFileList(dirList);

            Deduplicate fc = new Deduplicate(files, arguments.hasArgument("-high"));
            if (arguments.hasArgument("-delete")) {
                if (arguments.hasArgument("-confirm")) {
                    if (JOptionPane.showConfirmDialog(null, "Delete " + fc.repeats.size() + " ?") == JOptionPane.OK_OPTION) {
                        fc.deleteRepeats();
                    }
                } else {
                    fc.deleteRepeats();
                }
            }

            if (arguments.hasArgument("-trimdirs")) {
                for (File dir : dirList) {
                    DirectoryTree dt = new DirectoryTree(dir);
                    dt.deleteEmptyDirs();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public Deduplicate(List<File> files, boolean high) throws IOException {
        // create a map of lists of files with identicle size
        Map<Long, List<File>> lengthMap = new HashMap<Long, List<File>>();

        if (files.size() > 1) {
            if (high) {
                Arrays.reverse(files);
            }
            for (File file : files) {
                List<File> list = lengthMap.get(file.length());
                if (list == null) {
                    list = new ArrayList<File>();
                    lengthMap.put(file.length(), list);
                }
                list.add(file);
            }
        }

        // remove lists with only one file
        Iterator<Long> i = lengthMap.keySet().iterator();
        while (i.hasNext()) {
            Long key = i.next();
            if (lengthMap.get(key).size() == 1) {
                i.remove();
            }
        }

        Core.getTrace().info("Indenticle length groups: ", lengthMap.size());

        for (List<File> list : lengthMap.values()) {
            compareIdenticleLengths(list);
        }
    }

    private void compareIdenticleLengths(List<File> files) throws IOException {
        // create list of CRCs for files
        List<Integer> crcs = new ArrayList<Integer>();
        for (File file : files) {
            crcs.add(crc(file));
        }

        // compare each file with every other
        for (int x = 0; x < files.size(); x++) {
            for (int y = 0; y < files.size(); y++) {
                if (x != y) {
                    File fx = files.get(x);
                    File fy = files.get(y);

                    if (fx != null & fy != null & crcs.get(x).equals(crcs.get(y)) & Files.fileEquals(fx, fy)) {
                        // tag lower file (if -high, list was reversed)
                        repeats.add(fx);
                        // removed taged file from compare
                        files.set(x, null);

                        Core.getTrace().info(fx, "\r\n\t\t\t=\t", fy);
                    }
                }
            }
        }
    }

    public void deleteRepeats() throws IOException {
        for (File file : repeats) {
            try {
                file.delete();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        Core.getTrace().info("Repeats deleted:" + repeats.size());
    }

    // a crc of the start of the file, reduces the need to do binary compares of files which will never equate
    private static int crc(File file) throws IOException {
        // use small buffer, so that non-equal files dont need to load so much at start
        int bufferlen = (int) (file.length() < 1024 ? file.length() : 1024);
        byte[] buffer = new byte[bufferlen];

        DataInputStream is = new DataInputStream(new FileInputStream(file));
        try {
            is.readFully(buffer);
        } finally {
            is.close();
        }
        CRC32 crc32 = new CRC32();
        crc32.update(buffer);
        return (int) crc32.getValue();
    }

}
