package com.blackjack.util;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Contains a representation for a single playing card
 */
public class Card {

    private final int value;
    private final String stringRepr;
    private final boolean isAce;
    private final int cardNumber;

    private static final char SPADE = '\u2660';
    private static final char DIAMOND = '\u2666';
    private static final char CLUB = '\u2663';
    private static final char HEART = '\u2764';

    private static final Map<Integer, Character> valToFace = new HashMap<>();
    static {
        valToFace.put(11, 'J');
        valToFace.put(12, 'Q');
        valToFace.put(13, 'K');
        valToFace.put(14, 'A');
    }

    /*
     *  cardNum is a value from 0-51 representing each card in a 52 card deck.
     *  0-12 represents
     */
    public Card(int cardNum) {
        StringBuilder cardStringBuilder = new StringBuilder();
        int suit = cardNum / 13;
        switch (suit) {
            case 0:
                cardStringBuilder.append(SPADE);
                break;
            case 1:
                cardStringBuilder.append(DIAMOND);
                break;
            case 2:
                cardStringBuilder.append(CLUB);
                break;
            case 3:
                cardStringBuilder.append(HEART);
                break;
        }

        int val = cardNum % 13 + 2; //convert input number to value between [2,14]
        this.cardNumber = val;
        if (valToFace.containsKey(val)) {
            cardStringBuilder.append(valToFace.get(val));
        } else {
            cardStringBuilder.append(val);
        }
        this.stringRepr = cardStringBuilder.toString();

        if (val == 14) { //ace
            this.value = 11;
            this.isAce = true;
        } else { //2-K
            this.value = Math.min(10, val);
            this.isAce = false;
        }
        //System.out.println(cardNum + " " + this.stringRepr + " " + this.value);
    }

    public String toString() {
        return this.stringRepr;
    }

    public int getValue() {
        return this.value;
    }

    public boolean isAce() {
        return this.isAce;
    }

    public int getCardNumber() {
        return this.cardNumber;
    }

    /*
     * computes the score given the list of cards
     * Since a hand may contain an ace, we will return the score closest, but not over 21
     * an ace is assumed to be 11, unless our score puts us over 21.
     */
    public static int getScore(List<Card> cards) {
        int numAces = 0;
        int sum = 0;
        for (Card card : cards) {
            if (card.isAce()) {
                numAces++;
            }

            sum += card.getValue();
            if (numAces > 0 && sum > 21) {
                sum -= 10; //switch ace from 11 to 1 if we are over
                numAces--;
            }
        }
        return sum;
    }

}
