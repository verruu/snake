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
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.Timer;

public class SnakeGame extends JPanel {

    private static final int BOARD_WIDTH = 300;
    private static final int BOARD_HEIGHT = 300;
    private static final int DOT_SIZE = 10;
    private static final int ALL_DOTS = (BOARD_WIDTH * BOARD_HEIGHT) / (DOT_SIZE * DOT_SIZE);
    private static final int RAND_POS = BOARD_WIDTH / DOT_SIZE;
    private static final int DELAY = 140;

    private final int[] X = new int[ALL_DOTS];
    private final int[] Y = new int[ALL_DOTS];

    private int dots;
    private int appleX;
    private int appleY;
    private int foodEaten; // Counter for food eaten by the snake

    private boolean leftDirection = false;
    private boolean rightDirection = true;
    private boolean upDirection = false;
    private boolean downDirection = false;
    private boolean inGame = true;

    private Timer timer;
    private transient Image ball;
    private transient Image apple;
    private transient Image head;

    public SnakeGame() {
        initBoard();
    }

    private void initBoard() {
        setPreferredSize(new Dimension(BOARD_WIDTH, BOARD_HEIGHT));
        setBackground(Color.BLACK);
        setFocusable(true);
        addKeyListener(new SnakeKeyListener());
        loadImages();
        initGame();
    }

    private void loadImages() {
        try {
            ImageIcon iid = new ImageIcon(getClass().getResource("/resources/dot.png"));
            ball = iid.getImage();

            ImageIcon iia = new ImageIcon(getClass().getResource("/resources/apple.png"));
            apple = iia.getImage();

            ImageIcon iih = new ImageIcon(getClass().getResource("/resources/head.png"));
            head = iih.getImage();
        } catch (Exception e) {
            System.err.println("Failed to load image resources: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private void initGame() {
        dots = 3;
        foodEaten = 0; // Initialize foodEaten counter to 0

        for (int z = 0; z < dots; z++) {
            X[z] = 50 - z * DOT_SIZE;
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
            g.setColor(Color.BLACK);
            g.fillRect(0, 0, BOARD_WIDTH, BOARD_HEIGHT);
            g.drawImage(apple, appleX, appleY, this);

            for (int z = 0; z < dots; z++) {
                if (z == 0) {
                    g.setColor(Color.GREEN);
                    g.drawImage(head, X[z], Y[z], this);
                } else {
                    g.setColor(Color.WHITE);
                    g.drawImage(ball, X[z], Y[z], this);
                }
            }

            // Draw foodEaten counter in the upper right corner
            g.setColor(Color.WHITE);
            g.setFont(new Font("Helvetica", Font.BOLD, 14));
            g.drawString("" + foodEaten, BOARD_WIDTH - 15, 15);
        } else {
            gameOver(g);
        }
    }

    private void gameOver(Graphics g) {
        String msg = "Game Over. Press 'R' to restart or 'Q' to quit.";
        Font small = new Font("Helvetica", Font.BOLD, 14);
        FontMetrics metr = getFontMetrics(small);

        g.setColor(Color.WHITE);
        g.setFont(small);
        g.drawString(msg, (BOARD_WIDTH - metr.stringWidth(msg)) / 2, BOARD_HEIGHT / 2);
    }

    private void checkApple() {
        if ((X[0] == appleX) && (Y[0] == appleY)) {
            dots++;
            foodEaten++; // Increment foodEaten counter
            locateApple();
        }
    }

    private void checkCollision() {
        for (int z = dots; z > 0; z--) {
            if ((z > 4) && (X[0] == X[z]) && (Y[0] == Y[z])) {
                inGame = false;
                break;
            }
        }

        if (Y[0] >= BOARD_HEIGHT) {
            inGame = false;
        }

        if (Y[0] < 0) {
            inGame = false;
        }

        if (X[0] >= BOARD_WIDTH) {
            inGame = false;
        }

        if (X[0] < 0) {
            inGame = false;
        }

        if (!inGame) {
            timer.stop();
            int choice = JOptionPane.showOptionDialog(null, "Game Over. Do you want to restart?", "Game Over", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE, null, null, null);
            if (choice == JOptionPane.YES_OPTION) {
                restartGame();
            } else {
                System.exit(0);
            }
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

    private void restartGame() {
        inGame = true;
        dots = 3;
        foodEaten = 0;
        leftDirection = false;
        rightDirection = true;
        upDirection = false;
        downDirection = false;
        for (int z = 0; z < dots; z++) {
            X[z] = 50 - z * DOT_SIZE;
            Y[z] = 50;
        }
        locateApple();
        timer.start();
    }

    private void locateApple() {
        Random random = new Random();
        appleX = random.nextInt(RAND_POS) * DOT_SIZE;
        appleY = random.nextInt(RAND_POS) * DOT_SIZE;
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

            if ((key == KeyEvent.VK_R) && (!inGame)) {
                restartGame();
            }

            if ((key == KeyEvent.VK_Q) && (!inGame)) {
                System.exit(0);
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
