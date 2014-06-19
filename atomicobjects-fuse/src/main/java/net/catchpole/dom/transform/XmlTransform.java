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

import org.w3c.dom.Document;

import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.ByteArrayOutputStream;
import java.io.OutputStream;

public final class XmlTransform {
    private static final TransformerFactory factory = TransformerFactory.newInstance();

    public void printXML(Document document, OutputStream os) throws Exception {
        DOMSource source = new DOMSource(document);
        StreamResult result = new StreamResult(os);
        getTransformer().transform(source, result);
    }

    public void printXML(Document document) throws Exception {
        printXML(document, System.out);
    }

    public String getXML(Document document) throws Exception {
        ByteArrayOutputStream baos = new ByteArrayOutputStream(1024);
        printXML(document, baos);
        return baos.toString();
    }

    public Transformer getTransformer() throws Exception {
        factory.setAttribute("indent-number", 4);

        Transformer transformer = factory.newTransformer();
        transformer.setOutputProperty(OutputKeys.METHOD, "xml");
        transformer.setOutputProperty(OutputKeys.INDENT, "yes");
        transformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "4");
        return transformer;
    }
}
