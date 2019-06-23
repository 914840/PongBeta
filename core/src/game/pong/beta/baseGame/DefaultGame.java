package game.pong.beta.baseGame;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.NinePatch;
import com.badlogic.gdx.scenes.scene2d.ui.Label.LabelStyle;
import com.badlogic.gdx.scenes.scene2d.ui.List;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton.TextButtonStyle;
import com.badlogic.gdx.scenes.scene2d.ui.TextField.TextFieldStyle;
import com.badlogic.gdx.scenes.scene2d.utils.NinePatchDrawable;

/**
 * @author Maciej Tymorek
 * DefaultGame methods to create objects in game
 * Created when program is launched;
 * manages the screens that appear during the game.
 */
public abstract class DefaultGame extends Game {
    /**
     * Stores reference to game; used when calling setActiveScreen method.
     */
    private static DefaultGame game;

    public static LabelStyle labelStyle; // BitmapFont + Color
    public static TextButtonStyle textButtonStyle; // NPD + BitmapFont + Color
    public static TextFieldStyle textFieldStyle;


    /**
     * Called when game is initialized; stores global reference to game object.
     */
    public DefaultGame() {
        game = this;
    }

    /**
     * Called when game is initialized,
     * after Gdx.input and other objects have been initialized.
     */
    public void create() {
        // prepare for multiple classes/stages/baseGame to receive discrete input
        InputMultiplexer im = new InputMultiplexer();
        Gdx.input.setInputProcessor(im);

        labelStyle = new LabelStyle();
        BitmapFont newFont = new BitmapFont(Gdx.files.internal("Banschrift.fnt"));
        labelStyle.font = newFont;

        textFieldStyle = new TextFieldStyle();
        textFieldStyle.font = newFont;
        textFieldStyle.fontColor = Color.BLACK;
        textFieldStyle.background = null;

        textButtonStyle = new TextButtonStyle();

        Texture buttonTex = new Texture(Gdx.files.internal("button.png"));
        NinePatch buttonPatch = new NinePatch(buttonTex, 24, 24, 24, 24);
        textButtonStyle.up = new NinePatchDrawable(buttonPatch);
        textButtonStyle.font = newFont;
        textButtonStyle.fontColor = Color.BLACK;
        textButtonStyle.checkedFontColor = Color.GRAY;
        textButtonStyle.overFontColor = Color.GRAY;
        textButtonStyle.checkedOverFontColor = Color.GRAY;
        textButtonStyle.disabledFontColor = Color.GRAY;


        List.ListStyle listStyle = new List.ListStyle();
        listStyle.background = new NinePatchDrawable(
                new NinePatch
                        (
                                new Texture(Gdx.files.internal("button.png")),
                                10,
                                10,
                                10,
                                10
                        )
        );

        listStyle.font = newFont;
        listStyle.fontColorUnselected = Color.BLACK;
        listStyle.fontColorSelected = Color.GOLD;
    }

    /**
     * Used to switch screens while game is running.
     * Method is static to simplify usage.
     *
     * @param s parameter is used to access DefaultScreen object
     */
    public static void setActiveScreen(DefaultScreen s) {
        game.setScreen(s);
    }
}