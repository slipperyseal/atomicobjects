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

import net.catchpole.io.Files;
import net.catchpole.lang.Strings;
import net.catchpole.trace.Core;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Provides high level package dependency information.
 * <p/>
 * <P>Scans source files for import definitions, therefor does not detect inline package definitions.
 * eg.  java.util.List list = new java.util.ArrayList();
 */
public class DependencyFinder {
    private final Map<String, List<String>> dependencies = new HashMap<String, List<String>>();
    private final boolean byFile;
    private final String filter;

    public DependencyFinder(File dir, String filter, boolean byFile) throws IOException {
        this.byFile = byFile;
        this.filter = filter;

        load(null, dir);
    }

    public void print() {
        for (String filePackage : sort(dependencies.keySet())) {
            List<String> imports = dependencies.get(filePackage);

            Core.getTrace().info(filePackage);
            for (String depend : sort(imports)) {
                Core.getTrace().info(depend);
            }

        }
    }

    /**
     * Returns a list of error Strings if the package specified is imported within any other package.
     */
    public List<String> checkLocalized(String packageName) {
        List<String> localizeFailures = new ArrayList<String>();

        for (String filePackage : sort(dependencies.keySet())) {
            List<String> imports = dependencies.get(filePackage);

            if (anyStartWith(imports, packageName) && !filePackage.startsWith(packageName)) {
                localizeFailures.add(filePackage + " imports " + packageName + ' ' + imports);
            }
        }

        return localizeFailures.size() == 0 ? null : localizeFailures;
    }

    private void load(String path, File dir) throws IOException {
        path = path != null ? path + dir.getName() + '.' : "";

        File[] files = dir.listFiles();
        for (File file : files) {
            if (file.isDirectory()) {
                load(path, file);
            } else if (file.getName().endsWith(".java")) {
                read(path, file);
            }
        }
    }

    private void read(String path, File file) throws IOException {
        String pack = removeLastToken(path + (byFile ? file.getName() : removeLastToken(file.getName())));

        List<String> imports = dependencies.get(pack);
        if (imports == null) {
            imports = new ArrayList<String>();
            dependencies.put(pack, imports);
        }

        String[] lines = Strings.tokenize(new String(Files.loadFile(file)), '\r');
        for (String line : lines) {
            if (line.startsWith("import ")) {
                String importPackage = removeLastToken(line.substring(7, line.indexOf(';')));
                if (importPackage.startsWith(filter) && !imports.contains(importPackage)) {
                    imports.add(importPackage);
                }
            }
        }
    }

    private String removeLastToken(String name) {
        return name.substring(0, name.lastIndexOf('.'));
    }

    private boolean anyStartWith(List<String> list, String string) {
        for (String item : list) {
            if (item.startsWith(string)) {
                return true;
            }
        }
        return false;
    }

    private String[] sort(Collection<String> collection) {
        String[] array = collection.toArray(new String[collection.size()]);
        Arrays.sort(array);
        return array;
    }
}
