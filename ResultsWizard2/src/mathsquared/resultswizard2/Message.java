/**
 * 
 */
package mathsquared.resultswizard2;

/**
 * Represents one of several possible messages sent between the GUI and display units of the application.
 * 
 * <p>
 * An instance of this enum should be sent with all messages between these two components specifying the details of the message.
 * </p>
 * 
 * @author MathSquared
 * 
 */
public enum Message {
    POISON(ParamType.NONE), ADD(ParamType.MAP_STRING_SLIDES), REMOVE(ParamType.STRING), RETR_SLIDES(ParamType.NONE), RESP_SLIDES(ParamType.MAP_STRING_SLIDES), TICKER(ParamType.STRING), RETR_TICKER(ParamType.NONE), RESP_TICKER(ParamType.STRING), XMIT_ERROR_RESTART(ParamType.NONE);

    // TODO add messages for keep-alive system by server (possibly via a required confirmation for all packets)--not needed for client, since it never initiates communication with server and SHOULD proceed even if no comms from server

    private ParamType param;

    /**
     * Constructs a Message with the given {@linkplain ParamType type for its parameters}.
     * 
     * @param param the type for the parameters to this Message
     */
    private Message (ParamType param) {
        this.param = param;
    }

    /**
     * Gets the {@link ParamType} carried by this Message.
     * 
     * @return a representation of the parameters allowed by this Message
     */
    public ParamType getParamType () {
        return param;
    }
}
