package com.blackjack.intention;

import com.blackjack.player.Player;
import com.blackjack.util.Deck;

import java.util.List;

/**
 * Handles insurance logic
 */
public class Insurance implements Intention {

    @Override
    public void applyIntention(List<Player> players, int playerIndex, Deck deck) {
        Player player = players.get(playerIndex);
        player.intentions.remove("insurance");
        player.insurance = player.bet / 2;
        player.incrementBankroll(-player.insurance);
    }
}
