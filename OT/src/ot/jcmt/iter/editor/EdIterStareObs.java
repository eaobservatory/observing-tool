/*==============================================================*/
/*                                                              */
/*                UK Astronomy Technology Centre                */
/*                 Royal Observatory, Edinburgh                 */
/*                 Joint Astronomy Centre, Hilo                 */
/*                   Copyright (c) PPARC 2001                   */
/*                                                              */
/*==============================================================*/
// $Id$

package ot.jcmt.iter.editor;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.DefaultComboBoxModel;
import javax.swing.JComboBox;

import jsky.app.ot.editor.OtItemEditor;

import jsky.app.ot.gui.TextBoxWidgetExt;
import jsky.app.ot.gui.TextBoxWidgetWatcher;

import gemini.sp.SpAvTable;
import gemini.sp.SpItem;

/**
 * This is the editor for Stare Observe Mode iterator component.
 *
 * @author modified for JCMT by Martin Folger ( M.Folger@roe.ac.uk )
 */
public final class EdIterStareObs extends OtItemEditor
    implements TextBoxWidgetWatcher, ActionListener {

    private IterStareObsGUI _w;       // the GUI layout panel

    // If true, ignore action events
    private boolean ignoreActions = false;

    /**
     * The constructor initializes the title, description, and presentation source.
     */
    public EdIterStareObs() {
	_title       ="Stare Iterator";
	_presSource  = _w = new IterStareObsGUI();
	_description ="Stare Observation Mode";
    }

    public void actionPerformed(ActionEvent e) { }
    public void textBoxKeyPress(TextBoxWidgetExt e) { }
    public void textBoxAction(TextBoxWidgetExt e) { }
    public void _updateWidgets() { }
}

