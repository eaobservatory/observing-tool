package ot.editor;

import java.util.Vector;
import javax.swing.JPanel;
import javax.swing.JComboBox;
import jsky.app.ot.gui.DropDownListBoxWidgetExt;

import gemini.sp.SpObs;
import ot.gui.GuiUtil;

/**
 * Base class for MSB and Observation editor GUIs.
 *
 * Common aspects of these GUIs are being extracted
 * into this class from MsbEditorGUI and ObsGUI.
 */
public abstract class MsbObsCommonGUI extends JPanel {

	protected final int nPriorities = 99;

	public JComboBox jComboBox1;
	public DropDownListBoxWidgetExt remaining;

	public MsbObsCommonGUI() {

		// Initialize the priorities selection.
		
		Vector<Integer> priorities = new Vector<Integer>();

		for (int i = 1; i <= nPriorities; i ++) {
			priorities.add(new Integer(i));
		}

		jComboBox1 = new JComboBox(priorities);


		// Initialize the remaining counter selection.
		
		remaining = new DropDownListBoxWidgetExt();

		remaining.addItem(SpObs.REMOVE_STRING);

		for(int i = 0; i <= 100; i++) {
			remaining.addItem("" + i);
		}

	}
}
