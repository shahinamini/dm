package io.impl;

import domain.Adventurer;
import domain.Coordinates;
import domain.Move;
import domain.Orientation;
import io.AdventurerReader;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

/**
 * Created by shn on 20/06/2016.
 */
public class AdventurerReaderImpl implements AdventurerReader {

    private String pathname;
    private List<Adventurer> adventurers;

    public AdventurerReaderImpl(String pathname) {
        this.pathname = pathname;
        this.adventurers = new ArrayList<>();
    }

    public List<Adventurer> getAdventurers() {
        return adventurers;
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

            if (lineContents.length < 5) {
                System.out.println("Line number " + lineNumber + " ignored.");
                System.out.println("Not enough arguments to set an adventurer.");
                continue;
            }

            String name = lineContents[0];

            Coordinates position;
            try {
                position = new Coordinates(Integer.parseInt(lineContents[1]),
                        Integer.parseInt(lineContents[2]));
            } catch (NumberFormatException ex) {
                System.out.println("Line number " + lineNumber + " ignored.");
                System.out.println("Invalid numbers.");
                continue;
            }

            Orientation orientation;
            switch (lineContents[3].toUpperCase().charAt(0)) {
                case 'N':
                    orientation = Orientation.NORTH;
                    break;
                case 'E':
                    orientation = Orientation.EAST;
                    break;
                case 'S':
                    orientation = Orientation.SOUTH;
                    break;
                case 'O':
                case 'W':
                    orientation = Orientation.WEST;
                    break;
                default:
                    System.out.println("Line number " + lineNumber + " ignored.");
                    System.out.println("Invalid orientation.");
                    continue;
            }

            List<Move> moves = new ArrayList<>();
            String movesString = lineContents[4].toUpperCase();
            for (int i = 0; i < movesString.length(); i++)
                switch (lineContents[4].toUpperCase().charAt(i)) {
                    case 'A':
                    case 'F':
                        moves.add(Move.FORWARD);
                        break;
                    case 'D':
                    case 'R':
                        moves.add(Move.RIGHT);
                        break;
                    case 'G':
                    case 'L':
                        moves.add(Move.LEFT);
                        break;
                    default:
                        System.out.println("Line " + lineNumber + ": character '" + lineContents[4].charAt(i) +
                                "' ignored.");
                        System.out.println("Not a valid move.");
                }
            adventurers.add(new Adventurer(name, position, orientation, moves));

            Adventurer lastAdventurer = adventurers.get(adventurers.size() - 1);
            System.out.println("Adventurer " + lastAdventurer.getName() +
                    " added at position " + lastAdventurer.getInitialPosition().toString() +
                    " facing " + lastAdventurer.getInitialOrientation() + ".");
            System.out.print("His next moves are:");
            for (Move move : lastAdventurer.getMoveQueue())
                System.out.print(" " + move);
            System.out.println(".");
        }

        scanner.close();
    }
}

