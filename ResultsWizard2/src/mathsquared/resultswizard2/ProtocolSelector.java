/**
 * 
 */
package mathsquared.resultswizard2;


/**
 * Handles communication between client and server, including slide selection.
 * 
 * @author MathSquared
 * 
 */
public class ProtocolSelector implements Selector {
    private int width;
    private int height;

    private Slide current;

    public ProtocolSelector (int width, int height) {
        this.width = width;
        this.height = height;
        this.current = null;
    }

    public Slide getCurrent () {
        return current;
    }
}
