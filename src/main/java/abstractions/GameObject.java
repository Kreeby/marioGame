package abstractions;

import java.awt.*;
import java.util.LinkedList;

public abstract class GameObject {

    protected float x, y;
    protected ObjectId id;
    protected float velX = 0, velY = 0;
    protected boolean isFalling = true;
    protected boolean isJumping = false;

    public GameObject(float x, float y, ObjectId id) {
        this.x = x;
        this.y = y;
        this.id = id;
    }

    public abstract void tick(LinkedList<GameObject> object);
    public abstract void render(Graphics g);
    public abstract Rectangle getBounds();

    public float getX() {
        return x;
    }

    public void setX(float x) {
        this.x = x;
    }

    public float getY() {
        return y;
    }

    public void setY(float y) {
        this.y = y;
    }

    public ObjectId getId() {
        return id;
    }

    public float getVelX() {
        return velX;
    }

    public void setVelX(float velX) {
        this.velX = velX;
    }

    public float getVelY() {
        return velY;
    }

    public void setVelY(float velY) {
        this.velY = velY;
    }

    public boolean isFalling() {
        return isFalling;
    }

    public void setFalling(boolean falling) {
        isFalling = falling;
    }

    public boolean isJumping() {
        return isJumping;
    }

    public void setJumping(boolean jumping) {
        isJumping = jumping;
    }
}
