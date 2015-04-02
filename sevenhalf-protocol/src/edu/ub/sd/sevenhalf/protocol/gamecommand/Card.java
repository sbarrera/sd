package edu.ub.sd.sevenhalf.protocol.gamecommand;

import edu.ub.sd.sevenhalf.protocol.GameCard;
import edu.ub.sd.sevenhalf.protocol.GameCommand;
import edu.ub.sd.sevenhalf.protocol.IGameCommandParser;
import edu.ub.sd.sevenhalf.utils.ComUtils;

public class Card extends GameCommand {

    public static final String SemanticName = "Card";
    public static final String SyntacticName = "CARD";

    public static final IGameCommandParser Parser = new IGameCommandParser(){

        @Override
        public GameCommand parse(ComUtils com) throws Exception {
            com.readString(1);
            String value = com.readString(1);
            String suit = com.readString(1);
            return new Card(new GameCard(suit, value));
        }

    };

    private GameCard gameCard;

    public Card(GameCard gameCard) {
        this.gameCard = gameCard;
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
        return String.format("%s %s", SyntacticName, gameCard.getLogMessage());
    }

    @Override
    public byte[] toByteArray() {
        return concatenate(
                ComUtils.toByteArray(getSyntacticName()),
                getByteSpace(),
                ComUtils.toByteArray(gameCard.getValue().getRepresentation()),
                ComUtils.toByteArray(gameCard.getSuit().getRepresentation()));
    }

    public GameCard getGameCard() {
        return gameCard;
    }

}
