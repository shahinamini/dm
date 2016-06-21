package services.impl;

import domain.*;
import domain.Board;
import domain.Square;
import services.Game;
import services.Player;
import services.exceptions.GameException;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * Created by shn on 20/06/2016.
 */
public class GameImpl implements Game {
    private final Square[][] squares;
    private final Board board;
    private volatile List<Treasure> treasures;
    private volatile List<Player> playersStillPlaying;
    private volatile List<Player> playersWaiting;
    private volatile List<Player> playersReported;
    private volatile List<Thread> playersThreads;

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

        this.treasures = new CopyOnWriteArrayList<Treasure>();
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
            this.treasures.add(treasure);
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

            square.setAdventurer(adventurer);
            this.playersStillPlaying.add(new PlayerImpl(adventurer, this));
            prompt(adventurer.getName() + " set at " + square.getCoordinates());
        }

        playersWaiting = new CopyOnWriteArrayList<Player>();

        playersReported = new CopyOnWriteArrayList<Player>();

        playersThreads = new CopyOnWriteArrayList<Thread>();
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

    public void playerWaiting(Player player) {
        playersWaiting.add(player);
        prompt(player.getName() + " waiting...");

        if (playersWaiting.containsAll(playersStillPlaying))
            this.ends();
    }

    public void playerResumed(Player player) {
        playersWaiting.remove(player);
        prompt(player.getName() + " not waiting anymore.");
    }

    public void report(Player player) {
        playersReported.add(player);
    }

    public boolean hasSquareAt(Coordinates coordinates) {
        int x = coordinates.getX(), y = coordinates.getY();

        return x > 0 && x <= this.board.getSize().getX() && y > 0 && y <= this.board.getSize().getY();
    }

    private Square squareAt(Coordinates coordinates) {
        int x = coordinates.getX(), y = coordinates.getY();

        if (x > 0 && x <= this.board.getSize().getX() && y > 0 && y <= this.board.getSize().getY())
            return squares[x - 1][y - 1];

        return null;
    }

    public Adventurer getOccupantAt(Coordinates coordinates) {
        return squareAt(coordinates).getOccupant();
    }

    public boolean hasMountainAt(Coordinates coordinates) {
        return squareAt(coordinates).getMountain() != null;
    }

    public boolean isOccupiedAt(Coordinates coordinates) {
        return squareAt(coordinates).getOccupant() != null;
    }

    public List<Treasure> getTreasuresAt(Coordinates coordinates) {
        return squareAt(coordinates).getTreasures();
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
            player.prompt("Waiting for " + destinationSquare.getOccupant().getName() + " to free " +
                    destination + " to go there.");

            playerWaiting(player);

            wait();

            playerResumed(player);
        }

        destinationSquare.setAdventurer(adventurer);
        squareAt(position).letGo(adventurer);

        player.prompt("Went to " + destination + ".");

        notifyAll();
// Critical code ends
    }

    public void letOut(Adventurer adventurer, Coordinates coordinates) throws GameException {
        Square square = squareAt(coordinates);

        if (square.getOccupant() != adventurer)
            throw new GameException("Adventurer attempted to leave a square he is not on.");

        square.clearOccupant();
    }

    public void clearTreasuresAt(Coordinates coordinates) {
        squareAt(coordinates).clearTreasures();
    }

    private void prompt(String message) {
        System.out.println("GAME: " + message);
    }
}
