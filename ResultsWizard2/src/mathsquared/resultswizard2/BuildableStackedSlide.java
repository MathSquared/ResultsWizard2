/**
 * 
 */
package mathsquared.resultswizard2;

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
    private Graphics g;
    private int width;
    private int height;
    private ColorScheme color;

    // This will use a "commit" system: the client will add chunks to a buffer list and periodically either "commit" or "revert" the buffer. Totally not Git-inspired.
    private ArrayList<Chunk> committed;
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

        public int getMaxVertSize () {
            return g.getFontMetrics(font).getHeight();
        }
    }

    private class ThreeTextChunk implements Chunk {
        public String str1;
        public Font font1;
        public String str2;
        public Font font2;
        public String str3;
        public Font font3;

        public int getMaxVertSize () {
            int h1 = g.getFontMetrics(font1).getHeight();
            int h2 = g.getFontMetrics(font2).getHeight();
            int h3 = g.getFontMetrics(font3).getHeight();

            int mx = h1;
            mx = (h2 > mx) ? h2 : mx;
            mx = (h3 > mx) ? h3 : mx;

            return mx;
        }
    }

    private class FourTextChunk implements Chunk {
        public String str1;
        public Font font1;
        public String str2;
        public Font font2;
        public String str3;
        public Font font3;
        public String str4;
        public Font font4;

        public int getMaxVertSize () {
            int h1 = g.getFontMetrics(font1).getHeight();
            int h2 = g.getFontMetrics(font2).getHeight();
            int h3 = g.getFontMetrics(font3).getHeight();
            int h4 = g.getFontMetrics(font4).getHeight();

            int mx = h1;
            mx = (h2 > mx) ? h2 : mx;
            mx = (h3 > mx) ? h3 : mx;
            mx = (h4 > mx) ? h4 : mx;

            return mx;
        }
    }

    // LOGIC //

    public BuildableStackedSlide (Graphics g, int width, int height, ColorScheme color) {
        this.g = g;
        this.width = width;
        this.height = height;
        this.color = color;
    }

    public void draw (Graphics g) {
        // TODO
    }
}
