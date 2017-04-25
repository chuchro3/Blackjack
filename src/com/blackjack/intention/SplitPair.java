package com.blackjack.intention;

import com.blackjack.player.Player;
import com.blackjack.util.Deck;

import java.util.List;

/**
 * Contains logic for splitting pairs
 */
public class SplitPair implements Intention {

    @Override
    public void applyIntention(List<Player> players, int playerIndex, Deck deck) {
        Player player = players.get(playerIndex);
        player.intentions.remove("insurance");
        player.intentions.remove("splitPair");
        Player splitPlayer = player.split();
        player.currentHand.remove(1);
        players.add(playerIndex+1, splitPlayer);
    }
}
