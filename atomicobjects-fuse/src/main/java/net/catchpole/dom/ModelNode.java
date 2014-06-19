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
import org.w3c.dom.CharacterData;
import org.w3c.dom.DOMException;
import org.w3c.dom.Document;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.w3c.dom.Text;
import org.w3c.dom.UserDataHandler;

import java.util.ArrayList;
import java.util.List;

public class ModelNode implements Node, CharacterData {
    protected ModelDocument modelDocument;
    protected final Node proxyNode;
    protected final Model backingModel;
    protected final ModelNode parentNode;
    protected List<Node> nodeList;

    public ModelNode(ModelDocument modelDocument, ModelNode parentNode, String text) {
        this(modelDocument, parentNode, null, modelDocument.createTextNode(text));
    }

    public ModelNode(ModelDocument modelDocument, ModelNode parentNode, Model backingModel) {
        this(modelDocument, parentNode, backingModel,
                modelDocument.createElement(modelDocument.getElementBuilder().getName(backingModel)));
    }

    public ModelNode(ModelDocument modelDocument, ModelNode parentNode, Model backingModel, Node proxyNode) {
        this.modelDocument = modelDocument;
        this.parentNode = parentNode;
        this.backingModel = backingModel;
        this.proxyNode = proxyNode;
    }

    private void resolveNodeList() {
        if (nodeList == null && backingModel != null) {
            nodeList = new ArrayList<Node>();
            for (Model eachModel : backingModel) {
                nodeList.add(new ModelElement(modelDocument, this, eachModel));
            }
        }
    }

    public void attachSingleNode(Node node) {
        resolveNodeList();
        nodeList.add(node);
    }

    public void addTextNode(String text) {
        if (nodeList == null) {
            nodeList = new ArrayList<Node>();
        }
        nodeList.add(new ModelNode(modelDocument, this, text));
    }

    public String toString() {
        return this.getClass().getSimpleName() + ' ' + backingModel;
    }

    // node methods

    public String getNodeName() {
        return this.proxyNode.getNodeName();
    }

    public String getNodeValue() throws DOMException {
        return this.proxyNode.getNodeValue();
    }

    public void setNodeValue(String nodeValue) throws DOMException {
        this.proxyNode.setNodeValue(nodeValue);
    }

    public short getNodeType() {
        return this.proxyNode.getNodeType();
    }

    public Node getParentNode() {
        return this.parentNode;
    }

    public NodeList getChildNodes() {
        resolveNodeList();
        return new NodeList() {
            public Node item(int index) {
                return nodeList.get(index);
            }

            public int getLength() {
                return nodeList.size();
            }
        };
    }

    public Node getFirstChild() {
        resolveNodeList();
        return nodeList.size() > 0 ? nodeList.get(0) : null;
    }

    public Node getLastChild() {
        resolveNodeList();
        return nodeList.size() > 0 ? nodeList.get(nodeList.size() - 1) : null;
    }

    public Node getPreviousSibling() {
        List<Node> list = this.parentNode.nodeList;
        int l = list.size();
        for (int x = 0; x < l; x++) {
            if (list.get(x) == this) {
                if (x > 0) {
                    return list.get(x - 1);
                }
            }
        }
        return null;
    }

    public Node getNextSibling() {
        List<Node> list = this.parentNode.nodeList;
        int l = list.size();
        for (int x = 0; x < l; x++) {
            if (list.get(x) == this) {
                if (x < (l - 1)) {
                    return list.get(x + 1);
                }
            }
        }
        return null;
    }

    public NamedNodeMap getAttributes() {
        return this.proxyNode.getAttributes();
    }

    public Document getOwnerDocument() {
        return this.proxyNode.getOwnerDocument();
    }

    public Node insertBefore(Node newChild, Node refChild) throws DOMException {
        return this.proxyNode.insertBefore(newChild, refChild);
    }

    public Node replaceChild(Node newChild, Node oldChild) throws DOMException {
        return this.proxyNode.replaceChild(newChild, oldChild);
    }

    public Node removeChild(Node oldChild) throws DOMException {
        return this.proxyNode.removeChild(oldChild);
    }

    public Node appendChild(Node newChild) throws DOMException {
        return this.proxyNode.appendChild(newChild);
    }

    public boolean hasChildNodes() {
        resolveNodeList();
        return this.nodeList.size() != 0;
    }

    public Node cloneNode(boolean deep) {
        return this.proxyNode.cloneNode(deep);
    }

    public void normalize() {
        this.proxyNode.normalize();
    }

    public boolean isSupported(String feature, String version) {
        return this.proxyNode.isSupported(feature, version);
    }

    public String getNamespaceURI() {
        return this.proxyNode.getNamespaceURI();
    }

    public String getPrefix() {
        return this.proxyNode.getPrefix();
    }

    public void setPrefix(String prefix) throws DOMException {
        this.proxyNode.setPrefix(prefix);
    }

    public String getLocalName() {
        return this.proxyNode.getLocalName();
    }

    public boolean hasAttributes() {
        return this.proxyNode.hasAttributes();
    }

    public String getBaseURI() {
        return this.proxyNode.getBaseURI();
    }

    public short compareDocumentPosition(Node other) throws DOMException {
        return this.proxyNode.compareDocumentPosition(proxyNode);
    }

    public String getTextContent() throws DOMException {
        return this.proxyNode.getTextContent();
    }

    public void setTextContent(String textContent) throws DOMException {
        this.proxyNode.setTextContent(textContent);
    }

    public boolean isSameNode(Node other) {
        return this.proxyNode.isSameNode(other);
    }

    public String lookupPrefix(String namespaceURI) {
        return this.proxyNode.lookupPrefix(namespaceURI);
    }

    public boolean isDefaultNamespace(String namespaceURI) {
        return this.proxyNode.isDefaultNamespace(namespaceURI);
    }

    public String lookupNamespaceURI(String prefix) {
        return this.proxyNode.lookupNamespaceURI(prefix);
    }

    public boolean isEqualNode(Node arg) {
        return this.proxyNode.isEqualNode(arg);
    }

    public Object getFeature(String feature, String version) {
        return this.proxyNode.getFeature(feature, version);
    }

    public Object setUserData(String key, Object data, UserDataHandler handler) {
        return this.proxyNode.setUserData(key, data, handler);
    }

    public Object getUserData(String key) {
        return this.proxyNode.getUserData(key);
    }

    //


    public String getData() throws DOMException {
        return ((Text)proxyNode).getData();
    }

    public void setData(String s) throws DOMException {
        ((Text)proxyNode).setData(s);
    }

    public int getLength() {
        return getData().length();
    }

    public String substringData(int i, int i1) throws DOMException {
        return getData().substring(i,i1);
    }

    public void appendData(String s) throws DOMException {
        setData(getData()+s);
    }

    public void insertData(int i, String s) throws DOMException {
        throw new IllegalArgumentException("screw you buddy");
    }

    public void deleteData(int i, int i1) throws DOMException {
        throw new IllegalArgumentException("screw you buddy");
    }

    public void replaceData(int i, int i1, String s) throws DOMException {
        throw new IllegalArgumentException("screw you buddy");
    }
}
