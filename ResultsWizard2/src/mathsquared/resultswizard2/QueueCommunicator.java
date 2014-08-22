/**
 * 
 */
package mathsquared.resultswizard2;

import java.util.Queue;

/**
 * Feeds data from a queue to another object, and from the object to another queue. Currently, the only supported object is a {@link ProtocolSelector}.
 * 
 * @author MathSquared
 * 
 */
public class QueueCommunicator implements Runnable {
    private Queue rx; // receiving
    private Queue tx; // sending
    private ProtocolSelector comm;

    public static final long SLEEP_TIME = 100; // time to sleep when rx is empty

    public QueueCommunicator (Queue rx, Queue tx, ProtocolSelector comm) {
        this.rx = rx;
        this.tx = tx;
        this.comm = comm;
    }

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
