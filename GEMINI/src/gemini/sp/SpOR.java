/*==============================================================*/
/*                                                              */
/*                UK Astronomy Technology Centre                */
/*                 Royal Observatory, Edinburgh                 */
/*                 Joint Astronomy Centre, Hilo                 */
/*                   Copyright (c) PPARC 2001                   */
/*                                                              */
/*==============================================================*/
// $Id$

package gemini.sp;

import java.util.Enumeration;

/**
 * OMP class.
 * 
 * @author Martin Folger (M.Folger@roe.ac.uk)
 */
public class SpOR extends SpObsContextItem {

  /** This attribute records the number of items in the OR folder. */
  public static final String ATTR_NUMBER_OF_ITEMS = ":numberOfItems";


  /**
   * Default constructor.
   */
  protected SpOR() {
    super(SpType.OR_FOLDER);
    _avTable.noNotifySet(ATTR_NUMBER_OF_ITEMS, "1", 0);
  }

  /**
   * Set number of items in OR folder (such as MSBs, AND folder) that are to be selected.
   */
  public void setNumberOfItems(int numberOfItems) {
   _avTable.set(ATTR_NUMBER_OF_ITEMS, numberOfItems);
  }

  /**
   * Get number of items in OR folder (such as MSBs, AND folder) that are to be selected.
   *
   * @return number of items (default 1)
   */
  public int getNumberOfItems() {
    return _avTable.getInt(ATTR_NUMBER_OF_ITEMS, 1);
  }

  /**
   * Calculates the duration of this OR folder.
   *
   * Returns the elapsed time for the child item with the longest duration.
   */
  public double getElapsedTime() {
    double maxElapsedTime = 0.0;
    double elapsedTime = 0.0;
    Enumeration children = children();
    SpItem spItem = null;

    while(children.hasMoreElements()) {
      spItem = (SpItem)children.nextElement();

      elapsedTime = 0.0;

      if(spItem instanceof SpMSB) {
        elapsedTime = (((SpMSB)spItem).getElapsedTime() * ((SpMSB)spItem).getNumberRemaining());
      }

      if(spItem instanceof SpAND) {
        elapsedTime = ((SpAND)spItem).getElapsedTime();
      }

      if(elapsedTime > maxElapsedTime) {
        maxElapsedTime = elapsedTime;
      }
    }

    return maxElapsedTime;
  }
}

