/*==============================================================*/
/*                                                              */
/*                UK Astronomy Technology Centre                */
/*                 Royal Observatory, Edinburgh                 */
/*                 Joint Astronomy Centre, Hilo                 */
/*                   Copyright (c) PPARC 2002                   */
/*                                                              */
/*==============================================================*/
// $Id$

package ot.jcmt.inst.editor.scuba;

import java.awt.Graphics;
import java.awt.Polygon;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

/**
 * Class representing SCUBA bolometer array (SHORT, LONG).
 *
 * This class inherits from the Bolometer because the two classes
 * shares most functionality.
 * But Bolometer array has to draw itself as a hexagon rather than a circle.
 *
 * @author Martin Folger (M.Folger@roe.ac.uk)
 */
public class BolometerArray extends Bolometer implements ActionListener {

  public static final String ARRAY_SELECTION_SHORT      = "arraySelectionShort";
  public static final String ARRAY_SELECTION_LONG       = "arraySelectionLong";
  public static final String ARRAY_SELECTION_SHORT_LONG = "arraySelectionShortLong";
  public static final String ARRAY_SELECTION_LONG_SHORT = "arraySelectionLongShort";
  public static final String ARRAY_SELECTION_NONE       = "arraySelectionNone";


  private Polygon _polygon = null;

  public BolometerArray(int type, int[] xpoints, int[] ypoints, int npoints) {
    // Information about circle position and radius used by the
    // parent class Bolometer are not used by this class.
    // The label after the call of the super class constructor.
    super("", type, 0.0, 0.0);

    if(type == BOLOMETER_SHORT) {
      _label = "SHORT";
    }

    if(type == BOLOMETER_LONG) {
      _label = "LONG";
    }

    _polygon = new Polygon(xpoints, ypoints, npoints);
  }

  public void draw(Graphics g) {
    if(this == _primaryBolometer) {
      g.setColor(COLOR_PRIMARY);
    }
    else {
      if(_selected) {
        g.setColor(COLOR_SELECTED);
      }
      else {
        if(_enabled) {
          g.setColor(COLOR_ENABLED);
        }
	else {
          g.setColor(COLOR_DISABLED);
	}
      }
    }

    g.drawPolygon(_polygon);
  }

  /**
   * Listens to ActionEvent from an associated button etc.
   */
  public void actionPerformed(ActionEvent e) {

    if(e.getActionCommand().equals(ARRAY_SELECTION_SHORT)) {
      switch(_type) {
        case BOLOMETER_SHORT:
	  setPrimary(true);
	  break;
	case BOLOMETER_LONG:
	  setSelected(false);
	  break;
      }
    }

    if(e.getActionCommand().equals(ARRAY_SELECTION_LONG)) {
      switch(_type) {
        case BOLOMETER_SHORT:
	  setSelected(false);
	  break;
	case BOLOMETER_LONG:
	  setPrimary(true);
	  break;
      }
    }

    if(e.getActionCommand().equals(ARRAY_SELECTION_SHORT_LONG)) {
      switch(_type) {
        case BOLOMETER_SHORT:
	  setPrimary(true);
	  break;
	case BOLOMETER_LONG:
	  setPrimary(false);
	  setSelected(true);
	  break;
      }
    }

    if(e.getActionCommand().equals(ARRAY_SELECTION_LONG_SHORT)) {
      switch(_type) {
        case BOLOMETER_SHORT:
	  setPrimary(false);
	  setSelected(true);
	  break;
	case BOLOMETER_LONG:
	  setPrimary(true);
	  break;
      }
    }

    if(e.getActionCommand().equals(ARRAY_SELECTION_NONE)) {
      setSelected(false);
    }
  }
}

