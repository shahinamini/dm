package domain;

/**
 * Très simple classe représentant une montagne.
 *
 * Created by shn on 20/06/2016.
 */
public class Mountain implements AbstractSquare {

    @Override
    public boolean isCrossable() {
        return false;
    }

    @Override
    public int getTreasures() {
        return 0;
    }

    @Override
    public Adventurer getAdventurer() {
        return null;
    }
}
