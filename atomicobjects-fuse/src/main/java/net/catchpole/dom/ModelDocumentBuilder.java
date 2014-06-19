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

import net.catchpole.model.Model;
import org.w3c.dom.Document;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

public class ModelDocumentBuilder {
    private final DocumentBuilder documentBuilder;
    private final ElementBuilder elementBuilder;

    public ModelDocumentBuilder(ElementBuilder elementBuilder) throws ParserConfigurationException {
        this(elementBuilder, DocumentBuilderFactory.newInstance().newDocumentBuilder());
    }

    public ModelDocumentBuilder(ElementBuilder elementBuilder, DocumentBuilder documentBuilder) {
        this.elementBuilder = elementBuilder;
        this.documentBuilder = documentBuilder;
    }

    public ModelDocument newDocument(Model model) {
        Document document = this.documentBuilder.newDocument();
        return new ModelDocument(document, elementBuilder, model);
    }
}
