/**
 * 
 */
package mathsquared.resultswizard2;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.Window;
import java.io.IOException;

import javax.swing.JPanel;
import javax.swing.SwingUtilities;

/**
 * This JPanel will be the drawing surface for the slideshow.
 * 
 * <p>
 * Game loop logic is heavily based on the sample game loop in Chapter 2 of Killer Game Programming in Java by Andrew Davison, ISBN 978-0-596-00730-0.
 * </p>
 * 
 * <p>
 * If the connection to the server is ever lost, the DisplayPanel will continue projecting whatever data it has already received until it is able to connect to another server. Servers should NEVER make assumptions about the client's state, communicating with it often to request this information (also a good way to check for a dropped connection).
 * </p>
 * 
 * <p>
 * The intent is for this class to represent a versatile drawing surface. A {@link Selector} will handle choosing the slide to display, including protocol communication. The <code>Selector</code> will probably be handled by a JFrame that interfaces between a given communication method (network, user, etc.) and the <code>Selector</code>.
 * </p>
 * 
 * @author MathSquared
 * 
 */
public class DisplayPanel extends JPanel implements Runnable {
    // Size
    private final int WIDTH;
    private final int HEIGHT;

    private final Color BG_COLOR;

    // This thread actually runs the game
    private Thread animator;

    // volatile acts to synchronize all access to these variables
    private volatile boolean running = false;

    // double buffer
    private Graphics gBuf;
    private Image imgBuf = null;

    private final int FPS; // frames per second; initialized by constructor
    private long period; // set based on FPS

    // if we reach this many 0ms delay frames, yield to other processes
    private static final int NO_DELAYS_PER_YIELD = 16;

    // no frame skip logic needed

    // handle the protocol
    private Selector sel;

    /**
     * Creates a DisplayPanel with the given parameters and prepares it for use.
     * 
     * @param width the width of the projection surface, in pixels
     * @param height the height of the projection surface, in pixels
     * @param fps the desired frames per second of the projection (normally, this will equal the monitor refresh rate in Hz)
     * @param bgColor the desired background color of the projection
     * @param sel the {@link Selector} that will supply this DisplayPanel with slides
     */
    public DisplayPanel (int width, int height, int fps, Color bgColor, Selector sel) throws IOException {
        WIDTH = width;
        HEIGHT = height;
        FPS = fps;
        period = 1000000000L / fps;
        BG_COLOR = bgColor;
        this.sel = sel;

        setBackground(bgColor);
        setPreferredSize(new Dimension(width, height));

        // no event listeners, except that we will pop Messages from the stream each time
    }

    // /**
    // * (Re-)Initializes this Display to communicate over the given socket.
    // *
    // * @param sock the {@link Socket} over which to communicate
    // * @throws IOException if <code>sock.getInputStream()</code> and/or <code>sock.getOutputStream()</code> would throw an <code>IOException</code>
    // */
    // private void initStreams (Socket sock) throws IOException {
    // inRaw = sock.getInputStream();
    // outRaw = sock.getOutputStream();
    // }

    /**
     * Called when this component is added to another, this method simply starts the projection.
     */
    public void addNotify () {
        super.addNotify();
        startProjection();
    }

    /**
     * Starts the projection. This should be called by {@link #addNotify()}.
     */
    private void startProjection () {
        if (animator == null || !running) {
            animator = new Thread(this);
            animator.start();
        }
    }

    /**
     * Stops the projection.
     */
    public void stopProjection () {
        running = false;
    }

    public void run () {
        long beforeTime;
        long afterTime;
        long timeDiff;
        long sleepTime;

        long overSleepTime = 0L; // time by which we overslept last cycle
        int noDelays = 0;
        long excess = 0L;

        beforeTime = System.nanoTime(); // updated again right before next cycle

        running = true;

        while (running) {
            stateUpdate();
            renderElements();
            paintScreen();

            afterTime = System.nanoTime();
            timeDiff = afterTime - beforeTime;
            sleepTime = (period - timeDiff) - overSleepTime; // time to make up another period, minus oversleeping time last cycle

            if (sleepTime > 0) { // time left in period, sleep the rest
                try {
                    Thread.sleep(sleepTime / 1000000L); // ns > ms
                } catch (InterruptedException e) {}

                overSleepTime = (System.nanoTime() - afterTime) - sleepTime; // actual minus predicted sleep time
            } else { // period took too long
                excess -= sleepTime; // sleepTime is negative, so excess is positive time overrun by renderer
                overSleepTime = 0L;

                if (++noDelays >= NO_DELAYS_PER_YIELD) {
                    Thread.yield();
                    noDelays = 0;
                }
            }

            beforeTime = System.nanoTime();

            // We don't need any excess updates, since stateUpdate pulls everything from the queue, and that operation is idempotent over an infinitesimal time difference
        }

        // close the display
        Window anc = SwingUtilities.getWindowAncestor(this);
        if (anc != null) {
            anc.setVisible(false);
        }
    }

    /**
     * Updates the unit's state in preparation for a render.
     */
    private void stateUpdate () {
        // TODO parse events from the OIS/OOS
    }

    /**
     * Renders the projection elements into a buffer so that they can be painted.
     */
    private void renderElements () {
        // create buffer
        if (imgBuf == null) {
            imgBuf = createImage(WIDTH, HEIGHT);
            if (imgBuf == null) {
                System.out.println("imgBuf is null");
                return;
            } else {
                gBuf = imgBuf.getGraphics();
            }
        }

        // clear the screen
        gBuf.setColor(BG_COLOR);
        gBuf.fillRect(0, 0, WIDTH, HEIGHT);

        // Draw the current slide
        sel.getCurrent().draw(gBuf);

        // TODO ticker
    }

    /**
     * Actively render the current buffer to the screen.
     */
    private void paintScreen () {
        Graphics g;
        try {
            g = getGraphics();
            if (g != null && imgBuf != null) {
                g.drawImage(imgBuf, 0, 0, null);
            }
            Toolkit.getDefaultToolkit().sync(); // avoid tearing
            g.dispose();
        } catch (Exception e) {
            System.out.println("Graphics context error:");
            e.printStackTrace(System.out);
        }
    }

    // /**
    // * Indicates whether this client is connected to a server.
    // *
    // * @return true if this client is connected to an admin console; false if it is not connected (either never has been, or a previous connection drops)
    // */
    // public boolean getCommsActive () {
    // return commsActive;
    // }
}
