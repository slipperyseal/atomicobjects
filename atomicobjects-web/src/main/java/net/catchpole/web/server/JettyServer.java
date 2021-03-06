package net.catchpole.web.server;

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
import org.mortbay.jetty.Server;
import org.mortbay.jetty.servlet.Context;
import org.mortbay.jetty.servlet.ServletHolder;

import javax.servlet.Servlet;

public class JettyServer implements WebServer {
    private final Server server;

    public JettyServer(int port, Servlet servlet) throws Exception {
        this.server = new Server(port);
        Context root = new Context(server,"/", Context.SESSIONS);
        root.addServlet(new ServletHolder(servlet), "/*");
        this.server.start();
    }

    public void stop() {
        try {
            server.stop();
        } catch (Exception e) {
            throw Throw.unchecked(e);
        }
    }
}
