package edu.ub.sd.sevenhalf.bank.multithreaded;

import edu.ub.sd.sevenhalf.runtime.GameExecutable;

import java.io.File;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Arrays;
import java.util.List;

public class GameListener extends GameExecutable {

    private ServerSocket serverSocket;

    public GameListener(String[] args) throws Exception {
        super(args);

        if ( parameters.doShowHelp() ) {
            showHelp();
        } else {
            int port = parameters.getPort();
            serverSocket = new ServerSocket(port, 1000, InetAddress.getByName("0.0.0.0"));

            System.out.println(String.format("Application Path [%s]", new File(".").getAbsolutePath()));
            System.out.println(String.format("Listening at port [%s]", port));
        }
    }

    @Override
    public void run() {
        System.out.println("Waiting for client connections...");
        while ( true ) {
            try {
                Socket socket = serverSocket.accept();
                Thread thread = new Thread(new GameServer(socket, parameters));
                thread.start();
            } catch ( Exception ex ) {
                ex.printStackTrace();
            }
        }
    }

    public static void main(String args[]) {
        try {
            GameListener listener = new GameListener(args);
            if ( !listener.parameters.doShowHelp() ) {
                listener.run();
            }
        } catch ( Exception ex ) {
            ex.printStackTrace();
        }
    }

    @Override
    public void showHelp() {
        System.out.println("java Server -p <port> -b <starting_bet> -f <deckfile>");
    }

    @Override
    public List<String> getRequiredArguments() {
        return Arrays.asList(new String[]{ "-p", "-b", "-f" });
    }

    @Override
    public List<String> getOptionalArguments() {
        return Arrays.asList(new String[]{});
    }
}
