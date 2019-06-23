package game.pong.beta.components;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.scenes.scene2d.Stage;
import game.pong.beta.baseGame.DefaultActor;

/**
 * @author Paweł Kumanowski
 * Class describin Paddle, its velocity, position on the screen and Players selection
 */
public class Paddle extends DefaultActor {

    private Player player;
    private float maxSpeed = 700;

    Paddle(float x, float y, Stage s, Player player) {
        super(x, y, s);
        this.player = player;
        loadTexture("paddle2.jpg");

        setAcceleration(100 * this.maxSpeed);
        setMaxSpeed(this.maxSpeed);
        setDeceleration(100 * this.maxSpeed);

        //setBoundaryPolygon(256);
    }

    /**
     * Method for steering with the Paddle object
     *
     * @param dt elapsed time (second) since last frame (supplied by Stage act method)
     */
    @Override
    public void act(float dt) {
        super.act(dt);

        if (!player.isAi() && !player.isOnline()) {
            if (Gdx.input.isKeyPressed(Input.Keys.UP))
                accelerateWithoutRotation(1);
            if (Gdx.input.isKeyPressed(Input.Keys.DOWN))
                accelerateWithoutRotation(-1);
        } else if (player.isAi()) {
            setDeceleration(maxSpeed);
        }

        applyPhysics(dt);
        setAnimationPaused(!isMoving());
        boundToWorld();
    }

    /**
     * @param ball metoda przyjmuje jako parametr obiekt z klasu Ball
     */
    public void ballTracking(Ball ball) {
        float Bx = ball.getX();
        float By = ball.getY();
        float Px = this.getX();
        float Py = this.getY() + 75;
        if (Py + 10 < By) {
            this.moveBy(0, 7);
            return;
        }
        if (Py - 10 >= By) {
            this.moveBy(0, -7);
        }

    }

    /**
     * Method getting Player class
     *
     * @return it returns Player object
     */
    public Player getPlayer() {
        return this.player;
    }

}
