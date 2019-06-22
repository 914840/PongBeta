package game.pong.beta.components;

import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;
import game.pong.beta.BaseActor;
import game.pong.beta.BaseGame;
import game.pong.beta.BaseScreen;
import game.pong.beta.PongGameBeta;
//import game.pong.beta.UDP.UDPClient;
////import game.pong.beta.network.ServerSocketPong;
//import game.pong.beta.UDP.UDPServer;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.*;
import java.util.Scanner;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class LobbyScreen extends BaseScreen {

    private BaseActor background;
    private BaseActor title;

    protected Table uiTable;

    private TextButton joinButton, createButton, backButton;
    private TextField ipAdress, nickText;
    private Label ip, nick, rules;
    private Label waiting;
    private String ipS;



    private InetAddress IP;

    @Override
    public void initialize() {

        background = setBackground();
        title = setTitle("multiplayer.png");

        setButtonStyleTexture("button.png");

        uiTable = new Table();
        uiTable.setFillParent(true);

        if(PongGameBeta.gameLanguage.equals("PL"))
        {
            joinButton = new TextButton("    Dolacz    ", BaseGame.textButtonStyle);
            createButton = new TextButton("    Stworz    ", BaseGame.textButtonStyle);
            backButton = new TextButton(" Wstecz ", BaseGame.textButtonStyle );
            ip = new Label("Adres IP:", BaseGame.labelStyle);
            waiting = new Label( " OCZEKIWANIE NA GRACZA ", BaseGame.labelStyle);
        }
        else
        {
            joinButton = new TextButton("     Join     ", BaseGame.textButtonStyle);
            createButton = new TextButton("    Create    ", BaseGame.textButtonStyle);
            backButton = new TextButton("  Back  ", BaseGame.textButtonStyle );
            ip = new Label("IP adress: ", BaseGame.labelStyle);
            waiting = new Label(" WAITING FOR THE PLAYER ", BaseGame.labelStyle);
        }

        String command = null;
        if(System.getProperty("os.name").equals("Linux"))
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
        Scanner s = new Scanner(p.getInputStream());

        StringBuilder sb = new StringBuilder("");
        while(s.hasNext())
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

        ipAdress = new TextField(ipS ,BaseGame.textFieldStyle);
        nick = new Label("Nick: ", BaseGame.labelStyle);
        nickText = new TextField(PongGameBeta.nick, BaseGame.textFieldStyle);


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
                    if(!(e instanceof InputEvent) ||
                            !((InputEvent) e ).getType().equals(InputEvent.Type.touchDown) )
                        return false;
                    if(flagActiveNickText.get()) {
                        nickText.setText("");
                    }
                    return true;

                }
        );
        AtomicBoolean flagActiveIpAdress = new AtomicBoolean(true);
        ipAdress.addListener(
                e -> {
                    if(!(e instanceof InputEvent) ||
                            !((InputEvent) e ).getType().equals(InputEvent.Type.touchDown) )
                        return false;
                    if(flagActiveIpAdress.get()) {
                        ipAdress.setText("");
                    }
                    return true;
                }
        );

        createButton.addListener(
                e -> {
                    if(!(e instanceof InputEvent) ||
                            !((InputEvent) e ).getType().equals(InputEvent.Type.touchDown) )
                        return false;

                    PongGameBeta.nick = nickText.getText();

                    waiting.setPosition(mainStage.getWidth()/2 - 200, mainStage.getHeight()/2);
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
                    if(!(e instanceof InputEvent) ||
                            !((InputEvent) e ).getType().equals(InputEvent.Type.touchDown) )
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
                    if(!(e instanceof InputEvent) ||
                            !((InputEvent) e ).getType().equals(InputEvent.Type.touchDown) )
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
