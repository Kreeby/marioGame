package objects;

import abstractions.GameObject;
import abstractions.ObjectId;
import abstractions.Texture;
import game.Handler;
import game.Main;

import java.awt.*;
import java.util.LinkedList;

public class Enemy extends GameObject {
    private static final float GRAVITY = 0.5f;
    private static final float MAX_SPEED = 10;

    private final float WIDTH = 32;
    private final float HEIGHT = 32;

    private final float COLLISION_WIDTH = 5;

    private final Handler handler;

    private boolean isCollided = false;

    Texture texture = Main.getInstance();

    public Enemy(float x, float y, Handler handler, ObjectId id) {
        super(x, y, id);
        this.handler = handler;
        velX = -3;
    }

    @Override
    public void tick(LinkedList<GameObject> object) {

        if(isCollided) {
            velX = -velX;
            isCollided = false;
        }

        x += velX;
        y += velY;

        if (isFalling || isJumping) {
            velY += GRAVITY;
            if (velY > MAX_SPEED) {
                velY = MAX_SPEED;
            }
        }
        collision(object);
    }

    private void collision(LinkedList<GameObject> objects) {
        for(int i = 0; i < handler.objects.size(); i++) {
            GameObject tempObject = handler.objects.get(i);
            if(tempObject.getId() == ObjectId.Block) {

                if(getBoundsTop().intersects(tempObject.getBounds())) {
                    y = tempObject.getY() + (HEIGHT / 2);
                    velY = 0;
                }
                if(getBounds().intersects(tempObject.getBounds())) {
                    y = tempObject.getY() - HEIGHT;
                    velY = 0;
                    isFalling = false;
                    isJumping = false;
                } else {
                    isFalling = true;
                }

                if(getBoundsRight().intersects(tempObject.getBounds())) {
                    x = tempObject.getX() - WIDTH;
                    isCollided = true;
                }

                if(getBoundsLeft().intersects(tempObject.getBounds())) {
                    x = tempObject.getX() + 35;
                    isCollided = true;
                }
            }
            if(tempObject.getId() == ObjectId.Player) {
                if(getBoundsRight().intersects(tempObject.getBounds())) {
                    x = tempObject.getX() - WIDTH;
                    isCollided = true;
                }

                if(getBoundsLeft().intersects(tempObject.getBounds())) {
                    x = tempObject.getX() + 35;
                    isCollided = true;
                }
            }
        }
    }

    @Override
    public void render(Graphics g) {
        g.setColor(Color.BLACK);
        g.fillRect((int) x, (int) y, (int) WIDTH, (int) HEIGHT);
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
