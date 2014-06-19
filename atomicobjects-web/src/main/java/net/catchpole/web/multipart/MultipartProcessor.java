package net.catchpole.web.multipart;

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

import net.catchpole.io.*;
import net.catchpole.lang.Disposable;
import net.catchpole.lang.Strings;
import net.catchpole.lang.Throw;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.Map;

public class MultipartProcessor implements Disposable {
    private static final String NEWLINE = new String( new byte[]{ 0x0d, 0x0a } );
    private static final String DASHDASH = "--";
    private static final int MAXLINELEN = 512;
    private Map<String,Part> parts = new HashMap<String,Part>();

    public MultipartProcessor(HttpServletRequest request) throws ServletException, IOException {
        this(request, true);
    }

    public MultipartProcessor(HttpServletRequest request, boolean useDisk) throws ServletException, IOException {
        this(request, useDisk ? new TemporaryFileSource() : new TemporaryMemorySource());
    }

    public MultipartProcessor(HttpServletRequest request, InputOutputStreamSource store) throws ServletException, IOException {
        String contentType = request.getContentType();
        if (contentType == null || contentType.indexOf("multipart/") == -1) {
            // no parts in request
            return;
        }

        Map<String, String> contentMap = getPropertyMap(contentType);
        String boundary = DASHDASH + contentMap.get("boundary");
        byte[] boundarySeek = (NEWLINE + boundary).getBytes();

        InputStream is = request.getInputStream();
        String line1 = readLine(is);
        if (!boundary.equals(line1)) {
            throw new IOException("initial boundary not found: expected: [" +  boundary + "] got [" + line1 + "]");
        }

        Map<String, Map<String, String>> map = new HashMap<String, Map<String, String>>();

        String line;
        while ((line = readLine(is)) != null) {
            int colon;
            if ((colon = line.indexOf(':')) != -1) {
                map.put(line.substring(0, colon).toLowerCase(),
                        getPropertyMap(line));
            }
            // content begins after blank line
            if (line.length() == 0) {
                InputOutputStream ioStream = store.getInputOutputStream();

                OutputStream os = ioStream.getOutputStream();
                try {
                    Arrays.spoolUntilBoundary(is, os, boundarySeek);
                    os.close();
                } finally {
                    os.close();
                }

                Map<String,String> properties = map.get("content-disposition");
                if (properties != null) {
                    String name = properties.get("name");
                    Part part = new Part(name, properties.get("filename"), ioStream);
                    parts.put(name, part);
                }

                map.clear();
                // EOL or -- after boundary
                line = readLine(is);
                if (line == null) {
                    break;
                }
            }
        }
    }

    public boolean hasParts() {
        return parts.size() > 0;
    }

    public Map<String,Part> getParts() {
        return parts;
    }

    public String getProperty(String name) {
        try {
            Part part = parts.get(name);
            if (part != null) {
                byte[] bytes = part.getBytes();
                if (bytes != null) {
                    return new String(bytes);
                }
            }
        } catch (IOException ioe) {
            Throw.unchecked(ioe);
        }
        return null;
    }

    public void dispose() {
        for (Part part : parts.values()) {
            part.dispose();
        }
    }

    private String readLine(InputStream is) throws IOException {
        StringBuffer sb = new StringBuffer(128);
        int read = 0;
        int c;
        while ((c = is.read()) != -1) {
            if (c == '\n') {
                System.out.println("READ: " + sb.toString());
                return sb.toString();
            }
            if (c != '\r') {
                sb.append((char) c);
            }
            if (read++ > MAXLINELEN) {
                throw new IOException("Line length exceeded " + MAXLINELEN);
            }
        }
        return null;
    }

    private Map<String, String> getPropertyMap(String line) {
        Map<String, String> map = new HashMap<String, String>();

        int colon = line.indexOf(':');
        if (colon != -1) {
            line = line.substring(colon + 1);
        }
        String[] pairs = Strings.tokenize(line, ';');
        for (String pair : pairs) {
            String[] nameValue = Strings.tokenize(pair, '=');
            if (nameValue.length == 2) {
                map.put(nameValue[0], nameValue[1].replace('\"', ' ').trim());
            }
        }

        return map;
    }
}

/*

This is what multi-part looks like....



contentType=multipart/form-data; boundary=---------------------------458581354580075952123557074



-----------------------------458581354580075952123557074
Content-Disposition: form-data; name="userfile1"; filename="filename1"
Content-Type: application/octet-stream

[DATA GOES HERE]
-----------------------------458581354580075952123557074
Content-Disposition: form-data; name="userfile2"; filename="filename2"
Content-Type: application/octet-stream

[DATA GOES HERE]
-----------------------------458581354580075952123557074--


*/
