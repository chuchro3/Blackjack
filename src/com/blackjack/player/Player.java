package com.blackjack.player;

import com.blackjack.util.Card;

import java.util.ArrayList;
import java.util.List;

/**
 *  Outlines basic behavior for a blackjack player.
 */
public abstract class Player {


    protected Bankroll bankroll;
    protected String name;

    //current round state
    public List<Card> currentHand;
    public List<String> intentions;
    public int bet;
    public int insurance;
    public boolean isDone;
    public boolean hasHidden;

    public Player(String name, int bankroll) {
        this.name = name;
        this.bankroll = new Bankroll(bankroll);
        resetState();
    }

    public Player(String name, Bankroll bankroll) {
        this.name = name;
        this.bankroll = bankroll;
        resetState();
    }

    public abstract Player split();

    public void resetState() {
        this.currentHand = new ArrayList<>();
        this.intentions = new ArrayList<>();
        this.intentions.add("hit");
        this.intentions.add("stand");
        this.bet = 0;
        this.insurance = 0;
        this.isDone = false;
        this.hasHidden = false;
    }

    public Bankroll getBankroll() {
        return this.bankroll;
    }

    public String getName() {
        return this.name;
    }

    public String getIntentionsString() {
        StringBuilder intentionBuilder = new StringBuilder();
        for (String intention : intentions) {
            intentionBuilder.append(" ");
            intentionBuilder.append(intention);
        }
        return intentionBuilder.toString();
    }

    /*
     * If the player has a hidden card, this will reveal it,
     * and vice-versa
     */
    public void flipHidden() {
        this.hasHidden = !this.hasHidden;
    }

    public String toString() {
        StringBuilder nameBuilder = new StringBuilder();
        nameBuilder.append(name);
        nameBuilder.append(" $");
        nameBuilder.append(bankroll);
        for(int i=0;i<currentHand.size();i++) {
            Card card = currentHand.get(i);
            nameBuilder.append(" ");
            if (i == currentHand.size()-1 && hasHidden) {
                //don't reveal last card
                nameBuilder.append("--");
            } else {
                nameBuilder.append(card.toString());
            }
        }
        return nameBuilder.toString();
    }

    public void incrementBankroll(int profit) {
        this.bankroll.value += profit;
    }

    public abstract void placeBet();

    public abstract String getIntention();

    /**
     * Object wrapper for bankroll value
     */
    public class Bankroll {

        public Integer value;

        public Bankroll(int value) {
            this.value = value;
        }

        public String toString() {
            return value.toString();
        }
    }

}
