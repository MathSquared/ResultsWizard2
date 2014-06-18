/**
 * 
 */
package mathsquared.resultswizard2;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.Queue;
import java.util.concurrent.Callable;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

/**
 * Given an {@link ObjectInputStream} and an {@link ObjectOutputStream}, exposes {@link Queue}s that communicate with these streams.
 * 
 * @author MathSquared
 * 
 */
public class StreamQueueProxy implements Runnable {
    private final ObjectInputStream inS;
    private final ObjectOutputStream outS;

    private final Queue inQ;
    private final Queue outQ;

    // used to stop operation when needed
    private volatile boolean aborted = false;

    /**
     * Creates a StreamQueueProxy that communicates with the given streams.
     * 
     * <p>
     * Note that the parameters <code>inS</code> and <code>outS</code> should be pre-initialized, since the <code>ObjectInputStream</code> constructor can block if it hasn't received a stream header from the other end of the connection.
     * </p>
     * 
     * @param inS the <code>ObjectInputStream</code> that the client application will read from by way of the corresponding queue
     * @param outS the <code>ObjectOutputStream</code> that the client application will write to by way of the corresponding queue
     */
    public StreamQueueProxy (ObjectInputStream inS, ObjectOutputStream outS) {
        this.inS = inS;
        this.outS = outS;

        inQ = new ConcurrentLinkedQueue();
        outQ = new ConcurrentLinkedQueue();
    }

    /**
     * Continually updates the <code>Queue</code>s with data from the streams until {@linkplain #abort() aborted}.
     */
    public void run () {
        while (!aborted) {
            // Pop the output queue
            try { // Surround entire loop with try-catch so we don't get stuck trying to send one element
                while (!outQ.isEmpty()) {
                    outS.writeObject(outQ.peek());
                    outQ.poll(); // separated into two calls so if a write fails, we don't simply lose the polled element
                }
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }

            // Continually retrieve objects from the input queue until a given time limit is exceeded
            ExecutorService pool = Executors.newCachedThreadPool();
            try {
                while (true) {
                    Future<Object> future = pool.submit(new Callable<Object>() {
                        public Object call () {
                            try {
                                return inS.readObject();
                            } catch (ClassNotFoundException e) {
                                System.out.println("Error: Class not found: " + e.getMessage());
                                e.printStackTrace(System.out);
                            } catch (IOException e) {
                                System.out.println("An I/O error has occurred: " + e.getMessage());
                                e.printStackTrace(System.out);
                            }
                            return null; // if an exception is thrown
                        }
                    });
                    inQ.add(future.get(1000, TimeUnit.MILLISECONDS));
                }
            } catch (TimeoutException e) {

            } catch (InterruptedException e) {

            } catch (ExecutionException e) {
                System.out.println("Object retrieval failed: " + e.getMessage());
                e.printStackTrace(System.out);
            }
        }
    }

    /**
     * Stops the update process previously initiated by {@link #run()}. After a StreamQueueProxy has been aborted, it cannot be used further; the client must create a new StreamQueueProxy.
     */
    public void abort () {
        aborted = true;
    }

    /**
     * Returns a <code>Queue</code> that will be continually updated to contain objects from the given <code>ObjectInputStream</code> while this StreamQueueProxy is running. The <code>Queue</code> will be thread-safe.
     * 
     * @return a <code>Queue</code> for input into the client program
     */
    public Queue getInQ () {
        return inQ;
    }

    /**
     * Returns a <code>Queue</code> whose contents will be continually sent over the given <code>ObjectOutputStream</code> while this StreamQueueProxy is running. The <code>Queue</code> will be thread-safe.
     * 
     * @return a <code>Queue</code> for output from the client program
     */
    public Queue getOutQ () {
        return outQ;
    }
}
