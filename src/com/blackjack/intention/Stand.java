package com.blackjack.intention;

import com.blackjack.player.Player;
import com.blackjack.util.Deck;

import java.util.List;

/**
 * Contains logic for the stand action
 */
public class Stand implements Intention {


    @Override
    public void applyIntention(List<Player> players, int playerIndex, Deck deck) {
        players.get(playerIndex).isDone = true;
    }
}
