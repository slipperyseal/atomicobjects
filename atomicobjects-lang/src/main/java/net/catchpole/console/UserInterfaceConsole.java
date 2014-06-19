package net.catchpole.console;

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

import net.catchpole.console.telnet.AnsiWriter;
import net.catchpole.console.telnet.TelnetEvents;
import net.catchpole.console.telnet.TelnetInputStream;
import net.catchpole.io.Arrays;
import net.catchpole.lang.KeyedSource;
import net.catchpole.lang.Strings;
import net.catchpole.trace.Core;

import java.io.EOFException;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class UserInterfaceConsole {
    private Map<String, Command> commandMap;
    private Map<String, Runnable> internalCommands = new HashMap<String, Runnable>();
    private ArrayList<String> history = new ArrayList<String>();

    private TelnetInputStream telnetInputStream;
    private OutputStream outputStream;
    private AnsiWriter ansiWriter;
    private AnsiCommandInteraction ansiCommandInteraction;
    private String terminalType;
    private int width;
    private int height;

    public UserInterfaceConsole(InputStream is, OutputStream os, List<Command> commands) throws IOException {
        this.commandMap = new HashMap<String, Command>();
        for (Command command : commands) {
            this.commandMap.put(command.getClass().getSimpleName(), command);
        }

        this.outputStream = os;
        this.telnetInputStream = new TelnetInputStream(is, this.outputStream, new Events());
        this.ansiWriter = new AnsiWriter(this.outputStream, this.telnetInputStream);
        this.ansiCommandInteraction = new AnsiCommandInteraction();

        addInternalCommands();
    }

    public void dispose() {
        // todo: close this down
    }

    public void process() throws Exception {
        try {
            this.telnetInputStream.negotiate();

            //login();

            commandLoop();
        } catch (EOFException eofe) {
            // do nothing.
        }
    }

//    private void login() throws IOException {
//        Authorization authorization = application.getAuthorization();
//        ansiWriter.print("login: ");
//
//        String userName = ansiWriter.readInput(null, true);
//        ansiWriter.print("\r\npassword: ");
//
//        String password = ansiWriter.readInput(null, false);
//        ansiWriter.printLine(null);
//
//        try {
//            Identity identity = authorization.authorize(userName, password.toCharArray());
//
//            if (identity.isChangePassword()) {
//                ansiWriter.print("You must change your password: ");
//                String password1 = ansiWriter.readInput(null, false);
//                ansiWriter.print("\r\nConfirm: ");
//                String password2 = ansiWriter.readInput(null, false);
//                if (password1.equals(password2) && password1.length() > 3) {
//                    identity.setPassword(password1);
//                    identity.setChangePassword(false);
//                } else {
//                    ansiWriter.printNegative("Invalid password.", null);
//                    throw new EOFException();
//                }
//                ansiWriter.print("\r\n");
//            }
//
//            ansiWriter.printLine(null);
//            ansiWriter.printPositive(this.getClass().getSimpleName(), new Date());
//            ansiWriter.print("Welcome", identity.getName());
//        } catch (Exception e) {
//            ansiWriter.printNegative(null, e);
//            e.printStackTrace();
//            throw new EOFException();
//        }
//    }

    private void commandLoop() throws IOException {
        for (; ;) {
            ansiWriter.print("\r\n> ");
            String commandString = ansiWriter.readInput(history, true);
            ansiWriter.printLine(null);

            addToHistory(commandString);

            if (!executeInternalCommand(commandString)) {
                execute(commandString);
            }
        }
    }

    private void execute(String commandString) {
        String[] args = Strings.tokenize(commandString, ' ');
        if (args.length != 0) {
            // strip non alphanum chars and make sentence case
            String commandName = Strings.sentenceCase(args[0]);

            Command commandInstance = commandMap.get(commandName);
            // flush on return to prompt
            if (commandInstance == null) {
                ansiWriter.printNegative("Unknown command:", commandName);
                return;
            }

            execute(commandInstance, (String[]) Arrays.resizeArray(args, 1, args.length - 1));
        }
    }

    private void execute(Command command, String[] args) {
        try {
            command.handle(ansiCommandInteraction, args);
        } catch (IllegalArgumentException e) {
            ansiWriter.printNegative("Usage:", e.getMessage());
        } catch (Exception e) {
            ansiWriter.printNegative("Error:", e);
            e.printStackTrace();
        }
    }

    private void addToHistory(String commandString) {
        if (history.size() > 128) {
            history.remove(0);
        }
        history.add(commandString);
    }

    public String toString() {
        return UserInterfaceConsole.class.getName() + ' ' + terminalType + ' ' + width + 'x' + height;
    }

    /*
     * We could have just passed the command the AnsiWriter but this prevents
     * implementors casting it back to that class.
     */
    class AnsiCommandInteraction implements CommandInteraction {
        public void print(String message, Object object) {
            ansiWriter.print(message, object);
        }

        public void printNegative(String message, Object object) {
            ansiWriter.printNegative(message, object);
        }

        public void printPositive(String message, Object object) {
            ansiWriter.printPositive(message, object);
        }

        public Object choose(String message, List list, KeyedSource conversion) {
            return ansiWriter.choose(message, list, conversion);
        }
    }

    class Events implements TelnetEvents {
        public void setWindowSize(int width, int height) {
            UserInterfaceConsole.this.width = width;
            UserInterfaceConsole.this.height = height;
            Core.getTrace().info("Window sized", width, height);
        }

        public void setTerminalType(String terminalType) {
            UserInterfaceConsole.this.terminalType = terminalType;
            Core.getTrace().info("Terminal type", terminalType);
        }
    }

    private void addInternalCommands() {
        this.internalCommands.put("exit", new Exit());
        this.internalCommands.put("infoon", new Infoon());
        this.internalCommands.put("infooff", new Infooff());
    }

    private boolean executeInternalCommand(String command) {
        Runnable internalCommand = this.internalCommands.get(command);
        if (internalCommand != null) {
            internalCommand.run();
        }
        return (internalCommand != null);
    }

    class Infoon implements Runnable {
        public void run() {
            ansiWriter.setPrintClassInfo(true);
        }
    }

    class Infooff implements Runnable {
        public void run() {
            ansiWriter.setPrintClassInfo(false);
        }
    }

    class Exit implements Runnable {
        public void run() {
            throw new RuntimeException();
        }
    }
}
