package com.xalero.dominion.commons.cards.action;

import com.xalero.dominion.server.model.DominionModel;
import java.util.List;

import com.xalero.dominion.commons.utils.Result;

public interface ActionBase {
	public int getPlusBuys();
	public int getPlusDraws();
	public int getPlusTreasures();
	public int getPlusActions();
	public Result playCard(List<String> parameters, DominionModel dominionModel, Long playerId);
}
