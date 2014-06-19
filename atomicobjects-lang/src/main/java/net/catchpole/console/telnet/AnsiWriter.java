package net.catchpole.console.telnet;

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

import java.io.EOFException;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.util.List;

public class AnsiWriter extends PrintWriter implements CommandInteraction {
    private static final int ALLATTOFF = 0;
    private static final int BRIGHT = 1;
    private static final int UNDERLINE = 4;
    private static final int BLINK = 5;
    private static final int REVERSE = 7;
    private static final int RED = 31;
    private static final int GREEN = 32;
    private static final int YELLOW = 33;
    private static final int BLUE = 34;
    private static final int MAGENTA = 35;
    private static final int CYAN = 36;
    private static final int WHITE = 37;

    private InputStream inputStream;
    private boolean printClassInfo = true;
    private int indent = 20;

    public AnsiWriter(OutputStream outputStream, InputStream inputStream) {
        super(outputStream);
        this.inputStream = inputStream;
    }

    public void print(String message, Object object) {
        printDescriptive(message, object);
        super.flush();
    }

    public void printNegative(String message, Object object) {
        colorOn(RED);
        printDescriptive(message, object);
        colorOff();
        super.flush();
    }

    public void printPositive(String message, Object object) {
        colorOn(YELLOW);
        printDescriptive(message, object);
        colorOff();
        super.flush();
    }

    public Object choose(String message, List list, KeyedSource conversion) {
        if (message != null) {
            print(message, null);
        }
        try {
            int x = 1;
            for (Object choice : list) {
                if (conversion != null) {
                    choice = conversion.get(choice);
                }
                print(Integer.toString(x++), choice);
            }
            print("\r\n? ");
            String input = readInput(null, true);
            printLine(null);
            return list.get(Integer.parseInt(input) - 1);

        } catch (Exception e) {
        }
        throw new IllegalArgumentException();
    }

    public boolean isPrintClassInfo() {
        return printClassInfo;
    }

    public void setPrintClassInfo(boolean printClassInfo) {
        this.printClassInfo = printClassInfo;
    }

    public void write(char c) {
        super.write(c);
        super.flush();
    }

    public void backSpace(int spaces) {
        for (int i = 0; i < spaces; i++) {
            super.print((char) 8);
            super.print((char) 32);
            super.print((char) 8);
        }
        super.flush();
    }

    public void printLine(String text) {
        if (text != null) {
            super.println(text);
        } else {
            super.println();
        }
        super.flush();
    }

    public void print(String text) {
        if (text != null) {
            super.print(text);
        }
        super.flush();
    }

    public void printColor(String text, int color) {
        if (text != null) {
            colorOn(color);
            super.print(text);
            colorOff();
        }
    }

    public void colorOn(int color) {
        super.print((char) 27);
        super.print('[');
        super.print(1);
        super.print(';');
        super.print(color);
        super.print('m');
    }

    public void colorOff() {
        super.print((char) 27);
        super.print('[');
        super.print(0);
        super.print('m');
    }

    private void printDescriptive(String message, Object object) {
        if (message != null) {
            super.print(message);
        }
        int l = message != null ? message.length() : 0;
        while (l < indent) {
            super.print(' ');
            l++;
        }
        if (object != null) {
            super.print(' ');
            if (object instanceof Throwable) {
                // PRINT ERROR CONDITION
                colorOn(RED);
                super.print(object.getClass().getName());
                super.print(' ');
                colorOn(BLUE);
                super.print(((Throwable) object).getMessage());
                colorOff();
            } else {
                // PRINT OBJECT WITH OPTIONAL CLASS INFO
                if (printClassInfo && !(object instanceof String)) {
                    colorOn(CYAN);
                    super.print(Strings.smartSimpleClassName(object));
                    super.print(' ');
                }
                colorOn(BLUE);
                super.print(Strings.smartToString(object));
                colorOff();
            }
        }
        super.println();
        super.flush();
    }

    public String readInput(List history, boolean echo) throws IOException {
        StringBuilder text = new StringBuilder(256);

        for (; ;) {
            int i = inputStream.read();
            if (i == -1) {
                throw new EOFException();
            }

            char c = (char) i;
            switch (c) {
                case 27:
                    if (inputStream.read() != '[')
                        break;
                    int e = inputStream.read();
                    switch (e) {
                        case 'A':
                            if (history != null && history.size() > 0) {
                                String com = (String) history.get(history.size() - 1);
                                if (echo) {
                                    // remove anything typed already
                                    this.backSpace(text.length());
                                    // replace buffer text
                                    text.setLength(0);
                                    text.append(com);
                                    // display historic text
                                    this.print(com);
                                }
                            }
                            break;
                    }
                    break;
                case 10:
                    break;
                case 13:
                    return text.toString();
                case 8: // backspace
                    int l = text.length();
                    if (l > 0) {
                        text.setLength(l - 1);
                        if (echo) {
                            this.backSpace(1);
                        }
                    }
                    break;
                default:
                    // only add char to buffer if it's not a control char and the buffer inputStream not full
                    if (c >= 32 && text.length() < text.capacity()) {
                        text.append(c);
                        if (echo) {
                            this.write(c);
                        }
                    }
                    break;
            }
        }
    }
}

