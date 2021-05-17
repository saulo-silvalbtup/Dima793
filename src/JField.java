import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class JField {
    private JPanel jField;
    private JButton EndTurnButton;
    private JButton ExitButton;
    private paintTest Test;
    private JCheckBox AutomaticEndTurn;
    private JButton RefreshButton;
    private Robot robot;

    public JField() {

        GraphicsEnvironment env = GraphicsEnvironment.getLocalGraphicsEnvironment();
        GraphicsDevice screen = env.getDefaultScreenDevice();
        try {
            robot = new Robot(screen);
        } catch (AWTException ex){}
        ExitButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                System.exit(0);
//                robot.keyPress(KeyEvent.VK_ALT);
//                robot.keyPress(KeyEvent.VK_F4);
//                robot.keyRelease(KeyEvent.VK_F4);
//                robot.keyRelease(KeyEvent.VK_ALT);
            }
        });
        RefreshButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent r) {
                int i;
                Test.hero.setDefault(0, 0);
                Test.Ehero[0].setDefault(2, 17);
                Test.field.contain[342] = 1;
                Test.Ehero[1].setDefault(17, 18);
                Test.field.contain[377] = 2;
                Test.reset();
                Test.repaint();
            }
        });
        EndTurnButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent endTurn) {
                // Закончить ход
                paintTest.hero.currDistance = paintTest.hero.maxDistance;
                if (paintTest.hero.army[0].mana - paintTest.hero.army[0].currMana > 1) {
                    paintTest.hero.army[0].currMana += 2;
                }
                Test.moveIndicator = 0;
            }
        });
        AutomaticEndTurn.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent a) {
                Test.autoEndTurn = 1 - Test.autoEndTurn;
            }
        });

    }

    public JPanel getjField() {
        return jField;
    }
}
