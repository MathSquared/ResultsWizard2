/**
 * 
 */
package mathsquared.resultswizard2;

import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.io.PipedInputStream;
import java.io.PipedOutputStream;
import java.net.Socket;

import javax.swing.JOptionPane;

/**
 * Main class for the application; runs the administrator GUI that handles result entry.
 * 
 * @author MathSquared
 * 
 */
public class ResultsWizard2 {

    private ObjectOutputStream out;
    private ObjectInputStream in;

    /**
     * @param args the command-line arguments; unused
     */
    public static void main (String[] args) {
        // Setup comms
        WaitForConnectionFrame wfcf = new WaitForConnectionFrame();
        Socket sock = wfcf.call();
        InputStream istr;
        OutputStream ostr;
        if (sock != null) { // there is a socket; remote
            try {
                istr = sock.getInputStream();
                ostr = sock.getOutputStream();
            } catch (IOException e) {
                System.out.println("ERROR: I/O error occurred when initializing network communication");
                e.printStackTrace(System.out);
                System.out.println("Exiting...");
                System.exit(1);
                return;
            }
        } else { // local
            istr = new PipedInputStream();
            ostr = new PipedOutputStream();
            try {
                new Display(new PipedInputStream((PipedOutputStream) ostr), new PipedOutputStream((PipedInputStream) istr));
            } catch (IOException e) {
                System.out.println("ERROR: I/O error occurred when initializing display");
                e.printStackTrace(System.out);
                System.out.println("Exiting...");
                System.exit(1);
                return;
            }
        }

        // make sure this side calls the ObjectOutputStream constructor first! otherwise, deadlock results (see Javadoc)
        ObjectOutputStream objOut = null;
        ObjectInputStream objIn = null;
        try {
            objOut = new ObjectOutputStream(ostr);
            objIn = new ObjectInputStream(istr);
        } catch (IOException e) {
            JOptionPane.showMessageDialog(null, "An I/O error occurred: " + e.getMessage());
            System.exit(1);
        }
    }

}
