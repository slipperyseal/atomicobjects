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

import net.catchpole.web.http.HttpUtils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.Map;

public class ResponseCache {
    private Map<String,Map<String,byte[]>> cache = new HashMap<String, Map<String, byte[]>>();

    public boolean contains(HttpServletRequest req) {
        synchronized (cache) {
            Map<String,byte[]> map = cache.get(HttpUtils.getLocalPath(req));
            if (map == null) {
                return false;
            }

            return map.get(HttpUtils.getCompressionMethod(req)) != null;
        }
    }

    public OutputStream getOutputStream(HttpServletRequest req, HttpServletResponse res) throws IOException {
        String method = HttpUtils.getCompressionMethod(req);

        return HttpUtils.getCompressedOutputStream(
                new ResponseOutputStream(HttpUtils.getLocalPath(req),
                method, res), method, res);
    }

    public boolean service(HttpServletRequest req, HttpServletResponse res) throws IOException {
        byte[] data;
        String method;

        synchronized (cache) {
            Map<String,byte[]> map = cache.get(HttpUtils.getLocalPath(req));
            if (map == null) {
                return false;
            }

            method = HttpUtils.getCompressionMethod(req);
            data = map.get(method);
        }

        if (data == null) {
            return false;
        }
        service(method, data, res);
        return true;
    }

    private boolean service(String method, byte[] data, HttpServletResponse res) throws IOException {
        if (method != null) {
            res.setHeader("Vary", "Accept-Encoding");
            res.setHeader("Content-Encoding", method);
        }

        OutputStream os = res.getOutputStream();
        os.write(data);
        os.close();

        return true;
    }

    private synchronized void setResponse(String path, String compression, byte[] data) {
        synchronized (cache) {
            Map<String,byte[]> map = cache.get(path);

            if (map == null) {
                map = new HashMap<String,byte[]>();
                cache.put(path, map);
            }

            map.put(compression, data);
        }
    }

    class ResponseOutputStream extends ByteArrayOutputStream {
        private String path;
        private String compression;
        private HttpServletResponse res;

        public ResponseOutputStream(String path, String compression, HttpServletResponse res) {
            super(8192);

            ResponseOutputStream.this.path = path;
            ResponseOutputStream.this.compression = compression;
            ResponseOutputStream.this.res = res;
        }

        public void close() throws IOException {
            super.close();
            byte[] data = this.toByteArray();
            setResponse(path, compression, data);
            service(compression, data, res);
        }
    }
}
