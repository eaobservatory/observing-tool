// Copyright 1997 Association for Universities for Research in Astronomy, Inc.,
// Observatory Control System, Gemini Telescopes Project.
// See the file COPYRIGHT for complete details.
//
// $Id$
//
package jsky.app.ot.gui;

import java.awt.*;
import java.awt.event.*;
import java.util.*;
import javax.swing.*;
import javax.swing.border.BevelBorder;
import jsky.util.gui.BasicWindowMonitor;


/**
 * A "description" property is added.  It serves the same purpose as
 * "tip" but is shown on demand by clients of this widget rather than
 * anytime the mouse rests over the button.  A "resizeToContent" method
 * causes the button to resize itself to exactly enclose its contents.
 *
 * @author	Shane Walker, Dayle Kotturi, Allan Brighton (port to Swing)
 */
public class CommandButtonWidgetExt extends JButton
    implements DescriptiveWidget, ActionListener {
    private final static int _PADX = 2;
    private final static int _PADY = 2;

    /**
     * Like the "tip" but not shown automatically when the mouse rests on
     * the widget.
     * @see #getDescription
     * @see #setDescription
     */
    public String description;

    // The list of watchers.
    private Vector _watchers = new Vector();

    /** The default constructor. */
    public CommandButtonWidgetExt() {
	super();
	init();
    }

    /** Constructor with label. */
    public CommandButtonWidgetExt(String label) {
	super(label);
	init();
    }

    /** Constructor with icon. */
    public CommandButtonWidgetExt(ImageIcon icon) {
	super(icon);
	init();
    }

    /** Constructor with label and mode (mode ignored here). */
    public CommandButtonWidgetExt(String label, int mode) {
	super(label);
	init();
    }

    /** Initialize the button */
    protected void init() {
	addActionListener(this);
	
	setFont(getFont().deriveFont(Font.PLAIN));
    }

    /** Override parent class so that image buttons are raised */
    public void setIcon(Icon defaultIcon) {
	super.setIcon(defaultIcon);

	// make image buttons raised
	addMouseListener(new MouseAdapter() {
		public void mousePressed(MouseEvent e) {
		    if (isEnabled())
			setBorder(new BevelBorder(BevelBorder.LOWERED));
		}	
		public void mouseReleased(MouseEvent e) {
		    if (isEnabled())
			setBorder(new BevelBorder(BevelBorder.RAISED));
		}	
	    });
	setFocusPainted(false);
	setBorder(new BevelBorder(BevelBorder.RAISED));
    }


    /** Called when the button is pressed */
    public void actionPerformed(ActionEvent ae) {
	action();
    }

    /**
     * Set the description.
     * @see #description
     */
    public void setDescription(String newDescription) {
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
    public synchronized final void addWatcher(CommandButtonWidgetWatcher watcher) {
	if (_watchers.contains(watcher)) {
	    return;
	}
	_watchers.addElement(watcher);
    }
 
    /**
     * Delete a watcher.
     */
    public synchronized final void deleteWatcher(CommandButtonWidgetWatcher watcher) {
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

    /**
     * Notify watchers of an action event.
     */
    public void action() {
	Vector v = _getWatchers();
	int  cnt = v.size();
	for (int i=0; i<cnt; ++i) {
	    CommandButtonWidgetWatcher watcher;
	    watcher = (CommandButtonWidgetWatcher) v.elementAt(i);
	    watcher.commandButtonAction(this);
	}
    }

    /**
     * Resize the button to fit its contents.
     */
    public void	resizeToContent() {
	int imgW=0, imgH=0, labW=0, labH=0;
	boolean havePic = false, haveText = false;
	
	if (getIcon() != null) {
	    imgW = Math.max(getIcon().getIconWidth(), 0);
	    imgH = Math.max(getIcon().getIconHeight(), 0);
	    havePic = true;
	}

	if ( (getText() != null) && (!getText().equals("")) ) {
	    FontMetrics fm = getFontMetrics(getFont());
	    labW = fm.stringWidth(getText());
	    labH = fm.getHeight();
	    haveText = true;
	}

	int w = 0, h = 0;

	if(havePic && ! haveText) {
	    w = imgW;
	    h = imgH;
	}
	else if (havePic && haveText) {
	    w = Math.max(imgW, labW);
	    if (imgH > 0) {
		h = imgH + _PADY + labH;
	    } else {
		h = labH;
	    }
	}
	else {
	    w = labW;
	    h = labH;
	}

	w += _PADX + _PADX;
	h += _PADY + _PADY;

	setPreferredSize(new Dimension(w, h));
    }


    /**
     * Programatically press the button.  The button will repaint itself
     * and notify its watchers that it has been pressed.  However, no
     * ACTION or other events are fired.
     *
     */
    public void	press() {
	if (!isEnabled()) {
	    return;
	}
	doClick();

	/* XXX allan
	if (isSticky() && ((getGroup() == null) || !hasValue())) {
	    setValue(!hasValue());
	}
	repaint();
	action();
	XXX allan */
    }


    /**
     * test main
     */
    public static void main(String[] args) {
	JFrame frame = new JFrame("CommandButtonWidgetExt");

	CommandButtonWidgetExt button = new CommandButtonWidgetExt("Push Me");
	button.addWatcher(new CommandButtonWidgetWatcher() {
		public void commandButtonAction(CommandButtonWidgetExt cbw) {
		    System.out.println("OK");
		}
	    });

        frame.getContentPane().add("Center", button);
        frame.pack();
        frame.setVisible(true);
	frame.addWindowListener(new BasicWindowMonitor());
    }
}

