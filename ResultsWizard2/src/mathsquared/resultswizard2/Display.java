/**
 * 
 */
package mathsquared.resultswizard2;

import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.net.Socket;

import javax.swing.JOptionPane;

/**
 * Runs the display logic of the program; acts as the client in a client-server framework.
 * 
 * @author MathSquared
 * 
 */
public class Display implements Runnable {

    ObjectInputStream in;
    ObjectOutputStream out;

    /**
     * @param args the command-line arguments; unused
     */
    public static void main (String[] args) {
        ConnectionDetailsFrame cdf = new ConnectionDetailsFrame(false); // disallow local connections by passing in false
        Socket sock = cdf.call();

        // Don't allow the user to specify a null network connection (even though we disallow local, they can still press the close button)
        while (sock == null) {
            JOptionPane.showMessageDialog(null, "You must specify an IP address and port, then press the Submit button.");
            sock = cdf.call();
        }

        InputStream sockIn;
        OutputStream sockOut;

        try {
            sockIn = sock.getInputStream();
            sockOut = sock.getOutputStream();
        } catch (IOException e) {
            System.out.println("ERROR: I/O error occurred when initializing network communication");
            e.printStackTrace(System.out);
            System.out.println("Exiting...");
            System.exit(0);
            return;
        }

        // Run the app
        try {
            new Display(sockIn, sockOut);
        } catch (IOException e) {
            System.out.println("ERROR: I/O error occurred when initializing display");
            e.printStackTrace(System.out);
            System.out.println("Exiting...");
            System.exit(0);
            return;
        }
    }

    /**
     * Creates a Display instance which communicates with its server through the given streams.
     * 
     * @param cmds the InputStream carrying serialized {@link Command} instances for the Display to act upon
     * @param resp the OutputStream used for returning <code>Command</code> instances to the client
     * @throws IOException if an I/O error occurs when instantiating the object
     */
    public Display (InputStream cmds, OutputStream resp) throws IOException {
        // Create Object__Streams
        in = new ObjectInputStream(cmds);
        out = new ObjectOutputStream(resp);
    }

    public void run () {
        // TODO display stuff

        // Select a graphics device
        GraphicsDevice[] gds = GraphicsEnvironment.getLocalGraphicsEnvironment().getScreenDevices();
        GraphicsDevice gd = GraphicsDeviceSelector.selectGraphicsDevice(gds);
    }

}
