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

}
