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

public class MultiScreenServer extends BaseScreen {

    private BaseActor background;
    private Paddle paddle1, paddle2;
    private Ball ball;
    private BaseActor borderUp, borderDown,endGameBorderLeft, endGameBorderRight, winMessage, gameOverMessage;
    private Label scoreLabel, spaceLabel;

    private Label waiting;
    private Label ready;
    private String ready2;
    public boolean isServer; // zamienna publicza ze wzgledu na bardzo duze zagnieżdzenie.
    private int tcpPort = 54345, udpPort = 54789;
    private String ipHost;

    private Server server;


    private SomeRequest request;
    private SomeResponse response;
    private PaddleDirection direction;
    private Ballposition ballposition;

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
            ready = new Label( "GOTOWY?  NACISNIJ SPACJE", BaseGame.labelStyle);
            ready2 = "GOTOWY!";
        }
        else
        {
            waiting = new Label(" WAITING FOR THE PLAYER ", BaseGame.labelStyle);
            ready = new Label( "READY?  PRESS SPACE", BaseGame.labelStyle);
            ready2 = "READY!";
        }

        waiting.setPosition(mainStage.getWidth()/2 - 200, mainStage.getHeight()/2);
        ready.setPosition(mainStage.getWidth()/4 - 100, mainStage.getHeight()/2);
        ready.setVisible(false);

        // creating a paddle 1(player) & 2(cpu)
        paddle1 = new Paddle(30, (mainStage.getHeight() / 2) - 100, mainStage, new Player(PongGameBeta.nick));
        paddle2 = new Paddle( (mainStage.getWidth() - 60), (mainStage.getHeight()/2)-100, mainStage, new Player("CPU", false, true));

        ball = new Ball((mainStage.getWidth()/2)-16, (mainStage.getHeight()/2)-16,mainStage);


            server = new Server();
            server.start();
            mainStage.addActor(waiting);
            mainStage.addActor(ready);
            try {
                server.bind(54345, 54789);
            } catch (IOException e) {
                e.printStackTrace();
            }

            Kryo kryo = server.getKryo();
            kryo.register(SomeRequest.class);
            kryo.register(SomeResponse.class);
            kryo.register(PaddleDirection.class);
            kryo.register(Ballposition.class);

            server.addListener(new Listener() {
                public void received(Connection connection, Object object) {
                    if (object instanceof SomeRequest) {
                        SomeRequest request = (SomeRequest) object;
                        System.out.println(request.text);

                        response = new SomeResponse();
                        response.text = "Thanks";
                        connection.sendTCP(response);
                        waiting.setVisible(false);
                        ready.setVisible(true);
                    }
                    if  (object instanceof PaddleDirection) {
                        PaddleDirection direction = (PaddleDirection) object;
                        paddle2.accelerateWithoutRotation(direction.y);
                    }
                    if  (object instanceof Ballposition) {      // raczej server nie przyjmuje wartości pozycji piłki a ją wysyła
                        Ballposition ballposition = (Ballposition) object;
                        ball.setPosition(ballposition.x,ballposition.y);
                    }
                }
            });


    }
    @Override
    public void update(float dt) {
        ballposition = new Ballposition();
        ballposition.x = ball.getX();
        ballposition.y = ball.getY();
        server.sendToAllTCP(ballposition);

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
            PongGameBeta.setActiveScreen( new MenuScreen());
            if(isServer){
                server.close();
            }
        }

        if ((Gdx.input.isKeyPressed(Input.Keys.SPACE)) && flag !=1){
            ready.setText(ready2);
            request.text = "READY";
            server.sendToAllTCP(request);
            flag = 1;
//            ball.setSpeed(600);
//            ball.setMotionAngle(MathUtils.random(-45, 45));
        }
        if ((Gdx.input.isKeyPressed(Input.Keys.SPACE))){
            ball.setSpeed(600);
            ball.setMotionAngle(MathUtils.random(-45, 45));
        }

        /**
         *  bounced ball from paddle1  - Player
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
         *  bounced ball from paddle2  - CPU
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
         *  bounced ball from UP border
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

}
