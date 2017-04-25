package com.blackjack.intention;

import com.blackjack.player.Player;
import com.blackjack.util.Deck;

import java.util.List;

/**
 * Represents particular actions that a player can take on their turn
 */
public interface Intention {

    public void applyIntention(List<Player> players, int playerIndex, Deck deck);

}
