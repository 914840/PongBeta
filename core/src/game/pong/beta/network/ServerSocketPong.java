package game.pong.beta.network;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class ServerSocketPong {

    private int port = 8100;
    private ServerSocket serverSocket;

    public ServerSocketPong()
    {
        Runnable r = () -> {
            try {
                newServerSocketPong();
            } catch (IOException e) {
                e.printStackTrace();
            }
        };
        Thread t = new Thread(r);
        t.start();
    }



    public void newServerSocketPong() throws IOException {
        try (ServerSocket serverSocket = new ServerSocket(8100)) {

            //oczekuje na połączenie klienta
            try (Socket incoming = serverSocket.accept()) {
                //incoming.setSoTimeout(1000);
            }
        }
        finally {
            serverSocket.close();
        }

    }
}
