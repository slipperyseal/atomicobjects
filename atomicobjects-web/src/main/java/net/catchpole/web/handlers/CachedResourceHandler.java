package net.catchpole.web.handlers;

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

import net.catchpole.resource.ResourceCache;
import net.catchpole.web.paths.PathHandler;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Date;

public class CachedResourceHandler implements PathHandler {
    private Date lastModified = new Date();
    private ResourceCache resourceCache;
    private ServletContext servletContext;

    public CachedResourceHandler(ServletContext servletContext, ResourceCache resourceCache) {
        this.servletContext = servletContext;
        this.resourceCache = resourceCache;
    }

    public void handle(String[] path, HttpServletRequest req, HttpServletResponse res) throws IOException {
        String name = path[1];
        byte[] bytes = resourceCache.getResource(name);
        if (bytes != null) {
            String mimeType = servletContext.getMimeType(name);
            if (mimeType != null) {
                res.setHeader("Content-Type", mimeType);
            }
            // set last modified to at least the time the JVM was started - the cached content should not have changed
            res.setDateHeader("Last-Modified", lastModified.getTime());

            OutputStream os = res.getOutputStream();
            try {
                os.write(bytes);
                os.flush();
            } finally {
                os.close();
            }
        }
    }

    public long getLastModified(String[] path, HttpServletRequest req) throws IOException {
        return -1;
    }

}
