package com.xalero.dominion.commons.cards;

import com.xalero.dominion.commons.cards.action.ActionCard;
import com.xalero.dominion.commons.cards.treasure.TreasureCard;
import com.xalero.dominion.commons.cards.victory.VictoryCard;
import com.xalero.dominion.server.model.DominionConstants;
import com.xalero.dominion.server.model.DominionModel;
import com.xalero.dominion.commons.utils.Result;
import com.xalero.dominion.server.model.Player;
import java.util.List;


public class CardFactory {

    private static final CardFactory cardFactory = new CardFactory();
    private static final int numBaseKingdomCards = 25;
    private static final String baseCardsPath = "dominion/application/cards/base/";

    // Original Dominion Cards
    public static Card createCard(String card) {
        // big switch statement for creating a card
        switch (card.toLowerCase()) {
            case "adventurer":
                return adventurer;
            case "cellar":
                return cellar;
            case "bureaucrat":
                return bureaucrat;
            case "chancellor":
                return chancellor;
            case "chapel":
                return chapel;
            case "council room":
                return councilRoom;
            case "feast":
                return feast;
            case "festival":
                return festival;
            case "laboratory":
                return laboratory;
            case "library":
                return library;
            case "market":
                return market;
            case "militia":
                return militia;
            case "mine":
                return mine;
            case "moat":
                return moat;
            case "moneylender":
                return moneylender;
            case "remodel":
                return remodel;
            case "smithy":
                return smithy;
            case "spy":
                return spy;
            case "thief":
                return thief;
            case "throne room":
                return throneRoom;
            case "village":
                return village;
            case "witch":
                return witch;
            case "woodcutter":
                return woodcutter;
            case "workshop":
                return workshop;
            case "copper":
                return copper;
            case "silver":
                return silver;
            case "gold":
                return gold;
            case "estate":
                return estate;
            case "duchy":
                return duchy;
            case "province":
                return province;
            case "gardens":
                return gardens;
            default:
                return null;
        }

    }

    public static Card[] getBaseKingdomCards() {
        Card[] baseCards = new Card[numBaseKingdomCards];
        baseCards[0] = adventurer;
        baseCards[1] = bureaucrat;
        baseCards[2] = cellar;
        baseCards[3] = chancellor;
        baseCards[4] = chapel;
        baseCards[5] = councilRoom;
        baseCards[6] = feast;
        baseCards[7] = festival;
        baseCards[8] = laboratory;
        baseCards[9] = library;
        baseCards[10] = market;
        baseCards[11] = militia;
        baseCards[12] = mine;
        baseCards[13] = moat;
        baseCards[14] = moneylender;
        baseCards[15] = remodel;
        baseCards[16] = smithy;
        baseCards[17] = spy;
        baseCards[18] = thief;
        baseCards[19] = throneRoom;
        baseCards[20] = village;
        baseCards[21] = witch;
        baseCards[22] = woodcutter;
        baseCards[23] = workshop;
        baseCards[24] = gardens;

        return baseCards;
    }

    public final static Card adventurer = cardFactory.new Adventurer();
    public final static Card cellar = cardFactory.new Cellar();
    public final static Card bureaucrat = cardFactory.new Bureaucrat();
    public final static Card chancellor = cardFactory.new Chancellor();
    public final static Card chapel = cardFactory.new Chapel();
    public final static Card councilRoom = cardFactory.new CouncilRoom();
    public final static Card feast = cardFactory.new Feast();
    public final static Card festival = cardFactory.new Festival();
    public final static Card laboratory = cardFactory.new Laboratory();
    public final static Card library = cardFactory.new Library();
    public final static Card market = cardFactory.new Market();
    public final static Card militia = cardFactory.new Militia();
    public final static Card mine = cardFactory.new Mine();
    public final static Card moat = cardFactory.new Moat();
    public final static Card moneylender = cardFactory.new Moneylender();
    public final static Card remodel = cardFactory.new Remodel();
    public final static Card smithy = cardFactory.new Smithy();
    public final static Card spy = cardFactory.new Spy();
    public final static Card thief = cardFactory.new Thief();
    public final static Card throneRoom = cardFactory.new ThroneRoom();
    public final static Card village = cardFactory.new Village();
    public final static Card witch = cardFactory.new Witch();
    public final static Card woodcutter = cardFactory.new Woodcutter();
    public final static Card workshop = cardFactory.new Workshop();
    public final static Card copper = cardFactory.new Copper();
    public final static Card silver = cardFactory.new Silver();
    public final static Card gold = cardFactory.new Gold();

    public final static Card estate = cardFactory.new Estate();
    public final static Card duchy = cardFactory.new Duchy();
    public final static Card province = cardFactory.new Province();
    public final static Card gardens = cardFactory.new Gardens();
    public final static Card curse = cardFactory.new Curse();

    public class Adventurer extends ActionCard {

        public Adventurer() {
            super();
            id = 1;
        }

        public int getCost() {
            return DominionConstants.SIX;
        }

        @Override
        public int getPlusBuys() {
            return 0;
        }

        @Override
        public int getPlusDraws() {
            return 0;
        }

        @Override
        public int getPlusTreasures() {
            return 0;
        }

        @Override
        public int getPlusActions() {
            return 0;
        }

        @Override
        public Result playCard(List<String> parameters, DominionModel dominionModel, long playerId) {
            Player player = dominionModel.getPlayerById(playerId);
            Result result = player.canPlayAction(this);
            if (!result.isSuccess()) {
                return result;
            }

            player.removeAction();

            int treasureCardCount = 0;
            for (Card deckCard : player.getDeck()) {
                if (deckCard.isTreasure()) {
                    treasureCardCount++;
                }
            }
            for (Card discardCard : player.getDiscardPile()) {
                if (discardCard.isTreasure()) {
                    treasureCardCount++;
                }
            }

            if (treasureCardCount < 2) {
                return result;
            }

            int countNewCards = 0;
            while (countNewCards < 2) {
                Card card = player.drawCard();
                if (!card.isTreasure()) {
                    player.addToDiscardFromHand(card);
                }

                countNewCards++;
            }

            player.addToDiscardFromHand(this);
            player.setMostRecentCardPlayed(this);
            player.setMostRecentCardPlayed(this);
            dominionModel.notifyObserversCardPlayed(this.toString(), player);

            result.setMessage("Played a Adventurer! Schmauzow!!");
            return result;
        }

        @Override
        public String toString() {
            return "Adventurer";
        }
    }

    public class Bureaucrat extends ActionCard {

        public Bureaucrat() {
            super();
            id = 2;
        }

        public int getCost() {
            return DominionConstants.FOUR;
        }

        @Override
        public int getPlusBuys() {
            return 0;
        }

        @Override
        public int getPlusDraws() {
            return 0;
        }

        @Override
        public int getPlusTreasures() {
            return 0;
        }

        @Override
        public int getPlusActions() {
            return 0;
        }

        @Override
        public Result playCard(List<String> parameters, DominionModel dominionModel, long playerId) {
            Player player = dominionModel.getPlayerById(playerId);
            Result result = player.canPlayAction(this);
            if (!result.isSuccess()) {
                return result;
            }

            player.removeAction();

            Card freeSilverYay = dominionModel.getTreasures().getSilver();
            player.addToDeck(freeSilverYay, 0);

            for (Player p : dominionModel.getPlayers()) {
                if (p.getUniqueIdentifier() != playerId) {
                    for (Card c : p.getHand()) {
                        if (c.isVictory()) {
                            p.addToDeck(c, 0);
                            p.getHand().remove(c);
                            break;
                        }
                    }
                }
            }

            player.addToDiscardFromHand(this);
            player.setMostRecentCardPlayed(this);
            dominionModel.notifyObserversCardPlayed(this.toString(), player);

            result.setMessage("Played a Bureaucrat! Action....ATTACK! Schmauzow!!");
            return result;

        }

        @Override
        public String toString() {
            return "Bureaucrat";
        }
    }

    public class Cellar extends ActionCard {

        public Cellar() {
            super();
            id = 3;
        }

        @Override
        public int getCost() {
            return DominionConstants.TWO;
        }

        @Override
        public int getPlusBuys() {
            return 0;
        }

        @Override
        public int getPlusDraws() {
            return 0;
        }

        @Override
        public int getPlusTreasures() {
            return 0;
        }

        @Override
        public int getPlusActions() {
            return 1;
        }

        @Override
        public Result playCard(List<String> parameters, DominionModel dominionModel, long playerId) {
            Player player = dominionModel.getPlayerById(playerId);
            Result result = player.canPlayAction(this);
            if (!result.isSuccess()) {
                return result;
            }

            player.addToDiscardFromHand(this);

            int numCardsDiscarded = 0;
            if (parameters != null) {
                for (String cardToCreate : parameters) {
                    Card cardCreated = createCard(cardToCreate);
                    if (cardCreated != null && player.hasCardInHand(cardCreated)) {
                        player.addToDiscardFromHand(cardCreated);
                        numCardsDiscarded++;
                    }
                }
                player.draw(numCardsDiscarded);
            }
            player.setMostRecentCardPlayed(this);
            dominionModel.notifyObserversCardPlayed(this.toString(), player);

            result.setMessage("Played a Cellar");
            return result;
        }

        @Override
        public String toString() {
            return "Cellar";
        }
    }

    public class Chancellor extends ActionCard {

        public Chancellor() {
            super();
            id = 4;
        }

        public int getCost() {
            return DominionConstants.THREE;
        }

        @Override
        public int getPlusBuys() {
            return 0;
        }

        @Override
        public int getPlusDraws() {
            return 0;
        }

        @Override
        public int getPlusTreasures() {
            return DominionConstants.TWO;
        }

        @Override
        public int getPlusActions() {
            return 0;
        }

        @Override
        public Result playCard(List<String> parameters, DominionModel dominionModel, long playerId) {
            Player player = dominionModel.getPlayerById(playerId);
            Result result = player.canPlayAction(this);
            if (!result.isSuccess()) {
                return result;
            }

            player.removeAction();

            if (parameters != null && parameters.size() > 0 && parameters.get(0).equals("true")) {
                player.setChancellorEffect(true);
            }

            player.addMoney(2);
            player.addToDiscardFromHand(this);

            player.setMostRecentCardPlayed(this);
            dominionModel.notifyObserversCardPlayed(this.toString(), player);

            result.setMessage("Played a Chancellor");
            return result;
        }

        @Override
        public String toString() {
            return "Chancellor";
        }
    }

    public class Chapel extends ActionCard {

        public Chapel() {
            super();
            id = 5;
        }

        public int getCost() {
            return DominionConstants.TWO;
        }

        @Override
        public int getPlusBuys() {
            return 0;
        }

        @Override
        public int getPlusDraws() {
            return 0;
        }

        @Override
        public int getPlusTreasures() {
            return 0;
        }

        @Override
        public int getPlusActions() {
            return 0;
        }

        @Override
        public Result playCard(List<String> parameters, DominionModel dominionModel, long playerId) {
            Player player = dominionModel.getPlayerById(playerId);
            Result result = player.canPlayAction(this);
            if (!result.isSuccess()) {
                return result;
            }

            player.removeAction();
            player.addToDiscardFromHand(this);

            if (parameters != null) {
                for (int i = 0; i < 4 && i < parameters.size(); i++) {
                    Card cardCreated = createCard(parameters.get(i));
                    if (cardCreated != null && player.hasCardInHand(cardCreated)) {
                        player.removeCardFromHand(cardCreated);
                        dominionModel.addToTrash(cardCreated);
                    }
                }
            }

            player.setMostRecentCardPlayed(this);
            dominionModel.notifyObserversCardPlayed(this.toString(), player);

            result.setMessage("Played a Chapel");
            return result;
        }

        @Override
        public String toString() {
            return "Chapel";
        }
    }

    public class CouncilRoom extends ActionCard {

        public CouncilRoom() {
            super();
            id = 6;
        }

        public int getCost() {
            return DominionConstants.FIVE;
        }

        @Override
        public int getPlusBuys() {
            return DominionConstants.ONE;
        }

        @Override
        public int getPlusDraws() {
            return DominionConstants.FOUR;
        }

        @Override
        public int getPlusTreasures() {
            return 0;
        }

        @Override
        public int getPlusActions() {
            return 0;
        }

        @Override
        public Result playCard(List<String> parameters, DominionModel dominionModel, long playerId) {
            Player player = dominionModel.getPlayerById(playerId);
            Result result = player.canPlayAction(this);
            if (!result.isSuccess()) {
                return result;
            }

            player.removeAction();
            player.draw(4);
            player.addBuy();
            for (Player p : dominionModel.getPlayers()) {
                if (p.getUniqueIdentifier() != playerId) {
                    p.drawCard();
                }
            }
            player.addToDiscardFromHand(this);
            player.setMostRecentCardPlayed(this);
            dominionModel.notifyObserversCardPlayed(this.toString(), player);

            result.setMessage("Played a Council Room");
            return result;
        }

        @Override
        public String toString() {
            return "Council Room";
        }
    }

    public class Feast extends ActionCard {

        public Feast() {
            super();
            id = 7;
        }

        public int getCost() {
            return DominionConstants.FOUR;
        }

        @Override
        public int getPlusBuys() {
            return 0;
        }

        @Override
        public int getPlusDraws() {
            return 0;
        }

        @Override
        public int getPlusTreasures() {
            return 0;
        }

        @Override
        public int getPlusActions() {
            return 0;
        }

        @Override
        public Result playCard(List<String> parameters, DominionModel dominionModel, long playerId) {
            Player player = dominionModel.getPlayerById(playerId);
            Result result = player.canPlayAction(this);
            if (!result.isSuccess()) {
                return result;
            }

            player.removeAction();
            player.removeCardFromHand(this);
            dominionModel.addToTrash(this);

            if (parameters != null && parameters.size() > 0) {
                Card cardToFeast = createCard(parameters.get(0));
                if (cardToFeast != null) {
                    player.addToDiscard(cardToFeast);
                }
            } else {
                result.setSuccess(false);
                result.setMessage("Invalid card to gain");
                return result;
            }

            player.setMostRecentCardPlayed(this);
            dominionModel.notifyObserversCardPlayed(this.toString(), player);

            result.setMessage("Played a Feast");
            return result;
        }

        @Override
        public String toString() {
            return "Feast";
        }
    }

    public class Festival extends ActionCard {

        public Festival() {
            super();
            id = 8;
        }

        public int getCost() {
            return DominionConstants.FIVE;
        }

        @Override
        public int getPlusBuys() {
            return DominionConstants.ONE;
        }

        @Override
        public int getPlusDraws() {
            return 0;
        }

        @Override
        public int getPlusTreasures() {
            return DominionConstants.TWO;
        }

        @Override
        public int getPlusActions() {
            return DominionConstants.FIVE;
        }

        @Override
        public Result playCard(List<String> parameters, DominionModel dominionModel, long playerId) {
            Player player = dominionModel.getPlayerById(playerId);
            Result result = player.canPlayAction(this);
            if (!result.isSuccess()) {
                return result;
            }

            player.removeAction();
            player.addActions(2);
            player.addMoney(2);
            player.addBuy();
            player.addToDiscardFromHand(this);

            player.setMostRecentCardPlayed(this);
            dominionModel.notifyObserversCardPlayed(this.toString(), player);

            result.setMessage("Played a Festival");
            return result;
        }

        @Override
        public String toString() {
            return "Festival";
        }
    }

    public class Laboratory extends ActionCard {

        public Laboratory() {
            super();
            id = 9;
        }

        public int getCost() {
            return DominionConstants.FIVE;
        }

        @Override
        public int getPlusBuys() {
            return 0;
        }

        @Override
        public int getPlusDraws() {
            return DominionConstants.TWO;
        }

        @Override
        public int getPlusTreasures() {
            return 0;
        }

        @Override
        public int getPlusActions() {
            return DominionConstants.ONE;
        }

        @Override
        public Result playCard(List<String> parameters, DominionModel dominionModel, long playerId) {
            Player player = dominionModel.getPlayerById(playerId);
            Result result = player.canPlayAction(this);
            if (!result.isSuccess()) {
                return result;
            }

            player.removeAction();
            player.addAction();
            player.draw(2);
            player.addToDiscardFromHand(this);

            player.setMostRecentCardPlayed(this);
            dominionModel.notifyObserversCardPlayed(this.toString(), player);

            result.setMessage("Played a Laboratory");
            return result;
        }

        @Override
        public String toString() {
            return "Laboratory";
        }
    }

    public class Library extends ActionCard {

        public Library() {
            super();
            id = 10;
        }

        public int getCost() {
            return DominionConstants.FIVE;
        }

        @Override
        public int getPlusBuys() {
            return 0;
        }

        @Override
        public int getPlusDraws() {
            return 0;
        }

        @Override
        public int getPlusTreasures() {
            return 0;
        }

        @Override
        public int getPlusActions() {
            return 0;
        }

        @Override
        public Result playCard(List<String> parameters, DominionModel dominionModel, long playerId) {
            return null;
        }

        @Override
        public String toString() {
            return "Library";
        }
    }

    public class Market extends ActionCard {

        public Market() {
            super();
            id = 11;
        }

        public int getCost() {
            return DominionConstants.FIVE;
        }

        @Override
        public int getPlusBuys() {
            return DominionConstants.ONE;
        }

        @Override
        public int getPlusDraws() {
            return DominionConstants.ONE;
        }

        @Override
        public int getPlusTreasures() {
            return DominionConstants.ONE;
        }

        @Override
        public int getPlusActions() {
            return DominionConstants.ONE;
        }

        @Override
        public Result playCard(List<String> parameters, DominionModel dominionModel, long playerId) {
            Player player = dominionModel.getPlayerById(playerId);
            Result result = player.canPlayAction(this);
            if (!result.isSuccess()) {
                return result;
            }

            player.removeAction();
            player.addAction();
            player.drawCard();
            player.addBuy();
            player.addMoney();
            player.addToDiscardFromHand(this);

            player.setMostRecentCardPlayed(this);
            dominionModel.notifyObserversCardPlayed(this.toString(), player);

            result.setMessage("Played a  Market");
            return result;
        }

        @Override
        public String toString() {
            return "Market";
        }
    }

    public class Militia extends ActionCard {

        public Militia() {
            super();
            id = 12;
        }

        public int getCost() {
            return DominionConstants.FOUR;
        }

        @Override
        public int getPlusBuys() {
            return 0;
        }

        @Override
        public int getPlusDraws() {
            return 0;
        }

        @Override
        public int getPlusTreasures() {
            return DominionConstants.TWO;
        }

        @Override
        public int getPlusActions() {
            return 0;
        }

        @Override
        public Result playCard(List<String> parameters, DominionModel dominionModel, long playerId) {
            return null;
        }

        @Override
        public boolean isInteractive() {
            return true;
        }

        @Override
        public String toString() {
            return "Militia";
        }
    }

    public class Mine extends ActionCard {

        public Mine() {
            super();
            id = 13;
        }

        public int getCost() {
            return DominionConstants.FIVE;
        }

        @Override
        public int getPlusBuys() {
            return 0;
        }

        @Override
        public int getPlusDraws() {
            return 0;
        }

        @Override
        public int getPlusTreasures() {
            return 0;
        }

        @Override
        public int getPlusActions() {
            return 0;
        }

        @Override
        public Result playCard(List<String> parameters, DominionModel dominionModel, long playerId) {
            Player player = dominionModel.getPlayerById(playerId);
            Result result = player.canPlayAction(this);
            if (!result.isSuccess()) {
                return result;
            }

            player.removeAction();
            player.addToDiscardFromHand(this);

            if (parameters != null && parameters.size() > 1) {
                Card treasureToTrash = createCard(parameters.get(0));
                Card treasureToGrab = createCard(parameters.get(1));
                if (treasureToTrash == null
                        || !treasureToTrash.isTreasure()) {
                    result.setSuccess(false);
                    result.setMessage("Invalid treasure to trash");
                    return result;
                }
                if (treasureToGrab == null
                        || !treasureToGrab.isTreasure()) {
                    result.setSuccess(false);
                    result.setMessage("Invalid treasure to gain");
                    return result;
                }
                if (treasureToGrab.getCost() - treasureToTrash.getCost() > 3) {
                    result.setSuccess(false);
                    result.setMessage("Cannot gain a treasure costing more than three of the trashing treasure");
                    return result;
                }
                player.removeCardFromHand(treasureToTrash);
                dominionModel.addToTrash(treasureToTrash);
                player.addToHand(treasureToGrab);
            }

            player.setMostRecentCardPlayed(this);
            dominionModel.notifyObserversCardPlayed(this.toString(), player);

            result.setMessage("Just played a Mine");
            return result;
        }

        @Override
        public String toString() {
            return "Mine";
        }
    }

    public class Moat extends ActionCard {

        public Moat() {
            super();
            id = 14;
        }

        public int getCost() {
            return DominionConstants.TWO;
        }

        @Override
        public boolean isReaction() {
            return true;
        }

        @Override
        public int getPlusBuys() {
            return 0;
        }

        @Override
        public int getPlusDraws() {
            return DominionConstants.TWO;
        }

        @Override
        public int getPlusTreasures() {
            return 0;
        }

        @Override
        public int getPlusActions() {
            return 0;
        }

        @Override
        public Result playCard(List<String> parameters, DominionModel dominionModel, long playerId) {
            return null;
        }

        @Override
        public String toString() {
            return "Moat";
        }
    }

    public class Moneylender extends ActionCard {

        public Moneylender() {
            super();
            id = 15;
        }

        public int getCost() {
            return DominionConstants.FOUR;
        }

        @Override
        public int getPlusBuys() {
            return 0;
        }

        @Override
        public int getPlusDraws() {
            return 0;
        }

        @Override
        public int getPlusTreasures() {
            return 0;
        }

        @Override
        public int getPlusActions() {
            return 0;
        }

        @Override
        public Result playCard(List<String> parameters, DominionModel dominionModel, long playerId) {
            Player player = dominionModel.getPlayerById(playerId);
            Result result = player.canPlayAction(this);
            if (!result.isSuccess()) {
                return result;
            }
            if (!player.hasCardInHand(copper)) {
                result.setSuccess(false);
                result.setMessage("Moneylender requires a copper in your hand");
                return result;
            }

            player.removeAction();
            player.addMoney(3);
            dominionModel.addToTrash(copper);
            player.removeCardFromHand(copper);
            player.addToDiscardFromHand(this);
            player.setMostRecentCardPlayed(this);
            dominionModel.notifyObserversCardPlayed(this.toString(), player);

            result.setMessage("Played copper");
            return result;
        }

        @Override
        public String toString() {
            return "Moneylender";
        }
    }

    public class Remodel extends ActionCard {

        public Remodel() {
            super();
            id = 16;
        }

        public int getCost() {
            return DominionConstants.FOUR;
        }

        @Override
        public int getPlusBuys() {
            return 0;
        }

        @Override
        public int getPlusDraws() {
            return 0;
        }

        @Override
        public int getPlusTreasures() {
            return 0;
        }

        @Override
        public int getPlusActions() {
            return 0;
        }

        @Override
        public Result playCard(List<String> parameters, DominionModel dominionModel, long playerId) {
            Player player = dominionModel.getPlayerById(playerId);
            Result result = player.canPlayAction(this);
            if (!result.isSuccess()) {
                return result;
            }

            player.removeAction();
            player.addToDiscardFromHand(this);

            if (parameters != null && parameters.size() > 1) {
                Card cardToTrash = createCard(parameters.get(0));
                Card cardToGrab = createCard(parameters.get(1));
                if (cardToTrash == null) {
                    result.setSuccess(false);
                    result.setMessage("Invalid card to trash");
                    return result;
                }
                if (cardToGrab == null) {
                    result.setSuccess(false);
                    result.setMessage("Invalid card to gain");
                    return result;
                }
                if (cardToGrab.getCost() - cardToTrash.getCost() > 2) {
                    result.setSuccess(false);
                    result.setMessage("Cannot gain a card costing more than two of the trashing card");
                }
                player.removeCardFromHand(cardToTrash);
                if (dominionModel.kingdomCardInGame(cardToGrab)) {
                    cardToGrab = dominionModel.getKingdomCard(cardToGrab).drawCard();
                    player.addToDiscard(cardToGrab);
                } else if (cardToGrab.isTreasure()) {
                    player.addToDiscard(dominionModel.getTreasures().getTreasure(cardToGrab));
                } else if (cardToGrab.isVictory()) {
                    player.addToDiscard(dominionModel.getVictoryCards().getVictoryCard(cardToGrab));
                } else if (cardToGrab.equals(curse)) {
                    cardToGrab = dominionModel.drawCurse();
                    player.addToDiscard(cardToGrab);
                }
            }

            player.setMostRecentCardPlayed(this);
            dominionModel.notifyObserversCardPlayed(this.toString(), player);

            result.setMessage("Just played a Remodel");
            return result;
        }

        @Override
        public String toString() {
            return "Remodel";
        }
    }

    public class Smithy extends ActionCard {

        public Smithy() {
            super();
            id = 17;
        }

        public int getCost() {
            return DominionConstants.FOUR;
        }

        @Override
        public int getPlusBuys() {
            return 0;
        }

        @Override
        public int getPlusDraws() {
            return DominionConstants.THREE;
        }

        @Override
        public int getPlusTreasures() {
            return 0;
        }

        @Override
        public int getPlusActions() {
            return 0;
        }

        @Override
        public Result playCard(List<String> parameters, DominionModel dominionModel, long playerId) {
            Player player = dominionModel.getPlayerById(playerId);
            Result result = player.canPlayAction(this);
            if (!result.isSuccess()) {
                return result;
            }

            player.removeAction();
            player.draw(3);
            player.addToDiscardFromHand(this);

            player.setMostRecentCardPlayed(this);
            dominionModel.notifyObserversCardPlayed(this.toString(), player);

            result.setMessage("Played a Smithy");
            return result;
        }

        @Override
        public String toString() {
            return "Smithy";
        }
    }

    public class Spy extends ActionCard {

        public Spy() {
            super();
            id = 18;
        }

        public int getCost() {
            return DominionConstants.FOUR;
        }

        @Override
        public int getPlusBuys() {
            return 0;
        }

        @Override
        public int getPlusDraws() {
            return DominionConstants.ONE;
        }

        @Override
        public int getPlusTreasures() {
            return 0;
        }

        @Override
        public int getPlusActions() {
            return DominionConstants.ONE;
        }

        @Override
        public Result playCard(List<String> parameters, DominionModel dominionModel, long playerId) {
            Player player = dominionModel.getPlayerById(playerId);
            Result result = player.canPlayAction(this);
            if (!result.isSuccess()) {
                return result;
            }

            player.addToDiscardFromHand(this);
            player.drawCard();

            // display player's top card, keep or discard card
            // display other player's top card, keep or discard
            return null;
        }

        @Override
        public boolean isInteractive() {
            return true;
        }

        @Override
        public String toString() {
            return "Spy";
        }
    }

    public class Thief extends ActionCard {

        public Thief() {
            super();
            id = 19;
        }

        public int getCost() {
            return DominionConstants.FOUR;
        }

        @Override
        public int getPlusBuys() {
            return 0;
        }

        @Override
        public int getPlusDraws() {
            return 0;
        }

        @Override
        public int getPlusTreasures() {
            return 0;
        }

        @Override
        public int getPlusActions() {
            return 0;
        }

        @Override
        public Result playCard(List<String> parameters, DominionModel dominionModel, long playerId) {
            return null;
        }

        @Override
        public boolean isInteractive() {
            return true;
        }

        @Override
        public String toString() {
            return "Thief";
        }
    }

    public class ThroneRoom extends ActionCard {

        public ThroneRoom() {
            super();
            id = 20;
        }

        public int getCost() {
            return DominionConstants.FOUR;
        }

        @Override
        public int getPlusBuys() {
            return 0;
        }

        @Override
        public int getPlusDraws() {
            return 0;
        }

        @Override
        public int getPlusTreasures() {
            return 0;
        }

        @Override
        public int getPlusActions() {
            return 0;
        }

        @Override
        public Result playCard(List<String> parameters, DominionModel dominionModel, long playerId) {
            Player player = dominionModel.getPlayerById(playerId);
            Result result = player.canPlayAction(this);
            if (!result.isSuccess()) {
                return result;
            }

            player.removeAction();
            player.addToDiscardFromHand(this);

            if (parameters != null && parameters.size() > 0) {
                Card cardToPlayTwice = createCard(parameters.get(0));
                if (cardToPlayTwice != null && cardToPlayTwice.isAction()) {
                    ((ActionCard) cardToPlayTwice).playCard(parameters, dominionModel, playerId);
                    parameters.remove(0);
                    ((ActionCard) cardToPlayTwice).playCard(parameters, dominionModel, playerId);
                }
            }

            player.setMostRecentCardPlayed(this);
            dominionModel.notifyObserversCardPlayed(this.toString(), player);
            result.setMessage("Just played a Throne Room");

            return result;
        }

        @Override
        public String toString() {
            return "Throne Room";
        }
    }

    public class Village extends ActionCard {

        public Village() {
            super();
            id = 21;
        }

        public int getCost() {
            return DominionConstants.THREE;
        }

        @Override
        public int getPlusBuys() {
            return 0;
        }

        @Override
        public int getPlusDraws() {
            return DominionConstants.ONE;
        }

        @Override
        public int getPlusTreasures() {
            return 0;
        }

        @Override
        public int getPlusActions() {
            return DominionConstants.TWO;
        }

        @Override
        public Result playCard(List<String> parameters, DominionModel dominionModel, long playerId) {
            Player player = dominionModel.getPlayerById(playerId);
            Result result = player.canPlayAction(this);
            if (!result.isSuccess()) {
                return result;
            }

            player.removeAction();
            player.addActions(2);
            player.drawCard();
            player.addToDiscardFromHand(this);

            player.setMostRecentCardPlayed(this);
            dominionModel.notifyObserversCardPlayed(this.toString(), player);

            return new Result(true, "Played Village");
        }

        @Override
        public String toString() {
            return "Village";
        }
    }

    public class Witch extends ActionCard {

        public Witch() {
            super();
            id = 22;
        }

        public int getCost() {
            return DominionConstants.FIVE;
        }

        @Override
        public int getPlusBuys() {
            return 0;
        }

        @Override
        public int getPlusDraws() {
            return DominionConstants.TWO;
        }

        @Override
        public int getPlusTreasures() {
            return 0;
        }

        @Override
        public int getPlusActions() {
            return 0;
        }

        @Override
        public Result playCard(List<String> parameters, DominionModel dominionModel, long playerId) {
            Player player = dominionModel.getPlayerById(playerId);
            Result result = player.canPlayAction(this);
            if (!result.isSuccess()) {
                return result;
            }

            player.removeAction();
            player.draw(2);
            for (Player p : dominionModel.getPlayers()) {
                if (p.getUniqueIdentifier() != playerId) {
                    p.addToDiscard(dominionModel.drawCurse());
                }
            }
            player.addToDiscardFromHand(this);
            player.setMostRecentCardPlayed(this);
            dominionModel.notifyObserversCardPlayed(this.toString(), player);

            result.setMessage("Played a Witch");
            return result;
        }

        @Override
        public String toString() {
            return "Witch";
        }
    }

    public class Woodcutter extends ActionCard {

        public Woodcutter() {
            super();
            id = 23;
        }

        public int getCost() {
            return DominionConstants.THREE;
        }

        @Override
        public int getPlusBuys() {
            return 0;
        }

        @Override
        public int getPlusDraws() {
            return 0;
        }

        @Override
        public int getPlusTreasures() {
            return 0;
        }

        @Override
        public int getPlusActions() {
            return 0;
        }

        @Override
        public Result playCard(List<String> parameters, DominionModel dominionModel, long playerId) {
            Player player = dominionModel.getPlayerById(playerId);
            Result result = player.canPlayAction(this);
            if (!result.isSuccess()) {
                return result;
            }

            player.removeAction();
            player.addBuy();
            player.addMoney(2);
            player.addToDiscardFromHand(this);

            player.setMostRecentCardPlayed(this);
            dominionModel.notifyObserversCardPlayed(this.toString(), player);

            result.setMessage("Played a Woodcutter");
            return result;
        }

        @Override
        public String toString() {
            return "Woodcutter";
        }
    }

    public class Workshop extends ActionCard {

        public Workshop() {
            super();
            id = 24;
        }

        public int getCost() {
            return DominionConstants.THREE;
        }

        @Override
        public int getPlusBuys() {
            return 0;
        }

        @Override
        public int getPlusDraws() {
            return 0;
        }

        @Override
        public int getPlusTreasures() {
            return 0;
        }

        @Override
        public int getPlusActions() {
            return 0;
        }

        @Override
        public Result playCard(List<String> parameters, DominionModel dominionModel, long playerId) {
            Player player = dominionModel.getPlayerById(playerId);
            Result result = player.canPlayAction(this);
            if (!result.isSuccess()) {
                return result;
            }

            player.removeAction();
            player.addToDiscardFromHand(this);

            if (parameters != null && parameters.size() > 0) {
                Card cardToGain = createCard(parameters.get(0));
                if (cardToGain != null && cardToGain.getCost() <= 4) {
                    player.addToDiscard(cardToGain);
                } else {
                    result.setSuccess(false);
                    result.setMessage("Invalid card to gain");
                }
            }

            player.setMostRecentCardPlayed(this);
            dominionModel.notifyObserversCardPlayed(this.toString(), player);
            dominionModel.notifyObservers();
            result.setMessage("Played a Workshop");
            return result;
        }

        @Override
        public String toString() {
            return "Workshop";
        }
    }

    public class Copper extends TreasureCard {

        public Copper() {
            super();
            id = 25;
        }

        @Override
        public int getValue() {
            return DominionConstants.COPPER_WORTH;
        }

        @Override
        public int getCost() {
            return DominionConstants.ZERO;
        }

        @Override
        public String toString() {
            return "Copper";
        }
    }

    public class Silver extends TreasureCard {

        public Silver() {
            super();
            id = 26;
        }

        @Override
        public int getValue() {
            return DominionConstants.SILVER_WORTH;
        }

        @Override
        public int getCost() {
            return DominionConstants.THREE;
        }

        @Override
        public String toString() {
            return "Silver";
        }
    }

    public class Gold extends TreasureCard {

        public Gold() {
            super();
            id = 27;
        }

        @Override
        public int getValue() {
            return DominionConstants.GOLD_WORTH;
        }

        @Override
        public int getCost() {
            return DominionConstants.SIX;
        }

        @Override
        public String toString() {
            return "Gold";
        }
    }

    public class Province extends VictoryCard {

        public Province() {
            super();
            id = 28;
        }

        @Override
        public int getPoints() {
            return DominionConstants.PROVINCE_POINTS;
        }

        @Override
        public int getCost() {
            return DominionConstants.EIGHT;
        }

        @Override
        public String toString() {
            return "Province";
        }
    }

    public class Estate extends VictoryCard {

        public Estate() {
            super();
            id = 29;
        }

        @Override
        public int getPoints() {
            return DominionConstants.ESTATE_POINTS;
        }

        @Override
        public int getCost() {
            return DominionConstants.TWO;
        }

        @Override
        public String toString() {
            return "Estate";
        }
    }

    public class Duchy extends VictoryCard {

        public Duchy() {
            super();
            id = 30;
        }

        @Override
        public int getPoints() {
            return DominionConstants.DUCHY_POINTS;
        }

        @Override
        public int getCost() {
            return DominionConstants.FIVE;
        }

        @Override
        public String toString() {
            return "Duchy";
        }
    }

    public class Gardens extends VictoryCard {

        public Gardens() {
            super();
            id = 31;
        }

        public int getCost() {
            return DominionConstants.FOUR;
        }

        @Override
        public String toString() {
            return "Gardens";
        }
    }

    public class Curse extends VictoryCard {

        public Curse() {
            super();
            id = 32;
        }

        @Override
        public int getPoints() {
            return DominionConstants.CURSE_POINTS;
        }

        @Override
        public int getCost() {
            return 0;
        }

        @Override
        public String toString() {
            return "Curse";
        }
    }
}
