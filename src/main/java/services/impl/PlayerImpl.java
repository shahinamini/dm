package services.impl;

import domain.*;
import services.Game;
import services.exceptions.GameException;

import java.util.ArrayList;
import java.util.InputMismatchException;
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
        boolean hasJustMoved = true;
//        Square newSquare = game.squareAt(position);
        prompt("Starts at " + position + ".");

        while (!remainingMoves.isEmpty()) {
//            if (newSquare != null) {
            if (hasJustMoved) {
                prompt("Looks for treasure in " + position + ".");
                List<Treasure> currentSquareTreasures = game.getTreasuresAt(position);
                if (!currentSquareTreasures.isEmpty()) {
                    prompt("Found treasure!");
                    treasuresFoundInThisGame.addAll(currentSquareTreasures);
                    game.removeTreasuresAt(position);

                    try {
                        sleep(1000);
                    } catch (InterruptedException e) {
                        finishGame();
                        return;
                    }

                    hasJustMoved = false;
                    continue;
                }
            }

            switch (remainingMoves.get(0)) {
                case LEFT:
                    orientation = orientation.left();
                    prompt(remainingMoves.remove(0).toString());
                    hasJustMoved = false;
                    prompt("Turned left.");

                    try {
                        sleep(1000);
                    } catch (InterruptedException e) {
                        finishGame();
                        return;
                    }

                    continue;
                case RIGHT:
                    orientation = orientation.right();
                    prompt(remainingMoves.remove(0).toString());
                    hasJustMoved = false;
                    prompt("Turned right.");

                    try {
                        sleep(1000);
                    } catch (InterruptedException e) {
                        finishGame();
                        return;
                    }

                    continue;
                case FORWARD:
                    Coordinates destinationCoordinates = forwardSquareCoordinates();

//                    Square destinationSquare = game.squareAt(destinationCoordinates);
                    prompt("Set to go to " + destinationCoordinates + ".");

//                    if (destinationSquare == null || destinationSquare.isMountain()) {
                    if (!game.hasSquareAt(destinationCoordinates) || game.hasMountainAt(destinationCoordinates)) {
                        prompt("Cannot go to " + destinationCoordinates + ", not a nice place.");

                        do
                            prompt(remainingMoves.remove(0).toString());
                        while (!remainingMoves.isEmpty() && remainingMoves.get(0) == Move.FORWARD);

                        hasJustMoved = false;

                        continue;
                    }

                    try {
                        game.pleaseMove(this, position, destinationCoordinates);
                    } catch (Exception e) {
                        if (e instanceof GameException)
                            e.printStackTrace();
                        if (e instanceof InterruptedException) {
                            finishGame();
                            return;
                        }

                    }

/*
                    MovementLock movementLock = MovementLock.getInstance();
                    synchronized (movementLock) {
                        while (destinationSquare.isOccupied()) {
                            prompt("Waiting for " + destinationSquare.getOccupant().getName() + " to free " +
                                    destinationCoordinates + " to go there.");
                            try {
                                game.playerWaiting(this);
                                movementLock.wait();
                                game.playerResumed(this);
                            } catch (InterruptedException e) {
                                finishGame();
                                return;
                            }
                        }

                        destinationSquare.letIn(adventurer);
                        game.squareAt(position).letGo(adventurer);
                        movementLock.notifyAll();
                    }
*/

                    position = destinationCoordinates;
                    prompt(remainingMoves.remove(0).toString());
                    hasJustMoved = true;

                    try {
                        sleep(1000);
                    } catch (InterruptedException e) {
                        finishGame();
                        return;
                    }

                    continue;
                default:
                    throw new RuntimeException("This should never happen: " + adventurer.getName() +
                            " attempted an unknown move.");
            }
        }

        prompt("No moves left.");

        if (hasJustMoved) {
            prompt("Checking for treasure in the last square reached.");
            List<Treasure> currentSquareTreasures = game.getTreasuresAt(position);
            if (!currentSquareTreasures.isEmpty()) {
                prompt("Found treasure!");
                treasuresFoundInThisGame.addAll(currentSquareTreasures);
                game.removeTreasuresAt(position);
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
