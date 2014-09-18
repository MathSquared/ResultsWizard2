/**
 * 
 */
package mathsquared.resultswizard2;

import java.util.Deque;
import java.util.List;

import javax.swing.table.AbstractTableModel;

/**
 * Provides functionality for editing rankings of multiple participants that can include ties, including for last place.
 * 
 * @author MathSquared
 * @param <T> the type of data edited by this model
 * 
 */
public abstract class RankingEditorTableModel<T> extends AbstractTableModel implements EditorTableModel<T> {
    // These are the columns.
    // We initialize one for ties and leave the rest to the constructor.
    // Presumably, the constructor will be invoked by a subclass using super.
    private String[] colNames;
    private Class[] colClasses;

    // Normally, this is set to the number of places in the event (or sweeps contenders to be recognized).
    // The actual row count can exceed this number in cases of a tie for last place.
    private int retainPlaces;

    // These will contain the data. Unfortunately, we have to use List<Object[]> and cast at runtime because the table can expand.
    // data.get(i)[j] will be the entry in the ith row in the jth column
    // (column 0 will be the built-in tie handling)
    // These will probably be exposed to subclasses by way of methods that typecheck beforehand.
    private List<Object[]> data;

    // This will be used as a stack for rows that are removed from the table
    // because a tie near the end of the table is modified.
    // In this way, we can prevent permanent data loss when an operator accidentally checks an "End tie?" box.
    // We will push and pop from the beginning of the deque.
    // Rows that become invisible are removed from data and then pushed onto the stack from bottom to top.
    // When a row becomes visible again, it is popped from the stack and appended to data.
    private Deque<Object[]> invisible;

    /**
     * Instantiates a model with the given column names and classes. The model will add a column at index 0 with name "End tie?" and class <code>Boolean.class</code>.
     * 
     * @param names the names of the columns, not including column 0 ("End tie?")
     * @param classes the classes of the objects in each column, not including column 0 (<code>Boolean.class</code>)
     * @param retainPlaces the minimum number of rows that will always be present (rows can be added in case of a tie for last place)
     * @throws IllegalArgumentException if <code>names.length != classes.length</code>
     */
    public RankingEditorTableModel (String[] names, Class[] classes, int retainPlaces) {
        this.retainPlaces = retainPlaces;

        // The first elements of colNames and colClasses will be "End tie?" and Boolean.class, respectively.
        // We copy from names and classes into the rest of colNames and colClasses.

        // Let's do our length sanity check here so we still have a half-functional instance
        // if names and classes have different lengths
        // So, if the arguments aren't sane (i.e. the same length), we'll still initialize the array lengths to 1
        // (this helps mitigate finalizer exploits)
        if (names.length != classes.length) {
            colNames = new String[1];
            colClasses = new Class[1];
            // Throw the exception later so we initialize the arrays first to counteract finalizer attacks
        } else {
            colNames = new String[names.length + 1];
            colClasses = new Class[classes.length + 1];
        }

        // Initialize arrays
        colNames[0] = "End tie?";
        colClasses[0] = Boolean.class;

        // Now, die horribly if the arg lengths don't match
        if (names.length != classes.length) {
            throw new IllegalArgumentException("Length of names and classes must match (names: " + names.length + ", classes: " + classes.length + ")");
        }

        // Copy the remaining entries from names and classes into colNames and colClasses
        // String and Class are immutable
        System.arraycopy(names, 0, colNames, 1, names.length);
        System.arraycopy(classes, 0, colClasses, 1, classes.length);
    }

    /**
     * Changes the number of rows in the table to a given number. There are three possible cases:
     * 
     * <ul>
     * <li>If <code>newRows</code> is equal to the current row count, this method does nothing.</li>
     * <li>If <code>newRows</code> is greater than the current row count (i.e. we're adding rows), we pop rows from <code>invisible</code> and add them to <code>data</code> from top to bottom. If <code>invisible</code> is or becomes empty, the rows are initialized to new arrays.</li>
     * <li>Else, <code>newRows</code> is less than the current row count (i.e. we're subtracting rows), and we remove rows from the bottom of <code>data</code> and push them to <code>invisible</code> from bottom to top.</li>
     * </ul>
     * 
     * TODO fire events
     * 
     * <p>
     * Note that this method does no formatting checks; in particular, it does not check that the last row in the table has its "End tie?" bit set.
     * </p>
     * 
     * @param newRows the new amount of rows in the table
     */
    private void updateForNewRowCount (int newRows) {
        // Sanity: newRows >= 0
        if (newRows < 0) {
            throw new IllegalArgumentException("newRows must not be negative (" + newRows + ")");
        }

        // Get the old number of rows so we know whether to add or remove
        int oldRows = data.size();

        if (newRows == oldRows) {
            // do nothing
            return;
        } else if (newRows > oldRows) { // add rows
            // Algorithm: We're adding (newRows - oldRows) rows.
            int deltaRows = newRows - oldRows;
            // So, we do this deltaRows times.
            for (int i = 0; i < deltaRows; i++) {
                // We'll fetch an invisible row if it's available.
                if (!invisible.isEmpty()) {
                    // Add the new row
                    Object[] row = invisible.pop();
                    data.add(row);
                } else {
                    // Initialize a new row
                    data.add(new Object[colNames.length]);
                }
            }
        } else { // subtract rows
            // Algorithm: We're subtracting (oldRows - newRows) rows.
            int deltaRows = oldRows - newRows;
            // So, we do this deltaRows times.
            for (int i = 0; i < deltaRows; i++) {
                // Grab a row from the bottom of the data
                Object[] row = data.remove(data.size() - 1);
                // Push it to the invisible rows
                invisible.push(row);
            }
        }
    }

    // In theory, colNames and colClasses will have the same lengths.
    // This, my friends, is why RETM is not Serializable.
    // (don't want hostile streams messing with the array lengths)
    public int getColumnCount () {
        return colNames.length;
    }
}
