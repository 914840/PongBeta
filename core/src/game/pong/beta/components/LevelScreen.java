package game.pong.beta.components;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.utils.compression.lzma.Base;
import game.pong.beta.BaseActor;
import game.pong.beta.BaseGame;
import game.pong.beta.BaseScreen;
import game.pong.beta.PongGameBeta;

public class LevelScreen extends BaseScreen implements HudComponent {

    private BaseActor background;
    private Paddle paddle1, paddle2;
    private Ball ball;
    private BaseActor borderUp, borderDown,endGameBorderLeft, endGameBorderRight, winMessage, gameOverMessage;
    private Label scoreLabel, spaceLabel;

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
        borderUp = new BaseActor(0, (mainStage.getHeight()-10), mainStage);
        borderUp.loadTexture("border1600x10.png");

        // creating a solid lower border
        borderDown = new BaseActor( 0, 0, mainStage);
        borderDown.loadTexture("border1600x10.png");

        // creating a solid left border
        endGameBorderLeft = new BaseActor( 0, 0, mainStage);
        endGameBorderLeft.loadTexture("border2x900endGame.png");

        // creating a solid right border
        endGameBorderRight = new BaseActor( mainStage.getWidth()-2, 0, mainStage);
        endGameBorderRight.loadTexture("border2x900endGame.png");

        // creating a paddle 1(player) & 2(cpu)
        paddle1 = new Paddle(30, (mainStage.getHeight()/2)- 100 , mainStage, new Player(PongGameBeta.nick) );
        paddle2 = new Paddle( (mainStage.getWidth() - 60), (mainStage.getHeight()/2)-100, mainStage, new Player("CPU", true, false));

        // method to show scoreboard on screen
        showScoreboard();
        // method to show start Label with instructions
        showStartLabel();

        // Last item od mainStage
        ball = new Ball((mainStage.getWidth()/2)-16, (mainStage.getHeight()/2)-16,mainStage);

        upDateScoreboard();

    }

    @Override
    public void update(float dt)
    {

        // paddle tracking ball method
        paddle2.ballTracking(ball);


        if ((Gdx.input.isKeyPressed(Input.Keys.SPACE)) && ((flag == 0) || (flag == 2) || (flag == 9))) {
            ball.setSpeed(800);
            ball.setMotionAngle(MathUtils.random(-45, 45));
            spaceLabel.setText("");
            flag = 1;
        }
        else if(flag == 5 )
        {
            paddle1.getPlayer().getScore().setPoints(0);
            paddle2.getPlayer().getScore().setPoints(0);
        }
        else if((Gdx.input.isKeyPressed(Input.Keys.SPACE)) && (flag == 99))
        {
            PongGameBeta.setActiveScreen( new MenuScreen());
        }
        else if(Gdx.input.isKeyPressed(Input.Keys.ESCAPE))
        {
            PongGameBeta.setActiveScreen( new MenuScreen());
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

                upDateScoreboard();
                resetStartLocationLevelScreen(1); // punkt dla Player 2
                upDateStartLabel();

        }

        /**
         *  ball cross right border
         */
        if (ball.overlaps(endGameBorderRight))
        {
            flag = paddle1.getPlayer().getScore().addOnePoint();

            upDateScoreboard();
            resetStartLocationLevelScreen(0); // punkt dla Player 1
            upDateStartLabel();

        }



    }

    @Override
    public float getPlayerScore(Player player) {
        return 0;
    }

    @Override
    public void showScoreboard() {
        scoreLabel = new Label(paddle1.getPlayer().getNick() +
                "    " +
                paddle1.getPlayer().getScore().getPoints() +
                "          " +
                paddle1.getPlayer().getScore().getSets() +
                "   " +
                "(" + PongGameBeta.sets + ")" +
                "   " +
                paddle2.getPlayer().getScore().getSets() +
                "          " +
                paddle2.getPlayer().getScore().getPoints() +
                "    " +
                paddle2.getPlayer().getNick(), BaseGame.labelStyle);
        scoreLabel.setPosition((mainStage.getWidth()/2) - 240, mainStage.getHeight() - 50 );

        uiStage.addActor(scoreLabel);

    }


    @Override

    //TODO poprawić scoreboard jak w showScoreBoard - bez wyśfietlania nicków.
    public void upDateScoreboard() {
        scoreLabel.setText("                       " +
                paddle1.getPlayer().getScore().getPoints() +
                "          " +
                paddle1.getPlayer().getScore().getSets() +
                "   " +
                "(" + PongGameBeta.sets + ")" +
                "   " +
                paddle2.getPlayer().getScore().getSets() +
                "          " +
                paddle2.getPlayer().getScore().getPoints()
        );
    }

    // TODO metoda do resetowania ustawień sceny. - po dotknięciu do ściany lewej lub prawej wynik sie zmienia i
    //      resetują ustawienia piłki, paletek.
    public void resetStartLocationLevelScreen(int i)
    {
        paddle1.setPosition(30, (mainStage.getHeight()/2)- 75);
        paddle2.setPosition((mainStage.getWidth() - 60), (mainStage.getHeight()/2)-75);
        ball.setSpeed(0);
        //flag = 0;


        if(i==1){
            ball.setPosition(mainStage.getWidth() - 100, (mainStage.getHeight()/2)-16);
            ball.setMotionAngle(45);
        }
        if(i == 0){
            ball.setPosition(100 , (mainStage.getHeight()/2)-16);
            ball.setMotionAngle(135);
        }

    }

    public void showStartLabel() {
        if (PongGameBeta.gameLanguage.equals("PL")) {
            spaceLabel = new Label(" NACISNIJ SPACJE ABY ZACZAC ", BaseGame.labelStyle);
        } else {
            spaceLabel = new Label(" PRESS  SPACE  TO  START  ", BaseGame.labelStyle);
        }
        spaceLabel.setPosition((mainStage.getWidth()/2) - 200, mainStage.getHeight()/2);



        uiStage.addActor(spaceLabel);

        if (Gdx.input.isKeyPressed(Input.Keys.SPACE)) {
            spaceLabel.setText("");
        }


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

    public void setFlag(int flag)
    {
        this.flag = flag;
    }

}
