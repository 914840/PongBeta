package game.pong.beta.components;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryonet.Client;
import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.KryoNetException;
import com.esotericsoftware.kryonet.Listener;
import game.pong.beta.BaseActor;
import game.pong.beta.BaseGame;
import game.pong.beta.BaseScreen;
import game.pong.beta.PongGameBeta;

import java.io.IOException;

import static game.pong.beta.PongGameBeta.*;

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

    private Client client;

    private SomeRequest request;
    private SomeResponse response;
    private PaddleDirection direction;
    private BallPosition ballPosition;
    private ScoreBoard scoreBoard;
    private String score;
    private PaddlePosition paddlePosition;
    private StartLable startLable;



    private String ipHost;
    private int tcpPort = 54345;
    private int udpPort = 54789;

    /**
     * Flag codes: -1 - start game, 0 - game ready, 1 - game on, 2 - set point , 5 - new set, 9 - match point, 99 - GameOver
     */
    private int flag = -1;
    private boolean isVisible = false;
    private boolean back;
    private boolean isServerReady = false;
    private boolean isClientReady = false;
    private boolean isClientServe = false;
    private boolean isReadySend = false;


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
        if(gameLanguage.equals("PL"))
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
        paddle2 = new Paddle( (mainStage.getWidth() - 60), (mainStage.getHeight()/2)-100, mainStage, new Player(nick));

        ball = new Ball((mainStage.getWidth()/2)-16, (mainStage.getHeight()/2)-16,mainStage);
        try {
            client = new Client();
            client.start();
        }
        catch (KryoNetException e){
            setActiveScreen(new MenuScreen());
        }
            mainStage.addActor(waiting);
            mainStage.addActor(readyClient);
            mainStage.addActor(readyServer);
            try {
                client.connect(5000, PongGameBeta.ipHost, 54345, 54789);
            } catch (KryoNetException e) {
                System.out.println("Exception: KryoNet in MultiClient");
            } catch (IOException en){
                //setActiveScreen(new MenuScreen());
                System.out.println("Exception: IOException in MultiClient");
                back = true;
                //setActiveScreen(new MenuScreen());

            }

            Kryo kryo = client.getKryo();
            kryo.register(SomeRequest.class);
            kryo.register(SomeResponse.class);
            kryo.register(PaddleDirection.class);
            kryo.register(BallPosition.class);
            kryo.register(FlagStatus.class);
            kryo.register(ScoreBoard.class);
            kryo.register(PaddlePosition.class);
            kryo.register(StartLable.class);


            // Nawiązanie z serverem podstawowej łączności
            request = new SomeRequest();
            request.text = "INIT";
            client.sendTCP(request);
            request.text = "NICK" + nick;
            client.sendTCP(request);
            request.text = gameLanguage;

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
                            isServerReady = true;
                            if (isClientReady){
                                flag = 0;
                            }
                        }
                        else if(response.text.equals("YOU SERVE")){
                            startLable.isVisibile = true;
                            isClientServe = true;
                        }

                        else if(response.text.equals("CLOSE")){
                            client.close();
                            back = true;
                            //PongGameBeta.setActiveScreen(new MenuScreen());
                        }
                    }
                    if (object instanceof PaddleDirection) {
                        PaddleDirection direction = (PaddleDirection) object;
                        paddle1.accelerateWithoutRotation(direction.y);
                    }
                    if  (object instanceof BallPosition) {
                        BallPosition ballPosition = (BallPosition) object;
                        ball.setPosition(ballPosition.x,ballPosition.y);
                    }
                    if  (object instanceof FlagStatus) {
                        FlagStatus flagStatus = (FlagStatus) object;
                        flag = flagStatus.flag;
                        if(flagStatus.flag == 1){
                            spaceLabel.setText("");
                            readyClient.setText("");
                            readyServer.setText("");
                        }
                        if(flagStatus.flag == 2){
                            upDateStartLabel();
                        }
                    }
                    if  (object instanceof  ScoreBoard) {
                        ScoreBoard scoreBoard = (ScoreBoard) object;
                        score = scoreBoard.scoreBoard;
                        upDateScoreBoard();
                    }
                    if  (object instanceof  StartLable) {
                        StartLable startLable = (StartLable) object;
                        isVisible = startLable.isVisibile;

                    }
                }
            });

            showScoreboard();
            showStartLabel();

    }
    @Override
    public void update(float dt) {
        if(back){
            client.close();
            PongGameBeta.setActiveScreen(new MenuScreen());
        }

        if(flag == -1 && isClientReady){
            readyClient.setText(ready2);
        }
        if(flag == -1 && isServerReady) {
            readyServer.setText(ready2);
        }
        if(isServerReady && isClientReady){
            readyServer.setText("");
            readyClient.setText("");
            upDateStartLabel();
        }

        direction = new PaddleDirection();
        if (Gdx.input.isKeyPressed(Input.Keys.UP)){

            direction.y = 1;
            client.sendTCP(direction);
        }
        else if(Gdx.input.isKeyPressed(Input.Keys.DOWN)){

            direction.y = -1;
            client.sendTCP(direction);
        }
        else if ((Gdx.input.isKeyPressed(Input.Keys.SPACE)) && flag == -1 && !isReadySend) {
            readyClient.setText(ready2);
            request.text = "READY";
            client.sendTCP(request);
            isReadySend = true;
            isClientReady = true;

        }
        else if ((Gdx.input.isKeyPressed(Input.Keys.SPACE)) && (flag == 0 || flag == 2 || flag == 9 )){
            readyClient.setText("");
            spaceLabel.setText("");
            readyServer.setText("");
            request.text = "PLAY";
            client.sendTCP(request);
            isClientServe = false;

            flag = 1;
        }



        // powrót do menu, przerwanie gry.
        if(Gdx.input.isKeyPressed(Input.Keys.ESCAPE))
        {
            request.text="EXIT";
            client.sendTCP(request);
            client.close();
            setActiveScreen( new MenuScreen());


        }

        if(isVisible){
            upDateStartLabel();
        }
    }

    public static class SomeRequest {
        public String text;
    }
    public static class SomeResponse {
        public String text;
    }
    public static class BallPosition {
        public float x,y;
    }
    public static class PaddleDirection {
        public int y;
    }
    public static class FlagStatus {
        public int flag;
    }
    public static class ScoreBoard{
        public String scoreBoard;
    }
    public static class PaddlePosition {
        public float y;
    }
    public static class StartLable{
        public boolean isVisibile;
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
        if (gameLanguage.equals("PL")) {
            spaceLabel = new Label(" NACISNIJ SPACJE ABY ZACZAC ", BaseGame.labelStyle);
        } else {
            spaceLabel = new Label(" PRESS  SPACE  TO  START  ", BaseGame.labelStyle);
        }
        spaceLabel.setPosition(((mainStage.getWidth() / 4)*3) - 100, mainStage.getHeight() / 2);
        spaceLabel.setVisible(false);

        uiStage.addActor(spaceLabel);
    }

    public void showScoreboard() {
        scoreLabel = new Label("", BaseGame.labelStyle);
        scoreLabel.setPosition((mainStage.getWidth()/2) - 240, mainStage.getHeight() - 50 );

        scoreLabel.setText("                    " +
                paddle1.getPlayer().getScore().getPoints() +
                " / "+ PongGameBeta.points + "        " +
                paddle1.getPlayer().getScore().getSets() +
                "   " +
                "(" + PongGameBeta.sets + ")" +
                "   " +
                paddle2.getPlayer().getScore().getSets() +
                "          " +
                paddle2.getPlayer().getScore().getPoints() + " / " + PongGameBeta.points
        );
        uiStage.addActor(scoreLabel);

    }
    public void upDateScoreBoard(){
        scoreLabel.setText(score);
    }


    public void upDateStartLabel()
    {
        spaceLabel.setVisible(true);
        if(gameLanguage.equals("PL"))
        {
            if(flag == 0 && isClientServe)
            {
                spaceLabel.setText(" NACISNIJ SPACJE ABY ZACZAC ");
                readyServer.setText("");
                readyClient.setText("");
            }
            else if (flag == 2 && !isClientServe)
            {
                spaceLabel.setText(" PUNKT SETOWY! ");
                readyServer.setText("");
                readyClient.setText("");
            }
            else if (flag == 2 && isClientServe)
            {
                spaceLabel.setText(" NACISNIJ SPACJE ABY ZACZAC - PUNKT SETOWY!  ");
                readyServer.setText("");
                readyClient.setText("");
            }
            else if( flag == 9 && !isClientServe)
            {
                spaceLabel.setText(" PUNKT MECZOWY !!! ");
                readyServer.setText("");
                readyClient.setText("");
            }
            else if( flag == 9 && isClientServe)
            {
                spaceLabel.setText(" NACISNIJ SPACJE ABY ZACZAC - PUNKT MECZOWY !!! ");
                readyServer.setText("");
                readyClient.setText("");
            }
            else if( flag == 99 && !isClientServe)
            {
                spaceLabel.setText(" KONIEC GRY - WYGRALES");
                readyServer.setText("");
                readyClient.setText("");
            }
        }
        else if(gameLanguage.equals("EN") )
        {
            if(flag == 0 && isClientServe)
            {
                spaceLabel.setText(" PRESS  SPACE  TO  START  ");
                readyServer.setText("");
                readyClient.setText("");
            }
            else if (flag == 2 && isClientServe==false)
            {
                spaceLabel.setText(" SET POINT! ");
                readyServer.setText("");
                readyClient.setText("");
            }
            else if (flag == 2 && isClientServe)
            {
                spaceLabel.setText(" PRESS  SPACE  TO  START - SET POINT");
                readyServer.setText("");
                readyClient.setText("");
            }
            else if( flag == 9 && !isClientServe)
            {
                spaceLabel.setText(" MATCH POINT !!! ");
                readyServer.setText("");
                readyClient.setText("");
            }
            else if( flag == 9 && isClientServe)
            {
                spaceLabel.setText(" PRESS  SPACE  TO  START - MATCH POINT !!!  ");
                readyServer.setText("");
                readyClient.setText("");
            }
            else if( flag == 99 && !isClientServe)
            {
                spaceLabel.setText(" END GAME - YOU WIN");
                readyServer.setText("");
                readyClient.setText("");
            }
        }
        else if(!isClientServe){
            spaceLabel.setText("");
        }
    }
}
