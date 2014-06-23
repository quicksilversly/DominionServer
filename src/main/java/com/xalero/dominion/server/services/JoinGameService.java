package com.xalero.dominion.server.services;

import java.net.Socket;

import com.xalero.dominion.commons.model.PlayerType;
import com.xalero.dominion.commons.protocol.dtos.PlayerDto;
import com.xalero.dominion.commons.protocol.dtos.PlayerGameDto;
import com.xalero.dominion.server.manager.GameManager;
import com.xalero.dominion.server.model.DominionModel;

public class JoinGameService {

	public static synchronized long joinGame(PlayerGameDto playerGameDto, Socket playerSocket) {
		DominionModel dominionModel = GameManager.getGame(playerGameDto
				.getGameId());
		PlayerDto playerDto = playerGameDto.getPlayerDto();

		int maxHumanPlayers = dominionModel.getMaxHumanPlayers();
		int maxCompPlayers = dominionModel.getMaxCompPlayers();
		int numPlayers = dominionModel.getNumPlayers();

		boolean gameStarted = dominionModel.gameStarted();

		if (gameStarted || (numPlayers >= maxHumanPlayers + maxCompPlayers)) {
			return -1;
		}
		if ((playerDto.getPlayerType().equals(PlayerType.COMPUTER) && 
				dominionModel.getNumCompPlayers() >= maxCompPlayers) || 
			(playerDto.getPlayerType().equals(PlayerType.HUMAN) && 
				dominionModel.getNumHumanPlayers() >= maxHumanPlayers)) {
			return -1;
		}

		return dominionModel.addPlayer(playerDto, playerSocket); 
	}
}
