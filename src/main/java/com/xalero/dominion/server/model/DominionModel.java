package com.xalero.dominion.server.model;

import java.net.Socket;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Random;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.xalero.dominion.commons.cards.Card;
import com.xalero.dominion.commons.cards.CardFactory;
import com.xalero.dominion.commons.cards.action.KingdomCard;
import com.xalero.dominion.commons.cards.treasure.Treasures;
import com.xalero.dominion.commons.cards.victory.VictoryCards;
import com.xalero.dominion.commons.model.PlayerType;
import com.xalero.dominion.commons.protocol.DominionEvent;
import com.xalero.dominion.commons.protocol.DominionMessage;
import com.xalero.dominion.commons.protocol.dtos.GameSettingsDto;
import com.xalero.dominion.commons.protocol.dtos.PlayerDto;
import com.xalero.dominion.commons.utils.IUniqueObservable;
import com.xalero.dominion.commons.utils.IUniqueObserver;
import com.xalero.dominion.commons.utils.Result;

public final class DominionModel implements IUniqueObservable {

    private final Map<Long, IUniqueObserver> observers;
    private Treasures treasureCards;
    private VictoryCards victoryCards;
    private List<KingdomCard> kingdomCards;
    private List<Card> trash;
    private List<Player> players;
    private List<Card> curses;
    private boolean[] playersRespondedToCard;
    private final int maxHumanPlayers;
    private final int maxCompPlayers;
    private int playerTurn;
    private boolean gameStarted;
    private boolean gameOver;
    private final long gameId;

    private final int START_COPPER_COUNT = 7;
    private final int START_ESTATE_COUNT = 3;

    private final int TWO_PLAYER_CURSE_COUNT = 10;
    private final int THREE_PLAYER_CURSE_COUNT = 20;
    private final int FOUR_PLAYER_CURSE_COUNT = 30;

    /**
     * @param gameSettingsDto - The settings object contains all of the
     * necessary information to create and start a game.
     */
    public DominionModel(GameSettingsDto gameSettingsDto) {
        observers = new HashMap<>();
        players = new ArrayList<>();
        maxHumanPlayers = gameSettingsDto.getNumHumanPlayers();
        maxCompPlayers = gameSettingsDto.getNumCompPlayers();
        playerTurn = -1;
        gameStarted = false;
        gameOver = false;
        gameId = new Random().nextLong();

        initSupplyPile(gameSettingsDto.getGameCards(), gameSettingsDto.getNumCompPlayers() + gameSettingsDto.getNumHumanPlayers());
        initKingdomCards(gameSettingsDto.getGameCards(), gameSettingsDto.getNumCompPlayers() + gameSettingsDto.getNumHumanPlayers());
    }
    
    /**
     * @return the number of computer players in the game
     */
    public int getMaxCompPlayers() {
    	return maxCompPlayers;
    }
    
    /**
     * @return the number of human players in the game
     */
    public int getMaxHumanPlayers() {
    	return maxHumanPlayers;
    }

    /**
     * @return the number of players in the game
     */
    public int getNumPlayers() {
    	return players.size();
    }
    
    /**
     * @return the number of computer players currently in the game
     */
    public int getNumCompPlayers() {
    	int compPlayers = 0;
    	for (Player player : players) {
    		if (player.getPlayerType().equals(PlayerType.COMPUTER)) {
    			compPlayers++;
    		}
    	}
    	return compPlayers;
    }
    
    /**
     * @return the number of human players currently in the game
     */
    public int getNumHumanPlayers() {
    	int humanPlayers = 0;
    	for (Player player : players) {
    		if (player.getPlayerType().equals(PlayerType.HUMAN)) {
    			humanPlayers++;
    		}
    	}
    	return humanPlayers;
    }
    
    /**
     * @return a long representing a unique identifier for the game
     */
    public long getGameId() {
        return gameId;
    }

    /**
     * @return Whether or not the game has started.
     */
    public boolean gameStarted() {
        return gameStarted;
    }

    /**
     * @return a boolean of whether the game is over or not.
     */
    public boolean gameOver() {
        return gameOver;
    }

    /**
     * Ends the game.
     */
    private void endGame() {
        setPlayerTurn(-1);

        gameOver = true;
    }

    /**
     * Starts the game: determines the starting player, deals the cards...
     */
    public Result startGame() {
        Result result = new Result(true, "");

		// Random random = new Random();
        // setPlayerTurn(random.nextInt(players.size()));
        setPlayerTurn(0); // for testing
        players.get(playerTurn).turnStarted();

        dealCards();

        gameStarted = true;
        result.setMessage("Game started!");
        notifyObservers();

        return result;
    }

    /**
     * Deals out the standard 7 coppers and 3 estates to each player and then
     * shuffles their decks.
     */
    private void dealCards() {
        for (Player player : players) {
            for (int i = 0; i < START_COPPER_COUNT; i++) {
                player.addToDeck(treasureCards.getCopper());
            }
            for (int i = 0; i < START_ESTATE_COUNT; i++) {
                player.addToDeck(CardFactory.estate);
            }
            player.shuffleDeck();
            player.draw(5);
//			notifyObserver(player.getUniqueIdentifier())
        }
        notifyObservers();
    }

    /**
     * Depending on the cards chosen for the game and number of players, the
     * supply pile will be created accordingly. The supply pile will always
     * consist of gold, copper, silver, provinces, duchies, estates, and the
     * trash. Depending on whether the witch is in the game, curses will be be
     * added to the supply pile.
     *
     * @param selectedCards - The cards chosen for the game.
     * @param playerCount - the number of players in the game.
     */
    private void initSupplyPile(Collection<String> selectedCards, int playerCount) {
        victoryCards = new VictoryCards(playerCount);
        treasureCards = new Treasures();
        trash = new LinkedList<>();

        initCurses(selectedCards, playerCount);
    }

    /**
     * If a witch is in the game curses will be added to the supply depending on
     * the number of players in the game.
     *
     * @param cards - the cards chosen for the game
     * @param playerCount - the number of players in the game
     */
    private void initCurses(Collection<String> cards, int playerCount) {
        curses = new LinkedList<>();
        for (String cardName : cards) {
            Card card = CardFactory.createCard(cardName);
            if (card.equals(CardFactory.witch)) {
                if (playerCount == 2) {
                    for (int i = 0; i < TWO_PLAYER_CURSE_COUNT; i++) {
                        curses.add(CardFactory.curse);
                    }
                } else if (playerCount == 3) {
                    for (int i = 0; i < THREE_PLAYER_CURSE_COUNT; i++) {
                        curses.add(CardFactory.curse);
                    }
                } else {
                    for (int i = 0; i < FOUR_PLAYER_CURSE_COUNT; i++) {
                        curses.add(CardFactory.curse);
                    }
                }
                break;
            }
        }
    }

    /**
     * Initializes the game with the kingdom cards from the game settings
     * object.
     *
     * @param cards - The list of kingdom cards for the game.
     */
    private void initKingdomCards(Collection<String> cards, int playerCount) {
        kingdomCards = new LinkedList<>();
        for (String cardName : cards) {
            Card card = CardFactory.createCard(cardName);
            this.kingdomCards.add(new KingdomCard(card, playerCount));
        }
    }

    /**
     * Sets the player's turn for the beginning of the game.
     *
     * @param playerTurn - The index of the player in the list.
     */
    private void setPlayerTurn(int playerTurn) {
        this.playerTurn = playerTurn;
    }

    /**
     * @return The index of the current player's turn.
     */
    public int getPlayerTurn() {
        return playerTurn;
    }

    /**
     * @return The collection of players.
     */
    public Collection<Player> getPlayers() {
        return Collections.unmodifiableCollection(players);
    }

    /**
     * @param index - index of the player desired.
     * @return a player object.
     */
    public Player getPlayer(int index) {
        return players.get(index);
    }

    /**
     * @return The player whose turn it is
     */
    public Player getCurrentPlayer() {
        if (playerTurn < 0 || players.size() == 0
                || playerTurn >= players.size()) {
            return null;
        }
        return players.get(playerTurn);
    }

    /**
     * Adds a player to the current game.
     * @param playerDto the basic information for creating a player
     * @param playerSocket the socket used to communicate with the player
     * @return the player id
     */
    public long addPlayer(PlayerDto playerDto, Socket playerSocket) {
    	Player playerToAdd = new Player(playerDto, players.size() + 1, playerSocket);
    	if (players.add(playerToAdd)) {
    		return playerToAdd.getUniqueIdentifier();
    	}
    	return -1;
    }
    
    /**
     * Trashes a card from the game.
     *
     * @param card - the card to be trashed.
     */
    public void addToTrash(Card card) {
        trash.add(card);
    }

    /**
     * @return a collection of the curse cards in the game.
     */
    public Collection<Card> getCurses() {
        return Collections.unmodifiableCollection(curses);
    }

    /**
     * Draws a curse card to give to a player.
     *
     * @return A curse card.
     */
    public Card drawCurse() {
        Card curse = null;
        if (curses.size() > 0) {
            curse = curses.remove(0);
        }
        return curse;
    }

    /**
     * @return a collection of the trashed cards in the game.
     */
    public Collection<Card> getTrashCards() {
        return Collections.unmodifiableCollection(trash);
    }

    /**
     * @return the victory cards in the game.
     */
    public VictoryCards getVictoryCards() {
        return victoryCards;
    }

    /**
     * @return a collection of the kingdom cards.
     */
    public Collection<KingdomCard> getKingdomCards() {
        return Collections.unmodifiableCollection(kingdomCards);
    }

    /**
     * @return the treasures in the game.
     */
    public Treasures getTreasures() {
        return treasureCards;
    }

    /**
     * Check to see if a kingdom card is in the game.
     *
     * @param card - card to see if it is in the game.
     * @return true if the kingdom card is in the game, false otherwise.
     */
    public boolean kingdomCardInGame(Card card) {
        for (KingdomCard kingdomCard : kingdomCards) {
            if (kingdomCard.equals(card) && kingdomCard.getCardCount() > 0) {
                return true;
            }
        }
        return false;
    }

    /**
     * Used to get a kingdom card that is in the game
     *
     * @param card
     * @return
     */
    public KingdomCard getKingdomCard(Card card) {
        for (int i = 0; i < kingdomCards.size(); i++) {
            if (kingdomCards.get(i).equals(card)) {
                return kingdomCards.get(i);
            }
        }
        return null;
    }

    public Result buyCard(Long playerId, String cardName) {
        Card card = CardFactory.createCard(cardName);
        return buyCard(playerId, card);
    }

    /**
     * Buys a card for the player if the player is able to do so. Some
     * preventions include a player can't buy the card or it isn't the player's
     * turn.
     *
     * @param playerId The id of the player that is buying a card
     * @param card The card the player wants to buy
     * @return a result object
     */
    public Result buyCard(Long playerId, Card card) {
        Result result = new Result(false, "You can't buy that card");
        if (!gameStarted) {
            result.setMessage("Game has not yet started");
            return result;
        }

        Player player = getPlayerById(playerId);

        // if (players.get(playerTurn).equals(player)) {
        result = player.canBuyCard(card);
        if (result.isSuccess()) {
            if (kingdomCardInGame(card)) {
                KingdomCard kingdomCard = getKingdomCard(card);
                player.buyCard(kingdomCard.drawCard());
                result.setMessage("You just bought a " + card);
            } else if (treasureCards.contains(card)) {
                player.buyCard(treasureCards.getTreasure(card));
                result.setMessage("You just bought a " + card);
            } else if (victoryCards.contains(card)) {
                player.buyCard(victoryCards.getVictoryCard(card));
                result.setMessage("You just bought a " + card);
            } else {
                result.setMessage("That card doesn't exist");
            }
        }
		// } else {
        // result.setMessage("not your turn");
        //
        return result;
    }

    /**
     * Retrieves the discard pile of the player
     *
     * @param playerIndex the index of the player
     * @return an unmodifiable collection of the player's discard pile
     */
    public Collection<Card> getDiscardPile(int playerIndex) {
        return Collections.unmodifiableCollection(players.get(playerIndex)
                .getDiscardPile());
    }

    /**
     * Goes through the piles and looks for three missing piles or one empty
     * province pile.
     *
     * @return true if the game is over, false otherwise.
     */
    private boolean isGameOver() {
        if (victoryCards.getProvinceCount() == 0) {
            return true;
        }
        int pilesGone = 0;
        if (treasureCards.getCopperCount() == 0) {
            pilesGone++;
        }
        if (treasureCards.getSilverCount() == 0) {
            pilesGone++;
        }
        if (treasureCards.getGoldCount() == 0) {
            pilesGone++;
        }
        if (curses.size() == 0) {
            pilesGone++;
        }
        if (victoryCards.getEstateCount() == 0) {
            pilesGone++;
        }
        if (victoryCards.getDuchyCount() == 0) {
            pilesGone++;
        }

        for (KingdomCard kingdomCard : kingdomCards) {
            if (kingdomCard.getCardCount() == 0) {
                pilesGone++;
            }
        }

        if (pilesGone > 2) {
            return true;
        }
        return false;
    }

    /**
     * Ends a player's turn
     *
     * @param playerId the id of the player
     * @return a result object.
     */
    public Result endTurn(Long playerId) {

        // check for ending turn
        Player curPlayer = players.get(playerTurn);

        curPlayer.turnEnded();

        StringBuilder resultMessage = new StringBuilder(
                curPlayer.getPlayerName() + " has ended their turn.");
        playerTurn = ++playerTurn % players.size();
        curPlayer = players.get(playerTurn);
        curPlayer.turnStarted();

        if (isGameOver()) {
            resultMessage.append("Game Over!");
            endGame();
        } else {
            resultMessage.append("\nPlayer Turn: " + curPlayer.getPlayerName());
        }

        return new Result(true, resultMessage.toString());
    }

    /**
     * Retrieves the player by the player's id
     *
     * @param playerId the long representing the player id
     * @return a player object or null if htere is
     */
    public Player getPlayerById(Long playerId) {
        for (Player player : players) {
            if (playerId == player.getUniqueIdentifier()) {
                return player;
            }
        }
        return null;
    }

    @Override
    public void registerObserver(IUniqueObserver obs, Long uniqueId) {
        observers.put(uniqueId, obs);
        notifyObserver(uniqueId.longValue());
    }

    @Override
    public void removeObserver(IUniqueObserver obs) {
        observers.remove(obs.getUniqueId());
    }

    @Override
    public void notifyObservers() {
//        Gson gson = new GsonBuilder().create();
//
//        DominionMessage modelMessage = new DominionMessage(DominionEvent.PLAYER_MODEL, "");
//        for (Entry<Long, IUniqueObserver> obs : observers.entrySet()) {
//            SimpleSpecificPlayerDto specificPlayer = new SimpleSpecificPlayerDto(getPlayerById(obs.getKey()));
//            modelMessage.setValue(gson.toJson(specificPlayer));
//            obs.getValue().update(gson.toJson(modelMessage));
//        }
//
//        SimpleModelDto simpleModel = new SimpleModelDto(this);
//        String simpleJsonModel = gson.toJson(simpleModel);
//        modelMessage = new DominionMessage(DominionEvent.UPDATE_MODEL, simpleJsonModel);
//        String jsonModelEvent = gson.toJson(modelMessage);
//        for (Entry<Long, IUniqueObserver> obs : observers.entrySet()) {
//            obs.getValue().update(jsonModelEvent);
//        }
    }

    public void notifyObserver(long playerId) {
//        SimpleModelDto simpleModel = new SimpleModelDto(this);
//        Gson gson = new GsonBuilder().create();
//        String simpleJsonModel = gson.toJson(simpleModel);
//        DominionMessage modelEvent = new DominionMessage(DominionEvent.UPDATE_MODEL, simpleJsonModel);
//        String jsonModelEvent = gson.toJson(modelEvent);
//        observers.get(playerId).update(jsonModelEvent);
//
//        modelEvent = new DominionMessage(DominionEvent.PLAYER_MODEL, "");
//        SimpleSpecificPlayerDto specificPlayer = new SimpleSpecificPlayerDto(getPlayerById(playerId));
//        modelEvent.setValue(gson.toJson(specificPlayer));
//        observers.get(playerId).update(gson.toJson(modelEvent));
    }

    public void notifyObservers(String event) {
        for (Entry<Long, IUniqueObserver> obs : observers.entrySet()) {
            obs.getValue().update(event);
        }
    }

    @Override
    public void notifyObserver(Long uniqueId, String event) {
        observers.get(uniqueId).update(event);
    }

    public void notifyObserversCardPlayed(String cardName, Player player) {
    }
}
