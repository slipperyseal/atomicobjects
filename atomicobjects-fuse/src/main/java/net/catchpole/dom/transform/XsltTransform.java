package net.catchpole.dom.transform;

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

import net.catchpole.resource.FileResourceSource;
import net.catchpole.resource.ResourceSource;
import net.catchpole.resource.ResourceURIResolver;
import org.w3c.dom.Document;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;

public class XsltTransform {
    private static final DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();

    public XsltTransform(File xmlFile, File xslFileDir, String xslFileName, File outputFile) throws Exception {
        InputStream is = new FileInputStream(xmlFile);
        try {
            Document document = dbf.newDocumentBuilder().parse(is);

            FileOutputStream os = new FileOutputStream(outputFile);
            try {
                Transformer transformer = buildTransfomer(xslFileDir, xslFileName);
                transformer.transform(new DOMSource(document), new StreamResult(os));
            } finally {
                os.close();
            }
        } finally {
            is.close();
        }
    }

    private Transformer buildTransfomer(File xslFileDir, String xslFileName) throws Exception {
        ResourceSource resourceSource = new FileResourceSource(xslFileDir);
        ResourceURIResolver resourceURIResolver = new ResourceURIResolver(resourceSource);
        TransformerFactory factory = TransformerFactory.newInstance();
        factory.setURIResolver(resourceURIResolver);
        return factory.newTransformer(new StreamSource(resourceSource.getResourceStream(xslFileName)));
    }
}
