package game;
import java.util.Scanner;

import utility.Util;

public class Main {
    /**
     * Main method. Execution of the program starts here.
     * It request information needed to start the game object.
     * @param args
     */
    public static void main(String[] args) {
        Game game;
        final Scanner in = new Scanner(System.in); // this will be the only scanner object that handles all our input stream through out the game
        Character ans = (Character) Util.promptInputValidationByValue( "Would you like to start a new game? [Y/N]", in, new Object[]{ 'n', 'y', 'Y', 'N' } );
        
        if( Character.toLowerCase(ans) == 'y' ){
            final Integer int_plys = (Integer) Util.promptInputValidationByRange( "How many players will this game have? [INT]", in, 2, Integer.MAX_VALUE); //clean
            final Integer int_rounds = (Integer) Util.promptInputValidationByClass( "How many rounds will this game have? [INT]", in, Integer.class ); 
            final Boolean game_mode = (Boolean) Util.promptInputValidationByClass( "Would you like the game to handle clearing console output and hide computer cards for a more imersive game? [BOOLEAN]\n (Note: If correcting the assignment, set as false to get a clear view of how the game works!)", in, Boolean.class );
            game = new Game(1, int_plys, int_rounds, game_mode, new Scanner(System.in));
            game.start();
        }else{
            Util.print("Ending progran...");
        }
        
        in.close();
        System.exit(0);
    }
}
