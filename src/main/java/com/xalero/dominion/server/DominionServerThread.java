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

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Collection;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.google.gson.Gson;
import com.xalero.dominion.commons.protocol.DominionEvent;
import com.xalero.dominion.commons.protocol.DominionMessage;
import com.xalero.dominion.commons.protocol.dtos.BuyCardDto;
import com.xalero.dominion.commons.protocol.dtos.GameSettingsDto;
import com.xalero.dominion.commons.protocol.dtos.PlayerGameDto;
import com.xalero.dominion.commons.utils.Result;
import com.xalero.dominion.server.services.BuyCardService;
import com.xalero.dominion.server.services.CreateGameService;
import com.xalero.dominion.server.services.JoinGameService;
import com.xalero.dominion.server.services.KingdomCardService;
import com.xalero.dominion.server.services.SocketService;

/**
 *
 * @author jonathan
 */
public class DominionServerThread extends Thread {

    private final Socket socket;

    private final static Gson gson = new Gson();

    public DominionServerThread(Socket socket) {
        this.socket = socket;
    }

    @Override
    public void run() {
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream()))) {
            while (true) {
                String line = reader.readLine();
                DominionMessage request = gson.fromJson(line, DominionMessage.class);
                receiveEvent(request, socket);
            }
        } catch (IOException ex) {
        	Thread.currentThread().interrupt();
        }
    }

    private void receiveEvent(DominionMessage request, Socket socket) {
        switch (request.getEvent()) {
            case CREATE_GAME:
                GameSettingsDto gameSettings = gson.fromJson(request.getValue(), GameSettingsDto.class);
                Long gameId = CreateGameService.createGame(gameSettings);

                String jsonGameId = gson.toJson(gameId);
                DominionMessage response = new DominionMessage(DominionEvent.CREATE_GAME, jsonGameId);
                sendResponse(response, socket);
                break;
            case JOIN_GAME:
            	PlayerGameDto playerGameDto = gson.fromJson(request.getValue(), PlayerGameDto.class);
            	boolean playerAdded = JoinGameService.joinGame(playerGameDto, socket);
            	
            	String jsonPlayerAdded = gson.toJson(new Boolean(playerAdded));
            	response = new DominionMessage(DominionEvent.JOIN_GAME, jsonPlayerAdded);
            	sendResponse(response, socket);
            	break;
            case PLAY_CARD:
//                PlayCardDto playCardDto = gson.fromJson(request.getValue(), PlayCardDto.class);
                break;
            case BUY_CARD: // NEED TO TEST
                BuyCardDto buyCardDto = gson.fromJson(request.getValue(), BuyCardDto.class);
                Result result = BuyCardService.buyCard(buyCardDto);
                
                DominionMessage displayResponse = null;
                if (result.isSuccess()) {
                	displayResponse = new DominionMessage(DominionEvent.DISPLAY, 
                										  "You just bought a " + buyCardDto.getCardName());	
                	sendResponse(displayResponse, socket);
                	
                	displayResponse = new DominionMessage(DominionEvent.DISPLAY,
                										 buyCardDto.getPlayerGameDto().getPlayerDto().getName() + 
                										 " just bought a " + buyCardDto.getCardName());
                	
                	Collection<Socket> playerSockets = SocketService.playerSockets(buyCardDto.getPlayerGameDto());
                	sendResponses(displayResponse, playerSockets);
                } else {
                	displayResponse = new DominionMessage(DominionEvent.DISPLAY,
                										  "You can't buy that card");
                	sendResponse(displayResponse, socket);
                }
                break;
            case END_TURN:
//                PlayerGameDto playerGameDto = gson.fromJson(request.getValue(), PlayerGameDto.class);
//                result = dominionModel.endTurn(playerIdDto.getPlayerId());
//                resultStr = gson.toJson(result);
//                message = new DominionMessage(DominionEvent.DISPLAY, "Player Turn: " + dominionModel.getCurrentPlayer().getPlayerName());
//                dominionModel.notifyObservers(new Gson().toJson(message));
//                dominionModel.notifyObservers();
                break;
            case KINGDOM_CARD_LIST: // NEED TO TEST
            	gameId = gson.fromJson(request.getValue(), Long.class);
            	Collection<String> cards = KingdomCardService.kingdomCards(gameId);
            	response = new DominionMessage(DominionEvent.KINGDOM_CARD_LIST, gson.toJson(cards));
            	sendResponse(response, socket);
                break;
            default:
                break;
        }
    }

    private void sendResponse(DominionMessage response, Socket socket) {
        String jsonResponse = gson.toJson(response);
        try (PrintWriter pw = new PrintWriter(socket.getOutputStream(), true)) {
            pw.println(jsonResponse);
            pw.flush();
        } catch (IOException ex) {
            Logger.getLogger(DominionServerThread.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    private void sendResponses(DominionMessage response, Collection<Socket> sockets) {
    	for (Socket socket : sockets) {
    		sendResponse(response, socket);
    	}
    }
}
