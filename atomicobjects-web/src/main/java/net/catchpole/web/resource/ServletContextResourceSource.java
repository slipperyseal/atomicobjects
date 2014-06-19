package net.catchpole.web.resource;

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

import net.catchpole.resource.ResourceSource;

import javax.servlet.ServletContext;
import java.io.IOException;
import java.io.InputStream;

public class ServletContextResourceSource implements ResourceSource {
    private final ServletContext servletContext;

    public ServletContextResourceSource(ServletContext servletContext) {
        this.servletContext = servletContext;
    }
    public InputStream getResourceStream(String resource) throws IOException {
        return servletContext.getResourceAsStream(resource);
    }
}
