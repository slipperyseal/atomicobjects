package net.catchpole.web.http;

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

import net.catchpole.lang.Strings;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.OutputStream;
import java.util.zip.GZIPOutputStream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

public final class HttpUtils {
    private HttpUtils() {
    }

    public static String getLocalPath(HttpServletRequest req) {
        return req.getRequestURI().substring(req.getContextPath().length());
    }

    /**
     * Converts the path of a request to a String array.
     * <p/>
     * <p>eg. /project/dir/blah  becomes ["project","dir","blah"]
     *
     * @param req
     * @return
     */
    public static String[] pathToArray(HttpServletRequest req) {
        String pathInfo = req.getPathInfo();

        if (pathInfo == null) {
            return null;
        }

        if (pathInfo.startsWith("/")) {
            pathInfo = pathInfo.substring(1);
        }
        if (pathInfo.endsWith("/")) {
            pathInfo = pathInfo.substring(0, pathInfo.length() - 1);
        }

        return Strings.tokenize(pathInfo, '/');
    }

    public static void noCache(HttpServletResponse res) {
        res.setHeader("Cache-Control", "no-cache");  // HTTP 1.1
        res.setHeader("Pragma", "no-cache");         // HTTP 1.0
        res.setDateHeader("Expires", 0);           // prevents caching at the proxy server
    }

    /**
     * Based on a HttpServletRequest determines if the output stream for the HttpServletResponse
     * can be compressed, wrapping the appropriate compressor, else returns the uncompressed OutputStream.
     * <p/>
     * <p>The caller must remember to close the stream to correctly flush the compressed data.
     * The use of a try/finally block is suggested.
     *
     * @param req
     * @param res
     * @return
     * @throws java.io.IOException
     */
    public static OutputStream getCompressedOutputStream(HttpServletRequest req, HttpServletResponse res) throws IOException {
        OutputStream os = res.getOutputStream();

        getCompressedOutputStream(os, getCompressionMethod(req), res);

        return os;
    }

    public static String getCompressionMethod(HttpServletRequest req) {
        String encodings = req.getHeader("Accept-Encoding");
        if (encodings != null && encodings.contains("gzip")) {
            return "gzip";
        }
        if (encodings != null && encodings.contains("compress")) {
            return "x-compress";
        }
        return null;
    }

    public static OutputStream getCompressedOutputStream(OutputStream os, String method, HttpServletResponse res) throws IOException {
        if ("x-compress".equals(method)) {
            res.setHeader("Vary", "Accept-Encoding");
            ZipOutputStream zout = new ZipOutputStream(os);
            zout.putNextEntry(new ZipEntry("noname"));
            return zout;
        }

        if ("gzip".equals(method)) {
            res.setHeader("Vary", "Accept-Encoding");
            return new GZIPOutputStream(os);
        }

        return os;
    }

    public static String normalizePath(String path) {
        // use while loop in case "////" replaced as "//"
        while (path.contains("//")) {
            path = path.replaceAll("//","/");
        }
        // never allow .. which could become a relative directory path
        while (path.contains("..")) {
            path = path.replaceAll("..",".");
        }
        if (path.endsWith("/")) {
            path = path.substring(path.length()-1);
        }
        return path;
    }
}
