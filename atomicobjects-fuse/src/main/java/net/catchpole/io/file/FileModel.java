package net.catchpole.io.file;

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

import net.catchpole.lang.ArrayIterator;
import net.catchpole.lang.NullIterator;
import net.catchpole.lang.Transformation;
import net.catchpole.lang.TransformationIterator;
import net.catchpole.model.Model;

import java.io.File;
import java.io.FilenameFilter;
import java.util.Iterator;

public class FileModel implements Model {
    private static final FilenameFilter dotFilter = new FilenameFilter() {
        public boolean accept(File dir, String name) {
            return !name.startsWith(".");
        }
    };

    private final File file;

    public FileModel(File file) {
        this.file = file;
    }

    public String getName() {
        return file.getName();
    }

    public Class getType() {
        return File.class;
    }

    public Iterator getValues() {
        return new ArrayIterator<File>(new File[]{file});
    }

    public Iterator<Model> iterator() {
        try {
            if (file.isDirectory()) {
                return new TransformationIterator<File, Model>(
                        new ArrayIterator<File>(file.listFiles(dotFilter)),
                        new Transformation<File, Model>() {
                            public Model transform(File source) {
                                return new FileModel(source);
                            }
                        }
                );

            }
        } catch (Exception e) {
            //may be inaccessible due to permissions
        }
        return new NullIterator<Model>();
    }
}
