package players;

import utility.Util;

/**
 * Enumeration. A enum designed to hold information about the computer types.
 * It also contains conversion static methods and useful accessors.
 * @author paulo
 */
public enum ComputerType {
    LOGICAL( "Logical", "This computer tries to play in a 'smart' way." ),
    RANDOM( "Random", "This player will play randomly." ),
    DUMB( "Dumb", "This player will play in a tunnel vision manner." );
    
    private final String name;
    private final String description;
    
    /**
     * Constructor for the enum
     * @param name the name of the enum
     * @param description the description of the enum
     */
    ComputerType( String name, String description ){
        this.name = name;
        this.description = description;
    }
    
    /**
     * Gets the ComputerType from a string.
     * @param str the string to use
     * @return the ComputerType
     */
    public static ComputerType getFromString( String str ){
        switch( str ){
            case "LOGICAL":
                return LOGICAL;
            case "RANDOM":
                return RANDOM;
            case "DUMB":
                return DUMB;   
            default:
                throw new Error("Invalid conversion from string " + str + " to Difficulty enum!");
        }
    }
   
    /**
     * @return the description of the ComputerType
     */
    public String getDescription() {
        return description;
    }
    
    /**
     * @return the name of the ComputerType
     */
    public String getName() {
        return name;
    }
    
    /**
     * Prints the description for the Computer types.
     */
    public static void printDescription() {
        Util.printSeparator2("Computer Types");
        for ( ComputerType typ : ComputerType.values() ) {
            System.out.println( typ.getName().toUpperCase() + " - " + typ.getDescription());
        }
    }
    
    /**
     * @return the computer types as strings 
     */
    public static String[] getTypesAsStrings() {
        ComputerType[] arr = ComputerType.values();
        String[] types = new String[arr.length*2];
        int i = 0;
        for ( ComputerType typ : arr ) {
            types[i] = typ.toString();
            i++;
        }
        for ( ComputerType typ : arr ) {
            types[i] = typ.toString().toLowerCase();
            i++;
        }
        return types;
    }
}
