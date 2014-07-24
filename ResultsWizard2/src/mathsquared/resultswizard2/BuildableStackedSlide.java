/**
 * 
 */
package mathsquared.resultswizard2;

import java.awt.Canvas;
import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
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

    public static final int SIDE_MARGIN = 30; // side margin in pixels
    public static final int HORIZ_SPACER = 15; // how far apart to space elements horizontally
    public static final int BOTTOM_MARGIN = 40; // threshhold for "this slide is full"

    // CHUNK CLASSES //

    // These represent the different elements that can be created. //

    private interface Chunk {
        /**
         * Returns the maximum possible vertical size that a chunk of this type can take up with the given parameters. Maximum possible means that none of the fonts are shrunk due to lack of horizontal space.
         * 
         * @return the maximum possible vertical size of this chunk, in pixels
         */
        public int getMaxVertSize ();

        /**
         * Draws this Chunk to the given Graphics object at the given pixel location. The cursor is measured in pixels from the top and represents the top edge of the chunk.
         * 
         * @param g the Graphics object to which to draw this chunk
         * @param cursor the y-coordinate at which to draw the top edge of this chunk
         * @return the value of <code>cursor</code> that should be passed into the <code>draw</code> method for the next chunk
         */
        public int draw (Graphics g, int cursor);
    }

    private class SpacerChunk implements Chunk {
        public int px;

        public int getMaxVertSize () {
            return px;
        }

        public int draw (Graphics g, int cursor) {
            // nothing to see here
            return cursor + px;
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

        public int draw (Graphics g, int cursor) {
            // Get the height of this font before shrinkage
            FontMetrics metrics = g.getFontMetrics(font);
            int toBaseline = metrics.getAscent() + metrics.getLeading() / 2; // half the line spacing above the line
            int totalHeight = metrics.getHeight();
            int baselineLoc = cursor + toBaseline;

            // Shrink the font if needed
            int availableWidth = width - 2 * SIDE_MARGIN;
            Font shrunk = FontUtils.shrinkFontForWidth(font, str, availableWidth, g);

            // Draw the String
            g.setFont(shrunk);
            g.drawString(str, SIDE_MARGIN, baselineLoc);

            return cursor + totalHeight;
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

        public int draw (Graphics g, int cursor) {
            // Get the metrics
            FontMetrics met1 = g.getFontMetrics(font1);
            FontMetrics met2 = g.getFontMetrics(font2);
            FontMetrics met3 = g.getFontMetrics(font3);

            // Find the baseline location
            int toBaseline1 = met1.getAscent() + met1.getLeading() / 2;
            int toBaseline2 = met2.getAscent() + met2.getLeading() / 2;
            int toBaseline3 = met3.getAscent() + met3.getLeading() / 2;
            int toBaseline = toBaseline1; // find maximum
            toBaseline = (toBaseline2 > toBaseline) ? toBaseline2 : toBaseline;
            toBaseline = (toBaseline3 > toBaseline) ? toBaseline3 : toBaseline;
            int baselineLoc = cursor + toBaseline;

            // Shrink the outer fonts
            int totalAvailableWidth = width - 2 * SIDE_MARGIN;
            int outerColumnAvailableWidth = (totalAvailableWidth - 2 * HORIZ_SPACER) / 6; // subtract space taken by the horiz spacer
            Font shrunk1 = FontUtils.shrinkFontForWidth(font1, str1, outerColumnAvailableWidth, g);
            FontMetrics mets1 = g.getFontMetrics(shrunk1);
            Font shrunk3 = FontUtils.shrinkFontForWidth(font3, str3, outerColumnAvailableWidth, g);
            FontMetrics mets3 = g.getFontMetrics(shrunk3);

            // Draw the outer columns
            g.setFont(shrunk1);
            g.drawString(str1, SIDE_MARGIN, baselineLoc);
            g.setFont(shrunk3);
            g.drawString(str3, width - SIDE_MARGIN - mets3.stringWidth(str3), baselineLoc); // right-align

            // Draw the inner column
            int innerColumnAvailableWidth = totalAvailableWidth - mets1.stringWidth(str1) - mets3.stringWidth(str3) - 2 * HORIZ_SPACER;
            Font shrunk2 = FontUtils.shrinkFontForWidth(font2, str2, innerColumnAvailableWidth, g);
            g.setFont(shrunk2);
            g.drawString(str2, SIDE_MARGIN + mets1.stringWidth(str1) + HORIZ_SPACER, baselineLoc);

            // Find the total height (with unshrunk fonts)
            int totalHeight1 = met1.getHeight();
            int totalHeight2 = met2.getHeight();
            int totalHeight3 = met3.getHeight();
            int totalHeight = totalHeight1;
            totalHeight = (totalHeight2 > totalHeight) ? totalHeight2 : totalHeight;
            totalHeight = (totalHeight3 > totalHeight) ? totalHeight3 : totalHeight;

            return cursor + totalHeight;
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

        public int draw (Graphics g, int cursor) {
            // Get the metrics
            FontMetrics met1 = g.getFontMetrics(font1);
            FontMetrics met2 = g.getFontMetrics(font2);
            FontMetrics met3 = g.getFontMetrics(font3);
            FontMetrics met4 = g.getFontMetrics(font4);

            // Find the baseline location
            int toBaseline1 = met1.getAscent() + met1.getLeading() / 2;
            int toBaseline2 = met2.getAscent() + met2.getLeading() / 2;
            int toBaseline3 = met3.getAscent() + met3.getLeading() / 2;
            int toBaseline4 = met4.getAscent() + met4.getLeading() / 2;
            int toBaseline = toBaseline1; // find maximum
            toBaseline = (toBaseline2 > toBaseline) ? toBaseline2 : toBaseline;
            toBaseline = (toBaseline3 > toBaseline) ? toBaseline3 : toBaseline;
            toBaseline = (toBaseline4 > toBaseline) ? toBaseline4 : toBaseline;
            int baselineLoc = cursor + toBaseline;

            // Shrink the outer fonts
            int totalAvailableWidth = width - 2 * SIDE_MARGIN;
            int outerColumnAvailableWidth = (totalAvailableWidth - 2 * HORIZ_SPACER) / 6; // subtract space taken by the horiz spacer
            Font shrunk1 = FontUtils.shrinkFontForWidth(font1, str1, outerColumnAvailableWidth, g);
            FontMetrics mets1 = g.getFontMetrics(shrunk1);
            Font shrunk4 = FontUtils.shrinkFontForWidth(font4, str4, outerColumnAvailableWidth, g);
            FontMetrics mets4 = g.getFontMetrics(shrunk4);

            // Draw the outer columns
            g.setFont(shrunk1);
            g.drawString(str1, SIDE_MARGIN, baselineLoc);
            g.setFont(shrunk4);
            g.drawString(str4, width - SIDE_MARGIN - mets4.stringWidth(str4), baselineLoc); // right-align

            // Find the inner column widths
            int oneSideAvailableWidth = (totalAvailableWidth - HORIZ_SPACER) / 2; // width available for col. 1&2 or 3&4
            int col2AvailableWidth = oneSideAvailableWidth - mets1.stringWidth(str1) - HORIZ_SPACER;
            int col3AvailableWidth = oneSideAvailableWidth - mets4.stringWidth(str4) - HORIZ_SPACER;

            // Shrink the inner fonts
            Font shrunk2 = FontUtils.shrinkFontForWidth(font2, str2, col2AvailableWidth, g);
            Font shrunk3 = FontUtils.shrinkFontForWidth(font3, str3, col3AvailableWidth, g);

            // Draw the inner columns
            g.setFont(shrunk2);
            g.drawString(str2, SIDE_MARGIN + mets1.stringWidth(str1) + HORIZ_SPACER, baselineLoc);
            g.setFont(shrunk3);
            g.drawString(str3, SIDE_MARGIN + oneSideAvailableWidth + HORIZ_SPACER, baselineLoc);

            // Find the total height (with unshrunk fonts)
            int totalHeight1 = met1.getHeight();
            int totalHeight2 = met2.getHeight();
            int totalHeight3 = met3.getHeight();
            int totalHeight4 = met4.getHeight();
            int totalHeight = totalHeight1;
            totalHeight = (totalHeight2 > totalHeight) ? totalHeight2 : totalHeight;
            totalHeight = (totalHeight3 > totalHeight) ? totalHeight3 : totalHeight;
            totalHeight = (totalHeight4 > totalHeight) ? totalHeight4 : totalHeight;

            return cursor + totalHeight;
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

    private int findTotalVertSize (ArrayList<Chunk>... chunks) {
        int ret = 0;
        for (ArrayList<Chunk> x : chunks) {
            for (Chunk y : x) {
                ret += y.getMaxVertSize();
            }
        }
        return ret;
    }

    private boolean addChunk (Chunk toAdd) {
        index.add(toAdd); // always add
        int sizeToDate = findTotalVertSize(remote, head, index);
        // Return false if we've overflowed
        return sizeToDate <= width - BOTTOM_MARGIN;
    }
}
