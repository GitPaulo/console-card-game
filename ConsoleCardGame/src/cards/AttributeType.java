package cards;
/**
 * Enumeration. A enum designed to hold information about the attribute types.
 * It also contains conversion static methods and useful accessors.
 * @author paulo
 */
public enum AttributeType {
    INFLICTING( "Inflicting", "This attribute type deals damage to the cards on the board" ),
    BUFF( "Buff", "This attribute type Buffs the card played on the board" ),
    RESILIANCE( "Resiliance", "This attribute type buffs the next card played on the board" );
    
    private final String name;
    private final String description;
    
    /**
     * Constructor for the enum.
     * @param name the name of the enum.
     * @param description the description of the enum.
     */
    AttributeType( String name, String description ){
        this.name = name;
        this.description = description;
    }
    
    /**
     * @return the name of the attribute type
     */
    public String getName() {
        return name;
    }
    
    /**
     * @return the description of the attribute type
     */
    public String getDescription() {
        return description;
    }
    
    /**
     * Static method to translate a string into the attribute type ENUM
     * @param str Attribute type as string
     * @return Attribute type as enum
     */
    public static AttributeType getFromString( String str ){
        switch( str ){
            case "INFLICTING":
                return INFLICTING;
            case "BUFF":
                return BUFF;
            case "RESILIANCE":
                return RESILIANCE;   
            default:
                throw new Error("Invalid conversion from string " + str + " to AttributeType enum!");
        }
    }
}
