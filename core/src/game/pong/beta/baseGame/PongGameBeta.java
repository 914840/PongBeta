package game.pong.beta.baseGame;

import game.pong.beta.components.MenuScreen;
/**
 * @author Maciej Tymorek
 * @Project Pong
 * Class which sets default values for the game such as language, number of points and sets and default nick of the Player.
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
