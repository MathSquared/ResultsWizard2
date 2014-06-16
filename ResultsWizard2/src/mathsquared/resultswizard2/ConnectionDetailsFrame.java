package mathsquared.resultswizard2;

import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Component;
import java.awt.EventQueue;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.text.ParseException;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JFormattedTextField;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.border.EmptyBorder;
import javax.swing.text.DefaultFormatter;

public class ConnectionDetailsFrame extends JFrame {

    private JPanel contentPane;
    private JTextField portField;
    private JFormattedTextField ipAddrField;
    private final ButtonGroup localOrRemoteGroup = new ButtonGroup();

    /**
     * Launch the application.
     */
    public static void main (String[] args) {
        EventQueue.invokeLater(new Runnable() {
            public void run () {
                try {
                    ConnectionDetailsFrame frame = new ConnectionDetailsFrame();
                    frame.setVisible(true);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    /**
     * Create the frame.
     */
    public ConnectionDetailsFrame () {
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setBounds(100, 100, 450, 300);
        contentPane = new JPanel();
        contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
        contentPane.setLayout(new BorderLayout(0, 0));
        setContentPane(contentPane);

        JLabel lblHeader = new JLabel("Please specify the location of the component you wish to connect to:");
        contentPane.add(lblHeader, BorderLayout.NORTH);

        Component selectLocationHorizontalStrut = Box.createHorizontalStrut(10);
        contentPane.add(selectLocationHorizontalStrut, BorderLayout.WEST);

        JPanel selectLocationPanel = new JPanel();
        contentPane.add(selectLocationPanel, BorderLayout.CENTER);
        selectLocationPanel.setLayout(new BoxLayout(selectLocationPanel, BoxLayout.Y_AXIS));

        Component selectLocationVerticalStrut = Box.createVerticalStrut(10);
        selectLocationPanel.add(selectLocationVerticalStrut);

        JRadioButton rdbtnLocal = new JRadioButton("Local (this computer)");
        rdbtnLocal.setSelected(true);
        localOrRemoteGroup.add(rdbtnLocal);
        selectLocationPanel.add(rdbtnLocal);

        JRadioButton rdbtnRemote = new JRadioButton("Remote (another computer)");
        localOrRemoteGroup.add(rdbtnRemote);
        selectLocationPanel.add(rdbtnRemote);

        JPanel detailAndSubmitPanel = new JPanel();
        contentPane.add(detailAndSubmitPanel, BorderLayout.SOUTH);
        detailAndSubmitPanel.setLayout(new BorderLayout(0, 0));

        JPanel detailPanel = new JPanel();
        detailAndSubmitPanel.add(detailPanel, BorderLayout.NORTH);
        detailPanel.setLayout(new CardLayout(0, 0));

        JPanel localPanel = new JPanel();
        detailPanel.add(localPanel, "localCard");

        JLabel lblLocalDescriptor = new JLabel("This component will connect to another component running on this computer.");
        localPanel.add(lblLocalDescriptor);

        JPanel remotePanel = new JPanel();
        detailPanel.add(remotePanel, "remoteCard");
        remotePanel.setLayout(new BorderLayout(0, 0));

        JLabel lblRemoteHeader = new JLabel("Please specify the connection details below:");
        remotePanel.add(lblRemoteHeader, BorderLayout.NORTH);

        Component connectionDetailsHorizontalStrut = Box.createHorizontalStrut(10);
        remotePanel.add(connectionDetailsHorizontalStrut, BorderLayout.WEST);

        JPanel connectionDetailsPanel = new JPanel();
        remotePanel.add(connectionDetailsPanel, BorderLayout.CENTER);
        GridBagLayout gbl_connectionDetailsPanel = new GridBagLayout();
        gbl_connectionDetailsPanel.columnWidths = new int[] {0, 0, 0, 0};
        gbl_connectionDetailsPanel.rowHeights = new int[] {0, 0, 0, 0};
        gbl_connectionDetailsPanel.columnWeights = new double[] {0.0, 0.0, 1.0, Double.MIN_VALUE};
        gbl_connectionDetailsPanel.rowWeights = new double[] {0.0, 0.0, 0.0, Double.MIN_VALUE};
        connectionDetailsPanel.setLayout(gbl_connectionDetailsPanel);

        Component connectionDetailsVerticalStrut = Box.createVerticalStrut(10);
        GridBagConstraints gbc_connectionDetailsVerticalStrut = new GridBagConstraints();
        gbc_connectionDetailsVerticalStrut.insets = new Insets(0, 0, 5, 5);
        gbc_connectionDetailsVerticalStrut.gridx = 0;
        gbc_connectionDetailsVerticalStrut.gridy = 0;
        connectionDetailsPanel.add(connectionDetailsVerticalStrut, gbc_connectionDetailsVerticalStrut);

        JLabel lblIpAddress = new JLabel("IP address:");
        GridBagConstraints gbc_lblIpAddress = new GridBagConstraints();
        gbc_lblIpAddress.anchor = GridBagConstraints.WEST;
        gbc_lblIpAddress.insets = new Insets(0, 0, 5, 5);
        gbc_lblIpAddress.gridx = 0;
        gbc_lblIpAddress.gridy = 1;
        connectionDetailsPanel.add(lblIpAddress, gbc_lblIpAddress);

        Component connectionDetailsGridHorizontalStrut = Box.createHorizontalStrut(10);
        GridBagConstraints gbc_connectionDetailsGridHorizontalStrut = new GridBagConstraints();
        gbc_connectionDetailsGridHorizontalStrut.insets = new Insets(0, 0, 5, 5);
        gbc_connectionDetailsGridHorizontalStrut.gridx = 1;
        gbc_connectionDetailsGridHorizontalStrut.gridy = 1;
        connectionDetailsPanel.add(connectionDetailsGridHorizontalStrut, gbc_connectionDetailsGridHorizontalStrut);

        ipAddrField = new JFormattedTextField(new DefaultFormatter() {
            /**
             * 
             */
            private static final long serialVersionUID = -3566361116104247105L;

            {
                setValueClass(InetAddress.class);
            }

            public Object stringToValue (String addr) throws ParseException {
                try {
                    return InetAddress.getByName(addr);
                } catch (UnknownHostException e) {
                    ParseException parseE = new ParseException("Invalid IP address or host name", 0);
                    parseE.initCause(e);
                    throw parseE;
                }
            }
        });
        ipAddrField.setHorizontalAlignment(SwingConstants.CENTER);
        ipAddrField.setText("127.0.0.1");
        GridBagConstraints gbc_ipAddrField = new GridBagConstraints();
        gbc_ipAddrField.anchor = GridBagConstraints.WEST;
        gbc_ipAddrField.insets = new Insets(0, 0, 5, 0);
        gbc_ipAddrField.gridx = 2;
        gbc_ipAddrField.gridy = 1;
        connectionDetailsPanel.add(ipAddrField, gbc_ipAddrField);
        ipAddrField.setColumns(10);

        JLabel lblPort = new JLabel("Port:");
        GridBagConstraints gbc_lblPort = new GridBagConstraints();
        gbc_lblPort.insets = new Insets(0, 0, 0, 5);
        gbc_lblPort.anchor = GridBagConstraints.WEST;
        gbc_lblPort.gridx = 0;
        gbc_lblPort.gridy = 2;
        connectionDetailsPanel.add(lblPort, gbc_lblPort);

        portField = new JTextField();
        portField.setHorizontalAlignment(SwingConstants.CENTER);
        portField.setText("60845");
        GridBagConstraints gbc_portField = new GridBagConstraints();
        gbc_portField.anchor = GridBagConstraints.WEST;
        gbc_portField.gridx = 2;
        gbc_portField.gridy = 2;
        connectionDetailsPanel.add(portField, gbc_portField);
        portField.setColumns(5);

        Component submitStrut = Box.createVerticalStrut(20);
        detailAndSubmitPanel.add(submitStrut, BorderLayout.CENTER);

        JButton btnSubmit = new JButton("Submit");
        detailAndSubmitPanel.add(btnSubmit, BorderLayout.SOUTH);
    }

}
