package objects;

import abstractions.GameObject;
import abstractions.ObjectId;
import abstractions.Texture;
import game.Main;

import java.awt.*;
import java.util.LinkedList;

public class Block extends GameObject {

    Texture texture = Main.getInstance();
    private int type;

    public Block(float x, float y, int type, ObjectId id) {
        super(x, y, id);
        this.type = type;
    }

    @Override
    public void tick(LinkedList<GameObject> object) {
    }

    @Override
    public void render(Graphics g) {
        if (type == 0)
            g.drawImage(texture.blocks[0], (int) x, (int) y, null);
        if (type == 1)
            g.drawImage(texture.blocks[1], (int) x, (int) y, null);
        if (type == 2)
            g.drawImage(texture.blocks[2], (int) x, (int) y, null);
    }

    @Override
    public Rectangle getBounds() {
        return new Rectangle((int) x, (int) y, 32, 32);
    }
}
