package com.blackjack.player;

import java.util.Random;

/**
 * AI that makes random decisions
 */
public class RandomPlayer extends Player {

    public RandomPlayer(String name, int bankroll) {
        super(name, bankroll);
    }

    public RandomPlayer(String name, Bankroll bankroll) {
        super(name, bankroll);
    }

    @Override
    public Player split() {
        Player splitPlayer = new RandomPlayer(getName()+"-split", getBankroll());
        splitPlayer.currentHand.add(currentHand.get(1));
        splitPlayer.bet = bet;
        splitPlayer.incrementBankroll(-splitPlayer.bet);
        if (splitPlayer.getBankroll().value > splitPlayer.bet) {
            splitPlayer.intentions.add("doubleDown");
        }
        return splitPlayer;
    }

    @Override
    public void placeBet() {
        Random random = new Random();
        bet = random.nextInt(bankroll.value / 10) + 1;
        System.out.println("Rando bets $" + bet);
    }

    @Override
    public String getIntention() {
        Random random = new Random();
        int idx = random.nextInt(intentions.size());
        System.out.println("Rando decides to " + intentions.get(idx));
        return intentions.get(idx);
    }
}
