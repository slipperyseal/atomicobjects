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

import net.catchpole.lang.Throw;

import javax.xml.transform.Source;
import javax.xml.transform.TransformerException;
import javax.xml.transform.URIResolver;
import javax.xml.transform.stream.StreamSource;
import java.io.IOException;
import java.io.InputStream;

public class ResourceURIResolver implements URIResolver {
    private Class clazz;
    private ResourceSource resourceSource;

    public ResourceURIResolver(ResourceSource cache) {
        this.resourceSource = cache;
    }

    public ResourceURIResolver(Class clazz) {
        this.clazz = clazz;
    }

    public Source resolve(String href, String base) throws TransformerException {
        try {
            InputStream is = clazz != null ? clazz.getResourceAsStream(href) : resourceSource.getResourceStream(href);
            if (is == null) {
                return null;
            }
            //Core.getTrace().info(href + ' ' + base + ' ' + is);
            return new StreamSource(is);
        } catch (IOException ioe) {
            throw Throw.unchecked(ioe);
        }
    }
}
