import javax.swing.JPanel;
import javax.swing.Timer;

import java.awt.Rectangle;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Font;
import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class Gamepanel extends JPanel implements ActionListener, KeyListener {
    // gamestart boolean
    private boolean play = false;
    private boolean pause = false;

    private int score = 0;
    private int highestScore = 0;

    private int brickRows = 4;
    private int brickCols = 8; // bricks in each row
    private int totalBricks = brickRows * brickCols;

    private Timer timer;
    private int delay = 8;

    // ball properites
    private int ballposX = 200;
    private int ballposY = 400;
    private int ballXdir = 2;
    private int ballYdir = -5;

    // paddle properties
    private int paddleX = 300;
    private int paddleY = 540;

    private BrickGenerator brickObj;

    public Gamepanel() {
        addKeyListener(this);
        setFocusable(true);
        setFocusTraversalKeysEnabled(true);

        timer = new Timer(delay, this);
        timer.start();

        brickObj = new BrickGenerator(brickRows, brickCols);
    }

    // inbuilt method of every component
    public void paint(Graphics g) {
        // background color
        g.setColor(Color.black);
        g.fillRect(0, 0, 692, 592);

        // border
        g.setColor(Color.yellow);
        g.fillRect(0, 0, 692, 3);
        g.fillRect(0, 0, 3, 692);
        g.fillRect(680, 0, 3, 592);

        // paddle
        g.setColor(Color.green);
        g.fillRect(paddleX, paddleY, 100, 8);

        // ball
        g.setColor(Color.red);
        g.fillOval(ballposX, ballposY, 20, 20);

        // bricks
        brickObj.draw((Graphics2D) g);

        // pause
        if (!pause) {
            g.setColor(Color.green);
            g.setFont(new Font("serif", Font.BOLD, 20));
            g.drawString("Pause : ", 30, 25);
            g.fillRect(100, 10, 3, 15);
            g.fillRect(108, 10, 3, 15);
        }

        if (pause) {
            g.setColor(Color.green);
            g.setFont(new Font("serif", Font.BOLD, 20));
            g.drawString("Continue : ", 30, 25);
            // g.fillRect(150, 10, 3, 15);
            int x[] = { 120, 140, 120 };
            int y[] = { 10, 19, 28 };

            g.fillPolygon(x, y, 3);
        }

        // score
        g.setColor(Color.green);
        g.setFont(new Font("serif", Font.BOLD, 20));
        g.drawString("Highest Score : " + highestScore, 480, 25);
        g.drawString("Score : " + score, 550, 45);

        // First Start of Game
        if (totalBricks == brickRows * brickCols && highestScore == 0 && score == 0 && !play && !pause) {
            g.setColor(Color.green);
            g.setFont(new Font("serif", Font.BOLD, 45));
            g.drawString("Press Enter to Start!!", 140, 290);
        }

        // Gameover
        if (ballposY > 540) {
            play = false;
            ballXdir = 0;
            ballYdir = 0;
            ballposY = 610;

            g.setColor(Color.green);
            g.setFont(new Font("serif", Font.BOLD, 40));
            g.drawString("GameOver!!", 220, 280);
            g.drawString("Score : " + score, 230, 320);

            g.setFont(new Font("serif", Font.BOLD, 30));
            g.drawString("Press Enter to Restart!!", 180, 360);
        }

        // player Won
        if (totalBricks <= 0) {
            play = false;
            ballXdir = 0;
            ballYdir = 0;

            g.setColor(Color.green);
            g.setFont(new Font("serif", Font.BOLD, 40));
            g.drawString("You Won!!", 220, 280);
            g.drawString("Score : " + score, 220, 320);

            g.setFont(new Font("serif", Font.BOLD, 30));
            g.drawString("Press Enter to Restart!!", 170, 360);
        }

        g.dispose();
    }

    private void moveLeft() {
        paddleX -= 20;
    }

    private void moveRight() {
        paddleX += 20;
    }

    @Override
    public void keyPressed(KeyEvent e) {

        if (e.getKeyCode() == KeyEvent.VK_SPACE) {
            if (play) {
                play = false;
                pause = true;
            } else {
                play = true;
                pause = false;
            }
        }

        if (e.getKeyCode() == KeyEvent.VK_LEFT) {
            if (paddleX <= 3)
                paddleX = 3;
            else
                moveLeft();
        }
        if (e.getKeyCode() == KeyEvent.VK_RIGHT) {
            if (paddleX >= 578)
                paddleX = 578;
            else
                moveRight();
        }

        // First Start of Game
        if (e.getKeyCode() == KeyEvent.VK_ENTER) {
            if (totalBricks == brickRows * brickCols && highestScore == 0 && score == 0 && !play)
                play = true;

            else if (!play) {
                play = true;
                score = 0;
                totalBricks = brickRows * brickCols;
                ballposX = 200;
                ballposY = 400;
                ballXdir = 2;
                ballYdir = -5;
                paddleX = 300;
                brickObj = new BrickGenerator(brickRows, brickCols);
            }
        }
        repaint();
    }

    @Override
    public void actionPerformed(ActionEvent e) {

        if (play) {

            // checking for left Boundary
            if (ballposX <= 3)
                ballXdir = -ballXdir;

            // checking for right Boundary
            if (ballposX >= 659)
                ballXdir = -ballXdir;

            // checking for upper Boundary
            if (ballposY <= 3)
                ballYdir = -ballYdir;

            // checking for intersection of paddle & ball
            Rectangle ballRect = new Rectangle(ballposX, ballposY, 20, 20);
            Rectangle paddleRect = new Rectangle(paddleX, paddleY, 100, 8);

            if (ballRect.intersects(paddleRect))
                ballYdir = -ballYdir;

            // checking for intersection of bricks & ball
            int width = brickObj.brickWidth;
            int height = brickObj.brickHeight;

            A: for (int i = 0; i < brickObj.brick.length; ++i)
                for (int j = 0; j < brickObj.brick[0].length; ++j)
                    if (brickObj.brick[i][j] > 0) {

                        int brickposX = j * width + 80;
                        int brickposY = i * height + 50;

                        Rectangle brickRect = new Rectangle(brickposX, brickposY, width, height);

                        if (ballRect.intersects(brickRect)) {
                            brickObj.removeBrick(i, j, 0);
                            --totalBricks;
                            score = score + 10;
                            highestScore = score > highestScore ? score : highestScore;

                            if (ballposX + 20 <= brickposX || ballposX + 1 >= brickposX + width)
                                ballXdir = -ballXdir;
                            else
                                ballYdir = -ballYdir;

                            break A;
                        }
                    }

            ballposX += ballXdir;
            ballposY += ballYdir;
        }
        repaint();
    }

    @Override
    public void keyReleased(KeyEvent e) {
    }

    @Override
    public void keyTyped(KeyEvent e) {
    }

}