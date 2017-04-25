package com.blackjack;

import com.blackjack.intention.*;
import com.blackjack.player.DealerPlayer;
import com.blackjack.player.HumanPlayer;
import com.blackjack.player.Player;
import com.blackjack.player.RandomPlayer;
import com.blackjack.util.Card;
import com.blackjack.util.Deck;

import java.util.*;

/**
 * Contains logic that runs the actual game
 */
public class Blackjack implements Runnable {

    private final List<Player> players;
    private final Player dealer;
    private final Deck deck;

    private static final int NUM_DECKS = 6;

    //hold singletons for intention functionalities
    private static final Map<String, Intention> intentionFunctions = new HashMap<>();
    static {
        intentionFunctions.put("hit", new Hit());
        intentionFunctions.put("stand", new Stand());
        intentionFunctions.put("insurance", new Insurance());
        intentionFunctions.put("doubleDown", new DoubleDown());
        intentionFunctions.put("splitPair", new SplitPair());
    }

    public Blackjack(int numPlayers, boolean include_AI) {
        this.deck = new Deck(NUM_DECKS);
        this.players = new ArrayList<>();

        for(int i = 0;i < numPlayers; i++) {
            this.players.add(new HumanPlayer("P" + i, 1000));
        }
        this.dealer = new DealerPlayer("Dealer");
        this.players.add(dealer);

        if (include_AI) {
            players.add(0, new RandomPlayer("Rando", 1000));
        }

    }

    /*
     * main looping thread to run continuous rounds of the game
     */
    @Override
    public void run() {
        Scanner scan = new Scanner(System.in);
        boolean playing = true;
        while (playing) {

            setupPlayers();

            playRound();

            System.out.print("Play again? (y/n): ");
            String response = "";
            while (!response.equals("y") && !response.equals("n")) {
                response = scan.next();
            }
            System.out.println("\n####################");

            if (response.equals("n")) {
                playing = false;
            }

        }
        System.out.println("\nGoodbye.");
    }

    /*
     * merge any split players from previous round
     * remove players with no bankroll remaining
     * reset player's state variables
     */
    public void setupPlayers() {
        for(int i =0;i<players.size();i++) {
            if (players.get(i).toString().contains("split")) {
                players.remove(i);
            }
            if (players.get(i).getBankroll().value <= 0) {
                players.remove(i);
                continue;
            }
            players.get(i).resetState();
        }
    }

    /*
     * run through a single round of the game
     */
    public void playRound() {
        deck.checkShuffle();

        takeBets();

        deal();

        printState();

        boolean dealerNatural = checkNaturals();
        if (dealerNatural) {
            return;
        }

        insurance();

        splitPairs();

        doubleDown();

        signalIntentions();

        settlement();
    }

    /*
     * take bets from every player
     */
    public void takeBets() {
        for(int i =0;i<players.size();i++) {
            players.get(i).placeBet();
            players.get(i).incrementBankroll(-players.get(i).bet);
        }
    }

    /*
     * deal 2 cards to every player
     *
     */
    public void deal() {
        for(int c =0;c<2;c++) {
            for (int i = 0; i < players.size(); i++) {
                players.get(i).currentHand.add(deck.getNext());
            }
        }
    }

    /*
     * check for blackjacks
     */
    public boolean checkNaturals() {
        boolean dealerNatural = false;
        boolean blackjack = false;
        for (int i = 0; i < players.size()-1; i++) {
            if (Card.getScore(players.get(i).currentHand) == 21) {
                blackjack = true;
                System.out.println(players.get(i).getName() + " has a blackjack!");
                if (!dealerNatural && dealer.currentHand.get(0).getValue() >= 10) {
                    dealer.flipHidden();
                    dealerNatural = Card.getScore(dealer.currentHand) == 21;
                }
                if (!dealerNatural) {
                    players.get(i).incrementBankroll((int) (2.5 * players.get(i).bet));
                }
                players.get(i).isDone = true;
            }
        }
        if (blackjack) {
            printState();
        }
        return dealerNatural;
    }

    /*
     * enables insurance if dealer shows an ace
     */
    public void insurance() {
        if (dealer.currentHand.get(0).getValue() != 11) {
            return;
        }
        for (int i = 0; i < players.size() - 1; i++) {
            players.get(i).intentions.add("insurance");
        }
    }

    /*
     * enables splitting if card numbers are equal
     */
    public void splitPairs() {
        for (int i = 0; i < players.size() - 1; i++) {
            if (players.get(i).currentHand.get(0).getCardNumber() == players.get(i).currentHand.get(1).getCardNumber()) {
                players.get(i).intentions.add("splitPair");
            }
        }
    }

    /*
     * enables double down if bankroll is sufficient
     */
    public void doubleDown() {
        for (int i = 0; i < players.size() - 1; i++) {
            Player player = players.get(i);
            if (player.getBankroll().value >= player.bet) {
                player.intentions.add("doubleDown");
            }
        }
    }

    public void signalIntentions() {
        for (int i = 0; i < players.size(); i++) {
            Player player = players.get(i);
            while (!player.isDone) {
                if (i == players.size()-1 && dealer.hasHidden) {
                    dealer.flipHidden();
                }
                String intention = player.getIntention();
                intentionFunctions.get(intention).applyIntention(players, i, deck);
                printState();
            }
        }
    }

    public void settlement() {
        int dealerScore = Card.getScore(dealer.currentHand);
        boolean collectInsurance = dealerScore == 21;
        for (int i = 0; i < players.size() - 1; i++) {
            Player player = players.get(i);
            if (player.hasHidden) {
                player.flipHidden();
            }
            int profit = 0;
            if (collectInsurance) {
                profit += 2*player.insurance;
            }
            int playerScore = Card.getScore(player.currentHand);
            if (playerScore == dealerScore && playerScore <= 21) { //tie
                profit += player.bet;
            } else if ( (playerScore > dealerScore || dealerScore > 21) && playerScore <= 21) { //win
                profit += 2*player.bet;
            }
            player.incrementBankroll(profit);
            System.out.println(player.getName() + " nets " + (profit-player.bet-player.insurance));
        }
        printState();
    }

    public void printState() {
        System.out.println();
        for (int i = 0; i < players.size(); i++) {
            System.out.println(players.get(i));
        }
        System.out.println();
    }
}
