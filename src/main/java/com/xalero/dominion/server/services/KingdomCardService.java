package com.xalero.dominion.server.services;

import java.util.ArrayList;
import java.util.Collection;

import com.xalero.dominion.commons.cards.action.KingdomCard;
import com.xalero.dominion.server.manager.GameManager;
import com.xalero.dominion.server.model.DominionModel;

public class KingdomCardService {

	public static synchronized Collection<String> kingdomCards(Long gameId) {
		DominionModel dominionModel = GameManager.getGame(gameId);
		Collection<KingdomCard> kingdomCards = dominionModel.getKingdomCards();
		Collection<String> kingdomCardsString = new ArrayList<>();
		for (KingdomCard kingdomCard : kingdomCards) {
			kingdomCardsString.add(kingdomCard.toString());
		}
		return kingdomCardsString;
	}
}
