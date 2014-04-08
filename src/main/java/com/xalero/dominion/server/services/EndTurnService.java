package com.xalero.dominion.server.services;

import com.xalero.dominion.commons.protocol.dtos.PlayerDto;
import com.xalero.dominion.commons.protocol.dtos.PlayerGameDto;
import com.xalero.dominion.server.manager.GameManager;
import com.xalero.dominion.server.model.DominionModel;

public class EndTurnService {

	public static synchronized boolean endTurn(PlayerGameDto playerGameDto) {
		PlayerDto playerDto = playerGameDto.getPlayerDto();
		Long gameId = playerGameDto.getGameId();
		DominionModel dominionModel = GameManager.getGame(gameId);
		
		return dominionModel.endTurn(playerDto.getPlayerId()).isSuccess();
	}
}
