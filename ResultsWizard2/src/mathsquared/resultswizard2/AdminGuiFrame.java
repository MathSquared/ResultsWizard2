package mathsquared.resultswizard2;

import java.awt.BorderLayout;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

/**
 * Runs the GUI by which an event administrator can input results.
 * 
 * @author MathSquared
 * 
 */
public class AdminGuiFrame extends JFrame {

    private ObjectInputStream ois;
    private ObjectOutputStream oos;

    private JPanel contentPane;

    /**
     * Create the frame.
     */
    public AdminGuiFrame (ObjectInputStream ois, ObjectOutputStream oos) {
        // Initialize comms
        this.oos = oos;
        this.ois = ois;

        setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
        setBounds(100, 100, 450, 300);
        contentPane = new JPanel();
        contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
        contentPane.setLayout(new BorderLayout(0, 0));
        setContentPane(contentPane);
    }

}
