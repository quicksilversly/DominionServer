package com.xalero.dominion.commons.cards;

public interface CardBase {
	public int getCost();
	public boolean isAction();
	public boolean isAttack();
	public boolean isReaction();
	public boolean isTreasure();
	public boolean isVictory();
	public boolean isInteractive();
}
