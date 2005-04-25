/*==============================================================*/
/*                                                              */
/*                UK Astronomy Technology Centre                */
/*                 Royal Observatory, Edinburgh                 */
/*                 Joint Astronomy Centre, Hilo                 */
/*                   Copyright (c) PPARC 1999                   */
/*                                                              */
/*==============================================================*/

package ot.jcmt.inst.editor;

import gemini.sp.*;
import gemini.sp.obsComp.*;
import orac.jcmt.inst.SpInstHeterodyne;
import orac.jcmt.inst.SpInstSCUBA;
import jsky.app.ot.gui.KeyPressWatcher;
import jsky.app.ot.gui.TextBoxWidgetExt;
import jsky.app.ot.gui.TextBoxWidgetWatcher;
import jsky.app.ot.gui.TableWidgetExt;
import jsky.app.ot.gui.TableWidgetWatcher;
import jsky.app.ot.gui.CommandButtonWidgetExt;
import jsky.app.ot.gui.CommandButtonWidgetWatcher;
import jsky.app.ot.gui.DropDownListBoxWidgetExt;
import jsky.app.ot.gui.DropDownListBoxWidgetWatcher;
import jsky.app.ot.gui.CheckBoxWidgetExt;
import jsky.app.ot.gui.CheckBoxWidgetWatcher;
//import jsky.app.ot.gui.util.ErrorBox;
import ot.util.DialogUtil;

//import ot_ukirt.util.*;
import jsky.app.ot.editor.OtItemEditor;
import orac.jcmt.inst.SpDRRecipe;
import orac.util.LookUpTable;
import edfreq.EdFreq;
import edfreq.region.SpectralRegionEditor;

import java.awt.event.KeyEvent;
import java.awt.CardLayout;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.text.DecimalFormat;
import java.util.Hashtable;
import java.util.Vector;
import javax.swing.ButtonGroup;
import javax.swing.JRadioButton;
import javax.swing.JFrame;

// MFO: Preliminary. Get rid of GUI stuff inside this class.
import javax.swing.JComponent;
import java.awt.BorderLayout;

/**
 * This is the editor for the DRRecipe item.
 */
public final class EdDRRecipe extends OtItemEditor
                    implements KeyPressWatcher, 
		    TextBoxWidgetWatcher, 
		    TableWidgetWatcher,
		    DropDownListBoxWidgetWatcher, 
		    ActionListener {

  private static final String INST_STR_SCUBA      = "scuba";
  private static final String INST_STR_HETERODYNE = "heterodyne";

  private SpDRRecipe  _spDRRecipe;
  private SpInstObsComp _inst;
  private String      _currentRecipeSelected;
  private String      _instStr;
  private Hashtable   _baselineToButtonTable;

  private DRRecipeGUI _w;		// the GUI layout

  private DataReductionScreen _dataReductionScreen = new DataReductionScreen();

  private SpectralRegionDialog _spectralRegionFrame = new SpectralRegionDialog();
  private VelocityRegionDialog _velocityRegionFrame = new VelocityRegionDialog();

//  private boolean _ignoreEvents = false;

/**
 * The constructor initializes the title, description, and presentation source.
 */
public EdDRRecipe()
{
   _title       = "DR Recipe";
   _presSource  = _w = new DRRecipeGUI();;
   _description = "Enter the Data Reduction recipe to be used"; // for each observation type";
   _resizable   = true;

   _dataReductionScreen.recipe.setChoices(SpDRRecipe.DR_RECIPES);
   _dataReductionScreen.channelBinning.setChoices(SpDRRecipe.CHANNEL_BINNINGS);
   _dataReductionScreen.baselineRegionUnits.setChoices(SpDRRecipe.BASELINE_REGION_UNITS);
   _dataReductionScreen.regriddingMethod.setChoices(SpDRRecipe.REGRIDDING_METHODS);
   _dataReductionScreen.windowType.setChoices(SpDRRecipe.WINDOW_TYPES);
   _dataReductionScreen.baselineFittingPolynomial.setChoices(SpDRRecipe.POLYNOMIALS);
   _w.projection.setChoices(SpDRRecipe.PROJECTION_TYPES);
   _w.gridFunction.setChoices(SpDRRecipe.GRID_FUNCTION_TYPES);

   ButtonGroup bg1 = new ButtonGroup();
   bg1.add(_dataReductionScreen.noBaselineButton);
   bg1.add(_dataReductionScreen.automaticBaselineButton);
   bg1.add(_dataReductionScreen.manualBaselineButton);

   _baselineToButtonTable = new Hashtable();
   _baselineToButtonTable.put( SpDRRecipe.BASELINE_SELECTION[0],
	   _dataReductionScreen.noBaselineButton);
   _baselineToButtonTable.put( SpDRRecipe.BASELINE_SELECTION[1],
	   _dataReductionScreen.automaticBaselineButton);
   _baselineToButtonTable.put( SpDRRecipe.BASELINE_SELECTION[2],
	   _dataReductionScreen.manualBaselineButton);


   _dataReductionScreen.recipe.setEnabled(false);

   _dataReductionScreen.deSpike.setEnabled(false);

   _w.tabbedPaneHet.add(_dataReductionScreen, "ACSIS DR", 0);
   _w.tabbedPaneHet.setEnabledAt(1, false);

   _dataReductionScreen.truncation.addWatcher(this);
   _dataReductionScreen.automaticBaseline.addWatcher(this);
//   _dataReductionScreen.baselineFitting.addWatcher(this);
//   _dataReductionScreen.lineRegions.addWatcher(this);
   _dataReductionScreen.baselineRegions.addWatcher(this);
   _dataReductionScreen.baselineRegionUnits.addWatcher(this);
   _dataReductionScreen.baselineFittingPolynomial.addWatcher(this);
   _dataReductionScreen.channelBinning.addWatcher(this);
   _dataReductionScreen.regriddingMethod.addWatcher(this);
   _dataReductionScreen.windowType.addWatcher(this);
   _dataReductionScreen.showSpectralRegionEditor.addActionListener(this);
   _dataReductionScreen.noBaselineButton.addActionListener(this);
   _dataReductionScreen.automaticBaselineButton.addActionListener(this);
   _dataReductionScreen.manualBaselineButton.addActionListener(this);
   _w.projection.addWatcher(this);
   _w.gridFunction.addWatcher(this);
   _w.smoothingRad.addWatcher(this);
   _w.pixelSizeX.addWatcher(this);
   _w.pixelSizeY.addWatcher(this);
   _w.offsetX.addWatcher(this);
   _w.offsetY.addWatcher(this);
}

/**
 * This method initializes the widgets in the presentation to reflect the
 * current values of the items attributes.
 * Don't do anything in init at the moment.  The initialisation requires a  reference
 * to the instrument component. This isn't known until after setup is invoked, which
 * is after init.  Therefore do the initialisation as part of setup (see initInstWidgets).
 */
protected void
_init()
{


}

/**
 * This method initializes the instrument speific widgets in the presentation 
 * to reflect the current values of the items attributes and set the watchers.
 */
protected void
_initInstWidgets()
{

   CommandButtonWidgetExt cbw = null;
   CheckBoxWidgetExt ckbw = null;

   _inst = ((SpInstObsComp) SpTreeMan.findInstrument(_spDRRecipe));

   if(_inst == null) {
     // MFO: "empty" is hard-wired in DRRecipeGUI (as constraint strings of the
     // respective panels managed my the CardLayout of DRRecipeGUI).
     // Might not be elegant but is done in a similar way with instrument specific panels
     // (see below).  
     ((CardLayout)(_w.getLayout())).show(_w, "empty");
    
     DialogUtil.error(_w, "Can't identify instrument: probably none in scope?");
     return;
   }


   if(_inst instanceof SpInstHeterodyne) {
     _instStr = INST_STR_HETERODYNE;
   }
   else {
     _instStr = INST_STR_SCUBA;
   }  

//   System.out.println ("Inst is "+_instStr);

   // MFO: inst.type().getReadable() is hard-wired in DRRecipeGUI (as constraint strings of the
   // respective panels managed my the CardLayout of DRRecipeGUI).
   // Might not be elegant but has always been hard-wired in a similar way.
   ((CardLayout)(_w.getLayout())).show(_w, _instStr.toLowerCase());
     

   if(_instStr.equalsIgnoreCase(INST_STR_HETERODYNE)) {
     return;
   }

  // The recipes
  TextBoxWidgetExt rtbw; 

  rtbw = (TextBoxWidgetExt) getWidget(_instStr, "objectRecipe");
  // Disable it so it the user cannot use it.
  // _disableRecipeEntry(true);
  rtbw.setEditable(false);
  cbw = (CommandButtonWidgetExt) getWidget(_instStr, "objectSet");
  cbw.addWatcher( new CommandButtonWidgetWatcher() {
    public void commandButtonAction(CommandButtonWidgetExt cbw) {
      // Set the selected table value
      _spDRRecipe.setObjectRecipeName(_currentRecipeSelected);
      _spDRRecipe.setTitleAttr(_currentRecipeSelected);
  
      TextBoxWidgetExt tbwe = (TextBoxWidgetExt) getWidget(_instStr, "objectRecipe");
      tbwe.setText(_currentRecipeSelected);
      _disableRecipeEntry(true);
      }
  });

  // The table of possible recipes
  TableWidgetExt twe;
  twe = (TableWidgetExt) getWidget(_instStr, "recipeTable");
  twe.setColumnHeaders(new String[]{"Recipe Name", "Description"});
  twe.addWatcher(this);

  // Button to allow user to enter own names
  TextBoxWidgetExt tbwe = (TextBoxWidgetExt) getWidget(_instStr, "userRecipe");
  tbwe.setText("ENTER_YOUR_OWN_RECIPE_HERE");
  _disableRecipeEntry(true);
  tbwe.addWatcher(this);

  cbw = (CommandButtonWidgetExt) getWidget(_instStr, "userSpec");
  cbw.addWatcher (new CommandButtonWidgetWatcher() {
      public void commandButtonAction (CommandButtonWidgetExt cbw) {
	_disableRecipeEntry(false);
      }
  });

  // button to reset the recipe to default
  cbw = (CommandButtonWidgetExt) getWidget(_instStr, "defaultName");
  cbw.addWatcher( new CommandButtonWidgetWatcher() {
    public void commandButtonAction(CommandButtonWidgetExt cbw) {
      _spDRRecipe.useDefaults(_instStr);
      _currentRecipeSelected = "QUICK_LOOK";
      _updateWidgets();
    }
  });
}

private void
_disableRecipeEntry (boolean tf) {

   TextBoxWidgetExt tbwe = (TextBoxWidgetExt) getWidget(_instStr, "userRecipe");
   tbwe.setEditable(!tf);
}

/**
 * Initialize the Recipe table widget according to the selected
 * recipe category.
 */
private void
_showRecipeType(LookUpTable recipes)
{
   Vector[] rowsV = new Vector[recipes.getNumRows()];
   rowsV = recipes.getAsVectorArray();
   TableWidgetExt tw = (TableWidgetExt) getWidget(_instStr, "recipeTable");
   tw.setRows(rowsV);
}

/**
 * Get the index of the recipe in the given array, or -1 if the recipe
 * isn't in the array.
 */
   private int _getRecipeIndex( String recipe, LookUpTable rarray ) {
      int ri = -1;
      try {
         ri = rarray.indexInColumn( recipe, 0 );
      } catch (Exception ex) {
      }
      return ri;
   }

/**
 * Update the recipe choice related widgets.
 */
private void
_updateRecipeWidgets()
{
  String recipe = null;
  String instStr;

   SpInstObsComp inst = ((SpInstObsComp) SpTreeMan.findInstrument(_spDRRecipe));

   if(inst == null) {
     // MFO: "empty" is hard-wired in DRRecipeGUI (as constraint strings of the
     // respective panels managed my the CardLayout of DRRecipeGUI).
     // Might not be elegant but is done in a similar way with instrument specific panels
     // (see below).  
     ((CardLayout)(_w.getLayout())).show(_w, "empty");
    
     System.out.println ("No instrument found in scope");
     return;
   }


   if(inst instanceof SpInstHeterodyne) {
     instStr = INST_STR_HETERODYNE;
   }
   else {
     instStr = INST_STR_SCUBA;
   }  

//   System.out.println ("Inst is "+instStr);

   // MFO: inst.type().getReadable() is hard-wired in DRRecipeGUI (as constraint strings of the
   // respective panels managed my the CardLayout of DRRecipeGUI).
   // Might not be elegant but has always been hard-wired in a similar way.
   ((CardLayout)(_w.getLayout())).show(_w, instStr.toLowerCase());
     

   if(instStr.equalsIgnoreCase(INST_STR_HETERODYNE)) {
     return;
   }

  // First fill in the text box.
  TextBoxWidgetExt tbwe;
  CheckBoxWidgetExt cbwe;

  tbwe = (TextBoxWidgetExt) getWidget(_instStr, "objectRecipe");
  try {
    recipe = _spDRRecipe.getObjectRecipeName();
    tbwe.setValue(recipe);
  }catch (NullPointerException ex) { }
  
  // See which type of recipe the selected recipe is, if any.
  // Get the instrument and display the relevant options.
  // Then look for the recipe in these options. If its there highlight it.

  // What I really need to do is introduce imaging/spec capabilities into
  // instruments which I can then get.  Imager-spectrometers will be a 
  // special case

   LookUpTable  rarray = null;
   int index = -1;

   if (instStr.equalsIgnoreCase(INST_STR_SCUBA)) {

     rarray = SpDRRecipe.SCUBA;
	   
   }else if (instStr.equalsIgnoreCase(INST_STR_HETERODYNE)) {

     rarray = SpDRRecipe.HETERODYNE;

   }
   

   // Show the correct recipes, and select the option widget for the type
   _showRecipeType(rarray);

}

/**
 * Override setup to store away a reference to the SpDRRecipe item. Also initialise the widgets
 * Here.
 */
public void
setup(SpItem spItem)
{
  _spDRRecipe = (SpDRRecipe) spItem;
  _inst = ((SpInstObsComp) SpTreeMan.findInstrument(_spDRRecipe));
  _initInstWidgets();

   super.setup(spItem);
}

/**
 * Implements the _updateWidgets method from OtItemEditor in order to
 * setup the widgets to show the current values of the item.
 */
protected void
_updateWidgets()
{
   _updateRecipeWidgets();

   StringBuffer stringBuffer = new StringBuffer();
   DecimalFormat df = new DecimalFormat();
   df.setMinimumFractionDigits(3);
   df.setMaximumFractionDigits(3);
   for(int i = 0; i < _spDRRecipe.getNumBaselineRegions(); i++) {
      stringBuffer.append(df.format(_spDRRecipe.getBaselineRegionMin(i)) + ", " + df.format(_spDRRecipe.getBaselineRegionMax(i)) + "\n");
   }

   _dataReductionScreen.baselineRegions.setText(stringBuffer.toString());

   stringBuffer = new StringBuffer();
   for(int i = 0; i < _spDRRecipe.getNumLineRegions(); i++) {
      stringBuffer.append(_spDRRecipe.getLineRegionMin(i) + ", " + _spDRRecipe.getLineRegionMax(i) + "\n");
   }

//   _dataReductionScreen.lineRegions.setText(stringBuffer.toString());

   _dataReductionScreen.truncation.setValue("" + _spDRRecipe.getTruncationChannels());
   _dataReductionScreen.baselineRegionUnits.setValue(_spDRRecipe.getBaselineRegionUnits());
   _dataReductionScreen.baselineFittingPolynomial.setValue(_spDRRecipe.getPolynomialOrder());
   _dataReductionScreen.channelBinning.setValue("" + _spDRRecipe.getChannelBinning());
   _dataReductionScreen.regriddingMethod.setValue(_spDRRecipe.getRegriddingMethod());
   _dataReductionScreen.windowType.setValue(_spDRRecipe.getWindowType());
   _dataReductionScreen.automaticBaseline.setValue(""+_spDRRecipe.getBaselineFraction() );
//    _w.projection.setValue(_spDRRecipe.getProjection(0));
   _w.gridFunction.setValue(_spDRRecipe.getGridFunction(0));
   _w.smoothingRad.setValue(_spDRRecipe.getSmootingRad(0));
   _w.pixelSizeX.setValue(_spDRRecipe.getPixelSizeX(0));
   _w.pixelSizeY.setValue(_spDRRecipe.getPixelSizeY(0));
   _w.offsetX.setValue(_spDRRecipe.getOffsetX(0));
   _w.offsetY.setValue(_spDRRecipe.getOffsetY(0));

   // See which baseline fitting type we are using
   String baselineMethod = _spDRRecipe.getBaselineFitting();
   JRadioButton but = (JRadioButton) _baselineToButtonTable.get(baselineMethod);
   but.doClick();
   

}

/**
 * This public methods allows the SpectralRegionDialog to cause the
 * widgets of this editor to be updated.
*/
public void
refresh()
{
   _updateWidgets();
}


/**
 * A key has been pressed in the text box widget.
 * @see KeyPressWatcher
 */
   public void keyPressed( KeyEvent evt ) {
      if(evt.getSource() == _dataReductionScreen.baselineRegions) {
         _spDRRecipe.setBaselineRegions(_dataReductionScreen.baselineRegions.getText());
      }

//      if(evt.getSource() == _dataReductionScreen.lineRegions) {
//         _spDRRecipe.setLineRegions(_dataReductionScreen.lineRegions.getText());
//      }
   }

/**
 * Watch changes to the text box.
 * @see TextBoxWidgetWatcher
 */
public void
textBoxKeyPress(TextBoxWidgetExt tbwe)
{
  // This is the watcher entered hen the user types a character in a text box
  // widget, Currently the user specified recipe is the only such widget.
  _currentRecipeSelected = tbwe.getText().trim();


  if(tbwe == _dataReductionScreen.truncation) {
    _spDRRecipe.setTruncationChannels(_dataReductionScreen.truncation.getValue());
    return;
  }

  if ( tbwe == _dataReductionScreen.automaticBaseline ) {
      _spDRRecipe.setBaselineFraction(_dataReductionScreen.automaticBaseline.getValue());
      return;
  }


  if(tbwe == _w.smoothingRad) {
    _spDRRecipe.setSmoothingRad(_w.smoothingRad.getValue(), 0);
    return;
  }

  if(tbwe == _w.pixelSizeX) {
    _spDRRecipe.setPixelSizeX(_w.pixelSizeX.getValue(), 0);
    return;
  }

  if(tbwe == _w.pixelSizeY) {
    _spDRRecipe.setPixelSizeY(_w.pixelSizeY.getValue(), 0);
    return;
  }

  if(tbwe == _w.offsetX) {
    _spDRRecipe.setOffsetX(_w.offsetX.getValue(), 0);
    return;
  }

  if(tbwe == _w.offsetY) {
    _spDRRecipe.setOffsetY(_w.offsetY.getValue(), 0);
    return;
  }
}
 
/**
 * Text box action, ignore.
 * @see TextBoxWidgetWatcher
 */
   public void textBoxAction( TextBoxWidgetExt tbwe ) {
//     System.out.println ( "In tba" );
   }


public void
tableRowSelected(TableWidgetExt twe, int rowIndex)
{
   _currentRecipeSelected = (String) twe.getCell(0, rowIndex);
   String _defaultRecipe = "QUICK_LOOK";

// Allow for blank lines and headings.  The latter is defined to contain
// at least two lowercase letters or any space (testing for a colon
// might also be useful).  Merely substitute the default recipe.   
   if ( _currentRecipeSelected.length() == 0 ) {
      _currentRecipeSelected = _defaultRecipe;

   } else {
      int count = 0;
      for ( int i = 0; i < _currentRecipeSelected.length(); i++ ) {
          if ( Character.isWhitespace( _currentRecipeSelected.charAt( i ) ) ) {
             _currentRecipeSelected = _defaultRecipe;
             break;

          } else if ( Character.isLowerCase( _currentRecipeSelected.charAt( i ) ) ) {
             count++;
             if ( count > 1 ) {
                _currentRecipeSelected = _defaultRecipe;
                break;
             }
          }
       }
    }

   // Don't set the value if the new selection is the same as the old
   // (otherwise, we'd fool the OT into thinking a change had been made)
//    String curValue = _spDRRecipe.getRecipeName();
//    if ((curValue != null) && (curValue.equals(recipe))) {
//        //     System.out.println ("New same as old");
//       return;
//    }

}

/**
 * Must watch table widget actions as part of the TableWidgetWatcher
 * interface, but don't care about them.
 */
   public void tableAction( TableWidgetExt twe, int colIndex, int rowIndex ) {}


  public void dropDownListBoxSelect(DropDownListBoxWidgetExt dd, int i, String val) {}

  public void dropDownListBoxAction(DropDownListBoxWidgetExt dd, int i, String val) {
    if(dd == _dataReductionScreen.channelBinning) {
      _spDRRecipe.setChannelBinning(val);
      return;
    }

    if(dd == _dataReductionScreen.regriddingMethod) {
      _spDRRecipe.setRegriddingMethod(val);
      return;
    }

    if(dd == _dataReductionScreen.windowType) {
      _spDRRecipe.setWindowType(val);
      return;
    }

    if(dd == _w.projection) {
//       _spDRRecipe.setProjection(val, 0);
      return;
    }

    if(dd == _w.gridFunction) {
      _spDRRecipe.setGridFunction(val, 0);
      return;
    }

    if(dd == _dataReductionScreen.baselineRegionUnits) {
      _spDRRecipe.setBaselineRegionUnits(val);
      return;
    }

    if(dd == _dataReductionScreen.baselineFittingPolynomial) {
      _spDRRecipe.setPolynomialOrder(val);
      return;
    }
  }

  public void actionPerformed(ActionEvent e) {
    Object source = e.getSource();

    if ( _inst != null ) {
	if ( _inst instanceof SpInstHeterodyne ) {
	    if ( source == _dataReductionScreen.showSpectralRegionEditor ) {
                if ( SpDRRecipe.BASELINE_REGION_UNITS[1].equals(_spDRRecipe.getBaselineRegionUnits() ) ) {
                    // Use frequency
		    _spectralRegionFrame.show(_spDRRecipe, (SpInstHeterodyne)_inst, this);
                }
                else {
                    // Use velocity by default
                    _velocityRegionFrame.show( _spDRRecipe, (SpInstHeterodyne)_inst, this);
                }
	    }
	    if ( source == _dataReductionScreen.noBaselineButton ) {
		_spDRRecipe.setBaselineFitting(SpDRRecipe.BASELINE_SELECTION[0]);
		// Hide the baseline panel
		_dataReductionScreen.baselineFitPanel.setVisible(false);
		// Disable the input part for automated baseline fitting
		_dataReductionScreen.automaticBaseline.setEnabled(false);
	    }
	    if ( source == _dataReductionScreen.automaticBaselineButton) {
		_spDRRecipe.setBaselineFitting(SpDRRecipe.BASELINE_SELECTION[1]);
		//  Hide the baseline panel
		_dataReductionScreen.baselineFitPanel.setVisible(false);
		// Enable the input part for automated baseline fitting
		_dataReductionScreen.automaticBaseline.setEnabled(true);
	    }
	    if ( source == _dataReductionScreen.manualBaselineButton) {
		_spDRRecipe.setBaselineFitting(SpDRRecipe.BASELINE_SELECTION[2]);
		// Enable the baseline panel
		_dataReductionScreen.baselineFitPanel.setVisible(true);
		// Disable the input part for automated baseline fitting
		_dataReductionScreen.automaticBaseline.setEnabled(false);
	    }
	}
	else if ( _inst instanceof SpInstSCUBA ) {
	}
    }

  }

   /**
    * @see #getWidget(java.lang.String)
    */
   protected JComponent getWidget(String instrument, String widgetName) {
     return getWidget(instrument.toLowerCase() + "_" + widgetName);
   }

   /**
    * Helper method.
    *
    * In EdDRRecipe the widgets of the associated GUI class cannot as easily be referred to directly
    * as in the subclasses of EdCompInstBase because the widget names in the freebongo version often comprised an
    * instrument part and a paramater part (e.g. _instStr, "flatInGroup"). In freebongo something like this is possible
    * because one gets hold of a widget by calling the getWidget(String) method on the presentation. If this method is
    * called with a String that refers to a widget that does not exist (e.g. "IRCAM3.flatInGroup") then a
    * NullPointerException occurres which can be called and ignored. This was used in the freebongo version of this class
    * to handle every widget for every instrument. Whenever a widget was addressed in the context of an instrument whose GUI
    * panel does not have this widget that a NullPointerException occurrs and is ignored. To use a similar approach in
    * the swing version of the OT this helper method getWidget has been introduced in the EdDRRecipe class.
    *
    * ('_' are used instead of '.' to separate instrument name from widget name.)
    * 
    * @return widget in DRRecipeGUI or null if specified widget does not exist.
    */
   protected JComponent getWidget(String widgetName) {
     try {
       return (JComponent)(_w.getClass().getDeclaredField(widgetName).get(_w));
     }
     catch(NoSuchFieldException e) {
       if((System.getProperty("DEBUG") != null) && System.getProperty("DEBUG").equalsIgnoreCase("ON")) {
         System.out.println ("Could not find widget / component \"" + widgetName + "\".");
       }

       return null;
     }
     catch(Exception e) {
       e.printStackTrace();
       return null;
     }
   }
}
