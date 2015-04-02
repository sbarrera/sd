package edu.ub.sd.sevenhalf.protocol.gamecommand;

import edu.ub.sd.sevenhalf.protocol.GameCard;
import edu.ub.sd.sevenhalf.protocol.GameCommand;
import edu.ub.sd.sevenhalf.protocol.Hand;
import edu.ub.sd.sevenhalf.protocol.IGameCommandParser;
import edu.ub.sd.sevenhalf.protocol.exception.MalformedGameCommandException;
import edu.ub.sd.sevenhalf.utils.ComUtils;

import java.text.DecimalFormat;

public class BankScore extends GameCommand {

    public static final String SemanticName = "Bank Score";
    public static final String SyntacticName = "BKSC";

    public static final IGameCommandParser Parser = new IGameCommandParser(){

        @Override
        public GameCommand parse(ComUtils com) throws Exception {
            Hand hand = new Hand();

            // Get bank cards from the stream
            com.readString(1);
            int cardsLength = com.readInteger();
            for ( int i = 0 ; i < cardsLength ; i++ ) {
                String value = com.readString(1);
                String suit = com.readString(1);
                hand.add(new GameCard(suit, value));
            }

            // Get the bank score from the stream
            com.readString(1);
            double score = com.readDouble();
            if ( score != hand.getTotal() ) {
                throw new MalformedGameCommandException(String.format("Score value not valid. Expected Score [%s] Given Score [%s]", hand.getTotal(), score));
            }

            return new BankScore(hand);
        }

    };

    private Hand hand;

    public BankScore(Hand hand) {
        this.hand = hand;
    }

    public Hand getBankHand() {
        return hand;
    }

    @Override
    public String getSemanticName() {
        return SemanticName;
    }

    @Override
    public String getSyntacticName() {
        return SyntacticName;
    }

    public String getLogMessage() {
        return String.format("%s %s", SyntacticName, hand.getLogMessage());
    }

    @Override
    public byte[] toByteArray() {
        return concatenate(
                ComUtils.toByteArray(getSyntacticName()),
                getByteSpace(),
                ComUtils.toByteArray(hand.size()),
                hand.toByteArray(),
                getByteSpace(),
                ComUtils.toByteArray(hand.getTotal()));
    }

}
