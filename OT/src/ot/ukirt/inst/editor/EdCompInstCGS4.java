/*==============================================================*/
/*                                                              */
/*                UK Astronomy Technology Centre                */
/*                 Royal Observatory, Edinburgh                 */
/*                 Joint Astronomy Centre, Hilo                 */
/*                   Copyright (c) PPARC 1999                   */
/*                                                              */
/*==============================================================*/

package ot.ukirt.inst.editor;

//import ot_ukirt.util.*;
import orac.util.LookUpTable;
import orac.ukirt.inst.SpInstCGS4;

import jsky.app.ot.gui.CheckBoxWidgetExt;
import jsky.app.ot.gui.CheckBoxWidgetWatcher;
import jsky.app.ot.gui.DropDownListBoxWidgetExt;
import jsky.app.ot.gui.DropDownListBoxWidgetWatcher;
import jsky.app.ot.gui.CommandButtonWidgetExt;
import jsky.app.ot.gui.CommandButtonWidgetWatcher;
import jsky.app.ot.gui.TextBoxWidgetExt;
import jsky.app.ot.gui.TextBoxWidgetWatcher;

import gemini.sp.SpItem;
import jsky.app.ot.util.MathUtil;

import java.awt.Event;
import java.util.Vector;

import jsky.app.ot.tpe.TelescopePosEditor;
import jsky.app.ot.tpe.TpeManager;


/**
 * This is the editor for the CGS4 instrument Observation Component
 */
public final class EdCompInstCGS4 extends EdCompInstBase
{
   private EdChopCapability  _edChopCapability;
   private EdStareCapability _edStareCapability;
   private SpInstCGS4 _instCGS4;

   private Cgs4GUI _w;		// the GUI layout

/**
 * The constructor initializes the title, description, and presentation source.
 */
public EdCompInstCGS4()
{
   _title       ="CGS4";
   _presSource  = _w = new Cgs4GUI();
   _description ="The CGS4 instrument is configured with this component.";

//   _edChopCapability  = new EdChopCapability();
//   _edStareCapability = new EdStareCapability();
}

/**
 * This method initializes the widgets in the presentation to reflect the
 * current values of the items attributes.
 */
protected void
_init()
{
   CommandButtonWidgetExt   cbw;
   DropDownListBoxWidgetExt ddlbw;
   TextBoxWidgetExt         tbw;

   //
   // Disperser
   //
   ddlbw = (DropDownListBoxWidgetExt) _w.disperser;
   LookUpTable disps = SpInstCGS4.DISPERSERS;
   Vector         v = new Vector();
   for (int i=0; i<SpInstCGS4.getDispersers().getNumRows(); ++i) {
      String res = (String) disps.elementAt(i,1);
      if (i==2) res = "40000";
      v.addElement(disps.elementAt(i,0) + " (R\u007e" + res + ")");
   }
   ddlbw.setChoices(v);

   ddlbw.addWatcher( new DropDownListBoxWidgetWatcher() {
      public void
      dropDownListBoxSelect(DropDownListBoxWidgetExt dd, int i, String val) {}

      public void
      dropDownListBoxAction(DropDownListBoxWidgetExt dd, int i, String val) {
         _instCGS4.setDisperser( _dispTrim(val) );
         _updateOrder();
	 _updateFilter();
         _instCGS4.useDefaultAcquisition();
         _updateWidgets();
      }
   });

   //
   // source magnitude
   //
   ddlbw = (DropDownListBoxWidgetExt) _w.sourceMag;
   ddlbw.setChoices(SpInstCGS4.SRCMAGS);

   ddlbw.addWatcher( new DropDownListBoxWidgetWatcher() {
      public void
      dropDownListBoxSelect(DropDownListBoxWidgetExt dd, int i, String val) {}

      public void
      dropDownListBoxAction(DropDownListBoxWidgetExt dd, int i, String val) {
         _instCGS4.setSourceMagnitude( val );
         _instCGS4.useDefaultAcquisition();
         _updateWidgets();
      }
   });

   //
   // sampling
   //
   ddlbw = (DropDownListBoxWidgetExt) _w.sampling;
   ddlbw.setChoices(SpInstCGS4.SAMPLINGS);

   ddlbw.addWatcher( new DropDownListBoxWidgetWatcher() {
      public void
      dropDownListBoxSelect(DropDownListBoxWidgetExt dd, int i, String val) {}

      public void
      dropDownListBoxAction(DropDownListBoxWidgetExt dd, int i, String val) {
         _instCGS4.setSampling( val );
         _updateWidgets();
      }
   });

   //
   // Mask
   //
   ddlbw = (DropDownListBoxWidgetExt) _w.mask;
   ddlbw.setChoices(SpInstCGS4.MASKS.getColumn(0));

   ddlbw.addWatcher( new DropDownListBoxWidgetWatcher() {
      public void
      dropDownListBoxSelect(DropDownListBoxWidgetExt dd, int i, String val) {}

      public void
      dropDownListBoxAction(DropDownListBoxWidgetExt dd, int i, String val) {
         _instCGS4.setMask( val );
         _updateWidgets();

         TelescopePosEditor tpe = TpeManager.get(_instCGS4);
         if (tpe != null) tpe.repaint();
      }
   });

   //
   // Polariser
   //
   ddlbw = (DropDownListBoxWidgetExt) _w.polariser;
   ddlbw.setChoices(SpInstCGS4.POLARISERS);

   ddlbw.addWatcher( new DropDownListBoxWidgetWatcher() {
      public void
      dropDownListBoxSelect(DropDownListBoxWidgetExt dd, int i, String val) {}

      public void
      dropDownListBoxAction(DropDownListBoxWidgetExt dd, int i, String val) {
         _instCGS4.setPolariser( val );
         _updateWidgets();
      }
   });

   //
   // Acq. Mode
   //
   ddlbw = (DropDownListBoxWidgetExt) _w.acqMode;
   ddlbw.setChoices(SpInstCGS4.MODES);

   ddlbw.addWatcher( new DropDownListBoxWidgetWatcher() {
      public void
      dropDownListBoxSelect(DropDownListBoxWidgetExt dd, int i, String val) {}

      public void
      dropDownListBoxAction(DropDownListBoxWidgetExt dd, int i, String val) {
         _instCGS4.setMode( val );
         _updateSecondaryAcqMode();
         _updateWidgets();
      }
   });

   //
   // Central Wavelength
   //
   tbw = (TextBoxWidgetExt) _w.centralWavelength;
   tbw.addWatcher( new TextBoxWidgetWatcher() {
      public void textBoxKeyPress(TextBoxWidgetExt tbw) {
         _instCGS4.setCentralWavelength( tbw.getText() );
         _updateWavelengthCoverage();
         _updateResolution();
         _updateOrder();
         _updateFilter();
         _instCGS4.useDefaultAcquisition();
	 _updateExpInfo();
	 _updateSecondaryAcqMode();
      }

      public void textBoxAction(TextBoxWidgetExt tbw) {} // ignore
   });

   //
   // CVF offset
   //
   tbw = (TextBoxWidgetExt) _w.cvfOffset;
   tbw.addWatcher( new TextBoxWidgetWatcher() {
      public void textBoxKeyPress(TextBoxWidgetExt tbw) {
         _instCGS4.setCvfOffset( tbw.getText() );
	 //         _updateWidgets();
      }

      public void textBoxAction(TextBoxWidgetExt tbw) {} // ignore
   });

   //
   // Order
   //
   tbw = (TextBoxWidgetExt) _w.order;
   tbw.addWatcher( new TextBoxWidgetWatcher() {
      public void textBoxKeyPress(TextBoxWidgetExt tbw) {
         _instCGS4.setOrder( tbw.getText() );
	 _updateWavelengthCoverage();
	 _updateResolution();
      }

      public void textBoxAction(TextBoxWidgetExt tbw) {} // ignore
   });

   cbw = (CommandButtonWidgetExt) _w.defaultOrder;
   cbw.addWatcher( new CommandButtonWidgetWatcher() {
      public void commandButtonAction(CommandButtonWidgetExt cbw) {
         _instCGS4.useDefaultOrder();
         _updateWidgets();
      }
   });

   CheckBoxWidgetExt cbwe;
   cbwe = (CheckBoxWidgetExt) _w.useND;
   cbwe.addWatcher( new CheckBoxWidgetWatcher() {
      public void checkBoxAction(CheckBoxWidgetExt cbw) {
         _instCGS4.setNdFilter(cbw.getBooleanValue());
         _updateWidgets();
      }
   });

   tbw = (TextBoxWidgetExt) _w.exposureTime;
   tbw.addWatcher( new TextBoxWidgetWatcher() {
      public void textBoxKeyPress(TextBoxWidgetExt tbw) {
         _instCGS4.setExpTime(tbw.getText());
      }

      public void textBoxAction(TextBoxWidgetExt tbw) {}
   });

   cbw = (CommandButtonWidgetExt) _w.defaultAcquisition;
   cbw.addWatcher( new CommandButtonWidgetWatcher() {
      public void commandButtonAction(CommandButtonWidgetExt cbw) {
         _instCGS4.useDefaultAcquisition();
         _updateWidgets();
      }
   });

   super._init();
// mf ??    GroupWidget gw;
// mf ?? 
// mf ??    gw = (GroupWidget) _w.chopControlGroup;
// mf ?? //   _edChopCapability._init(gw, this);
// mf ??    tbw = (TextBoxWidgetExt) gw.getWidget("expPerChopPos"); 
// mf ??    tbw.addWatcher( new TextBoxWidgetWatcher() {
// mf ??       public void textBoxKeyPress(TextBoxWidgetExt tbw) {
// mf ??          _instCGS4.setExpPerChopPos(tbw.getText());
// mf ??       }
// mf ?? 
// mf ??       public void textBoxAction(TextBoxWidgetExt tbw) {}
// mf ??    });
// mf ??    tbw = (TextBoxWidgetExt) gw.getWidget("cyclesPerObs"); 
// mf ??    tbw.addWatcher( new TextBoxWidgetWatcher() {
// mf ??       public void textBoxKeyPress(TextBoxWidgetExt tbw) {
// mf ??          _instCGS4.setCyclesPerObs(tbw.getText());
// mf ??       }
// mf ?? 
// mf ??       public void textBoxAction(TextBoxWidgetExt tbw) {}
// mf ??    });
// mf ?? 
// mf ??    gw = (GroupWidget) _w.stareControlGroup;
// mf ?? //   _edStareCapability._init(gw, this);
// mf ??    tbw = (TextBoxWidgetExt) gw.getWidget("coadds");
// mf ??    tbw.addWatcher( new TextBoxWidgetWatcher() {
// mf ??       public void textBoxKeyPress(TextBoxWidgetExt tbw) {
// mf ??          _instCGS4.setNoCoadds(tbw.getText());
// mf ??       }
// mf ?? 
// mf ??       public void textBoxAction(TextBoxWidgetExt tbw) {}
// mf ??    });


}

//
// Trim a disperser selection
//
private String
_dispTrim(String disperser)
{
  return disperser.substring(0, disperser.lastIndexOf('(')).trim();
}

//
// Add the resolution to a disperser selection
//
private String
_fullDispName(String disperser)
{
   LookUpTable d = SpInstCGS4.DISPERSERS;
   try {
     int i = d.indexInColumn (disperser, 0);
     String res = (String) d.elementAt(i,1);
     if (i==2) res = "40000";
     return (d.elementAt(i,0) + " (R\u007e" + res + ")");
   }catch (Exception ex) {
     return null;
   }
}

/**
 * Override setup to store away a reference to the SpInstCGS4 item.
 */
public void
setup(SpItem spItem)
{
   _instCGS4 = (SpInstCGS4) spItem;
   super.setup(spItem);
}

/**
 * Implements the _updateWidgets method from OtItemEditor in order to
 * setup the widgets to show the current values of the item.
 */
protected void
_updateWidgets()
{
   DropDownListBoxWidgetExt ddlbw;
   TextBoxWidgetExt         tbw;
   CheckBoxWidgetExt        cbwe;

   ddlbw        = (DropDownListBoxWidgetExt) _w.acqMode;
   ddlbw.setValue( _instCGS4.getMode() );

   ddlbw        = (DropDownListBoxWidgetExt) _w.disperser;
   ddlbw.setValue( _fullDispName(_instCGS4.getDisperser()) );

   ddlbw         = (DropDownListBoxWidgetExt) _w.sourceMag;
   ddlbw.setValue( _instCGS4.getSourceMagnitude () );

   ddlbw         = (DropDownListBoxWidgetExt) _w.mask;
   ddlbw.setValue( _instCGS4.getMask() );

   ddlbw         = (DropDownListBoxWidgetExt) _w.polariser;
   ddlbw.setValue( _instCGS4.getPolariser() );

   ddlbw         = (DropDownListBoxWidgetExt) _w.sampling;
   ddlbw.setValue( _instCGS4.getSampling () );

   tbw = (TextBoxWidgetExt) _w.centralWavelength;
   double centralWavelength = _instCGS4.getCentralWavelength();
   tbw.setText( Double.toString(centralWavelength) );

   tbw = (TextBoxWidgetExt) _w.order;
   int order = _instCGS4.getOrder();
   tbw.setText( Integer.toString(order) );

// mf ??    tbw = (TextBoxWidgetExt) _w.filter;
// mf ??    tbw.setText(_instCGS4.getFilter());

   cbwe = (CheckBoxWidgetExt) _w.useND;
   cbwe.setValue(_instCGS4.getNdFilter());

   tbw = (TextBoxWidgetExt) _w.cvfOffset;
   tbw.setValue(_instCGS4.getCvfOffset());

   _updateScienceFOV();
   _updateWavelengthCoverage();
   _updateResolution();

   super._updateWidgets();

   _updateExpInfo();
   _updateSecondaryAcqMode();
}

//
// Update the science field of view based upon the camera and mask
// settings.
//
private void
_updateScienceFOV()
{
   TextBoxWidgetExt tbw = (TextBoxWidgetExt) _w.scienceFOV;
   double[] scienceArea = _instCGS4.getScienceArea();

   double w = MathUtil.round(scienceArea[0], 2);
   double h = MathUtil.round(scienceArea[1], 2);

   tbw.setText(w + " x " + h);
}

//
// Update the order based upon the wavelength and disperser
//
private void
_updateOrder()
{
   TextBoxWidgetExt tbw = (TextBoxWidgetExt) _w.order;
   int o = _instCGS4.getDefaultOrder();
   _instCGS4.setOrder(o);
   tbw.setText(Integer.toString(o));
}

//
// Update the filter
//
private void
_updateFilter()
{
// mf ??    TextBoxWidgetExt tbw = (TextBoxWidgetExt) _w.filter;
// mf ??    String filter = _instCGS4.getDefaultFilter();   
// mf ??    _instCGS4.setFilter (filter);
// mf ??    tbw.setText (filter);
}

//
// Update the exposure time and coadds
//
private void
_updateExpInfo()
{
   TextBoxWidgetExt tbw = (TextBoxWidgetExt) _w.exposureTime;
   double d = _instCGS4.getExpTime();
   String e = Double.toString(d);
   _instCGS4.setExpTime(e);
   tbw.setText (e);

   String mode = _instCGS4.getMode();
   if (mode.equals("CHOP")) {
// mf ??       GroupWidget chopGW  = (GroupWidget) _pres.getWidget("chopControlGroup");
// mf ??       tbw = (TextBoxWidgetExt) chopGW.getWidget("expPerChopPos"); 
// mf ??       int expPCP = _instCGS4.getExpPerChopPos();
// mf ??       _instCGS4.setExpPerChopPos(expPCP);
// mf ??       tbw.setText (Integer.toString(expPCP));
// mf ??       tbw = (TextBoxWidgetExt) chopGW.getWidget("cyclesPerObs"); 
// mf ??       int cycPO = _instCGS4.getCyclesPerObs();
// mf ??       _instCGS4.setCyclesPerObs(cycPO);
// mf ??       tbw.setText (Integer.toString(cycPO));
   }else{
// mf ??       GroupWidget stareGW = (GroupWidget) _pres.getWidget("stareControlGroup");
// mf ??       tbw = (TextBoxWidgetExt) stareGW.getWidget("coadds"); 
// mf ??       int coadds = _instCGS4.getNoCoadds();
// mf ??       _instCGS4.setNoCoadds(coadds);
// mf ??       tbw.setText (Integer.toString(coadds));

/* mf ?? */      tbw = (TextBoxWidgetExt) _w.coadds; 
/* mf ?? */      int coadds = _instCGS4.getNoCoadds();
/* mf ?? */      _instCGS4.setNoCoadds(coadds);
/* mf ?? */      tbw.setText (Integer.toString(coadds));

   }

}

//
// Update the secondary acqmode items based upon the acq Mode.
//
private void
_updateSecondaryAcqMode()
{
   String mode = _instCGS4.getMode();

   DropDownListBoxWidgetExt ddlwe;

// mf ??    GroupWidget chopGW  = (GroupWidget) _pres.getWidget("chopControlGroup");
// mf ??    GroupWidget stareGW = (GroupWidget) _pres.getWidget("stareControlGroup");
// mf ??    if (mode.equals("CHOP")) {
// mf ??       chopGW.setVisible(true);
// mf ??       stareGW.setVisible(false);
//      _edChopCapability._updateWidgets(chopGW, _instCGS4.getChopCapability());
// mf ??    } else {
// mf ??       chopGW.setVisible(false);
// mf ??       stareGW.setVisible(true);
// mf ?? //      _edStareCapability._updateWidgets(stareGW, _instCGS4.getStareCapability());
// mf ??   }
}

//
// Update the wavelength coverage based upon the grating and central
// wavelength.
//
private void
_updateWavelengthCoverage()
{
   TextBoxWidgetExt tbw;
   tbw = (TextBoxWidgetExt) _w.wavelengthCoverage;

   double coverage[] = _instCGS4.getWavelengthCoverage();
   //  If resolution > 10000  (arbitrary) display 4 dec. pl., otherwise 2.
   int dp = 2;
   if (_instCGS4.getResolution() > 10000) dp = 4;
   double ll = MathUtil.round(coverage[0], dp);
   double ul = MathUtil.round(coverage[1], dp);
   tbw.setText(ll + "-" + ul);
}

//
// Update the resolution
//
private void
_updateResolution()
{
   TextBoxWidgetExt tbw;
   tbw = (TextBoxWidgetExt) _w.resolution;

   int res = (new Double (_instCGS4.getResolution())).intValue();
   tbw.setText (Integer.toString(res));
}

public TextBoxWidgetExt getCoaddsTextBox() {
  return new TextBoxWidgetExt();
}

public TextBoxWidgetExt getExposureTimeTextBox() {
  return new TextBoxWidgetExt();
}

public TextBoxWidgetExt getPosAngleTextBox() {
  return new TextBoxWidgetExt();
}

}
