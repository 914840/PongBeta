package game.pong.beta.components;
/**
 * @Author: Maciej Tymorek
 * @Project: Pong
 *
 */
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Event;
import com.badlogic.gdx.scenes.scene2d.EventListener;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputEvent.Type;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Button.ButtonStyle;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import game.pong.beta.BaseActor;
import game.pong.beta.BaseGame;
import game.pong.beta.BaseScreen;
import game.pong.beta.PongGameBeta;

public class MenuScreen extends BaseScreen
{
    private BaseActor background;
    private BaseActor title;

    private TextButton startButton;
    private TextButton multiButton;
    private TextButton settingsButton;
    private TextButton exitButton;

    @Override
    public void initialize()
    {
        background = setBackground();
        title = setTitle("pong.png");

        setButtonStyleTexture("button.png");

        /**
         *  Menu buttons
         */


        if(PongGameBeta.gameLanguage.equals("PL")){
            startButton = new TextButton("  Jeden Gracz  ", BaseGame.textButtonStyle );
            multiButton = new TextButton("  Wielu Graczy ", BaseGame.textButtonStyle);
            settingsButton = new TextButton( "    Ustawienia    " , BaseGame.textButtonStyle);
            exitButton = new TextButton("         Wyjscie        ", BaseGame.textButtonStyle);
        }
        else {
            startButton = new TextButton("Single Player", BaseGame.textButtonStyle );
            multiButton = new TextButton("  Multi Player  ", BaseGame.textButtonStyle);
            settingsButton = new TextButton( "       Settings       " , BaseGame.textButtonStyle);
            exitButton = new TextButton("            Exit              ", BaseGame.textButtonStyle);
        }

        //TextButton startButton = new TextButton("Single Player", BaseGame.textButtonStyle );
        startButton.setPosition(mainStage.getWidth()/2 - 120, (mainStage.getHeight()/2)+ 100);
        uiStage.addActor(startButton);

        //TextButton multiButton = new TextButton("  Multi Player  ", BaseGame.textButtonStyle);
        multiButton.setPosition(mainStage.getWidth()/2 - 120, (mainStage.getHeight()/2));
        uiStage.addActor(multiButton);

        //TextButton settingsButton = new TextButton( "       Settings       " , BaseGame.textButtonStyle);
        settingsButton.setPosition( (mainStage.getWidth()/2) - 120, (mainStage.getHeight()/2) -100 );
        uiStage.addActor(settingsButton);

        //TextButton exitButton = new TextButton("            Exit              ", BaseGame.textButtonStyle);
        exitButton.setPosition((mainStage.getWidth()/2) - 120, (mainStage.getHeight()/2) -200);
        uiStage.addActor(exitButton);


        startButton.addListener(
                e -> {
                    if (!(e instanceof InputEvent) ||
                            !((InputEvent) e).getType().equals(Type.touchDown) )
                        return false;

                    PongGameBeta.setActiveScreen(new LevelScreen());
                    return false;
                }
        );

        multiButton.addListener(
                e -> {
                    if (!(e instanceof InputEvent) ||
                            !((InputEvent) e).getType().equals(Type.touchDown) )
                        return false;

                    PongGameBeta.setActiveScreen(new LobbyScreen());
                    return false;
                }
        );

        settingsButton.addListener(
                e -> {
                    if (!(e instanceof InputEvent) ||
                            !((InputEvent) e).getType().equals(Type.touchDown) )
                        return false;

                    PongGameBeta.setActiveScreen(new SettingsScreen());
                    return false;
                }
        );

        exitButton.addListener(
                e -> {
                    if (!(e instanceof InputEvent) ||
                            !((InputEvent) e).getType().equals(Type.touchDown) )
                        return false;

                    Gdx.app.exit();
                    return false;
                }
        );
    }

    @Override
    public void update(float dt)
    {

    }
}
