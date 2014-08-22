/**
 * 
 */
package mathsquared.resultswizard2;

import java.util.Queue;

/**
 * Feeds data from a queue to another object, and from the object to another queue. The object must be able to {@linkplain CommandProcessor process commands}.
 * 
 * @author MathSquared
 * 
 */
public class QueueCommunicator implements Runnable {
    private Queue rx; // receiving
    private Queue tx; // sending
    private CommandProcessor comm;

    public static final long SLEEP_TIME = 100; // time to sleep when rx is empty

    /**
     * Creates a QueueCommunicator communicating between two given queues and a given {@link CommandProcessor}. Note that the QueueCommunicator must be {@linkplain #run() run} to have any effect.
     * 
     * @param rx a queue from which to obtain messages received from the outside
     * @param tx a queue to which to send messages intended for return to the sender of any messages popped from <code>rx</code>
     * @param comm a CommandProcessor that will receive the commands from <code>rx</code> and generate new commands to send to <code>tx</code>
     */
    public QueueCommunicator (Queue rx, Queue tx, CommandProcessor comm) {
        this.rx = rx;
        this.tx = tx;
        this.comm = comm;
    }

    /**
     * Runs the QueueCommunicator until aborted.
     * 
     * <p>
     * A QueueCommunicator is aborted when a Command is removed from the queue given by <code>rx</code> whose {@link Command#getType() getType()} method returns {@link Message#POISON} or {@link Message#XMIT_ERROR_RESTART}. If the CommandProcessor designates a response to these commands, it will be sent prior to the QueueCommunicator being aborted.
     * </p>
     * 
     * <p>
     * After being aborted, a QueueCommunicator may be restarted, with the same queues, by calling this method again.
     * </p>
     */
    public void run () {
        boolean aborted = false;
        while (!aborted) {
            // Wait until we have data
            while (rx.size() == 0) {
                try {
                    Thread.sleep(SLEEP_TIME);
                } catch (InterruptedException e) {
                    // ignore
                }
            }

            // Process the data

            Object pop = rx.poll();

            // This is an object stream, so we must check and cast (and ensure that we're not about to NPE, just to be safe)
            if (pop != null && pop instanceof Command) {
                Command cmd = (Command) pop;
                Command send = comm.processMessage(cmd); // Get the command to send back
                if (send != null) { // Only send the command if there's actually something to send (SQProxy's queues choke on null)
                    tx.add(send);
                }
                // Ensure that we're not aborting (POISON or XMIT_ERROR_RESTART invalidate the streams)
                if (cmd.getType().equals(Message.POISON) || cmd.getType().equals(Message.XMIT_ERROR_RESTART)) {
                    aborted = true;
                }
            }
        }
    }
}
