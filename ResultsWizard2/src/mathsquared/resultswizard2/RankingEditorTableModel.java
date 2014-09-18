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

    // In theory, colNames and colClasses will have the same lengths.
    // This, my friends, is why RETM is not Serializable.
    public int getColumnCount () {
        return colNames.length;
    }
}
