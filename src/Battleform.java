import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class Battleform {
    private JButton ExitButton;
    private JPanel battleform;
    private BattlePaint BatP;
    private Robot robot;
    public Battleform() {
        //BatP = new BattlePaint(2);
        GraphicsEnvironment env = GraphicsEnvironment.getLocalGraphicsEnvironment();
        GraphicsDevice screen = env.getDefaultScreenDevice();
        try {
            robot = new Robot(screen);
        } catch (AWTException ex){}

        ExitButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                robot.keyPress(KeyEvent.VK_ALT);
                robot.keyPress(KeyEvent.VK_F4);
                robot.keyRelease(KeyEvent.VK_F4);
                robot.keyRelease(KeyEvent.VK_ALT);
            }
        });
    }

    public JPanel getbattleform() {
        return battleform;
    }
}