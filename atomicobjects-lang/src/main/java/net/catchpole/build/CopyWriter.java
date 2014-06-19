package net.catchpole.build;

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

import net.catchpole.trace.Core;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

/**
 * Ensures each class has a copyright notice.  Also counts classes, lines and files updated.
 */
public class CopyWriter {
    private final String[] copyright;
    private final String packagePrefix;
    private int totalLines;
    private int totalFiles;
    private int processedFiles;

    public CopyWriter(File dir, String[] copyright, String packageName) throws IOException {
        this.copyright = copyright;
        this.packagePrefix = "package " + packageName;
        load(dir);
        Core.getTrace().info("CopyWriter: " + totalFiles + " classes, " + totalLines + " lines, " + processedFiles + " updated");
    }

    private void load(File dir) throws IOException {
        File[] files = dir.listFiles();
        for (File file : files) {
            if (file.isDirectory()) {
                load(file);
            } else if (file.getName().endsWith(".java")) {
                process(file);
            }
        }
    }

    private void process(File file) throws IOException {
        totalFiles++;

        List<String> list = new ArrayList<String>();
        BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(file)));
        try {
            String line;
            while ((line = br.readLine()) != null) {
                list.add(line);
            }
            String[] lines = list.toArray(new String[list.size()]);

            totalLines += lines.length;

            if (lines.length > 5) {
                if (lines[0].startsWith(packagePrefix) &&
                        lines[1].trim().length() == 0 &&
                        !lines[2].startsWith(copyright[0])) {
                    addCopyright(file, lines);
                }
            }
        } finally {
            br.close();
        }
    }

    private void addCopyright(File file, String[] lines) throws IOException {
        processedFiles++;

        Core.getTrace().info(this.getClass().getName() + " updating: " + file);
        PrintWriter pw = new PrintWriter(new FileOutputStream(file));
        try {
            pw.println(lines[0]);
            pw.println(lines[1]);
            for (String line : copyright) {
                pw.println(line);
            }
            pw.println();

            boolean header = true;
            for (int x = 2; x < lines.length; x++) {
                // eat blank lines between copyright and start of code
                if (header) {
                    if (lines[x].trim().length() == 0 || lines[x].startsWith("//")) {
                        continue;
                    } else {
                        header = false;
                    }
                }
                pw.println(lines[x]);
            }
        } finally {
            pw.close();
        }
    }
}
