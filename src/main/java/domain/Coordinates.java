package domain;

/**
 * Simple classe représentant les coordonnées d'une case, d'un trésor, d'une montagne ou d'un aventurier sur une carte.
 *
 * Created by shn on 20/06/2016.
 */
public class Coordinates {
    private final int x;
    private final int y;

    public Coordinates(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public String toString() {
        return "(" + x + "," + y + ")";
    }
}
