package com.blackjack.util;

import java.util.Collections;
import java.util.LinkedList;

/**
 * Contains instances of all the cards to be played with
 * may contain multiple standard card decks
 *
 * Contains the state of the deck for dealing purposes
 */
public class Deck {

    private final LinkedList<Card> deck;
    private final int reshuffleCount;
    private int dealIndex;

    public Deck(int numDecks) {
        this.reshuffleCount = 40 * numDecks;
        this.dealIndex = reshuffleCount;
        this.deck = new LinkedList<>();
        for (int i=0;i<numDecks;i++) {
            for (int c=0;c<52;c++) {
                deck.push(new Card(c));
            }
        }
    }

    /*
     * returns the top card of the deck
     */
    public Card getNext() {
        Card nextCard = deck.poll();
        deck.offer(nextCard);
        dealIndex++;
        return nextCard;
    }

    /*
     * checks to see if a shuffle needs to be done
     * this occurs when we have dealt more than the reshuffleCount of the deck
     */
    public void checkShuffle() {
        if (dealIndex >= reshuffleCount) {
            System.out.println("\nShuffling!\n");
            Collections.shuffle(deck);
            dealIndex = 0;
        }
    }
}
