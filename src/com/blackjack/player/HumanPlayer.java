package com.blackjack.player;

import java.util.InputMismatchException;
import java.util.Scanner;

/**
 * Human implementation of the player
 */
public class HumanPlayer extends Player {

    public HumanPlayer(String name, int bankroll) {
        super(name, bankroll);
    }

    public HumanPlayer(String name, Bankroll bankroll) {
        super(name, bankroll);
    }

    @Override
    public Player split() {
        Player splitPlayer = new HumanPlayer(getName()+"-split", getBankroll());
        splitPlayer.currentHand.add(currentHand.get(1));
        splitPlayer.bet = bet;
        splitPlayer.incrementBankroll(-splitPlayer.bet);
        if (splitPlayer.getBankroll().value > splitPlayer.bet) {
            splitPlayer.intentions.add("doubleDown");
        }
        return splitPlayer;
    }


    public void resetState() {
        super.resetState();
    }
    @Override
    public void placeBet() {
        Scanner scan = new Scanner(System.in);
        System.out.print("Bet for " + this + ": ");
        int bet = 0;
        while (bet <= 0 || bet > this.bankroll.value) {
            try {
                bet = scan.nextInt();
            } catch(InputMismatchException e) {
                bet = 0;
                scan = new Scanner(System.in);
            }
        }
        System.out.println();
        this.bet = bet;
    }

    @Override
    public String getIntention() {
        System.out.println("Current turn: " + name);
        System.out.println("Enter legal intention from: " + getIntentionsString());
        Scanner scan = new Scanner(System.in);
        String intention = "";
        while (!intentions.contains(intention)) {
            intention = scan.next();
        }
        return intention;
    }

}
