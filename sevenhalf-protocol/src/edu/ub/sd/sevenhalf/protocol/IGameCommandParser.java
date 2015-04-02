package edu.ub.sd.sevenhalf.protocol;

import edu.ub.sd.sevenhalf.utils.ComUtils;

public interface IGameCommandParser {

    // TODO If you're using Java 8, please change this to a default method. Could be fun :3
    public GameCommand parse(ComUtils com) throws Exception;

}
