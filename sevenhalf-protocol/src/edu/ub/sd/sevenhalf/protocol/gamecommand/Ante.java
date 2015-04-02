package edu.ub.sd.sevenhalf.protocol.gamecommand;

import edu.ub.sd.sevenhalf.protocol.GameCommand;
import edu.ub.sd.sevenhalf.protocol.IGameCommandParser;
import edu.ub.sd.sevenhalf.utils.ComUtils;

public class Ante extends GameCommand {

    public static final String SemanticName = "Ante";
    public static final String SyntacticName = "ANTE";

    public static final IGameCommandParser Parser = new IGameCommandParser(){

        @Override
        public GameCommand parse(ComUtils com) throws Exception {
            Ante cmd = new Ante(-1);
            com.readString(1);
            cmd.setUprising(com.readInteger());
            return cmd;
        }

    };

    private int uprising = 0;

    public Ante(int uprising) {
        this.uprising = uprising;
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
        return String.format("%s %s", SyntacticName, uprising);
    }

    @Override
    public byte[] toByteArray() {
        return concatenate(
                ComUtils.toByteArray(getSyntacticName()),
                getByteSpace(),
                ComUtils.toByteArray(uprising));
    }

    public int getUprising() {
        return uprising;
    }

    public void setUprising(int uprising) {
        this.uprising = uprising;
    }

}
