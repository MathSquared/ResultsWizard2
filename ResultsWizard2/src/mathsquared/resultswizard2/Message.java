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
    POISON, ADD, REMOVE, RETR_SLIDES, RESP_SLIDES, TICKER, RETR_TICKER, RESP_TICKER;
}
