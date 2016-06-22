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
    private final List<Treasure> treasures;
    private volatile Adventurer occupant;

    public Square(Coordinates coordinates) {
        this.coordinates = coordinates;
        this.mountain = null;
        this.treasures = new CopyOnWriteArrayList<>();
        this.occupant = null;
    }

    public Coordinates getCoordinates() {
        return coordinates;
    }

    public void setMountain(Mountain mountain) {
        this.mountain = mountain;
    }

    public Mountain getMountain() {
        return mountain;
    }

    public void addTreasure(Treasure treasure) {
        treasures.add(treasure);
    }

    public void clearTreasures() {
        treasures.clear();
    }

    public List<Treasure> getTreasures() {
        return treasures;
    }

    public void setOccupant(Adventurer adventurer) {
        occupant = adventurer;
    }

    public Adventurer getOccupant() {
        return occupant;
    }

    public boolean isMountain() {
        return (mountain != null);
    }

    public boolean isOccupied() {
        return (occupant != null);
    }
}
