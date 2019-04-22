package game.pong.beta.components;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.scenes.scene2d.Stage;
import game.pong.beta.BaseActor;

public class Paddle extends BaseActor {

    private Player player;

    Paddle(float x, float y, Stage s, Player player){
        super(x,y,s);
        this.player = player;
        loadTexture("paddle.jpg");

        setAcceleration(800);
        setMaxSpeed(400);
        setDeceleration(1000);

        setBoundaryPolygon(8);
    }

    @Override
    public void act(float dt) {
        super.act(dt);

        if(player.isAi() == false){
            if (Gdx.input.isKeyPressed(Input.Keys.UP))
                accelerateWithoutRotation(1);
            if (Gdx.input.isKeyPressed(Input.Keys.DOWN))
                accelerateWithoutRotation(-1);
        }

        applyPhysics(dt);
        setAnimationPaused( !isMoving() );
        boundToWorld();
    }
}
