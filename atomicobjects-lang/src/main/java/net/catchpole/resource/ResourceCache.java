package net.catchpole.resource;

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

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.Map;

/**
 * Maintains a cache of resources which are requested through this object.
 * Can be supplied with a class to use a path reference or can be subclassed by a class
 * which is packaged in the appropriate location for the resources.
 */
public class ResourceCache implements ResourceSource {
    private ResourceSource resourceSource;

    private Map<String, byte[]> resources = new HashMap<String, byte[]>();

    public ResourceCache() {
        // sub-classing class
        this.resourceSource = new ClasspathResourceSource(this.getClass());
    }

    public ResourceCache(ResourceSource resourceSource) {
        this.resourceSource = resourceSource;
    }

    public InputStream getResourceStream(String name) throws IOException {
        byte[] bytes = findResource(name);
        return (bytes != null ? new ByteArrayInputStream(bytes) : null);
    }

    public byte[] getResource(String name) throws IOException {
        return findResource(name);
    }

    public boolean writeResource(String name, OutputStream os) throws IOException {
        byte[] bytes = findResource(name);
        if (bytes != null) {
            os.write(bytes);
        }
        return bytes != null;
    }

    private byte[] findResource(String name) throws IOException {
        byte[] bytes = resources.get(name);
        if (bytes == null) {
            InputStream is = this.resourceSource.getResourceStream(name);
            if (is == null) {
                return null;
            }
            ByteArrayOutputStream baos = new ByteArrayOutputStream(1024);
            try {
                Arrays.spool(is, baos);
            } catch (IOException ioe) {
                return null;
            }
            bytes = baos.toByteArray();
            // only synchronize here, not around slow resource load
            synchronized (resources) {
                if (!resources.containsKey(name)) {
                    resources.put(name, bytes);
                }
            }
        }
        return bytes;
    }
}
