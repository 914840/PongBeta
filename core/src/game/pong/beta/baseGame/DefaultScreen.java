package game.pong.beta.baseGame;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;

/**
 * @author Paweł Kumanowski
 * Class is used as a template for other game screens.
 */
public abstract class DefaultScreen implements Screen, InputProcessor {
    protected Stage mainStage;
    protected Stage uiStage;

    /**
     * Default screen constructor
     */
    public DefaultScreen() {
        mainStage = new Stage();
        uiStage = new Stage();
        initialize();
    }

    /**
     * Abstract method for initialization
     */
    public abstract void initialize();

    /**
     * Abstract update method
     * @param dt Time elapsed since previous frame (delta time); typically obtained from <code>act</code> method.
     */
    public abstract void update(float dt);

    // Gameloop:
    // (1) process input (discrete handled by listener; continuous in update)
    // (2) update game logic
    // (3) render the graphics
    public void render(float dt) {
        // act methods
        uiStage.act(dt);
        mainStage.act(dt);

        // defined by user
        update(dt);

        // clear the screen
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        // draw the graphics
        mainStage.draw();
        uiStage.draw();
    }

    /**
     * Method for resizing game window
     * @param width int
     * @param height int
     */
    public void resize(int width, int height) {
    }

    /**
     * Method for pausing the game
     */
    public void pause() {
    }

    /**
     * Method for resuming the game
     */
    public void resume() {
    }

    /**
     * Method for disposing the game
     */
    public void dispose() {
    }

    /**
     * Called when this becomes the active screen in a Game.
     * Set up InputMultiplexer here, in case screen is reactivated at a later time.
     */
    public void show() {
        InputMultiplexer im = (InputMultiplexer) Gdx.input.getInputProcessor();
        im.addProcessor(this);
        im.addProcessor(uiStage);
        im.addProcessor(mainStage);
    }

    /**
     * Called when this is no longer the active screen in a Game.
     * Screen class and Stages no longer process input.
     * Other InputProcessors must be removed manually.
     */
    public void hide() {
        InputMultiplexer im = (InputMultiplexer) Gdx.input.getInputProcessor();
        im.removeProcessor(this);
        im.removeProcessor(uiStage);
        im.removeProcessor(mainStage);
    }

    // methods required by InputProcessor interface
    public boolean keyDown(int keycode) {
        return false;
    }

    public boolean keyUp(int keycode) {
        return false;
    }

    public boolean keyTyped(char c) {
        return false;
    }

    public boolean mouseMoved(int screenX, int screenY) {
        return false;
    }

    public boolean scrolled(int amount) {
        return false;
    }

    public boolean touchDown(int screenX, int screenY, int pointer, int button) {
        return false;
    }

    public boolean touchDragged(int screenX, int screenY, int pointer) {
        return false;
    }

    public boolean touchUp(int screenX, int screenY, int pointer, int button) {
        return false;
    }

    /**
     * this method sets background for screen
     *
     * @return DefaultActor background
     */
    public DefaultActor setBackground() {
        DefaultActor background = new DefaultActor(0, 0, mainStage);
        background.loadTexture("abstract1600x900.jpg");
        background.setSize(mainStage.getWidth(), mainStage.getHeight());
        return background;
    }

    /**
     * this method load texture of title from fileName - png file.
     *
     * @param fileName name of file.png
     * @return DefaultActor title
     */
    public DefaultActor setTitle(String fileName) {
        DefaultActor title = new DefaultActor(0, 0, mainStage);
        title.loadTexture(fileName);
        title.centerAtPosition(mainStage.getWidth() / 2 + 25, mainStage.getHeight() - 100);
        return title;
    }

    /**
     * Called when screen have Buttons to initialize buttonStyle, buttonTexture, TextureRegion
     *
     * @param fileName name of file.png
     */
    public void setButtonStyleTexture(String fileName) {
        Button.ButtonStyle buttonStyle = new Button.ButtonStyle();
        Label.LabelStyle labelStyle = new Label.LabelStyle();

        Texture buttonTex = new Texture(Gdx.files.internal(fileName));
        TextureRegion buttonRegion = new TextureRegion(buttonTex);
        buttonStyle.up = new TextureRegionDrawable(buttonRegion);
    }

}