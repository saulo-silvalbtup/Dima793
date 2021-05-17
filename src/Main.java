import java.awt.*;
import javax.swing.*;
public class Main {

    public static void main(String[] args) {
        JFrame frame = new JFrame();
        //frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        JField Field1 = new JField();
        frame.setContentPane(Field1.getjField());
        frame.pack();
        frame.setSize(600, 600);
        frame.setExtendedState(Frame.MAXIMIZED_BOTH);
        frame.dispose();
        frame.setUndecorated(true);
        frame.setVisible(true);
    }
}