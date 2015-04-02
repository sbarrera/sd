package edu.ub.sd.sevenhalf.bank.multithreaded;

import edu.ub.sd.sevenhalf.bank.DeckParser;
import edu.ub.sd.sevenhalf.protocol.GameCard;
import edu.ub.sd.sevenhalf.protocol.GameSocket;
import edu.ub.sd.sevenhalf.protocol.Hand;
import edu.ub.sd.sevenhalf.protocol.exception.GameException;
import edu.ub.sd.sevenhalf.protocol.exception.MalformedGameCommandException;
import edu.ub.sd.sevenhalf.protocol.exception.UnexpectedGameCommandException;
import edu.ub.sd.sevenhalf.protocol.gamecommand.*;
import edu.ub.sd.sevenhalf.protocol.gamecommand.Error;
import edu.ub.sd.sevenhalf.runtime.GameParameters;

import java.net.Socket;
import java.nio.file.Paths;
import java.util.Queue;

public class GameServer extends GameSocket {

    public static enum GameState {
        Initialized,
        WaitingDraw,
        WaitingPlayerInput,
        PlayBankTurn,
        NotifyScore,
        End
    }

    protected GameState state;
    protected int playerBet = 12;
    protected Queue<GameCard> deck;
    protected Hand playerHand;
    protected Hand bankHand;

    protected String connectionName;

    public GameServer(GameParameters parameters) throws Exception {
        initialize(parameters);
    }

    public GameServer(Socket socket, GameParameters parameters) throws Exception {
        super(socket);
        initialize(parameters);
    }

    protected void initialize(GameParameters parameters) throws Exception {
        deck = DeckParser.parse(Paths.get(parameters.getDeckFilePath()).toAbsolutePath().toString());
        playerBet = parameters.getStartingBet();
        playerHand = new Hand();
        bankHand = new Hand();
        announce();

        state = GameState.Initialized;
    }

    protected void announce() {
        connectionName = String.format("%s:%s", socket.getInetAddress().getHostAddress(), socket.getLocalPort());
        System.out.println(String.format("New player [%s]", connectionName));
    }

    @Override
    public void run() {
        try {
            try {
                while ( true ) {
                    if ( state == GameState.Initialized ) {
                        onInitialize();
                    } else if ( state == GameState.WaitingDraw) {
                        onWaitingDraw();
                    } else if ( state == GameState.WaitingPlayerInput ) {
                        onWaitingPlayerInput();
                    } else if ( state == GameState.PlayBankTurn) {
                        onPlayBankTurn();
                    } else if ( state == GameState.NotifyScore ) {
                        onNotifyScore();
                    } else if ( state == GameState.End ) {
                        System.out.println("Game has ended");
                        break;
                    }
                }
            } catch ( UnexpectedGameCommandException ugce ) {
                if ( Error.SyntacticName.equals(ugce.getCommandHeader()) ) {
                    Error error = (Error) parseCommand(Error.SyntacticName);
                    System.out.println(String.format("CLIENT REPORTED ERROR [%s]", error.getMessage()));
                } else {
                    sendCommand(new Error("Invalid command"));
                }
            }
        } catch ( MalformedGameCommandException mgce ) {
            System.out.println(String.format("MALFORMED COMMAND EXCEPTION [%s]", mgce.getMessage()));
        } catch ( Exception ex ) {
            ex.printStackTrace();
        }

        System.out.println(String.format("Client connection terminated [%s]", connectionName));
    }

    protected void onInitialize() throws Exception {
        expectCommandHeader(Start.SyntacticName);
        sendCommand(new StartingBet(playerBet));
        state = GameState.WaitingDraw;
        lastCommandHeader = null;
    }

    protected void onWaitingDraw() throws Exception {
        expectCommandHeader(Draw.SyntacticName);
        sendCard();
        lastCommandHeader = null;
    }

    protected void onWaitingPlayerInput() throws Exception {
        String cmdHeader = expectCommandHeaders(Draw.SyntacticName, Ante.SyntacticName, Pass.SyntacticName);

        if ( cmdHeader.equals(Draw.SyntacticName) ) {
            sendCard();
            if ( playerHand.getTotal() > 7.5 ) {
                sendCommand(new Busting());
                state = GameState.PlayBankTurn;
            }
        } else if ( cmdHeader.equals(Pass.SyntacticName) ) {
            state = GameState.PlayBankTurn;
        } else if ( cmdHeader.equals(Ante.SyntacticName) ) {
            Ante cmd = (Ante) parseCommand(cmdHeader);

            if ( cmd.getUprising() <= 0 ) {
                //throw new GameException("ANTE value has to be positive");
                sendCommand(new Error("ANTE value has to be positive"));
            }

            playerBet += cmd.getUprising();
            System.out.println(String.format("player bet [%s] uprising [%s]", playerBet, cmd.getUprising()));
            state = GameState.WaitingDraw;
        }
        lastCommandHeader = null;
    }

    protected void onPlayBankTurn() throws Exception {
        if ( playerHand.getTotal() > 7.5 ) {
            bankHand.add(deck.poll());
        } else {
            while ( bankHand.getTotal() < 7.5 ) {
                bankHand.add(deck.poll());

                if ( bankHand.getTotal() > playerHand.getTotal() /* || bankHand.getTotal() == 7.0 && Math.random() > 5.0 */) {
                    break;
                }
            }
        }
        System.out.println(String.format("Bank deck [%s]", bankHand));
        state = GameState.NotifyScore;
        lastCommandHeader = null;
    }

    protected void onNotifyScore() throws Exception {
        int gains;

        boolean hasPlayerBusted = playerHand.getTotal() > 7.5;
        boolean doBothPlayerBankHaveSameScore = playerHand.getTotal() == bankHand.getTotal();
        boolean hasBankScoredMoreThanPlayer = bankHand.getTotal() > playerHand.getTotal();
        boolean hasPlayerWon = playerHand.getTotal() == 7.5;

        if ( doBothPlayerBankHaveSameScore ) {
            gains = 0;
        } else if ( hasPlayerWon ) {
            gains = 2 * playerBet;
        } else if ( hasPlayerBusted || hasBankScoredMoreThanPlayer ) {
            gains = -playerBet;
        } else {
            gains = playerBet;
        }

        sendCommand(new BankScore(bankHand));
        sendCommand(new Gains(gains));

        state = GameState.End;
        lastCommandHeader = null;
    }

    protected void sendCard() throws Exception {
        GameCard gameCard = deck.peek();
        sendCommand(new Card(gameCard));
        playerHand.add(deck.poll());
        state = GameState.WaitingPlayerInput;
    }

}
