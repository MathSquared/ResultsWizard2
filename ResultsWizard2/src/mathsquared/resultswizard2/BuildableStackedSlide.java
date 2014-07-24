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
import java.util.List;

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

    // Allows the user to update chunks later.
    private ArrayList<TextChunk> update;

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
            // The offset at which to draw the String
            int textOffset = center ? (width / 2 - g.getFontMetrics(shrunk).stringWidth(str) / 2) : SIDE_MARGIN;
            g.drawString(str, textOffset, baselineLoc);

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

    /**
     * Constructs a new BuildableStackedSlide that renders with the given height and width.
     * 
     * @param width the width available for this slide's rendering, in pixels
     * @param height the height available for this slide's rendering, in pixels
     */
    public BuildableStackedSlide (int width, int height) {
        this.width = width;
        this.height = height;
    }

    @Override
    public void draw (Graphics g) {
        int cursor = 0; // Start at the top (users should add a spacer at the top for a top margin)
        for (Chunk x : remote) {
            cursor = x.draw(g, cursor);
        }
    }

    /**
     * Clears the first buffer.
     */
    public void reset () {
        index.clear();
    }

    /**
     * Copies the first buffer to the second buffer, then clears the first buffer.
     */
    public void commit () {
        head.addAll(index);
        index.clear(); // we've already added these, we can get rid of them now
    }

    /**
     * Clears the second buffer.
     */
    public void revert () {
        head.clear();
    }

    /**
     * Copies the second buffer to the third buffer (allowing its elements to be rendered), then clears the second buffer.
     */
    public void push () {
        remote.addAll(head);
        head.clear(); // we've already added these, we can get rid of them now
    }

    /**
     * Computes the total vertical space taken up by the chunks in one or more lists, in pixels.
     * 
     * @param chunks one or more lists of chunks whose vertical size to compute
     * @return the sum of {@link Chunk#getMaxVertSize()} for each element of each passed-in list
     */
    @SafeVarargs
    private static int findTotalVertSize (List<Chunk>... chunks) {
        int ret = 0;
        for (List<Chunk> x : chunks) {
            for (Chunk y : x) {
                ret += y.getMaxVertSize();
            }
        }
        return ret;
    }

    /**
     * Adds the given chunk to the first buffer. Note that it must be propagated to the third buffer using {@link #commit()} and {@link #push()} before it is rendered.
     * 
     * @param toAdd the chunk to add
     * @return false if the elements in all three buffers take up more room than is available on the slide
     */
    private boolean addChunk (Chunk toAdd) {
        index.add(toAdd); // always add
        int sizeToDate = findTotalVertSize(remote, head, index);
        // Return false if we've overflowed
        return sizeToDate <= width - BOTTOM_MARGIN;
    }

    /**
     * Adds a spacer of the given vertical pixel size to the first buffer. Note that it must be propagated to the third buffer using {@link #commit()} and {@link #push()} before it is rendered.
     * 
     * @param px the amount of vertical space that this spacer should occupy, in pixels
     * @return false if the elements in all three buffers take up more room than is available on the slide
     */
    public boolean addSpacer (int px) {
        SpacerChunk toAdd = new SpacerChunk();
        toAdd.px = px;
        return addChunk(toAdd);
    }

    /**
     * Adds a text string to the first buffer. Note that it must be propagated to the third buffer using {@link #commit()} and {@link #push()} before it is rendered.
     * 
     * @param str the string to display
     * @param font the font with which to display the string
     * @param color the color in which to display the string
     * @return false if the elements in all three buffers take up more room than is available on the slide
     */
    public boolean addText (String str, Font font, Color color, boolean center) {
        TextChunk toAdd = new TextChunk();
        toAdd.str = str;
        toAdd.font = font;
        toAdd.color = color;
        toAdd.center = center;
        return addChunk(toAdd);
    }

    /**
     * Adds a unit containing three columns of text to the first buffer. Note that it must be propagated to the third buffer using {@link #commit()} and {@link #push()} before it is rendered.
     * 
     * @param str1 the string to display on the left
     * @param font1 the font with which to display the string on the left
     * @param color1 the color in which to display the string on the left
     * @param str2 the string to display in the center
     * @param font2 the font with which to display the string in the center
     * @param color2 the color in which to display the string in the center
     * @param str3 the string to display on the right
     * @param font3 the font with which to display the string on the right
     * @param color3 the color in which to display the string on the right
     * @return false if the elements in all three buffers take up more room than is available on the slide
     */
    public boolean addThreeText (String str1, Font font1, Color color1, String str2, Font font2, Color color2, String str3, Font font3, Color color3) {
        ThreeTextChunk toAdd = new ThreeTextChunk();
        toAdd.str1 = str1;
        toAdd.font1 = font1;
        toAdd.color1 = color1;
        toAdd.str2 = str2;
        toAdd.font2 = font2;
        toAdd.color2 = color2;
        toAdd.str3 = str3;
        toAdd.font3 = font3;
        toAdd.color3 = color3;
        return addChunk(toAdd);
    }

    /**
     * Adds a unit containing four columns of text to the first buffer. Note that it must be propagated to the third buffer using {@link #commit()} and {@link #push()} before it is rendered.
     * 
     * @param str1 the string to display on the left
     * @param font1 the font with which to display the string on the left
     * @param color1 the color in which to display the string on the left
     * @param str2 the string to display in the left center
     * @param font2 the font with which to display the string in the left center
     * @param color2 the color in which to display the string in the left center
     * @param str3 the string to display in the right center
     * @param font3 the font with which to display the string in the right center
     * @param color3 the color in which to display the string in the right center
     * @param str4 the string to display on the right
     * @param font4 the font with which to display the string on the right
     * @param color4 the color in which to display the string on the right
     * @return false if the elements in all three buffers take up more room than is available on the slide
     */
    public boolean addFourText (String str1, Font font1, Color color1, String str2, Font font2, Color color2, String str3, Font font3, Color color3, String str4, Font font4, Color color4) {
        FourTextChunk toAdd = new FourTextChunk();
        toAdd.str1 = str1;
        toAdd.font1 = font1;
        toAdd.color1 = color1;
        toAdd.str2 = str2;
        toAdd.font2 = font2;
        toAdd.color2 = color2;
        toAdd.str3 = str3;
        toAdd.font3 = font3;
        toAdd.color3 = color3;
        toAdd.str4 = str4;
        toAdd.font4 = font4;
        toAdd.color4 = color4;
        return addChunk(toAdd);
    }

    /**
     * Allows the user to update a unit previously added to the buffer. Specifically, allows the text content of this chunk to be changed in the future, and returns a number to be used for this purpose.
     * 
     * <p>
     * This method must be called immediately after adding a unit, and the unit must be in the first buffer. If the first buffer is empty, this method throws an {@link IllegalStateException}.
     * </p>
     * 
     * <p>
     * The update process is only allowed for {@linkplain #addText(String, Font, Color, boolean) one-column text units}. Attempting to call this method after adding another type of unit throws an {@link UnsupportedOperationException}.
     * </p>
     * 
     * @return the number which should be used to update the unit in the future
     * @throws IllegalStateException if the first buffer is empty (i.e. has been {@linkplain #reset() reset} or {@linkplain #commit() committed})
     * @throws UnsupportedOperationException if this method is called when the last element of the first buffer is anything but a one-column text unit
     */
    public int updatable () {
        if (index.isEmpty()) {
            throw new IllegalStateException("Must call updatable immediately after adding a unit");
        }
        if (!(index.get(index.size() - 1) instanceof TextChunk)) {
            throw new UnsupportedOperationException("Updatable is only supported for text units");
        }
        assert index.get(index.size() - 1) instanceof TextChunk;
        update.add((TextChunk) index.get(index.size() - 1)); // add the last (most recent) element of index
        return update.size() - 1;
    }

    /**
     * Updates the text of a text unit. Specifically, given an ID obtained from {@link #updatable()}, updates the text of the corresponding unit so that it will now display <code>newText</code>.
     * 
     * @param updatableId the ID to update
     * @param newText the new text of the text unit
     * @throws IndexOutOfBoundsException if <code>updatableId</code> does not represent an ID validly obtained from <code>updatable()</code>
     */
    public void update (int updatableId, String newText) {
        update.get(updatableId).str = newText;
    }
}
