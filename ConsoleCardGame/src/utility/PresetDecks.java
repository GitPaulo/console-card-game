package utility;

import cards.Attribute;
import cards.AttributeType;
import cards.Card;
import cards.Deck;

/**
 * This class is used to load preset decks into the game.
 * Contains static methods to load the decks.
 * @author paulo
 */
public final class PresetDecks {
    /**
     * Loads the elven preset deck! 
     * @param deck
     * @return the preset deck
     */
    public final static Deck loadElvenDeck( Deck deck ){
        // 1
        deck.addCard(new Card( 
                "Dol Blathanna Protector",
                "As long as we stand, no human foot shall trample Dol Blathanna's meadows.",
                24,
                new Attribute[]{
                        new Attribute("War Cry", "", 10, 1, false, 0, AttributeType.BUFF),
                        new Attribute("Percise Shot", "", 40, 1, false, 0, AttributeType.INFLICTING),
                        new Attribute("Sensibility", "", 15, 3, true, 2, AttributeType.RESILIANCE)
                }   
            ).setOwner(deck.getOwner()) // Assumes that the deck has an owner. But since the logic was made for it to have an owner. This is fine.
        );
        //2
        deck.addCard(new Card(
                "Elf Infantry",
                "Elf infantry unit. They are... fast very fast.",
                45,
                new Attribute[]{
                        new Attribute("War Cry", "", 10, 1, false, 0, AttributeType.BUFF),
                        new Attribute("Quick Attack", "", 5, 5, false, 0, AttributeType.INFLICTING),
                        new Attribute("Sensibility", "", 15, 3, true, 2, AttributeType.RESILIANCE)
                }   
            ).setOwner(deck.getOwner())
        );
        // 3
        deck.addCard(new Card(
                "Elf Commander",
                "Commander of the elf army. Hates humans! Hates meat too. Maybe related?",
                40,
                new Attribute[]{
                        new Attribute("War Cry", "", 10, 1, false, 0, AttributeType.BUFF),
                        new Attribute("Regroup, charge!", "", 15, 3, false, 0, AttributeType.INFLICTING),
                        new Attribute("Loyalty!", "", 20, 3, false, 0, AttributeType.RESILIANCE)
                }   
            ).setOwner(deck.getOwner())
        );
        // 4
        deck.addCard(new Card(
                "Elf King",
                "King and ruler of the elf kingdom.",
                60,
                new Attribute[]{
                        new Attribute("Peace and prosperity", "", 5, 1, false, 0, AttributeType.BUFF),
                        new Attribute("King's word", "", 17, 3, false, 0, AttributeType.INFLICTING),
                        new Attribute("Immortality", "", 50, 2, false, 0, AttributeType.RESILIANCE)
                }   
            ).setOwner(deck.getOwner())
        );
        //5
        deck.addCard(new Card(
                "Elf Queen",
                "Queen of the beautiful kingdom of the elfs.",
                57,
                new Attribute[]{
                        new Attribute("Queen's Love", "", 8, 3, false, 0, AttributeType.BUFF),
                        new Attribute("Queen's Beauty", "", 20, 2, false, 0, AttributeType.INFLICTING),
                        new Attribute("Peace", "", 200, 1, true, 2, AttributeType.RESILIANCE)
                }   
            ).setOwner(deck.getOwner())
        );
        return deck;
    }
    
    /**
     * Loads the pirate's preset deck!
     * @param deck
     * @return the preset deck
     */
    public final static Deck loadPirateDeck( Deck deck ){
        //1
        deck.addCard(new Card(
                "Sailor",
                "Just a man part of a ship.",
                30,
                new Attribute[]{
                        new Attribute("Ahoy!", "", 20, 3, true, 4, AttributeType.BUFF),
                        new Attribute("Cannon ball!", "", 5, 2, false, 0, AttributeType.INFLICTING),
                        new Attribute("Pirate's true love is his ship.", "", 1, 1, false, 0, AttributeType.RESILIANCE)
                }   
            ).setOwner(deck.getOwner())
        );
        // 2
        deck.addCard(new Card(
                "Quarter Master",
                "Second in command, when it comes to a ship.",
                45,
                new Attribute[]{
                        new Attribute("Ahoy!", "", 20, 3, true, 4, AttributeType.BUFF),
                        new Attribute("Hand's on deck!", "", 10, 2, false, 0, AttributeType.INFLICTING),
                        new Attribute("Pirate's true love is his ship.", "", 1, 1, false, 0, AttributeType.RESILIANCE)
                }   
            ).setOwner(deck.getOwner())
        );
        // 3
        deck.addCard(new Card(
                "Captain",
                "Captain of this ship is here!",
                60,
                new Attribute[]{
                        new Attribute("Captain. Ahoy!", "", 20, 3, true, 2, AttributeType.BUFF),
                        new Attribute("Cannon balls!", "", 20, 2, false, 0, AttributeType.INFLICTING),
                        new Attribute("Pirate's true love is his ship.", "", 1, 1, false, 0, AttributeType.RESILIANCE)
                }   
            ).setOwner(deck.getOwner())
        );
        // 4
        deck.addCard(new Card(
                "Black Beard",
                "The captain of his legendary ship.",
                75,
                new Attribute[]{
                        new Attribute("Black Beard. Ahoy!", "", 30, 3, true, 2, AttributeType.BUFF),
                        new Attribute("Cannon balls!", "", 25, 2, false, 0, AttributeType.INFLICTING),
                        new Attribute("Pirate's true love is his ship.", "", 1, 1, false, 0,AttributeType.RESILIANCE)
                }   
            ).setOwner(deck.getOwner())
        );
        // 5
        deck.addCard(new Card(
                "Pirate King",
                "Not the guy from one piece.",
                90,
                new Attribute[]{
                        new Attribute("King of the 7 Seas", "", 100, 1, false, 0, AttributeType.BUFF),
                        new Attribute("Cannon balls!", "", 25, 2, false, 0, AttributeType.INFLICTING),
                        new Attribute("Pirate's true love is his ship.", "", 1, 1, false, 0, AttributeType.RESILIANCE)
                }     
            ).setOwner(deck.getOwner())
        );
        return deck;
    }
    
    /**
     * Loads the Kingdom's preset deck.
     * @param deck
     * @return
     */
    public final static Deck loadKingdomDeck( Deck deck ){
       //1 
       deck.addCard(new Card(
                "Peasent",
                "Just a man. Part of a big kingdom.",
                1,
                new Attribute[]{
                        new Attribute("In god we trust.", "", 3, 10, false, 0, AttributeType.BUFF),
                        new Attribute("To arms!", "", 10, 1, false, 0, AttributeType.INFLICTING),
                        new Attribute("Tax paying", "", 5, 2, false, 0, AttributeType.RESILIANCE)
                }   
            ).setOwner(deck.getOwner())
        );
        // 2
        deck.addCard(new Card(
                "Assasin",
                "Peace cannot be achieved without bloodshed.",
                30,
                new Attribute[]{
                        new Attribute("Silence", "", 7, 3, false, 0, AttributeType.BUFF),
                        new Attribute("Assasination", "", 100, 1, false, 0, AttributeType.INFLICTING),
                        new Attribute("Death is the way out.", "", 1, 1, false, 0, AttributeType.RESILIANCE)
                }   
            ).setOwner(deck.getOwner())
        );
        // 3
        deck.addCard(new Card(
                "Knight",
                "Loyal soldier to crown.",
                90,
                new Attribute[]{
                        new Attribute("Loyalty and Honor!", "", 20, 2, false, 0, AttributeType.BUFF),
                        new Attribute("Duel!", "", 30, 2, false, 0, AttributeType.INFLICTING),
                        new Attribute("To the death!", "", 20, 1, false, 0, AttributeType.RESILIANCE)
                }   
            ).setOwner(deck.getOwner())
        );
        // 4
        deck.addCard(new Card(
                "Commander",
                "Commander of the forces of the kingdom.",
                105,
                new Attribute[]{
                        new Attribute("Charge!", "", 30, 2, false, 0, AttributeType.BUFF),
                        new Attribute("Spears!", "", 12, 4, false, 0, AttributeType.INFLICTING),
                        new Attribute("Rout.", "", 1, 1, false, 0, AttributeType.RESILIANCE)
                }   
            ).setOwner(deck.getOwner())
        );
        // 5
        deck.addCard(new Card(
                "Prince",
                "Eldest son, heir to the crown. Fights well the lad.",
                60,
                new Attribute[]{
                        new Attribute("Confort", "", 10, 3, false, 0, AttributeType.BUFF),
                        new Attribute("Crown, isn't just for show", "", 20, 2, false, 0, AttributeType.INFLICTING),
                        new Attribute("Death of a monarch.", "", 20, 2, false, 0, AttributeType.RESILIANCE)
                }     
            ).setOwner(deck.getOwner())
        );
        return deck;
    }
}
