package net.catchpole.web.site.pages;

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

import net.catchpole.lang.Throw;
import org.w3c.dom.Document;

import javax.servlet.ServletContext;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.IOException;
import java.io.InputStream;

public class ServletContextFileSource implements PageSource {
    private final String[] rootPath =  new String[] { "root" };
    private final DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
    private final ServletContext servletContext;

    public ServletContextFileSource(ServletContext servletContext) {
        this.servletContext = servletContext;
    }

    public Document getPage(String[] path) {
        if (path == null || path.length == 0) {
            path = rootPath;
        }

        StringBuilder sb = new StringBuilder("/WEB-INF/content");
        for (String element : path) {
            sb.append('/');
            sb.append(element);
        }
        sb.append(".xml");

        try {
            InputStream is = servletContext.getResourceAsStream(sb.toString());
            if (is != null) {
                try {
                    return dbf.newDocumentBuilder().parse(is);
                } finally {
                    try {
                        is.close();
                    } catch (IOException ioe) {
                    }
                }
            }
        } catch (Exception e) {
            Throw.unchecked(e);
        }
        return null;
    }
}
