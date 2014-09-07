/**
 * 
 */
package mathsquared.resultswizard2;

import javax.swing.event.TableModelListener;

/**
 * Allows for the editing of one facet (individual, team, or special) of {@link EventResults}.
 * 
 * @author MathSquared
 *
 */
public class EventResultsEditorTableModel implements EditorTableModel<EventResults> {
	private EventResults buffer; // this holds the current state of the results, as edited by the user

	@Override
	public void addTableModelListener(TableModelListener arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public Class<?> getColumnClass(int arg0) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public int getColumnCount() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public String getColumnName(int arg0) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public int getRowCount() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public Object getValueAt(int arg0, int arg1) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean isCellEditable(int arg0, int arg1) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void removeTableModelListener(TableModelListener arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setValueAt(Object arg0, int arg1, int arg2) {
		// TODO Auto-generated method stub
		
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
