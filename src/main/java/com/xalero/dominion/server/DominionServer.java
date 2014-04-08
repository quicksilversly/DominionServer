/*
 * Copyright 2014 jonathan.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.xalero.dominion.server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author jonathan
 */
public class DominionServer {

    private final int port;
    private boolean running;
    private Thread serverThread;

    public DominionServer(int port) {
        this.port = port;
        this.running = true;
    }

    public DominionServer(String port) {
        this.port = Integer.parseInt(port);
        this.running = true;
    }

    public void startServer() {
        running = true;
        try (ServerSocket serverSocket = new ServerSocket(port)) {
            while (running) {
                Socket socket = serverSocket.accept();
                socket.setKeepAlive(true);
                new DominionServerThread(socket).start();
            }
        } catch (IOException ex) {
            Logger.getLogger(DominionServer.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void stopServer() {
        running = false;
    }
}
