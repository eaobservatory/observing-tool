// Copyright 1997 Association for Universities for Research in Astronomy, Inc.,
// Observatory Control System, Gemini Telescopes Project.
// See the file COPYRIGHT for complete details.
//
// $Id$
//
package ot.gui;

import java.awt.event.*;
import java.util.*;
import javax.swing.*;
import javax.swing.event.*;
import jsky.util.gui.BasicWindowMonitor;
//import jsky.app.ot.gui.PasswordWidgetWatcher;
import jsky.app.ot.gui.DescriptiveWidget;

/**
 * A TextBoxWidget that permits clients to register as key press watchers.
 *
 * @author	Shane Walker, Allan Brighton (Swing port)
 */
public class PasswordWidgetExt extends JPasswordField 
    implements DescriptiveWidget, DocumentListener, ActionListener {
    // Observers
//MFO    private Vector _watchers = new Vector();

    // if true, ignore changes in the text box content
    private boolean _ignoreChanges = false;


   /**
    * Like the "tip" but not shown automatically when the mouse rests on
    * the widget.
    * @see #getDescription
    * @see #setDescription
    */
    public String description;

    /**
    * The style that this box had before it was disabled.
    */
    // XXX allan private int _oldStyle;

    
    /** Default constructor */
    public PasswordWidgetExt() {
	getDocument().addDocumentListener(this);
	addActionListener(this);
    }


    // -- For the DocumentListener interface --

    /** 
     * Gives notification that there was an insert into the
     * document. The range given by the DocumentEvent bounds the
     * freshly inserted region. 
     */
    public void insertUpdate(DocumentEvent e) {
/*MFO	if (! _ignoreChanges)
	    _notifyKeyPress(); */
    }

    /**
     * Gives notification that a portion of the document has been
     * removed. The range is given in terms of what the view last saw
     * (that is, before updating sticky positions).  
     */
    public void removeUpdate(DocumentEvent e) {
/*MFO	if (! _ignoreChanges)
	    _notifyKeyPress();*/
    }

    /** Gives notification that an attribute or set of attributes changed. */
    public void changedUpdate(DocumentEvent e) {
    }


    // -- For the ActionListener interface --

    /** Invoked when an action occurs. */
    public void actionPerformed(ActionEvent e) {
//MFO	_notifyAction();
    }

    /**
     * Get the properties of this widget.
     * XXX allan
    public void getProperties(PropertyList list) {
	// store super class properties in the list
	super.getProperties(list);
	list.setString("description", description,   null);
	list.setInteger("oldStyle",   _oldStyle, BOXED);
    }
    XXX allan */
 
    /**
     * Set the properties of this widget.
     * XXX allan
    public void	setProperties(PropertyList list) {
	// retrieve super class properties from the list
	super.setProperties(list);
	description   = list.getString("description", null);
	_oldStyle      = list.getInteger("oldStyle", BOXED);
    }
    XXX allan */
 
    /**
     * Set the description.
     * @see #description
     */
    public void	setDescription(String newDescription) {
	description = newDescription;
    }
 
    /**
     * Get the description.
     * @see #description
     */
    public String getDescription() {
	return description;
    }
 

    /**
     * Add a watcher.  Watchers are notified when a key is pressed in the
     * text box.
     */
/*MFO    public synchronized final void addWatcher(PasswordWidgetWatcher watcher) {
	if (_watchers.contains(watcher)) {
	    return;
	}

	_watchers.addElement(watcher);
    }
*/
    /**
     * Delete a watcher.
     */
/*MFO    public synchronized final void deleteWatcher(PasswordWidgetWatcher watcher)  {
	_watchers.removeElement(watcher);
    }
*/
    /**
     * Delegate this method from the Observable interface.
     */
/*MFO    public synchronized final void deleteWatchers() {
	_watchers.removeAllElements();
    }*/

    //
    // Get a copy of the _watchers Vector.
    //
/*MFO    private synchronized final Vector _getWatchers() {
	return (Vector) _watchers.clone();
    }
*/
    //
    // Notify watchers that a key has been pressed.
    // 
/*MFO    private void _notifyKeyPress() {
	// XXX allan int  tmp = getSelStart();
	Vector v = _getWatchers();
	int  cnt = v.size();
	for (int i=0; i<cnt; ++i) {
	    PasswordWidgetWatcher watcher = (PasswordWidgetWatcher) v.elementAt(i);
	    try {
		watcher.textBoxKeyPress(this);
	    }
	    catch(Exception e) {
	    }
	}
	// XXX allan select(tmp);
    }
*/
    //
    // Notify watchers that a return key has been pressed in the text box.
    //
/*MFO    private void _notifyAction() {
	Vector v = _getWatchers();
	int  cnt = v.size();
	for (int i=0; i<cnt; ++i) {
	    PasswordWidgetWatcher watcher = (PasswordWidgetWatcher) v.elementAt(i);
	    watcher.textBoxAction(this);
	}
    }
*/
    /**
     * Get the current value as a double.
     */
    public double getDoubleValue(double def) {
	try {
	    return (Double.valueOf((String) getValue())).doubleValue();
	} catch (Exception ex) { }
	return def;
    }

    /**
     * Set the current value as a double.
     */
    public void setValue(double d) {
	setText(String.valueOf(d));
    }

    /**
     * Get the current value as an int.
     */
    public int getIntegerValue(int def) {
	try {
	    return Integer.parseInt((String) getValue());
	} catch (Exception ex) { }
 
	return def;
    }

    /**
     * Set the current value
     */
    public void setText(String s) {
	_ignoreChanges = true;
	super.setText(s);
	_ignoreChanges = false;
    }


    /**
     * Set the current value as an int.
     */
    public void setValue(int i) {
	setText(String.valueOf(i));
    }

    /**
     * Set the current value.
     */
    public void setValue(String s) {
	setText(s);
    }

    /**
     * Return the current value.
     */
    public String getValue() {
	return new String(getPassword());
    }

    /**
     * test main
     */
    public static void main(String[] args) {
	JFrame frame = new JFrame("PasswordWidgetExt");

	PasswordWidgetExt tbw = new PasswordWidgetExt();
	/* MFO
	tbw.addWatcher(new PasswordWidgetWatcher() {
		public void textBoxKeyPress(PasswordWidgetExt tbwe) {
		    System.out.println("textBoxKeyPress: " + tbwe.getValue());
		}
		public void textBoxAction(PasswordWidgetExt tbwe) {
		    System.out.println("textBoxAction: " + tbwe.getValue());
		}
	    });
        */
        frame.getContentPane().add("Center", tbw);
        frame.pack();
        frame.setVisible(true);
	frame.addWindowListener(new BasicWindowMonitor());
    }
}

