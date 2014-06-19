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

import net.catchpole.console.CommandInteraction;
import net.catchpole.lang.KeyedSource;
import net.catchpole.lang.Strings;
import net.catchpole.lang.Throw;

import java.io.OutputStream;
import java.io.PrintWriter;
import java.util.List;

public class HttpInteraction implements CommandInteraction {
    private PrintWriter writer;

    public HttpInteraction(OutputStream os) {
        this.writer = new PrintWriter(os);
    }

    public void begin() {
        // todo: replace with XSLT
        this.writer.print("<html><head><title>catchpole.net</title></head><body bgcolor=\"#ffffff\" text=\"#000000\"><center><table cellspacing=\"1\" cellpadding=\"2\" bgcolor=\"#eeeeee\">");
    }

    public void end() {
        this.writer.println("</table></center><br></body></html>");
        this.writer.flush();
    }

    public void print(String message, Object object) {
        print(message, object, "normal");
    }

    public void printNegative(String message, Object object) {
        print(message, object, "negative");
    }

    public void printPositive(String message, Object object) {
        print(message, object, "positive");
    }

    public Object choose(String message, List list, KeyedSource conversion) {
        return null;
    }

    private void print(String message, Object object, String style) {
        try {
            // message
            this.writer.print("<tr><td><font face=\"arial\" size=\"2\">");
            if (message != null) {
                this.writer.print(message);
            }
            this.writer.println("</font></td>");

            // possible class name
            this.writer.print("<td><font face=\"arial\" size=\"2\">");
            if (object != null && !(object instanceof String)) {
                this.writer.print(Strings.smartClassName(object));
            }
            this.writer.println("</font></td>");

            // object to string
            this.writer.print("<td class=\"");
            this.writer.print(style);
            this.writer.print("\"><font face=\"arial\" size=\"2\">");
            if (object != null) {
                this.writer.print(Strings.smartToString(object));
            }
            this.writer.println("</font></td></tr>");
        } catch (Exception e) {
            throw Throw.unchecked(e);
        }
    }
}
