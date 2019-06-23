package game.pong.beta.baseGame;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.Texture.TextureFilter;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Animation.PlayMode;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.*;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.Array;

import java.util.ArrayList;

/**
 * @author Maciej Tymorek
 * Extends functionality of the LibGDX Actor class.
 * by adding support for textures, animations
 * collision polygons, movement, world boundaries.
 * As most game objects should extend this class; lists of extensions can be retrieved by stage and class name.
 */
public class DefaultActor extends Actor {
    private Animation<TextureRegion> animation;
    private float elapsedTime;
    private boolean animationPaused;

    private Vector2 velocityVec;
    private Vector2 accelerationVec;
    private float acceleration;
    private float maxSpeed;
    private float deceleration;

    private Polygon boundaryPolygon;

    // stores size of game world for all baseGame
    public static Rectangle worldBounds;

    public DefaultActor(float x, float y, Stage s) {
        // call constructor from Actor class
        super();

        // perform additional initialization tasks
        setPosition(x, y);
        s.addActor(this);

        //initialize animation data
        animation = null;
        elapsedTime = 0;
        animationPaused = false;

        // initialize physics data
        velocityVec = new Vector2(0, 0);
        accelerationVec = new Vector2(0, 0);
        acceleration = 0;
        maxSpeed = 1000;
        deceleration = 0;

        boundaryPolygon = null;
    }

    /**
     * Coordinate center of actor at given position coordinates.
     *
     * @param x x-coordinate to center at
     * @param y y-coordinate to center at
     */
    public void centerAtPosition(float x, float y) {
        setPosition(x - getWidth() / 2, y - getHeight() / 2);
    }

    // ----------------------------------------------
    // Animation methods
    // ----------------------------------------------

    /**
     * Animation is set when actor is rendered and its size set.
     *
     * @param anim animation starts after rendering the actor
     */
    public void setAnimation(Animation<TextureRegion> anim) {
        animation = anim;
        TextureRegion tr = animation.getKeyFrame(0);
        float w = tr.getRegionWidth();
        float h = tr.getRegionHeight();
        setSize(w, h);
        setOrigin(w / 2, h / 2);

        if (boundaryPolygon == null)
            setBoundaryRectangle();
    }

    /**
     * Creates an animation from images stored in separate files.
     *
     * @param nameFiles array of names of files containing animation images
     * @param frameTime how long each frame should be displayed
     * @param loop      should the animation loop
     * @return animation created (useful for storing multiple animations)
     */
    public Animation<TextureRegion> loadAnimationFromFiles(String[] nameFiles, float frameTime, boolean loop) {
        Array<TextureRegion> textureArray = new Array<>();

        for (String fileName : nameFiles) {
            Texture texture = new Texture(Gdx.files.internal(fileName));
            texture.setFilter(TextureFilter.Linear, TextureFilter.Linear);
            textureArray.add(new TextureRegion(texture));
        }

        Animation<TextureRegion> anim = new Animation<>(frameTime, textureArray);

        if (loop)
            anim.setPlayMode(PlayMode.LOOP);
        else
            anim.setPlayMode(PlayMode.NORMAL);

        if (animation == null)
            setAnimation(anim);

        return anim;
    }

    /**
     * Method used for 1-frame animation from a single texture.
     *
     * @param fileName names of image file
     */
    public void loadTexture(String fileName) {
        String[] fileNames = new String[1];
        fileNames[0] = fileName;
        loadAnimationFromFiles(fileNames, 1, true);
    }

    /**
     * Pause is set here for the animation.
     *
     * @param pause true when animation is paused, false when resumed
     */
    public void setAnimationPaused(boolean pause) {
        animationPaused = pause;
    }

    //     ----------------------------------------------
    //     physics/motion methods
    //     ----------------------------------------------


    /**
     * Object acceleration is set here
     *
     * @param acc Acceleration in (pixels/second) per second.
     **/
    public void setAcceleration(float acc) {
        acceleration = acc;
    }

    /**
     * Deceleration is set here
     * Only apply it when object is not accelerating.
     *
     * @param dec Deceleration in (pixels/second) per second.
     **/
    public void setDeceleration(float dec) {
        deceleration = dec;
    }


    /**
     * Set maximum speed of this object.
     *
     * @param ms Maximum speed of this object in (pixels/second).
     */
    public void setMaxSpeed(float ms) {
        maxSpeed = ms;
    }

    /**
     * Set the speed of movement (in pixels/second) in current direction.
     * If current speed is zero (direction is undefined), direction will be set to 0 degrees.
     *
     * @param speed of movement (pixels/second)
     */
    public void setSpeed(float speed) {
        // if length is zero, then assume motion angle is zero degrees
        if (velocityVec.len() == 0)
            velocityVec.set(speed, 0);
        else
            velocityVec.setLength(speed);
    }

    /**
     * Calculates the speed of movement (in pixels/second).
     *
     * @return speed of movement (pixels/second)
     */
    public float getSpeed() {
        return velocityVec.len();
    }

    /**
     * Determines if this object is moving (if speed is greater than zero).
     *
     * @return false when speed is zero, true otherwise
     */
    public boolean isMoving() {
        return (getSpeed() > 0);
    }

    /**
     * Sets the angle of motion (in degrees).
     * If current speed is zero, this will have no effect.
     *
     * @param angle of motion (degrees)
     */
    public void setMotionAngle(float angle) {
        velocityVec.setAngle(angle);
    }

    /**
     * Get the angle of motion (in degrees), calculated from the velocity vector.
     * <br>
     * To align actor image angle with motion angle, use <code>setRotation( getMotionAngle() )</code>.
     *
     * @return angle of motion (degrees)
     */
    public float getMotionAngle() {
        return velocityVec.angle();
    }


    /**
     * Update accelerate vector by angle and value stored in acceleration field.
     * Acceleration is applied by <code>applyPhysics</code> method.
     *
     * @param angle Angle (degrees) in which to accelerate.
     * @see #acceleration
     * @see #applyPhysics
     **/

    public void accelerateAtAngle(float angle) {
        accelerationVec.add(
                new Vector2(acceleration, 0).setAngle(angle));
    }

    /**
     * Update accelerate vector by current rotation angle and value stored in acceleration field.
     * Acceleration is applied by <code>applyPhysics</code> method.
     *
     * @see #acceleration
     * @see #applyPhysics
     **/
    public void accelerateForward() {
        accelerateAtAngle(getRotation());
    }

    /**
     * Method for paddle movement up and down
     *
     * @param direction takes integer value and is used as a flag conditional statement
     */

    public void accelerateWithoutRotation(int direction) {

        if (direction == 1) {
            accelerationVec.add(
                    new Vector2(0, acceleration).setAngle(90)
            );
        }
        if (direction == -1) {
            accelerationVec.add(
                    new Vector2(0, -acceleration).setAngle(270)
            );
        }
    }

    /**
     * Adjust velocity vector based on acceleration vector,
     * then adjust position based on velocity vector. <br>
     * If not accelerating, deceleration value is applied. <br>
     * Speed is limited by maxSpeed value. <br>
     * Acceleration vector reset to (0,0) at end of method. <br>
     *
     * @param dt Time elapsed since previous frame (delta time); typically obtained from <code>act</code> method.
     * @see #maxSpeed
     */
    public void applyPhysics(float dt) {
        // apply acceleration
        velocityVec.add(accelerationVec.x * dt, accelerationVec.y * dt);

        float speed = getSpeed();

        // decrease speed (decelerate) when not accelerating
        if (accelerationVec.len() == 0)
            speed -= deceleration * dt;

        // keep speed within set bounds
        speed = MathUtils.clamp(speed, 0, maxSpeed);

        // update velocity
        setSpeed(speed);

        // apply velocity
        moveBy(velocityVec.x * dt, velocityVec.y * dt);

        // reset acceleration
        accelerationVec.set(0, 0);
    }

    /**
     * Set rectangular-shaped collision polygon.
     * This method is automatically called when animation is set,
     * provided that the current boundary polygon is null.
     */
    public void setBoundaryRectangle() {
        float w = getWidth();
        float h = getHeight();

        float[] vertices = {0, 0, w, 0, w, h, 0, h};
        boundaryPolygon = new Polygon(vertices);
    }

    /**
     * Replace default (rectangle) collision polygon with an n-sided polygon. <br>
     * Vertices of polygon lie on the ellipse contained within bounding rectangle.
     * Note: one vertex will be located at point (0,width);
     * a 4-sided polygon will appear in the orientation of a diamond.
     *
     * @param numSides number of sides of the collision polygon
     */
    public void setBoundaryPolygon(int numSides) {
        float w = getWidth();
        float h = getHeight();

        float[] vertices = new float[2 * numSides];
        for (int i = 0; i < numSides; i++) {
            float angle = i * 6.28f / numSides;
            // x-coordinate
            vertices[2 * i] = w / 2 * MathUtils.cos(angle) + w / 2;
            // y-coordinate
            vertices[2 * i + 1] = h / 2 * MathUtils.sin(angle) + h / 2;
        }
        boundaryPolygon = new Polygon(vertices);

    }

    /**
     * Returns bounding polygon for this DefaultActor, adjusted by Actor's current position and rotation.
     *
     * @return bounding polygon for this DefaultActor
     */
    public Polygon getBoundaryPolygon() {
        boundaryPolygon.setPosition(getX(), getY());
        boundaryPolygon.setOrigin(getOriginX(), getOriginY());
        boundaryPolygon.setRotation(getRotation());
        boundaryPolygon.setScale(getScaleX(), getScaleY());
        return boundaryPolygon;
    }

    /**
     * Determine if this DefaultActor overlaps other DefaultActor (according to collision polygons).
     *
     * @param other DefaultActor to check for overlap
     * @return true if collision polygons of this and other DefaultActor overlap
     */
    public boolean overlaps(DefaultActor other) {
        Polygon poly1 = this.getBoundaryPolygon();
        Polygon poly2 = other.getBoundaryPolygon();

        // initial test to improve performance
        if (!poly1.getBoundingRectangle().overlaps(poly2.getBoundingRectangle()))
            return false;

        return Intersector.overlapConvexPolygons(poly1, poly2);
    }

    /**
     * Set world dimensions for use by methods boundToWorld() and scrollTo().
     *
     * @param width  width of world
     * @param height height of world
     */
    public static void setWorldBounds(float width, float height) {
        worldBounds = new Rectangle(0, 0, width, height);
    }

    /**
     * Set world dimensions for use by methods boundToWorld() and scrollTo().
     *
     * @param ba whose size determines the world bounds (typically a background image)
     */
    public static void setWorldBounds(DefaultActor ba) {
        setWorldBounds(ba.getWidth(), ba.getHeight());
    }

    /**
     * If an edge of an object moves past the world bounds,
     * adjust its position to keep it completely on screen.
     */
    public void boundToWorld() {
        if (getX() < 0)
            setX(0);
        if (getX() + getWidth() > worldBounds.width)
            setX(worldBounds.width - getWidth());
        if (getY() < 0)
            setY(0);
        if (getY() + getHeight() > worldBounds.height)
            setY(worldBounds.height - getHeight());
    }

    // ----------------------------------------------
    // Instance list methods
    // ----------------------------------------------

    // ----------------------------------------------
    // Actor methods: act and draw
    // ----------------------------------------------

    /**
     * Processes all Actions and related code for this object;
     * automatically called by act method in Stage class.
     *
     * @param dt elapsed time (second) since last frame (supplied by Stage act method)
     */
    public void act(float dt) {
        super.act(dt);

        if (!animationPaused)
            elapsedTime += dt;
    }

    /**
     * Draws current frame of animation; automatically called by draw method in Stage class. <br>
     * If color has been set, image will be tinted by that color. <br>
     * If no animation has been set or object is invisible, nothing will be drawn.
     *
     * @param batch       (supplied by Stage draw method)
     * @param parentAlpha (supplied by Stage draw method)
     * @see #setColor
     * @see #setVisible
     */
    public void draw(Batch batch, float parentAlpha) {
        super.draw(batch, parentAlpha);

        // apply color tint effect
        Color c = getColor();
        batch.setColor(c.r, c.g, c.b, c.a);

        if (animation != null && isVisible())
            batch.draw(animation.getKeyFrame(elapsedTime),
                    getX(), getY(), getOriginX(), getOriginY(),
                    getWidth(), getHeight(), getScaleX(), getScaleY(), getRotation());
    }
}
