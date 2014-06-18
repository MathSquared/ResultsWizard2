package mathsquared.resultswizard2;

import java.awt.Component;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.border.EmptyBorder;
import javax.swing.border.EtchedBorder;

public class WaitForConnectionFrame extends JFrame implements Callable<Socket> {

    private JPanel contentPane;
    private JTextField portField;

    private int port = 60845;

    // returning from the call method; ready is true when the user makes a selection
    private boolean ready = false;
    private Socket sock;

    private volatile ServerSocket serv;

    /**
     * Create the frame.
     */
    public WaitForConnectionFrame () {
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setBounds(100, 100, 450, 300);
        contentPane = new JPanel();
        contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
        setContentPane(contentPane);
        contentPane.setLayout(new BoxLayout(contentPane, BoxLayout.Y_AXIS));

        JLabel lblIfYouWish = new JLabel("If you wish to display the projection on this computer, click the button below:");
        lblIfYouWish.setAlignmentX(Component.CENTER_ALIGNMENT);
        contentPane.add(lblIfYouWish);

        JButton btnInitiateLocalSession = new JButton("Initiate Local Session");
        btnInitiateLocalSession.addActionListener(new ActionListener() {
            public void actionPerformed (ActionEvent e) {
                sock = null;
                ready = true;
                WaitForConnectionFrame.this.setVisible(false);
            }
        });
        btnInitiateLocalSession.setAlignmentX(Component.CENTER_ALIGNMENT);
        contentPane.add(btnInitiateLocalSession);

        Component verticalStrut_1 = Box.createVerticalStrut(20);
        contentPane.add(verticalStrut_1);

        JLabel lblOtherwisePleaseConnect = new JLabel("Otherwise, please connect the display with the following information:");
        lblOtherwisePleaseConnect.setAlignmentX(Component.CENTER_ALIGNMENT);
        contentPane.add(lblOtherwisePleaseConnect);

        JPanel ipInfoPanel = new JPanel();
        ipInfoPanel.setBorder(new EtchedBorder(EtchedBorder.LOWERED, null, null));
        contentPane.add(ipInfoPanel);
        ipInfoPanel.setLayout(new GridLayout(2, 2, 10, 0));

        JLabel lblIpAddress = new JLabel("IP Address:");
        lblIpAddress.setHorizontalAlignment(SwingConstants.RIGHT);
        ipInfoPanel.add(lblIpAddress);

        JLabel lblIpLabel;
        try {
            lblIpLabel = new JLabel(InetAddress.getLocalHost().getHostAddress());
        } catch (UnknownHostException e2) {
            lblIpLabel = new JLabel("ERROR: UNKNOWN IP");
        }
        ipInfoPanel.add(lblIpLabel);

        JLabel lblPort = new JLabel("Port:");
        lblPort.setHorizontalAlignment(SwingConstants.RIGHT);
        ipInfoPanel.add(lblPort);

        final JLabel lblPortLabel = new JLabel(Integer.toString(port));
        ipInfoPanel.add(lblPortLabel);

        Component verticalStrut = Box.createVerticalStrut(20);
        contentPane.add(verticalStrut);

        JPanel portChangePanel = new JPanel();
        contentPane.add(portChangePanel);
        portChangePanel.setLayout(new FlowLayout(FlowLayout.CENTER, 5, 5));

        JLabel lblChangePort = new JLabel("Change port:");
        portChangePanel.add(lblChangePort);

        Component horizontalStrut = Box.createHorizontalStrut(10);
        portChangePanel.add(horizontalStrut);

        portField = new JTextField();
        portField.setHorizontalAlignment(SwingConstants.CENTER);
        portField.setText("60845");
        portChangePanel.add(portField);
        portField.setColumns(5);

        Component horizontalStrut_1 = Box.createHorizontalStrut(10);
        portChangePanel.add(horizontalStrut_1);

        JButton btnUpdate = new JButton("Update");
        btnUpdate.addActionListener(new ActionListener() {
            public void actionPerformed (ActionEvent e) {
                try {
                    int portCand = Integer.parseInt(portField.getText());
                    if (portCand >= 0 && portCand <= 65535) {
                        ServerSocket oldServ = serv;
                        serv = new ServerSocket(portCand);
                        oldServ.close(); // release the port
                        port = portCand;
                    } else {
                        throw new NumberFormatException("Invalid range for port parameter");
                    }
                } catch (NumberFormatException e1) {
                    JOptionPane.showMessageDialog(WaitForConnectionFrame.this, "The port you entered is not a valid port (must be an integer between 0 and 65535, inclusive). Please try again.");
                } catch (IOException e1) {
                    JOptionPane.showMessageDialog(WaitForConnectionFrame.this, "Error: Unable to listen on port given: " + e1.getMessage());
                }

                // Update GUI
                lblPortLabel.setText(Integer.toString(port));
            }
        });
        portChangePanel.add(btnUpdate);

        Component verticalGlue = Box.createVerticalGlue();
        contentPane.add(verticalGlue);

        // make a ServerSocket
        try {
            serv = new ServerSocket(port);
        } catch (IOException e1) {
            portField.setText("");
            lblPortLabel.setText("");
            JOptionPane.showMessageDialog(WaitForConnectionFrame.this, "Error: Unable to listen on default port: " + e1.getMessage());
        }
    }

    /**
     * Returns a <code>Socket</code> representing a client connected to the user-specified port, or null if the user closes the window or opts to run a local session.
     * 
     * @return the described <code>Socket</code>, or null if a local session
     */
    public synchronized Socket call () {
        setVisible(true);

        ExecutorService pool = Executors.newCachedThreadPool();
        while (!ready) {
            Future<Socket> future = pool.submit(new Callable<Socket>() {
                public Socket call () throws IOException {
                    return serv.accept();
                }
            });
            try {
                sock = future.get(1000, TimeUnit.MILLISECONDS);
                ready = true;
            } catch (TimeoutException e) {
                // unable to retrieve socket, try again!
            } catch (InterruptedException e) {
                // interrupted, try again!
            } catch (ExecutionException e) {
                // IO error, try again!
            }
        }

        return sock;
    }

}
