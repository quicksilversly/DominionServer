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
import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.net.StandardSocketOptions;
import java.nio.channels.AsynchronousChannelGroup;
import java.nio.channels.AsynchronousServerSocketChannel;
import java.nio.channels.AsynchronousSocketChannel;
import java.nio.channels.CompletionHandler;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

/**
 *
 * @author jonathan
 */
public class DominionServer {

	private final List<PlayerConnection> connections;
    private final int port;
    private final AsynchronousServerSocketChannel listener;
    private final AsynchronousChannelGroup channelGroup;
    private boolean running;

    public DominionServer(int port) throws IOException{
    	this.connections = Collections.synchronizedList(new ArrayList<PlayerConnection>());
        this.port = port;
        this.running = true;
        this.channelGroup = AsynchronousChannelGroup.withFixedThreadPool(Runtime.getRuntime().availableProcessors(),
                Executors.defaultThreadFactory());
        this.listener = createListener(channelGroup);
    }

    /**
     * Starts accepting connections
     */
    public void startServer() {
        running = true;
        
        listener.accept(null, new CompletionHandler<AsynchronousSocketChannel, Void>() {
        	@Override
        	public void completed(AsynchronousSocketChannel playerSocket, Void attachment) {
        		listener.accept(null, this);
        		handleNewConnection(playerSocket);
        	}
        	
        	@Override
        	public void failed(Throwable exception, Void attachment) { }
        });
    }

    /**
     * Stops the server
     * @throws InterruptedException 
     * @throws IOException 
     */
    public void stopServer() throws InterruptedException, IOException {
    	channelGroup.shutdownNow();
    	channelGroup.awaitTermination(1, TimeUnit.SECONDS);
        running = false;
    }

    /**
     * Creates a listener and starts accepting connections
     * 
     * @param channelGroup
     * @return
     * @throws IOException
     */
    private AsynchronousServerSocketChannel createListener(AsynchronousChannelGroup channelGroup) throws IOException {
    	final AsynchronousServerSocketChannel listener = openChannel(channelGroup);
        listener.setOption(StandardSocketOptions.SO_REUSEADDR, true);
        listener.bind(new InetSocketAddress(port));
        return listener;
    }
    
    private AsynchronousServerSocketChannel openChannel(AsynchronousChannelGroup channelGroup) throws IOException {
        return AsynchronousServerSocketChannel.open(channelGroup);
    }
    
    /**
     * Creates a new client and adds it to the list of connections.
     * 
     * @param channel the newly accepted channel
     */
    private void handleNewConnection(AsynchronousSocketChannel channel) {
    	PlayerConnection playerConnection = new PlayerConnection(channel);
    	try {
    		channel.setOption(StandardSocketOptions.SO_KEEPALIVE, new Boolean(true));
    	} catch (Exception e) {
    		// ignore
    	}
    	connections.add(playerConnection);
    	playerConnection.run();
    }

    /**
    *
    * @return The socket address that the server is bound to
    * @throws java.io.IOException if an I/O error occurs
    */
	public SocketAddress getSocketAddress() throws IOException {
		return listener.getLocalAddress();
	}
    
	/**
	 * @return a boolean of whether or not the server is currently running
	 */
	public boolean isRunning() {
		return running;
	}
}
