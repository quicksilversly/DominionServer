package com.xalero.dominion.server.services;

import java.net.Socket;
import java.util.ArrayList;
import java.util.Collection;

import com.xalero.dominion.commons.protocol.dtos.PlayerGameDto;
import com.xalero.dominion.server.manager.GameManager;
import com.xalero.dominion.server.model.DominionModel;
import com.xalero.dominion.server.model.Player;

public class SocketService {

	public static synchronized Collection<Socket> playerSockets(PlayerGameDto playerGameDto) {
		
		DominionModel dominionModel = GameManager.getGame(playerGameDto.getGameId());
		Collection<Player> players = dominionModel.getPlayers();
		Collection<Socket> playerSockets = new ArrayList<>();
		for (Player player: players) {
			playerSockets.add(player.getPlayerSocket());
		}
		return playerSockets;
	}
}
