package net.catchpole.net.tool;

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
import net.catchpole.io.FileDateComparator;
import net.catchpole.lang.Arguments;
import net.catchpole.lang.Strings;
import net.catchpole.net.SimpleAuthenticator;
import net.catchpole.trace.Core;
import net.catchpole.trace.Trace;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.Authenticator;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.List;

/**
 * Slurps sets of files from URLs based on a numbered pattern.
 * <p/>
 * <p>for example, to slurp http://site/image000big.jpg to http://site/image090big.jpg
 * <p>-target . -url http://site/image -start 0 -stop 90 -digits 3 -suffix big.jpg -type .jpg
 */
public class Slurper {
    private Trace trace = Core.getTrace(this);
    private List<String> slurpList = new ArrayList<String>();
    private File targetDir;
    private String extension;

    public static void main(String[] args) throws IOException {
        Arguments arguments = new Arguments(args);
        String user = arguments.getArgument("-user", null);
        String password = arguments.getArgument("-password", null);

        if (user != null && password != null) {
            Authenticator.setDefault(new SimpleAuthenticator(user, password));
        }

        Slurper slurper = new Slurper(new File(arguments.getArgument("-target")),
                arguments.getArgument("-type"));

        slurper.addSet(Integer.parseInt(arguments.getArgument("-start")),
                Integer.parseInt(arguments.getArgument("-stop")),
                arguments.getArgument("-url"),
                Integer.parseInt(arguments.getArgument("-digits", "1")),
                arguments.getArgument("-suffix"));

        slurper.slurp();
    }

    public Slurper(File targetDir, String extension) {
        this.targetDir = targetDir;
        this.extension = extension;
    }

    public void slurp(String urlString) throws IOException {
        try {
            File file = new File(targetDir, Strings.alphaNum(urlString) + extension);
            if (!file.exists()) {
                URL url = new URL(urlString);
                URLConnection urlc = url.openConnection();
                urlc.connect();

                InputStream is = urlc.getInputStream();
                try {
                    FileOutputStream fos = new FileOutputStream(file);
                    try {
                        Arrays.spool(is, fos);
                    } finally {
                        fos.close();
                    }
                    trace.info("Slurped", url, file.length());
                } finally {
                    is.close();
                }
            } else {
                trace.info("Exists", urlString);
            }
        } catch (IOException e) {
            trace.error(urlString, e);
        }
    }

    public void slurp() throws IOException {
        for (String url : slurpList) {
            trace.info(url);
        }

        for (String string : slurpList) {
            slurp(string);
        }
    }

    public void addSet(int start, int stop, String pre, int digits, String end) {
        for (int x = start; x < (stop + 1); x++) {
            String num = Integer.toString(x);
            while (num.length() < digits) {
                num = "0" + num;
            }
            slurpList.add(pre + num + end);
        }
    }

    public byte[] loadURL(String urlString) throws IOException {
        URL url = new URL(urlString);
        URLConnection urlc = url.openConnection();
        urlc.connect();

        InputStream is = urlc.getInputStream();
        try {
            ByteArrayOutputStream baos = new ByteArrayOutputStream(1024);
            try {
                Arrays.spool(is, baos);
            } finally {
                baos.close();
            }
            byte[] bytes = baos.toByteArray();
            trace.info("Loaded", url, bytes.length);
            return bytes;
        } finally {
            is.close();
        }
    }

    public void sortByDate(File dir) {
        File[] all = dir.listFiles();
        List<File> list = new ArrayList<File>();
        for (File file : all) {
            if (file.getName().endsWith(extension)) {
                list.add(file);
            }
        }
        all = list.toArray(new File[list.size()]);

        java.util.Arrays.sort(all, new FileDateComparator());

        int x = 0;
        for (File file : all) {
            x++;
            String n = "" + x;
            while (n.length() < 4) {
                n = "0" + n;
            }
            File newFile = new File(dir, n + '-' + file.getName());
            trace.info(newFile);
            file.renameTo(newFile);
        }
    }
}


