package players;
import java.util.Scanner;

import cards.Attribute;
import cards.Card;
import cards.Deck;
import cards.DeckFaction;
import cards.Graveyard;
import cards.Hand;
import game.Board;
import game.Game;
import utility.Util;

public abstract class Player {
    // instance constants/variables
    private final int id;
    private String name; 
    private int rounds_won;
    private Hand hand;
    private Deck deck;
    private Graveyard graveyard;
    private DeckFaction faction;
    private boolean passed;
    private Game game;
    
    /**
     * Constructor. Creates a new player for the game.
     * @param id the id of the player
     * @param name the name of the player
     */
    public Player( int id, String name ){
        this.id = id;
        this.name = name;
        
        passed = false;
        rounds_won = 0;
    }
    
    /**
     * @return the name of the player
     */
    public String getName() {
        return name;
    }
    
    /**
     * @return the id of the player
     */
    public int getId() {
        return id;
    }
    
    /**
     * Sets the hand of a player.
     * @param hand the hand ADT for the player
     */
    public void setHand( Hand hand ){
        this.hand = hand;
    }
    
    /**
     * @return the hand ADT of the player
     */
    public Hand getHand() {
        return hand;
    }
    
    /**
     * Sets the deck of the player
     * @param deck the Deck ADT for the player
     */
    public void setDeck( Deck deck ){
        this.deck = deck;
    }
    
    /**
     * @return the deck ADT of the player
     */
    public Deck getDeck() {
        return deck;
    }

    /**
     * @return the Graveyard ADT of the player
     */
    public Graveyard getGraveyard() {
        return graveyard;
    }
    
    /**
     * Sets the Graveyard ADT for the player
     * @param graveyard the Graveyard ADT of the player
     */
    public void setGraveyard(Graveyard graveyard) {
        this.graveyard = graveyard;
    }
    
    /**
     * @return the faction of the deck currently in use of the player
     */
    public DeckFaction getFaction() {
        return faction;
    }
    
    /**
     * Sets the faction of the deck currently in use of the player
     * @param faction the faction of the deck currently in use of the player
     */
    public void setFaction(DeckFaction faction) {
        this.faction = faction;
    }
    
    /**
     * @return the number of cards left in hand and deck of the player
     */
    public int getCardsLeft(){
        if (deck == null || hand == null) 
            throw new NullPointerException("This method cannot be called without first both the hand and deck to the player.");
        return hand.getCardsInHand() + deck.getCardsLeft();
    }
    
    /**
     * @return the number of rounds won by the player
     */
    public int getRoundsWon() {
        return rounds_won;
    }
    
    /**
     * Sets the number of rounds won by the player
     * @param rounds_won the number of rounds
     */
    public void setRoundsWon(int rounds_won) {
        this.rounds_won = rounds_won;
    }
    
    /**
     * @return the game the player is part of
     */
    public Game getGame() {
        return game;
    }
    
    /**
     * Sets the game the player will be part of
     * @param game the game the player is part of
     */
    public void setGame(Game game) {
        this.game = game;
    }
    
    /**
     * @return if the player has passed
     */
    public boolean hasPassed() {
        return passed;
    }
    
    /**
     * Sets if the player has passed.
     * @param passed has the player passed
     */
    public void setPassed(boolean passed) {
        this.passed = passed;
    }
    
    /**
     * Shuffles the player's deck
     */
    public void shuffleDeck(){
        if (deck == null)
            throw new NullPointerException("This method cannot be called without first setting a deck to the player.");
        Util.print("Player %s started shuffling deck..", name);
        deck.shuffle();
    }
    
    /**
     * Displays the hand of the player
     */
    public void printHand(){
        if (hand == null)
            throw new NullPointerException("This method cannot be called without first setting a hand to the player.");
        hand.printCards(); 
    }
    
    /**
     * Displays the hidden hand of the player (simulates looking at the back of the hand to count cards)
     */
    public void printHandHidden() {
        if (hand == null)
            throw new NullPointerException("This method cannot be called without first setting a hand to the player.");
        hand.printCardsHidden(); 
    }
    
    /**
     * Makes the player draw a card from the deck and puts it in hand.
     * @return the card that was drawn
     */
    public Card drawCard(){
        if (deck == null || hand == null) 
            throw new NullPointerException("This method cannot be called without first both the hand and deck to the player.");
        
        if( deck.getCardsLeft() == 0 ){
            Util.print("Player %s has no more cards in deck!", name); 
            return null;
        }
        
        Card new_card = deck.removeCard();
        hand.addCardToHand(new_card);
        Util.print("Player %s drew a card!", name);
        
        return new_card; 
    }
    
    /**
     * Makes a player play a card index. Removes it from hand and returns the card.
     * @param index the index of the card to play
     * @return the card selected
     */
    public Card playCard( int index ){
        if (hand == null)
            throw new NullPointerException("This method cannot be called without first setting a hand to the player.");
        
        if( hand.getCardsInHand() == 0 ){
            Util.print("Player %s has no more cards in hand!", name);
            return null;
        }
            
        Util.print("Player %s removed a card from hand!", name);
        return hand.removeCardFromHand(index);
    }
    
    /**
     * Resets state of the player object
     */
    public void resetGameState() {
        hand = null;
        deck = null;
        graveyard = null;
        faction = null;
        passed = false;
        Util.printDebug("Player %s's game state was reset", name); 
    }
    
    /**
     * This method is dependent on player type so its abstract
     * Controls the logic played per turn
     * @param players 
     */
    public abstract PlayData play( Scanner in, Player[] players, Board board );
    
    /**
     * This class is used to contain the data for a board play.
     * @author paulo
     */
    public final static class PlayData{
        public final Player player;
        public final Card card_played;
        public final Attribute at_played;
        public final Player targeted_ply;
        public final int[] targetids;
        
        /**
         * Creates an instance of PlayData
         * @param player the player that is playing
         * @param card_played the card the player selected
         * @param at_played the attribute the player has selected
         * @param targeted_ply the targeted player
         * @param targetids the targeted ids of the cards on the board
         */
        PlayData( Player player, Card card_played, Attribute at_played, Player targeted_ply, int[] targetids ){
            this.player = player;
            this.card_played = card_played;
            this.at_played = at_played;
            this.targeted_ply = targeted_ply;
            this.targetids = targetids;
        }
    }
}
