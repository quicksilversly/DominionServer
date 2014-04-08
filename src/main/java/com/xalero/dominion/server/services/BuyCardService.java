package com.xalero.dominion.server.services;

import com.xalero.dominion.commons.cards.Card;
import com.xalero.dominion.commons.cards.CardFactory;
import com.xalero.dominion.commons.protocol.dtos.BuyCardDto;
import com.xalero.dominion.commons.protocol.dtos.PlayerGameDto;
import com.xalero.dominion.commons.utils.Result;
import com.xalero.dominion.server.manager.GameManager;
import com.xalero.dominion.server.model.DominionModel;
import com.xalero.dominion.server.model.Player;

public class BuyCardService {

	public static synchronized Result buyCard(BuyCardDto buyCardDto) {
		PlayerGameDto playerGameDto = buyCardDto.getPlayerGameDto();
		Long playerId = playerGameDto.getPlayerDto().getPlayerId();
		Long gameId = playerGameDto.getGameId();
		
		DominionModel dominionModel = GameManager.getGame(gameId);
		Player player = dominionModel.getPlayerById(playerId);
		
		Card cardToBuy = CardFactory.createCard(buyCardDto.getCardName());
		return player.canBuyCard(cardToBuy);
	}
}
