/*==============================================================*/
/*                                                              */
/*                UK Astronomy Technology Centre                */
/*                 Royal Observatory, Edinburgh                 */
/*                 Joint Astronomy Centre, Hilo                 */
/*                   Copyright (c) PPARC 2001                   */
/*                                                              */
/*==============================================================*/
// $Id$

package edfreq;

/**
 * @author Martin Folger
 */
public interface FrequencyEditorConstants {

  // FrontEnd

  /** */
  public static final String XML_ELEMENT_HETERODYNE_GUI = "meta_gui_heterodyne";

  /** */
  public static final String XML_ELEMENT_GUI = "gui";
   
  /** */
  public static final String XML_ATTRIBUTE_FE_NAME = "feName";

  /** */
  public static final String XML_ATTRIBUTE_MODE = "mode";

  /** */
  public static final String XML_ATTRIBUTE_BAND_MODE = "bandMode";

  /** */
  public static final String XML_ATTRIBUTE_VELOCITY = "velocity";

  /** */
  public static final String XML_ATTRIBUTE_BAND = "band";

  /** */
  public static final String XML_ATTRIBUTE_MOLECULE = "molecule";

  /** */
  public static final String XML_ATTRIBUTE_TRANSITION = "transition";

  /** */
  public static final String XML_ATTRIBUTE_MOLECULE_FREQUENCY = "moleculeFrequency";

  /** */
  public static final String XML_ATTRIBUTE_MOLECULE2 = "molecule2";

  /** */
  public static final String XML_ATTRIBUTE_TRANSITION2 = "transition2";

  /** */
  public static final String XML_ATTRIBUTE_MOLECULE_FREQUENCY2 = "moleculeFrequency2";

  /** */
  public static final String XML_ATTRIBUTE_OVERLAP = "overlap";

  /** */
  public static final String XML_ATTRIBUTE_LO1 = "lo1";


  // FrequencyTable

  /** */
  public static final String XML_ELEMENT_BANDSYSTEM = "bandSystem";

  /** */
  public static final String XML_ELEMENT_SUBSYSTEM = "subSystem";

  /** */
  public static final String XML_ATTRIBUTE_COUNT = "count";

  /** */
  public static final String XML_ATTRIBUTE_BANDNUM = "bandNum";

  /** */
  public static final String XML_ATTRIBUTE_CENTRE_FREQUENCY = "centreFrequency";

  /** */
  public static final String XML_ATTRIBUTE_BANDWIDTH = "bandWidth";

  /** */
  public static final String XML_ATTRIBUTE_CHANNELS = "channels";

}

