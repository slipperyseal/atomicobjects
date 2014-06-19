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

import net.catchpole.console.telnet.ConsoleSocketHandler;
import net.catchpole.lang.Disposable;
import net.catchpole.net.SocketListener;

import java.io.IOException;
import java.net.InetAddress;
import java.util.List;

public class ConsoleSocketListener extends SocketListener implements Disposable {
    public ConsoleSocketListener(InetAddress inetAddress, int port, List<Command> commands) throws IOException {
        super(inetAddress, port, new ConsoleSocketHandler(commands));
    }

    public String toString() {
        return this.getClass().getSimpleName() + ':' + super.toString();
    }

    public void dispose() {
        super.close();
    }
}
