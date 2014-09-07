/**
 * 
 */
package mathsquared.resultswizard2;

import javax.swing.table.AbstractTableModel;

/**
 * Allows for the editing of one {@linkplain Facet facet} (individual, team, or special) of {@link EventResults}.
 * 
 * @author MathSquared
 * 
 */
public class EventResultsEditorTableModel extends AbstractTableModel implements EditorTableModel<EventResults> {
	private EventResults buffer; // this holds the current state of the results, as edited by the user
	private Facet facet; // the facet edited by this editor

	@Override
	public int getColumnCount() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int getRowCount() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public Object getValueAt(int rowIndex, int columnIndex) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public int numStages() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int currentStage() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public void nextStage() {
		// TODO Auto-generated method stub

	}

	@Override
	public void previousStage() {
		// TODO Auto-generated method stub

	}

	@Override
	public EventResults getResult() {
		// TODO Auto-generated method stub
		return null;
	}
}
