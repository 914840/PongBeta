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

    private BaseActor background;

    protected Table uiTable;
    private ButtonGroup<TextButton> group1, group2, group3;
    private TextButton bEN, bPL;
    private TextButton b1s, b3s, b5s;
    private TextButton b3p, b6p, b11p;
    private BaseActor labelNick;
    private BaseActor labelPoints;
    private BaseActor labelSets;
    private BaseActor labelLanguage;
    private TextButton backButton;

    @Override
    public void initialize() {

        uiTable = new Table();
        uiTable.setFillParent(true);


        background = setBackground();


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
        group1 = new ButtonGroup<>();
        bEN = new TextButton("EN", BaseGame.textButtonStyle);
        group1.add(bEN);
        bPL = new TextButton("PL", BaseGame.textButtonStyle);
        group1.add(bPL);

        labelLanguage = new BaseActor( 0,0, mainStage);
        labelSets = new BaseActor(0,0,mainStage);
        labelPoints = new BaseActor(0,0,mainStage);
        labelNick = new BaseActor(0,0,mainStage);
        if(PongGameBeta.gameLanguage.equals("PL"))
        {
            labelLanguage.loadTexture("label/Jezyk.png");
            labelSets.loadTexture("label/Sety.png");
            labelPoints.loadTexture("label/Punkty.png");
            labelNick.loadTexture("label/Nick.png");
            backButton = new TextButton(" Wstecz ", BaseGame.textButtonStyle );
            bPL.setChecked(true);
        }
        else
        {
            labelLanguage.loadTexture("label/Language.png");
            labelSets.loadTexture("label/Sets.png");
            labelPoints.loadTexture("label/Points.png");
            labelNick.loadTexture("label/Nick.png");
            backButton = new TextButton(" Back ", BaseGame.textButtonStyle );
        }

//        labelSets.loadTexture("label/Sets.png");
//
//        labelPoints.loadTexture("label/Points.png");
//
//        labelNick.loadTexture("label/Nick.png");

      // **********************

        group2 = new ButtonGroup<>();
        b1s = new TextButton(" 1 ", BaseGame.textButtonStyle);
        group2.add(b1s);
        b3s = new TextButton(" 3 ", BaseGame.textButtonStyle);
        group2.add(b3s);
        b5s = new TextButton(" 5 ", BaseGame.textButtonStyle);
        group2.add(b5s);


         group3 = new ButtonGroup<>();
        b3p = new TextButton(" 3 ", BaseGame.textButtonStyle);
        group3.add(b3p);
        b6p = new TextButton(" 6 ", BaseGame.textButtonStyle);
        group3.add(b6p);
        b11p = new TextButton(" 11 ", BaseGame.textButtonStyle);
        group3.add(b11p);

        if(PongGameBeta.sets == 3)
        {
            b3s.setChecked(true);
        }
        else if( PongGameBeta.sets == 5)
        {
            b5s.setChecked(true);
        }
        if( PongGameBeta.points == 6)
        {
            b6p.setChecked(true);
        }
        else if( PongGameBeta.points == 11)
        {
            b11p.setChecked(true);
        }




        TextField textField = new TextField( PongGameBeta.nick , BaseGame.textFieldStyle );


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

                    PongGameBeta.gameLanguage = "PL";
                    //PongGameBeta.setActiveScreen(new SettingsScreen());
                    return true;
                }
        );

        bEN.addListener(
                e -> {
                    if(!(e instanceof InputEvent) ||
                            !((InputEvent) e ).getType().equals(InputEvent.Type.touchDown) )
                        return false;

                    PongGameBeta.gameLanguage = "EN";
                    //PongGameBeta.setActiveScreen(new SettingsScreen());
                    return true;
                }
        );

        b1s.addListener(
                e -> {
                    if(!(e instanceof InputEvent) ||
                            !((InputEvent) e ).getType().equals(InputEvent.Type.touchDown) )
                        return false;

                    PongGameBeta.sets = 1;
                    return true;
                }
        );

        b3s.addListener(
                e -> {
                    if(!(e instanceof InputEvent) ||
                            !((InputEvent) e ).getType().equals(InputEvent.Type.touchDown) )
                        return false;

                    PongGameBeta.sets = 3;
                    return true;
                }
        );

        b5s.addListener(
                e -> {
                    if(!(e instanceof InputEvent) ||
                            !((InputEvent) e ).getType().equals(InputEvent.Type.touchDown) )
                        return false;

                    PongGameBeta.sets = 5;
                    return true;
                }
        );

        b3p.addListener(
                e -> {
                    if(!(e instanceof InputEvent) ||
                            !((InputEvent) e ).getType().equals(InputEvent.Type.touchDown) )
                        return false;

                    PongGameBeta.points = 3;
                    return true;
                }
        );

        b6p.addListener(
                e -> {
                    if(!(e instanceof InputEvent) ||
                            !((InputEvent) e ).getType().equals(InputEvent.Type.touchDown) )
                        return false;

                    PongGameBeta.points = 6;
                    return true;
                }
        );

        b11p.addListener(
                e -> {
                    if(!(e instanceof InputEvent) ||
                            !((InputEvent) e ).getType().equals(InputEvent.Type.touchDown) )
                        return false;

                    PongGameBeta.points = 11;
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

                    PongGameBeta.nick = textField.getText();
                    PongGameBeta.setActiveScreen(new MenuScreen());
                    return false;
                }
        );
    }

    @Override
    public void update(float dt) {
        if(PongGameBeta.gameLanguage.equals("PL")){
            labelLanguage.loadTexture("label/Jezyk.png");
            labelSets.loadTexture("label/Sety.png");
            labelPoints.loadTexture("label/Punkty.png");
            labelNick.loadTexture("label/Nick.png");
            backButton = new TextButton(" Wstecz ", BaseGame.textButtonStyle );
            bPL.setChecked(true);
        }
        else {
            labelLanguage.loadTexture("label/Language.png");
            labelSets.loadTexture("label/Sets.png");
            labelPoints.loadTexture("label/Points.png");
            labelNick.loadTexture("label/Nick.png");
            backButton = new TextButton(" Back ", BaseGame.textButtonStyle );
        }
    }
}
