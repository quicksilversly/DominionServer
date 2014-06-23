package com.xalero.dominion.server.services;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Collection;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.google.gson.Gson;
import com.xalero.dominion.commons.protocol.DominionEvent;
import com.xalero.dominion.commons.protocol.DominionMessage;
import com.xalero.dominion.server.PlayerConnection;

public class MessagingService {
	
	private static Gson gson = new Gson();

    public static void sendResponse(DominionEvent event, Socket socket, Object message) {
    	DominionMessage response = new DominionMessage(event, gson.toJson(message));
        sendResponse(response, socket);
    }
    
    public static void sendResponses(DominionEvent event, Collection<Socket> sockets, Object message) {
    	DominionMessage response = new DominionMessage(event, gson.toJson(message));
    	for (Socket socket : sockets) {
    		sendResponse(response, socket);
    	}
    }
    
    private static void sendResponse(DominionMessage response, Socket socket) {
    	String jsonResponse = gson.toJson(response);
    	try (PrintWriter pw = new PrintWriter(socket.getOutputStream(), true)) {
    		pw.println(jsonResponse);
    		pw.flush();
    	} catch (IOException ex) {
    		Logger.getLogger(PlayerConnection.class.getName()).log(Level.SEVERE, null, ex);
    	}
    }
}
