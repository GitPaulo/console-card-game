package cards;

import utility.Util;

/**
 * A data structure to control the hand of the user.
 * Designed to be a static array of size = MAX_CARDS_IN_HAND
 * A Player HAS-A hand.
 * A Hand contains Cards that the user can play to the Board.
 * @author paulo
 * @see {@link Board}
 * @see {@link Card}
 * @see {@link Player}
 */
public class Hand {
    // Data structure variables
    public static final int MAX_CARDS_IN_HAND = 2; // this should remain as 2 - some logic depends on it :b (slightly lazy)
    private final Card array[];
    private int cards_in_hand;
    
    /**
     * Constructor.
     * Creates a new hand instance with size MAX_CARDS_IN_HAND
     */
    public Hand(){
        array = new Card[MAX_CARDS_IN_HAND];
        cards_in_hand = 0;
    }
    
    /**
     * Adds a card to the Hand
     * @param card
     */
    public void addCardToHand( Card card ){
        if ( cards_in_hand == MAX_CARDS_IN_HAND ) {
            throw new IllegalStateException("Hand is full!");
        }
        
        for( int i = 0; i < array.length; i++ ){
            if( array[i] == null ){ // find first free space and break
                array[i] = card;
                Util.printDebug("Added card %s to a hand at pos %d!", array[i].getName(), i);
                break;
            }
        }
       
        cards_in_hand++;
    }
    
    /**
     * Removes a card from the hand data structure.
     * @param index index of the card to remove
     * @return returns the removed card.
     */
    public Card removeCardFromHand( int index ){
        if ( cards_in_hand == 0 ) {
            throw new IllegalStateException("Hand is empty");
        } 
        
        if ( !hasCardOnIndex(index) ) {
            throw new IllegalStateException("There is no card on the hand at this index!");
        }
        
        Card cardremoved = array[index];
        array[index] = null;
        cards_in_hand--;
        
        return cardremoved;
    }
    
    /**
     * Checks if the index holds a card.
     * @param index index of the card to check
     * @return true or false
     */
    public boolean hasCardOnIndex( int index ){
        return array[index] != null;
    }
    
    /**
     * @return the number of cards in the hand datastructure.
     */
    public int getCardsInHand(){
        return cards_in_hand;
    }
    
    /**
     * Gets the card at an index.
     * @param index index of the card to get
     * @return the card
     */
    public Card getCardFromHand( int index ){
        if ( !hasCardOnIndex(index) ) {
            throw new IllegalStateException("There is no card on the hand at this index!");
        }
        
        return array[index];
    }
    
    /**
     * @return the first card found in hand
     */
    public int getFirstCardIndexFromHand() {
        return ( array[0] == null ) ? 1 : 0;
    }
    
    /**
     * Prints the cards in a game format in this data structure.
     */
    public void printCards() {
        int i = 0;
        for( Card c : array ){
            if( c != null ){
                Util.print("Card Index on hand: %d", i);
                c.printCard();
                i++;
            }
        }
        if( i == 0 ){
            Util.printEmptyMessage("HAND IS EMPTY");
        }
    }
    
    /**
     * Prints the cards in a game hidden format in this data structure.
     */
    public void printCardsHidden() {
        int i = 0;
        for( Card c : array ){
            if( c != null ) {
                c.printCardHidden();
                i++;
            }
        }
        if( i == 0 ){
            Util.printEmptyMessage("HAND IS EMPTY");
        }
    }
    
}
