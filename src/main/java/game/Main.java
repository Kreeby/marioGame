package game;

import abstractions.KeyInput;
import abstractions.ObjectId;
import objects.Block;
import objects.Player;

import java.awt.*;
import java.awt.image.BufferStrategy;
import java.awt.image.BufferedImage;


public class Main extends Canvas implements Runnable {
    private static final int NUMBER_OF_BUFFERS = 3;
    private static final int WINDOW_WIDTH = 800;
    private static final int WINDOW_HEIGHT = 600;
    private static final double AMOUNT_OF_TICKS = 60.0;
    private static final int MAX_NUMBER = 1_000_000_000;
    private static final int MAX_TIMER = 1_000;
    private static final int STATIC_DELTA = 1;

    public static int WIDTH;
    public static int HEIGHT;

    private boolean isRunning = false;
    private Handler handler;
    private Camera camera;

    private void init() {
        WIDTH = getWidth();
        HEIGHT = getHeight();

        BufferedImageLoader bufferedImageLoader = new BufferedImageLoader();
        BufferedImage level = bufferedImageLoader.loadImage("res/leveltest.jpg");

        handler = new Handler();
        camera = new Camera(0, 0);
        loadImageLevel(level);

        handler.addObjects(new Player(100, 100, handler, ObjectId.Player));
        handler.createLevel();

        this.addKeyListener(new KeyInput(handler));
    }


    public synchronized void start() {
        if (isRunning)
            return;

        isRunning = true;
        Thread thread = new Thread(this);
        thread.start();
    }

    @Override
    public void run() {
        init();
        this.requestFocus();
        long lastTime = System.nanoTime();
        double ns = MAX_NUMBER / AMOUNT_OF_TICKS;
        double delta = 0;
        long timer = System.currentTimeMillis();
        int updates = 0;
        int frames = 0;
        while (isRunning) {
            long now = System.nanoTime();
            delta += (now - lastTime) / ns;
            lastTime = now;
            while (delta >= STATIC_DELTA) {
                tick();
                updates++;
                delta--;
            }
            render();
            frames++;

            if (System.currentTimeMillis() - timer > MAX_TIMER) {
                timer += MAX_TIMER;
                System.out.println("FPS: " + frames + " TICKS: " + updates);
                frames = 0;
                updates = 0;
            }
        }
    }

    private void render() {
        BufferStrategy bs = this.getBufferStrategy();
        if (bs == null) {
            this.createBufferStrategy(NUMBER_OF_BUFFERS); // for uploading the images with preloading it in buffer
            return;
        }

        Graphics g = bs.getDrawGraphics();
        Graphics2D graphics2D = (Graphics2D) g;
        ////

        // Draw here

        g.setColor(Color.BLACK);
        g.fillRect(0, 0, getWidth(), getHeight());

        graphics2D.translate(camera.getX(), camera.getY()); // beginning of camera
        handler.render(g);
        graphics2D.translate(-camera.getX(), -camera.getY()); // end of camera
        ////

        g.dispose();
        bs.show();
    }

    private void tick() {
        handler.tick();
        for (int i = 0; i < handler.objects.size(); i++) {
            if (handler.objects.get(i).getId() == ObjectId.Player) {
                camera.tick(handler.objects.get(i));
            }
        }
    }

    private void loadImageLevel(BufferedImage image) {
        int w = image.getWidth();
        int h = image.getHeight();

        for (int i = 0; i < h; i++) {
            for (int j = 0; j < w; j++) {
                int pixel = image.getRGB(i, j);
                int red = (pixel >> 16) & 0xff;
                int green = (pixel >> 8) & 0xff;
                int blue = (pixel) & 0xff;

                if (red == 255 && green == 255 && blue == 255) {
                    handler.addObjects(new Block(i * 32, j * 32, ObjectId.Block));
                }
                if (red == 0 && green == 0 && blue == 255) {
                    handler.addObjects(new Player(i * 32, j * 32, handler, ObjectId.Player));
                }
            }
        }
    }

    public static void main(String[] args) {
        new Board(WINDOW_WIDTH, WINDOW_HEIGHT, "Rashad's Mario game", new Main());
    }


}
