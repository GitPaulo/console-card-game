package cards;
import java.util.ArrayList;

import utility.Util;

/**
 * A data structure. Simply a array list that can grow.
 * Holds the cards that have been removed from the board.
 * It is specific to a player instance.
 * A player HAS-A board.
 * @author paulo
 * @see {@link Deck}
 */
public class Graveyard {
    private ArrayList<Card> yard;
    
    /**
     * Constructor. Creates a graveyard instance.
     */
    public Graveyard(){
        yard = new ArrayList<Card>();
    }
    
    /**
     * Adds a card to the graveyard.
     * @param card Card to be added to the graveyard.
     */
    public void addCard( Card card ){
        yard.add(card);
        card.setInGraveyard(true);
        Util.print("%s was sent to the graveyard", card.getName());
    }
    
    /**
     * Removes a card out of the graveyard
     * @param index Index of the Card to be removed.
     * @return the Card that was removed
     */
    public Card removeCard( int index ){
        Card card = yard.remove(index);
        card.setInGraveyard(false);
        return card;
    }
    
    /**
     * Does the graveyard contain the card?
     * @param card The Card to be checked.
     * @return boolean - Does the graveyard contain the card?
     */
    public boolean containsCard( Card card ){
        return yard.contains(card);
    }
    
    /**
     * @return the number of cards in the graveyard.
     */
    public int numCardsGraveyard(){
        return yard.size();
    }
    
    /**
     * Prints the graveyard structure to a game format.
     */
    public void printGraveyard(){
        int i = 0;
        for( Card c : yard ){
            if( c != null ){
                Util.print("Card Index on graveyard: %d", i);
                c.printCard();
                i++;
            }
        }
        if( i == 0 ){
            Util.printEmptyMessage("GRAVEYARD IS EMPTY");
        }
    }
}
