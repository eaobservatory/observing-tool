package ot.editor;

import gemini.sp.SpMSB;
import jsky.app.ot.editor.OtItemEditor;

/**
 * Base class for MSB and Observation editors.
 *
 * Common aspects of the MSB and Observation editors
 * can be extracted into this class.
 */
public abstract class EdMsbObsCommon extends OtItemEditor
		implements MsbObsCommonGUI.RemainingCountListener {

	/**
	 * If true, ignore action events.
	 */
	protected boolean ignoreActions = false ;


	/**
	 * Default constructor.
	 */
	public EdMsbObsCommon() {
	}

	/**
 	 * Handle changes to the remaining counter.
 	 *
 	 * Does not need to check the ignoreActions flag because
 	 * this will not be triggered by calls to setRemainingCount.
 	 */
	public void remainingCountChanged(int numberRemaining) {
		SpMSB spMSB = (SpMSB) _spItem;
		spMSB.setNumberRemaining(numberRemaining);
	}

	/**
	 * Hangle removing / unremoving the observations remaining.
	 *
	 * Does not need to check the ignoreActions flag because
	 * this will not be triggered by calls to setRemainingCount.
	 */ 
	public void remainingCountToggleRemoved() {
		SpMSB spMSB = (SpMSB) _spItem;
		spMSB.setNumberRemaining(-1 * spMSB.getNumberRemaining());
		_updateWidgets();

	}
}
