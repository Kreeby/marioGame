package game;

import abstractions.GameObject;
import abstractions.ObjectId;
import objects.Block;

import java.awt.*;
import java.util.LinkedList;

public class Handler {
    public LinkedList<GameObject> objects = new LinkedList<>();
    private GameObject tempObject;
    private static final int CONSTANT_NUMBER = 32;

    public void tick() {
        for (int i = 0; i < objects.size(); i++) {
            tempObject = objects.get(i);
            tempObject.tick(objects);
        }
    }

    public void render(Graphics g) {
        for (GameObject object : objects) {
            tempObject = object;
            tempObject.render(g);
        }
    }

    public void addObjects(GameObject object) {
        this.objects.add(object);
    }

    public void removeObject(GameObject object) {
        this.objects.remove(object);
    }
}
