package services.impl;

import domain.*;
import services.Game;

import java.util.ArrayList;
import java.util.List;

import static java.lang.Thread.sleep;

/**
 * Created by shn on 20/06/2016.
 */
public class PlayerImpl implements services.Player {
    Adventurer adventurer;
    Coordinates position;
    Orientation orientation;
    List<Treasure> treasuresFoundInThisGame;
    List<Move> remainingMoves;
    Game game;
    int turnsPlayed;

    public PlayerImpl(Adventurer adventurer, Game game) {
        this.adventurer = adventurer;
        this.game = game;

        this.position = adventurer.getInitialPosition();
        this.orientation = adventurer.getInitialOrientation();
        this.remainingMoves = new ArrayList<Move>(adventurer.getMoveQueue());
        this.treasuresFoundInThisGame = new ArrayList<Treasure>();

        prompt("Set at " + position);
        turnsPlayed = 0;
    }

    public Adventurer getAdventurer() {
        return adventurer;
    }

    public void run() {
        boolean hasJustArrived = true;
        prompt("Starts at " + position + ".");

        while (!remainingMoves.isEmpty()) {
            if (hasJustArrived) {
                prompt("Looks for treasure in " + position + ".");
                List<Treasure> currentSquareTreasures = game.getTreasuresAt(position);
                if (!currentSquareTreasures.isEmpty()) {
                    prompt("Found treasure!");
                    treasuresFoundInThisGame.addAll(currentSquareTreasures);
                    game.clearTreasuresAt(position);

                    hasJustArrived = false;
                    continue;
                }
            }

            switch (remainingMoves.get(0)) {
                case LEFT:
                    orientation = orientation.left();
                    remainingMoves.remove(0);
                    hasJustArrived = false;
                    prompt("Turned left.");

                    break;
                case RIGHT:
                    orientation = orientation.right();
                    prompt("Turned right.");

                    remainingMoves.remove(0).toString();

                    hasJustArrived = false;
                    break;
                case FORWARD:
                    Coordinates destination = forwardSquareCoordinates();

                    prompt("Set to go to " + destination + ".");

                    if (!game.hasSquareAt(destination) || game.hasMountainAt(destination)) {
                        prompt("Cannot go to " + destination + ", off board or mountain.");

                        do
                            remainingMoves.remove(0);
                        while (!remainingMoves.isEmpty() && remainingMoves.get(0) == Move.FORWARD);

                        hasJustArrived = false;
                        continue;
                    }

                    try {
                        game.pleaseMoveWithCare(this, position, destination);
                    } catch (InterruptedException e) {
                        finishGame();
                        return;
                    }

                    position = destination;

                    remainingMoves.remove(0);

                    hasJustArrived = true;
                    break;
                default:
                    throw new RuntimeException("This should never happen: " + adventurer.getName() +
                            " attempted an unknown move.");
            }

            prompt("-------------------------------");
            try {
                sleep(1000);
            } catch (InterruptedException e) {
                finishGame();
                return;
            }
        }

        prompt("No moves left.");

        if (hasJustArrived) {
            prompt("Checking for treasure in the last square reached.");
            List<Treasure> currentSquareTreasures = game.getTreasuresAt(position);
            if (!currentSquareTreasures.isEmpty()) {
                prompt("Found treasure!");
                treasuresFoundInThisGame.addAll(currentSquareTreasures);
                game.clearTreasuresAt(position);
            }
        }

        // Tell the game that this player has no more moves.
        game.playerDone(this);
        prompt("Done!");

        // Stay put until the other players finish.
        while (true) {
            prompt("Waiting for other players to finish.");
            try {
                sleep(1000);
            } catch (InterruptedException e) {
                finishGame();
                return;
            }
        }
    }

    private Coordinates forwardSquareCoordinates() {
        switch (orientation) {
            case NORTH:
                return new Coordinates(position.getX(), position.getY() - 1);
            case SOUTH:
                return new Coordinates(position.getX(), position.getY() + 1);
            case EAST:
                return new Coordinates(position.getX() + 1, position.getY());
            case WEST:
                return new Coordinates(position.getX() - 1, position.getY());
            default:
                throw new RuntimeException("This should not happen!");
        }
    }

    public void finishGame() {
        prompt("Finished at " + position + ".");
        prompt("Found " + treasuresFoundInThisGame.size() + " treasures.");
        game.report(this);
    }

    public void prompt(String message) {
        System.out.println(adventurer.getName().toUpperCase() + ": " + message);
//        System.out.println(adventurer.getName().toUpperCase() + ": " + remainingMoves.size() + " moves left.");
//        System.out.println(adventurer.getName().toUpperCase() + ": " + treasuresFoundInThisGame.size() + " treasures found.");
    }

    public String getName() {
        return adventurer.getName();
    }
}
