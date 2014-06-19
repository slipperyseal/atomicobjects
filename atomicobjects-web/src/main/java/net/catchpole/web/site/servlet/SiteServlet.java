package net.catchpole.web.site.servlet;

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
import net.catchpole.lang.Strings;
import net.catchpole.lang.Throw;
import net.catchpole.model.BeanModel;
import net.catchpole.web.http.HttpUtils;
import net.catchpole.web.site.model.Backing;
import net.catchpole.web.site.model.Session;
import net.catchpole.web.site.model.cart.Catalog;
import net.catchpole.web.site.model.cart.Invoice;
import net.catchpole.web.site.pages.*;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

import javax.servlet.ServletConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.xml.transform.Templates;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class SiteServlet extends HttpServlet {
    private final Date startTime = new Date();
    private final ModelDocumentBuilder modelDocumentBuilder;
    private final ResponseCache responseCache = new ResponseCache();
    private final Map<String,PathHandler> pathHanderMap = new HashMap<String, PathHandler>();
    private AgentDetection agentDetection = new AgentDetection();
    private ServletContext servletContext;
    private PageSource pageSource;
    private Templates templates;
    private boolean testMode = false;

    private Catalog catalog = new Catalog();

    public SiteServlet() {
        try {
            this.modelDocumentBuilder = new ModelDocumentBuilder(new XsdBuilder());
        } catch (Exception e) {
            throw Throw.unchecked(e);
        }
    }

    public void init(ServletConfig servletConfig) throws ServletException {
        try {
            this.servletContext = servletConfig.getServletContext();
            this.pageSource = new ServletContextFileSource(servletContext);

            this.pathHanderMap.put("catalog", new CatalogHandler());
        } catch (Exception e) {
            throw Throw.unchecked(e);
        }
    }

    public void destroy() {
    }

    public void doPost(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
        doGet(req, res);
    }

    public void doGet(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
        try {
            //System.out.println(req.getRemoteAddr() + " " +  req.getRequestURI().substring(req.getContextPath().length()));
            if (!testMode && responseCache.service(req, res)) {
                return;
            }

            String[] path = getPath(req);

            Backing backing = new Backing();
            backing.setAgent(agentDetection.detectAgent(req.getHeader("User-Agent")));
            backing.setCatalog(catalog);

            getSession(req, backing);

            boolean handled = false;
            if (path.length > 0) {
                PathHandler pathHandler = pathHanderMap.get(path[0]);
                if (pathHandler != null) {
                    handled = pathHandler.handle(backing, path);
                }
            } else {
                // root path
                handled = true;
            }
            if (!handled) {
                res.setStatus(404);
            }

            boolean cachable = true;
            if (!cachable) {
                HttpUtils.noCache(res);
            }
            res.setContentType("text/html");
            render(cachable ?
                    responseCache.getOutputStream(req, res) :
                    HttpUtils.getCompressedOutputStream(req, res), backing, path);
        } catch (Exception e) {
            throw new ServletException(e);
        }
    }

    private Session getSession(HttpServletRequest req, Backing backing) {
        HttpSession httpSession = req.getSession();
        if (httpSession != null) {
            Session session = (Session)httpSession.getAttribute("session");
            if (session == null) {
                session = new Session();
                session.setInvoice(new Invoice());
                httpSession.setAttribute("session", session);
            }
            backing.setSession(session);
            return session;
        }
        return null;
    }

    public void render(OutputStream os, Object bean, String[] path) throws IOException {
        try {
            ModelDocument document = modelDocumentBuilder.newDocument(new BeanModel(bean));

            Document contentDocument = pageSource.getPage(path);
            if (contentDocument != null) {
                document.attach(contentDocument);
            }

            //new XmlTransform().printXML(document);
            try {
                Transformer transformer = buildTransfomer();
                transformer.transform(new DOMSource(document), new StreamResult(os));
            } finally {
                // very important - if this is a compressed stream, we should close it here
                os.flush();
                os.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw Throw.unchecked(e);
        }
    }

    protected long getLastModified(HttpServletRequest req) {
        return responseCache.contains(req) ? startTime.getTime() : -1;
    }

    private synchronized Transformer buildTransfomer() throws Exception {
        if (templates == null) {
            this.templates = TransformerFactory.newInstance().newTemplates(
                    new StreamSource(servletContext.getResourceAsStream("/WEB-INF/root.xsl")));
        }
        try {
            return this.templates.newTransformer();
        } finally {
            if (testMode) {
                this.templates = null;
            }
        }
    }

    private String[] getPath(HttpServletRequest req) {
        String path = HttpUtils.getLocalPath(req);
        if (path.startsWith("/")) {
            path = path.substring(1);
        }
        return Strings.tokenize(path,'/');
    }
}
