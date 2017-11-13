package game;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

import cards.Attribute;
import cards.Card;
import players.Player;
import utility.Util;

/**
 * ADT - Board Class.
 * Designed to hold and process the logic of the card game board.
 * In other words, everything that goes on that is visible to the players eye on a regular
 * card game is calculated here. A game HAS-A board. 
 * @see {@link Game}
 * @author paulo
 */
public class Board {
    private final HashMap<Player, ArrayList<Card>> boardmap;
    private final Game game;
    private final Player[] players;
    private final ArrayList<TimedAttribute> timed_attributes;
    
    /**
     * A static class to hold data about timed attributes within the board class.
     * @author paulo
     */
    private static class TimedAttribute{
        public final Attribute attribute;
        public final int turn;
        public final Card card;
        public final Player ply;
        public final Player target;
        public final Card[] targets;
        
        private TimedAttribute( Attribute attribute, int turn, Card card, Player ply, Player target, Card[] targets){
            this.attribute = attribute;
            this.turn = turn;
            this.card = card;
            this.ply = ply;
            this.target = target;
            this.targets = targets;
            Util.print("Timed Attribute! %s's attribute %s (will deploy on turn number %d)\n", ply.getName(), card.getName(), turn);
        }
    }
    
    /**
     * Constructor. Constructs a board for the players in the game.
     * @param game an instance of a active game
     * @param players the players part of that game
     */
    public Board(Game game, Player[] players){
        boardmap = new HashMap<Player, ArrayList<Card>>();
        timed_attributes = new ArrayList<TimedAttribute>();
        this.game = game;
        this.players = players;
        for( Player ply : players ){
            boardmap.put(ply, new ArrayList<Card>());
        }
    }
    
    /**
     * Adds a card to the board
     * @param ply the player that owns the card
     * @param card the card to be added
     */
    private void addCard( Player ply, Card card ){
        ArrayList<Card> ply_board =  boardmap.get(ply);
        boardmap.get(ply).add(card);
        card.setBoardId(ply_board.size()-1);
        Util.print("Card %s was added to %s's board!", card.getName(), ply.getName());
    }
    
    /**
     * Removes a crd from the board
     * @param ply the player that owns the card
     * @param card the card to be removed
     */
    private void removeCard( Player ply, Card card ){
        boardmap.get(ply).remove(card);
        ply.getGraveyard().addCard(card);
    }
    
    /**
     * Gets a card from a players board
     * @param ply the player from which you want a card
     * @param index the card index
     * @return a card selected by index
     */
    private Card getCard( Player ply, int index ){
        return boardmap.get(ply).get(index);
    }
    
    /**
     * Activates an attribute. Logic behind it.
     * @param ply the player 
     * @param card
     * @param attribute
     * @param target
     * @param targets
     */
    private void activateAttribute( Player ply, Card card, Attribute attribute, Player target, Card[] targets ){
        // perform attribute logic, every effect tick.
        for( Card tcard : targets ){ 
            if( tcard.inGraveyard() )
                continue;

            attribute.activate(card, tcard);
                
            if( tcard.getPower() <= 0 ){
                removeCard(target, tcard); // Avoids index out of bounds if a cards dies and gets targeted again.
                Util.print("%s's card has been destroyed - %s", target.getName(), tcard.getName());
            }
            
            if( card.getPower() <= 0 ){
                removeCard(ply, tcard);
                Util.print("%s's card has been destroyed - %s ", ply.getName(), card.getName());
                break; // if the activator of the attribute dies. Attribute stops.
            }
        }
    }
    
    /**
     * Method that checks for all the timed attribute states.
     */
    private void checkTimedAttributes(){
        Util.printSeparator("Timed Attributes");
        if( timed_attributes.isEmpty() ){
            Util.printEmptyMessage("No timed attributes active on the board or placed on this turn!");
            return;
        }
        
        // to avoid concurrent modification exception
        Iterator<TimedAttribute> iter = timed_attributes.iterator();
        while (iter.hasNext()) {
            TimedAttribute ta = iter.next();
            if( game.getTurn() == ta.turn ){
                System.out.printf("Timed attriubte effect triggered! %s's %s", ta.card.getName(), ta.attribute.getName());
                activateAttribute(ta.ply, ta.card, ta.attribute, ta.target, ta.targets);
                iter.remove();
            }else{
                System.out.printf("Timed attriubte! %s's '%s' attribute! (turns left: %d )(activates at the end of turn)\n", ta.card.getName(), ta.attribute.getName(),  ta.turn - (game.getTurn()+1));
            }
        }
    }
    
    /**
     * Plays a card to the board.
     * @param ply the player playing the card
     * @param card the card played by the player
     * @param attribute the attribute played along with the card
     * @param target the target board
     * @param targetids the target cards ids from the target board
     */
    public void playBoard( Player.PlayData pdata ){      
        // check timed attributes
        checkTimedAttributes();
        
        Util.printSeparator("Board Changes");
        // add card to board
        pdata.card_played.setActiveAttribute(pdata.at_played);
        addCard(pdata.player, pdata.card_played);
        
        // skip if no targets ( Case when we place the only card on the board! )
        if( pdata.targetids.length == 0 ){
            Util.print("No targets found for %s of %s! Card was placed on the board without attribute triggering.", pdata.card_played.getName(), pdata.at_played.getName() );
            return;
        }
        
        // target id translation into reference
        Card[] targets = new Card[pdata.targetids.length];
        for( int i = 0; i < pdata.targetids.length; i++ ){
            targets[i] = getCard(pdata.targeted_ply, pdata.targetids[i]);
        }
        
        // does this card have a timed attribute?
        if( pdata.at_played.isTimed() ){
            timed_attributes.add(new TimedAttribute(pdata.at_played, game.getTurn() + pdata.at_played.getTurns(), pdata.card_played, pdata.player, pdata.targeted_ply, targets));
        }else{ // no, active it on deploy!
            activateAttribute(pdata.player, pdata.card_played, pdata.at_played, pdata.targeted_ply, targets);
        }
    }
    
    /**
     * Gets the total power on board of a player
     * @param ply the player to get the total power from
     * @return total board power
     */
    public int getTotalPlayerPower( Player ply ){
        int t = 0;
        for( Card c : boardmap.get(ply) ){
            t = t + c.getPower();
        }
        return t;
    }
    
    /**
     * Returns cards on board of a player
     * @param ply the player to get it from
     * @return number of cards on board
     */
    public int getCardsOnBoard( Player ply ){
        return boardmap.get(ply).size();
    }
    
    /**
     * Displays the board in console.
     */
    public void printBoard(){
        for( Player ply : players ){
            int i = 0;
            boolean empty = true;
            Util.printSeparator("BOARD - '" + ply.getName() + "' - POWER: " + getTotalPlayerPower(ply) );
            for( Card card : boardmap.get(ply) ){
                Util.print("CARD INDEX (ON BOARD): %d", i);
                card.printCard();
                i++;
                empty = false;
            }
            if (empty) {
                Util.printEmptyMessage("NO CARDS IN THIS PLAYER'S BOARD");
            }
        }
    }
    
    /**
     * Displays a specific players section of the board.
     * @param ply the player to display the section of the board
     */
    public void printPlayerBoard( Player ply ){
        int i = 0;
        boolean empty = true;
        Util.printSeparator("BOARD - '" + ply.getName() + "' - POWER: " + getTotalPlayerPower(ply) );
        for( Card card : boardmap.get(ply) ){
            Util.print("CARD INDEX (ON BOARD): %d", i);
            card.printCard();
            i++;
            empty = false;
        }
        if (empty) {
            Util.printEmptyMessage("NO CARDS IN THIS PLAYER'S BOARD");
        }
    }
}
