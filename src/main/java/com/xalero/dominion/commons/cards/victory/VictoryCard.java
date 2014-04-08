package com.xalero.dominion.commons.cards.victory;

import com.xalero.dominion.commons.cards.Card;
import com.xalero.dominion.commons.cards.CardBase;

public abstract class VictoryCard extends Card implements VictoryBase, CardBase {
	
	@Override
	public boolean isAction() {
		return false;
	}

	@Override
	public boolean isAttack() {
		return false;
	}
	
	@Override
	public boolean isReaction() {
		return false;
	}

	@Override
	public boolean isTreasure() {
		return false;
	}

	@Override
	public boolean isVictory() {
		return true;
	}
}
