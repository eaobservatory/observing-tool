/*==============================================================*/
/*                                                              */
/*                UK Astronomy Technology Centre                */
/*                 Royal Observatory, Edinburgh                 */
/*                 Joint Astronomy Centre, Hilo                 */
/*                   Copyright (c) PPARC 2002                   */
/*                                                              */
/*==============================================================*/
//
// Copyright 1997 Association for Universities for Research in Astronomy, Inc.,
// Observatory Control System, Gemini Telescopes Project.
// See the file COPYRIGHT for complete details.
//
// author: Alan Pickup = dap@roe.ac.uk         2001 Nov 6
//
package ot.ukirt.inst.editor;

import orac.util.LookUpTable;
import orac.ukirt.inst.SpInstUIST;

import gemini.sp.*;
import gemini.sp.obsComp.SpInstObsComp;
import jsky.app.ot.gui.TableWidgetExt;
import jsky.app.ot.gui.TableWidgetWatcher;
import jsky.app.ot.gui.TextBoxWidgetExt;
import jsky.app.ot.gui.TextBoxWidgetWatcher;
import jsky.app.ot.gui.DropDownListBoxWidgetExt;
import jsky.app.ot.gui.DropDownListBoxWidgetWatcher;
import jsky.app.ot.gui.CommandButtonWidgetExt;
import jsky.app.ot.gui.CommandButtonWidgetWatcher;
import jsky.app.ot.gui.OptionWidgetExt;
import jsky.app.ot.gui.CheckBoxWidgetExt;
import jsky.app.ot.gui.CheckBoxWidgetWatcher;

import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.util.Vector;

import jsky.app.ot.tpe.TelescopePosEditor;
import jsky.app.ot.tpe.TpeManager;

import java.awt.CardLayout;
import javax.swing.ButtonGroup;


/**
 * This is the editor for the UIST instrument
 */
public final class EdCompInstUIST extends EdCompInstBase
                               implements ActionListener
{
  /*
    private EdChopCapability  _edChopCapability;
    private EdStareCapability _edStareCapability;
  */
    private SpInstUIST   _instUIST;
    private boolean haveInitialised = false;
    private UistGUI _w;


   /**
    * This flag is set true while _init is executed to prevent actionPerformed() to do react to
    * action events caused by initializing widgets.
    */

/**
 * The constructor initializes the title, description, and presentation source.
 */
public EdCompInstUIST()
{
    _title       ="UIST";
    _presSource  = _w = new UistGUI();
    _description ="The UIST instrument is configured with this component.";

    /*
    _edChopCapability  = new EdChopCapability();
    _edStareCapability = new EdStareCapability();
    */

    //    _w.camera.addItem("imaging");
    //    _w.camera.addItem("spectroscopy");
    //    _w.camera.addItem("ifu");

    ButtonGroup grp = new ButtonGroup();
    grp.add(_w.filterBroadBand);
    grp.add(_w.filterNarrowBand);

    _w.filterBroadBand.addActionListener(this);
    _w.filterNarrowBand.addActionListener(this);

    //
    // Camera
    //
    _w.camera.addWatcher( new DropDownListBoxWidgetWatcher() {
        public void
        dropDownListBoxSelect(DropDownListBoxWidgetExt dd, int i, String val) {
        }

        public void
        dropDownListBoxAction(DropDownListBoxWidgetExt dd, int i, String val) {
	    //	    try { throw new Exception("started"); } catch(Exception e) {e.printStackTrace(); }
            _updateCamera(val);
            _updateSourceMag();
            _updateWidgets();
            TelescopePosEditor tpe = TpeManager.get(_instUIST);
            if (tpe != null) tpe.repaint();
        }
    });

    //
    // Polarimetry
    //
    _w.polarimetry.addWatcher( new CheckBoxWidgetWatcher() {
        public void checkBoxAction(CheckBoxWidgetExt cb) {
            if (cb.getBooleanValue()) {
                _instUIST.setPolarimetry("yes");
	    } else {
                _instUIST.setPolarimetry("no");
	    }
            _instUIST.useDefaultDisperser();
            _instUIST.useDefaultMask();
            _instUIST.useDefaultFilter();
            _instUIST.useDefaultFilterCategory();
            _instUIST.useDefaultOrder();
            _instUIST.useDefaultAcquisition();
            _updatePolarimetry();
            _updateCameraChoices();
            _showCamera();
            _updateDisperserChoices();
            _updateFilterChoices();
            _updateMaskChoices();
            _updateWidgets();
            TelescopePosEditor tpe = TpeManager.get(_instUIST);
            if (tpe != null) tpe.repaint();
        }
    });

    //
    // Source magnitude
    //
    _w.imaging_sourceMag.addWatcher( new DropDownListBoxWidgetWatcher() {
        public void
        dropDownListBoxSelect(DropDownListBoxWidgetExt dd, int i, String val) {}

        public void
        dropDownListBoxAction(DropDownListBoxWidgetExt dd, int i, String val) {
            _instUIST.setSourceMag(val);
            _instUIST.useDefaultAcquisition();
            _updateWidgets();
        }
    });

    _w.spectroscopy_sourceMag.addWatcher( new DropDownListBoxWidgetWatcher() {
        public void
        dropDownListBoxSelect(DropDownListBoxWidgetExt dd, int i, String val) {}

        public void
        dropDownListBoxAction(DropDownListBoxWidgetExt dd, int i, String val) {
            _instUIST.setSourceMag(val);
            _instUIST.useDefaultAcquisition();
            _updateWidgets();
        }
    });



    //
    // GUIs in imaging group
    //

// Added by RDK
    //
    // Pupil imaging mode
    //
    _w.imaging_pupilCamera.addWatcher( new CheckBoxWidgetWatcher() {
        public void checkBoxAction(CheckBoxWidgetExt cb) {
            if (cb.getBooleanValue()) {
                _instUIST.setPupilImaging("yes");               
	    } else {
                _instUIST.setPupilImaging("no");
	    }
            _instUIST.useDefaultMask();
            _updateImagerChoices();
            _updatePupilCamera();
            _updateFilterChoices();
            _updateSourceMag();
            _updateWidgets();
        }
    });

// End of added by RDK

    //
    // Imager list
    //
    _w.imaging_imagerList.addWatcher( new DropDownListBoxWidgetWatcher() {
        public void
        dropDownListBoxSelect(DropDownListBoxWidgetExt dd, int i, String val) {}

        public void
        dropDownListBoxAction(DropDownListBoxWidgetExt dd, int i, String val) {
            _instUIST.setImager( val );
            _updateFilterChoices();
            _instUIST.useDefaultAcquisition();
            _updateWidgets();
            TelescopePosEditor tpe = TpeManager.get(_instUIST);
            if (tpe != null) tpe.repaint();
        }
    });

    //
    // Filter
    //
    _w.imaging_filter.addWatcher( new DropDownListBoxWidgetWatcher() {
        public void
        dropDownListBoxSelect(DropDownListBoxWidgetExt dd, int i, String val) {}

        public void
        dropDownListBoxAction(DropDownListBoxWidgetExt dd, int i, String val) {
            _instUIST.setFilter( val );
            _instUIST.useDefaultAcquisition();
            _updateWidgets(_w.imaging_filter);
        }
    });


    //
    // GUIs in spectroscopy group
    //

// Added by RDK
    //
    // Target acquistion mode
    //
    _w.spectroscopy_targetAcqMode.addWatcher( new CheckBoxWidgetWatcher() {
        public void checkBoxAction(CheckBoxWidgetExt cb) {
            if (cb.getBooleanValue()) {
                _instUIST.setTargetAcq("yes");
	    } else {
                _instUIST.setTargetAcq("no");
	    }
            _updateDisperserChoices();
            _updateMaskChoices();
            _updateTargetAcqMode();
            _instUIST.useDefaultReadMode();
            _instUIST.useDefaultReadArea();
            _updateWidgets();
        }
    });
// End of added by RDK

    //
    // Dispersers = Grisms
    //
    _w.spectroscopy_grism.addWatcher( new DropDownListBoxWidgetWatcher() {
        public void
        dropDownListBoxSelect(DropDownListBoxWidgetExt dd, int i, String val) {}

        public void
        dropDownListBoxAction(DropDownListBoxWidgetExt dd, int i, String val) {
            _instUIST.setDisperser( val );
            _instUIST.useDefaultOrder();
            _instUIST.useDefaultResolution();
            _instUIST.useDefaultAcquisition();
            _updateMaskChoices();
            _updateWidgets();

            TelescopePosEditor tpe = TpeManager.get(_instUIST);
           if (tpe != null) tpe.repaint();
        }
    });

    //
    // Resolution
    //
    _w.spectroscopy_resolution.addWatcher( new TextBoxWidgetWatcher() {
        public void textBoxKeyPress(TextBoxWidgetExt tbw) {
	    try {
	    }catch( Exception ex) {
	        // ignore
            }
        }

        public void textBoxAction(TextBoxWidgetExt tbw) {} // ignore
    });

    //
    // Order
    //
    _w.spectroscopy_order.addWatcher( new TextBoxWidgetWatcher() {
        public void textBoxKeyPress(TextBoxWidgetExt tbw) {
	    try {
	    }catch( Exception ex) {
	        // ignore
            }
        }

        public void textBoxAction(TextBoxWidgetExt tbw) {} // ignore
    });

    //
    // Mask
    //
    /*
    _w.spectroscopy_mask.setChoices(_instUIST.getMaskList());
    */

    _w.spectroscopy_mask.addWatcher( new DropDownListBoxWidgetWatcher() {
        public void
        dropDownListBoxSelect(DropDownListBoxWidgetExt dd, int i, String val) {}

        public void
        dropDownListBoxAction(DropDownListBoxWidgetExt dd, int i, String val) {
            _instUIST.setMask( val );
            _instUIST.useDefaultResolution();
            _instUIST.useDefaultAcquisition();
            _updateWidgets();

            TelescopePosEditor tpe = TpeManager.get(_instUIST);
           if (tpe != null) tpe.repaint();
        }
    });

    //
    // Position angle
    //
    _w.spectroscopy_posAngle.addWatcher( new TextBoxWidgetWatcher() {
        public void textBoxKeyPress(TextBoxWidgetExt tbw) {
	    _instUIST.getAvEditFSM().setEachEditNotifies(false);
            String pas = tbw.getText();
            double pa = Double.parseDouble(pas);
	    _instUIST.getAvEditFSM().deleteObserver(EdCompInstUIST.this);
            if ((pa > 0.00001) || pa < -0.00001) {
                _instUIST.setPosAngleDegrees(pa);
                //_updateWidgets(_w.spectroscopy_posAngle);
	    } else {
                _instUIST.setPosAngleDegrees(0.0);
	    }
	    _instUIST.getAvEditFSM().addObserver(EdCompInstUIST.this);
        }

        public void textBoxAction(TextBoxWidgetExt tbw) {} // ignore
    });

    //
    // GUIs in data acquisition group
    //

    //
    // Readout
    //

// Commented for testing by RDK 30 Dec 2002
//     _w.dataAcq_readout.addWatcher( new DropDownListBoxWidgetWatcher() {
//         public void
//         dropDownListBoxSelect(DropDownListBoxWidgetExt dd, int i, String val) {}

//         public void
//         dropDownListBoxAction(DropDownListBoxWidgetExt dd, int i, String val) {
// 	  //            _instUIST.useDefaultAcquisition();
//             _instUIST.setReadoutOT( val );
//             _updateWidgets(_w.dataAcq_readout);
//         }
//     });

// Added by RDK
    //
    // Readout mode
    //
     _w.dataAcq_readMode.addWatcher( new DropDownListBoxWidgetWatcher() {
         public void
        dropDownListBoxSelect(DropDownListBoxWidgetExt dd, int i, String val) {}

        public void
        dropDownListBoxAction(DropDownListBoxWidgetExt dd, int i, String val) {
	  //            _instUIST.useDefaultAcquisition();
            _instUIST.setReadMode( val );
            _updateWidgets(_w.dataAcq_readMode);
        }
    });

    //
    // Readout area
    //
     _w.dataAcq_readArea.addWatcher( new DropDownListBoxWidgetWatcher() {
         public void
        dropDownListBoxSelect(DropDownListBoxWidgetExt dd, int i, String val) {}

        public void
        dropDownListBoxAction(DropDownListBoxWidgetExt dd, int i, String val) {
	  //            _instUIST.useDefaultAcquisition();
            _instUIST.setReadAreaString( val );
            _updateWidgets(_w.dataAcq_readArea);
        }
    });

// End of added by RDK
    //
    // Actual exposure time
    //
    _w.dataAcq_actualExposureTime.addWatcher( new TextBoxWidgetWatcher() {
        public void textBoxKeyPress(TextBoxWidgetExt tbw) {
	    try {
	      //ignore
	    }catch( Exception ex) {
	        // ignore
	    }
        }

        public void textBoxAction(TextBoxWidgetExt tbw) {} // ignore
    });

    //
    // Actual observation time
    //
    _w.dataAcq_actualObservationTime.addWatcher( new TextBoxWidgetWatcher() {
        public void textBoxKeyPress(TextBoxWidgetExt tbw) {
	    try {
	      //ignore
	    }catch( Exception ex) {
	        // ignore
	    }
        }

        public void textBoxAction(TextBoxWidgetExt tbw) {} // ignore
    });

    //
    // Chop frequency
    //
    _w.dataAcq_chopFrequency.addWatcher( new TextBoxWidgetWatcher() {
        public void textBoxKeyPress(TextBoxWidgetExt tbw) {
	    try {
	      //ignore
	    }catch( Exception ex) {
	        // ignore
	    }
        }

        public void textBoxAction(TextBoxWidgetExt tbw) {} // ignore
    });

    //
    // Duty cycle
    //
    _w.dataAcq_dutyCycle.addWatcher( new TextBoxWidgetWatcher() {
        public void textBoxKeyPress(TextBoxWidgetExt tbw) {
	    try {
	      //ignore
	    }catch( Exception ex) {
	        // ignore
	    }
        }

        public void textBoxAction(TextBoxWidgetExt tbw) {} // ignore
    });

    //
    // Coadds
    //
    _w.dataAcq_coadds.addWatcher( new TextBoxWidgetWatcher() {
        public void textBoxKeyPress(TextBoxWidgetExt tbw) {
	    try {
// Added by RDK
                String coaddsString = tbw.getText();
                int coadds = Integer.parseInt(coaddsString);
                if (coadds > 0) {
                    _instUIST.setCoadds(coadds);
                    _updateWidgets(_w.dataAcq_coadds);
                }
// End of added by RDK
	      //ignore
	    }catch( Exception ex) {
	        // ignore
	    }
        }

        public void textBoxAction(TextBoxWidgetExt tbw) {} // ignore
    });

    //
    // Exposure time
    //
    _w.dataAcq_exposureTime.addWatcher( new TextBoxWidgetWatcher() {
        public void textBoxKeyPress(TextBoxWidgetExt tbw) {
	    try {
                String ets = tbw.getText();
                double et = Double.parseDouble(ets);
                if (et > 0.00001) {
// Commented by RDK
//                    _instUIST.setExpTimeOT(et);
//                    _instUIST.useDefaultReadoutOT();
// End of commented by RDK
// Added by RDK
                    _instUIST.changeExpTimeOT(et);
// End of added by RDK
                    _updateWidgets(_w.dataAcq_exposureTime);
		}
	    }catch( Exception ex) {
	        // ignore
            }
        }

        public void textBoxAction(TextBoxWidgetExt tbw) {} // ignore
    });

    //
    // Default exposure and observation times
    //
    _w.dataAcq_defaultExpTime.addWatcher( new CommandButtonWidgetWatcher() {
        public void commandButtonAction(CommandButtonWidgetExt cbw) {
// Commented out by RDK
//            _instUIST.useDefaultReadoutOT();
//            _instUIST.useDefaultObsTimeOT();
//            _instUIST.useDefaultExpTimeOT();
//            _updateObsTimeOT();
// End of commented out by RDK
// Added by RDK
            _instUIST.useDefaultExpTimeOT();
            _instUIST.useDefaultReadMode();
            _instUIST.useDefaultReadArea();
            _instUIST.useDefaultCoadds();
// End of added by RDK
            _updateWidgets();
        }
    });

    //
    // Observation time
    //
// Commented for testing by RDK 30 Dec 2002
//     _w.dataAcq_observationTime.addWatcher( new TextBoxWidgetWatcher() {
//         public void textBoxKeyPress(TextBoxWidgetExt tbw) {
// 	    try {
//                 String ots = tbw.getText();
//                 double ot = Double.parseDouble(ots);
//                 if (ot > 0.00001) {
//                     _instUIST.setObsTimeOT(ot);
//                     _instUIST.setCoadds(0);
//                     _updateWidgets(_w.dataAcq_observationTime);
// 		}
// 	    }catch( Exception ex) {
// 	        // ignore
//             }
//         }
//         public void textBoxAction(TextBoxWidgetExt tbw) {} // ignore
//     });

    //
    // Default observation time
    //
    //    _w.dataAcq_defaultObsTime.addWatcher( new CommandButtonWidgetWatcher() {
    //  public void commandButtonAction(CommandButtonWidgetExt cbw) {
    //      _instUIST.useDefaultObservationTime();
    //      _updateObsTime();
    //      _updateWidgets();
    //  }
    //});

}


/**
 * Override method in super class to avoid exposure time and position angle text box watchers
 * being added twice.
 */
protected void _init() { }


/**
 * Override setup to store away a reference to the SpInstUIST item.
 */
public void
setup(SpItem spItem)
{
   _instUIST = (SpInstUIST) spItem;
// Added by RDK
   _instUIST.avTableUpdate();
//Edn of added by RDK
   haveInitialised = false;
   super.setup(spItem);
}


/**
 * Implements the _updateWidgets method from OtItemEditor in order to
 * setup the widgets to show the current values of the item.
 */
protected void _updateWidgets()
{
    _updateWidgets(null);
}

protected void
_updateWidgets(Object source)
{
    if (!haveInitialised) {
        // Load drop down lists only first time in
        _updateCameraChoices();
        _showCamera();
        _updateImagerChoices();
        _updateImager();
        _updateFilterCategory();
        _updateFilterChoices();
        _updateDisperserChoices();
        _updateDisperser();
        _updateMaskChoices();
        _updatePolarimetry();
        _updateTargetAcqMode();
        _updatePupilCamera();
        _updateFilter();
// Commented out by RDK
//        _updateReadoutChoices();
// End of commented by RDK
// Added by RDK
        _updateReadModeChoices();
        _updateReadAreaChoices();
// End of added by RDK
        _updateObsTime();
        haveInitialised = true;
    }
    _updateDisperser();
    _updateImager();
    if (!_instUIST.isImaging()) {
        _updatePosAngle();
        _updateSpecFilter();
        _updateResolution();
        _updateOrder();
    } else {
        if ((_w.filterBroadBand == source) || (_w.filterNarrowBand == source)) {
        } else {
            _updateFilterCategory();
        }
        if (_w.imaging_filter != source) {
            _updateFilter();
	}
    }
    _updateSourceMagChoices();
    _updateSourceMag();
    _updateWavelengthCoverage();
    _updateMask();
// Commented for testing by RDK 30 Dec 2002
//     if (_w.dataAcq_readout != source) {
//         _updateReadoutChoices();
//         _updateReadout();
//     }
// Added for testing by RDK 30 Dec 2002
//    _updateReadoutChoices();
//    _updateReadout();

// Added by RDK
    if (_w.dataAcq_readMode != source) {
        _updateReadModeChoices();
        _updateReadMode();
    }
    if (_w.dataAcq_readArea != source) {
        _updateReadAreaChoices();
        _updateReadArea();
    }

// End of added by RDK

    _updateAcquisition(source);
    _updateScienceFOV();
    _updateChopFreq();
    _updateDutyCycle();
// Commented for testing by RDK
//    _updateCoadds();
    _updateExpTime();
    _updateObsTime();

    //super._updateWidgets();
    TextBoxWidgetExt tbwe;
    //    tbwe = getPosAngleTextBox();
    //    tbwe.setText( ((SpInstObsComp) _spItem).getPosAngleDegreesStr() );

    if(_w.dataAcq_exposureTime != source) {
      String ets = _instUIST.getExpTimeOTString();
      _w.dataAcq_exposureTime.setText(ets);
    }
// Added by RDK

    if(_w.dataAcq_coadds != source) {
      String coadds = _instUIST.getCoaddsString();
      _w.dataAcq_coadds.setText(coadds);
    }
// End of added by RDK
}

//
// Update the observation time OT
//
// Commented for testing by RDK 30 Dec 2002
//private void
//_updateObsTimeOT()
//{
//    String obts = _instUIST.getObsTimeOTString();
//    _w.dataAcq_observationTime.setText(obts);
//}

//
// Update the actual observation time
//
private void
_updateObsTime()
{
    String aobts = _instUIST.getObservationTimeString();
    _w.dataAcq_actualObservationTime.setText(aobts);
}

//
// Update the actual exposure time
//
private void
_updateExpTime()
{
    String aexpts = _instUIST.getExposureTimeString();
    _w.dataAcq_actualExposureTime.setText(aexpts);
}

//
// Update the polarimetry check box
//
private void
_updatePolarimetry()
{

    String p = _instUIST.getPolarimetry();
    _w.polarimetry.setValue(p.equalsIgnoreCase("yes"));
}

// Added by RDK
//
// Update the pupil camera check box
//
private void
_updatePupilCamera()
{

    String p = _instUIST.getPupilImaging();
    _w.imaging_pupilCamera.setValue(p.equalsIgnoreCase("yes"));
}
//
// Update the target acquisition check box
//
private void
_updateTargetAcqMode()
{

    String t = _instUIST.getTargetAcq();
    _w.spectroscopy_targetAcqMode.setValue(t.equalsIgnoreCase("yes"));
}

// End of added by RDK

//
// Update the resolution
//
private void
_updateResolution()
{
    _w.spectroscopy_resolution.setText(_instUIST.getResolutionString());
}

//
// Update the order
//
private void
_updateOrder()
{
    _w.spectroscopy_order.setText(_instUIST.getOrderString());
}

//
// Update the position angle
//
private void
_updatePosAngle()
{
    double pa = _instUIST.getPosAngleDegrees();
    _w.spectroscopy_posAngle.setText(Double.toString(pa));
}


//
// Update the science field of view displays on both the 
// imaging and spectroscopy panes
//
private void
_updateScienceFOV()
{
    String scienceArea = _instUIST.getScienceAreaString();
    _w.imaging_fieldOfView.setText(scienceArea);
    _w.spectroscopy_fieldOfView.setText(scienceArea);
}

//
// Update the wavelength coverage on both the
// imaging and spectroscopy panes
//
private void
_updateWavelengthCoverage()
{
    String spectralCoverage = _instUIST.getSpectralCoverage();
    _w.spectroscopy_coverage.setText(spectralCoverage);
    _w.imaging_bandpass.setText(spectralCoverage);
}

//
// Update the imaging filter
//
private void
_updateFilter()
{
    _w.imaging_filter.setValue(_instUIST.getFilter());
}

//
// Update the spectroscopy filter
//
private void
_updateSpecFilter()
{
    _w.spectroscopy_filter.setValue(_instUIST.getFilter());
}

//
// Update the list of available cameras
//
private void
_updateCameraChoices()
{
    String choices[] = new String[_instUIST.getCameraList().length];
    choices = _instUIST.getCameraList();
    _w.camera.setChoices(choices);
}

//
// Update the list of available imagers
//
private void
_updateImagerChoices()
{
    String choices[] = new String[_instUIST.getImagerList().length];
    choices = _instUIST.getImagerList();
    _w.imaging_imagerList.setChoices(choices);
}

//
// Update the list of source magnitude choices
//
private void
_updateSourceMagChoices()
{
    int numChoices = _instUIST.getSourceMagList().length;
    String choices[] = new String[numChoices];
    choices = _instUIST.getSourceMagList();
    _w.imaging_sourceMag.setMaximumRowCount(numChoices);
    _w.imaging_sourceMag.setChoices(choices);
    _w.spectroscopy_sourceMag.setMaximumRowCount(numChoices);
    _w.spectroscopy_sourceMag.setChoices(choices);
}

//
// Update the source magnitude
//
private void
_updateSourceMag()
{
    String sourceMag = _instUIST.getSourceMag();
    _w.imaging_sourceMag.setValue(sourceMag);
    _w.spectroscopy_sourceMag.setValue(sourceMag);
}

// Commented out by RDK
//
// Update the list of readout choices
//
// private void
// _updateReadoutChoices()
// {
//     String choices[] = new String[_instUIST.getReadoutChoices().length];
// Commented for testing by RDK 30 Dec 2002
//    choices = _instUIST.getReadoutChoices();
//    _w.dataAcq_readout.setChoices(choices);
// Added for testing by RDK 30 Dec 2002
//     choices[0] = "NDSTARE";
//     _w.dataAcq_readMode.setChoices(choices);
//     choices[0] = "1024x1024";
//     _w.dataAcq_readArea.setChoices(choices);
// }

//
// Update the readout
//
// private void
// _updateReadout()
// {
//     String readout = _instUIST.getReadoutOT();
// Commented for testing by RDK 30 Dec 2002
//    _w.dataAcq_readout.setValue(readout);
// Added for testing by RDK 30 Dec 2002
//     String readMode = "NDSTARE";
//     _w.dataAcq_readMode.setValue(readMode);
//     String readArea = "1024x1024";
//     _w.dataAcq_readArea.setValue(readArea);
// }
// End of commented out by RDK

// Added by RDK
//
// Update the list of read mode choices
//
private void
_updateReadModeChoices()
{
    String choices[] = new String[_instUIST.getReadModeChoices().length];
    choices = _instUIST.getReadModeChoices();
    _w.dataAcq_readMode.setChoices(choices);
}

//
// Update the read mode
//
private void
_updateReadMode()
{
    String readMode = _instUIST.getReadMode();
    _w.dataAcq_readMode.setValue(readMode);
}

// Update the list of read area choices
//
private void
_updateReadAreaChoices()
{
    String choices[] = new String[_instUIST.getReadAreaChoices().length];
    choices = _instUIST.getReadAreaChoices();
    _w.dataAcq_readArea.setChoices(choices);
}

//
// Update the read area
//
private void
_updateReadArea()
{
    String readArea = _instUIST.getReadAreaString();
    _w.dataAcq_readArea.setValue(readArea);
}
// End of added by RDK

//
// Update the mask
//
private void
_updateMask()
{
    _w.spectroscopy_mask.setValue(_instUIST.getMask());
}

//
// Update the imager
//
private void
_updateImager()
{
    _w.imaging_imagerList.setValue(_instUIST.getImager());
}


//
// Update the list of available filters
//
private void
_updateFilterChoices()
{
    _w.imaging_filter.setChoices(_instUIST.getFilterList());
    _updateFilter();
}

//
// Update the list of available dispersers
//
private void
_updateDisperserChoices()
{
    int numChoices = _instUIST.getDisperserList().length;
    String choices[] = new String[numChoices];
    _w.spectroscopy_grism.setMaximumRowCount(numChoices);
    _w.spectroscopy_grism.setChoices(_instUIST.getDisperserList());
}

//
// Update the disperser
//
private void
_updateDisperser()
{
    _w.spectroscopy_grism.setValue(_instUIST.getDisperser());
}

//
// Update the list of available masks
//
private void
_updateMaskChoices()
{
    _w.spectroscopy_mask.setChoices(_instUIST.getMaskList());
    _w.spectroscopy_mask.setValue(_instUIST.getMask());
}

//
// Update the chop frequency
//
private void
_updateChopFreq()
{
    _w.dataAcq_chopFrequency.setText(_instUIST.getChopFreqRound());
}

//
// Update the duty cycle
//
private void
_updateDutyCycle()
{
    _w.dataAcq_dutyCycle.setText(_instUIST.getDutyCycleRound());
}

//
// Update the coadds
//
private void
_updateCoadds()
{
    _w.dataAcq_coadds.setText(_instUIST.getCoaddsString());
}

//
// Update the acquisition
//
private void
_updateAcquisition(Object source)
{
    _instUIST.setAcquisition();
    if(_w.dataAcq_exposureTime != source) {
      String ets = _instUIST.getExpTimeOTString();
      _w.dataAcq_exposureTime.setText(ets);
    }

// Added by RDK

    if(_w.dataAcq_coadds != source) {
      String coadds = _instUIST.getCoaddsString();
      _w.dataAcq_coadds.setText(coadds);
    }
// End of added by RDK

// Commented for testing by RDK 30 Dec 2002
//    if(_w.dataAcq_observationTime != source) {
//     String ots = _instUIST.getObsTimeOTString();
//      _w.dataAcq_observationTime.setText(ots);
//    }

}

//
// Update camera
//
private void
_updateCamera(String camera)
{
    _instUIST.setCamera( camera );
    if (camera.equals("imaging")) {
        _updateFilterChoices();
    } else {
        _updateDisperserChoices();
        _updateMaskChoices();
    }
    _instUIST.useDefaultAcquisition();
    _showCamera();
}

//
// Show camera
//
private void
_showCamera()
{

    String camera = _instUIST.getCamera();
    _w.camera.setValue(_instUIST.getCamera());
    // Make the appropriate imaging or spectroscopy config area visible
    if (camera.equals("imaging")) {
        ((CardLayout)(_w.modePanel.getLayout())).show(_w.modePanel, "imagingPanel");
    } else {
        ((CardLayout)(_w.modePanel.getLayout())).show(_w.modePanel, "spectroscopyPanel");
    }
    if (camera.equals("ifu")) {
        _w.polarimetry.setEnabled(false);
    } else {
        _w.polarimetry.setEnabled(true);
    }

}

    /** Return the position angle text box */
    public TextBoxWidgetExt getPosAngleTextBox() {
      return _w.spectroscopy_posAngle;
    }

    /** Return the exposure time text box */
    public TextBoxWidgetExt getExposureTimeTextBox() {
      return _w.dataAcq_exposureTime;
    }

    /** Return the coadds text box, or null if not available. */
    public TextBoxWidgetExt getCoaddsTextBox() {
      // UIST does not have a coadds text box.
// Commented by RDK
//      return new TextBoxWidgetExt();
// End of commented by RDK
// Added by RDK
      return _w.dataAcq_coadds;
// End of added by RDK
    }

//
// Update filterCategoary
//
private void
_updateFilterCategory()
{
    OptionWidgetExt ow = null;
    String filterCategory = _instUIST.getFilterCategory();
    if (filterCategory.equalsIgnoreCase("broad")) {
        ow = (OptionWidgetExt) _w.filterBroadBand;
        ow.setValue(true);
        ow = (OptionWidgetExt) _w.filterNarrowBand;
        if (_instUIST.getNarrowFilterSet() == 0) {
            ow.setEnabled(false);
	} else {
            ow.setEnabled(true);
	}
    } else {
        ow = (OptionWidgetExt) _w.filterNarrowBand;
        ow.setValue(true);
    }
}

/**
 *
 */
public void actionPerformed(ActionEvent evt) {

   Object w  = evt.getSource();

   if (_w.filterBroadBand == w) {
       _instUIST.setFilterCategory("broad");

   } else if (_w.filterNarrowBand == w) {
       _instUIST.setFilterCategory("narrow");
      }
   _updateFilterChoices();
   _instUIST.useDefaultAcquisition();
   _updateWidgets(w);
   return;
}

}
