package net.catchpole.dom;

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

import net.catchpole.dom.builder.XsdBuilder;
import net.catchpole.dom.transform.XmlTransform;
import net.catchpole.model.BeanModel;
import net.catchpole.model.Model;
import org.junit.Test;
import org.w3c.dom.Document;

import javax.xml.parsers.DocumentBuilderFactory;
import java.io.ByteArrayInputStream;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class ModelDocumentTest {
    @Test
    public void testModelDocument() throws Exception {
        Model model = new BeanModel(getTestMap());

        ModelDocument document = new ModelDocumentBuilder(new XsdBuilder()).newDocument(model);
        new XmlTransform().printXML(document);
    }

    @Test
    public void testModelAttachedDocument() throws Exception {
        Model model = new BeanModel(getTestMap());
        ModelDocument targetDocument = new ModelDocumentBuilder(new XsdBuilder()).newDocument(model);

        targetDocument.attach(getTestDocument());

        new XmlTransform().printXML(targetDocument);
    }

    public Map getTestMap() throws Exception {
        Map map = new HashMap();
        map.put("testing","value");
        map.put("the date", new Date());
        return map;
    }

    public Document getTestDocument() throws Exception {
        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
        return dbf.newDocumentBuilder().parse(new ByteArrayInputStream(
                "<root><attachnode><nested>oh HAI</nested></attachnode></root>".getBytes()));
    }
}
