package game.pong.beta.components;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Listener;
import com.esotericsoftware.kryonet.Server;
import game.pong.beta.BaseActor;
import game.pong.beta.BaseGame;
import game.pong.beta.BaseScreen;
import game.pong.beta.PongGameBeta;

import java.io.IOException;
import java.net.BindException;

import static game.pong.beta.PongGameBeta.gameLanguage;

public class MultiScreenServer extends BaseScreen {

    private BaseActor background;
    private Paddle paddle1, paddle2;
    private Ball ball;
    private BaseActor borderUp, borderDown,endGameBorderLeft, endGameBorderRight, winMessage, gameOverMessage;
    private Label scoreLabel, spaceLabel;
    private String scoreLabelString;

    private Label waiting;
    private Label readyClient;
    private Label readyServer;
    private String ready2;
    private String exit;
    private String restart;
    private int tcpPort = 54345, udpPort = 54789;
    private String ipHost;

    private Server server;


    private SomeRequest request;
    private SomeResponse response;
    private PaddleDirection direction;
    private BallPosition ballPosition = new BallPosition();
    private FlagStatus flagStatus;
    private ScoreBoard scoreBoard;
    private PaddlePosition paddlePosition;
    private StartLable startLable;

    /**
     * Flag codes: -1 - start game, 0 - game ready, 1 - game on, 2 - set point , 5 - new set, 9 - match point, 99 - GameOver
     */
    private int flag = -1;
    private boolean isClientReady = false;
    private boolean isPlayerConnected = false;
    private boolean reConnection = false;
    private boolean serverServe = true;
    private boolean isServerReady= false;
    private boolean isReadySend = false;

    private boolean isFirstBall = true;


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
            readyClient = new Label( "GOTOWY?  NACISNIJ SPACJE", BaseGame.labelStyle);
            readyServer = new Label( "GOTOWY?  NACISNIJ SPACJE", BaseGame.labelStyle);
            ready2 = "GOTOWY!";
            exit = "Gracz wyszedl z gry";
            restart = " NACISNIJ SPACJE ABY ZRESETOWAC SERVER LUB ESCAPE ABY WYJSC DO MENU ";
        }
        else
        {
            waiting = new Label(" WAITING FOR THE PLAYER ", BaseGame.labelStyle);
            readyClient = new Label( "READY?  PRESS SPACE", BaseGame.labelStyle);
            readyServer = new Label( "READY?  PRESS SPACE", BaseGame.labelStyle);
            ready2 = "READY!";
            exit = "The Player left the game";
            restart = " PRESS SPACE TO RESTART SERVER OR ESCAPE TO EXIT TO THE MENU ";
        }

        waiting.setPosition(mainStage.getWidth()/2 - 200, mainStage.getHeight()/2);
        readyClient.setPosition(((mainStage.getWidth()/4)*3) - 100, mainStage.getHeight()/2);
        readyClient.setVisible(false);
        readyServer.setPosition(mainStage.getWidth()/4 - 100, mainStage.getHeight()/2);
        readyServer.setVisible(false);

        // creating a paddle 1(player) & 2(cpu)
        paddle1 = new Paddle(30, (mainStage.getHeight() / 2) - 100, mainStage, new Player(PongGameBeta.nick));
        paddle2 = new Paddle( (mainStage.getWidth() - 60), (mainStage.getHeight()/2)-100, mainStage, new Player("CPU", false, true));

        ball = new Ball((mainStage.getWidth()/2)-16, (mainStage.getHeight()/2)-16,mainStage);
        ball.setMotionAngle(35);

            server = new Server();
            server.start();
            mainStage.addActor(waiting);
            mainStage.addActor(readyClient);
            mainStage.addActor(readyServer);
            try {
                server.bind(54345, 54789);
            } catch (BindException e1) {

                System.out.println("Exception: Bind exception in MultiScreanServer.init");
            } catch (IOException e){

            }


            Kryo kryo = server.getKryo();
            kryo.register(SomeRequest.class);
            kryo.register(SomeResponse.class);
            kryo.register(PaddleDirection.class);
            kryo.register(BallPosition.class);
            kryo.register(FlagStatus.class);
            kryo.register(ScoreBoard.class);
            kryo.register(PaddlePosition.class);
            kryo.register(StartLable.class);

            server.addListener(new Listener() {
                public void received(Connection connection, Object object) {
                    if (object instanceof SomeRequest) {
                        SomeRequest request = (SomeRequest) object;
                        System.out.println(request.text);
                        if(request.text.equals("INIT")) {
                            response = new SomeResponse();
                            response.text = "CONNECT";
                            connection.sendTCP(response);
                            waiting.setVisible(false);
                            readyClient.setVisible(true);
                            readyServer.setVisible(true);
                            isPlayerConnected = true;
                        }
                        else if(request.text.startsWith("NICK:")) {
                            paddle2.getPlayer().setNick(request.text.substring(5));
                        }
                        else if(request.text.equals("READY")) {
                            //readyClient.setText(ready2);
                            isClientReady = true;
                            if(isServerReady){
                                flag = 0;
                            }
                        }
                        else if(request.text.equals("PLAY")) {
                            if( !serverServe && isServerReady){
                                ball.setSpeed(900);
                                flag = 1;
                                readyClient.setVisible(false);
                                readyServer.setVisible(false);
                            }
                            else if(serverServe && isServerReady){
                                readyClient.setVisible(false);
                            }
                            else if(!serverServe && !isServerReady){

                            }
                        }
                        else if(request.text.equals("EXIT")) {
                            readyClient.setText(exit);
                            readyClient.setVisible(true);
                            paddle1.getPlayer().getScore().setPoints(0);
                            paddle1.getPlayer().getScore().setSets(0);
                            paddle2.getPlayer().getScore().setPoints(0);
                            paddle2.getPlayer().getScore().setSets(0);
                            upDateScoreboard();
                            ball.setSpeed(0);
                            ball.setVisible(false);
                            spaceLabel.setVisible(true);
                            spaceLabel.setPosition(spaceLabel.getX(),spaceLabel.getY()- 150);
                            spaceLabel.setText(restart);
                            reConnection = true;
                        }
                    }
                    if  (object instanceof PaddleDirection) {
                        PaddleDirection direction = (PaddleDirection) object;
                        paddle2.accelerateWithoutRotation(direction.y);
                    }
                    if  (object instanceof BallPosition) {      // raczej server nie przyjmuje wartości pozycji piłki a ją wysyła
                        BallPosition ballposition = (BallPosition) object;
                        ball.setPosition(ballposition.x,ballposition.y);
                    }
                    if  (object instanceof PaddlePosition){
                        PaddlePosition paddlePosition = (PaddlePosition) object;
                        paddle2.setPosition(paddle2.getX(), paddlePosition.y);
                    }
                }
            });

            showScoreboard();
            showStartLabel();
            upDateScoreboard();
            ScoreBoard scoreBoard = new ScoreBoard();
            scoreBoard.scoreBoard = scoreLabelString;
            server.sendToAllTCP(scoreBoard);

    }
    @Override
    public void update(float dt) {
        if(flag == -1 && isClientReady){
            readyClient.setText(ready2);
        }
        if(flag == -1 && isServerReady) {
            readyServer.setText(ready2);
        }
        if(isServerReady && isClientReady && isFirstBall){
            readyServer.setVisible(false);
            readyClient.setVisible(false);
            upDateStartLabel();
            resetStartLocationLevelScreen(1);
            isFirstBall = false;
            flag = 0;
        }


        ballPosition.x = ball.getX();
        ballPosition.y = ball.getY();
        server.sendToAllTCP(ballPosition);


        direction = new PaddleDirection();
        if (Gdx.input.isKeyPressed(Input.Keys.UP)){
            direction.y = 1;
            server.sendToAllTCP(direction);
        }
        else if(Gdx.input.isKeyPressed(Input.Keys.DOWN)){
            direction.y = -1;
            server.sendToAllTCP(direction);
        }



        // powrót do menu, przerwanie gry.
        if(Gdx.input.isKeyPressed(Input.Keys.ESCAPE))
        {
            if(isPlayerConnected == true) {
                response.text = "CLOSE";
                server.sendToAllTCP(response);
            }
            server.close();
            PongGameBeta.setActiveScreen( new MenuScreen());


        }
        else if ((Gdx.input.isKeyPressed(Input.Keys.SPACE)) && flag == -1 && !isReadySend && isPlayerConnected){
            readyServer.setText(ready2);  // ready

            response.text = "READY";
            server.sendToAllTCP(response);// wysyła komunikat READT

            isServerReady = true;
            isReadySend = true;

            if(isClientReady && isFirstBall){
                readyServer.setVisible(false);
                readyClient.setVisible(false);
                upDateStartLabel();
                resetStartLocationLevelScreen(1);
                isFirstBall = false;
                flag = 0;
            }

        }
        else if ((Gdx.input.isKeyPressed(Input.Keys.SPACE)) &&  isClientReady && isServerReady && serverServe && (flag == 0 || flag == 2 || flag == 9)) {
            ball.setSpeed(900);
            serverServe = false;
            readyServer.setText("");
            readyClient.setText("");

            flag = 1;
            flagStatus = new FlagStatus();
            flagStatus.flag = 1;
            server.sendToAllTCP(flagStatus);

        }
        else if(((Gdx.input.isKeyPressed(Input.Keys.SPACE)) && reConnection == true )){
            server.close();
            System.out.println("Server restart");
            PongGameBeta.setActiveScreen(new MultiScreenServer());
        }

        /**
         *  bounced ball from paddle1  - Player-Server
         */
        if (ball.overlaps(paddle1))
        {
            if (paddle1.isMoving())
            {
                if((paddle1.getY() + 25) < (ball.getY()-32) || paddle1.getY()+ 175 > (ball.getY() +32))
                {
                    float a = 180 - ball.getMotionAngle() + MathUtils.random(45, 60);
                    if (a > 80 && a < 280 )
                    {
                        a = MathUtils.random(50, 75);
                    }
                    ball.setMotionAngle(a);
                }
                else if((paddle1.getY() + 60) < (ball.getY()-32) ||  paddle1.getY()+ 140 > (ball.getY() +32))
                {
                    ball.setMotionAngle(180 - ball.getMotionAngle() + MathUtils.random(30, 50));
                }
                else if((paddle1.getY() + 60) > (ball.getY()-32) &&  paddle1.getY()+ 140 < (ball.getY() +32))
                {
                    ball.setMotionAngle(180 - ball.getMotionAngle() + MathUtils.random(10, 30));
                }
            }
            else if (!paddle1.isMoving())
            {
                ball.setMotionAngle(180 - ball.getMotionAngle());
            }
        }

        /**
         *  bounced ball from paddle2  - Player - Online
         */
        if (ball.overlaps(paddle2))
        {
            if (paddle2.isMoving())
            {
                if((paddle1.getY() + 25) < (ball.getY()-32) || paddle1.getY()+ 175 > (ball.getY() +32))
                {
                    float a = 180 - ball.getMotionAngle() + MathUtils.random(45, 60);
                    if (a > 260 && a < 100 )
                    {
                        a = MathUtils.random(120, 160);
                    }
                    ball.setMotionAngle(a);
                }
                else if((paddle1.getY() + 60) < (ball.getY()-32) ||  paddle1.getY()+ 140 > (ball.getY() +32))
                {
                    ball.setMotionAngle(180 - ball.getMotionAngle() + MathUtils.random(30, 50));
                }
                else if((paddle1.getY() + 60) > (ball.getY()-32) &&  paddle1.getY()+ 140 < (ball.getY() +32))
                {
                    ball.setMotionAngle(180 - ball.getMotionAngle() + MathUtils.random(10, 30));
                }
            }
            else if (!paddle2.isMoving())
            {
                ball.setMotionAngle(180 - ball.getMotionAngle());
            }
        }


        /**
         *  bounced ball from UP border
         */
        if (ball.overlaps(borderUp))
        {

            float angle = (180 - ball.getMotionAngle()) + 180;
            if (angle > 350){
                ball.setMotionAngle(angle - 15);
            }
            else if( angle < 190 ){
                ball.setMotionAngle(angle + 15);
            }
            else {
                ball.setMotionAngle(angle);
            }
        }

        /**
         *  bounced ball from DOWN border
         */
        if (ball.overlaps(borderDown))
        {
            float angle = (180 - ball.getMotionAngle()) + 180;
            if (angle < 10){
                ball.setMotionAngle(angle + 15);
            }
            else if( angle > 170 ){
                ball.setMotionAngle(angle - 15);
            }
            else {
                ball.setMotionAngle(angle);
            }
        }

        /**
         *  ball cross left border
         */
        if (ball.overlaps(endGameBorderLeft))
        {

            flag = paddle2.getPlayer().getScore().addOnePoint();

            serverServe = true;

            upDateScoreboard();



            ScoreBoard scoreBoard = new ScoreBoard();
            scoreBoard.scoreBoard = scoreLabelString;
            server.sendToAllTCP(scoreBoard);

            FlagStatus flagStatus = new FlagStatus();
            flagStatus.flag = flag;
            server.sendToAllTCP(flagStatus);
            resetStartLocationLevelScreen(0);
            upDateStartLabel();

        }

        /**
         *  ball cross right border
         */
        if (ball.overlaps(endGameBorderRight))
        {
            flag = paddle1.getPlayer().getScore().addOnePoint();

            serverServe = false;
            response = new SomeResponse();
            response.text = "YOU SERVE";
            server.sendToAllTCP(response);

            upDateScoreboard();

            ScoreBoard scoreBoard = new ScoreBoard();
            scoreBoard.scoreBoard = scoreLabelString;
            server.sendToAllTCP(scoreBoard);

            FlagStatus flagStatus = new FlagStatus();
            flagStatus.flag = flag;
            server.sendToAllTCP(flagStatus);

            resetStartLocationLevelScreen(1); // punkt dla Player 1
            //upDateStartLabel();
            startLable = new StartLable();
            startLable.isVisibile = true;
            server.sendToAllTCP(startLable);


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


    // TODO zostawidc
    public void showScoreboard() {
        scoreLabel = new Label("", BaseGame.labelStyle);
        scoreLabel.setPosition((mainStage.getWidth()/2) - 240, mainStage.getHeight() - 50 );

        uiStage.addActor(scoreLabel);

    }

    // TODO zostawic
    public void upDateScoreboard() {

                scoreLabelString ="                    " +
                paddle1.getPlayer().getScore().getPoints() +
                " / "+ PongGameBeta.points + "        " +
                paddle1.getPlayer().getScore().getSets() +
                "   " +
                "(" + PongGameBeta.sets + ")" +
                "   " +
                paddle2.getPlayer().getScore().getSets() +
                "          " +
                paddle2.getPlayer().getScore().getPoints() + " / " + PongGameBeta.points;
        scoreLabel.setText(scoreLabelString);
    }

    // TODO zostawic
    public void showStartLabel() {
        if (PongGameBeta.gameLanguage.equals("PL")) {
            spaceLabel = new Label(" NACISNIJ SPACJE ABY ZACZAC ", BaseGame.labelStyle);
        } else {
            spaceLabel = new Label(" PRESS  SPACE  TO  START  ", BaseGame.labelStyle);
        }
        spaceLabel.setPosition((mainStage.getWidth() / 4) - 100, mainStage.getHeight() / 2);
        spaceLabel.setVisible(false);

        uiStage.addActor(spaceLabel);
    }


    // TODO ZOSTAWIc
    public void upDateStartLabel() {
        spaceLabel.setVisible(true);
        if (gameLanguage.equals("PL")) {
            if (flag == 0 && serverServe) {
                spaceLabel.setText(" NACISNIJ SPACJE ABY ZACZAC ");
                readyServer.setText("");
                readyClient.setText("");
            } else if (flag == 2 && !serverServe) {
                spaceLabel.setText(" PUNKT SETOWY! ");
                readyServer.setText("");
                readyClient.setText("");
            } else if (flag == 2 && serverServe) {
                spaceLabel.setText(" NACISNIJ SPACJE ABY ZACZAC ");
                readyServer.setText("");
                readyClient.setText("");
            } else if (flag == 9 && !serverServe) {
                spaceLabel.setText(" PUNKT MECZOWY !!! ");
                readyServer.setText("");
                readyClient.setText("");
            } else if (flag == 9 && serverServe) {
                spaceLabel.setText(" NACISNIJ SPACJE ABY ZACZAC ");
                readyServer.setText("");
                readyClient.setText("");
            } else if (flag == 99 && !serverServe) {
                spaceLabel.setText(" KONIEC GRY - WYGRALES");
                readyServer.setText("");
                readyClient.setText("");
            }
        } else if (gameLanguage.equals("EN")) {
            if (flag == 0 && serverServe) {
                spaceLabel.setText(" PRESS  SPACE  TO  START  ");
                readyServer.setText("");
                readyClient.setText("");
            } else if (flag == 2 && !serverServe) {
                spaceLabel.setText(" SET POINT! ");
                readyServer.setText("");
                readyClient.setText("");
            } else if (flag == 2 && serverServe) {
                spaceLabel.setText(" PRESS  SPACE  TO  START  ");
                readyServer.setText("");
                readyClient.setText("");
            } else if (flag == 9 && !serverServe) {
                spaceLabel.setText(" MATCH POINT !!! ");
                readyServer.setText("");
                readyClient.setText("");
            } else if (flag == 9 && serverServe) {
                spaceLabel.setText(" PRESS  SPACE  TO  START  ");
                readyServer.setText("");
                readyClient.setText("");
            } else if (flag == 99) {
                spaceLabel.setText(" END GAME ");
                readyServer.setText("");
                readyClient.setText("");
            }
        } else if (!serverServe) {
            spaceLabel.setText("");
        }
    }

    //TODO zostawić
    public void resetStartLocationLevelScreen(int i) {
        paddle1.setPosition(30, (mainStage.getHeight() / 2) - 75);
        paddle2.setPosition((mainStage.getWidth() - 60), (mainStage.getHeight() / 2) - 75);
        ball.setSpeed(0);
        //flag = 0;


        if (i == 1) {
            ball.setPosition(mainStage.getWidth() - 100, (mainStage.getHeight() / 2) - 16);
            ball.setMotionAngle(45);
        }
        if (i == 0) {
            ball.setPosition(100, (mainStage.getHeight() / 2) - 16);
            ball.setMotionAngle(135);
        }
    }
}
