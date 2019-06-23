package game.pong.beta.components;


import com.badlogic.gdx.scenes.scene2d.Stage;
import game.pong.beta.baseGame.DefaultActor;

/**
 * @author Paweł Kumanowski
 * Class used for initializing Ball default settings as speed and texture.
 */

public class Ball extends DefaultActor {

    /**
     * Ball constructor
     * @param x float
     * @param y float
     * @param s Stage object
     */
    Ball(float x, float y, Stage s) {
        super(x, y, s);
        loadTexture("ball.png");


        setSpeed(0); // 0 bo ustawiane w LevelScreen > showStartLabel
        setMotionAngle(120);
        setBoundaryPolygon(4);
    }

    /**
     * Method activating the Ball
     * @param dt elapsed time (second) since last frame (supplied by Stage act method)
     */
    @Override
    public void act(float dt) {
        super.act(dt);

        applyPhysics(dt);
        setAnimationPaused(!isMoving());
        boundToWorld();

    }

}
