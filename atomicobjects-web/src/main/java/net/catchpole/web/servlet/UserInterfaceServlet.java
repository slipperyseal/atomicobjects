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

import net.catchpole.fuse.Junction;
import net.catchpole.fuse.JunctionBox;
import net.catchpole.lang.Throw;
import net.catchpole.resource.ClasspathResourceSource;
import net.catchpole.resource.ResourceSource;
import net.catchpole.web.handlers.ImageHandler;
import net.catchpole.web.paths.PathResolver;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;

/**
 */
public class UserInterfaceServlet extends HttpServlet {
    private final Junction junction = new JunctionBox();
    private final PathResolver pathResolver = new PathResolver();
    private final ResourceSource resourceSource = new ClasspathResourceSource(this.getClass());

    public UserInterfaceServlet() {
    }

    public void init(ServletConfig servletConfig) throws ServletException {
        this.pathResolver.addHandler("image", new ImageHandler(
                new File(servletConfig.getInitParameter("ImageHandler.image.dir")),
                new File(servletConfig.getInitParameter("ImageHandler.scaled.dir"))));
//        this.pathResolver.addDefaultHandler(new BeanPageHandler(resourceSource, new HashMap()));
//        this.pathResolver.addHandler("view", new ViewHandler(iterable, null, this.servletContext));
//        this.pathResolver.addHandler("list", new ListHandler(iterable));
//        this.pathResolver.addHandler("resources", new CachedResourceHandler(servletContext, new ResourceCache(resourceSource)));
//        this.pathResolver.addHandler("bean", new BeanPageHandler(new FileResourceSource(new File(".")), iterable));
    }

    public void dispose() {
    }

    public void destroy() {
    }

    public void doPost(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
        doGet(req, res);
    }

    public void doGet(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
        try {
            serve(req, res);
        } catch (Exception e) {
            throw new ServletException(e);
        }
    }

    protected long getLastModified(HttpServletRequest req) {
        try {
            return this.pathResolver.getLastModified(req);
        } catch (IOException ioe) {
            throw Throw.unchecked(ioe);
        }
    }

    private void serve(HttpServletRequest req, HttpServletResponse res) throws Exception {
        this.pathResolver.handle(req, res);
    }
}
