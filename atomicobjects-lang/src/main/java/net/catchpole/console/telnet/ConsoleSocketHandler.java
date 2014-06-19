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

import net.catchpole.console.Command;
import net.catchpole.console.UserInterfaceConsole;
import net.catchpole.net.SocketHandler;

import java.io.IOException;
import java.net.Socket;
import java.util.List;

public class ConsoleSocketHandler implements SocketHandler {
    private List<Command> commands;

    public ConsoleSocketHandler(List<Command> commands) {
        this.commands = commands;
    }

    public void handle(Socket socket) throws IOException {
        try {
            UserInterfaceConsole interfaceConsole = new UserInterfaceConsole(socket.getInputStream(), socket.getOutputStream(), commands);
            try {
                interfaceConsole.process();
            } catch (Exception e) {
                e.printStackTrace();
            }
        } finally {
            socket.close();
        }
    }
}

