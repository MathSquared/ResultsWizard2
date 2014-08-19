/**
 * 
 */
package mathsquared.resultswizard2;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * A simple, immutable implementation of {@link Command}.
 * 
 * @author MathSquared
 * 
 */
public class SimpleCommand implements Command {
    private Message type;
    private String str;
    private LinkedHashMap<String, SlideList> mss;

    /**
     * Constructs a new SimpleCommand with the given message type, string payload, and Map&lt;String, {@link SlideList}> payload.
     * 
     * <p>
     * Note that none of the constructors of this class check whether the payload given is {@linkplain Message#getParamType() supported} by the Message. Any superfluous payload will be ignored.
     * </p>
     * 
     * <p>
     * The iteration order of <code>mss</code> is retained; any maps returned from {@link #getStringSlideListPayload()} will have the same iteration order as <code>mss</code>.
     * </p>
     * 
     * @param type the Message type represented by this Command
     * @param str the String payload of the Command
     * @param mss the Map<String, SlideList> payload of the Command; note that while no references to the <code>mss</code> parameter are retained outside the constructor, the SlideLists are not cloned
     */
    public SimpleCommand (Message type, String str, Map<String, SlideList> mss) {
        this.type = type;
        this.str = str;
        this.mss = new LinkedHashMap<String, SlideList>(mss);
    }

    public Message getType () {
        return type;
    }

    public String getStringPayload () {
        if (type.getParamType().equals(ParamType.STRING)) {
            return str;
        } else {
            throw new UnsupportedOperationException("Message type " + type + " does not carry a String payload");
        }
    }

    public Map<String, SlideList> getStringSlideListPayload () {
        if (type.getParamType().equals(ParamType.MAP_STRING_SLIDELIST)) {
            return mss;
        } else {
            throw new UnsupportedOperationException("Message type " + type + " does not carry a Map<String, SlideList> payload");
        }
    }
}
