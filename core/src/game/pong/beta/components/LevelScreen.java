package game.pong.beta.components;


import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import game.pong.beta.baseGame.DefaultActor;
import game.pong.beta.baseGame.DefaultGame;
import game.pong.beta.baseGame.DefaultScreen;
import game.pong.beta.baseGame.PongGameBeta;

/**
 * @author Maciej Tymorek
 * Class for displaying the Game level screen
 */

public class LevelScreen extends DefaultScreen {

    private DefaultActor background;
    private Paddle paddle1, paddle2;
    private Ball ball;
    private DefaultActor borderUp, borderDown, endGameBorderLeft, endGameBorderRight, winMessage, gameOverMessage;
    private Label scoreLabel, spaceLabel;

    /**
     * Flag codes: 0 - start game, 1 - game on, 2 - set point , 5 - new set, 9 - match point, 99 - GameOver
     */
    private int flag = 0;


    /**
     * Method which initializes default look of the game screen with two paddles, scoreboard and ball.
     */
    @Override
    public void initialize() {

        // method to initiate background
        background = setBackground();

        // method to set not-solid border of screen
        DefaultActor.setWorldBounds(background);

        // creating a solid upper border
        borderUp = new DefaultActor(0, (mainStage.getHeight() - 10), mainStage);
        borderUp.loadTexture("border1600x10.png");

        // creating a solid lower border
        borderDown = new DefaultActor(0, 0, mainStage);
        borderDown.loadTexture("border1600x10.png");

        // creating a solid left border
        endGameBorderLeft = new DefaultActor(0, 0, mainStage);
        endGameBorderLeft.loadTexture("border2x900endGame.png");

        // creating a solid right border
        endGameBorderRight = new DefaultActor(mainStage.getWidth() - 2, 0, mainStage);
        endGameBorderRight.loadTexture("border2x900endGame.png");

        // creating a paddle 1(player) & 2(cpu)
        paddle1 = new Paddle(30, (mainStage.getHeight() / 2) - 100, mainStage, new Player(PongGameBeta.nick));
        paddle2 = new Paddle((mainStage.getWidth() - 60), (mainStage.getHeight() / 2) - 100, mainStage, new Player("CPU", true, false));

        // method to show scoreboard on screen
        showScoreboard();
        // method to show start Label with instructions
        showStartLabel();

        // Last item od mainStage
        ball = new Ball((mainStage.getWidth() / 2) - 16, (mainStage.getHeight() / 2) - 16, mainStage);

        upDateScoreboard();

    }

    /**
     * Method is used for updating the the game state.
     * When Space is pressed the game starts
     * When ESC is pressed the game stops and returns to main menu
     * Here we also have description of game logic conditions - After ball crosses right or lest border appropriate method is called
     *
     * @param dt Time elapsed since previous frame (delta time); typically obtained from <code>act</code> method
     */
    @Override
    public void update(float dt) {

        // paddle tracking ball method
        paddle2.ballTracking(ball);


        if ((Gdx.input.isKeyPressed(Input.Keys.SPACE)) && ((flag == 0) || (flag == 2) || (flag == 9))) {
            ball.setSpeed(800);
            ball.setMotionAngle(MathUtils.random(-45, 45));
            spaceLabel.setText("");
            flag = 1;
        } else if (flag == 5) {
            paddle1.getPlayer().getScore().setPoints(0);
            paddle2.getPlayer().getScore().setPoints(0);
        } else if ((Gdx.input.isKeyPressed(Input.Keys.SPACE)) && (flag == 99)) {
            PongGameBeta.setActiveScreen(new MenuScreen());
        } else if (Gdx.input.isKeyPressed(Input.Keys.ESCAPE)) {
            PongGameBeta.setActiveScreen(new MenuScreen());
        }


        /**
         *  bounced ball from paddle1  - Player
         */
        if (ball.overlaps(paddle1)) {
            if (paddle1.isMoving()) {
                if ((paddle1.getY() + 25) < (ball.getY() - 32) || paddle1.getY() + 175 > (ball.getY() + 32)) {
                    float a = 180 - ball.getMotionAngle() + MathUtils.random(45, 60);
                    if (a > 80 && a < 280) {
                        a = MathUtils.random(50, 75);
                    }
                    ball.setMotionAngle(a);
                } else if ((paddle1.getY() + 60) < (ball.getY() - 32) || paddle1.getY() + 140 > (ball.getY() + 32)) {
                    ball.setMotionAngle(180 - ball.getMotionAngle() + MathUtils.random(30, 50));
                } else if ((paddle1.getY() + 60) > (ball.getY() - 32) && paddle1.getY() + 140 < (ball.getY() + 32)) {
                    ball.setMotionAngle(180 - ball.getMotionAngle() + MathUtils.random(10, 30));
                }
            } else if (!paddle1.isMoving()) {
                ball.setMotionAngle(180 - ball.getMotionAngle());
            }
        }

        /**
         *  bounced ball from paddle2  - CPU
         */
        if (ball.overlaps(paddle2)) {
            if (paddle2.isMoving()) {
                if ((paddle1.getY() + 25) < (ball.getY() - 32) || paddle1.getY() + 175 > (ball.getY() + 32)) {
                    float a = 180 - ball.getMotionAngle() + MathUtils.random(45, 60);
                    if (a > 260 && a < 100) {
                        a = MathUtils.random(120, 160);
                    }
                    ball.setMotionAngle(a);
                } else if ((paddle1.getY() + 60) < (ball.getY() - 32) || paddle1.getY() + 140 > (ball.getY() + 32)) {
                    ball.setMotionAngle(180 - ball.getMotionAngle() + MathUtils.random(30, 50));
                } else if ((paddle1.getY() + 60) > (ball.getY() - 32) && paddle1.getY() + 140 < (ball.getY() + 32)) {
                    ball.setMotionAngle(180 - ball.getMotionAngle() + MathUtils.random(10, 30));
                }
            } else if (!paddle2.isMoving()) {
                ball.setMotionAngle(180 - ball.getMotionAngle());
            }
        }

        /**
         *  bounced ball from UP border
         */
        if (ball.overlaps(borderUp)) {

            float angle = (180 - ball.getMotionAngle()) + 180;
            if (angle > 350) {
                ball.setMotionAngle(angle - 15);
            } else if (angle < 190) {
                ball.setMotionAngle(angle + 15);
            } else {
                ball.setMotionAngle(angle);
            }
        }

        /**
         *  bounced ball from DOWN border
         */
        if (ball.overlaps(borderDown)) {
            float angle = (180 - ball.getMotionAngle()) + 180;
            if (angle < 10) {
                ball.setMotionAngle(angle + 15);
            } else if (angle > 170) {
                ball.setMotionAngle(angle - 15);
            } else {
                ball.setMotionAngle(angle);
            }
        }

        /**
         *  ball cross left border
         */
        if (ball.overlaps(endGameBorderLeft)) {

            flag = paddle2.getPlayer().getScore().addOnePoint();

            upDateScoreboard();
            resetStartLocationLevelScreen(1); // punkt dla Player 2
            upDateStartLabel();

        }

        /**
         *  ball cross right border
         */
        if (ball.overlaps(endGameBorderRight)) {
            flag = paddle1.getPlayer().getScore().addOnePoint();

            upDateScoreboard();
            resetStartLocationLevelScreen(0); // punkt dla Player 1
            upDateStartLabel();

        }


    }


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
                paddle2.getPlayer().getNick(), DefaultGame.labelStyle);
        scoreLabel.setPosition((mainStage.getWidth() / 2) - 240, mainStage.getHeight() - 50);

        uiStage.addActor(scoreLabel);

    }


    /**
     * Method for updating Scoreboard
     */
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

    /**
     * Method used for resetting the scene, after ball touches the right or left border, the result changes and position of paddles and the ball are reset.
     *
     * @param i (used for setting a flag)
     */
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

    /**
     * Method used for displaying default labels when Player enters main game stage.
     */

    public void showStartLabel() {
        if (PongGameBeta.gameLanguage.equals("PL")) {
            spaceLabel = new Label(" NACISNIJ SPACJE ABY ZACZAC ", DefaultGame.labelStyle);
        } else {
            spaceLabel = new Label(" PRESS  SPACE  TO  START  ", DefaultGame.labelStyle);
        }
        spaceLabel.setPosition((mainStage.getWidth() / 2) - 200, mainStage.getHeight() / 2);


        uiStage.addActor(spaceLabel);

        if (Gdx.input.isKeyPressed(Input.Keys.SPACE)) {
            spaceLabel.setText("");
        }


    }

    /**
     * Method used for changing the message strings of the Start label depending of the language selected.
     */
    public void upDateStartLabel() {
        if (PongGameBeta.gameLanguage.equals("PL")) {
            if (flag == 0) {
                spaceLabel.setText(" NACISNIJ SPACJE ABY ZACZAC ");
            } else if (flag == 2) {
                spaceLabel.setText(" PUNKT SETOWY! ");
            } else if (flag == 9) {
                spaceLabel.setText(" PUNKT MECZOWY !!! ");
            } else if (flag == 99) {
                spaceLabel.setText(" KONIEC GRY ");
            }
        } else if (PongGameBeta.gameLanguage.equals("EN")) {
            if (flag == 0) {
                spaceLabel.setText(" PRESS  SPACE  TO  START  ");
            } else if (flag == 2) {
                spaceLabel.setText(" SET POINT! ");
            } else if (flag == 9) {
                spaceLabel.setText(" MATCH POINT !!! ");
            } else if (flag == 99) {
                spaceLabel.setText(" END GAME ");
            }
        }
    }

    /**
     * Helper method
     *
     * @param flag (parameter used for conditions selection)
     */

    public void setFlag(int flag) {
        this.flag = flag;
    }

}
