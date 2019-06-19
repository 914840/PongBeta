package game.pong.beta.components;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.esotericsoftware.kryonet.Client;
import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Listener;
import com.esotericsoftware.kryonet.Server;
import game.pong.beta.BaseActor;
import game.pong.beta.BaseGame;
import game.pong.beta.BaseScreen;
import game.pong.beta.PongGameBeta;

import java.io.IOException;

public class MultiScreen extends BaseScreen {

    private BaseActor background;
    private Paddle paddle1, paddle2;
    private Ball ball;
    private BaseActor borderUp, borderDown,endGameBorderLeft, endGameBorderRight, winMessage, gameOverMessage;
    private Label scoreLabel, spaceLabel;

    private Label waiting;
    public boolean isServer; // zamienna publicza ze wzgledu na bardzo duze zagnieżdzenie.
    private int tcpPort = 54345, udpPort = 54789;
    private String ipHost;

    private Server server;
    private Client client;

    /**
     * Flag codes: 0 - start game, 1 - game on, 2 - set point , 5 - new set, 9 - match point, 99 - GameOver
     */
    private int flag = 0;


    @Override
    public void initialize() {

        // method to initiate background
        background = setBackground();


        // method to set not-solid border of screen
        BaseActor.setWorldBounds(background);

        // creating a solid upper border
        borderUp = new BaseActor(0, (mainStage.getHeight() - 10), mainStage);
        borderUp.loadTexture("border1600x10.png");

        // creating a solid lower border
        borderDown = new BaseActor(0, 0, mainStage);
        borderDown.loadTexture("border1600x10.png");

        // creating a solid left border
        endGameBorderLeft = new BaseActor(0, 0, mainStage);
        endGameBorderLeft.loadTexture("border2x900endGame.png");

        // creating a solid right border
        endGameBorderRight = new BaseActor(mainStage.getWidth() - 2, 0, mainStage);
        endGameBorderRight.loadTexture("border2x900endGame.png");
        if(PongGameBeta.gameLanguage.equals("PL"))
        {
            waiting = new Label( " OCZEKIWANIE NA GRACZA ", BaseGame.labelStyle);
        }
        else
        {
            waiting = new Label(" WAITING FOR THE PLAYER ", BaseGame.labelStyle);
        }

        waiting.setPosition(mainStage.getWidth()/2 - 200, mainStage.getHeight()/2);


        // creating a paddle 1(player) & 2(cpu)
        paddle1 = new Paddle(30, (mainStage.getHeight() / 2) - 100, mainStage, new Player(PongGameBeta.nick));
        paddle2 = new Paddle( (mainStage.getWidth() - 60), (mainStage.getHeight()/2)-100, mainStage, new Player("CPU", false, true));

        //isServer = true; // dlaczego bez tego nie działa server ?!?!
        if(getIsServer() == true) {
            server = new Server();
            server.start();
            mainStage.addActor(waiting);
            try {
                server.bind(tcpPort, udpPort);
            } catch (IOException e) {
                e.printStackTrace();
            }

            server.addListener(new Listener() {
                public void received(Connection connection, Object object) {
                    if (object instanceof SomeRequest) {
                        SomeRequest request = (SomeRequest) object;
                        System.out.println(request.text);

                        SomeResponse response = new SomeResponse();
                        response.text = "Thanks";
                        connection.sendTCP(response);
                    }
                }
            });
        }
        if(getIsServer()== false){
            client = new Client();
            client.start();
            try {
                client.connect(5000, ipHost, tcpPort, udpPort);
            } catch (IOException e) {
                e.printStackTrace();
            }
            SomeRequest request = new SomeRequest();
            request.text = "Here is the request";
            client.sendTCP(request);
        }
    }
    @Override
    public void update(float dt) {





        // powrót do menu, przerwanie gry.
        if(Gdx.input.isKeyPressed(Input.Keys.ESCAPE))
        {
            PongGameBeta.setActiveScreen( new MenuScreen());
            if(isServer){
                server.close();
            }
            else if(!isServer){
                client.close();
            }
        }
    }

    public class SomeRequest {
        public String text;
    }
    public class SomeResponse {
        public String text;
    }
    public void setIsServer(boolean isServer){
        this.isServer = isServer;
    }
    public void setTcpPort(int tcpPort){
        this.tcpPort = tcpPort;
    }
    public void setUdpPort(int udpPort){
        this.udpPort = udpPort;
    }
    public void setIpHost(String ipHost){
        this.ipHost = ipHost;
    }

    public boolean getIsServer(){
        return this.isServer;
    }
}
