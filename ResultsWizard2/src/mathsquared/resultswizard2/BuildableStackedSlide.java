/**
 * 
 */
package mathsquared.resultswizard2;

import java.awt.Canvas;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.util.ArrayList;

/**
 * Represents a Slide that can be built up with several elements until it is full.
 * 
 * @author MathSquared
 * 
 */
public class BuildableStackedSlide implements Slide {
    private int width;
    private int height;
    private ColorScheme color;

    // This will use a "commit" system: the client will add chunks to a buffer list and periodically either "commit" or "revert" the buffer. Totally not Git-inspired.
    // There are two layers of "undo": only what is "pushed to the remote" is actually rendered. You can push from h. to r., revert clear h., commit from i. to h., or reset clear i.
    // (Yes, this terminology is specifically familiar to users of Git. Hey, I'm using Git for this project--I couldn't resist.)
    private ArrayList<Chunk> remote;
    private ArrayList<Chunk> head;
    private ArrayList<Chunk> index;

    // CHUNK CLASSES //

    // These represent the different elements that can be created. //

    private interface Chunk {
        /**
         * Returns the maximum possible vertical size that a chunk of this type can take up with the given parameters. Maximum possible means that none of the fonts are shrunk due to lack of horizontal space.
         * 
         * @return the maximum possible vertical size of this chunk, in pixels
         */
        public int getMaxVertSize ();
    }

    private class SpacerChunk implements Chunk {
        public int px;

        public int getMaxVertSize () {
            return px;
        }
    }

    private class TextChunk implements Chunk {
        public String str;
        public Font font;
        public Color color;
        public boolean center; // whether to center-align the text

        public int getMaxVertSize () {
            Canvas c = new Canvas();
            return c.getFontMetrics(font).getHeight();
        }
    }

    private class ThreeTextChunk implements Chunk {
        public String str1;
        public Font font1;
        public Color color1;
        public String str2;
        public Font font2;
        public Color color2;
        public String str3;
        public Font font3;
        public Color color3;
        public boolean wideThird; // whether to make the third column wider than normal

        public int getMaxVertSize () {
            Canvas c = new Canvas();
            int h1 = c.getFontMetrics(font1).getHeight();
            int h2 = c.getFontMetrics(font2).getHeight();
            int h3 = c.getFontMetrics(font3).getHeight();

            int mx = h1;
            mx = (h2 > mx) ? h2 : mx;
            mx = (h3 > mx) ? h3 : mx;

            return mx;
        }
    }

    private class FourTextChunk implements Chunk {
        public String str1;
        public Font font1;
        public Color color1;
        public String str2;
        public Font font2;
        public Color color2;
        public String str3;
        public Font font3;
        public Color color3;
        public String str4;
        public Font font4;
        public Color color4;

        public int getMaxVertSize () {
            Canvas c = new Canvas();
            int h1 = c.getFontMetrics(font1).getHeight();
            int h2 = c.getFontMetrics(font2).getHeight();
            int h3 = c.getFontMetrics(font3).getHeight();
            int h4 = c.getFontMetrics(font4).getHeight();

            int mx = h1;
            mx = (h2 > mx) ? h2 : mx;
            mx = (h3 > mx) ? h3 : mx;
            mx = (h4 > mx) ? h4 : mx;

            return mx;
        }
    }

    // LOGIC //

    public BuildableStackedSlide (int width, int height) {
        this.width = width;
        this.height = height;
    }

    public void draw (Graphics g) {
        // TODO
    }

    public void reset () {
        index.clear();
    }

    public void commit () {
        head.addAll(index);
        index.clear(); // we've already added these, we can get rid of them now
    }

    public void revert () {
        head.clear();
    }

    public void push () {
        remote.addAll(head);
        head.clear(); // we've already added these, we can get rid of them now
    }
}
