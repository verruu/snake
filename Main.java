import javax.swing.*;

public class Main {

    public static void main(String[] args) {
        
        JFrame frame = new JFrame();
        frame.add(new SnakeGame());
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        frame.setTitle("Snake Game");
        frame.pack();
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }
}
