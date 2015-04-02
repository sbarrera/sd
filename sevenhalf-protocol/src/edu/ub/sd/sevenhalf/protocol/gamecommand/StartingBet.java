package edu.ub.sd.sevenhalf.protocol.gamecommand;

import edu.ub.sd.sevenhalf.protocol.GameCommand;
import edu.ub.sd.sevenhalf.protocol.IGameCommandParser;
import edu.ub.sd.sevenhalf.utils.ComUtils;

public class StartingBet extends GameCommand {

    public static final String SemanticName = "Starting Bet";
    public static final String SyntacticName = "STBT";

    public static final IGameCommandParser Parser = new IGameCommandParser(){

        @Override
        public GameCommand parse(ComUtils com) throws Exception {
            StartingBet cmd = new StartingBet(-1);
            com.readString(1);
            cmd.setStartingBet(com.readInteger());
            return cmd;
        }

    };

    private int startingBet = 0;

    public StartingBet(int startingBet) {
        this.startingBet = startingBet;
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
        return String.format("%s %s", SyntacticName, startingBet);
    }

    @Override
    public byte[] toByteArray() {
        return concatenate(
                ComUtils.toByteArray(getSyntacticName()),
                getByteSpace(),
                ComUtils.toByteArray(startingBet));
    }

    public int getStartingBet() {
        return startingBet;
    }

    public void setStartingBet(int startingBet) {
        this.startingBet = startingBet;
    }

}
