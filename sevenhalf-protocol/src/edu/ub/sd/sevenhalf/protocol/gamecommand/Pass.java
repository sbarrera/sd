package edu.ub.sd.sevenhalf.protocol.gamecommand;

import edu.ub.sd.sevenhalf.protocol.GameCommand;
import edu.ub.sd.sevenhalf.utils.ComUtils;

public class Pass extends GameCommand {

    public static final String SemanticName = "Pass";
    public static final String SyntacticName = "PASS";

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
