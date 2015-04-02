package edu.ub.sd.sevenhalf.protocol;

import edu.ub.sd.sevenhalf.protocol.exception.UnknownGameCommandException;
import edu.ub.sd.sevenhalf.utils.ComUtils;
import edu.ub.sd.sevenhalf.utils.ILoggable;

import java.util.HashMap;
import java.util.Map;

public abstract class GameCommand implements ILoggable {

    public abstract String getSemanticName();
    public abstract String getSyntacticName();
    public abstract byte[] toByteArray();

    private static final byte[] byteSpace = " ".getBytes();

    protected byte[] getByteSpace() {
        return byteSpace;
    }

    protected byte[] concatenate(byte[]... arrays) {
        byte[] in;
        int length = 0;
        int offset = 0;

        if ( arrays.length == 0 )
            return new byte[0];

        for ( byte[] array : arrays ) {
            length += array.length;
        }

        in = new byte[length];

        for ( byte[] array : arrays ) {
            for ( int i = 0  ; i < array.length ; i++  ) {
                in[i + offset] = array[i];
            }
            offset += array.length;
        }

        return in;
    }

    @Override
    public String toString() {
        return String.format("[%s [%s]]",
                getSemanticName(),
                ComUtils.bytesToHex(toByteArray()));
    }

    private static Map<String, IGameCommandParser> parsers = new HashMap<>();

    public static GameCommand parse(String syntacticName, GameSocket com) throws Exception {
        if ( parsers.containsKey(syntacticName) ) {
            //System.out.println(String.format("Found parser for [%s]", syntacticName));
            IGameCommandParser parser = parsers.get(syntacticName);
            GameCommand gameCommand = parser.parse(com);
            com.client(gameCommand);
            return gameCommand;
        }

        throw new UnknownGameCommandException();
    }

    public static void registerGameCommandParser(String syntacticName, IGameCommandParser parser) {
        System.out.println(String.format("Registering parser for [%s]", syntacticName));
        parsers.put(syntacticName, parser);
    }

}
