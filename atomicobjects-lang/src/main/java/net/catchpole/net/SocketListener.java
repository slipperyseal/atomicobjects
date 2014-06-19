package net.catchpole.net;

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

import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;

public class SocketListener {
    private ServerSocket serverSocket;
    private SocketHandler socketHandler;

    public SocketListener(InetAddress address, int port, SocketHandler socketHandler) throws IOException {
        this.socketHandler = socketHandler;
        this.serverSocket = (address == null ? new ServerSocket(port) : new ServerSocket(port, 10, address));
        new Thread(new Dispatcher()).start();
    }

    public synchronized void close() {
        try {
            serverSocket.close();
        } catch (Exception e) {
        }
    }

    public InetAddress getAddress() {
        return serverSocket.getInetAddress();
    }

    public int getPort() {
        return serverSocket.getLocalPort();
    }

    /**
     * The Dispatcher thread accepts and dispatches Spawn threads, which in turn process the sockets.
     */
    private class Dispatcher implements Runnable {
        public void run() {
            try {
                // loop forever dispatching sockets. an exception will be thrown if the server socket closes.
                for (; ;) {
                    new Thread(new SocketThread(serverSocket.accept())).start();
                }
            } catch (Exception ioe) {
            }
        }
    }

    private class SocketThread implements Runnable {
        private Socket socket;

        public SocketThread(Socket socket) {
            this.socket = socket;
        }

        public void run() {
            try {
                socketHandler.handle(socket);
            } catch (Throwable t) {
                t.printStackTrace();
            }
        }
    }

    public String toString() {
        return this.getClass().getName() + ' ' + serverSocket.getInetAddress() + ':' + serverSocket.getLocalPort() + '>' + socketHandler.getClass().getName();
    }
}

