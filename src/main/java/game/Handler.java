package game;

import abstractions.GameObject;
import abstractions.ObjectId;
import objects.Block;
import objects.Enemy;
import objects.Flag;
import objects.Player;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.LinkedList;

public class Handler {
    public LinkedList<GameObject> objects = new LinkedList<>();
    private GameObject tempObject;
    private static final int CONSTANT_NUMBER = 32;
    private Camera camera;
    private final BufferedImage level2;

    public Handler(Camera camera) {
        this.camera = camera;
        BufferedImageLoader bufferedImageLoader = new BufferedImageLoader();
        level2 = bufferedImageLoader.loadImage("res/level2.png");
    }

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

    private void clearLevel() {
        objects.clear();
    }

    public void loadImageLevel(BufferedImage image) {
        int w = image.getWidth();
        int h = image.getHeight();

        for (int xx = 0; xx < h; xx++) {
            for (int yy = 0; yy < w; yy++) {
                int pixel = image.getRGB(xx, yy);
                int red = (pixel >> 16) & 0xff;
                int green = (pixel >> 8) & 0xff;
                int blue = (pixel) & 0xff;

                if (red == 255 && green == 255 && blue == 255) {
                    addObjects(new Block(xx * 32, yy * 32, 0,  ObjectId.Block));
                }
                if(red == 255 && green == 242 && blue == 0) {
                    addObjects(new Enemy(xx * 32, yy * 32, 3, this, ObjectId.Enemy));
                }
                if (red == 0 && green == 0 && blue == 255) {
                    addObjects(new Player(xx * 32, yy * 32, this, camera, ObjectId.Player));
                }
                if (red == 0 && green == 255 && blue == 0) {
                    addObjects(new Block(xx * 32, yy * 32, 2, ObjectId.Block));
                }
                if(red == 255 && green == 0 && blue == 0) {
                    addObjects(new Block(xx * 32, yy * 32, 1, ObjectId.Block));
                }
                if(red == 63 && green == 72 && blue == 204) {
                    addObjects(new Flag(xx * 32, yy * 32, ObjectId.Flag));
                }
            }
        }
    }

    public void switchLevel() {
        clearLevel();
        camera.setX(0);
        camera.setY(0);


        switch (Main.LEVEL) {
            case 1:
                loadImageLevel(level2);
                break;
        }
        Main.LEVEL++;
    }
}
