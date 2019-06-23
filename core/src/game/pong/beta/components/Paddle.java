package game.pong.beta.components;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.scenes.scene2d.Stage;
import game.pong.beta.baseGame.BaseActor;

/**
 * @Author: Paweł Kumanowski
 * @Project: Pong
 *
 */
public class Paddle extends BaseActor {

    private Player player;
    private float maxSpeed = 700;

    Paddle(float x, float y, Stage s, Player player){
        super(x,y,s);
        this.player = player;
        loadTexture("paddle2.jpg");

        setAcceleration(100*this.maxSpeed);
        setMaxSpeed(this.maxSpeed);
        setDeceleration(100*this.maxSpeed);

        //setBoundaryPolygon(256);
    }

    @Override
    public void act(float dt) {
        super.act(dt);

        if(player.isAi() == false && player.isOnline() == false){
            if (Gdx.input.isKeyPressed(Input.Keys.UP))
                accelerateWithoutRotation(1);
            if (Gdx.input.isKeyPressed(Input.Keys.DOWN))
                accelerateWithoutRotation(-1);
        } else if (player.isAi() == true) {
            setDeceleration(maxSpeed);
        }

        applyPhysics(dt);
        setAnimationPaused( !isMoving() );
        boundToWorld();
    }

    public void ballTracking(Ball ball)
    {
        float Bx = ball.getX();
        float By = ball.getY();
        float Px =  this.getX();
        float Py = this.getY() + 75;
        if(Py + 10 < By){
            this.moveBy(0,7);
            return;
        }
        if(Py - 10 >= By){
            this.moveBy(0,-7);
            return;
        }

    }

    public Player getPlayer(){
        return this.player;
    }

    public void setPaddleDirection(int x){
        if(x == 1){
            accelerateWithoutRotation(1);
        }
        else if( x == -1){
            accelerateWithoutRotation(-1);
        }

    }
}
