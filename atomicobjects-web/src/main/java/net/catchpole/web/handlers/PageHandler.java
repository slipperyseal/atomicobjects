package net.catchpole.web.handlers;

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
import net.catchpole.resource.ResourceURIResolver;
import net.catchpole.web.http.HttpUtils;
import net.catchpole.web.paths.PathHandler;
import org.w3c.dom.Document;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Arrays;

public class PageHandler implements PathHandler {
    private static final DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
    private ResourceSource resourceSource;

    public PageHandler(ResourceSource resourceCache) {
        this.resourceSource = resourceCache;
    }

    public void handle(String[] path, HttpServletRequest req, HttpServletResponse res) throws IOException {
        String fileName = (path != null && path.length == 0 || path[0].length() == 0 ? "root" : path[0]);
        try {
            InputStream is = resourceSource.getResourceStream(fileName + ".xml");
            try {
                if (is == null) {
                    throw new IllegalArgumentException(Arrays.asList(path).toString());
                }

                Document document = dbf.newDocumentBuilder().parse(is);
                //XmlUtils.printXML(document);

                // set no cache headers
                HttpUtils.noCache(res);

                // write content compressed if available
                OutputStream os = HttpUtils.getCompressedOutputStream(req, res);
                try {
                    Transformer transformer = buildTransfomer();
                    transformer.transform(new DOMSource(document), new StreamResult(os));
                } finally {
                    // very important - if this is a compressed stream, we need to close it.
                    os.flush();
                    os.close();
                }
            } finally {
                is.close();
            }
        } catch (Exception e) {
            throw Throw.unchecked(e);
        }
    }

    public long getLastModified(String[] path, HttpServletRequest req) throws IOException {
        return -1;
    }

    private Transformer buildTransfomer() throws Exception {
        ResourceURIResolver resourceURIResolver = new ResourceURIResolver(resourceSource);
        TransformerFactory factory = TransformerFactory.newInstance();
        factory.setURIResolver(resourceURIResolver);
        return factory.newTransformer(new StreamSource(resourceSource.getResourceStream("root.xsl")));
    }
}
