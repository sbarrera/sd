package edu.ub.sd.sevenhalf.runtime;

import edu.ub.sd.sevenhalf.protocol.Connection;

import java.util.HashMap;
import java.util.List;

public class GameParameters {

    private boolean showHelp = false;

    private HashMap<String, String> mapArgs = new HashMap<>();

    public static GameParameters parse(String[] args, List<String> requiredArguments, List<String> optionalArguments) {
        GameParameters parameters = new GameParameters();
        int verifiedArguments = 0;

        if ( args.length == 0 || ( args.length == 1 && args[0].equals("-h") ) ) {
            parameters.showHelp = true;
        } else if ( args.length % 2 == 0 ) {
            for ( int i = 0 ; i < args.length ; i += 2 ) {
                boolean isRequired = requiredArguments.contains(args[i]);
                boolean isOptional = optionalArguments.contains(args[i]);
                if ( isRequired || isOptional ) {
                    parameters.mapArgs.put(args[i], args[i + 1]);
                    if ( isRequired ) verifiedArguments++;
                } else {
                    break;
                }
            }
        }

        if ( verifiedArguments != requiredArguments.size() ) {
            parameters.showHelp = true;
        }

        return parameters;
    }

    public String getDeckFilePath() {
        return mapArgs.get("-f");
    }

    public String getIp() {
        return mapArgs.get("-s");
    }

    public int getPort() {
        String port = mapArgs.get("-p");
        port = port != null ? port : "" + Connection.DEFAULT_PORT;
        return Integer.parseInt(port);
    }

    public int getStartingBet() {
        String startingBet = mapArgs.get("-b");
        startingBet = startingBet != null ? startingBet : "100";
        return Integer.parseInt(startingBet);
    }

    public double getTopCard() {
        String topCard = mapArgs.get("-a");
        topCard = topCard != null ? topCard : "0.0";
        return Double.parseDouble(topCard);
    }

    public boolean doShowHelp() {
        return showHelp;
    }
}
