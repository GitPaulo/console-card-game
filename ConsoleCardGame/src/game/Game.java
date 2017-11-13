package game;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map.Entry;
import java.util.Scanner;
import java.util.StringJoiner;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;

import cards.AttributeType;
import cards.Deck;
import cards.DeckFaction;
import cards.Graveyard;
import cards.Hand;
import players.Computer;
import players.ComputerType;
import players.Human;
import players.Player;
import utility.Util;

/**
 * Core class of the program.
 * Program setup, logic and conclusion is controlled in this class.
 * Links the user and the program.
 * @author paulo
 * @see {@link Main}
 */
public class Game {
    // instance constants
    private final int id; 
    private final int n_players;
    private final Scanner in;
    private final Player[] players;
    private final int max_rounds;
    private final Round[] round_data;
    private final boolean game_mode;
    
    // instance variables
    private int current_round;
    private Board board;
    private int turn;
    private boolean is_active;
    
    /**
     * Inner class, used to encapsulate the behavior of rounds which are part of a game.
     * Eases readability and access to data tied specifically to each round.
     * @author paulo
     */
    private final class Round {
        public final int number;
        public final Player[] winners; 
        public final HashMap<Player, Integer> score = new HashMap<Player, Integer>();
        public final boolean draw;
        
        /**
         * Constructor. Creates a new instance of the round ADT.
         */
        private Round(){
            this.number = current_round;
            int max = Integer.MIN_VALUE;
            int max_count = 0;
            Player winner = null;
            for( Player ply : players ){
                int tpower = board.getTotalPlayerPower(ply);
                if( max_count == 0 || tpower > max ){
                    max = tpower;
                    winner = ply;
                    max_count = 1;
                }else if ( tpower == max ){
                    max_count++;
                }
                // store round score
                score.put(ply,tpower);
            }
            
            this.winners = new Player[max_count];
            if ( max_count > 1 ) {
                draw = true;
                // better way doing this?
                int i = 0;
                for( Player ply : score.keySet() ){
                    int tpower = board.getTotalPlayerPower(ply);
                    if ( tpower == max ) {
                        this.winners[i] = ply;
                        i++;
                    }       
                }
            }else{
                draw = false;
                this.winners[0] = winner;
            }
        }
    }
    
    /**
     * Constructor. Creates a new instance of the Game.
     * @param id the game ID
     * @param n_players the number of players in the game
     * @param max_rounds the max number of rounds in the game
     * @param game_mode auto clear text in console? hide computer draw and hand? (makes game imersive)
     * @param in the input stream for use in the class
     */
    Game( int id, int n_players, int max_rounds, boolean game_mode, Scanner in ){
        this.id = id;
        this.n_players = n_players;
        this.max_rounds = max_rounds;
        this.in = in;
        this.game_mode = game_mode;
        this.players = new Player[n_players];
        this.round_data = new Round[max_rounds];
        
        current_round = 0;
        is_active = true;
    }
    
    /**
     * @return id of the game
     */
    public int getId() {
        return id;
    }
    
    /**
     * @return current round of the game
     */
    public int getCurrentRound() {
        return current_round;
    }
    
    /**
     * @return current turn of the game
     */
    public int getTurn() {
        return turn;
    }
    
    /**
     * Starts the game logic.
     * Starts the game-setup and game-phase stages.
     * After calling this method, expect a long runtime for the calling thread. 
     */
    public void start(){
        // Music
        Util.print("Turn on music? [Y/N]");
        if( in.nextLine().equalsIgnoreCase("y") ){
            try {
                music();
            } catch (LineUnavailableException | IOException | UnsupportedAudioFileException e) {
                Util.printError("Unable to start music! Lets just ignore that...");
            }
        }
        
        // Wait a bit for user to process what is going on
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        
        if( game_mode )
            Util.clearConsole();
        
        // Introduction
        Util.printSeparator2("Welcome to 'Not A Rip-off GWENT' - A Card Game - COMP213 Assesement 1 - By Paulo Santos");
        introduction();
        if( game_mode )
            Util.clearConsole();
        
        // Player Setup
        Util.printSeparator2("A game has started! ID: " + id + " #Players: " + n_players);
        setUpPlayers();
        
        while( is_active ){ // Allows re matches with new decks for the same players!
            if( game_mode )
                Util.clearConsoleConfirm(in);
            
            current_round++;
            turn = 0;
            
            // Board/Deck Setup
            board = new Board(this, players);
            Util.printSeparator2("Deck selection!");
            deckSelection();
            
            if( game_mode )
                Util.clearConsoleConfirm(in);
            
            // Hand Setup and initial draw
            Util.printSeparator2("Setting up player's hands...");
            setUpHand();
            
            if( game_mode )
                Util.clearConsoleConfirm(in);
            
            // Round start
            Util.printSeparator2("Starting round " + current_round); 
            logic();
            
            // Game end?
            if(current_round < max_rounds){
                Util.print("Advancing to the next round! Round number: %d/%d", current_round, max_rounds);
                is_active = true;
            }else{
                is_active = false;
            }
        }
        
        Util.printSeparator2("Game over!");
        gameOver();
        
        // close stream
        in.close();
    }
    
    /**
     * Loads and attempts to play the game, type .wav, music file.
     * The audio will play in a infinite loop.
     * @throws LineUnavailableException
     * @throws IOException
     * @throws UnsupportedAudioFileException
     */
    private void music() throws LineUnavailableException, IOException, UnsupportedAudioFileException {
        AudioInputStream audioIn = AudioSystem.getAudioInputStream(Game.class.getResource("game_background.wav"));
        Clip clip = AudioSystem.getClip();
        clip.open(audioIn);
        clip.loop(Clip.LOOP_CONTINUOUSLY); // make it always play
        clip.start();
        Util.print("Music started! \n - Type: " + clip.getFormat() + "\n - Length in minutes: " + clip.getMicrosecondLength()/1000000/60);
    }
    
    /**
     * Displays the introduction to the card game.
     */
    private void introduction(){
        StringJoiner joiner = new StringJoiner("\n");
        joiner.add("===============[ Introduction & Game Instructions ]===============");
        joiner.add("A Round based card game with 3 factions, attribute types and categories, multiple players, ai computers, hand, deck and graveyard data structures.");
        joiner.add(" 1. Player Setup:\n   - Here the user inputs the number of players participating in the game and decides if they are a Computer or a Human.");
        joiner.add(" 2. Deck Setup:\n   - Here the user picks each player's deck.");
        joiner.add(" 3. Game Logic:\n   - The game round starts. Hand size = " + Hand.MAX_CARDS_IN_HAND +"\n   - Each turn a player plays a card.\n   - The player selects which attribute the card will be played with.\n   - There are 2 Categories of attributes TIMED and NON-TIMED. Each attribute can be on of the 3 types of attributes BUFF, INFLICT and RESILIANCE.\n   - The card selected will be played with an active attribute placed on the board\n   - Turns are repeated untill a player passes or runs out of cards\n   - Round winner is the player with the highest power on the board.\n   - Repeat until no more cards. \n   - Winner of the round is the player with most power on board.\n   - Repeat until max rounds. Game Winner is player with most round wins.");
       
        joiner.add("\n===============[ Specifics - Factions ]===============");  
        for( DeckFaction v : DeckFaction.values() )
            joiner.add( "Name: " + v.getName() + "\nDescription: " + v.getDescription());
        
        joiner.add("\n===============[ Specifics - Attributes ]===============");
        joiner.add("Attributes can be timed or not timed. Timed attributes are deployed after a certains number of turns. Non timed are deployed at play.\nEach attribute can be on of the 3 types:");  
        for( AttributeType v : AttributeType.values() )
            joiner.add( "Name: " + v.getName() + "\nDescription: " + v.getDescription());
        
        joiner.add("\n===============[ Specifics - AI Difficulties ]==============="); 
        for( ComputerType v : ComputerType.values() )
            joiner.add( "Name: " + v.getName() + "\nDescription: " + v.getDescription());
        joiner.add("\n===============[ Copyright for the song ]==============="); 
        joiner.add("Copyright Disclaimer Under Section 107 of the Copyright Act 1976, allowance is made for \"fair use\" for purposes such as criticism, comment, news reporting, teaching, scholarship, and research. Fair use is a use permitted by copyright statute that might otherwise be infringing.\nNon-profit, educational or personal use tips the balance in favor of fair use.");
        joiner.add("Song: L' Arabesque Danse Toujours from Magi OST. Rights reserved to the original content creators");
        
        System.out.println(joiner.toString());
        Util.print("Done reading? Type anything to move on...");
        in.nextLine();
    }
    
    /**
     * Sets up all the players before each game.
     */
    private void setUpPlayers(){
        for( int i = 0; i < n_players; i++ ){
            // Setup player information       
            String ans = (String) Util.promptInputValidationByValue("[PLAYER SETUP][ID: "+i+"] \nInsert Player type\n - Human\n - Computer", in, new Object[]{"Human", "Computer", "HUMAN", "COMPUTER", "human", "computer"} );
            if( ans.equalsIgnoreCase("human") ){
                Util.print("[COMPUTER SETUP] Please enter players name:");
                players[i] = new Human(i, in.nextLine());
            }else if( ans.equalsIgnoreCase("computer") ){
                ComputerType dif = null;
                ComputerType.printDescription();
                String difstr = (String) Util.promptInputValidationByValue("[COMPUTER SETUP] Please enter Computer difficulty level:", in, ComputerType.getTypesAsStrings() );
                dif = ComputerType.getFromString(difstr.toUpperCase());
                players[i] = new Computer( i, "PC"+i+"-"+dif.getName(), dif );
            }else{
                throw new Error("Invalid input processed!");
            }
            players[i].setGame(this);
        }
    }
    
    /**
     * Sets up the decks that each player will use.
     */
    private void deckSelection() {
        Util.print("[DECK SELECTION] For each of the players, please select the faction they are going to play with. Factions:\n - Elves\n - Pirates\n - Kingdom");
        for( Player ply : players ){
            Util.printSeparator("DECK SELECTION - " + ply.getName());
            String ans = (String) Util.promptInputValidationByValue("[DECK SELECTION]["+ply.getName()+"][ID: "+ply.getId()+"] Please select a faction for the player!", in, new Object[]{"ELVES", "PIRATES", "KINGDOM", "elves", "pirates", "kingdom", "Elves", "Pirates", "Kingdom"} );
            ply.setFaction(DeckFaction.getFromString(ans.toUpperCase()));
            ply.setDeck(Deck.loadPresetDeck(ply.getFaction()));
            ply.getDeck().setOwner(ply);
            ply.shuffleDeck();
            ply.setGraveyard(new Graveyard());
        }
    }
    
    /** 
     * Sets up each player's card hand.
     */
    private void setUpHand(){
        for( Player ply : players ){
            ply.setHand(new Hand());
            Util.printSeparator("Initial Card for '" + ply.getName() + "'");
            // draw first card, so the hand always has 2
            if( game_mode && ply instanceof Computer )
                ply.drawCard().printCardHidden();
            else
                ply.drawCard().printCard(); 
        }
    }
    
    /**
     * Calculates the main logic of the game.
     * Note: The reason this method was not split is because all of the code ran in the block
     * is part of the same logic process. Furthermore, this section of code is to be ran multiple times
     * per round. This makes it simple to call.
     */
    private void logic(){
        while( everyoneHasNotPassed() ){
            for( Player ply : players ){
                if ( !ply.hasPassed() ){
                    // Confirmation telling players the next turn is about to be computed:
                    turn++;
                    Util.print("[NEXT TURN ALERT][#%d] Type anything to move on to the next turn.. whenever you are ready!", turn);
                    in.nextLine();
                    
                    // Check if player can play this turn!
                    if( ply.getCardsLeft() == 0 ){
                        ply.setPassed(true);
                        Util.print("Player %s has no more cards! He has passed. Total power: %d\nSkipping turn!", ply.getName(), board.getTotalPlayerPower(ply));
                        continue;
                    } 
                    
                    // Check if the player can draw a card from deck to hand
                    if( ply.getDeck().getCardsLeft() == 0 ) {
                        Util.print("Player %s has no more cards in the deck! Unable to draw!", ply.getName());
                    } else {
                        // Player draws a card from deck
                        Util.printSeparator("Card drawn by '" + ply.getName() + "'");
                        if( game_mode && ply instanceof Computer )
                            ply.drawCard().printCardHidden();
                        else
                            ply.drawCard().printCard();
                    }
                    
                    // Prints to console cards in hand
                    Util.printSeparator("Hand for '" + ply.getName() + "'");
                    if( game_mode && ply instanceof Computer )
                        ply.printHandHidden();
                    else
                        ply.printHand();
                    
                    // Prints to console board state
                    Util.print("Type anything to show board... whenever you are ready!");
                    in.nextLine();
                    board.printBoard();
                    
                    // Call abstract method that runs the logic depending on the instance of Player. Run-time Polymorphism!
                    // Store data in a ADT for easy access and use.
                    Player.PlayData pdata = ply.play(in, players, board);
                    
                    // Check if the player passed 
                    if ( ply.hasPassed() ) {
                        continue;
                    } else {
                        // Move on to board logic
                        board.playBoard(pdata);
                    }
                }else{
                    Util.print("Player %s has passed. Skipping turn!", ply.getName());
                }
                
                if( game_mode )
                    Util.clearConsoleConfirm(in);
            }
        }
        
        // Store and calculate round data
        round_data[current_round-1] = new Round();
        Util.printSeparator2("Round Winner");

        if( round_data[current_round-1].draw ){
            Util.print("There was a draw! Consiting of %d players! These players get a round win!", round_data[current_round-1].winners.length);
            for ( Player winner : round_data[current_round-1].winners ) {
                Util.print("One of the winners of this round was %s with a total power of %d", winner.getName(), board.getTotalPlayerPower(winner));
                winner.setRoundsWon(winner.getRoundsWon() + 1);
            }
        }else{
            Player winner = round_data[current_round-1].winners[0];
            Util.print("The winner of this round was %s with a total power of %d", winner.getName(), board.getTotalPlayerPower(winner));
            winner.setRoundsWon(winner.getRoundsWon() + 1);
        }
        
        // reset player states
        for( Player ply : players ) 
            ply.resetGameState();
    }
    
    /**
     * Ends the game and calculates and displays the winner of the game.
     */
    private void gameOver(){
        Util.printSeparator2("Game is over! Round(s) results:");
        for( Round round : round_data ){
            System.out.println("----------[ Round: " + round.number + " ]------------");
            for (Entry<Player, Integer> entry : round.score.entrySet()) {
                Player ply = entry.getKey();
                int value = entry.getValue();
                System.out.println("[" + ply.getName() + "]: " + value );
            }
        }
        
        Util.printSeparator2("Game winner");
        
        int max = Integer.MIN_VALUE;
        int max_count = 0;
        Player winner = null;
        for( Player ply : players ){
            int rwon = ply.getRoundsWon();
            if( max_count == 0 || ply.getRoundsWon() > max ){
                max = rwon;
                winner = ply;
                max_count = 1;
            }else if ( rwon == max ){
                max_count++;
            }
        }
        
        if ( max_count > 1 ) {
            Util.printInBox("There was a overall game draw! Consiting of %d players! No one wins the game!", round_data[current_round-1].winners.length);
        }else{
            Util.printInBox("The winner of the game is: %s with %d round wins!", winner.getName(), winner.getRoundsWon());
        }
    }
    
    /**
     * Displays the current players game information.
     */
    public void printPlayers(){
        Util.printSeparator( " PLAYERS INFO " );
        for( Player ply : players ){
            System.out.println("----------[ Player ID: " + ply.getId() + " ]-----------");
            System.out.println("Name: " + ply.getName());
            System.out.println("Cards left: " + ply.getCardsLeft());
            System.out.println("Cards hand: " + ply.getHand().getCardsInHand());
            System.out.println("Cards deck: " + ply.getDeck().getCardsLeft());
            System.out.println("Cards board: " + board.getCardsOnBoard(ply));
            System.out.println("Total Power: " + board.getTotalPlayerPower(ply));
        }
    }
    
    /**
     * @return whether everyone has passed or not on the round.
     */
    private boolean everyoneHasNotPassed(){ 
        for( Player ply : players )
            if( ply.hasPassed() == false )
                return true;
        
        return false;
    }
    
    /**
     * @return if the game instance is in game-mode.
     */
    public boolean isGameMode() {
        return game_mode;
    }
}