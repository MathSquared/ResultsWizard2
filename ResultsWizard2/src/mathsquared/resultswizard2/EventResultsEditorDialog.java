package mathsquared.resultswizard2;

import java.awt.BorderLayout;

import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import javax.swing.border.EmptyBorder;

/**
 * Runs the GUI by which an event administrator can input or edit {@link EventResults}.
 * 
 * @author MathSquared
 * 
 */
public class EventResultsEditorDialog extends JDialog {

    private JPanel contentPane;

    /**
     * Create the dialog.
     */
    public EventResultsEditorDialog () {
        setModalityType(ModalityType.DOCUMENT_MODAL);
        setModal(true);
        setResizable(false);
        setTitle("Edit Event Results");
        setBounds(100, 100, 450, 300);
        contentPane = new JPanel();
        contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
        contentPane.setLayout(new BorderLayout(0, 0));
        setContentPane(contentPane);

        JTabbedPane tabbedPane = new JTabbedPane(JTabbedPane.TOP);
        contentPane.add(tabbedPane, BorderLayout.CENTER);

        JPanel indivPanel = new JPanel();
        tabbedPane.addTab("Individual", null, indivPanel, null);

        JPanel teamPanel = new JPanel();
        tabbedPane.addTab("Team", null, teamPanel, null);

        JPanel honorsPanel = new JPanel();
        tabbedPane.addTab("Special Honors", null, honorsPanel, null);

    }

}
