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

import java.util.Vector;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Toolkit;
import java.awt.event.MouseEvent;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

/**
 * SCUBA Bolometer GUI.
 *
 * A Bolometer object can be enabled/disabled, selected/deselected and selected as
 * primary bolometer and it can draw itself according to its selection status.
 *
 * @author Martin Folger (M.Folger@roe.ac.uk)
 */
public class Bolometer extends ClickableCircle {

  public static final Color COLOR_DISABLED = Color.gray;
  public static final Color COLOR_ENABLED  = Color.black;
  public static final Color COLOR_SELECTED = Color.magenta;
  public static final Color COLOR_PRIMARY  = Color.red;
  

  public static final int BOLOMETER_NONE  = 0;
  public static final int BOLOMETER_SHORT = 1;
  public static final int BOLOMETER_LONG  = 2;
  public static final int BOLOMETER_P1100 = 4;
  public static final int BOLOMETER_P1350 = 8;
  public static final int BOLOMETER_P2000 = 16;

  private static final double SIZE_FACTOR = 0.07;

  /**
   * There can be several selecetd Bolometers but only one of them is the primary one.
   *
   * All Bolometers share one static primary Bolometer.
   */
  protected static Bolometer _primaryBolometer = null;

  /**
   * Bolometer type.
   *
   * @see #getBolometerType()
   */
  protected int    _type = BOLOMETER_NONE;

  /** GUI enabled. */
  protected boolean _enabled  = false;

  /** Bolometer selected. */
  protected boolean _selected = false;

  /**
   * The position on the focal plane pointed at the source is determined by the
   * primary sub-instrument.
   */


  /**
   * @param label name of bolometer
   * @param type one of
   * <pre>
   *                {@link #BOLOMETER_NONE},
   *                {@link #BOLOMETER_SHORT},
   *                {@link #BOLOMETER_LONG},
   *                {@link #BOLOMETER_P1100},
   *                {@link #BOLOMETER_P1350},
   *                {@link #BOLOMETER_P2000}
   * </pre>
   * @param x x coordinate of centre.
   * @param y x coordinate of centre.
   */
  public  Bolometer(String label, int type, double x, double y) {
    super(x, y, 0.0, label);

    _type = type;    

    double radius = BOLOMETER_NONE;

    switch(type) {
      case BOLOMETER_SHORT:
             radius =  450.0 * SIZE_FACTOR;
	     break;

      case BOLOMETER_LONG:
             radius =  850.0 * SIZE_FACTOR;
	     break;

      case BOLOMETER_P1100:
             radius = 1100.0 * SIZE_FACTOR;
	     break;

      case BOLOMETER_P1350:
             radius = 1350.0 * SIZE_FACTOR;
	     break;

      case BOLOMETER_P2000:
             radius = 2000.0 * SIZE_FACTOR;
	     break;
    }

    setFrame(x, y, radius, radius);
  }


  /**
   * Get bolometer type.
   *
   * <pre>
   * Returns one of the following values:
   *
   *   {@link #BOLOMETER_UNDEFINED},
   *   {@link #BOLOMETER_SHORT},
   *   {@link #BOLOMETER_LONG},
   *   {@link #BOLOMETER_P1100},
   *   {@link #BOLOMETER_P1350},
   *   {@link #BOLOMETER_P2000},
   * </pre>

   */
  public int getBolometerType() {
    return _type;
  }

  /**
   * Wrapper method for getLabel().
   */
  public String getBolometerName() {
    return getLabel();
  }

  /**
   * @see #setEnabled(boolean)
   */
  public boolean isEnabled() {
    return _enabled;
  }

  /**
   * Enables/disables GUI display of this Bolometer.
   *
   * Note that a bolometer can be selected but have its GUI disabled if it is part of
   * a selected array (LONG or SHORT).
   */
  public void setEnabled(boolean enabled) {
    _enabled = enabled;
  }

  /**
   * @see #setSelected(boolean)
   */
  public boolean isSelected() {
    return _selected;
  }

  /**
   * Select/unselect this Bolometer.
   *
   * Note that a bolometer can be selected but have its GUI disabled if it is part of
   * a selected array (LONG or SHORT).
   */
  public void setSelected(boolean selected) {
    _selected = selected;

    if(_primaryBolometer == null) {
      _primaryBolometer = this;
    }

    if((!_selected) && (this == _primaryBolometer)) {
      _primaryBolometer = null;
    }
  }

  /**
   * @see #setPrimary(boolean)
   */
  public boolean isPrimary() {
    return (this == _primaryBolometer);
  }

  /**
   * Enables/disables GUI display of this Bolometer.
   */
  public void setPrimary(boolean primary) {
    if(primary) {
      _primaryBolometer = this;
      _selected = true;
    }
    else {
      if(this == _primaryBolometer) {
        _primaryBolometer = null;
      }
    }
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

    super.draw(g);
  }


  public void mouseClicked(MouseEvent e) {
    if(!contains(e.getPoint())) {
      return;
    }

    if(_enabled) {
      int command;
      if(SwingUtilities.isRightMouseButton(e)) {
        setPrimary(true);
      }
      else {
        setSelected(!_selected);
      }
    }
    else {
      Toolkit.getDefaultToolkit().beep();
    }

    super.mouseClicked(e);
  }


  /**
   * @param typeString as read from the scuba flat.dat file (e.g. SHORT, P2000, A1, etc).
   *
   * @return 
   * <pre>
   * One of
   *   {@link #BOLOMETER_SHORT}
   *   {@link #BOLOMETER_LONG}
   *   {@link #BOLOMETER_P1100}
   *   {@link #BOLOMETER_P1350}
   *   {@link #BOLOMETER_P2000}
   * </pre>
   */
  public static int toBolometerType(String typeString) {
    if(typeString.equalsIgnoreCase("SHORT")) {
      return BOLOMETER_SHORT;
    }

    if(typeString.equalsIgnoreCase("LONG")) {
      return BOLOMETER_LONG;
    }

    if(typeString.equalsIgnoreCase("P1100")) {
      return BOLOMETER_P1100;
    }

    if(typeString.equalsIgnoreCase("P1350")) {
      return BOLOMETER_P1350;
    }

    if(typeString.equalsIgnoreCase("P2000")) {
      return BOLOMETER_P2000;
    }

    return BOLOMETER_NONE;
  }
}

