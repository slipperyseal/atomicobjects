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

import net.catchpole.io.Arrays;
import net.catchpole.web.http.HttpUtils;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.URLConnection;

/**
 * A Servlet which allows mapping of directories to be served as static content.
 * Allows static content to be placed within application paths.  eg.  http://site/app/static/index.html
 * Supports last modified date and text compression.
 */

public class FileServlet extends HttpServlet {
    private File directory;

    public void init(ServletConfig servletConfig) throws ServletException {
        try {
            this.directory = new File(
                    servletConfig.getServletContext().getRealPath("."),
                    servletConfig.getInitParameter("directory"));
        } catch (Exception e) {
            throw new ServletException(e);
        }
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String path = request.getPathInfo();
        // prevent going above directory root
        if (!isLegalPath(path)) {
            response.sendError(404);
            return;
        }
        File file = determineFile(path);
        if (file == null) {
            response.sendError(404);
            return;
        }

        long lastModified = file.lastModified();
        if (lastModified <= request.getDateHeader("If-Modified-Since")) {
            response.setStatus(HttpServletResponse.SC_NOT_MODIFIED);
        } else {
            response.setDateHeader("Last-Modified", lastModified);
            String contentType = URLConnection.guessContentTypeFromName(file.getName());
            response.setContentType(contentType);
            // compress if text
            OutputStream os = contentType.startsWith("text/") ?
                    HttpUtils.getCompressedOutputStream(request, response) :
                    response.getOutputStream();
            try {
                FileInputStream fis = new FileInputStream(file);
                try {
                    Arrays.spool(fis, os);
                } finally {
                    fis.close();
                }
            } finally {
                os.close();
            }
        }
    }

    /**
     * Determines the real file, if any, to be served.  Directory paths
     * may be mapped to an index.html file or similar.
     *
     * @param path
     * @return returns null if no file can be found for this path.
     */
    private File determineFile(String path) {
        File file = new File(this.directory, path);
        if (file.isDirectory()) {
            File index = new File(file, "index.html");
            if (index.exists() && !file.isDirectory()) {
                return index;
            } else {
                return null;
            }
        }
        if (!file.exists()) {
            return null;
        }
        return file;
    }

    /**
     * Determines if the path contains any characters or sequences which
     * could be used to breach the root directory path.
     * ie.  .. \ or any character below 33 or above 127
     *
     * @param path
     * @return
     */

    private boolean isLegalPath(String path) {
        if (path.indexOf("..") != -1 ||
                path.indexOf('\\') != -1) {
            return false;
        }

        int l = path.length();
        for (int x = 0; x < l; x++) {
            char c = path.charAt(x);
            if (c < 33 || c > 127) {
                return false;
            }
        }

        return true;
    }
}
