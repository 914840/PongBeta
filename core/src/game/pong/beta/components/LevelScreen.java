package game.pong.beta.components;

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
    private Label scoreLabel;

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

        paddle1 = new Paddle(30, (mainStage.getHeight()/2)- 75 , mainStage, new Player("Maciek") );
        paddle2 = new Paddle( (mainStage.getWidth() - 60), (mainStage.getHeight()/2)-75, mainStage, new Player("CPU", 0, true));

        showScoreboard();


        // Last item od mainStage
        ball = new Ball((mainStage.getWidth()/2)-16, (mainStage.getHeight()/2)-16,mainStage);


        // TODO
        //  scoreboard implementacja koniecznie na uiStage;

    }

    @Override
    public void update(float dt)
    {
        paddle2.ballTracking(ball);

        if (ball.overlaps(paddle1))
        {

            ball.setMotionAngle(180 - ball.getMotionAngle());
        }
        if (ball.overlaps(paddle2))
        {
            ball.setMotionAngle(180 - ball.getMotionAngle());
        }
        if (ball.overlaps(borderUp))
        {
            ball.setMotionAngle((180 - ball.getMotionAngle()) + 180);
        }
        if (ball.overlaps(borderDown))
        {
            ball.setMotionAngle((180 - ball.getMotionAngle()) + 180);
        }
        if (ball.overlaps(endGameBorderLeft))
        {
            //TODO obsługa wyniku i ponownego rozgrywania
            paddle2.getPlayer().setScore( paddle2.getPlayer().getScore() +1);
            upDateScoreboard();
            //ball.setOpacity(0);
            PongGameBeta.setActiveScreen(new LevelScreen());
        }
        if (ball.overlaps(endGameBorderRight))
        {
            paddle1.getPlayer().setScore( paddle1.getPlayer().getScore() +1);
            upDateScoreboard();
            //ball.setOpacity(0);
            PongGameBeta.setActiveScreen(new LevelScreen());
        }



    }

    @Override
    public float getPlayerScore(Player player) {
        return 0;
    }

    @Override
    public void showScoreboard() {
        scoreLabel = new Label(paddle1.getPlayer().getNick() + ": " + paddle1.getPlayer().getScore(), BaseGame.labelStyle);
        uiStage.addActor(scoreLabel);

    }

    @Override
    public void upDateScoreboard() {
        scoreLabel.setText(paddle1.getPlayer().getNick() + ": " + paddle1.getPlayer().getScore());
    }

    // TODO metoda do resetowania ustawień sceny. - po dotknięciu do ściany lewej lub prawej wynik sie zmienia i
    //      resetują ustawienia piłki, paletek.

}
