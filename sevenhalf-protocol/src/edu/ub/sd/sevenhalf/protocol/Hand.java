package edu.ub.sd.sevenhalf.protocol;

import edu.ub.sd.sevenhalf.utils.ComUtils;
import edu.ub.sd.sevenhalf.utils.ILoggable;

import java.text.DecimalFormat;
import java.util.ArrayList;

public class Hand extends ArrayList<GameCard> implements ILoggable {

    public double getTotal() {
        double sum = 0.0;

        for ( GameCard gameCard : this ) {
            sum += gameCard.getValue().getNumber();
        }

        return sum;
    }

    @Override
    public String toString() {
        String out = String.format("[Hand total [%s] [ ", getTotal());
        for ( GameCard gameCard : this ) {
            out += gameCard + " ";
        }
        out += "]";
        return out;
    }

    public byte[] toByteArray() {
        byte[] data = new byte[size() * 2];
        int i = 0;

        for ( GameCard gameCard : this ) {
            data[i] = ComUtils.toByteArray(gameCard.getValue().getRepresentation())[0];
            data[i + 1] = ComUtils.toByteArray(gameCard.getSuit().getRepresentation())[0];
            i += 2;
        }

        return data;
    }

    public String getLogMessage() {
        String cards = "";
        for ( GameCard gameCard : this ) {
            cards += gameCard.getLogMessage();
        }
        String score = new DecimalFormat("00.0").format(getTotal());
        score = score.replace(",", ".");
        return String.format("%s %s", cards, score);
    }

}
