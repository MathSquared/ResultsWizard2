/**
 * 
 */
package mathsquared.resultswizard2;

import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;

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
        // TODO obtain network address (in a local Piped__Stream setup, this object would be instantiated by the main app)
        // TODO create Socket and streams

        // TODO instantiate and run the Display
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
    }

}
