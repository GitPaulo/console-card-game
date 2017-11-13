package players;
import java.util.Random;
import java.util.Scanner;

import cards.Attribute;
import cards.Card;
import game.Board;
import utility.Util;

/**
 * Child class of Player.
 * Exists mostly to implement the play method and to define the 
 * type of computer.
 * Logic involved the Computer moves is done here.
 * @see {@link Player}
 * @see {@link ComputerType}
 * @author paulo
 *
 */
public class Computer extends Player {
    // instance constant
    private final ComputerType type;
    
    /**
     * Constructor
     * @param id the id of the computer player
     * @param name the name of the computer player
     * @param type the type of the computer player
     */
    public Computer(int id, String name, ComputerType type) {
        super(id, name);
        this.type = type;
    }
    
    /**
     * @return the type of the computer player
     */
    public ComputerType getType() {
        return type;
    }
    
    /**
     * Process the logic involved with the computer's turn play.
     * The reason this method was not split into 3 subclass is because of lack of functionality.
     * The classes would only have 1 method the overriding one.
     * @param in The scanner used by the game for input.
     * @param players The players part of the game.
     * @param board The board that the game has.
     */
    @Override
    public PlayData play(Scanner in, Player[] players, Board board) {
        Card card_played = null;
        Attribute at_played = null;
        Player targeted_ply = null;
        int[] targetids = null;
        switch ( type ) {
            case LOGICAL:
                int cindex0 = getHand().getFirstCardIndexFromHand();
                card_played = getHand().getCardFromHand(cindex0);
                Util.print("%s has selected the card %s to play!", getName(), card_played.getName());
                
                // selects infliction always
                at_played = card_played.getAttributes()[1];
                Util.print("%s has selected the attribute %s for the card %s!", getName(), at_played.getName(), card_played.getName());
                
                // always a enemy player (very basic but works)
                targeted_ply = (players[0] == this) ? players[1] : players[0];
                Util.print("%s has targeted the player %s's board!", getName(), targeted_ply.getName());
                
                // remove card from hand - always first card, dumb computer.
                playCard(cindex0);
                
                int at_played_num_targets0 = at_played.getNumTargets();
                int cards_on_selected_board0 = board.getCardsOnBoard(targeted_ply);
                if(  cards_on_selected_board0 == 0 ){
                    Util.print("There are no cards on %s's board! Placing your card on your board with base power!", targeted_ply.getName());
                    targetids = new int[0];
                }else{
                    targetids = new int[at_played_num_targets0];
                    for( int i = 0; i < at_played_num_targets0; i++ ){
                        int tindex = 1;
                        tindex = 0;
                        targetids[i] = tindex;
                        Util.print("%s has selected the target index %d's on %s board!", getName(), tindex, targeted_ply.getName());
                    }
                }
                break;
            case RANDOM:
                Random rn = new Random();
                int nhand = getHand().getCardsInHand();
                
                // deal with the case there is only 1 card in hand but can be in either index 1 or 0
                int cindex = -1;
                if ( nhand < 2 ){
                    cindex = getHand().getFirstCardIndexFromHand();
                } else {
                    cindex = rn.nextInt(nhand); 
                }
                
                card_played = getHand().getCardFromHand(cindex);
                Util.print("%s has selected the card %s to play!", getName(), card_played.getName());
                
                at_played = card_played.getAttributes()[rn.nextInt(Card.MAX_CARD_ATTRIBUTE)];
                Util.print("%s has selected the attribute %s for the card %s!", getName(), at_played.getName(), card_played.getName());
                 
                targeted_ply = players[rn.nextInt(players.length)];
                Util.print("%s has targeted the player %s's board!", getName(), targeted_ply.getName());
                
                // remove card from hand
                playCard(cindex);  
                
                int at_played_num_targets = at_played.getNumTargets();
                int cards_on_selected_board = board.getCardsOnBoard(targeted_ply);
                if(  cards_on_selected_board == 0 ){
                    Util.print("There are no cards on %s's board! Placing your card on your board with base power!", targeted_ply.getName());
                    targetids = new int[0];
                }else{
                    targetids = new int[at_played_num_targets];
                    for( int i = 0; i < at_played_num_targets; i++ ){
                        int tindex = 1;
                        tindex = rn.nextInt(cards_on_selected_board);
                        targetids[i] = tindex;
                        Util.print("%s has selected the target index %d's on %s board!", getName(), tindex, targeted_ply.getName());
                    }
                }
                break;   
            case DUMB:
                int cindex1 = getHand().getFirstCardIndexFromHand();
                card_played = getHand().getCardFromHand(cindex1);
                Util.print("%s has selected the card %s to play!", getName(), card_played.getName());
                
                at_played = card_played.getAttributes()[0];
                Util.print("%s has selected the attribute %s for the card %s!", getName(), at_played.getName(), card_played.getName());
                
                targeted_ply = players[0];
                Util.print("%s has targeted the player %s's board!", getName(), targeted_ply.getName());
                
                // remove card from hand - always first card, dumb computer.
                playCard(cindex1);
                
                int at_played_num_targets1 = at_played.getNumTargets();
                int cards_on_selected_board1 = board.getCardsOnBoard(targeted_ply);
                if(  cards_on_selected_board1 == 0 ){
                    Util.print("There are no cards on %s's board! Placing your card on your board with base power!", targeted_ply.getName());
                    targetids = new int[0];
                }else{
                    targetids = new int[at_played_num_targets1];
                    for( int i = 0; i < at_played_num_targets1; i++ ){
                        int tindex = 1;
                        tindex = 0;
                        targetids[i] = tindex;
                        Util.print("%s has selected the target index %d's on %s board!", getName(), tindex, targeted_ply.getName());
                    }
                }
                break;
            default:
                throw new Error("Invalid ComputerType when trying to play with the computer. Method: Computer.play()");
        }
        return new PlayData(this, card_played, at_played, targeted_ply, targetids);
    }
}
