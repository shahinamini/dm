package io;

import services.Player;

import java.io.IOException;

/**
 * Created by shn on 21/06/2016.
 */
public interface ResultsWriter {

    void writeLn(Player player) throws IOException;

    void close();
}
