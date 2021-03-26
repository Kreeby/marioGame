package objects;

import abstractions.GameObject;
import abstractions.ObjectId;
import abstractions.Texture;
import game.Animation;
import game.Camera;
import game.Handler;
import game.Main;
import org.w3c.dom.Text;

import java.awt.*;
import java.util.LinkedList;

public class Player extends GameObject {
    private static final float GRAVITY = 0.5f;
    private static final float MAX_SPEED = 10;


    private final float WIDTH = 32;
    private final float HEIGHT = 64;

    private final float COLLISION_WIDTH = 5;

    private final Handler handler;

    Texture texture = Main.getInstance();
    private int facing = -1;

    private Animation playerWalk, playerWalkLeft;
    private Camera camera;

    public Player(float x, float y, Handler handler, Camera camera, ObjectId id) {
        super(x, y, id);
        this.handler = handler;
        this.camera = camera;

        playerWalk = new Animation(5, texture.player[1],
                texture.player[2], texture.player[3], texture.player[4]);

        playerWalkLeft = new Animation(5, texture.player[6],
                texture.player[7], texture.player[8], texture.player[9]);

    }

    @Override
    public void tick(LinkedList<GameObject> object) {
        x += velX;
        y += velY;

        if(velX < 0) facing = 1;
        else if (velX > 0) facing = -1;

        if (isFalling || isJumping) {
            velY += GRAVITY;
            if (velY > MAX_SPEED) {
                velY = MAX_SPEED;
            }
        }
        collision(object);

        playerWalk.runAnimation();
        playerWalkLeft.runAnimation();
    }

    private void collision(LinkedList<GameObject> objects) {
        for (int i = 0; i < handler.objects.size(); i++) {
            GameObject tempObject = handler.objects.get(i);
            if (tempObject.getId() == ObjectId.Block) {
                if (getBoundsTop().intersects(tempObject.getBounds())) {
                    y = tempObject.getY() + (HEIGHT / 2);
                    velY = 0;
                }
                if (getBounds().intersects(tempObject.getBounds())) {
                    y = tempObject.getY() - HEIGHT;
                    velY = 0;
                    isFalling = false;
                    isJumping = false;
                } else {
                    isFalling = true;
                }
                if (getBoundsRight().intersects(tempObject.getBounds())) {
                    x = tempObject.getX() - WIDTH;
                }

                if (getBoundsLeft().intersects(tempObject.getBounds())) {
                    x = tempObject.getX() + 35;
                }
            } else if (tempObject.getId() == ObjectId.Enemy) {
                if (getBounds().intersects(tempObject.getBounds())) {
                    y = tempObject.getY() - HEIGHT - 16;
                    velY = -10;
                    handler.removeObject(tempObject);
                }
            } else if (tempObject.getId() == ObjectId.Flag) {
                //switch the levels
                if(getBounds().intersects(tempObject.getBounds())) {
                    handler.switchLevel();
                }
            }
        }
    }

    @Override
    public void render(Graphics g) {
        if (velX != 0) {
            if (facing == 1)
                playerWalk.drawAnimation(g, (int) x, (int) y);
            else
                playerWalkLeft.drawAnimation(g, (int) x, (int) y);
        } else
            if (facing == 1) g.drawImage(texture.player[0], (int) x, (int) y, null);
            else if(facing == -1) g.drawImage(texture.player[5], (int) x, (int) y, null);
    }


    public Rectangle getBounds() {
        return new Rectangle((int) ((int) x + (WIDTH / 2) - ((WIDTH / 2) / 2)), (int) ((int) y + (HEIGHT / 2)), (int) WIDTH / 2, (int) HEIGHT / 2);
    }

    public Rectangle getBoundsTop() {
        return new Rectangle((int) ((int) x + (WIDTH / 2) - ((WIDTH / 2) / 2)), (int) y, (int) WIDTH / 2, (int) HEIGHT / 2);
    }

    public Rectangle getBoundsRight() {
        return new Rectangle((int) ((int) x + WIDTH - COLLISION_WIDTH), (int) ((int) y + COLLISION_WIDTH), (int) COLLISION_WIDTH, (int) HEIGHT - 10);
    }

    public Rectangle getBoundsLeft() {
        return new Rectangle((int) x, (int) ((int) y + COLLISION_WIDTH), (int) COLLISION_WIDTH, (int) HEIGHT - 10);
    }

}
