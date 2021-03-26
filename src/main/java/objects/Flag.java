package objects;

import abstractions.GameObject;
import abstractions.ObjectId;

import java.awt.*;
import java.util.LinkedList;

public class Flag extends GameObject {

    private final float WIDTH = 32;
    private final float HEIGHT = 32;

    public Flag(float x, float y, ObjectId id) {
        super(x, y, id);
    }

    @Override
    public void tick(LinkedList<GameObject> object) {

    }

    @Override
    public void render(Graphics g) {
        g.setColor(Color.RED);
        g.fillRect((int) x, (int) y, (int) WIDTH, (int) HEIGHT);
    }

    @Override
    public Rectangle getBounds() {
        return new Rectangle((int) x, (int) y, (int) WIDTH, (int) HEIGHT);
    }
}
