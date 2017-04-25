package com.blackjack.intention;

import com.blackjack.player.Player;
import com.blackjack.util.Deck;

import java.util.List;

/**
 * Handles double down logic
 */
public class DoubleDown implements Intention {

    @Override
    public void applyIntention(List<Player> players, int playerIndex, Deck deck) {
        Player player = players.get(playerIndex);
        player.intentions.remove("doubleDown");
        player.intentions.remove("insurance");
        player.intentions.remove("splitPair");
        player.incrementBankroll(-player.bet);
        player.bet *= 2;
        player.currentHand.add(deck.getNext());
        player.flipHidden();
        player.isDone = true;
    }
}
