package domain;

/**
 * Très simple classe représentant une montagne.
 *
 * Created by shn on 20/06/2016.
 */
public class Mountain {
    private final Coordinates coordinates;

    Mountain(Coordinates coordinates) {
        this.coordinates = coordinates;
    }

    public Coordinates getCoordinates() {
        return coordinates;
    }
}
