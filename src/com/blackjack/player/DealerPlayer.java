package com.blackjack.player;

import com.blackjack.util.Card;

/**
 * represents the rules followed by the dealer player
 */
public class DealerPlayer extends Player {
    public DealerPlayer(String name) {
        super(name, 1);
    }

    @Override
    public Player split() {
        return null;
    }

    public void resetState() {
        super.resetState();

        this.hasHidden = true;
    }

    public String toString() {
        StringBuilder nameBuilder = new StringBuilder();
        nameBuilder.append(name);
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

    @Override
    public void placeBet() {}

    @Override
    public String getIntention() {
        String intention = "stand";
        if (Card.getScore(currentHand) < 17) {
            intention = "hit";
        }
        System.out.println("Dealer decides to " + intention);
        return intention;
    }
}
