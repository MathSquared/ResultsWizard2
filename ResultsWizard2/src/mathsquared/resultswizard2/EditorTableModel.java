/**
 * 
 */
package mathsquared.resultswizard2;

import javax.swing.table.TableModel;

/**
 * Represents data that can be edited in one or more stages and then transformed into another object.
 * 
 * <p>
 * Note that these stages should not change the column headers or layout in this table. They should only change which columns are editable and which are not. This will help generate a consistent user experience.
 * </p>
 * 
 * @author MathSquared
 * @param <T> the type of data that can be obtained from this model using {@link #getResult()}
 * 
 */
public interface EditorTableModel<T> extends TableModel {
    /**
     * Returns the number of stages used by this editor.
     * 
     * @return a number indicating the number of stages; this number must always be greater than or equal to 1
     */
    public int numStages ();

    /**
     * Returns the current stage.
     * 
     * @return a zero-based index for the current stage; this is guaranteed to be <code>&gt;= 0</code> and <code>&lt; {@link #numStages()}</code>
     */
    public int currentStage ();

    /**
     * Moves to the next stage.
     * 
     * @throws IllegalStateException if we are at the last stage
     */
    public void nextStage ();

    /**
     * Moves to the previous stage.
     * 
     * @throws IllegalStateException if we are at the first stage
     */
    public void previousStage ();

    /**
     * Obtains the current result of this model. This only works if we are at the last stage.
     * 
     * @return an object representing the data stored in this model
     * @throws IllegalStateException if we are not at the last stage
     */
    public T getResult ();
}
