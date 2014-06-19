package net.catchpole.web.servlet;

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

import net.catchpole.dom.ModelDocument;
import net.catchpole.dom.ModelDocumentBuilder;
import net.catchpole.dom.builder.XsdBuilder;
import net.catchpole.dom.transform.XmlTransform;
import net.catchpole.lang.Throw;
import net.catchpole.model.Model;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.OutputStream;

public class WebServiceServlet extends HttpServlet {
    private Model model;

    public WebServiceServlet(Model model) {
        this.model = model;
    }

    public void doPost(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
        doGet(req, res);
    }

    public void doGet(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
        res.setContentType("text/xml");
        outputModel(model, res.getOutputStream());
    }

    private void outputModel(Model model, OutputStream os) {
        try {
            ModelDocumentBuilder modelDocumentBuilder = new ModelDocumentBuilder(new XsdBuilder());
            ModelDocument modelDocument = modelDocumentBuilder.newDocument(model);
            new XmlTransform().printXML(modelDocument, os);
        } catch (Exception e) {
            throw Throw.unchecked(e);
        }
    }
}
