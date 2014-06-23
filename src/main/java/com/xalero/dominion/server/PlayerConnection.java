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
import java.net.Socket;
import java.nio.ByteBuffer;
import java.nio.channels.AsynchronousSocketChannel;
import java.nio.channels.CompletionHandler;
import java.util.Collection;

import com.google.gson.Gson;
import com.xalero.dominion.commons.protocol.DominionEvent;
import com.xalero.dominion.commons.protocol.DominionMessage;
import com.xalero.dominion.commons.protocol.dtos.BuyCardDto;
import com.xalero.dominion.commons.protocol.dtos.GameSettingsDto;
import com.xalero.dominion.commons.protocol.dtos.PlayCardDto;
import com.xalero.dominion.commons.protocol.dtos.PlayerGameDto;
import com.xalero.dominion.commons.utils.Result;
import com.xalero.dominion.server.services.BuyCardService;
import com.xalero.dominion.server.services.CreateGameService;
import com.xalero.dominion.server.services.EndTurnService;
import com.xalero.dominion.server.services.JoinGameService;
import com.xalero.dominion.server.services.KingdomCardService;
import com.xalero.dominion.server.services.MessagingService;
import com.xalero.dominion.server.services.PlayCardService;
import com.xalero.dominion.server.services.SocketService;

/**
 *
 * @author jonathan
 */
public class PlayerConnection {

    private final AsynchronousSocketChannel channel;

    private final static Gson gson = new Gson();

    public PlayerConnection(AsynchronousSocketChannel channel) {
        this.channel = channel;
    }

    public void run() {
    	ByteBuffer input = ByteBuffer.allocate(256);
    	if (!channel.isOpen()) {
    		return;
    	}
    	
    	CompletionHandler<Integer, ? super ByteBuffer> completionHandler = new CompletionHandler<Integer, ByteBuffer>() {

			@Override
			public void completed(Integer result, ByteBuffer attachment) {
				
			}

			@Override
			public void failed(Throwable exc, ByteBuffer attachment) {
				// TODO Auto-generated method stub
				
			}
    		
    	};
    	
    	channel.read(input, input, completionHandler);
//        try (BufferedReader reader = new BufferedReader(new InputStreamReader(channel.getInputStream()))) {
//            while (true) {
//                String line = reader.readLine();
//                DominionMessage request = gson.fromJson(line, DominionMessage.class);
//                receiveEvent(request, channel);
//            }
//        } catch (IOException ex) {
//        	Thread.currentThread().interrupt();
//        }
    }

    private void receiveEvent(DominionMessage request, AsynchronousSocketChannel channel) {
        switch (request.getEvent()) {
            case CREATE_GAME:
                GameSettingsDto gameSettings = gson.fromJson(request.getValue(), GameSettingsDto.class);
                long gameId = CreateGameService.createGame(gameSettings);
                
                Collection<Socket> playerSockets = null;
                
                MessagingService.sendResponse(DominionEvent.CREATE_GAME, channel, gameId);
                break;
            case JOIN_GAME:
            	PlayerGameDto playerGameDto = gson.fromJson(request.getValue(), PlayerGameDto.class);
            	long playerId = JoinGameService.joinGame(playerGameDto, channel);
            	
            	MessagingService.sendResponse(DominionEvent.JOIN_GAME, channel, playerId);
            	break;
            case PLAY_CARD: // TODO NEED TO TEST
                PlayCardDto playCardDto = gson.fromJson(request.getValue(), PlayCardDto.class);
                Result result = PlayCardService.playCard(playCardDto);
                
                MessagingService.sendResponse(DominionEvent.PLAY_CARD, channel, result);
                break;
            case BUY_CARD: // TODO NEED TO TEST
                BuyCardDto buyCardDto = gson.fromJson(request.getValue(), BuyCardDto.class);
                result = BuyCardService.buyCard(buyCardDto);
                
//                if (result.isSuccess()) {
//                	Collection<Socket> playerSockets = SocketService.playerSockets(buyCardDto.getPlayerGameDto().getGameId());
                	
//                	MessagingService.sendResponse(DominionEvent.DISPLAY, 
//                								  socket, 
//                								  "You just bought a " + buyCardDto.getCardName());
//                	MessagingService.sendResponses(DominionEvent.DISPLAY, 
//                								   playerSockets, 
//                								   buyCardDto.getPlayerGameDto().getPlayerDto().getName() + 
//                								   " just bought a " + buyCardDto.getCardName());
//                } else {
//                	MessagingService.sendResponse(DominionEvent.DISPLAY, socket, "You can't buy that card");
//                }
                break;
            case END_TURN: // TODO NEED TO TEST
            	playerGameDto = gson.fromJson(request.getValue(), PlayerGameDto.class);
            	boolean turnEnded = EndTurnService.endTurn(playerGameDto);
            	
            	if (turnEnded) {
            		playerSockets = SocketService.playerSocketsExcept(playerGameDto.getGameId(), playerGameDto.getPlayerDto().getPlayerId());
//            		MessagingService.sendResponse(DominionEvent.END_TURN, 
//            									  socket,
//            									  );
//            		MessagingService.sendResponses(DominionEvent.DISPLAY, 
//            									   playerSockets, 
//            									   playerGameDto.getPlayerDto().getName() + " just ended their turn");
            	} else {
//            		MessagingService.sendResponse(DominionEvent.DISPLAY, socket, "You cannot end your turn");
            	}
                break;
            case KINGDOM_CARD_LIST: // TODO NEED TO TEST
            	gameId = gson.fromJson(request.getValue(), Long.class);
            	Collection<String> cards = KingdomCardService.kingdomCards(gameId);
            	MessagingService.sendResponse(DominionEvent.KINGDOM_CARD_LIST, channel, cards);
                break;
            default:
                break;
        }
    }

    /**
     * Closes the channel
     */
    public void close() {
        try {
            channel.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
