import java.awt.Graphics2D;
import java.awt.Color;
import java.awt.BasicStroke;
import java.security.SecureRandom;

public class BrickGenerator {
    public int brick[][];
    public int brickWidth, brickHeight;
    SecureRandom random = new SecureRandom();
    public Color color[][];

    public BrickGenerator(int row, int col) {
        brick = new int[row][col];
        color = new Color[row][col];

        for (int i = row - 1; i >= 0; --i)
            for (int j = col - 1; j >= 0; --j) {
                brick[i][j] = 1;
                color[i][j] = new Color(random.nextInt(256), random.nextInt(256), random.nextInt(256));
            }

        brickWidth = 540 / col;
        brickHeight = 150 / row;

    }

    public void removeBrick(int r, int c, int value) {
        brick[r][c] = value;
    }

    public void draw(Graphics2D g) {
        for (int i = 0; i < brick.length; ++i) {
            for (int j = 0; j < brick[0].length; ++j)
                if (brick[i][j] > 0) {

                    // building each brick
                    // g.setColor(new Color(random.nextInt(256), random.nextInt(256), random.nextInt(256)));
                    g.setColor(color[i][j]);
                    g.fillRect(j * brickWidth + 80, i * brickHeight + 50, brickWidth, brickHeight);

                    g.setColor(Color.black);
                    g.setStroke(new BasicStroke(3));
                    g.drawRect(j * brickWidth + 80, i * brickHeight + 50, brickWidth, brickHeight);
                }
        }
    }
}
