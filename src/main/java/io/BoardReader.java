package io;

import domain.Board;
import domain.Coordinates;

import java.io.IOException;
import java.util.List;

/**
 * Created by shn on 20/06/2016.
 */
public interface BoardReader {

    void read() throws IOException;

    Board getBoard();
}
