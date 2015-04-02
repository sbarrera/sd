package edu.ub.sd.sevenhalf.protocol.gamecommand;

import edu.ub.sd.sevenhalf.protocol.GameCommand;
import edu.ub.sd.sevenhalf.protocol.IGameCommandParser;
import edu.ub.sd.sevenhalf.protocol.exception.MalformedGameCommandException;
import edu.ub.sd.sevenhalf.utils.ComUtils;

public class Error extends GameCommand {

    public static final String SemanticName = "Error";
    public static final String SyntacticName = "ERRO";

    public static final IGameCommandParser Parser = new IGameCommandParser(){

        @Override
        public GameCommand parse(ComUtils com) throws Exception {
            com.readString(1);
            int length = com.readLengthInteger();

            if ( length < 0 || length > 99 ) {
                throw new MalformedGameCommandException(String.format("Error message length not valid. Given value [%s]", length));
            }

            String message = com.readString(length);
            return new Error(message);
        }

    };

    private String message;

    public Error(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
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
        return String.format("%s %s", SyntacticName, message);
    }

    @Override
    public String toString() {
        return String.format("[%s length [%s] message [%s] bytes [%s]]",
                getSemanticName(),
                message.length(),
                message,
                ComUtils.bytesToHex(toByteArray()));
    }

    @Override
    public byte[] toByteArray() {
        int length = message.length();
        length = length >= 100 ? 99 : length;
        return concatenate(
                ComUtils.toByteArray(getSyntacticName()),
                getByteSpace(),
                ComUtils.toByteArray(Integer.toString(length)),
                ComUtils.toByteArray(message.substring(0, length)));
    }

}
