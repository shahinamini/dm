package domain;

import java.util.List;

/**
 * Created by shn on 05/07/2016.
 */
public interface AbstractSquare {
    boolean isCrossable();
    int getTreasures();
    Adventurer getAdventurer();
}