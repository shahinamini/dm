package io.impl;

import io.ResultsWriter;
import services.Player;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

/**
 * Created by shn on 22/06/2016.
 */
public class ResultsWriterImpl extends PrintWriter implements ResultsWriter {

    public ResultsWriterImpl(String pathname) throws IOException {
        super(new BufferedWriter(new FileWriter(pathname)));
    }

    public void writeLn(Player player) throws IOException {
        if (player == null) return;

        String name = player.getName();
        int x = player.getPosition().getX();
        int y = player.getPosition().getY();
        int treasures = player.howManyTreasuresFound();
        char facing = '?';
        switch (player.getOrientation()) {
            case NORTH:
                facing = 'N';
                break;
            case EAST:
                facing = 'E';
                break;
            case SOUTH:
                facing = 'S';
                break;
            case WEST:
                facing = 'O';
                break;
        }

        println(name + " " + x + "-" + y + " " + facing + " " + treasures);
    }
}
