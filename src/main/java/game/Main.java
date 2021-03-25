package game;

import abstractions.KeyInput;
import abstractions.ObjectId;
import abstractions.Texture;
import objects.Block;
import objects.Enemy;
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
    private static Texture texture;
    private BufferedImage clouds = null;
    private void init() {
        WIDTH = getWidth();
        HEIGHT = getHeight();

        texture = new Texture();

        BufferedImageLoader bufferedImageLoader = new BufferedImageLoader();
        BufferedImage level = bufferedImageLoader.loadImage("res/level.png");
//        clouds = bufferedImageLoader.loadImage("res/clouds.png");



        handler = new Handler();
        camera = new Camera(0, 0);
        loadImageLevel(level);


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

        g.drawImage(clouds, 0, 0, this);

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

        for (int xx = 0; xx < h; xx++) {
            for (int yy = 0; yy < w; yy++) {
                int pixel = image.getRGB(xx, yy);
                int red = (pixel >> 16) & 0xff;
                int green = (pixel >> 8) & 0xff;
                int blue = (pixel) & 0xff;

                if (red == 255 && green == 255 && blue == 255) {
                    handler.addObjects(new Block(xx * 32, yy * 32, 0,  ObjectId.Block));
                }
                if(red == 255 && green == 242 && blue == 0) {
                    handler.addObjects(new Enemy(xx * 32, yy * 32, handler, ObjectId.Enemy));
                }
                else if (red == 0 && green == 0 && blue == 255) {
                    handler.addObjects(new Player(xx * 32, yy * 32, handler, ObjectId.Player));
                } else if (red == 0 && green == 255 && blue == 0) {
                    handler.addObjects(new Block(xx * 32, yy * 32, 2, ObjectId.Block));
                } else if(red == 255 && green == 0 && blue == 0)
                    handler.addObjects(new Block(xx * 32, yy * 32, 1, ObjectId.Block));
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
