package abstractions;

import game.BufferedImageLoader;

import java.awt.image.BufferedImage;

public class Texture {

    private SpriteSheet bs, ps;
    private BufferedImage blockSheet = null;
    private BufferedImage playerSheet = null;

    public BufferedImage[] blocks = new BufferedImage[4];
    public BufferedImage[] player = new BufferedImage[10];
    public Texture() {

        BufferedImageLoader bufferedImageLoader = new BufferedImageLoader();
        try {
            blockSheet = bufferedImageLoader.loadImage("res/sprite_sheet.png");
            playerSheet = bufferedImageLoader.loadImage("res/player_spritesheet_upd.png");
        } catch (Exception e) {
            e.printStackTrace();
        }

        this.bs = new SpriteSheet(blockSheet);
        this.ps = new SpriteSheet(playerSheet);

        getTextures();
    }

    private void getTextures() {
        blocks[0] = bs.grabImage(1, 1, 32, 32); // area block
        blocks[1] = bs.grabImage(2, 1, 32, 32); // tubes block
        blocks[2] = bs.grabImage(3, 1, 32, 32); // coin box block
        blocks[3] = bs.grabImage(1, 2, 32, 32); // enemy block


        //left
        player[0] = ps.grabImage(1, 1, 32, 64); // player
        player[1] = ps.grabImage(2, 1, 32, 64); // player
        player[2] = ps.grabImage(3, 1, 32, 64); // player
        player[3] = ps.grabImage(4, 1, 32, 64); // player
        player[4] = ps.grabImage(5, 1, 32, 64); // player

        //right
        player[5] = ps.grabImage(1, 2, 32, 64); // player
        player[6] = ps.grabImage(2, 2, 32, 64); // player
        player[7] = ps.grabImage(3, 2, 32, 64); // player
        player[8] = ps.grabImage(4, 2, 32, 64); // player
        player[9] = ps.grabImage(5, 2, 32, 64); // player

    }
}
