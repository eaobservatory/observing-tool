// Copyright 1997 Association for Universities for Research in Astronomy, Inc.,
// Observatory Control System, Gemini Telescopes Project.
// See the file COPYRIGHT for complete details.
//
// $Id$
//
package jsky.app.ot;

import java.awt.event.*;
import java.util.*;
import javax.swing.*;

import gemini.sp.SpFactory;
import gemini.sp.SpItem;
import gemini.sp.SpType;

import jsky.util.QuickSort;

/**
 * A popup menu used to create observation components.
 */
class OtCompPopupMenu extends JPopupMenu { 
   
    /**
     * Create the popup menu, adding entries for each observation
     * component.
     */
    public OtCompPopupMenu(OtTreeWidget treeWidget) {
        // MFO: Changed because UKIRT and JCMT use different site quality components.
	_add(treeWidget, SpType.get(SpType.OBSERVATION_COMPONENT_TYPE, "schedInfo")); //OBSERVATION_COMPONENT_SITE_QUALITY);
	_add(treeWidget, SpType.OBSERVATION_COMPONENT_TARGET_LIST);
	addSeparator();

	// Sort the instrument types
	Vector         spTypeV = OtCfg.instrumentTypes;
	OtSortableSpType[] sst = new OtSortableSpType[spTypeV.size()];
	for (int i=0; i<sst.length; ++i) {
	    sst[i] = new OtSortableSpType((SpType) spTypeV.elementAt(i));
	}
	QuickSort.sort(sst, 0, sst.length, null);

	// Add each type
	for (int i=0; i<sst.length; ++i) {
	    _add(treeWidget, sst[i].spType);
	}
    }

    //
    // Add a new menu item based on the given SpType.  The label displayed
    // on the menu is taken from the SpType, and the command that will be
    // executed addes a new item of the given SpType to the given treeWidget.
    //
    private void _add(final OtTreeWidget treeWidget, final SpType spType) {
	JMenuItem menuItem = new JMenuItem(spType.getReadable());
        menuItem.addActionListener(new ActionListener() {
		public void actionPerformed(ActionEvent ae) {
		    treeWidget.addItem( SpFactory.create(spType));
		}
	    });
	add(menuItem);
    }
}

