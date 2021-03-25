package game;

import javax.swing.*;
import java.awt.*;

public class Board {

    public Board(int width, int height, String title, Main main) {
        main.setPreferredSize(new Dimension(width, height));
        main.setMaximumSize(new Dimension(width, height));
        main.setMinimumSize(new Dimension(width, height));

        JFrame jFrame = new JFrame(title);
        jFrame.add(main);
        jFrame.pack();
        jFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        jFrame.setResizable(false);
        jFrame.setLocationRelativeTo(null);
        jFrame.setVisible(true);


        main.start();

    }
}
