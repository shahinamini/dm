package io;

import domain.Coordinates;

import java.io.IOException;
import java.util.List;

/**
 * Created by shn on 20/06/2016.
 */
public interface BoardParser {

    Coordinates getBoardSize();

    List<Coordinates> getTreasures();

    List<Coordinates> getMountains();

    void parse() throws IOException;
}
