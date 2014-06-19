package net.catchpole.lang;

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
import java.util.ArrayList;
import java.util.List;

public class Arguments {
    private final String[] args;

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

    public File[] getFileArray(String key) {
        String[] strings = getArgumentArray(key);
        File[] files = new File[strings.length];
        for (int x = 0; x < files.length; x++) {
            files[x] = new File(strings[x]);
        }
        return files;
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
