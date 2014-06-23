package com.xalero.dominion.server.services;

import java.util.ArrayList;

import com.xalero.dominion.commons.cards.Card;
import com.xalero.dominion.commons.cards.CardFactory;
import com.xalero.dominion.commons.cards.action.ActionBase;
import com.xalero.dominion.commons.protocol.dtos.PlayCardDto;
import com.xalero.dominion.commons.protocol.dtos.PlayerDto;
import com.xalero.dominion.commons.utils.Result;
import com.xalero.dominion.server.manager.GameManager;
import com.xalero.dominion.server.model.DominionModel;

public class PlayCardService {

	public static synchronized Result playCard(PlayCardDto playCardDto) {
		PlayerDto playerDto = playCardDto.getPlayerGameDto().getPlayerDto();
		Long gameId = playCardDto.getPlayerGameDto().getGameId();
		Card card = CardFactory.createCard(playCardDto.getCardName());
		DominionModel dominionModel = GameManager.getGame(gameId);
		
		return ((ActionBase)card).playCard(new ArrayList<>(playCardDto.getParameters()), dominionModel, playerDto.getPlayerId());
	}
}
