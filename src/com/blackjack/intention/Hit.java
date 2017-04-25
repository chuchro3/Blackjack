package com.blackjack.intention;

import com.blackjack.player.Player;
import com.blackjack.util.Card;
import com.blackjack.util.Deck;

import java.util.List;

/**
 * contains logic for getting another card from dealer
 */
public class Hit implements Intention {


    @Override
    public void applyIntention(List<Player> players, int playerIndex, Deck deck) {
        Player player = players.get(playerIndex);
        player.intentions.remove("doubleDown");
        player.intentions.remove("insurance");
        player.intentions.remove("splitPair");
        player.currentHand.add(deck.getNext());
        if (Card.getScore(player.currentHand) > 21) {
            System.out.println("Bust!");
            player.isDone = true;
        }
    }
}
