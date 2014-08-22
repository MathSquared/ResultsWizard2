package mathsquared.resultswizard2;

import javax.swing.JDialog;

/**
 * Runs the GUI by which an event administrator can input or edit {@link EventResults}.
 * 
 * @author MathSquared
 * 
 */
public class EventResultsEditorDialog extends JDialog {

    /**
     * Create the dialog.
     */
    public EventResultsEditorDialog () {
        setModalityType(ModalityType.DOCUMENT_MODAL);
        setModal(true);
        setResizable(false);
        setTitle("Edit Event Results");
        setBounds(100, 100, 450, 300);

    }

}
