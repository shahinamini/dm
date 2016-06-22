package services;

import domain.Coordinates;
import domain.Treasure;
import io.ResultsWriter;
import services.exceptions.GameException;

import java.util.List;

/**
 * Created by shn on 20/06/2016.
 */
public interface Game {
//    Square squareAt(Coordinates coordinates);

    void play();

    List<Player> getResults();

    void pleaseMoveWithCare(Player player, Coordinates position, Coordinates destination)
            throws GameException, InterruptedException;

    void playerDone(Player player);

    void report(Player player);

    boolean isCrossableAt(Coordinates coordinates);

    List<Treasure> getTreasuresAt(Coordinates coordinates) throws GameException;

    void clearTreasuresAt(Coordinates coordinates) throws GameException;
}
