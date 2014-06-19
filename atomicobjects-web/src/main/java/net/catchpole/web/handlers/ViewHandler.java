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

import net.catchpole.image.RenderedImageWriter;
import net.catchpole.lang.CurrentTime;
import net.catchpole.web.http.HttpUtils;
import net.catchpole.web.paths.PathHandler;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.OutputStream;
import java.net.InetAddress;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ViewHandler implements PathHandler {
    private ServletContext servletContext;
    private Iterable iterable;
    private Iterable detail;

    public ViewHandler(Iterable iterable, Iterable detail, ServletContext servletContext) {
        this.iterable = iterable;
        this.servletContext = servletContext;
        this.detail = detail;
    }

    public void handle(String[] path, HttpServletRequest req, HttpServletResponse res) throws IOException {
        HttpUtils.noCache(res);

        res.setContentType(servletContext.getMimeType("view.png"));

        String client = InetAddress.getByName(req.getRemoteAddr()).getHostName();

        List list = new ArrayList();
        list.addAll(Arrays.asList("Real-time view", new CurrentTime(), client));
        if (detail != null) {
            for (Object o : detail) {
                list.add(o);
            }
        }

        OutputStream os = res.getOutputStream();
        try {
            RenderedImageWriter riw = new RenderedImageWriter();
            // todo: fail!
            riw.write(null, os);
            os.flush();
        } finally {
            os.close();
        }
    }

    public long getLastModified(String[] path, HttpServletRequest req) throws IOException {
        return -1;
    }
}
