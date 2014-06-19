package net.catchpole.web.log;

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

import javax.servlet.http.HttpServletRequest;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Represents Web Log entries which can be output in standard HTTP log format.
 * 10.1.1.1 - - [08/Feb/2004:05:37:49 -0800] "GET /index.htm HTTP/1.1" 200 2758 "http://catchpole.net/" "Mozilla/4.0 (compatible; MSIE 6.0; Windows 98; YPC 3.0.2)"
 */
public class WebLogEntry {
    private static final SimpleDateFormat dateFormatter = new SimpleDateFormat("dd/MMM/yyyy:HH:mm:ss Z");

    private String remoteHost;
    private String clientUser;
    private String authenticatedUser;
    private Date date;
    private String request;
    private int statusCode;
    private long bytes;
    private String referrer;
    private String userAgent;

    public WebLogEntry(String remoteHost, String clientUser, String authenticatedUser, Date date, String request, int statusCode, long bytes, String referrer, String userAgent) {
        this.remoteHost = remoteHost;
        this.clientUser = clientUser;
        this.authenticatedUser = authenticatedUser;
        this.date = date;
        this.request = request;
        this.statusCode = statusCode;
        this.bytes = bytes;
        this.referrer = referrer;
        this.userAgent = userAgent;
    }

    public WebLogEntry(HttpServletRequest req, int statusCode, long bytes) {
        this.remoteHost = req.getRemoteAddr();
        this.clientUser = req.getRemoteUser();
        this.authenticatedUser = null;
        this.date = new Date();
        this.request = req.getMethod() + ' ' + req.getQueryString() + ' ' + req.getProtocol();
        this.statusCode = statusCode;
        this.bytes = bytes;
        this.referrer = req.getHeader("Referer");
        this.userAgent = req.getHeader("Identity-Agent");
    }

    public String getRemoteHost() {
        return remoteHost;
    }

    public String getClientUser() {
        return clientUser;
    }

    public String getAuthenticatedUser() {
        return authenticatedUser;
    }

    public Date getDate() {
        return date;
    }

    public String getRequest() {
        return request;
    }

    public int getStatusCode() {
        return statusCode;
    }

    public long getBytes() {
        return bytes;
    }

    public String getReferrer() {
        return referrer;
    }

    public String getUserAgent() {
        return userAgent;
    }

    public String toString() {
        return remoteHost + ' ' +
                (clientUser != null ? clientUser : '-') + ' ' +
                (authenticatedUser != null ? authenticatedUser : '-') + ' ' +
                '[' + formatDate(date) + ']' + ' ' +
                '"' + request + '"' + ' ' +
                statusCode + ' ' +
                bytes + ' ' +
                (referrer != null ? '"' + referrer + '"' : '-') + ' ' +
                (userAgent != null ? '"' + userAgent + '"' : '-');
    }

    private static synchronized String formatDate(Date date) {
        return dateFormatter.format(date);
    }
}
