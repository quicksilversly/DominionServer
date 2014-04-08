package com.xalero.dominion.commons.cards.treasure;

import com.xalero.dominion.commons.cards.Card;
import com.xalero.dominion.commons.cards.CardBase;


public abstract class TreasureCard extends Card implements TreasureBase, CardBase {
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
		return true;
	}

	@Override
	public boolean isVictory() {
		return false;
	}
}
