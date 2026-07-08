package com.redshift.ShadowDarkCalculator.cards;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * A simple deck of cards that can randomly draw numbered cards without replacement.
 */

public class DeckOfCards {

    private final List<Integer> cards = new ArrayList<>();
    private final int size;

    public DeckOfCards(int size) {
        if (size < 1) throw new IllegalArgumentException("Deck must have at least one card in it.");
        this.size = size;
        shuffle();
    }

    /**
     * Draws a card from the top of the deck. Throws an exception if there are none and it
     * needs to be shuffled.
     */

    public int draw() {
        if (cards.isEmpty()) throw new IllegalStateException("Deck of " + size + " cards is empty!");
        return cards.removeFirst();
    }

    /**
     * Randomly shuffles all cards back into the deck.
     */

    public void shuffle() {
        cards.clear();

        for (int i=0; i < size; i++) {
            cards.add(i + 1);
        }

        Collections.shuffle(cards);
    }

}
