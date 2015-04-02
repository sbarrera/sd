package edu.ub.sd.sevenhalf.protocol;

import edu.ub.sd.sevenhalf.protocol.exception.MalformedGameCommandException;
import edu.ub.sd.sevenhalf.utils.ILoggable;

import java.util.HashMap;
import java.util.Map;

public class GameCard implements ILoggable {

    public static enum Suits {

        Oros("o"), Bastos("b"), Copas("c"), Espadas("e");

        String repr;

        Suits(String repr) {
            this.repr = repr;
        }

        public String getRepresentation() {
            return repr;
        }
    }

    public static enum Values {
        One("1", 1.0), Two("2", 2.0), Three("3", 3.0), Four("4", 4.0),
        Five("5", 5.0), Six("6", 6.0), Seven("7", 7.0),
        Sota("s", 0.5), Caballo("c", 0.5), Rey("r", 0.5);

        String repr;
        double number;

        Values(String repr, double number) {
            this.repr = repr;
            this.number = number;
        }

        public String getRepresentation() {
            return repr;
        }

        public double getNumber() {
            return number;
        }
    }

    public static final Map<String, Suits> SuitsMap = new HashMap<>();
    public static final Map<String, Values> ValuesMap = new HashMap<>();

    static {
        for ( Suits suit : Suits.values() ) {
            SuitsMap.put(suit.repr, suit);
        }

        for ( Values value : Values.values() ) {
            ValuesMap.put(value.repr, value);
        }
    }

    private Suits suit;
    private Values value;

    public GameCard(String suit, String value) throws MalformedGameCommandException {
        this.suit = SuitsMap.get(suit.toLowerCase());
        this.value = ValuesMap.get(value.toLowerCase());

        if ( this.suit == null || this.value == null ) {
            throw new MalformedGameCommandException(String.format("UNKNOWN GAMECARD. SUIT [%s] VALUE [%s]", suit, value));
        }
    }

    public Values getValue() {
        return value;
    }

    public Suits getSuit() {
        return suit;
    }

    @Override
    public boolean equals(Object o) {
        GameCard gameCard = (GameCard) o;
        return gameCard.getSuit().equals(this.suit) && gameCard.getValue().equals(this.value);
    }

    @Override
    public String toString() {
        return String.format("[%s:%s [%s%s]]", value, suit, value.repr, suit.repr);
    }

    public String getLogMessage() {
        return String.format("%s%s", value.repr, suit.repr);
    }

}
