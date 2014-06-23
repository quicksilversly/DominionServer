package com.xalero.dominion.commons.cards;

public enum CardSet {
	RANDOM ("Random"),
	CUSTOM ("Custom");
	
	private String displayName;
	
	private CardSet(String displayName) {
		this.displayName = displayName;
	}
	
	@Override
	public String toString() {
		return displayName;
	}
}
