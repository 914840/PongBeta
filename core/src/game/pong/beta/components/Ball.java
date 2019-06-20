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


        setSpeed(0); // 0 bo ustawiane w LevelScreen > showStartLabel
        setMotionAngle(120);
        setBoundaryPolygon(4);
    }

    @Override
    public void act(float dt) {
        super.act(dt);

        applyPhysics(dt);
        setAnimationPaused( !isMoving() );
        boundToWorld();

    }

}
