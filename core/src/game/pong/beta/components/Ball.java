package game.pong.beta.components;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Stage;
import game.pong.beta.BaseActor;

public class Ball extends BaseActor {

    private float speed;

    Ball(float x, float y, Stage s)
    {
        super(x,y,s);
        loadTexture("ball.png");


        setSpeed(500);
        setMotionAngle(120);
        setBoundaryPolygon(8);
    }

    @Override
    public void act(float dt) {
        super.act(dt);

        applyPhysics(dt);
        setAnimationPaused( !isMoving() );
        boundToWorld();

    }

    //TODO
    // Metody do obsługi odbicia od Paddle oraz od ściany
}
