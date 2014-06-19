package net.catchpole.web.paths;

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

import net.catchpole.lang.Throw;
import net.catchpole.resource.ResourceSource;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;

public class ResourcePathSource {
    private ResourceSource resourceSource;

    public ResourcePathSource(ResourceSource resourceSource) {
        this.resourceSource = resourceSource;
    }

    public InputStream getPage(String[] path, String type) {
        StringBuilder sb = new StringBuilder("site");
        if (path.length == 1) {
            sb.append("/root." + type);
        } else {
            for (int x = 1; x < path.length; x++) {
                // accept no paths with dots - they could be dot dot "back directory"
                if (path[x].indexOf('.') != -1) {
                    return null;
                }
                sb.append('/');
                sb.append(path[x]);
            }
            sb.append('.');
            sb.append(type);
        }
        File file = new File(sb.toString());
        if (!file.exists()) {
            return null;
        }
        try {
            return new FileInputStream(file);
        } catch (Exception e) {
            throw Throw.unchecked(e);
        }
    }
}
