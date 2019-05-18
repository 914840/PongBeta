package game.pong.beta.components;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import game.pong.beta.BaseActor;
import game.pong.beta.BaseGame;
import game.pong.beta.BaseScreen;
import game.pong.beta.PongGameBeta;

public class LobbyScreen extends BaseScreen {

    private BaseActor background;
    private BaseActor title;

    protected Table uiTable;

    private TextButton joinButton, createButton, backButton;

    @Override
    public void initialize() {

        background = setBackground();
        title = setTitle("multiplayer.png");

        setButtonStyleTexture("button.png");

        uiTable = new Table();
        uiTable.setFillParent(true);

        if(PongGameBeta.gameLanguage.equals("PL"))
        {
            joinButton = new TextButton(" Dolacz ", BaseGame.textButtonStyle);
            createButton = new TextButton(" Stworz ", BaseGame.textButtonStyle);
            backButton = new TextButton(" Wstecz ", BaseGame.textButtonStyle );
        }
        else
        {
            joinButton = new TextButton(" Join ", BaseGame.textButtonStyle);
            createButton = new TextButton(" Create ", BaseGame.textButtonStyle);
            backButton = new TextButton(" Back ", BaseGame.textButtonStyle );
        }

        uiTable.row();
        uiTable.add(joinButton);
        uiTable.add(createButton);
        uiTable.row();
        uiTable.add(backButton).colspan(2);

        uiStage.addActor(uiTable);

        backButton.addListener(
                e -> {
                    if(!(e instanceof InputEvent) ||
                            !((InputEvent) e ).getType().equals(InputEvent.Type.touchDown) )
                        return false;

                    PongGameBeta.setActiveScreen(new MenuScreen());
                    return false;
                }
        );

    }



    @Override
    public void update(float dt) {

    }
}
