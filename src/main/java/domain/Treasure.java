package domain;

/**
 * Classe représentant un trésor.
 *
 * Created by shn on 20/06/2016.
 */
public class Treasure {
    private final Coordinates coordinates;

    Treasure(Coordinates coordinates) {
        this.coordinates = coordinates;
    }

    public Coordinates getCoordinates() {
        return coordinates;
    }
}
