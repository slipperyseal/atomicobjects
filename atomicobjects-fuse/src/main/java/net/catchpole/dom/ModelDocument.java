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
import org.w3c.dom.*;

public class ModelDocument extends ModelNode implements Document, Element {
    private final Document proxyDocument;
    private final Element rootElement;
    private final ElementBuilder elementBuilder;

    public ModelDocument(Document proxyDocument, ElementBuilder elementBuilder, Model model) {
        super(null, null, model,
                proxyDocument.createElement(elementBuilder.getName(model)));

        this.modelDocument = this;
        this.proxyDocument = proxyDocument;
        this.elementBuilder = elementBuilder;
        this.rootElement = new ModelElement(this.modelDocument, this, model);
    }

    public void attach(Document sourceDocument) {
        Element root1 = sourceDocument.getDocumentElement();

        Node importedNode = proxyDocument.importNode(root1, true);
        Node child = proxyDocument.getFirstChild();

//        Element root2 = proxyDocument.getDocumentElement();
//        root2.appendChild(importedNode);
        attachSingleNode(root1);
    }


    public Document getProxyDocument() {
        return proxyDocument;
    }

    public ElementBuilder getElementBuilder() {
        return elementBuilder;
    }

    public DocumentType getDoctype() {
        return this.modelDocument.getDoctype();
    }

    public DOMImplementation getImplementation() {
        return this.modelDocument.getImplementation();
    }

    public Element getDocumentElement() {
        return this.rootElement;
    }

    public Element createElement(String tagName) throws DOMException {
        return this.proxyDocument.createElement(tagName);
    }

    public DocumentFragment createDocumentFragment() {
        throw new UnsupportedOperationException();
    }

    public Text createTextNode(String data) {
        return this.proxyDocument.createTextNode(data);
    }

    public Comment createComment(String data) {
        return this.proxyDocument.createComment(data);
    }

    public CDATASection createCDATASection(String data) throws DOMException {
        throw new UnsupportedOperationException();
    }

    public ProcessingInstruction createProcessingInstruction(String target, String data) throws DOMException {
        throw new UnsupportedOperationException();
    }

    public Attr createAttribute(String name) throws DOMException {
        throw new UnsupportedOperationException();
    }

    public EntityReference createEntityReference(String name) throws DOMException {
        throw new UnsupportedOperationException();
    }

    public NodeList getElementsByTagName(String tagname) {
        throw new UnsupportedOperationException();
    }

    public Node importNode(Node importedNode, boolean deep) throws DOMException {
        return this.proxyDocument.importNode(importedNode, deep);
    }

    public Element createElementNS(String namespaceURI, String qualifiedName) throws DOMException {
        return this.modelDocument.createElementNS(namespaceURI, qualifiedName);
    }

    public Attr createAttributeNS(String namespaceURI, String qualifiedName) throws DOMException {
        return this.modelDocument.createAttributeNS(namespaceURI, qualifiedName);
    }

    public NodeList getElementsByTagNameNS(String namespaceURI, String localName) {
        return this.modelDocument.getElementsByTagNameNS(namespaceURI, localName);
    }

    public Element getElementById(String elementId) {
        return this.modelDocument.getElementById(elementId);
    }

    public String getInputEncoding() {
        return this.modelDocument.getInputEncoding();
    }

    public String getXmlEncoding() {
        return this.modelDocument.getXmlEncoding();
    }

    public boolean getXmlStandalone() {
        return this.modelDocument.getXmlStandalone();
    }

    public void setXmlStandalone(boolean xmlStandalone) throws DOMException {
        this.modelDocument.setXmlStandalone(xmlStandalone);
    }

    public String getXmlVersion() {
        return this.modelDocument.getXmlVersion();
    }

    public void setXmlVersion(String xmlVersion) throws DOMException {
        this.modelDocument.setXmlVersion(xmlVersion);
    }

    public boolean getStrictErrorChecking() {
        return this.modelDocument.getStrictErrorChecking();
    }

    public void setStrictErrorChecking(boolean strictErrorChecking) {
        this.modelDocument.setStrictErrorChecking(strictErrorChecking);
    }

    public String getDocumentURI() {
        return this.modelDocument.getDocumentURI();
    }

    public void setDocumentURI(String documentURI) {
        this.modelDocument.setDocumentURI(documentURI);
    }

    public Node adoptNode(Node source) throws DOMException {
        return this.modelDocument.adoptNode(source);
    }

    public DOMConfiguration getDomConfig() {
        return this.modelDocument.getDomConfig();
    }

    public void normalizeDocument() {
        this.modelDocument.normalizeDocument();
    }

    public Node renameNode(Node n, String namespaceURI, String qualifiedName) throws DOMException {
        return this.modelDocument.renameNode(n, namespaceURI, qualifiedName);
    }

    // ELEMENT


    public String getTagName() {
        return this.rootElement.getTagName();
    }

    public String getAttribute(String s) {
        return this.rootElement.getAttribute(s);
    }

    public void setAttribute(String s, String s1) throws DOMException {
        this.rootElement.setAttribute(s, s1);
    }

    public void removeAttribute(String s) throws DOMException {
        this.rootElement.removeAttribute(s);
    }

    public Attr getAttributeNode(String s) {
        return this.rootElement.getAttributeNode(s);
    }

    public Attr setAttributeNode(Attr attr) throws DOMException {
        return this.rootElement.setAttributeNode(attr);
    }

    public Attr removeAttributeNode(Attr attr) throws DOMException {
        return this.rootElement.removeAttributeNode(attr);
    }

    public String getAttributeNS(String s, String s1) throws DOMException {
        return this.rootElement.getAttributeNS(s, s1);
    }

    public void setAttributeNS(String s, String s1, String s2) throws DOMException {
        this.rootElement.setAttributeNS(s,s1,s2);
    }

    public void removeAttributeNS(String s, String s1) throws DOMException {
        this.rootElement.removeAttributeNS(s,s1);
    }

    public Attr getAttributeNodeNS(String s, String s1) throws DOMException {
        return this.rootElement.getAttributeNodeNS(s, s1);
    }

    public Attr setAttributeNodeNS(Attr attr) throws DOMException {
        return this.rootElement.setAttributeNodeNS(attr);
    }

    public boolean hasAttribute(String s) {
        return this.rootElement.hasAttribute(s);
    }

    public boolean hasAttributeNS(String s, String s1) throws DOMException {
        return this.rootElement.hasAttributeNS(s, s1);
    }

    public TypeInfo getSchemaTypeInfo() {
        return this.rootElement.getSchemaTypeInfo();
    }

    public void setIdAttribute(String s, boolean b) throws DOMException {
        this.rootElement.setIdAttribute(s,b);
    }

    public void setIdAttributeNS(String s, String s1, boolean b) throws DOMException {
        this.rootElement.setIdAttributeNS(s,s1,b);
    }

    public void setIdAttributeNode(Attr attr, boolean b) throws DOMException {
        this.rootElement.setIdAttributeNode(attr,b);
    }
}
