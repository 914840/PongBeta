package game.pong.beta.components;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import game.pong.beta.BaseActor;
import game.pong.beta.BaseGame;
import game.pong.beta.BaseScreen;
import game.pong.beta.PongGameBeta;

public class SettingsScreen extends BaseScreen {

    protected Table uiTable;

    @Override
    public void initialize() {

        uiTable = new Table();
        uiTable.setFillParent(true);


        BaseActor background = new BaseActor(0,0,mainStage);
        background.loadTexture("abstract1600x900.jpg");
        background.setSize(mainStage.getWidth(), mainStage.getHeight());

        BaseActor title = new BaseActor(0,0,mainStage);
        title.loadTexture("settings.png");
        title.centerAtPosition(mainStage.getWidth()/2 + 25, mainStage.getHeight()- 100);


        Button.ButtonStyle buttonStyle = new Button.ButtonStyle();

        Texture buttonTex = new Texture(Gdx.files.internal("button.png") );
        TextureRegion buttonRegion = new TextureRegion(buttonTex);
        buttonStyle.up = new TextureRegionDrawable( buttonRegion );


        /**
         *  Label
         */


        BaseActor labelLanguage = new BaseActor( 0,0, mainStage);
        BaseActor labelSets = new BaseActor(0,0,mainStage);
        BaseActor labelPoints = new BaseActor(0,0,mainStage);
        BaseActor labelNick = new BaseActor(0,0,mainStage);
        TextButton backButton;
        if(language.equals("PL")){
            labelLanguage.loadTexture("label/Jezyk.png");
            labelSets.loadTexture("label/Sety.png");
            labelPoints.loadTexture("label/Punkty.png");
            labelNick.loadTexture("label/Nick.png");
            backButton = new TextButton(" Wstecz ", BaseGame.textButtonStyle );
        }
        else {
            labelLanguage.loadTexture("label/Language.png");
            labelSets.loadTexture("label/Sets.png");
            labelPoints.loadTexture("label/Points.png");
            labelNick.loadTexture("label/Nick.png");
            backButton = new TextButton(" Back ", BaseGame.textButtonStyle );
        }

        labelSets.loadTexture("label/Sets.png");

        labelPoints.loadTexture("label/Points.png");

        labelNick.loadTexture("label/Nick.png");

        ButtonGroup<TextButton> group1 = new ButtonGroup<>();
        TextButton bEN = new TextButton("EN", BaseGame.textButtonStyle);
        group1.add(bEN);
        TextButton bPL = new TextButton("PL", BaseGame.textButtonStyle);
        group1.add(bPL);

        ButtonGroup<TextButton> group2 = new ButtonGroup<>();
        TextButton b1s = new TextButton(" 1 ", BaseGame.textButtonStyle);
        group2.add(b1s);
        TextButton b3s = new TextButton(" 3 ", BaseGame.textButtonStyle);
        group2.add(b3s);
        TextButton b5s = new TextButton(" 5 ", BaseGame.textButtonStyle);
        group2.add(b5s);

        ButtonGroup<TextButton> group3 = new ButtonGroup<>();
        TextButton b3p = new TextButton(" 3 ", BaseGame.textButtonStyle);
        group3.add(b3p);
        TextButton b6p = new TextButton(" 6 ", BaseGame.textButtonStyle);
        group3.add(b6p);
        TextButton b11p = new TextButton(" 11 ", BaseGame.textButtonStyle);
        group3.add(b11p);

        TextField textField = new TextField("Player",BaseGame.textFieldStyle);


        uiStage.addActor(uiTable);


        /**
         *  Button
         */

        //TextButton backButton = new TextButton(" Back ", BaseGame.textButtonStyle );
        backButton.setPosition(mainStage.getWidth()/2 - 70, 100);
       // uiStage.addActor(backButton);

        uiTable.row();
        uiTable.add(labelLanguage);
        uiTable.add(bEN);
        uiTable.add(bPL);
        uiTable.row();
        uiTable.add(labelSets);
        uiTable.add(b1s);
        uiTable.add(b3s);
        uiTable.add(b5s);
        uiTable.row();
        uiTable.add(labelPoints);
        uiTable.add(b3p);
        uiTable.add(b6p);
        uiTable.add(b11p);
        uiTable.row();
        uiTable.add(labelNick);
        uiTable.add(textField);
        uiTable.row();
        uiTable.add(backButton).colspan(4);

        bPL.addListener(
                e -> {
                    if(!(e instanceof InputEvent) ||
                            !((InputEvent) e ).getType().equals(InputEvent.Type.touchDown) )
                        return false;

                    language = "PL";
                    bPL.isChecked();
                    //PongGameBeta.setActiveScreen(new SettingsScreen());
                    return true;
                }
        );

        textField.addListener(
                e -> {
                    if(!(e instanceof InputEvent) ||
                            !((InputEvent) e ).getType().equals(InputEvent.Type.touchDown) )
                        return false;

                    textField.setText(" ");
                    return true;

                }
        );

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
