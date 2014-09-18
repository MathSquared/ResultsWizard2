/**
 * 
 */
package mathsquared.resultswizard2;

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

    public RankingEditorTableModel (String[] names, Class[] classes) {
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

    // In theory, colNames and colClasses will have the same lengths.
    // This, my friends, is why RETM is not Serializable.
    // (don't want hostile streams messing with the array lengths)
    public int getColumnCount () {
        return colNames.length;
    }
}
