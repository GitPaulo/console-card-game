package cards;

import utility.Util;

/**
 * ADT - Attribute class. 
 * Designed to hold information about an attribute.
 * There are 2 categories of attributes: Timed and non timed
 * Each category can have any of the types described in AttributeType.
 * @see {@link AttributeType}
 * @author paulo
 */
public class Attribute {
    // Instance variables
    private String name;
    private String description;
    private AttributeType type;
    private int value;
    private int num_targets;
    // Instance constants
    private final boolean is_timed;
    private final int turns;
    
    /**
     * Constructor. Creates a new instance of an attribute.
     * @param name the name of the attribute
     * @param description the description of the attribute
     * @param value the base value of the attribute
     * @param num_targets the number of targets the attribute can affect
     * @param is_timed if the attribute is timed or not
     * @param turns the number of turns waited until the attribute comes to effect, if the attribute is timed
     * @param type the type of the attribute
     */
    public Attribute( String name, String description, int value, int num_targets, boolean is_timed, int turns, AttributeType type ){
        this.name = name;
        this.description = description;
        this.value = value;
        this.type = type;
        this.num_targets = num_targets;
        this.is_timed = is_timed;
        this.turns = turns;
    }
    
    /**
     * @return name of the attribute
     */
    public String getName() {
        return name;
    }
    
    /**
     * @return description of the attribute
     */
    public String getDescription() {
        return description;
    }
    
    /**
     * @return base value of the attribute
     */
    public int getValue() {
        return value;
    }
    
    /**
     * @return type of the attribute
     */
    public AttributeType getType(){
        return type;
    }
    
    /**
     * @return number of targets the attribute can target
     */
    public int getNumTargets() {
        return num_targets;
    }
    
    /**
     * @return if the attribute is timed or not
     */
    public boolean isTimed() {
        return is_timed;
    }
    
    /**
     * @return turns untill the attribute has effect
     */
    public int getTurns() {
        return turns;
    }
    
    /**
     * Activates a attribute from an activator card on a target card
     * @param activator the card which the attribute belongs to
     * @param target the target card
     */
    public void activate( Card activator, Card target ){
        switch( type ){
            case INFLICTING:
                int damage_after_resiliance = target.getResiliance()-value;
                if( damage_after_resiliance < 0 ){
                    target.setResiliance(0);
                }else{
                    target.setResiliance(damage_after_resiliance);
                    Util.print("%s has inflicted %d points of damage on %s, however, resiliance protected the target's power!", activator.getName(), value, target.getName());
                    break; // No need to continue
                }
                
                target.setPower(target.getPower()+damage_after_resiliance); // left over damage subtracts on power  
                Util.print("%s has inflicted %d points of damage on %s", activator.getName(), value, target.getName());
                break;
            case BUFF:
                target.setPower(target.getPower()+value);
                Util.print("%s has buffed %s by %d", activator.getName(), target.getName(), value);
                break;
            case RESILIANCE:
                target.setResiliance(target.getResiliance()+value);
                Util.print("%s has inscreased %s resiliance by %d", activator.getName(), target.getName(), value);
                break;
            default:
                throw new IllegalStateException("Invalid attribute type during activation!");
        }
        
        Util.print( "Attribute '%s' from the card %s was activated targeting the card %s", name, activator.getName(), target.getName() );
    }
}
