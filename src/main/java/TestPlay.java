import domain.Board;
import io.AdventurerReader;
import io.BoardReader;
import io.impl.AdventurerReaderImpl;
import io.impl.BoardReaderImpl;
import services.Game;
import services.impl.GameImpl;

import java.util.Scanner;

/**
 * Created by shn on 20/06/2016.
 */
public class TestPlay {

    public static void main(String[] args) {
//        String fileName = readLine("Entrez le chemin et le nom du fichier décrivant le plateau : ");
        String fileName = "/Users/shn/test-board.txt";
        BoardReader boardReader = new BoardReaderImpl(fileName);
        try {
            boardReader.read();
        } catch (Exception e) {
            e.printStackTrace();
        }

        Board board = new Board(boardReader.getBoardSize(), boardReader.getMountains(), boardReader.getTreasures());

//        fileName = readLine("Entrez le chemin et le nom du fichier contenant l'inventaire des joueurs : ");
        fileName = "/Users/shn/test-adventurers.txt";
        AdventurerReader adventurerReader = new AdventurerReaderImpl(fileName);
        try {
            adventurerReader.read();
        } catch (Exception e) {
            e.printStackTrace();
        }

        Game game = new GameImpl(board, adventurerReader.getAdventurers());

        game.play();
    }

    public static String readLine(String prompt) {
        System.out.print(prompt);
        return new Scanner(System.in).next();
    }
}
