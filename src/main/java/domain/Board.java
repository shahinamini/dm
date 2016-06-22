package domain;

import domain.*;

import java.util.ArrayList;
import java.util.List;

/**
 * Classe qui contient les information d'une carte.
 * La classe GameImpl de services en déploie les informations pour en crée une partie de jeu.
 *
 * Created by shn on 20/06/2016.
 */
public class Board {
    private final Coordinates size;
    private final List<Mountain> mountains;
    private final List<Treasure> treasures;

    public Board(Coordinates size, List<Coordinates> mountainsCoordinates, List<Coordinates> treasuresCoordinates) {
        this.size = size;

        this.mountains = new ArrayList<>();
        for (Coordinates mountainCoordinates : mountainsCoordinates)
            if (this.hasSquare(mountainCoordinates))
                this.mountains.add(new Mountain(mountainCoordinates));
            else
                System.out.println("Mountain at " + mountainCoordinates + " ignored: not on the board.");

        this.treasures = new ArrayList<>();
        for (Coordinates treasureCoordinates : treasuresCoordinates)
            if (this.hasSquare(treasureCoordinates)) //                         CAN THERE BE TREASURES ON THE MOUNTAINS?
                this.treasures.add(new Treasure(treasureCoordinates));
            else
                System.out.println("Treasure at " + treasureCoordinates + " ignored: not on the board.");
    }

    public Coordinates getSize() {
        return size;
    }

    public List<Mountain> getMountains() {
        return mountains;
    }

    public List<Treasure> getTreasures() {
        return treasures;
    }

    private boolean hasSquare(Coordinates coordinates) {
        return coordinates.getX() > 0 && coordinates.getX() <= this.size.getX() &&
                coordinates.getY() > 0 && coordinates.getY() <= this.size.getY();
    }

}
