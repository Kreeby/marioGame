package game;

import abstractions.KeyInput;
import abstractions.ObjectId;
import abstractions.Texture;

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
    public static int LEVEL = 1;
    public static int WIDTH;
    public static int HEIGHT;


    private boolean isRunning = false;
    private Handler handler;
    private Camera camera;
    private static Texture texture;
    private BufferedImage clouds = null;
    public BufferedImage level = null;
    private void init() {
        WIDTH = getWidth();
        HEIGHT = getHeight();

        texture = new Texture();

        BufferedImageLoader bufferedImageLoader = new BufferedImageLoader();
        level = bufferedImageLoader.loadImage("res/level.png");
        clouds = bufferedImageLoader.loadImage("res/clouds.png");


        camera = new Camera(0, 0);

        handler = new Handler(camera);
        handler.loadImageLevel(level);


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

        g.setColor(Color.CYAN);
        g.fillRect(0, 0, getWidth(), getHeight());


        int width;
        graphics2D.translate(camera.getX(), camera.getY()); // beginning of camera
            for (int i = 0; i < clouds.getWidth() * 5; i+= clouds.getWidth()) {
                width = i + 100;
                g.drawImage(clouds, i + width * 4, 100, this);
            }
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





    public static Texture getInstance() {
        return texture;
    }

    public static void main(String[] args) {
        new Board(WINDOW_WIDTH, WINDOW_HEIGHT, "Rashad's Mario game", new Main());
    }


}
