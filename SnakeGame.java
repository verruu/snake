import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.Random;
import javax.swing.ImageIcon;
import javax.swing.JPanel;
import javax.swing.Timer;

public class SnakeGame extends JPanel {

    private final int B_WIDTH = 300;
    private final int B_HEIGHT = 300;
    private final int DOT_SIZE = 10;
    private final int ALL_DOTS = 900;
    private final int RAND_POS = 29;
    private final int DELAY = 140;

    private final int[] X = new int[ALL_DOTS];
    private final int[] Y = new int[ALL_DOTS];

    private int dots;
    private int apple_x;
    private int apple_y;

    private boolean leftDirection = false;
    private boolean rightDirection = true;
    private boolean upDirection = false;
    private boolean downDirection = false;
    private boolean inGame = true;

    private Timer timer;
    private Image ball;
    private Image apple;
    private Image head;

    public SnakeGame() {
        initBoard();
    }

    private void initBoard() {
        setPreferredSize(new Dimension(B_WIDTH, B_HEIGHT));
        setBackground(Color.BLACK);
        setFocusable(true);
        addKeyListener(new SnakeKeyListener());
        loadImages();
        initGame();
    }

    private void loadImages() {
        ImageIcon iid = new ImageIcon(getClass().getResource("/resources/dot.png"));
        ball = iid.getImage();

        ImageIcon iia = new ImageIcon(getClass().getResource("/resources/apple.png"));
        apple = iia.getImage();

        ImageIcon iih = new ImageIcon(getClass().getResource("/resources/head.png"));
        head = iih.getImage();
    }

    private void initGame() {
        dots = 3;

        for (int z = 0; z < dots; z++) {
            X[z] = 50 - z * 10;
            Y[z] = 50;
        }

        locateApple();

        timer = new Timer(DELAY, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (inGame) {
                    checkApple();
                    checkCollision();
                    move();
                }
                repaint();
            }
        });
        timer.start();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        doDrawing(g);
    }

    private void doDrawing(Graphics g) {
        if (inGame) {
            g.setColor(Color.GRAY);
            g.fillRect(0, 0, B_WIDTH, B_HEIGHT);
            g.drawImage(apple, apple_x, apple_y, this);

            for (int z = 0; z < dots; z++) {
                if (z == 0) {
                    g.setColor(Color.GREEN);
                    g.drawImage(head, X[z], Y[z], this);
                } else {
                    g.setColor(Color.WHITE);
                    g.drawImage(ball, X[z], Y[z], this);
                }
            }
        } else {
            gameOver(g);
        }
    }

    private void gameOver(Graphics g) {
        String msg = "Game Over";
        Font small = new Font("Helvetica", Font.BOLD, 14);
        FontMetrics metr = getFontMetrics(small);

        g.setColor(Color.WHITE);
        g.setFont(small);
        g.drawString(msg, (B_WIDTH - metr.stringWidth(msg)) / 2, B_HEIGHT / 2);
    }

    private void checkApple() {
        if ((X[0] == apple_x) && (Y[0] == apple_y)) {
            dots++;
            locateApple();
        }
    }

    private void move() {
        for (int z = dots; z > 0; z--) {
            X[z] = X[(z - 1)];
            Y[z] = Y[(z - 1)];
        }

        if (leftDirection) {
            X[0] -= DOT_SIZE;
        }

        if (rightDirection) {
            X[0] += DOT_SIZE;
        }

        if (upDirection) {
            Y[0] -= DOT_SIZE;
        }

        if (downDirection) {
            Y[0] += DOT_SIZE;
        }
    }

    private void checkCollision() {
        for (int z = dots; z > 0; z--) {
            if ((z > 4) && (X[0] == X[z]) && (Y[0] == Y[z])) {
                inGame = false;
                break;
            }
        }

        if (Y[0] >= B_HEIGHT) {
            inGame = false;
        }

        if (Y[0] < 0) {
            inGame = false;
        }

        if (X[0] >= B_WIDTH) {
            inGame = false;
        }

        if (X[0] < 0) {
            inGame = false;
        }

        if (!inGame) {
            timer.stop();
        }
    }

    private void locateApple() {
        Random random = new Random();
        apple_x = random.nextInt(RAND_POS) * DOT_SIZE;
        apple_y = random.nextInt(RAND_POS) * DOT_SIZE;
    }

    private class SnakeKeyListener implements KeyListener {
        @Override
        public void keyPressed(KeyEvent e) {
            int key = e.getKeyCode();

            if ((key == KeyEvent.VK_LEFT) && (!rightDirection)) {
                leftDirection = true;
                upDirection = false;
                downDirection = false;
            }

            if ((key == KeyEvent.VK_RIGHT) && (!leftDirection)) {
                rightDirection = true;
                upDirection = false;
                downDirection = false;
            }

            if ((key == KeyEvent.VK_UP) && (!downDirection)) {
                upDirection = true;
                rightDirection = false;
                leftDirection = false;
            }

            if ((key == KeyEvent.VK_DOWN) && (!upDirection)) {
                downDirection = true;
                rightDirection = false;
                leftDirection = false;
            }
        }

        @Override
        public void keyTyped(KeyEvent e) {
        }

        @Override
        public void keyReleased(KeyEvent e) {
        }
    }
}
