/**
 * 
 */
package mathsquared.resultswizard2;

import java.io.Serializable;
import java.util.Map;

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
     * @return the type of this Command, as a {@link Message}
     */
    public Message getType ();

    /**
     * Returns the String parameter to this Message, if applicable.
     * 
     * @return the String parameter to this Command, if supported by the {@linkplain #getType() relevant} {@link Message}
     * @throws UnsupportedOperationException if this Message does not {@linkplain ParamType support} a String parameter
     */
    public String getStringPayload ();

    /**
     * Returns the Map<String, SlideList> parameter to this Message, if applicable.
     * 
     * @return the Map<String, SlideList> parameter to this Command, if supported by the {@linkplain #getType() relevant} {@link Message}
     * @throws UnsupportedOperationException if this Message does not {@linkplain ParamType support} a Map<String, SlideList> parameter
     */
    public Map<String, SlideList> getStringSlideListPayload ();
}
