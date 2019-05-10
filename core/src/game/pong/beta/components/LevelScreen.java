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

    private Paddle paddle1, paddle2;
    private Ball ball;
    private BaseActor borderUp, borderDown,endGameBorderLeft, endGameBorderRight;
    private Label scoreLabel, spaceLabel;

    private int flag = 0;

    @Override
    public void initialize() {


        BaseActor background = new BaseActor(0,0,mainStage);
        background.loadTexture("abstract1600x900.jpg");
        background.setSize(mainStage.getWidth(), mainStage.getHeight());

        BaseActor.setWorldBounds(background);

        borderUp = new BaseActor(0, (mainStage.getHeight()-10), mainStage);
        borderUp.loadTexture("border1600x10.png");

        borderDown = new BaseActor( 0, 0, mainStage);
        borderDown.loadTexture("border1600x10.png");

        endGameBorderLeft = new BaseActor( 0, 0, mainStage);
        endGameBorderLeft.loadTexture("border2x900endGame.png");

        endGameBorderRight = new BaseActor( mainStage.getWidth()-2, 0, mainStage);
        endGameBorderRight.loadTexture("border2x900endGame.png");

        paddle1 = new Paddle(30, (mainStage.getHeight()/2)- 100 , mainStage, new Player("Maciek") );
        paddle2 = new Paddle( (mainStage.getWidth() - 60), (mainStage.getHeight()/2)-100, mainStage, new Player("CPU", 0, true));


//        spaceLabel = new Label(" PRESS  SPACE  TO  START  ",BaseGame.labelStyle );
//        spaceLabel.setPosition((mainStage.getWidth()/2) - 200, mainStage.getHeight()/2);
//        spaceLabel.setVisible(true);
//        mainStage.addActor(spaceLabel);

        showScoreboard();
        showStartLabel();

        // Last item od mainStage
        ball = new Ball((mainStage.getWidth()/2)-16, (mainStage.getHeight()/2)-16,mainStage);


        // TODO
        //  scoreboard implementacja koniecznie na uiStage;

    }

    @Override
    public void update(float dt)
    {
        paddle2.ballTracking(ball);
        if (Gdx.input.isKeyPressed(Input.Keys.SPACE) && flag == 0) {
            ball.setSpeed(800);
            ball.setMotionAngle(MathUtils.random(-45, 45));
            spaceLabel.setText("");
            flag = 1;
        }


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
        if (ball.overlaps(endGameBorderLeft))
        {
            //TODO obsługa wyniku i ponownego rozgrywania
            paddle2.getPlayer().setScore( paddle2.getPlayer().getScore() +1);
            upDateScoreboard();
            //ball.setOpacity(0);
            resetStartLocationLevelScreen(1); // punkt dla Player 2
            upDateStartLabel();
        }
        if (ball.overlaps(endGameBorderRight))
        {
            paddle1.getPlayer().setScore( paddle1.getPlayer().getScore() +1);
            upDateScoreboard();
            //ball.setOpacity(0);
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
        scoreLabel = new Label(paddle1.getPlayer().getNick() + ": " + paddle1.getPlayer().getScore() + "              " +
                "     " +paddle2.getPlayer().getNick() + ": " + paddle2.getPlayer().getScore() , BaseGame.labelStyle);
        scoreLabel.setPosition((mainStage.getWidth()/2) - 200, mainStage.getHeight() - 50 );

        uiStage.addActor(scoreLabel);

    }


    @Override
    public void upDateScoreboard() {
        scoreLabel.setText(paddle1.getPlayer().getNick() + ": " + paddle1.getPlayer().getScore() + "              " +
                "     " +paddle2.getPlayer().getNick() + ": " + paddle2.getPlayer().getScore() );
    }

    // TODO metoda do resetowania ustawień sceny. - po dotknięciu do ściany lewej lub prawej wynik sie zmienia i
    //      resetują ustawienia piłki, paletek.
    public void resetStartLocationLevelScreen(int i)
    {
        paddle1.setPosition(30, (mainStage.getHeight()/2)- 75);
        paddle2.setPosition((mainStage.getWidth() - 60), (mainStage.getHeight()/2)-75);
        ball.setSpeed(0);
        flag = 0;


        if(i==1){
            ball.setPosition(mainStage.getWidth() - 100, (mainStage.getHeight()/2)-16);
            ball.setMotionAngle(45);
        }
        if(i == 0){
            ball.setPosition(100 , (mainStage.getHeight()/2)-16);
            ball.setMotionAngle(135);
        }

    }

    public void showStartLabel()
    {
        spaceLabel = new Label(" PRESS  SPACE  TO  START  ",BaseGame.labelStyle );
        spaceLabel.setPosition((mainStage.getWidth()/2) - 200, mainStage.getHeight()/2);



        uiStage.addActor(spaceLabel);

        if (Gdx.input.isKeyPressed(Input.Keys.SPACE)) {
            spaceLabel.setText("");
        }


    }
    public void upDateStartLabel()
    {
        spaceLabel.setText(" PRESS  SPACE  TO  START  ");

    }
}
