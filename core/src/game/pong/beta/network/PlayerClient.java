package game.pong.beta.network;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Socket;

public class PlayerClient {
    private Socket playerSocket;
    private int port = 8100;

    public PlayerClient(String ip) throws IOException {
        playerSocket = new Socket();
        playerSocket.connect(new InetSocketAddress( ip, port));
    }

}
