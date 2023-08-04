package client.pane;

import commons.api.GameApi;

import javax.swing.JPanel;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;

public class GameBoard extends JPanel {
    GameApi api;

    public GameBoard(GameApi api) {
        this.api = api;
        this.setPreferredSize(new Dimension(460, 460));

    }

    /**
     * Get location
     *
     * @param x
     * @param y
     * @return
     */
    private int[] getPosition(int x, int y) {
        int newX = x * 50 + x + 1;
        int newY = y * 50 + y + 1;
        return new int[]{newX, newY};
    }


    @Override
    public void paint(Graphics g) {
        try {
            super.paint(g);
            g.setColor(Color.BLACK);
            g.fillRect(0, 0, 460, 460);
            for (int i = 0; i < 9; i++) {
                for (int j = 0; j < 9; j++) {
                    int[] position = getPosition(i, j);
                    g.setColor(Color.decode("#C7B68D"));
                    g.fillRect(position[0], position[1], 50, 50);
                    switch (api.getGameGrids()[j][i]) {
                        case 0:
                            g.setColor(Color.decode("#C7B68D"));
                            break;
                        case 1:
                            g.setColor(Color.red);
                            break;
                        case 2:
                            g.setColor(Color.white);
                            break;
                    }
                    if (api.getCrownX() == i && api.getCrownY() == j) {
                        g.setColor(Color.yellow);
                        g.fillOval(position[0] + 10, position[1] + 10, 30, 30);
                    } else {
                        g.fillOval(position[0] + 10, position[1] + 10, 30, 30);
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
