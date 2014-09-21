/**
 * 
 */
package mathsquared.resultswizard2;

import java.util.Deque;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.table.AbstractTableModel;

/**
 * Provides functionality for editing rankings of multiple participants that can include ties, including for last place.
 * 
 * @author MathSquared
 * @param <T> the type of data edited by this model
 * 
 */
public abstract class RankingEditorTableModel<T> extends AbstractTableModel implements EditorTableModel<T>, TableModelListener {
    // These are the columns.
    // We initialize one for ties and leave the rest to the constructor.
    // Presumably, the constructor will be invoked by a subclass using super.
    private String[] colNames;
    private Class[] colClasses;

    // Normally, this is set to the number of places in the event (or sweeps contenders to be recognized).
    // The actual row count can exceed this number in cases of a tie for last place.
    private int retainPlaces;

    // These will contain the data. Unfortunately, we have to use List<Object[]> and cast at runtime because the table can expand
    // if there's a tie for last place.
    // data.get(i)[j] will be the entry in the ith row in the jth column
    // (column 0 will be the built-in tie handling)
    // These will probably be exposed to subclasses by way of methods that typecheck beforehand (using the colClasses array).
    private List<Object[]> data;

    // This will be used as a stack for rows that are removed from the table
    // because a tie near the end of the table is modified.
    // In this way, we can prevent permanent data loss when an operator accidentally checks an "End tie?" box.
    // We will push and pop from the beginning of the deque.
    // Rows that become invisible are removed from data and then pushed onto the stack from bottom to top.
    // When a row becomes visible again, it is popped from the stack and appended to data.
    private Deque<Object[]> invisible;

    // After a row is recomputed, we fire an event on the relevant row.
    // However, if we simply call computeRow in the event handler, this would result in infinite recursion.
    // To solve this, before firing the event, we add the row number to this set.
    // In the event handler, we only call computeRow on the row if it is not present in this set.
    // The row is removed from the set after the events are fired.
    private Set<Integer> excludeFromRecomputation = new HashSet<Integer>();

    /**
     * Instantiates a model with the given column names and classes. The model will add a column at index 0 with name "End tie?" and class <code>Boolean.class</code>.
     * 
     * @param names the names of the columns, not including column 0 ("End tie?")--so <code>names[0]</code> is the name of column 1
     * @param classes the classes of the objects in each column, not including column 0 (<code>Boolean.class</code>)--so <code>classes[0]</code> is the class of column 1
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
     * <p>
     * After performing these actions, the method notifies listeners of the row insertion or deletion, if applicable.
     * </p>
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

            // Let's fire events.
            // We added rows from (oldRows + 1) to newRows.
            fireTableRowsInserted(oldRows + 1, newRows);
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

            // Let's fire events.
            // We removed rows from (newRows + 1) to oldRows.
            fireTableRowsDeleted(newRows + 1, oldRows);
        }
    }

    public int getRowCount () {
        return data.size();
    }

    // In theory, colNames and colClasses will have the same lengths.
    // This, my friends, is why RETM is not Serializable.
    // (don't want hostile streams messing with the array lengths)
    public int getColumnCount () {
        return colNames.length;
    }

    public String getColumnName (int col) {
        return colNames[col];
    }

    public Class getColumnClass (int col) {
        return colClasses[col];
    }

    public Object getValueAt (int row, int col) {
        return data.get(row)[col];
    }

    /**
     * {@inheritDoc}
     * 
     * @throws IllegalArgumentException if <code>val</code> is not of the correct class for column <code>col</code>
     */
    public void setValueAt (Object val, int row, int col) {
        // Check the class
        if (!colClasses[col].isInstance(val)) {
            throw new IllegalArgumentException("Values in column " + col + " must be of class " + colClasses[col]);
        }
        data.get(row)[col] = val;
    }

    /**
     * Verifies that the objects in a given row are instances of the corresponding classes in <code>colClasses</code>.
     * 
     * @param row the index of the row of the table to check
     * @return false if the length of the row doesn't match the length of <code>colClasses</code> or if one of the entries in the row is not an instance of the corresponding class in <code>colClasses</code> or a subclass
     */
    private boolean verifyClassConsistency (int row) {
        Object[] toCheck = data.get(row);

        return verifyClassConsistency(toCheck);
    }

    /**
     * Verifies that the objects in a given row are instances of the corresponding classes in <code>colClasses</code>.
     * 
     * @param toCheck an array of the elements in the row to check
     * @return false if the length of <code>toCheck</code> doesn't match the length of <code>colClasses</code> or if one of the entries in <code>toCheck</code> is not an instance of the corresponding class in <code>colClasses</code> or a subclass
     */
    private boolean verifyClassConsistency (Object[] toCheck) {
        // Check lengths
        if (toCheck.length != colClasses.length) {
            return false;
        }

        // Check each class
        // We use isInstance to allow for the values in the row to be subclasses of the values in colClasses
        for (int i = 0; i < toCheck.length; i++) {
            if (!colClasses[i].isInstance(toCheck[i])) {
                return false;
            }
        }

        // All is well
        return true;
        // TODO test
    }

    public final void tableChanged (TableModelEvent evt) {
        // Don't process rows that we're currently not processing (this is the anti-infinite-recursion measure)
        computeRows(evt, false);
    }

    /**
     * Recomputes data for all rows covered by the given {@link TableModelEvent}.
     * 
     * @param evt the event carrying information about the rows to update
     * @param processExclusions whether to still recompute rows that are present in <code>excludeFromRecompuation</code>
     */
    private final void computeRows (TableModelEvent evt, boolean processExclusions) {
        computeRows(evt.getFirstRow(), evt.getLastRow(), processExclusions);
    }

    /**
     * Recomputes data for all rows in the given range.
     * 
     * <p>
     * If <code>lastRow &lt; firstRow</code>, this method does nothing. Otherwise, if <code>firstRow == {@link TableModelEvent#HEADER_ROW}</code>, this is equivalent to <code>computeRows(0, data.size())</code>.
     * </p>
     * 
     * @param firstRow the first row to recompute, inclusive
     * @param lastRow the last row to update, inclusive
     * @param processExclusions whether to still recompute rows that are present in <code>excludeFromRecompuation</code>
     * @throws IllegalArgumentException if either parameter is negative and not equal to {@link TableModelEvent#HEADER_ROW}
     */
    private final void computeRows (int firstRow, int lastRow, boolean processExclusions) {
        // Because of the crazy HEADER_ROW logic below, let's sanity check that firstRow >= lastRow now.
        if (firstRow > lastRow) {
            return;
        }

        // Sanity: both greater than 0 or equal to TableModelEvent.HEADER_ROW
        if (firstRow < 0 && firstRow != TableModelEvent.HEADER_ROW) {
            throw new IllegalArgumentException("firstRow (" + firstRow + ") must be non-negative or equal to HEADER_ROW (" + TableModelEvent.HEADER_ROW);
        }
        if (lastRow < 0 && lastRow != TableModelEvent.HEADER_ROW) {
            throw new IllegalArgumentException("lastRow (" + lastRow + ") must be non-negative or equal to HEADER_ROW (" + TableModelEvent.HEADER_ROW);
        }

        // HEADER_ROW means that the table structure has changed, a.k.a. the whole table has changed.
        if (firstRow == TableModelEvent.HEADER_ROW) {
            for (int i = 0; i < data.size(); i++) {
                if (processExclusions || !excludeFromRecomputation.contains(i)) {
                    computeRow(i);
                }
            }
        } else {
            for (int i = firstRow; i <= lastRow; i++) {
                if (processExclusions || !excludeFromRecomputation.contains(i)) {
                    computeRow(i);
                }
            }
        }
    }

    /**
     * Recomputes data for the given row. This is a delegate to the {@link #computeRow(Object[])} method, which allows subclassers to directly modify the relevant row. The changes made to the row will be {@linkplain #verifyClassConsistency(Object[]) checked} against the classes in the table before updating the actual table.
     * 
     * <p>
     * This method fires a row update event for <code>row</code>. During the event firing, <code>row</code> will be present in <code>excludeFromRecomputation</code>.
     * </p>
     * 
     * @param row the row to update
     */
    // @formatter:off
    /*
     * IMPORTANT NOTE
     * ==============
     * 
     * If I call this method indirectly from the table event listener, it WILL result in calling the same method over and over again. This is because this method fires an update on the row that it modifies; if I don't watch for this, it will call the method to compute this row again.
     * 
     * BEHAVIOR TAKEN INTO ACCOUNT: excludeFromRecomputation
     */
    // @formatter:on
    protected final void computeRow (int row) {
        Object[] tableRow = data.get(row);
        computeRow(tableRow);

        // Check the classes on tableRow before updating the table
        if (verifyClassConsistency(tableRow)) {
            data.set(row, tableRow);
            excludeFromRecomputation.add(row);
            fireTableRowsUpdated(row, row);
            excludeFromRecomputation.remove(row);
        }
    }

    /**
     * Recomputes data for the given row. That is, allows subclassers to modify data that is computed from other elements of the array.
     * 
     * <p>
     * Subclassers <strong>should</strong>:
     * </p>
     * 
     * <ul>
     * <li>directly make changes to <code>row</code> for them to be propagated to the data structure</li>
     * <li>not modify <code>row[0]</code></li>
     * <li>remain consistent with the classes assigned for each column</li>
     * <li>refrain from modifying fields that are user-editable (since this is used by the event listening architecture to determine whether a row needs to be recomputed)</li>
     * </ul>
     * 
     * <p>
     * In addition, <strong>callers of this method</strong> should fire a table update event for the relevant row when updating it. {@link computeRow(int)} takes care of this.
     * </p>
     * 
     * @param row the row to modify
     */
    protected abstract void computeRow (Object[] row);
}
