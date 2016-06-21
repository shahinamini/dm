package domain;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * Classe qui représente les cases de la carte.
 * Les case peuvent contenir une montagne (Mountain), des trésors (Treasure) et un aventurier (Adventurer).
 *
 * Created by shn on 20/06/2016.
 */
public class Square {
    private final Coordinates coordinates;
    private Mountain mountain;
    private volatile List<Treasure> treasures;
    private volatile Adventurer occupant;

    public Square(Coordinates coordinates) {
        this.coordinates = coordinates;
        this.mountain = null;
        this.treasures = new CopyOnWriteArrayList<Treasure>();
        this.occupant = null;
    }

    public void setMountain(Mountain mountain) {
        this.mountain = mountain;
    }

    public Coordinates getCoordinates() {
        return coordinates;
    }

    public void addTreasure(Treasure treasure) {
        treasures.add(treasure);
    }

    public void clearTreasures() {
        treasures.clear();
    }

    public void setOccupant(Adventurer adventurer) {
        if (this.isMountain() || this.isOccupied())
            throw new RuntimeException(adventurer.getName() + " attempted to enter " +
                    (this.isMountain() ? "a mountain." : "a square occupied by " + occupant.getName() + "."));

        occupant = adventurer;
    }

    public void clearOccupant() {
        this.occupant = null;
    }

    public Adventurer getOccupant() {
        return occupant;
    }

    public Mountain getMountain() {
        return mountain;
    }

    public boolean isMountain() {
        return (mountain != null);
    }

    public synchronized boolean isOccupied() {
        return (occupant != null);
    }

    public synchronized List<Treasure> getTreasures() {
        return treasures;
    }

    public void setAdventurer(Adventurer adventurer) {
        if (this.isMountain() || this.isOccupied())
            throw new RuntimeException(adventurer.getName() + " attempted to enter " +
                    (this.isMountain() ? "a mountain." : "a square occupied by " + occupant.getName() + "."));

        occupant = adventurer;
    }

    public void letGo(Adventurer adventurer) {
        if (occupant != adventurer)
            throw new RuntimeException(adventurer.getName() + " attempted to leave a square he is not on.");

        occupant = null;
    }

    public void removeTreasures() {
        treasures.clear();
    }
}
