/*==============================================================*/
/*                                                              */
/*                UK Astronomy Technology Centre                */
/*                 Royal Observatory, Edinburgh                 */
/*                 Joint Astronomy Centre, Hilo                 */
/*                   Copyright (c) PPARC 2002                   */
/*                                                              */
/*==============================================================*/
// $Id$

package orac.jcmt.iter;

import gemini.sp.SpFactory;
import gemini.sp.SpType;

import orac.jcmt.SpJCMTConstants;

/**
 * Noise Iterator for JCMT (SCUBA).
 *
 * @author Martin Folger (M.Folger@roe.ac.uk)
 */
public class SpIterNoiseObs extends SpIterJCMTObs {

  /** Default number of integrations for Noise Iterator. */
  private static final int NOISE_INTEGRATIONS_DEF = 64;

  public static final SpType SP_TYPE =
        SpType.create(SpType.ITERATOR_COMPONENT_TYPE, "noiseObs", "Noise");

  // Register the prototype.
  static {
    SpFactory.registerPrototype(new SpIterNoiseObs());
  }


  /**
   * Default constructor.
   */
  public SpIterNoiseObs() {
    super(SP_TYPE);

    _avTable.noNotifySet(ATTR_NOISE_SOURCE, NOISE_SOURCES[0], 0);
    _avTable.noNotifySet(ATTR_INTEGRATIONS, String.valueOf(NOISE_INTEGRATIONS_DEF), 0);
  }

  /** Get the noise source. */
  public String getNoiseSource() {
    return _avTable.get(ATTR_NOISE_SOURCE);
  }

  /**
   * Set noise source to one of NOISE_SOURCES.
   *
   * @param noiseSource if this is not one of the NOISE_SOURCES then it will be ignored.
   *
   * @see orac.jcmt.SpJCMTConstants.NOISE_SOURCES
   */
  public void setNoiseSource(String noiseSource) {
    for(int i = 0; i < NOISE_SOURCES.length; i++) {
      if(noiseSource.equals(NOISE_SOURCES[i])) {
        _avTable.set(ATTR_NOISE_SOURCE, NOISE_SOURCES[i]);
      }
    }
  }
}

