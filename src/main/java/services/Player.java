package services;

import domain.Adventurer;
import domain.Coordinates;
import domain.Orientation;

/**
 * Created by shn on 21/06/2016.
 */
public interface Player extends Runnable {

    Adventurer getAdventurer();

    String getName();

    Coordinates getPosition();

    Orientation getOrientation();

    Integer howManyTreasuresFound();

    void prompt(String message);
}
