package cards;
/**
 * Enumeration. A enum designed to hold information about the computer types.
 * It also contains conversion static methods and useful accessors.
 * @author paulo
 */
public enum DeckFaction {
    ELVES( "Elves", "A group of forest elves that advocate peace above all else.\n - Strong Resiliance\n - Weak power\n - Average Infliction"),
    PIRATES( "Pirates", "Ahoy! No, not that kind of pirates. These guys are more of the robin hood kind of group... except they have ships.. and curved swords.\n - Weak Resiliance\n - Strong Power\n - Average Infliction"),
    KINGDOM( "Kingdom", "Knights, sourcerers, kings and queens.. what you expect from any fantasy kingdom.\n - Average Resiliance\n - Weak Power\n - Average Infliction");
    
    private final String name;
    private final String description;
    
    /**
     * Constructor for enum.
     * @param name the name of the enum
     * @param description the description of the enum
     */
    DeckFaction( String name, String description ){
        this.name = name;
        this.description = description;
    }
    
    /**
     * @return the name of the Deck Faction
     */
    public String getName() {
        return name;
    }
    
    /**
     * @return the description of the Deck Faction
     */
    public String getDescription() {
        return description;
    }
    
    /**
     * Gets the enum from a string
     * @param str the string to use
     * @return the enum
     */
    public static DeckFaction getFromString( String str ){
        switch( str ){
            case "ELVES":
                return ELVES;
            case "PIRATES":
                return PIRATES;
            case "KINGDOM":
                return KINGDOM;   
            default:
                throw new Error("Invalid conversion from string " + str + " to DeckFaction enum!");
        }
    }

}
