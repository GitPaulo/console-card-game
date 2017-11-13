package cards;
import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;

import players.Player;
import utility.PresetDecks;
import utility.Util;

/**
 * A data structure. Designed to follow the Queue format - FIFO.
 * A deck contains is of size = DEFAULT_DECK_SIZE.
 * A Player HAS-A Deck.
 * @author paulo
 * @see {@link Player}
 * @see {@link Card}
 */
public class Deck implements Iterable<Card>{
    private static final int DEFAULT_DECK_SIZE = 5;
    private final int MAX_SIZE;
    private final Card[] array;
    private String name;
    private int size;
    private int front;
    private int rear;
    private Player owner;
    
    /**
     * Constructor. Creates a deck which is a collection of cards.
     * @param name the name of the deck
     * @param MAX_SIZE the max size of the deck
     */
    public Deck( String name, int MAX_SIZE ){
        this.MAX_SIZE = MAX_SIZE;
        this.name = name;
        this.size = 0;
        this.front = 0;
        this.rear = 0;
        array = new Card[MAX_SIZE];
        
        this.owner = null;
    }
    
    /**
     * Adds a card to the data structure. FIFO style.
     * @param card
     */
    private void enqueue( Card card ){
        if( size == MAX_SIZE ){
            throw new IllegalStateException("Queue for deck is full!");
        }
        
        array[rear] = card;
        rear = (++rear % MAX_SIZE);
        size++;
    }
    
    /**
     * Removes and returns a card from the data structure.
     * @return
     */
    private Card dequeue(){
        if( size == 0 ){
            throw new IllegalStateException("Queue for deck is empty!");
        }
        
        Card data = array[front%MAX_SIZE];
        array[front] = null;
        front = (++front % MAX_SIZE);
        size--;
        return data;
    }
    
    /**
     * Removes and returns the top card of the deck
     * @return top card of the deck
     */
    public Card removeCard(){
        Card card = dequeue();
        Util.printDebug("Removed card %s from deck %s", card.getName(), name);
        return card;
    }
    
    /**
     * Adds a card to the deck
     * @param card card to be added to the deck
     */
    public void addCard( Card card ){
        enqueue(card);
        Util.printDebug("Added card %s to deck %s", card.getName(), name);
    }
    
    /**
     * @return the number of cards left in deck
     */
    public int getCardsLeft(){
        return size;
    }
    
    /**
     * @return the owner of the deck
     */
    public Player getOwner() {
        return owner;
    }
    
    /**
     * Sets the owner of the deck
     * @param owner the player owner of the deck
     */
    public void setOwner(Player owner) {
        this.owner = owner;
    }
    
    /**
     * Shuffles the deck equally randomly.
     */
    public void shuffle(){
        Random rnd = ThreadLocalRandom.current();
        for (int i = array.length - 1; i > 0; i--) {
          int index = rnd.nextInt(i + 1);
          // Simple swap
          Card c = array[index];
          array[index] = array[i];
          array[i] = c;
        }
        Util.print("Deck %s has been shuffled!", name);
    }
    
    /**
     * Static method to ease the creation of a deck.
     * @param faction faction the deck is part off
     * @return the deck that was preset loaded
     */
    public static Deck loadPresetDeck( DeckFaction faction ){
        Deck deck = new Deck( faction.getName(), DEFAULT_DECK_SIZE );
        switch(faction){  
            case ELVES: 
                deck = PresetDecks.loadElvenDeck(deck);
                break;
            case PIRATES:
                deck = PresetDecks.loadPirateDeck(deck);
                break;
            case KINGDOM:
                deck = PresetDecks.loadKingdomDeck(deck);
                break;
        }
        Util.print("Finished loading deck preset '%s'", faction.getName() );
        return deck;
    }
    
    /**
     * Iterator class for the deck data structure. Used to iterate it easily.
     * @author paulo
     */
    private final class DeckIterator implements Iterator<Card> {
        private int pointer;
        
        public DeckIterator(){
            pointer = rear;
        }
        
        @Override
        public boolean hasNext() {
            return pointer < front;
        }
        
        @Override
        public Card next() {
            if(this.hasNext()) {
               return array[++pointer];
            }
            throw new NoSuchElementException();
        }
    }
 
    @Override
    public Iterator<Card> iterator() {
        return new DeckIterator();
    }
}
