package game.pong.beta.components;


import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;
import game.pong.beta.baseGame.DefaultActor;
import game.pong.beta.baseGame.DefaultGame;
import game.pong.beta.baseGame.DefaultScreen;
import game.pong.beta.baseGame.PongGameBeta;

import java.io.IOException;
import java.net.InetAddress;
import java.util.Objects;
import java.util.Scanner;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author Paweł Kumanowski
 * Class which sets up the Multi-player Lobby Screen
 */

public class LobbyScreen extends DefaultScreen {

    private DefaultActor background;
    private DefaultActor title;

    protected Table uiTable;

    private TextButton joinButton, createButton, backButton;
    private TextField ipAdress, nickText;
    private Label ip, nick, rules;
    private Label waiting;
    private String ipS;

    private InetAddress IP;

    /**
     * Method which initializes Lobby screen, by setting background and button pictures and their string messages. <br>
     * Method also adds listeners to the buttons created.
     */
    @Override
    public void initialize() {

        background = setBackground();
        title = setTitle("multiplayer.png");

        setButtonStyleTexture("button.png");

        uiTable = new Table();
        uiTable.setFillParent(true);

        if (PongGameBeta.gameLanguage.equals("PL")) {
            joinButton = new TextButton("    Dolacz    ", DefaultGame.textButtonStyle);
            createButton = new TextButton("    Stworz    ", DefaultGame.textButtonStyle);
            backButton = new TextButton(" Wstecz ", DefaultGame.textButtonStyle);
            ip = new Label("Adres IP:", DefaultGame.labelStyle);
            waiting = new Label(" OCZEKIWANIE NA GRACZA ", DefaultGame.labelStyle);
        } else {
            joinButton = new TextButton("     Join     ", DefaultGame.textButtonStyle);
            createButton = new TextButton("    Create    ", DefaultGame.textButtonStyle);
            backButton = new TextButton("  Back  ", DefaultGame.textButtonStyle);
            ip = new Label("IP adress: ", DefaultGame.labelStyle);
            waiting = new Label(" WAITING FOR THE PLAYER ", DefaultGame.labelStyle);
        }

        String command;
        if (System.getProperty("os.name").equals("Mac OS X"))
            command = "ifconfig";
        else
            command = "ipconfig";
        Runtime r = Runtime.getRuntime();
        Process p = null;
        try {
            p = r.exec(command);
        } catch (IOException e) {
            e.printStackTrace();
        }
        Scanner s = new Scanner(Objects.requireNonNull(p).getInputStream());

        StringBuilder sb = new StringBuilder();
        while (s.hasNext())
            sb.append(s.next());
        String ipconfig = sb.toString();
        Pattern pt = Pattern.compile("192\\.168\\.[0-9]{1,3}\\.[0-9]{1,3}");
        Matcher mt = pt.matcher(ipconfig);
        mt.find();
        ipS = mt.group();

//        try {
//            IP = InetAddress.getLocalHost();
//        } catch (UnknownHostException e) {
//            e.printStackTrace();
//        }

        ipAdress = new TextField(ipS, DefaultGame.textFieldStyle);
        nick = new Label("Nick: ", DefaultGame.labelStyle);
        nickText = new TextField(PongGameBeta.nick, DefaultGame.textFieldStyle);


        //uiTable.pad(6);
        uiTable.add(ip).width(150).pad(50);
        uiTable.add(ipAdress).width(200).pad(10);
        uiTable.row();
        uiTable.add(nick).width(150).pad(50);
        uiTable.add(nickText).width(200).pad(10);
        uiTable.row();
        uiTable.add().width(100);
        uiTable.add(joinButton);
        uiTable.add(createButton).pad(150);
        uiTable.add().expandX();
        uiTable.row();
        uiTable.add(backButton).colspan(6);


        uiStage.addActor(uiTable);

        AtomicBoolean flagActiveNickText = new AtomicBoolean(true);
        nickText.addListener(
                e -> {
                    if (!(e instanceof InputEvent) ||
                            !((InputEvent) e).getType().equals(InputEvent.Type.touchDown))
                        return false;
                    if (flagActiveNickText.get()) {
                        nickText.setText("");
                    }
                    return true;

                }
        );
        AtomicBoolean flagActiveIpAdress = new AtomicBoolean(true);
        ipAdress.addListener(
                e -> {
                    if (!(e instanceof InputEvent) ||
                            !((InputEvent) e).getType().equals(InputEvent.Type.touchDown))
                        return false;
                    if (flagActiveIpAdress.get()) {
                        ipAdress.setText("");
                    }
                    return true;
                }
        );

        createButton.addListener(
                e -> {
                    if (!(e instanceof InputEvent) ||
                            !((InputEvent) e).getType().equals(InputEvent.Type.touchDown))
                        return false;

                    PongGameBeta.nick = nickText.getText();

                    waiting.setPosition(mainStage.getWidth() / 2 - 200, mainStage.getHeight() / 2);
                    ipAdress.setDisabled(true);
                    flagActiveIpAdress.set(false);
                    nickText.setDisabled(true);
                    flagActiveNickText.set(false);
                    joinButton.setDisabled(true);
                    mainStage.addActor(waiting);

                    MultiScreenServer multiScreenServer = new MultiScreenServer();
                    PongGameBeta.setActiveScreen(multiScreenServer);

                    return true;
                }
        );

        joinButton.addListener(
                e -> {
                    if (!(e instanceof InputEvent) ||
                            !((InputEvent) e).getType().equals(InputEvent.Type.touchDown))
                        return false;

                    ipAdress.setDisabled(true);
                    flagActiveIpAdress.set(false);
                    nickText.setDisabled(true);
                    flagActiveNickText.set(false);
                    createButton.setDisabled(true);


                    PongGameBeta.nick = nickText.getText();
                    PongGameBeta.ipHost = ipAdress.getText();
                    MultiScreenClient multiScreenClient = new MultiScreenClient(ipAdress.getText());

                    PongGameBeta.setActiveScreen(multiScreenClient);


                    return true;
                }
        );

        backButton.addListener(
                e -> {
                    if (!(e instanceof InputEvent) ||
                            !((InputEvent) e).getType().equals(InputEvent.Type.touchDown))
                        return false;

                    PongGameBeta.nick = nickText.getText();
                    PongGameBeta.setActiveScreen(new MenuScreen());
                    return false;
                }
        );

    }


    @Override
    public void update(float dt) {

    }
}
