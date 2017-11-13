package cards;

import players.Player;
import utility.Util;

/**
 * ADT - Card class.
 * Designed to hold information about a card that is part of the game.
 * A card HAS-A set of 3 attributes.
 * A card is part of a deck.
 * @see {@link Deck}
 * @author paulo
 */
public class Card {
    public static final int MAX_CARD_ATTRIBUTE = 3; 
    private String name;
    private String description;
    private int power;
    private int resiliance;
    private boolean in_graveyard;
    private Attribute[] attributes;
    private Player owner;
    
    // board data
    private Attribute active_attribute;
    private int board_id;
    
    /**
     * Constructor. Creates a new card instance.
     * @param name the name of the card
     * @param description the description of the card
     * @param power the power of the card
     * @param attributes the attributes the card has
     */
    public Card( String name, String description, int power, Attribute[] attributes ){ 
        if ( attributes.length != MAX_CARD_ATTRIBUTE )
            throw new IllegalArgumentException("Attributes array is not within the specified length!");

        this.name = name;
        this.description = description;
        this.power = power;
        this.attributes = attributes;
        this.in_graveyard = false;
        resiliance = 0; 
        // not necessary but here to mean that these are set once a card gets ownership.
        this.owner = null;
        this.active_attribute = null;
        this.board_id = -1;
    }
    
    /**
     * @return the name of the card
     */
    public String getName(){
        return name;
    }

    /**
     * @return the power of the card
     */
    public int getPower() {
        return power;
    }
    
    /**
     * @return the description of the card
     */
    public String getDescription(){
        return description;
    }

    /**
     * Sets the base power of the card
     * @param power the value of the base power of the card
     */
    public void setPower(int power) {
        this.power = power;
    }
    
    /**
     * @return the resiliance of the card
     */
    public int getResiliance() {
        return resiliance;
    }
    
    /**
     * Sets the resiliance of the card
     * @param resiliance the resiliance value of the card
     */
    public void setResiliance(int resiliance) {
        this.resiliance = resiliance;
    }

    /**
     * @return gets the card attributes
     */
    public Attribute[] getAttributes() {
        return attributes.clone();
    }

    /**
     * @return gets the card active attribute (the one it is played with)
     */
    public Attribute getActiveAttribute() {
        return active_attribute;
    }
    
    /**
     * Sets the active atribute of the card
     * @param active_attribute active attribute of the card (the one it is played with)
     */
    public void setActiveAttribute(Attribute active_attribute) {
        this.active_attribute = active_attribute;
    }
    
    /**
     * @return gets the owner of the card
     */
    public Player getOwner() {
        return owner;
    }
    
    /**
     * Sets the owner of the card
     * @param owner the player that owns the card
     * @return the card instance for programming convinence
     */
    public Card setOwner(Player owner) {
        this.owner = owner;
        return this; // Why? Eases the loading of decks. To much writing otherwise. Why not have it in the constructor? Because there may be cards without owners - someday :D
    }
    
    /**
     * @return if the card is in the graveyard
     */
    public boolean inGraveyard() {
        return in_graveyard;
    }
    
    /**
     * Sets if the card is in the graveyard
     * @param in_graveyard
     */
    public void setInGraveyard(boolean in_graveyard) {
        this.in_graveyard = in_graveyard;
    }
    
    /**
     * @return the id of the board the card is in 
     */
    public int getBoardId() {
        return board_id;
    }
    
    /**
     * Sets the board id for this card
     * @param board_id the id of the board
     */
    public void setBoardId(int board_id) {
        this.board_id = board_id;
    }
    
    /**
     * Displays the card in console
     */
    public void printCard(){
        final int NAME_SPACE = 24;
        final int POWER_SPACE = 23;
        final int RESILIANCE_SPACE = 18;
        final int ATTRIBUTE_SPACE = 26;
        final String over = "(...)";
        
        // Format name
        String name = this.name;
        if ( name.length() >= NAME_SPACE ){
            name = name.substring(0, NAME_SPACE-over.length());
            name = name + over;
        }
        
        int dif1 = NAME_SPACE-name.length();
        StringBuilder sbname = new StringBuilder(name);
        for ( int i = 0; i < dif1; i++ )
            sbname.append(" ");
        
        name = sbname.toString();
        
        // Format power
        String power = Integer.toString(this.power);
        StringBuilder sbpower = new StringBuilder(power);
        int dif2 = POWER_SPACE-power.length();
        for ( int i2 = 0; i2 < dif2; i2++ )
            sbpower.append(" ");
      
        power = sbpower.toString();
        
        // Format resiliance
        String resiliance = Integer.toString(this.resiliance);
        StringBuilder sbresiliance = new StringBuilder(resiliance);
        int dif3 = RESILIANCE_SPACE-resiliance.length();
        for ( int i3 = 0; i3 < dif3; i3++ )
            sbresiliance.append(" ");
        
        resiliance = sbresiliance.toString();
        
        System.out.printf("|-------------------------------|%n"); // 23 chars
        System.out.printf("| Name: %s|%n", name); // 14 chars left
        System.out.printf("| Power: %s|%n", power); // 13 chars left
        System.out.printf("| Resiliance: %s|%n", resiliance); // 13 chars left
        System.out.printf("|                               |%n");
        
        // Format Attributes
        int in = 0;
        for ( Attribute v : attributes ){
            String at = v.getName();
            if ( at.length() >= ATTRIBUTE_SPACE ){
                at = at.substring(0, NAME_SPACE-over.length());
                at = at + over;
            }
            int dif4 = ATTRIBUTE_SPACE-at.length();
            StringBuilder sbat = new StringBuilder(at);
            for ( int i4 = 0; i4 < dif4; i4++ )
                sbat.append(" ");
            
            at = sbat.toString();
            System.out.printf("| A%d: %s|%n", ++in, at);
        }
        
        System.out.printf("|-------------------------------|%n");
    }
    
    /**
     * Prints out the card but "hidden" used by computer player in game-mode state.
     */
    public void printCardHidden() {
        System.out.println("|||||||||||||||||||||||||||||||||");
        System.out.println("|-------------------------------|");
        System.out.println("|-------------------------------|");
        System.out.println("|-------------------------------|");
        System.out.println("|--------- HIDDEN CARD ---------|");
        System.out.println("|-------------------------------|");
        System.out.println("|-------------------------------|");
        System.out.println("|-------------------------------|");
        System.out.println("|||||||||||||||||||||||||||||||||");     
    }
    
    /**
     * Displays the attributes of the card in console
     */
    public void printAttributes(){
        Util.printSeparator( name + " - CARD ATTRIBUTES");
        int i = 0;
        for( Attribute at : attributes ){
            System.out.println("--------[ INDEX " + i + " ]---------" );
            System.out.println("Name: " + at.getName());
            System.out.println("Value: " + at.getValue());
            System.out.println("Type: " + at.getType().toString());
            System.out.println("Timed: " + Boolean.toString(at.isTimed()) );
            System.out.println("TimedTurns: " + at.getTurns());
            System.out.println("#Targets: " + at.getNumTargets());
            System.out.println("Description: " + at.getDescription());
            i++;
        }
    }
}
