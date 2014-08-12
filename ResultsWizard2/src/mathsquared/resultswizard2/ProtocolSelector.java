/**
 * 
 */
package mathsquared.resultswizard2;

import java.util.Queue;

/**
 * Handles communication between client and server, including slide selection.
 * 
 * @author MathSquared
 * 
 */
public class ProtocolSelector implements Selector {
    private int width;
    private int height;
    private Queue in; // from the admin console
    private Queue out; // to the console

    private Slide current;

    public ProtocolSelector (int width, int height, Queue in, Queue out) {
        this.width = width;
        this.height = height;
        this.in = in;
        this.out = out;
        this.current = null;
    }

    public Slide getCurrent () {
        return current;
    }
}
