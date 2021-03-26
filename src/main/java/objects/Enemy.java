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
    private int type;

    public Enemy(float x, float y, int type, Handler handler, ObjectId id) {
        super(x, y, id);
        this.type = type;
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
                    x = tempObject.getX() - 35;
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
                    System.out.println("DUDE U DIED");
                }

                if(getBoundsLeft().intersects(tempObject.getBounds())) {
                    x = tempObject.getX() + 35;
                    System.out.println("DUDE U DIED");
                    isCollided = true;
                }
            }
        }
    }

    @Override
    public void render(Graphics g) {
        if(type == 3)
            g.drawImage(texture.blocks[3],(int) x, (int) y, null);
        Graphics2D g2d = (Graphics2D) g;
        g.setColor(Color.red);
        g2d.draw(getBounds());
        g2d.draw(getBoundsTop());
        g2d.draw(getBoundsRight());
        g2d.draw(getBoundsLeft());
    }


    public Rectangle getBounds() {
        return new Rectangle((int) ((int) x + (WIDTH / 2) - ((WIDTH / 2) / 2)), (int) ((int) y + (HEIGHT / 2)), (int) WIDTH / 2, (int) HEIGHT / 2);
    }

    public Rectangle getBoundsTop() {
        return new Rectangle((int) ((int) x + (WIDTH) - ((WIDTH))), (int) y, (int) WIDTH, (int) HEIGHT / 2);
    }

    public Rectangle getBoundsRight() {
        return new Rectangle((int) ((int) x + WIDTH - COLLISION_WIDTH) + 5, (int) ((int) y + COLLISION_WIDTH), (int) COLLISION_WIDTH, (int) HEIGHT - 10);
    }

    public Rectangle getBoundsLeft() {
        return new Rectangle((int) x - 5, (int) ((int) y + COLLISION_WIDTH), (int) COLLISION_WIDTH, (int) HEIGHT - 10);
    }
}
