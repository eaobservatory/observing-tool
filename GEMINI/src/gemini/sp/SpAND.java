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
public class SpAND extends SpObsContextItem {

  /**
   * Default constructor.
   */
  protected SpAND() {
    super(SpType.AND_FOLDER);
  }

  /**
   * Calculates the duration of this AND folder.
   */
  public double getTotalTime() {
    double elapsedTime = 0.0;
    Enumeration children = children();
    SpItem spItem = null;

    while(children.hasMoreElements()) {
      spItem = (SpItem)children.nextElement();

      if(spItem instanceof SpMSB) {
	if ( ((SpMSB)spItem).getNumberRemaining() > 0.0 ) {
          elapsedTime += (((SpMSB)spItem).getTotalTime() * ((SpMSB)spItem).getNumberRemaining());
	}
      }
    }

    return elapsedTime;
  }

  /**
   * Calculates the duration of this AND folder.
   */
  public double getElapsedTime() {
    double elapsedTime = 0.0;
    Enumeration children = children();
    SpItem spItem = null;

    while(children.hasMoreElements()) {
      spItem = (SpItem)children.nextElement();

      if(spItem instanceof SpMSB) {
        elapsedTime += (((SpMSB)spItem).getElapsedTime() * ((SpMSB)spItem).getNumberRemaining());
      }
    }

    return elapsedTime;
  }
}

