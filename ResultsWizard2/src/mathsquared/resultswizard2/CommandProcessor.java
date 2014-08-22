/**
 * 
 */
package mathsquared.resultswizard2;

/**
 * Represents an object capable of receiving and sending {@link Command}s as part of a formal protocol.
 * 
 * @author MathSquared
 * 
 */
public interface CommandProcessor {
    /**
     * Processes a single command. That is, takes action based on that command and determines what command, if any, to return to the sender.
     * 
     * @param msg a {@link Command} to process
     * @return the Command to return to the sender, or null if no return command is needed
     */
    public Command processMessage (Command msg);
}
