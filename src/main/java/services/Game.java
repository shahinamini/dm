package services;

import domain.Adventurer;
import domain.Coordinates;
import domain.Square;
import domain.Treasure;
import services.exceptions.GameException;

import java.util.List;

/**
 * Created by shn on 20/06/2016.
 */
public interface Game {
//    Square squareAt(Coordinates coordinates);

    void playerDone(Player player);

    void playerWaiting(Player player);

    void playerResumed(Player player);

    void report(Player player);

    void play();

    boolean hasSquareAt(Coordinates coordinates);

    Adventurer getOccupantAt(Coordinates coordinates);

    boolean hasMountainAt(Coordinates coordinates);

    boolean isOccupiedAt(Coordinates coordinates);

    List<Treasure> getTreasuresAt(Coordinates coordinates);

    void pleaseMoveWithCare(Player player, Coordinates position, Coordinates destination)
            throws GameException, InterruptedException;

    void letOut(Adventurer adventurer, Coordinates coordinates) throws GameException;

    void clearTreasuresAt(Coordinates coordinates);

}
