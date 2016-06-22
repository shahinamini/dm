package services.impl;

import domain.*;
import services.Game;
import services.exceptions.GameException;

import java.util.ArrayList;
import java.util.List;

import static java.lang.Thread.sleep;

/**
 * Created by shn on 20/06/2016.
 */
public class PlayerImpl implements services.Player {
    private Adventurer adventurer;
    private Coordinates position;
    private Orientation orientation;
    private List<Treasure> treasuresFoundInThisGame;
    private List<Move> remainingMoves;
    private Game game;

    PlayerImpl(Adventurer adventurer, Game game) {
        this.adventurer = adventurer;
        this.game = game;

        this.position = adventurer.getInitialPosition();
        this.orientation = adventurer.getInitialOrientation();
        this.remainingMoves = new ArrayList<>(adventurer.getMoveQueue());
        this.treasuresFoundInThisGame = new ArrayList<>();

        prompt("Set at " + position);
    }

    public Adventurer getAdventurer() {
        return adventurer;
    }

    public void run() {
        prompt("Starts at " + position + ".");

        try {
            while (!remainingMoves.isEmpty()) {
                if (!game.getTreasuresAt(position).isEmpty()) {
                    prompt("Found treasure at " + position + ".");

                    treasuresFoundInThisGame.addAll(game.getTreasuresAt(position));
                    game.clearTreasuresAt(position);
                } else {
                    switch (remainingMoves.get(0)) {
                        case LEFT:
                            orientation = orientation.left();
                            prompt("Turned left.");

                            remainingMoves.remove(0);
                            break;
                        case RIGHT:
                            orientation = orientation.right();
                            prompt("Turned right.");

                            remainingMoves.remove(0);
                            break;
                        case FORWARD:
                            Coordinates destination = forwardSquareCoordinates();

                            prompt("Set to go to " + destination + ".");

                            if (game.isCrossableAt(destination)) {
                                game.pleaseMoveWithCare(this, position, destination);
                            } else {
                                prompt("Cannot go to " + destination + ", off board or mountain.");

                                do {
                                    remainingMoves.remove(0);
                                } while (!remainingMoves.isEmpty() && remainingMoves.get(0) == Move.FORWARD);
                                continue;
                            }

                            position = destination;

                            remainingMoves.remove(0);
                            break;
                        default:
                            throw new RuntimeException("Player " + adventurer.getName() +
                                    " attempted an unknown move.");
                    }
                }
                prompt("------------------------------------------");
                sleep(1000);
            }

            if (!game.getTreasuresAt(position).isEmpty()) {
                prompt("Found treasure!");

                treasuresFoundInThisGame.addAll(game.getTreasuresAt(position));
                game.clearTreasuresAt(position);
            }

            prompt("No moves left.");

            game.playerDone(this);
            prompt("Done!");

            while (true) {
                prompt("Waiting for other players to finish...");
                sleep(1000);
            }
        } catch (GameException e) {
            throw new RuntimeException(e);
        } catch (InterruptedException e) {
            finishGame();
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

    private void finishGame() {
        prompt("Finished at " + position + ".");
        prompt("Found " + treasuresFoundInThisGame.size() + " treasures.");
        adventurer.addAllTreasures(treasuresFoundInThisGame);
        game.report(this);
    }

    public void prompt(String message) {
        System.out.println(adventurer.getName().toUpperCase() + ": " + message);
    }

    public Integer howManyTreasuresFound() {
        return treasuresFoundInThisGame.size();
    }

    public String getName() {
        return adventurer.getName();
    }

    public Coordinates getPosition() {
        return position;
    }

    public Orientation getOrientation() {
        return orientation;
    }
}
