package mathsquared.resultswizard2;

import java.awt.BorderLayout;
import java.awt.Component;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import javax.swing.border.BevelBorder;
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
        setTitle("Results Wizard");
        // Initialize comms
        this.oos = oos;
        this.ois = ois;

        setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
        setBounds(100, 100, 450, 300);
        contentPane = new JPanel();
        contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
        contentPane.setLayout(new BorderLayout(0, 0));
        setContentPane(contentPane);

        JTabbedPane tabbedPane = new JTabbedPane(JTabbedPane.TOP);
        contentPane.add(tabbedPane, BorderLayout.CENTER);

        // Contains configuration options, such as setting a color scheme
        JPanel configPanel = new JPanel();
        tabbedPane.addTab("Configuration", null, configPanel, null);
        configPanel.setLayout(new BoxLayout(configPanel, BoxLayout.Y_AXIS));

        Component configFirstVerticalStrut = Box.createVerticalStrut(20);
        configPanel.add(configFirstVerticalStrut);

        // Allows the user to input an event configuration file
        JButton configBtnSelectEventConfiguration = new JButton("Select Event Configuration...");
        configBtnSelectEventConfiguration.setAlignmentX(Component.CENTER_ALIGNMENT);
        configPanel.add(configBtnSelectEventConfiguration);

        Component configSecondVerticalStrut = Box.createVerticalStrut(10);
        configPanel.add(configSecondVerticalStrut);

        // Allows the user to input a color scheme for use in all new slides
        JButton configBtnSelectColorScheme = new JButton("Select Color Scheme...");
        configBtnSelectColorScheme.setToolTipText("Will NOT affect previously sent slides");
        configBtnSelectColorScheme.setAlignmentX(Component.CENTER_ALIGNMENT);
        configPanel.add(configBtnSelectColorScheme);

        // Contains a GUI for inputting event results and manipulating the projection
        // inputPanel contains only layout border elements
        JPanel inputPanel = new JPanel();
        tabbedPane.addTab("Input Results", null, inputPanel, null);
        inputPanel.setLayout(new BorderLayout(0, 0));

        Component inputTopStrut = Box.createVerticalStrut(20);
        inputPanel.add(inputTopStrut, BorderLayout.NORTH);

        Component inputBottomStrut = Box.createVerticalStrut(20);
        inputPanel.add(inputBottomStrut, BorderLayout.SOUTH);

        Component inputLeftStrut = Box.createHorizontalStrut(10);
        inputPanel.add(inputLeftStrut, BorderLayout.WEST);

        Component inputRightStrut = Box.createHorizontalStrut(10);
        inputPanel.add(inputRightStrut, BorderLayout.EAST);

        // Contains the actual GUI elements
        JPanel inputInteriorPanel = new JPanel();
        inputPanel.add(inputInteriorPanel, BorderLayout.CENTER);
        inputInteriorPanel.setLayout(new BorderLayout(10, 0));

        // Contains buttons for manipulating inputList below
        JPanel inputButtonPanel = new JPanel();
        inputInteriorPanel.add(inputButtonPanel, BorderLayout.EAST);
        inputButtonPanel.setLayout(new BoxLayout(inputButtonPanel, BoxLayout.Y_AXIS));

        // Contains entries representing the different SlideLists that the display unit has in its directory
        JList inputList = new JList();
        inputList.setBorder(new BevelBorder(BevelBorder.LOWERED, null, null, null, null));
        inputInteriorPanel.add(inputList, BorderLayout.CENTER);

        // Contains commands relating to the projection system itself, such as closing the socket or sending a Message.POISON
        JPanel systemPanel = new JPanel();
        tabbedPane.addTab("System Options", null, systemPanel, null);
        systemPanel.setLayout(new BoxLayout(systemPanel, BoxLayout.Y_AXIS));

        Component systemFirstVerticalStrut = Box.createVerticalStrut(20);
        systemPanel.add(systemFirstVerticalStrut);

        // Allows the user to close the connection (this should close this frame and open a WFCFrame)
        JButton systemBtnCloseConnection = new JButton("Close Connection...");
        systemBtnCloseConnection.setToolTipText("Close this control panel but keep the projection running");
        systemBtnCloseConnection.setAlignmentX(Component.CENTER_ALIGNMENT);
        systemPanel.add(systemBtnCloseConnection);

        Component systemSecondVerticalStrut = Box.createVerticalStrut(10);
        systemPanel.add(systemSecondVerticalStrut);

        // Allows the user to send Message.POISON (should also close the connectiona and close this frame)
        JButton systemBtnTerminateProjection = new JButton("Terminate Projection...");
        systemBtnTerminateProjection.setToolTipText("WARNING: input data will NOT BE SAVED");
        systemBtnTerminateProjection.setAlignmentX(Component.CENTER_ALIGNMENT);
        systemPanel.add(systemBtnTerminateProjection);
    }

}
