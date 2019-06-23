package game.pong.beta.components;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import game.pong.beta.baseGame.DefaultActor;
import game.pong.beta.baseGame.DefaultGame;
import game.pong.beta.baseGame.DefaultScreen;
import game.pong.beta.baseGame.PongGameBeta;

/**
 * @author Maciej Tymorek
 * Class sets the default values and looks of the Setting screen.
 */
public class SettingsScreen extends DefaultScreen {

    private DefaultActor background;

    protected Table uiTable;
    private ButtonGroup<TextButton> group1, group2, group3;
    private TextButton bEN, bPL;
    private TextButton b1s, b3s, b5s;
    private TextButton b3p, b6p, b11p;
    private DefaultActor labelNick;
    private DefaultActor labelPoints;
    private DefaultActor labelSets;
    private DefaultActor labelLanguage;
    private TextButton backButton;

    /**
     * Method used for initialization of the Settings screen which sets buttons look, and listeners.
     */
    @Override
    public void initialize() {

        uiTable = new Table();
        uiTable.setFillParent(true);


        background = setBackground();


        DefaultActor title = new DefaultActor(0, 0, mainStage);
        title.loadTexture("settings.png");
        title.centerAtPosition(mainStage.getWidth() / 2 + 25, mainStage.getHeight() - 100);


        Button.ButtonStyle buttonStyle = new Button.ButtonStyle();

        Texture buttonTex = new Texture(Gdx.files.internal("button.png"));
        TextureRegion buttonRegion = new TextureRegion(buttonTex);
        buttonStyle.up = new TextureRegionDrawable(buttonRegion);

        group1 = new ButtonGroup<>();
        bEN = new TextButton("EN", DefaultGame.textButtonStyle);
        group1.add(bEN);
        bPL = new TextButton("PL", DefaultGame.textButtonStyle);
        group1.add(bPL);

        labelLanguage = new DefaultActor(0, 0, mainStage);
        labelSets = new DefaultActor(0, 0, mainStage);
        labelPoints = new DefaultActor(0, 0, mainStage);
        labelNick = new DefaultActor(0, 0, mainStage);
        if (PongGameBeta.gameLanguage.equals("PL")) {
            labelLanguage.loadTexture("label/Jezyk.png");
            labelSets.loadTexture("label/Sety.png");
            labelPoints.loadTexture("label/Punkty.png");
            labelNick.loadTexture("label/Nick.png");
            backButton = new TextButton(" Wstecz ", DefaultGame.textButtonStyle);
            bPL.setChecked(true);
        } else {
            labelLanguage.loadTexture("label/Language.png");
            labelSets.loadTexture("label/Sets.png");
            labelPoints.loadTexture("label/Points.png");
            labelNick.loadTexture("label/Nick.png");
            backButton = new TextButton(" Back ", DefaultGame.textButtonStyle);
        }

        group2 = new ButtonGroup<>();
        b1s = new TextButton(" 1 ", DefaultGame.textButtonStyle);
        group2.add(b1s);
        b3s = new TextButton(" 3 ", DefaultGame.textButtonStyle);
        group2.add(b3s);
        b5s = new TextButton(" 5 ", DefaultGame.textButtonStyle);
        group2.add(b5s);


        group3 = new ButtonGroup<>();
        b3p = new TextButton(" 3 ", DefaultGame.textButtonStyle);
        group3.add(b3p);
        b6p = new TextButton(" 6 ", DefaultGame.textButtonStyle);
        group3.add(b6p);
        b11p = new TextButton(" 11 ", DefaultGame.textButtonStyle);
        group3.add(b11p);

        if (PongGameBeta.sets == 3) {
            b3s.setChecked(true);
        } else if (PongGameBeta.sets == 5) {
            b5s.setChecked(true);
        }
        if (PongGameBeta.points == 6) {
            b6p.setChecked(true);
        } else if (PongGameBeta.points == 11) {
            b11p.setChecked(true);
        }


        TextField textField = new TextField(PongGameBeta.nick, DefaultGame.textFieldStyle);


        uiStage.addActor(uiTable);

        //TextButton backButton = new TextButton(" Back ", DefaultGame.textButtonStyle );
        backButton.setPosition(mainStage.getWidth() / 2 - 70, 100);
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
                    if (!(e instanceof InputEvent) ||
                            !((InputEvent) e).getType().equals(InputEvent.Type.touchDown))
                        return false;

                    PongGameBeta.gameLanguage = "PL";
                    //PongGameBeta.setActiveScreen(new SettingsScreen());
                    return true;
                }
        );

        bEN.addListener(
                e -> {
                    if (!(e instanceof InputEvent) ||
                            !((InputEvent) e).getType().equals(InputEvent.Type.touchDown))
                        return false;

                    PongGameBeta.gameLanguage = "EN";
                    return true;
                }
        );

        b1s.addListener(
                e -> {
                    if (!(e instanceof InputEvent) ||
                            !((InputEvent) e).getType().equals(InputEvent.Type.touchDown))
                        return false;

                    PongGameBeta.sets = 1;
                    return true;
                }
        );

        b3s.addListener(
                e -> {
                    if (!(e instanceof InputEvent) ||
                            !((InputEvent) e).getType().equals(InputEvent.Type.touchDown))
                        return false;

                    PongGameBeta.sets = 3;
                    return true;
                }
        );

        b5s.addListener(
                e -> {
                    if (!(e instanceof InputEvent) ||
                            !((InputEvent) e).getType().equals(InputEvent.Type.touchDown))
                        return false;

                    PongGameBeta.sets = 5;
                    return true;
                }
        );

        b3p.addListener(
                e -> {
                    if (!(e instanceof InputEvent) ||
                            !((InputEvent) e).getType().equals(InputEvent.Type.touchDown))
                        return false;

                    PongGameBeta.points = 3;
                    return true;
                }
        );

        b6p.addListener(
                e -> {
                    if (!(e instanceof InputEvent) ||
                            !((InputEvent) e).getType().equals(InputEvent.Type.touchDown))
                        return false;

                    PongGameBeta.points = 6;
                    return true;
                }
        );

        b11p.addListener(
                e -> {
                    if (!(e instanceof InputEvent) ||
                            !((InputEvent) e).getType().equals(InputEvent.Type.touchDown))
                        return false;

                    PongGameBeta.points = 11;
                    return true;
                }
        );

        textField.addListener(
                e -> {
                    if (!(e instanceof InputEvent) ||
                            !((InputEvent) e).getType().equals(InputEvent.Type.touchDown))
                        return false;

                    textField.setText("");
                    return true;

                }
        );

        backButton.addListener(
                e -> {
                    if (!(e instanceof InputEvent) ||
                            !((InputEvent) e).getType().equals(InputEvent.Type.touchDown))
                        return false;

                    PongGameBeta.nick = textField.getText();
                    PongGameBeta.setActiveScreen(new MenuScreen());
                    return false;
                }
        );
    }

    /**
     * Method for loading textures with condition for language selected
     *
     * @param dt Time elapsed since previous frame (delta time); typically obtained from <code>act</code> method.
     */
    @Override
    public void update(float dt) {
        if (PongGameBeta.gameLanguage.equals("PL")) {
            labelLanguage.loadTexture("label/Jezyk.png");
            labelSets.loadTexture("label/Sety.png");
            labelPoints.loadTexture("label/Punkty.png");
            labelNick.loadTexture("label/Nick.png");
            backButton = new TextButton(" Wstecz ", DefaultGame.textButtonStyle);
            bPL.setChecked(true);
        } else {
            labelLanguage.loadTexture("label/Language.png");
            labelSets.loadTexture("label/Sets.png");
            labelPoints.loadTexture("label/Points.png");
            labelNick.loadTexture("label/Nick.png");
            backButton = new TextButton(" Back ", DefaultGame.textButtonStyle);
        }
    }
}
