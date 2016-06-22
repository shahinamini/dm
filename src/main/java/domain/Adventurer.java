package domain;

import java.util.ArrayList;
import java.util.List;

/**
 * Classe qui garde les informations d'un aventurier.
 * Ces information sont indépendant d'un jeu particulier.
 * La classe Player de service les utilise pour créer un joueur Runnable.
 *
 * Created by shn on 20/06/2016.
 */
public class Adventurer {

    private final String name;
    private final Coordinates initialPosition;
    private final Orientation initialOrientation;
    private final List<Move> moveQueue;
    private final List<Treasure> treasuresFound;

    public Adventurer(String name, Coordinates initialPosition, Orientation initialOrientation, List<Move> moveQueue) {
        this.name = name;
        this.initialPosition = initialPosition;
        this.initialOrientation = initialOrientation;
        this.moveQueue = moveQueue;
        this.treasuresFound = new ArrayList<>();
    }

    public String getName() {
        return name;
    }

    public Coordinates getInitialPosition() {
        return initialPosition;
    }

    public Orientation getInitialOrientation() {
        return initialOrientation;
    }

    public List<Move> getMoveQueue() {
        return moveQueue;
    }

    public void addAllTreasures(List<Treasure> treasuresNewlyFound) {
        this.treasuresFound.addAll(treasuresNewlyFound);
    }
}
