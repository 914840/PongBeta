package game.pong.beta.components;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryonet.Client;
import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Listener;
import game.pong.beta.BaseActor;
import game.pong.beta.BaseGame;
import game.pong.beta.BaseScreen;
import game.pong.beta.PongGameBeta;

import java.io.IOException;

public class MultiScreenClient extends BaseScreen {

    MultiScreenClient(String ipHost){
        this.ipHost = ipHost;
    }

    private BaseActor background;
    private Paddle paddle1, paddle2;
    private Ball ball;
    private BaseActor borderUp, borderDown,endGameBorderLeft, endGameBorderRight, winMessage, gameOverMessage;
    private Label scoreLabel, spaceLabel;

    private Label waiting;
    private Label readyClient;
    private Label readyServer;
    private String ready2;
    public boolean isServer; // zamienna publicza ze wzgledu na bardzo duze zagnieżdzenie.

    private Client client;

    private SomeRequest request;
    private SomeResponse response;
    private PaddleDirection direction;
    private Ballposition ballposition;

    private String ipHost;
    private int tcpPort = 54345;
    private int udpPort = 54789;

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
            waiting = new Label( " OCZEKIWANIE NA SERVER ", BaseGame.labelStyle);
            readyClient = new Label( "GOTOWY?  NACISNIJ SPACJE", BaseGame.labelStyle);
            readyServer = new Label( "GOTOWY?  NACISNIJ SPACJE", BaseGame.labelStyle);
            ready2 = "GOTOWY!";
        }
        else
        {
            waiting = new Label(" WAITING FOR THE SERVER ", BaseGame.labelStyle);
            readyClient = new Label( "READY?  PRESS SPACE", BaseGame.labelStyle);
            readyServer = new Label( "READY?  PRESS SPACE", BaseGame.labelStyle);
            readyServer = new Label( "READY?  PRESS SPACE", BaseGame.labelStyle);
            ready2 = "READY!";
        }

        waiting.setPosition(mainStage.getWidth()/2 - 200, mainStage.getHeight()/2);
        readyClient.setPosition(((mainStage.getWidth()/4)*3) - 100, mainStage.getHeight()/2);
        readyClient.setVisible(false);
        readyServer.setPosition(mainStage.getWidth()/4 - 100, mainStage.getHeight()/2);
        readyServer.setVisible(false);


        // creating a paddle 1(player) & 2(cpu)
        paddle1 = new Paddle(30, (mainStage.getHeight() / 2) - 100, mainStage, new Player(" ", false,true));
        paddle2 = new Paddle( (mainStage.getWidth() - 60), (mainStage.getHeight()/2)-100, mainStage, new Player(PongGameBeta.nick));

        ball = new Ball((mainStage.getWidth()/2)-16, (mainStage.getHeight()/2)-16,mainStage);

            client = new Client();
            client.start();
            mainStage.addActor(waiting);
            mainStage.addActor(readyClient);
            mainStage.addActor(readyServer);
            try {
                client.connect(5000, PongGameBeta.ipHost, 54345, 54789);
            } catch (IOException e) {
                e.printStackTrace();
            }
            Kryo kryo = client.getKryo();
            kryo.register(SomeRequest.class);
            kryo.register(SomeResponse.class);
            kryo.register(PaddleDirection.class);
            kryo.register(Ballposition.class);


            // Nawiązanie z serverem podstawowej łączności
            request = new SomeRequest();
            request.text = "Here is the request";
            client.sendTCP(request);
            request.text = PongGameBeta.nick;
            client.sendTCP(request);
            request.text = PongGameBeta.gameLanguage;

            client.addListener(new Listener() {
                public void received (Connection connection, Object object) {
                    if (object instanceof SomeResponse) {
                        SomeResponse response = (SomeResponse)object;
                        System.out.println(response.text);
                        if(response.text.equals("CONNECT")) {
                            waiting.setVisible(false);
                            readyClient.setVisible(true);
                            readyServer.setVisible(true);
                        }
                        else if(response.text.equals("READY")){
                            readyServer.setText(ready2);
                        }
                    }
                    if (object instanceof PaddleDirection) {
                        PaddleDirection direction = (PaddleDirection) object;
                        paddle1.accelerateWithoutRotation(direction.y);
                    }
                    if  (object instanceof Ballposition) {
                        Ballposition ballposition = (Ballposition) object;
                        ball.setPosition(ballposition.x,ballposition.y);
                    }
                }
            });


    }
    @Override
    public void update(float dt) {


        direction = new PaddleDirection();
        if (Gdx.input.isKeyPressed(Input.Keys.UP)){
            direction.y = 1;
            client.sendTCP(direction);
        }
        else if(Gdx.input.isKeyPressed(Input.Keys.DOWN)){
            direction.y = -1;
            client.sendTCP(direction);
        }

        if ((Gdx.input.isKeyPressed(Input.Keys.SPACE)) && flag != 1 ){
            readyClient.setText(ready2);
            request.text = "READY";
            client.sendTCP(request);
            flag = 1;
        }





        // powrót do menu, przerwanie gry.
        if(Gdx.input.isKeyPressed(Input.Keys.ESCAPE))
        {
            PongGameBeta.setActiveScreen( new MenuScreen());

            if(!isServer){
                client.close();
            }
        }
    }

    public static class SomeRequest {
        public String text;
    }
    public static class SomeResponse {
        public String text;
    }
    public static class Ballposition {
        public float x,y;
    }
    public static class PaddleDirection {
        public int y;
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

    public void showStartLabel() {
        if (PongGameBeta.gameLanguage.equals("PL")) {
            spaceLabel = new Label(" NACISNIJ SPACJE ABY ZACZAC ", BaseGame.labelStyle);
        } else {
            spaceLabel = new Label(" PRESS  SPACE  TO  START  ", BaseGame.labelStyle);
        }
        spaceLabel.setPosition(((mainStage.getWidth() / 4)*3) - 100, mainStage.getHeight() / 2);
        spaceLabel.setVisible(false);

        uiStage.addActor(spaceLabel);
    }


    public void upDateStartLabel()
    {
        if(PongGameBeta.gameLanguage.equals("PL"))
        {
            if(flag == 0)
            {
                spaceLabel.setText(" NACISNIJ SPACJE ABY ZACZAC ");
            }
            else if (flag == 2)
            {
                spaceLabel.setText(" PUNKT SETOWY! ");
            }
            else if( flag == 9)
            {
                spaceLabel.setText(" PUNKT MECZOWY !!! ");
            }
            else if( flag == 99)
            {
                spaceLabel.setText(" KONIEC GRY ");
            }
        }
        else if(PongGameBeta.gameLanguage.equals("EN") )
        {
            if(flag == 0)
            {
                spaceLabel.setText(" PRESS  SPACE  TO  START  ");
            }
            else if (flag == 2)
            {
                spaceLabel.setText(" SET POINT! ");
            }
            else if( flag == 9)
            {
                spaceLabel.setText(" MATCH POINT !!! ");
            }
            else if( flag == 99)
            {
                spaceLabel.setText(" END GAME ");
            }
        }
    }
}
