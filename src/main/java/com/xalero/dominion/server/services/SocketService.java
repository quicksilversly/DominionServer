package com.xalero.dominion.server.services;

import java.net.Socket;
import java.util.ArrayList;
import java.util.Collection;
import java.util.stream.Collectors;

import com.xalero.dominion.server.manager.GameManager;
import com.xalero.dominion.server.model.DominionModel;
import com.xalero.dominion.server.model.Player;

public class SocketService {

	public static synchronized Collection<Socket> playerSockets(Long gameId) {
		
		DominionModel dominionModel = GameManager.getGame(gameId);
		Collection<Player> players = dominionModel.getPlayers();
		Collection<Socket> playerSockets = new ArrayList<>();
		for (Player player: players) {
			playerSockets.add(player.getPlayerSocket());
		}
		return playerSockets;
	}
	
	public static synchronized Collection<Socket> playerSocketsExcept(Long gameId, 
																	  Long playerId) {
		DominionModel dominionModel = GameManager.getGame(gameId);
		Collection<Socket> playerSockets = dominionModel.getPlayers().stream()
												  			.filter(p -> p.getUniqueIdentifier() != playerId)
										  				    .map(p -> p.getPlayerSocket())
										  				    .collect(Collectors.toList());

		return playerSockets;
	}
}
