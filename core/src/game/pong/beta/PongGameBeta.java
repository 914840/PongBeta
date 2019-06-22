package game.pong.beta;

import game.pong.beta.components.MenuScreen;
/**
 * @author Maciej Tymorek
 * @Project Pong
 */

public class PongGameBeta extends BaseGame
{
	public static String gameLanguage = "EN";
	public static int sets = 1 , points = 3;
	public static String nick = "Player1";
	public static String ipHost = " ";
	
	public void create()
	{
		super.create();

		setActiveScreen( new MenuScreen() );
	}

}
