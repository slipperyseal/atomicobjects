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

import net.catchpole.lang.MapTarget;
import net.catchpole.model.Model;
import net.catchpole.model.ValueModel;
import org.w3c.dom.Attr;
import org.w3c.dom.DOMException;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.w3c.dom.TypeInfo;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class ModelElement extends ModelNode implements Element {
    private final Element proxyElement;

    public ModelElement(ModelDocument modelDocument, ModelNode parentNode, Model model) {
        super(modelDocument, parentNode, model,
                modelDocument.getProxyDocument().createElement(modelDocument.getElementBuilder().getName(model)));
        this.proxyElement = (Element) super.proxyNode;

        ElementBuilder elementBuilder = modelDocument.getElementBuilder();

        if (model instanceof ValueModel) {
            Map<String,Object> attributes = ((ValueModel)model).getAttributes();
            if (attributes != null) {
                for (String name : attributes.keySet()) {
                    Object value = attributes.get(name);
                    if (value != null) {
                        proxyElement.setAttribute(name, value.toString());
                    }
                }
            }
        }

        elementBuilder.setAttributes(model, new MapTarget<String, String>() {
            public void set(String key, String value) {
                proxyElement.setAttribute(key, value);
            }
        });

        String text = elementBuilder.getText(model);
        if (text != null) {
            addTextNode(text);
        }
    }

    public String getTagName() {
        return this.proxyElement.getTagName();
    }

    public String getAttribute(String name) {
        return this.proxyElement.getAttribute(name);
    }

    public void setAttribute(String name, String value) throws DOMException {
        this.proxyElement.setAttribute(name, value);
    }

    public void removeAttribute(String name) throws DOMException {
        this.proxyElement.removeAttribute(name);
    }

    public Attr getAttributeNode(String name) {
        return this.proxyElement.getAttributeNode(name);
    }

    public Attr setAttributeNode(Attr newAttr) throws DOMException {
        return this.proxyElement.setAttributeNode(newAttr);
    }

    public Attr removeAttributeNode(Attr oldAttr) throws DOMException {
        return this.proxyElement.removeAttributeNode(oldAttr);
    }

    public NodeList getElementsByTagName(String name) {
        final List<Node> filteredNodeList = new ArrayList<Node>();
        for (Node node : nodeList) {
            if (node.getNodeName().equals(name)) {
                filteredNodeList.add(node);
            }
        }

        return new NodeList() {
            public Node item(int index) {
                return filteredNodeList.get(index);
            }

            public int getLength() {
                return filteredNodeList.size();
            }
        };
    }

    public String getAttributeNS(String namespaceURI, String localName) throws DOMException {
        return this.proxyElement.getAttributeNS(namespaceURI, localName);
    }

    public void setAttributeNS(String namespaceURI, String qualifiedName, String value) throws DOMException {
        this.proxyElement.setAttributeNS(namespaceURI, qualifiedName, value);
    }

    public void removeAttributeNS(String namespaceURI, String localName) throws DOMException {
        this.proxyElement.removeAttributeNS(namespaceURI, localName);
    }

    public Attr getAttributeNodeNS(String namespaceURI, String localName) throws DOMException {
        return this.proxyElement.getAttributeNodeNS(namespaceURI, localName);
    }

    public Attr setAttributeNodeNS(Attr newAttr) throws DOMException {
        return this.proxyElement.setAttributeNodeNS(newAttr);
    }

    public NodeList getElementsByTagNameNS(String namespaceURI, String localName) throws DOMException {
        return this.proxyElement.getElementsByTagNameNS(namespaceURI, localName);
    }

    public boolean hasAttribute(String name) {
        return this.proxyElement.hasAttribute(name);
    }

    public boolean hasAttributeNS(String namespaceURI, String localName) throws DOMException {
        return this.proxyElement.hasAttributeNS(namespaceURI, localName);
    }

    public TypeInfo getSchemaTypeInfo() {
        return this.proxyElement.getSchemaTypeInfo();
    }

    public void setIdAttribute(String name, boolean isId) throws DOMException {
        this.proxyElement.setIdAttribute(name, isId);
    }

    public void setIdAttributeNS(String namespaceURI, String localName, boolean isId) throws DOMException {
        this.proxyElement.setIdAttributeNS(namespaceURI, localName, isId);
    }

    public void setIdAttributeNode(Attr idAttr, boolean isId) throws DOMException {
        this.proxyElement.setIdAttributeNode(idAttr, isId);
    }
}
