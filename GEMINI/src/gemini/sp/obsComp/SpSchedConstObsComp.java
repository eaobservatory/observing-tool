/*==============================================================*/
/*                                                              */
/*                UK Astronomy Technology Centre                */
/*                 Royal Observatory, Edinburgh                 */
/*                 Joint Astronomy Centre, Hilo                 */
/*                   Copyright (c) PPARC 2001                   */
/*                                                              */
/*==============================================================*/
// $Id$

package gemini.sp.obsComp;

import gemini.sp.SpFactory;
import gemini.sp.SpType;

/**
 * Component for OMP scheduling contraints.
 * 
 * @author Martin Folger (M.Folger@roe.ac.uk)
 */
public class SpSchedConstObsComp extends SpObsComp {

  /** This attribute records the earliest scheduling date. */
  public static final String ATTR_EARLIEST = "earliest";

  /** This attribute records the latest scheduling date. */
  public static final String ATTR_LATEST = "latest";

  /** This attribute records the minimum elevation. */
  public static final String ATTR_MIN_ELEVATION = "minEl";

  /** This attribute records the monitoring period */
  public static final String ATTR_PERIOD = "period";

  public static final String NO_VALUE = "none";

  public static final String SUBTYPE = "schedConstraints";

  public static final SpType SP_TYPE =
   	SpType.create(SpType.OBSERVATION_COMPONENT_TYPE, SUBTYPE, "Sched. Constraints");

  // Register the prototype.
  static {
    SpFactory.registerPrototype(new SpSchedConstObsComp());
  }

  /**
   * Default constructor.  Initialize the component type.
   */
  public SpSchedConstObsComp() {
    super(SP_TYPE);
   
    _avTable.noNotifySet(ATTR_EARLIEST, NO_VALUE, 0);
    _avTable.noNotifySet(ATTR_LATEST,   NO_VALUE, 0);
  }
  
  /**
   * Get earliest scheduling date.
   */
  public String getEarliest() {
    String earliest = _avTable.get(ATTR_EARLIEST);

    if (earliest == null) {
      earliest = NO_VALUE;
    }
    
    return earliest;
  }
    
  /**
   * Set earliest scheduling date.
   */
  public void setEarliest(String earliest) {
    _avTable.set(ATTR_EARLIEST, earliest);
  }

  /**
   * Set earliest scheduling date without notifying state machine.
   *
   * Useful for initialising item.
   */
  public void initEarliest(String earliest) {
    _avTable.noNotifySet(ATTR_EARLIEST, earliest, 0);
  }


  /**
   * Get latest scheduling date.
   */
  public String getLatest() {
    String latest = _avTable.get(ATTR_LATEST);

    if (latest == null) {
      latest = NO_VALUE;
    }
    
    return latest;
  }
    
  /**
   * Set latest scheduling date.
   */
  public void setLatest(String latest) {
    _avTable.set(ATTR_LATEST, latest);
  }

  /**
   * Set latest scheduling date without notifying state machine.
   *
   * Useful for initialising item.

   */
  public void initLatest(String latest) {
    _avTable.noNotifySet(ATTR_LATEST, latest, 0);
  }

  /**
   * Get min elevation
   */
  public String getMinElevation() {
    return _avTable.get(ATTR_MIN_ELEVATION);
  }
    
  /**
   * Set min elevation.
   */
  public void setMinElevation(double minEl) {
    _avTable.set(ATTR_MIN_ELEVATION, minEl);
  }

  /**
   * Get resheculing period
   */
  public String getPeriod() {
    return _avTable.get(ATTR_PERIOD);
  }
    
  /**
   * Set rescheduling period.
   */
  public void setPeriod(double period) {
    _avTable.set(ATTR_PERIOD, period);
  }
}



