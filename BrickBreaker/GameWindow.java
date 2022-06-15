import javax.swing.JFrame;

public class GameWindow {
    public static void main(String[] args) {

        // window frame
        JFrame frame = new JFrame();

        Gamepanel gamepanel = new Gamepanel();
        frame.add(gamepanel);
        
        frame.setTitle("Brick Breaker");
        frame.setSize(700, 600);
        frame.setLocationRelativeTo(null);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);
        frame.setResizable(false);
    }
}