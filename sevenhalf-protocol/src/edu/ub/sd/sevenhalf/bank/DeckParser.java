package edu.ub.sd.sevenhalf.bank;

import edu.ub.sd.sevenhalf.protocol.GameCard;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.LinkedList;
import java.util.Queue;

public class DeckParser {

    public static Queue<GameCard> parse(String path) throws Exception {
        String line;
        Queue<GameCard> deck = new LinkedList<>();

        FileInputStream fstream = new FileInputStream(path);
        InputStreamReader instream = new InputStreamReader(fstream, "UTF-8");
        BufferedReader d = new BufferedReader(instream);

        line = d.readLine();
        while ( line != null ) {
            if ( line.length() == 2 ) {
                String strValue = line.substring(0, 1);
                String strSuite = line.substring(1, 2);

                deck.add(new GameCard(strSuite, strValue));
            }
            line = d.readLine();
        }

        d.close();
        instream.close();
        fstream.close();

        System.out.println(String.format("Loaded deck size [%s]", deck.size()));

        return deck;
    }

}
