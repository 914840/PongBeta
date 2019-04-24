package game.pong.beta.components;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.scenes.scene2d.Stage;
import game.pong.beta.BaseActor;

public class Paddle extends BaseActor {

    private Player player;
    private float maxSpeed = 500;

    Paddle(float x, float y, Stage s, Player player){
        super(x,y,s);
        this.player = player;
        loadTexture("paddle.jpg");

        setAcceleration(2*this.maxSpeed);
        setMaxSpeed(this.maxSpeed);
        setDeceleration(2*this.maxSpeed);

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
        } else if (player.isAi() == true){
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
        if(Py < By){
            this.moveBy(0,6);
            return;
        }
        if(Py >= By){
            this.moveBy(0,-6);
            return;
        }

    }

    public Player getPlayer(){
        return this.player;
    }
}
