/**
 * 
 */
package mathsquared.resultswizard2;

import java.io.Serializable;

/**
 * Used to transmit a message between client and server. All implementors should define a serialVersionUID.
 * 
 * @author MathSquared
 * 
 */
public interface Command extends Serializable {
    /**
     * Returns a Message representing the type of command that this object is carrying.
     * 
     * @return the type of this Command
     */
    public Message getType ();
}
