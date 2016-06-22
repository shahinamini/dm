package services.impl;

import domain.*;
import io.ResultsWriter;
import services.Game;
import services.Player;
import services.exceptions.GameException;

import java.io.IOException;
import java.util.*;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * Created by shn on 20/06/2016.
 */
public class GameImpl implements Game {
    private final Square[][] squares;
    private final Board board;
    private final List<Player> playersStillPlaying;
    private final List<Player> playersWaiting;
    private final List<Player> playersReported;
    private final List<Thread> playersThreads;

    public GameImpl(Board board, List<Adventurer> players) {
        this.board = board;

        this.squares = new Square[board.getSize().getX()][board.getSize().getY()];

        for (int i = 0; i < board.getSize().getX(); i++)
            for (int j = 0; j < board.getSize().getY(); j++)
                squares[i][j] = new Square(new Coordinates(i + 1, j + 1));


        for (Mountain mountain : board.getMountains()) {
            Square square = squareAt(mountain.getCoordinates());

            if (square == null) {
                System.out.println("Mountain at " + mountain.getCoordinates().toString() + " ignored.");
                System.out.println("Mountain off board.");
                continue;
            }

            square.setMountain(mountain);
            prompt("Mountain at " + square.getCoordinates());
        }

        for (Treasure treasure : board.getTreasures()) {
            Square square = squareAt(treasure.getCoordinates());

            if (square == null) {
                System.out.println("Treasure at " + treasure.getCoordinates().toString() + " ignored.");
                System.out.println("Treasure off board.");
                continue;
            }

            if (square.isMountain()) {
                System.out.println("Treasure at " + treasure.getCoordinates().toString() + " is unreachable.");
                System.out.println("Treasure on a mountain.");
            }

            square.addTreasure(treasure);
            prompt("Treasure at " + square.getCoordinates());
        }

        this.playersStillPlaying = new CopyOnWriteArrayList<Player>();
        for (Adventurer adventurer : players) {
            Square square = squareAt(adventurer.getInitialPosition());

            if (square == null) {
                System.out.println(adventurer.getName() + " ignored.");
                System.out.println("Player off board.");
                continue;
            }

            if (square.isMountain()) {
                System.out.println(adventurer.getName() + " ignored.");
                System.out.println("Player was set to start on a mountain.");
                continue;
            }

            if (square.isOccupied()) {
                System.out.println(adventurer.getName() + " ignored");
                System.out.println("There is already " + square.getOccupant().getName() +
                        " at the same starting point.");
                continue;
            }

            square.setOccupant(adventurer);
            this.playersStillPlaying.add(new PlayerImpl(adventurer, this));
            prompt(adventurer.getName() + " set at " + square.getCoordinates());
        }

        playersWaiting = new CopyOnWriteArrayList<>();

        playersReported = new ArrayList<>();

        playersThreads = new ArrayList<>();
    }

    public void play() {
        for (Player player : playersStillPlaying) {
            Thread playerThread = new Thread(player, player.getName());
            playersThreads.add(playerThread);
            prompt(player.getName() + " has now a thread.");
        }

        for (Thread playerThread : playersThreads) {
            prompt("Starting " + playerThread.getName());
            playerThread.start();
        }

        for (Thread playerThread : playersThreads) {

            try {
                playerThread.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    public List<Player> getResults() {
        Comparator<Player> whoWins =
                (Player p1, Player p2) -> p2.howManyTreasuresFound().compareTo(p1.howManyTreasuresFound()); //   JAVA 8!

        Collections.sort(playersReported, whoWins);

        return playersReported;
    }

    public synchronized void pleaseMoveWithCare(Player player, Coordinates position, Coordinates destination)
            throws GameException, InterruptedException {
        Square positionSquare = squareAt(position);
        Square destinationSquare = squareAt(destination);
        Adventurer adventurer = player.getAdventurer();

        if (destinationSquare == null || positionSquare == null)
            throw new GameException("Adventurer talks of nonexistent squares.");

        if (destinationSquare.getMountain() != null)
            throw new GameException("Adventurer attempted to climb.");

// Critical code starts
        while (destinationSquare.isOccupied()) {
            prompt(player, "Waiting for " + destinationSquare.getOccupant().getName() + " to free " +
                    destination + " to go there.");

            letWait(player);

            wait();

            letResume(player);
        }

        destinationSquare.setOccupant(adventurer);
        positionSquare.setOccupant(null);

        prompt(player, "Went to " + destination + ".");

        notifyAll();
// Critical code ends
    }

    public void playerDone(Player player) {
        playersStillPlaying.remove(player);
        prompt(player.getName() + " is done.");

        if (playersWaiting.containsAll(playersStillPlaying))
            this.ends();
    }

    private void ends() {
        prompt("Game ends.");
        for (Thread playerThread : playersThreads) {
            playerThread.interrupt();
        }
    }

    private void letWait(Player player) {
        playersWaiting.add(player);
        prompt(player.getName() + " waiting...");

        if (playersWaiting.containsAll(playersStillPlaying))
            this.ends();
    }

    private void letResume(Player player) {
        playersWaiting.remove(player);
        prompt(player.getName() + " not waiting anymore.");
    }

    public synchronized void report(Player player) {
        playersReported.add(player);
    }

    private boolean hasSquareAt(Coordinates coordinates) {
        int x = coordinates.getX(), y = coordinates.getY();

        return x > 0 && x <= this.board.getSize().getX() && y > 0 && y <= this.board.getSize().getY();
    }

    private Square squareAt(Coordinates coordinates) {
        int x = coordinates.getX(), y = coordinates.getY();

        if (x > 0 && x <= this.board.getSize().getX() && y > 0 && y <= this.board.getSize().getY())
            return squares[x - 1][y - 1];

        return null;
    }

    public boolean isCrossableAt(Coordinates coordinates) {
        return hasSquareAt(coordinates) && !squareAt(coordinates).isMountain();
    }

    public List<Treasure> getTreasuresAt(Coordinates coordinates) throws GameException {
        if (!hasSquareAt(coordinates))
            throw new GameException("Operation on nonexistent square requested.");
        return squareAt(coordinates).getTreasures();
    }

    public void clearTreasuresAt(Coordinates coordinates) throws GameException {
        if (!hasSquareAt(coordinates))
            throw new GameException("Operation on nonexistent square requested.");
        squareAt(coordinates).clearTreasures();
    }

    private void prompt(String message) {
        System.out.println("GAME: " + message);
    }

    private static void prompt(Player player, String message) {
        System.out.println(player.getName().toUpperCase() + ": " + message);
    }
}
