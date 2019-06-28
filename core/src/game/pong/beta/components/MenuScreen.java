package game.pong.beta.components;


import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputEvent.Type;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import game.pong.beta.baseGame.DefaultActor;
import game.pong.beta.baseGame.DefaultGame;
import game.pong.beta.baseGame.DefaultScreen;
import game.pong.beta.baseGame.PongGameBeta;

/**
 * @author Paweł Kumanowski
 * Class used for Creating Menu screen that Player sees when he initiates the Game.
 */
public class MenuScreen extends DefaultScreen {
    private DefaultActor background;
    private DefaultActor title;

    private TextButton startButton;
    private TextButton multiButton;
    private TextButton settingsButton;
    private TextButton exitButton;

    /**
     * Method used for initialization of the Menu screen which sets buttons look, and listeners.
     */
    @Override
    public void initialize() {
        background = setBackground();
        title = setTitle("Pong.png");

        setButtonStyleTexture("button.png");

        if (PongGameBeta.gameLanguage.equals("PL")) {
            startButton = new TextButton("  Jeden Gracz  ", DefaultGame.textButtonStyle);
            multiButton = new TextButton("  Wielu Graczy ", DefaultGame.textButtonStyle);
            settingsButton = new TextButton("    Ustawienia    ", DefaultGame.textButtonStyle);
            exitButton = new TextButton("         Wyjscie        ", DefaultGame.textButtonStyle);
        } else {
            startButton = new TextButton("Single Player", DefaultGame.textButtonStyle);
            multiButton = new TextButton("  Multi Player  ", DefaultGame.textButtonStyle);
            settingsButton = new TextButton("       Settings       ", DefaultGame.textButtonStyle);
            exitButton = new TextButton("            Exit              ", DefaultGame.textButtonStyle);
        }

        //TextButton startButton = new TextButton("Single Player", DefaultGame.textButtonStyle );
        startButton.setPosition(mainStage.getWidth() / 2 - 120, (mainStage.getHeight() / 2) + 100);
        uiStage.addActor(startButton);

        //TextButton multiButton = new TextButton("  Multi Player  ", DefaultGame.textButtonStyle);
        multiButton.setPosition(mainStage.getWidth() / 2 - 120, (mainStage.getHeight() / 2));
        uiStage.addActor(multiButton);

        //TextButton settingsButton = new TextButton( "       Settings       " , DefaultGame.textButtonStyle);
        settingsButton.setPosition((mainStage.getWidth() / 2) - 120, (mainStage.getHeight() / 2) - 100);
        uiStage.addActor(settingsButton);

        //TextButton exitButton = new TextButton("            Exit              ", DefaultGame.textButtonStyle);
        exitButton.setPosition((mainStage.getWidth() / 2) - 120, (mainStage.getHeight() / 2) - 200);
        uiStage.addActor(exitButton);


        startButton.addListener(
                e -> {
                    if (!(e instanceof InputEvent) ||
                            !((InputEvent) e).getType().equals(Type.touchDown))
                        return false;

                    PongGameBeta.setActiveScreen(new LevelScreen());
                    return false;
                }
        );

        multiButton.addListener(
                e -> {
                    if (!(e instanceof InputEvent) ||
                            !((InputEvent) e).getType().equals(Type.touchDown))
                        return false;

                    PongGameBeta.setActiveScreen(new LobbyScreen());
                    return false;
                }
        );

        settingsButton.addListener(
                e -> {
                    if (!(e instanceof InputEvent) ||
                            !((InputEvent) e).getType().equals(Type.touchDown))
                        return false;

                    PongGameBeta.setActiveScreen(new SettingsScreen());
                    return false;
                }
        );

        exitButton.addListener(
                e -> {
                    if (!(e instanceof InputEvent) ||
                            !((InputEvent) e).getType().equals(Type.touchDown))
                        return false;

                    Gdx.app.exit();
                    return false;
                }
        );
    }

    @Override
    public void update(float dt) {

    }
}
