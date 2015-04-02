package edu.ub.sd.sevenhalf.player;

import edu.ub.sd.sevenhalf.protocol.*;
import edu.ub.sd.sevenhalf.protocol.exception.GameException;
import edu.ub.sd.sevenhalf.protocol.exception.UnexpectedGameCommandException;
import edu.ub.sd.sevenhalf.protocol.gamecommand.*;
import edu.ub.sd.sevenhalf.protocol.gamecommand.Error;
import edu.ub.sd.sevenhalf.runtime.GameParameters;

import java.net.Socket;

public class GameClient extends GameSocket {

    public static enum GameState {
        BeginGame,
        WaitingPlayerInput,
        DrawCard, AnteUp, Pass,
        WaitingBankInput,
        Busted,
        Error,
        End
    }

    private GameState state;

    private int bet;
    private int additionalBet;
    private boolean errorRisen = false;
    private int gains;

    private Hand hand;
    private Hand bankHand;

    public GameClient(GameParameters parameters) throws Exception {
        super();

        try {
            disableLogging();
            System.out.println("Connecting to game server...");

            Socket socket = new Socket(parameters.getIp(), parameters.getPort());
            setSocket(socket);

            hand = new Hand();

            System.out.println("Connection established");
        } catch ( Exception ex ) {
            System.out.println("Failed to establish connection to the game server");
            throw ex;
        }

        state = GameState.BeginGame;
    }

    public int getCurrentBet() {
        return bet;
    }

    @Override
    public void run() {
        try {
            try {
                while ( true ) {
                    System.out.println(String.format("state [%s]", state));
                    if ( state == GameState.BeginGame ) {
                        onBeginGame();
                    } else if ( state == GameState.DrawCard ) {
                        onDrawCard();
                    } else if ( state == GameState.AnteUp ) {
                        onAnteUp();
                    } else if ( state == GameState.Busted ) {
                        onBusted();
                    } else if ( state == GameState.Pass ) {
                        onPass();
                    } else if ( state == GameState.WaitingBankInput ) {
                        onWaitingBankInput();
                    } else if ( state == GameState.WaitingPlayerInput || state == GameState.Error || state == GameState.End ) {
                        break;
                    }
                }
            } catch ( UnexpectedGameCommandException ugce ) {
                if ( Error.SyntacticName.equals(ugce.getCommandHeader()) ) {
                    Error error = (Error) parseCommand(Error.SyntacticName);
                    System.out.println(String.format("SERVER REPORTED ERROR [%s]", error.getMessage()));
                    setError();
                } else {
                    sendCommand(new Error("Invalid command"));
                    setError();
                }
            }
        } catch ( Exception ex ) {
            ex.printStackTrace();
            setError();
        }
    }

    public Hand getHand() {
        return hand;
    }

    public Hand getBankHand() {
        return bankHand;
    }

    public int getGains() {
        return gains;
    }

    public boolean isWaitingForPlayerInput() {
        return state.equals(GameState.WaitingPlayerInput);
    }

    public boolean hasGameBusted() {
        return state.equals(GameState.Busted);
    }

    public boolean hasGameEnded() {
        return state.equals(GameState.End);
    }

    public boolean hasErrorRisen() {
        return errorRisen;
    }

    public void shutdown() {
        try {
            socket.close();
        } catch ( Exception ex ) {
            ex.printStackTrace();
        }
    }

    public void drawCard() {
        System.out.println("Drawing card...");
        state = GameState.DrawCard;
    }

    public double getCurrentScore() {
        return hand.getTotal();
    }

    public void anteUp(int additionalBet) {
        state = GameState.AnteUp;
        this.additionalBet = additionalBet;
    }

    public void pass() {
        state = GameState.Pass;
    }

    private void onBeginGame() throws Exception {
        sendCommand(new Start());
        StartingBet cmd = (StartingBet) expectCommand(StartingBet.SyntacticName);
        bet = cmd.getStartingBet();
        state = GameState.WaitingPlayerInput;
    }

    private void onAnteUp() throws Exception {
        sendCommand(new Ante(additionalBet));
        onDrawCard();
        bet += additionalBet;
        additionalBet = 0;
    }

    private void onDrawCard() throws Exception {
        sendCommand(new Draw());
        Card cmd = (Card) expectCommand(Card.SyntacticName);

        GameCard card = cmd.getGameCard();

        if ( hand.contains(card) ) {
            sendCommand(new Error("Repeated card"));
            throw new GameException("Repeated card");
        } else {
            hand.add(card);
            if ( hand.getTotal() > 7.5 ) {
                state = GameState.Busted;
            } else if ( hand.getTotal() == 7.5 ) {
                state = GameState.Pass;
            } else {
                state = GameState.WaitingPlayerInput;
            }
        }
    }

    private void onBusted() throws Exception {
        expectCommandHeader(Busting.SyntacticName);
        state = GameState.WaitingBankInput;
    }

    private void onPass() throws Exception {
        sendCommand(new Pass());
        state = GameState.WaitingBankInput;
    }

    private void onWaitingBankInput() throws Exception {
        BankScore bankScore = (BankScore) expectCommand(BankScore.SyntacticName);
        Gains gains = (Gains) expectCommand(Gains.SyntacticName);

        bankHand = bankScore.getBankHand();
        this.gains = gains.getGains();

        state = GameState.End;
    }

    private void setError() {
        state = GameState.Error;
        errorRisen = true;
    }

}
