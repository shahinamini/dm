package domain;

/**
 * Les orientations.
 * Les méthodes left() et right() retournent l'orientation de 90° à gauche ou à droite de l'orientation actuelle.
 *
 * Created by shn on 20/06/2016.
 */
public enum Orientation {
    NORTH, EAST, SOUTH, WEST;

    private static Orientation[] nesw = values();

    public Orientation right() {
        return nesw[(this.ordinal() + 1) % 4];
    }

    public Orientation left() {
        return nesw[(this.ordinal() + 3) % 4];
    }
}
