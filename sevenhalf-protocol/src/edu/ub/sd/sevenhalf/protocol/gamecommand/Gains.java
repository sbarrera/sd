package edu.ub.sd.sevenhalf.protocol.gamecommand;

import edu.ub.sd.sevenhalf.protocol.GameCommand;
import edu.ub.sd.sevenhalf.protocol.IGameCommandParser;
import edu.ub.sd.sevenhalf.utils.ComUtils;

public class Gains extends GameCommand {

    public static final String SemanticName = "Gains";
    public static final String SyntacticName = "GAIN";

    public static final IGameCommandParser Parser = new IGameCommandParser(){

        @Override
        public GameCommand parse(ComUtils com) throws Exception {
            Gains cmd = new Gains(-1);
            com.readString(1);
            cmd.setGains(com.readInteger());
            return cmd;
        }

    };

    private int gains = 0;

    public Gains(int gains) {
        this.gains = gains;
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
        return String.format("%s %s", SyntacticName, gains);
    }

    @Override
    public byte[] toByteArray() {
        return concatenate(
                ComUtils.toByteArray(getSyntacticName()),
                getByteSpace(),
                ComUtils.toByteArray(gains));
    }

    public int getGains() {
        return gains;
    }

    public void setGains(int gains) {
        this.gains = gains;
    }

}
