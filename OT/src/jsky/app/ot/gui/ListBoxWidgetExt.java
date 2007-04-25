// Copyright 1997 Association for Universities for Research in Astronomy, Inc.,
// Observatory Control System, Gemini Telescopes Project.
// See the file COPYRIGHT for complete details.
//
// $Id$
//
package jsky.app.ot.gui;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.Vector;
import javax.swing.DefaultListModel;
import javax.swing.JFrame;
import javax.swing.JList;
import javax.swing.JScrollPane;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import jsky.util.gui.BasicWindowMonitor;


/**
 * An extension of the Marimba ListBoxWidget to selection observers.
 *
 * @author	Shane Walker
 */
public class ListBoxWidgetExt extends JList
    implements DescriptiveWidget {

    // Observers
    private Vector _watchers = new Vector();

   /**
    * Like the "tip" but not shown automatically when the mouse rests on
    * the widget.
    * @see #getDescription
    * @see #setDescription
    */
    public String description;
    
    
    /** Default Constructor */
    public ListBoxWidgetExt() {
	getSelectionModel().addListSelectionListener(new ListSelectionListener() {
		public void valueChanged(ListSelectionEvent e) {
		    if (!e.getValueIsAdjusting())
			_notifySelect(getSelectedIndex());
		}
	    });

	addMouseListener(new MouseAdapter() {
		public void mouseClicked(MouseEvent e) {
		    if (e.getClickCount() == 2) {
			_notifyAction(locationToIndex(e.getPoint()));
		    }
		}
	    }); 
	setModel(new DefaultListModel());
    }

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
     * Add a watcher.  Watchers are notified when an item is selected.
     */
    public synchronized final void addWatcher(ListBoxWidgetWatcher watcher) {
	if (_watchers.contains(watcher)) {
	    return;
	}
	_watchers.addElement(watcher);  
    }

    /**
     * Delete a watcher.
     */
    public synchronized final void deleteWatcher(ListBoxWidgetWatcher watcher) {
	_watchers.removeElement(watcher);
    }
 
    /**
     * Delete all watchers.
     */
    public synchronized final void deleteWatchers() {
	_watchers.removeAllElements();
    }

    //
    // Get a copy of the _watchers Vector.
    //
    private synchronized final Vector _getWatchers() {
	return (Vector) _watchers.clone();
    }
 
    //
    // Notify watchers that an item has been selected.
    //
    private void _notifySelect(int index) {
	Vector v = _getWatchers();
	int  cnt = v.size();
	for (int i=0; i<cnt; ++i) {
	    ListBoxWidgetWatcher watcher = (ListBoxWidgetWatcher) v.elementAt(i);
	    watcher.listBoxSelect(this, index, getStringValue());
	}
    }

    //
    // Notify watchers that an item has been double-clicked.
    //
    private void _notifyAction(int index) {
	Vector v = _getWatchers();
	int  cnt = v.size();
	for (int i=0; i<cnt; ++i) {
	    ListBoxWidgetWatcher watcher = (ListBoxWidgetWatcher) v.elementAt(i);
	    watcher.listBoxAction(this, index, getStringValue());
	}
    }

    /** Return the String value of teh selected row */
    public String getStringValue() {
	return (String)getSelectedValue();
    }

    /**
     * Focus at the selected item.  I couldn't find an easy way to do this.
     */
    public void	focusAtSelectedItem() {
    }

    
    /** Set the contents of the list */
    public void setRows() {
    }


    /**
     * test main
     */
    public static void main(String[] args) {
	JFrame frame = new JFrame("ListBoxWidgetExt");

	ListBoxWidgetExt list = new ListBoxWidgetExt();
	DefaultListModel model = new DefaultListModel();
	for(int i = 0; i < 50; i++) {
	    model.addElement("row " + i);
	}
	list.setModel(model);
	list.addWatcher(new ListBoxWidgetWatcher() {
		public void listBoxSelect(ListBoxWidgetExt lbwe, int index, String val) {
		    System.out.println("listBoxSelect: " + index);
		}
		public void listBoxAction(ListBoxWidgetExt lbwe, int index, String val) {
		    System.out.println("listBoxAction: " + index);
		}
	    });

        frame.getContentPane().add("Center", new JScrollPane(list));
        frame.pack();
        frame.setVisible(true);
	frame.addWindowListener(new BasicWindowMonitor());
    }

    /** Select the given value */
    public void setValue(int i) {
	getSelectionModel().clearSelection();
	if (i >= 0)
	    getSelectionModel().addSelectionInterval(i, i);
    }

    /** Select the given value */
    public void setValue(String s) {
	setValue(((DefaultListModel)getModel()).indexOf(s));
    }

    /** Set the contents of the list */
    public void setChoices(Vector v) {
	DefaultListModel model = new DefaultListModel();
	int n = v.size();
	for(int i = 0; i < n; i++)
	    model.addElement(v.get(i));
	setModel(model);
    }

    /** Set the contents of the list */
    public void setChoices(Object[] ar) {
	DefaultListModel model = new DefaultListModel();
	for(int i = 0; i < ar.length; i++)
	    model.addElement(ar[i]);
	setModel(model);
    }

    /** Clear  out the list */
    public void clear() {
	((DefaultListModel)getModel()).clear();
    }

}
