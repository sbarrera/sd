package edu.ub.sd.sevenhalf.protocol.gamecommand;

import edu.ub.sd.sevenhalf.protocol.GameCommand;
import edu.ub.sd.sevenhalf.utils.ComUtils;

public class Start extends GameCommand {

    public static final String SemanticName = "Start";
    public static final String SyntacticName = "STRT";

    @Override
    public String getSemanticName() {
        return SemanticName;
    }

    @Override
    public String getSyntacticName() {
        return SyntacticName;
    }

    public String getLogMessage() {
        return SyntacticName;
    }

    @Override
    public byte[] toByteArray() {
        return ComUtils.toByteArray(getSyntacticName());
    }

}
