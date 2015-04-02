package edu.ub.sd.sevenhalf.runtime;

import java.util.List;

public abstract class GameExecutable implements Runnable {

    protected GameParameters parameters;

    public GameExecutable(String[] args) throws Exception {
        this.parameters = GameParameters.parse(args, getRequiredArguments(), getOptionalArguments());
    }

    public abstract void showHelp();
    public abstract List<String> getRequiredArguments();
    public abstract List<String> getOptionalArguments();

}
