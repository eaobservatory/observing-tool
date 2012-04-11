package ot.editor;

import java.util.Vector;
import java.util.ArrayList;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JPanel;
import javax.swing.JComboBox;
import javax.swing.JCheckBox;
import jsky.app.ot.gui.DropDownListBoxWidgetExt;
import jsky.app.ot.gui.TextBoxWidgetExt;

import gemini.sp.SpObs;
import ot.gui.GuiUtil;

/**
 * Base class for MSB and Observation editor GUIs.
 *
 * Common aspects of these GUIs are being extracted
 * into this class from MsbEditorGUI and ObsGUI.
 *
 * The observations remaining counter has unusual code
 * to handle the presense of the REMOVED state as well as
 * numeric states.  Therefore it has been made protected
 * and an interface provided to prevent editor classes
 * from having to deal with the widget directly.
 */
public abstract class MsbObsCommonGUI extends JPanel {

	protected final int nPriorities = 99;

	public JComboBox jComboBox1;
	protected DropDownListBoxWidgetExt remaining;

	public TextBoxWidgetExt nameBox = new TextBoxWidgetExt() ;
	public TextBoxWidgetExt estimatedTime = new TextBoxWidgetExt() ;
	public JCheckBox unSuspendCB = new JCheckBox() ;

	private ArrayList<RemainingCountListener> remainingCountListeners;
	private boolean remainingCountActions;

	/**
	 * Default constructor.  Initializes the drop down lists
	 * for priorities and remaining count.
	 */
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

		remainingCountListeners = new ArrayList<RemainingCountListener>();
		remainingCountActions = true;

		remaining.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (! remainingCountActions) return;

				if (e.getSource() == remaining) {
					for (RemainingCountListener listener: remainingCountListeners) {
						if (remaining.getSelectedItem().equals(SpObs.REMOVE_STRING)) {
							listener.remainingCountUnRemoved();
						}
						else {
							listener.remainingCountChanged(remaining.getSelectedIndex() - 1);
						}
					}
				}
			}
		});
	}

	/**
 	 * Add a RemainingCountListener.
 	 */ 
	public void addRemainingCountListener(RemainingCountListener listener) {
		remainingCountListeners.add(listener);
	}


	/**
	 * Updates the observations remaining counter to show the specified number.
	 *
	 * RemainingCountListeners will not be triggered unless the
	 * value is found to be invalid.
	 *
	 * @param numberRemaining the number of remaining observations.
	 * 	A negative value indicates that the observation has been removed.
	 */
	public void setRemainingCount(int numberRemaining) {
		try {
			remainingCountActions = false;

			if (numberRemaining < 0) {
				remaining.setValue(SpObs.REMOVE_STRING);
			}
			else {
				remaining.setSelectedIndex(numberRemaining + 1);
			}
		}
		catch (IllegalArgumentException e) {
			GuiUtil.showWarning(this,
				"Number of observes exceeds maximum, setting to maximum",
				"Too many observes found (" + numberRemaining + ")");
			
			remainingCountActions = true;
			remaining.setSelectedIndex(101);
		}
		finally {
			remainingCountActions = true;
		}
	}

	/**
	 * Enables or disables the observations remaining drop down.
	 * @param enabled true if the control should be enabled
	 */
	public void setRemainingCountEnabled(boolean enabled) {
		remaining.setEnabled(enabled);
	} 


	/**
	 * Interface for classes interested changes to the remaining
	 * observations counter.
	 */ 
	public interface RemainingCountListener {
		/**
		 * Invoked when the remaining count is changed.
		 *
		 * @param numberRemaining the new value
		 */ 
		void remainingCountChanged(int numberRemaining);

		/**
		 * Invoked when the remaining count should be
		 * toggled between removed and unremoved.
		 */
		void remainingCountUnRemoved();
	}
}
