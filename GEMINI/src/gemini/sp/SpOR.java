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
   * Returns the mean of the elapsed time per item in the OR folder
   * multiplied by the number of items that are to be selected.
   *
   * @see #getNumberOfItems()
   */
  public double getTotalTime() {
    double elapsedTime = 0.0;

    // Records the number of children that have a duration, i.e. SpAND and
    // SpMSB (and its subclass SpObs).
    int n = 0;

    Enumeration children = children();
    SpItem spItem = null;

    while(children.hasMoreElements()) {
      spItem = (SpItem)children.nextElement();

      if(spItem instanceof SpMSB) {
	if ( ((SpMSB)spItem).getNumberRemaining() > 0 ) {
          elapsedTime += (((SpMSB)spItem).getTotalTime() * ((SpMSB)spItem).getNumberRemaining());
	}
        n++;
      }

      if(spItem instanceof SpAND) {
        elapsedTime += ((SpAND)spItem).getTotalTime();
        n++;
      }

      if ( spItem instanceof SpSurveyContainer ) {
          elapsedTime += ((SpSurveyContainer)spItem).getTotalTime();
          n++;
      }
    }

    return (elapsedTime / n) * getNumberOfItems();
  }

  /**
   * Calculates the duration of this OR folder.
   *
   * Returns the mean of the elapsed time per item in the OR folder
   * multiplied by the number of items that are to be selected.
   *
   * @see #getNumberOfItems()
   */
  public double getElapsedTime() {
    double elapsedTime = 0.0;

    // Records the number of children that have a duration, i.e. SpAND and
    // SpMSB (and its subclass SpObs).
    int n = 0;

    Enumeration children = children();
    SpItem spItem = null;

    while(children.hasMoreElements()) {
      spItem = (SpItem)children.nextElement();

      if(spItem instanceof SpMSB) {
	if ( ((SpMSB)spItem).getNumberRemaining() > 0 ) {
            elapsedTime += (((SpMSB)spItem).getElapsedTime() * ((SpMSB)spItem).getNumberRemaining());
            n++;
        }
      }

      if(spItem instanceof SpAND) {
        elapsedTime += ((SpAND)spItem).getElapsedTime();
        n++;
      }

      if ( spItem instanceof SpSurveyContainer ) {
          elapsedTime += ((SpSurveyContainer)spItem).getElapsedTime();
          n++;
      }
    }

    return (elapsedTime / n) * getNumberOfItems();
  }
}

