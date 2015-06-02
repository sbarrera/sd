package edu.ub.sd.sevenhalf.bank.selector;

import edu.ub.sd.sevenhalf.bank.multithreaded.GameServer;
import edu.ub.sd.sevenhalf.protocol.exception.EndOfStreamException;
import edu.ub.sd.sevenhalf.protocol.exception.MalformedGameCommandException;
import edu.ub.sd.sevenhalf.protocol.exception.NotEnoughBytesException;
import edu.ub.sd.sevenhalf.protocol.exception.UnexpectedGameCommandException;
import edu.ub.sd.sevenhalf.protocol.gamecommand.Error;
import edu.ub.sd.sevenhalf.runtime.GameParameters;
import edu.ub.sd.sevenhalf.utils.ComUtils;

import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;
import java.nio.file.Files;
import java.nio.file.Paths;

public class GameChannelServer extends GameServer {

    private ByteBuffer inBuffer = ByteBuffer.allocate(1024 * 4);
    private ByteBuffer outBuffer = ByteBuffer.allocate(256);

    private int byteCount = 0;
    private int id = -1;

    private boolean doProcess;
    private boolean closeChannel;

    private SocketChannel socketChannel;

    public GameChannelServer(SocketChannel socketChannel, int id, GameParameters parameters) throws Exception {
        super(parameters);

        this.socketChannel = socketChannel;
        this.id = id;

        connectionName = String.format("%s:%s", socketChannel.socket().getInetAddress().getHostAddress(), socketChannel.socket().getLocalPort());
    }

    @Override
    public void run() {
        closeChannel = false;
        doProcess = true;

        try {
            try {
                while ( doProcess ) {
                    System.out.println(String.format("state [%s] lastCommandHeader [%s] ip [%s]", state, lastCommandHeader, connectionName));
                    if (state == GameState.Initialized) {
                        onInitialize();
                    } else if (state == GameState.WaitingDraw) {
                        onWaitingDraw();
                    } else if (state == GameState.WaitingPlayerInput) {
                        onWaitingPlayerInput();
                        if (state == GameState.PlayBankTurn) {
                            onPlayBankTurn();
                            onNotifyScore();
                            System.out.println("Game has ended");
                            closeChannel = true;
                        }
                    }
                }
            } catch ( UnexpectedGameCommandException ugce ) {
                if ( Error.SyntacticName.equals(ugce.getCommandHeader()) ) {
                    Error error = (Error) parseCommand(Error.SyntacticName);
                    System.out.println(String.format("CLIENT REPORTED ERROR [%s]", error.getMessage()));
                } else {
                    sendCommand(new Error("Invalid command"));
                }

                closeChannel = true;
            } catch ( NotEnoughBytesException ef ) {
                // TODO Treat fragmented command header transmissions
            }
        } catch ( MalformedGameCommandException mgce ) {
            System.out.println(String.format("MALFORMED COMMAND EXCEPTION [%s]", mgce.getMessage()));
            closeChannel = true;
        } catch ( Exception ex ) {
            ex.printStackTrace();
            closeChannel = true;
        }
    }

    public boolean doCloseChannel() {
        return closeChannel;
    }

    @Override
    public byte[] readBytes(int numBytes) throws Exception {
        byte[] data = new byte[0];
        int readBytes = socketChannel.read(inBuffer);

        if ( readBytes == -1 ) {
            throw new EndOfStreamException("Received END-OF-STREAM");
        }

        //if ( readBytes == 0 ) {
        //    throw new NotEnoughBytesException();
        //}

        byteCount += readBytes;

//        System.out.println(String.format("readBytes [%s] byteCount [%s] numBytes [%s] remaining [%s]", readBytes, byteCount, numBytes, inBuffer.remaining()));
        System.out.println(String.format("readBytes [%s] byteCount [%s] numBytes [%s]", readBytes, byteCount, numBytes));

        if ( byteCount >= numBytes ) {
            data = new byte[numBytes];
//            System.out.println(String.format("before flip numBytes [%s] position [%s] remaining [%s]", numBytes, inBuffer.position(), inBuffer.remaining()));
            inBuffer.flip();
//            System.out.println(String.format("after flip numBytes [%s] position [%s] remaining [%s]", numBytes, inBuffer.position(), inBuffer.remaining()));
            inBuffer.get(data, 0, numBytes);
//            System.out.println(String.format("after get numBytes [%s] position [%s] remaining [%s]", numBytes, inBuffer.position(), inBuffer.remaining()));

            if ( inBuffer.remaining() > 0 ) {
//                byte[] remaining = new byte[inBuffer.remaining()];
//                System.out.println(
//                        String.format("position [%s] remaining [%s] inBuffer.last [%s]",
//                                inBuffer.position(),
//                                inBuffer.remaining(),
//                                inBuffer.position() + inBuffer.remaining()));
//                inBuffer.duplicate().get(remaining, 0, inBuffer.remaining());
//                System.out.println(String.format("remaining [%s]", ComUtils.bytesToHex(remaining)));
//                System.out.println(String.format("second flip before numBytes [%s] position [%s] remaining [%s]", numBytes, inBuffer.position(), inBuffer.remaining()));
                System.out.println("Compacting buffer...");
                inBuffer.compact();
                doProcess = true;
                //inBuffer.flip();
//                System.out.println(String.format("second flip after numBytes [%s] position [%s] remaining [%s]", numBytes, inBuffer.position(), inBuffer.remaining()));
            } else {
                System.out.println("Clearing buffer...");
                doProcess = false;
                inBuffer.clear();
            }

//            System.out.println(String.format("end numBytes [%s] position [%s] remaining [%s]", numBytes, inBuffer.position(), inBuffer.remaining()));
            byteCount -= numBytes;
        } else {
            throw new NotEnoughBytesException();
        }

        System.out.println(String.format("data [%s]", ComUtils.bytesToHex(data)));

        return data;
    }

    @Override
    public void send(byte bytes[]) throws Exception {
        outBuffer.clear();
        outBuffer.put(bytes);
        outBuffer.flip();
        socketChannel.write(outBuffer);
    }

    @Override
    protected void announce() {
    }

    @Override
    protected String expectCommandHeaders(String... syntacticNames) throws Exception {
        if ( lastCommandHeader != null ) {
            System.out.println(String.format("[%s] lastCommandHeader [%s]", "expectCommandHeaders", lastCommandHeader));
            return lastCommandHeader;
        }

        return super.expectCommandHeaders(syntacticNames);
    }

    protected String expectCommandHeader(String syntacticName) throws Exception {
        if ( lastCommandHeader != null ) {
            System.out.println(String.format("[%s] lastCommandHeader [%s]", "expectCommandHeader", lastCommandHeader));
            return lastCommandHeader;
        }

        return super.expectCommandHeader(syntacticName);
    }

    protected void initializeLog() throws Exception {
        logFilename = String.format("ServerGame-%s.log", id);
        logPath = Paths.get("./" + logFilename);
        Files.deleteIfExists(logPath);
    }

}
