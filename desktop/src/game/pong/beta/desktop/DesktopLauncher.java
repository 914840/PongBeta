package game.pong.beta.desktop;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import game.pong.beta.PongGameBeta;
import game.pong.beta.network.ClientThread;
import game.pong.beta.network.ServerThread;

public class DesktopLauncher {
	public static void main (String[] arg) {
		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
		config.width = 1600;
		config.height = 900;
		new LwjglApplication(new PongGameBeta(), config);
	}
	private void run(){
		ServerThread server = new ServerThread();
		Thread threadServer = new Thread(server);
		threadServer.setName("Server");
		threadServer.start();

		Thread threadClient = new Thread(new ClientThread());
		threadClient.setName("Client");
		threadClient.setDaemon(true);
		while (!server.isReady()){
			Thread.yield();
		}
		threadClient.start();
	}
}
