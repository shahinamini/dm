import domain.Board;
import io.AdventurerParser;
import io.BoardParser;
import io.impl.AdventurerParserImpl;
import io.impl.BoardParserImpl;
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
        BoardParser boardParser = new BoardParserImpl(fileName);
        try {
            boardParser.parse();
        } catch (Exception e) {
            e.printStackTrace();
        }

        Board board = new Board(boardParser.getBoardSize(), boardParser.getMountains(), boardParser.getTreasures());

//        fileName = readLine("Entrez le chemin et le nom du fichier contenant l'inventaire des joueurs : ");
        fileName = "/Users/shn/test-adventurers.txt";
        AdventurerParser adventurerParser = new AdventurerParserImpl(fileName);
        try {
            adventurerParser.parse();
        } catch (Exception e) {
            e.printStackTrace();
        }

        Game game = new GameImpl(board, adventurerParser.getAdventurers());

        game.play();
    }

    public static String readLine(String prompt) {
        System.out.print(prompt);
        return new Scanner(System.in).next();
    }
}
