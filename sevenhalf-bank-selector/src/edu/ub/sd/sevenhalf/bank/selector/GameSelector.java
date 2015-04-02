package edu.ub.sd.sevenhalf.bank.selector;

import edu.ub.sd.sevenhalf.protocol.Connection;
import edu.ub.sd.sevenhalf.runtime.GameExecutable;

import java.io.File;
import java.net.InetSocketAddress;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

public class GameSelector extends GameExecutable {

    Selector selector;
    ServerSocketChannel server;
    SelectionKey serverKey;
    int gameId = 0;

    public GameSelector(String[] args) throws Exception {
        super(args);

        if ( parameters.doShowHelp() ) {
            showHelp();
        } else {
            selector = Selector.open();
            server = ServerSocketChannel.open();
            server.socket().bind(new InetSocketAddress("0.0.0.0", Connection.DEFAULT_PORT));
            server.configureBlocking(false);
            serverKey = server.register(selector, SelectionKey.OP_ACCEPT);

            System.out.println(String.format("Application Path [%s]", new File(".").getAbsolutePath()));
            System.out.println(String.format("Listening at port [%s]", server.socket().getLocalPort()));
        }
    }

    @Override
    public void run() {
            while (true) {
                try {
                    selector.select();
                    Set keys = selector.selectedKeys();

                    Iterator<SelectionKey> iterator = keys.iterator();
                    while ( iterator.hasNext() ) {
                        SelectionKey key = iterator.next();
                        iterator.remove();

                        if ( key.equals(serverKey) ) {
                            if ( key.isAcceptable() ) {
                                SocketChannel channel = server.accept();
                                channel.configureBlocking(false);
                                SelectionKey clientKey = channel.register(selector, SelectionKey.OP_READ);
                                clientKey.attach(new GameChannelServer(channel, gameId, parameters));
                                gameId++;
                            }
                        } else {
                            SocketChannel channel = (SocketChannel) key.channel();
                            if (!key.isReadable()) {
                                continue;
                            }
                            System.out.println("--------------------------------------------------------------------------");

                            GameChannelServer gameChannelServer = (GameChannelServer) key.attachment();
                            gameChannelServer.run();

                            if ( gameChannelServer.doCloseChannel() ) {
                                key.cancel();
                                channel.close();
                            }
                            
                            System.out.println("--------------------------------------------------------------------------");
                        }
                    }
                } catch ( Exception ex ) {
                    ex.printStackTrace();
                }
            }
    }

    public static void main(String args[]) {
        try {
            GameSelector listener = new GameSelector(args);
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
        return Arrays.asList(new String[]{"-p", "-b", "-f"});
    }

    @Override
    public List<String> getOptionalArguments() {
        return Arrays.asList(new String[]{});
    }

}
