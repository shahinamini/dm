import domain.Board;
import io.AdventurerReader;
import io.BoardReader;
import io.ResultsWriter;
import io.impl.AdventurerReaderImpl;
import io.impl.BoardReaderImpl;
import io.impl.ResultsWriterImpl;
import services.Game;
import services.Player;
import services.impl.GameImpl;

import java.io.IOException;
import java.util.List;
import java.util.Scanner;

/**
 * Created by shn on 20/06/2016.
 */
public class TestPlay {

    public static void main(String[] args) {
        String pathname = readLine("Entrez le chemin et le nom du fichier décrivant le plateau : ");
//        String pathname = "/Users/shn/test-board.txt";

        BoardReader boardReader = new BoardReaderImpl(pathname);

        try {
            boardReader.read();
        } catch (Exception e) {
            e.printStackTrace();
        }

        Board board = boardReader.getBoard();

        pathname = readLine("Entrez le chemin et le nom du fichier contenant l'inventaire des joueurs : ");
//        pathname = "/Users/shn/test-adventurers.txt";

        AdventurerReader adventurerReader = new AdventurerReaderImpl(pathname);

        try {
            adventurerReader.read();
        } catch (Exception e) {
            e.printStackTrace();
        }

        Game game = new GameImpl(board, adventurerReader.getAdventurers());

        game.play();

        List<Player> results = game.getResults();

        pathname = readLine("Dans quel fichier souhaitez-vous garder les résultats du jeu ? ");
//        pathname = "/Users/shn/test-results.txt";

        try {
            ResultsWriter resultsWriter = new ResultsWriterImpl(pathname);
            for (Player result : results)
                resultsWriter.writeLn(result);
            resultsWriter.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static String readLine(String prompt) {
        System.out.print(prompt);
        return new Scanner(System.in).next();
    }
}
