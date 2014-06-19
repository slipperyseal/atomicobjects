package net.catchpole.net.tool.url;

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
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.List;

/**
 * Loads files by URL to a directory if they do not already exist.
 * usage: -target [dir] -urls [url1] [url2] [url3 etc.]
 * <p/>
 * This class is specifically designed to have no project imports so that it can be compiled separately
 * before the project us built.
 */
public class URLFileLoader {
    public static void main(String args[]) throws IOException {
        Arguments arguments = new Arguments(args);
        String dir = arguments.getArgument("-target");
        String[] urls = arguments.getArgumentArray("-urls");

        File target = new File(dir);
        for (String url : urls) {
            new URLFileLoader(new URL(url), target);
        }
    }

    public URLFileLoader(URL url, File directory) throws IOException {
        if (!directory.isDirectory()) {
            directory.mkdirs();
        }

        if (!directory.isDirectory()) {
            throw new IllegalArgumentException("Not a directory: " + directory);
        }

        String fileName = new File(url.getFile()).getName();
        File target = new File(directory, fileName);

        System.out.println((target.exists() ? "exists: " : "loading: ") + url + " > " + target);

        if (target.exists()) {
            return;
        }

        load(url, target);
    }

    private void load(URL url, File target) throws IOException {
        URLConnection connection = url.openConnection();
        InputStream is = connection.getInputStream();
        try {
            OutputStream os = new FileOutputStream(target);
            try {
                spool(is, os);
            } catch (IOException ioe) {
                // delete any half-downloaded file to prevent problems.. yes, im looking at you Maven
                target.delete();
                throw new IOException(url.toString() + " failed.  " + target + " deleted.");
            } finally {
                os.close();
            }
        } finally {
            is.close();
        }
    }

    private static void spool(InputStream is, OutputStream os) throws IOException {
        spool(is, os, 4096);
    }

    private static void spool(InputStream is, OutputStream os, int spoolSize) throws IOException {
        spool(is, os, new byte[spoolSize]);
    }

    private static void spool(InputStream is, OutputStream os, byte[] spoolBuffer) throws IOException {
        int l;
        while ((l = is.read(spoolBuffer, 0, spoolBuffer.length)) != -1) {
            os.write(spoolBuffer, 0, l);
        }
    }
}

class Arguments {
    private String[] args;

    public Arguments(String[] args) {
        this.args = args;
    }

    /**
     * Returns the String following the specified key.
     *
     * @param key Key name to look for.
     * @return String following the key or defaultValue if key not found.
     */
    public boolean hasArgument(String key) {
        for (String value : args) {
            if (value.equalsIgnoreCase(key)) {
                return true;
            }
        }
        return false;
    }

    /**
     * Returns the String following the specified key.
     *
     * @param key Key name to look for.
     * @return String following the key or defaultValue if key not found.
     */
    public String getArgument(String key, String defaultValue) {
        for (int x = 0; x < args.length - 1; x++) {
            if (args[x].equalsIgnoreCase(key)) {
                return args[x + 1];
            }
        }
        return defaultValue;
    }

    public String getArgument(String key) {
        for (int x = 0; x < args.length - 1; x++) {
            if (args[x].equalsIgnoreCase(key)) {
                return args[x + 1];
            }
        }
        throw new IllegalArgumentException("Required argument: " + key);
    }

    public String[] getArgumentArray(String key) {
        for (int x = 0; x < args.length - 1; x++) {
            if (args[x].equalsIgnoreCase(key)) {
                List list = new ArrayList();
                while (x < args.length - 1 && !args[x + 1].startsWith("-")) {
                    list.add(args[++x]);
                }
                return (String[]) list.toArray(new String[list.size()]);
            }
        }
        return new String[0];
    }

    /**
     * Returns the String following the specified key.
     *
     * @param key Key name to look for.
     * @return String following the key or defaultValue if key not found.
     */
    public int getArgumentProperty(String key, int defaultValue) {
        for (int x = 0; x < args.length - 1; x++) {
            if (args[x].equalsIgnoreCase(key)) {
                return Integer.parseInt(args[x + 1]);
            }
        }
        return defaultValue;
    }

    public String toString() {
        StringBuffer sb = new StringBuffer(64);
        sb.append(this.getClass().getSimpleName());
        sb.append(':');
        for (String arg : args) {
            sb.append(' ');
            sb.append(arg);
        }
        return sb.toString();
    }
}
