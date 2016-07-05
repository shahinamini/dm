package domain;

/**
 * Created by shn on 05/07/2016.
 */
public class Plain implements AbstractSquare {
    private int treasuers;
    private Adventurer occupant;

    public void setTreasuers(int treasures) {
        this.treasuers = treasures;
    }

    public Adventurer getOccupant() {
        return occupant;
    }

    public void setOccupant(Adventurer occupant) {
        this.occupant = occupant;
    }

    @Override
    public boolean isCrossable() {
        return true;
    }

    @Override
    public int getTreasures() {
        return treasuers;
    }

    @Override
    public Adventurer getAdventurer() {
        return null;
    }
}
