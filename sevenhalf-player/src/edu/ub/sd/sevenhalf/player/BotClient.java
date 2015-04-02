package edu.ub.sd.sevenhalf.player;

import edu.ub.sd.sevenhalf.runtime.GameParameters;

public class BotClient implements Runnable {

    private GameParameters parameters;
    private GameClient client;
    private boolean endGame;

    private double topScore = 0.0;

    public BotClient(GameParameters gameParameters) {
        parameters = gameParameters;
        topScore = gameParameters.getTopCard();
    }

    @Override
    public void run() {
        try {
            client = new GameClient(parameters);

            while ( !hasGameEnded() ) {
                client.run();

                if ( client.isWaitingForPlayerInput() ) {
                    playTurn();
                }

                if ( client.hasErrorRisen() || client.hasGameEnded() ) {
                    endGame();
                }
            }
        } catch ( Exception ex ) {
            endGame();
        } finally {
            if ( client != null ) {
                System.out.println(String.format("  Drawn Cards: [%s]", client.getHand()));
                System.out.println(String.format("  Bank Drawn Cards: [%s]", client.getBankHand()));
                System.out.println(String.format("  Gains: [%s]", client.getGains()));
                client.shutdown();
            }
        }
    }

    public void playTurn() {
        if ( client.getCurrentScore() < topScore ) {
            client.drawCard();
        } else {
            client.pass();
        }
    }

    public void endGame() {
        endGame = true;
    }

    public boolean hasGameEnded() {
        return endGame;
    }

}
