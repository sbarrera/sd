package edu.ub.sd.sevenhalf.protocol;

import edu.ub.sd.sevenhalf.protocol.exception.NotEnoughBytesException;
import edu.ub.sd.sevenhalf.protocol.exception.UnexpectedGameCommandException;
import edu.ub.sd.sevenhalf.protocol.gamecommand.*;
import edu.ub.sd.sevenhalf.protocol.gamecommand.Error;
import edu.ub.sd.sevenhalf.utils.ComUtils;
import edu.ub.sd.sevenhalf.utils.ILoggable;

import java.net.Socket;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;

public abstract class GameSocket extends ComUtils implements Runnable {

    /**
     * Static initializer to register GameCommand parsers. As this is a point to be used
     * early from any part of both applications, it is a nice place to set it.
     *
     * The first time the GameSocket class is being referenced, this static block will be
     * executed.
     */
    static {
        GameCommand.registerGameCommandParser(Ante.SyntacticName, Ante.Parser);
        GameCommand.registerGameCommandParser(BankScore.SyntacticName, BankScore.Parser);
        GameCommand.registerGameCommandParser(Card.SyntacticName, Card.Parser);
        GameCommand.registerGameCommandParser(Error.SyntacticName, Error.Parser);
        GameCommand.registerGameCommandParser(Gains.SyntacticName, Gains.Parser);
        GameCommand.registerGameCommandParser(StartingBet.SyntacticName, StartingBet.Parser);
    }

    protected String lastCommandHeader = null;
    protected String logFilename = null;
    protected Path logPath = null;
    private boolean enabledLog = true;

    private static String CLIENT = "C";
    private static String SERVER = "S";

    public GameSocket() {
        super();
    }

    public GameSocket(Socket socket) throws Exception {
        super(socket);
    }

    public void disableLogging() {
        enabledLog = false;
    }

    protected void initializeLog() throws Exception {
        logFilename = String.format("Server%s.log", Thread.currentThread().getName());
        logPath = Paths.get("./" + logFilename);
        Files.deleteIfExists(logPath);
    }

    protected void sendCommand(GameCommand cmd) throws Exception {
        System.out.println(String.format("Sending command [%s]", cmd));
        server(cmd);
        send(cmd.toByteArray());
    }

    protected String expectCommandHeaders(String... syntacticNames) throws Exception {
        printSyntacticNames(syntacticNames);
        String cmdHeader = readGameCommandHeaderFromStream();

        for ( String syntacticName : syntacticNames ) {
            if ( syntacticName.equals(cmdHeader) ) {
                lastCommandHeader = syntacticName;
                System.out.println(String.format("Received command [%s]", syntacticName));
                client(cmdHeader);
                return syntacticName;
            }
        }

        System.out.println(String.format("Received unexpected command [%s]", cmdHeader));
        throw new UnexpectedGameCommandException(cmdHeader);
    }

    protected String expectCommandHeader(String syntacticName) throws Exception {
        System.out.println(String.format("Expecting command [%s]", syntacticName));
        String cmdHeader = readGameCommandHeaderFromStream();

        if ( syntacticName.equals(cmdHeader) ) {
            lastCommandHeader = syntacticName;
            System.out.println(String.format("Received command [%s]", syntacticName));
            client(cmdHeader);
            return cmdHeader;
        }

        System.out.println(String.format("Received unexpected command [%s]", cmdHeader));
        throw new UnexpectedGameCommandException(cmdHeader);
    }

    protected String readGameCommandHeaderFromStream() throws Exception {
        try {
            return readString(4).toUpperCase();
        } catch ( NotEnoughBytesException ex ) {
            // TODO Treat fragmented command header transmissions
        }

        return null;
    }

    protected GameCommand expectCommand(String syntacticName) throws Exception {
        String cmdHeader = expectCommandHeader(syntacticName);
        return GameCommand.parse(cmdHeader, this);
    }

    protected GameCommand parseCommand(String syntacticName) throws Exception {
        return GameCommand.parse(syntacticName, this);
    }

    public void client(ILoggable loggable) throws Exception {
        log(CLIENT, loggable.getLogMessage());
    }

    public void server(ILoggable loggable) throws Exception {
        log(SERVER, loggable.getLogMessage());
    }

    public void client(String message) throws Exception {
        log(CLIENT, message);
    }

    public void server(String message) throws Exception {
        log(SERVER, message);
    }

    private void printSyntacticNames(String... syntacticNames) {
        String out = "Expecting command [ ";
        for ( String syntacticName : syntacticNames ) {
            out += syntacticName + " ";
        }
        out += "]";
        System.out.println(out);
    }

    private void log(String k, String message) throws Exception {
        if ( enabledLog ) {
            if ( logPath == null ) {
                initializeLog();
            }

            String line = String.format("%s: %s\n", k, message);
            // System.out.println(String.format(">> LOG [%s]", line));
            Files.write(logPath, line.getBytes(), StandardOpenOption.CREATE, StandardOpenOption.WRITE, StandardOpenOption.APPEND);
        }
    }

}
