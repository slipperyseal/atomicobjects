package net.catchpole.trace;

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

import java.io.PrintStream;
import java.util.Date;

public class PrintTrace implements Trace {
    private final int indent;
    private final PrintStream out;
    private boolean printDate = false;
    private boolean caller = true;

    public PrintTrace(PrintStream out) {
        this.out = out;
        this.indent = 0;
    }

    private PrintTrace(PrintTrace parent, int indent) {
        this.out = parent.out;
        this.indent = indent;
    }

    public Trace drill() {
        return new PrintTrace(this, indent + 1);
    }

    public Trace drill(Object object) {
        info(object);
        return drill();
    }

    private void start(Object object) {
        for (int x = 0; x < indent; x++) {
            out.print("  ");
        }
        if (printDate) {
            out.print(new Date().toString());
            out.print(' ');
        }
        if (caller) {
            out.print(whereAmI(new Throwable()));
            out.print(' ');
        }
        if (object != null) {
            out.print(object);
        }
    }

    public void info(Object object) {
        start(object);
        out.println();
    }

    public void info(Object... list) {
        start(null);
        for (Object obj : list) {
            out.print(' ');
            if (obj instanceof Object[]) {
                out.print('[');
                Object[] oa = (Object[]) obj;
                boolean first = true;
                for (Object element : oa) {
                    if (!first) {
                        out.print(',');
                    }
                    if (element != null) {
                        String string = element.toString();
                        if (string.length() == 0) {
                            string = "\"\"";
                        }
                        element = string;
                    }
                    out.print(element);
                    first = false;
                }
                out.print(']');
                out.print(oa.length);
            } else {
                out.print(obj);
            }
        }
        out.println();
    }

    public void error(Object object) {
        info(object);
    }

    public void error(Object... list) {
        info(list);
    }

    public void warning(Object object) {
        info(object);
    }

    public void warning(Object... list) {
        info(list);
    }

    public void critical(Object object) {
        info(object);
    }

    public void critical(Object... list) {
        info(list);
    }

    private String whereAmI(Throwable throwable) {
        for (StackTraceElement ste : throwable.getStackTrace()) {
            String className = ste.getClassName();
            // search stack for first element not within this class
            if (!className.equals(this.getClass().getName())) {
                int dot = className.lastIndexOf('.');
                if (dot != -1) {
                    className = className.substring(dot + 1);
                }
                return className + '.' + ste.getMethodName();
            }
        }
        return "";
    }

    public void demote() {

    }

    public void promote() {

    }
}
