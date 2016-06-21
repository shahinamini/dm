package services;

import domain.Adventurer;

/**
 * Created by shn on 21/06/2016.
 */
public interface Player extends Runnable {
    Adventurer getAdventurer();

    String getName();

    void finishGame();

    void prompt(String message);
}
