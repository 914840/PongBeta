package game.pong.beta.components;

import com.badlogic.gdx.utils.compression.lzma.Base;
import game.pong.beta.BaseActor;
import game.pong.beta.BaseScreen;

public class LevelScreen extends BaseScreen {

    private Paddle paddle1, paddle2;
    private Ball ball;
    private BaseActor borderUp, borderDown;


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


        paddle1 = new Paddle(30, (mainStage.getHeight()/2)- 75 , mainStage, new Player("Maciek") );
        paddle2 = new Paddle( (mainStage.getWidth() - 60), (mainStage.getHeight()/2)-75, mainStage, new Player("CPU", 0, true));
        ball = new Ball((mainStage.getWidth()/2)-16, (mainStage.getHeight()/2)-16,mainStage);




        // TODO
        //  scoreboard implementacja koniecznie na uiStage;

    }

    @Override
    public void update(float dt)
    {
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

    }
}
