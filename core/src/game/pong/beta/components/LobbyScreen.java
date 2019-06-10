package game.pong.beta.components;

import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;
import game.pong.beta.BaseActor;
import game.pong.beta.BaseGame;
import game.pong.beta.BaseScreen;
import game.pong.beta.PongGameBeta;
import game.pong.beta.UDP.UDPClient;
import game.pong.beta.network.ServerSocketPong;
import game.pong.beta.UDP.UDPServer;
import java.net.*;

public class LobbyScreen extends BaseScreen {

    private BaseActor background;
    private BaseActor title;

    protected Table uiTable;

    private TextButton joinButton, createButton, backButton;
    private TextField ipAdress, nickText;
    private Label ip, nick, rules;

    private ServerSocketPong serverSocketPong;
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
        }
        else
        {
            joinButton = new TextButton("     Join     ", BaseGame.textButtonStyle);
            createButton = new TextButton("    Create    ", BaseGame.textButtonStyle);
            backButton = new TextButton("  Back  ", BaseGame.textButtonStyle );
            ip = new Label("IP adress: ", BaseGame.labelStyle);
        }

        try {
            IP=InetAddress.getLocalHost();
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }
        ipAdress = new TextField(IP.toString().substring(3),BaseGame.textFieldStyle);
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


        nickText.addListener(
                e -> {
                    if(!(e instanceof InputEvent) ||
                            !((InputEvent) e ).getType().equals(InputEvent.Type.touchDown) )
                        return false;

                    nickText.setText("");
                    return true;

                }
        );

        createButton.addListener(
                e -> {
                    if(!(e instanceof InputEvent) ||
                            !((InputEvent) e ).getType().equals(InputEvent.Type.touchDown) )
                        return false;

                    PongGameBeta.nick = nickText.getText();
                    // TODO reszta metody
                    //*****************************
                    // tworzy gniazdo servera
//                    serverSocketPong = new ServerSocketPong();

                    try {
                        UDPServer server = new UDPServer(IP.toString(), 8100);
                    } catch (SocketException ex) {
                        ex.printStackTrace();
                    }


                    return true;
                }
        );

        joinButton.addListener(
                e -> {
                    if(!(e instanceof InputEvent) ||
                            !((InputEvent) e ).getType().equals(InputEvent.Type.touchDown) )
                        return false;

                    PongGameBeta.nick = nickText.getText();
                    UDPClient client = new UDPClient(ipAdress.getText(), 8100);
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
