package io.impl;

import domain.Board;
import domain.Coordinates;
import io.BoardReader;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

/**
 * Created by shn on 20/06/2016.
 */
public class BoardReaderImpl implements BoardReader {
    private String pathname;
    private Coordinates boardSize;
    private List<Coordinates> mountains;
    private List<Coordinates> treasures;

    public BoardReaderImpl(String filename) {
        this.pathname = filename;
        this.boardSize = null;
        this.mountains = new ArrayList<>();
        this.treasures = new ArrayList<>();
    }

    public void read() throws IOException {
        File boardFile = new File(pathname);
        Scanner scanner = new Scanner(boardFile);
        int lineNumber = 0;

        while (scanner.hasNextLine()) {
            lineNumber++;
            String line = scanner.nextLine().trim();

            System.out.println(lineNumber + ": " + line);

            if (line.length() == 0 || line.startsWith("#")) {
                System.out.println("Line number " + lineNumber + " ignored.");
                System.out.println("Line is a comment or empty.");
                continue;
            }

            String[] lineContents = line.split("[ -]");

            switch (line.toUpperCase().charAt(0)) {
                case 'C':
                    System.out.println("C");
                    if (boardSize != null) {
                        System.out.println("Line number " + lineNumber + " ignored.");
                        System.out.println("Size of the board is already set.");
                        break;
                    }
                    if (lineContents.length < 3) {
                        System.out.println("Line number " + lineNumber + " ignored.");
                        System.out.println("Not enough arguments to set the size of the board.");
                        break;
                    }
                    try {
                        boardSize = new Coordinates(Integer.parseInt(lineContents[1]),
                                Integer.parseInt(lineContents[2]));
                    } catch (NumberFormatException ex) {
                        System.out.println("Line number " + lineNumber + " ignored.");
                        System.out.println("Bad numbers.");
                        break;
                    }
                    System.out.println("Board size: " + boardSize.getX() + "x" + boardSize.getY());
                    break;
                case 'T':
                    System.out.println("T");
                    if (lineContents.length < 4) {
                        System.out.println("Line number " + lineNumber + " ignored.");
                        System.out.println("Not enough arguments to set a treasure.");
                        break;
                    }
                    try {
                        for (int i = 0; i < Integer.parseInt(lineContents[3]); i++) {
                            treasures.add(new Coordinates(Integer.parseInt(lineContents[1]),
                                    Integer.parseInt(lineContents[2])));
                            System.out.println("New treasure added at (" + Integer.parseInt(lineContents[1]) +
                                    "," + lineContents[2] + ").");
                        }
                    } catch (NumberFormatException ex) {
                        System.out.println("Line number " + lineNumber + " ignored.");
                        System.out.println("Bad numbers.");
                        break;
                    }
                    break;
                case 'M':
                    System.out.println("M");
                    if (lineContents.length < 3) {
                        System.out.println("Line number " + lineNumber + " ignored.");
                        System.out.println("Not enough arguments to set a mountain.");
                        break;
                    }
                    try {
                        mountains.add(new Coordinates(Integer.parseInt(lineContents[1]),
                                Integer.parseInt(lineContents[2])));
                        System.out.println("New mountain added at (" + Integer.parseInt(lineContents[1]) +
                                "," + lineContents[2] + ").");
                    } catch (NumberFormatException ex) {
                        System.out.println("Line number " + lineNumber + " ignored.");
                        System.out.println("Bad numbers.");
                        break;
                    }
                    break;
                default:
                    System.out.println("Line number " + lineNumber + " ignored.");
            }
        }
    }

    public Board getBoard() {
        return new Board(boardSize, mountains, treasures);
    }
}
