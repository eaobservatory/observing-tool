/*                                                              */
/*                UK Astronomy Technology Centre                */
/*                 Royal Observatory, Edinburgh                 */
/*                 Joint Astronomy Centre, Hilo                 */
/*                   Copyright (c) PPARC 2003                   */
/*                                                              */
/*==============================================================*/
// $Id$

package edfreq.region;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JPopupMenu;
import javax.swing.JMenuItem;
import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JScrollPane;
import javax.swing.Box;
import javax.swing.border.TitledBorder;
import java.awt.Dialog;
import java.awt.Frame;
import java.awt.Window;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Point;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.Vector;

/**
 * Tool for selecting baseline fit regions and line regions in a spectrum.
 *
 * Terminology: "Baseline region", "Baseline fit region" and "Fit region" all mean the same
 * in the context of this package. They are different from the "Line Region".
 *
 * @author Martin Folger (M.Folger@roe.ac.uk)
 */
public class SpectralRegionEditor extends JPanel implements ActionListener {

  private Color FIT_REGION_BAR_COLOR  = Color.blue;
  private Color LINE_REGION_BAR_COLOR = Color.red;

  private int [] ZOOM_OPTIONS = { 1, 2, 3, 4 };

//  private JButton _addLineRegionButton    = new JButton("Add");
//  private JButton _removeLineRegionButton = new JButton("Remove");
  private JButton _addFitRegionButton     = new JButton("Add");
  private JButton _removeFitRegionButton  = new JButton("Remove");
//  private JButton _addCombinedRegionsButton  = new JButton("Add");
//  private JButton _removeCombinedRegionsButton  = new JButton("Remove");

  private JComboBox _zoomOptions = new JComboBox();
  private JButton   _viewLSB  = new JButton("View LSB");
  private JButton   _viewUSB  = new JButton("View USB");

  /**
   * Contains the Line Region DoubleSideBandRangeBar bars.
   *
   * Every DoubleSideBandRangeBar that is in _lineRegionBars is also in
   * {@link #_allRegionBars}.
   */
  private Vector _lineRegionBars = new Vector();

  /**
   * Contains the Baseline Fit Region DoubleSideBandRangeBar bars.
   *
   * Every DoubleSideBandRangeBar that is in _fitRegionBars is also in
   * {@link #_allRegionBars}.
   */
  private Vector _fitRegionBars  = new Vector();

  /**
   * Contains the combined Baseline Fit Region, Line Region, Baseline Fit Region DoubleSideBandRangeBar bars.
   *
   * The areas ouside line line region are interpreted as baseline fit regions.
   *
   * Every DoubleSideBandRangeBar that is in _combinedRegionBars is also in
   * {@link #_allRegionBars}.
   */
  private Vector _combinedRegionBars  = new Vector();
  /**
   * Contains all DoubleSideBandRangeBar bars.
   *
   * Every DoubleSideBandRangeBar that is in _allRegionBars is also in one of the following:
   * {@link #_fitRegionBars}, {@link #_lineRegionBars}, {@link #_combinedRegionBars}
   *
   * _allRegionBars is mainly used to maintain the GUI.
   */
  private Vector _allRegionBars  = new Vector();

  private static final Point _lsbViewPosition = new Point(0, 0);
  private              Point _usbViewPosition = new Point(0, 0);

  private Window _parent = null;
  private JPanel _rangeBarPanel  = new JPanel();
  private JPanel _rangeBarLineDisplayPanel = new JPanel(new BorderLayout());
  private JScrollPane _scrollPane;
  private JPanel _allButtonPanels = new JPanel(new GridLayout(3, 1));

  private LineDisplay _lineDisplay = new LineDisplay();

  private int _feBandWidthPixels;
  private double _feBandWidth;
  private double _feIF;
  private Vector _beBandWidth = new Vector(4);
  private Vector _beIF = new Vector(4);

  private double _mainLine;

  /**
   * Indicates the sideband in which the main (first, uppermost) transition line is located.
   *
   * The meaning of _sideBand differs from that of
   * {@link edfreq.region.RangeBar._associatedSideBand}.
   */
  private int _sideBand;

  /** Double sideband versus single sideband. */
  private boolean _dsb;

  /**
   * Used to create alternating baseline fit regions below and above the
   * line region when the user adds baseline regions repeatedly.
   *
   * "Inside" means the baseline fit regions is between the LO1 and the
   * center of the sideband.
   */
  private boolean _fitRegionInside = false;

  public SpectralRegionEditor(Window parent) {

    setLayout(new BorderLayout());

    JPanel buttonPanel = new JPanel();
    JLabel label = new JLabel("Baseline Fit Region: ");
    label.setForeground(FIT_REGION_BAR_COLOR);
    label.setPreferredSize(new Dimension(160, label.getPreferredSize().height));
    buttonPanel.add(label);
    buttonPanel.add(_addFitRegionButton);
//    buttonPanel.add(_removeFitRegionButton);
    _allButtonPanels.add(buttonPanel);
/*
    buttonPanel = new JPanel();
    label = new JLabel("Line Region: ");
    label.setForeground(LINE_REGION_BAR_COLOR);
    label.setPreferredSize(new Dimension(160, label.getPreferredSize().height));
    buttonPanel.add(label);
    buttonPanel.add(_addLineRegionButton);
    buttonPanel.add(_removeLineRegionButton);
    _allButtonPanels.add(buttonPanel);

    buttonPanel = new JPanel();
    JPanel labelPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
    label = new JLabel("Baseline", JLabel.LEFT);
    label.setForeground(FIT_REGION_BAR_COLOR);
    labelPanel.add(label);
    label = new JLabel("Line");
    label.setForeground(LINE_REGION_BAR_COLOR);
    labelPanel.add(label);
    label = new JLabel("Baseline: ");
    label.setForeground(FIT_REGION_BAR_COLOR);
    labelPanel.add(label);
    labelPanel.setPreferredSize(new Dimension(160, labelPanel.getPreferredSize().height));
    buttonPanel.add(labelPanel);
    buttonPanel.add(_addCombinedRegionsButton);
    buttonPanel.add(_removeCombinedRegionsButton);
    _allButtonPanels.add(buttonPanel);

*/
    _rangeBarLineDisplayPanel.add(_rangeBarPanel,  BorderLayout.CENTER);
    _rangeBarLineDisplayPanel.add(_lineDisplay,    BorderLayout.SOUTH);

    _scrollPane = new JScrollPane(_rangeBarLineDisplayPanel);
    _scrollPane.setPreferredSize(new Dimension(820, 100 + 20));

    // Zoom
    for(int i = 0; i < ZOOM_OPTIONS.length; i++) {
      _zoomOptions.addItem("" + ZOOM_OPTIONS[i]);
    }

    JPanel zoomPanel = new JPanel(new GridLayout(3, 1, 4, 4));
    zoomPanel.setBorder(new TitledBorder("Zoom"));

    zoomPanel.add(_zoomOptions);
    zoomPanel.add(_viewLSB);
    zoomPanel.add(_viewUSB);

    JPanel bottomPanel = new JPanel(new BorderLayout());
    bottomPanel.add(_allButtonPanels, BorderLayout.CENTER);
    bottomPanel.add(zoomPanel, BorderLayout.EAST);

    add(_scrollPane, BorderLayout.CENTER);
    add(bottomPanel, BorderLayout.SOUTH);

    _addFitRegionButton.addActionListener(this);
    _removeFitRegionButton.addActionListener(this);
    //_addLineRegionButton.addActionListener(this);
    //_removeLineRegionButton.addActionListener(this);
    //_addCombinedRegionsButton.addActionListener(this);
    //_removeCombinedRegionsButton.addActionListener(this);
    _zoomOptions.addActionListener(this);
    _viewLSB.addActionListener(this);
    _viewUSB.addActionListener(this);

    _parent = parent;

    if(_parent instanceof Dialog) {
      ((Dialog)_parent).setResizable(false);
    }

    if(_parent instanceof Frame) {
      ((Frame)_parent).setResizable(false);
    }
  }

  public void setSideBand(int sideBand) {
    _sideBand = sideBand;
  }

  /**
   * Switches between single/double mode and lower/upper sideband.
   *
   * @param modeStr Allowed values: "ssb" (single side band), "dsb" (double side band)
   *                {@link orac.jcmt.inst.SpInstHeterodyne.setMode(String)}
   * @param bandStr Allowed values: "usb" (upper side band), "lsb" (lower side band), "best" (whichever sideband is best)
   *                {@link orac.jcmt.inst.SpInstHeterodyne.setBand(String)}
   */
  public void setModeAndBand(String modeStr, String bandStr) {
    if(modeStr.equalsIgnoreCase("ssb")) {
      _dsb = false;
    }
    else {
      _dsb = true;
    }

    if(bandStr.equalsIgnoreCase("lsb")) {
      _sideBand = DoubleSideBandRangeBar.SIDE_BAND_LSB;
      _scrollPane.getViewport().setViewPosition(_lsbViewPosition);
    }
    else {
      _sideBand = DoubleSideBandRangeBar.SIDE_BAND_USB;
      _scrollPane.getViewport().setViewPosition(_usbViewPosition);
    }
  }

  /**
   * Adds a DoubleSideBandRangeBar representing a baseline fit region.
   *
   * @param xMin        lower bound of baseline fit region in GHz.
   * @param xMax        upper bound of baseline fit region in GHz.
   * @param lo1         LO1 in GHz.
   * @param resetLayout The changes will only become visible if resetLayout == true.
   *                    However, if several calls to {@link #addLineRegion(int,int,boolean)},
                        and addBaselineFitRegion(int,int,boolean) or {@link removeAllRegions(boolean)}
   *                    are made in a row then resetLayout should only be set to true in the last of
   *                    these calls.
   */
  public void addBaselineFitRegion(double [] xMin, double [] xMax, double lo1, boolean resetLayout) {
    final DoubleSideBandRangeBar fitRegionBar = new DoubleSideBandRangeBar(Integer.parseInt((String)_zoomOptions.getSelectedItem()));

    int [] beUsbPixels = getBeUpperSideBandPixels();

    //fitRegionBar.resetRangeBars(_feBandWidthPixels, _lineDisplay.getDisplayWidth(),
    //                            beUsbPixels[0], beUsbPixels[1], _sideBand, _dsb);
    fitRegionBar.resetRangeBars(_feBandWidthPixels, _lineDisplay.getDisplayWidth(),
                                beUsbPixels, _sideBand, _dsb);

    fitRegionBar.setForeground(FIT_REGION_BAR_COLOR);

    fitRegionBar.addObserver(_lineDisplay);

    double pixelsPerValue = _lineDisplay.getPixelsPerValue();

    /*
    int barWidth = (int)Math.round((xMax -  xMin) * pixelsPerValue);
    int barX;

    if(xMin > lo1) {
      barX = ((int)Math.round((xMin - lo1) * pixelsPerValue)) - ((_lineDisplay.getDisplayWidth() / 2) - _feBandWidthPixels);
    }
    else {
      barX = (_lineDisplay.getDisplayWidth() / 2) - ((int)Math.round((lo1 - xMin) * pixelsPerValue));
    }

    fitRegionBar.setBars(barX, barWidth);
    */

    //for (int i=0; i<xMax.length; i++) {
    for (int i=0; i<fitRegionBar.getNumRanges(); i++) {
	int barWidth = (int)Math.round((xMax[i] -  xMin[i]) * pixelsPerValue);
	int barX;
	if(xMin[i] > lo1) {
	    barX = ((int)Math.round((xMin[i] - lo1) * pixelsPerValue)) - ((_lineDisplay.getDisplayWidth() / 2) - _feBandWidthPixels);
	}
	else {
	    barX = (_lineDisplay.getDisplayWidth() / 2) - ((int)Math.round((lo1 - xMin[i]) * pixelsPerValue));
	}
	fitRegionBar.setBars(barX, barWidth, i);
    }


    // Add a popup menu to allow removal
    final JPopupMenu menu = new JPopupMenu();
    JMenuItem  remove = new JMenuItem("Remove");
    remove.addActionListener( new ActionListener() {
	    public void actionPerformed(ActionEvent e) {
	        _fitRegionBars.remove(fitRegionBar);
		_allRegionBars.remove(fitRegionBar);
		resetLayout();
	    }
	});
    menu.add(remove);
    
    fitRegionBar.addMouseListener( new MouseAdapter() {
	    public void mousePressed(MouseEvent e) {
	        if ( e.getButton() == MouseEvent.BUTTON3 ) {
		    menu.show( e.getComponent(), e.getX(), e.getY() );
		}
	    }
	});

    _fitRegionBars.add(fitRegionBar);
    _allRegionBars.add(fitRegionBar);

    if(resetLayout) {
      resetLayout();
    }
  }

  /**
   * Creates default baseline fit region above or below the center of the
   * frontend sideband.
   *
   * @see addBaselineFitRegion(double,double,double,boolean)
   */
  public void addBaselineFitRegion(boolean resetLayout) {

    // Assume xMin and xMax to be in the upper sideband. In this context it does not matter
    // whether the mode is single or double sideband and whether we are in the upper or lower
    // sideband. Internally the DoubleSideBandRangeBar has RangeBars for both sidebands and
    // the method addLineRegion() accepts xMin and xMax values in both sidebands and
    // sets both RangeBars appropriatedly.
    double [] xMinHz = new double[_beIF.size()];
    double [] xMaxHz = new double[_beIF.size()];

    for ( int i=0; i<_beIF.size(); i++) {
        double beIF = ((Double)_beIF.get(i)).doubleValue();
	double beBandWidth = ((Double)_beBandWidth.get(i)).doubleValue();
        if(_fitRegionInside) {
          xMinHz[i] = (_lineDisplay.getLO1() + (beIF - (beBandWidth / 2.0)))/1.0E9;
          xMaxHz[i] = (_lineDisplay.getLO1() + (beIF - (beBandWidth / 6.0)))/1.0E9;
        }
        else {
          xMinHz[i] = (_lineDisplay.getLO1() + (beIF + (beBandWidth / 6.0)))/1.0E9;
          xMaxHz[i] = (_lineDisplay.getLO1() + (beIF + (beBandWidth / 2.0)))/1.0E9;
        }

        _fitRegionInside = !_fitRegionInside;

    }
    _fitRegionInside = ( (double)_beIF.size()%2.0 == 0 );
    //addBaselineFitRegion(xMinHz[0], xMaxHz[0], _lineDisplay.getLO1() / 1.0E9, resetLayout);
    addBaselineFitRegion(xMinHz, xMaxHz, _lineDisplay.getLO1() / 1.0E9, resetLayout);
  }

  /**
   * Adds a DoubleSideBandRangeBar representing a line region.
   *
   * @param xMin        lower bound of line region in GHz.
   * @param xMax        upper bound of line region in GHz.
   * @param lo1         LO1 in GHz.
   * @param resetLayout The changes will only become visible if resetLayout == true.
   *                    However, if several calls to addLineRegion(int,int,boolean),
   *                    {@link #addBaselineFitRegion(int,int,boolean)} or {@link removeAllRegions(boolean)}
   *                    are made in a row then resetLayout should only be set to true in the last of
   *                    these calls.
   * @param combined    If true then the regions outside xMin and xMax are treated as baseline fit regions.
   */
  public void addLineRegion(double xMin, double xMax, double lo1, boolean resetLayout, boolean combined) {
    DoubleSideBandRangeBar lineRegionBar = new DoubleSideBandRangeBar(Integer.parseInt((String)_zoomOptions.getSelectedItem()));

    int [] beUsbPixels = getBeUpperSideBandPixels();

    lineRegionBar.resetRangeBars(_feBandWidthPixels, _lineDisplay.getDisplayWidth(),
                                 beUsbPixels, _sideBand, _dsb);

    lineRegionBar.setForeground(LINE_REGION_BAR_COLOR);

    if(combined) {
      lineRegionBar.setRangeColor(FIT_REGION_BAR_COLOR);
    }

    lineRegionBar.addObserver(_lineDisplay);

    double pixelsPerValue = _lineDisplay.getPixelsPerValue();

    int barWidth = (int)Math.round((xMax -  xMin) * pixelsPerValue);
    int barX;

    barX = ((int)Math.round((xMin - lo1) * pixelsPerValue)) - ((_lineDisplay.getDisplayWidth() / 2) - _feBandWidthPixels);

    if(xMin > lo1) {
      barX = ((int)Math.round((xMin - lo1) * pixelsPerValue)) - ((_lineDisplay.getDisplayWidth() / 2) - _feBandWidthPixels);
    }
    else {
      barX = (_lineDisplay.getDisplayWidth() / 2) - ((int)Math.round((lo1 - xMin) * pixelsPerValue));
    }

    lineRegionBar.setBars(barX, barWidth);

    if(combined) {
      _combinedRegionBars.add(lineRegionBar);
    }
    else {
      _lineRegionBars.add(lineRegionBar);
    }

    _allRegionBars.add(lineRegionBar);

    if(resetLayout) {
      resetLayout();
    }
  }

  /** @see addLineRegion(double,double,double,boolean,boolean) */
  public void addLineRegion(double xMin, double xMax, double lo1, boolean resetLayout) {
    addLineRegion(xMin, xMax, lo1, resetLayout, false);
  }

  /**
   * Creates default line region.
   *
   * @see addLineRegion(double,double,double,boolean,boolean)
   */
   /*
  public void addLineRegion(boolean resetLayout, boolean combined) {

    // Assume xMin and xMax to be in the upper sideband. In this context it does not matter
    // whether the mode is single or double sideband and whether we are in the upper or lower
    // sideband. Internally the DoubleSideBandRangeBar has RangeBars for both sidebands and
    // the method addLineRegion() accepts xMin and xMax values in both sidebands and
    // sets both RangeBars appropriatedly.
    double xMinHz = _lineDisplay.getLO1() + (_beIF - (_beBandWidth / 6.0));
    double xMaxHz = _lineDisplay.getLO1() + (_beIF + (_beBandWidth / 6.0));

    addLineRegion(xMinHz / 1.0E9, xMaxHz / 1.0E9, _lineDisplay.getLO1() / 1.0E9, resetLayout, combined);
  }
  */

  /** @see addLineRegion(boolean,boolean) */
  /*
  public void addLineRegion(boolean resetLayout) {
    addLineRegion(resetLayout, false);
  }
  */

  /**
   * Checks whether any of the line regions forms a combined region together with
   * two of the baseline fit regions.
   *
   * If a combined region is found, it is added to the _combinedRegionBars Vector.
   * The corresponding line region and baseline fit regions are removed from
   * _lineRegionBars and _fitRegionBars respectively.
   */
  public void createCombinedRegions(boolean resetLayout) {

    DoubleSideBandRangeBar upperFitRegionBar;
    DoubleSideBandRangeBar lowerFitRegionBar;
    DoubleSideBandRangeBar lineRegionBar;
    DoubleSideBandRangeBar fitRegionBar;

    for(int i = 0; i < _lineRegionBars.size(); i++) {
      lineRegionBar = (DoubleSideBandRangeBar)_lineRegionBars.get(i);

      upperFitRegionBar = null;
      lowerFitRegionBar = null;

      for(int j = 0; j < _fitRegionBars.size(); j++) {
        fitRegionBar = (DoubleSideBandRangeBar)_fitRegionBars.get(j);

        if(lineRegionBar.getUpperBarX() + lineRegionBar.getBarWidth() == fitRegionBar.getUpperBarX()) {
          upperFitRegionBar = fitRegionBar;
	}

        if(lineRegionBar.getUpperBarX() == fitRegionBar.getUpperBarX() + fitRegionBar.getBarWidth()) {
          lowerFitRegionBar = fitRegionBar;
	}
      }

      if( (upperFitRegionBar != null) && (lowerFitRegionBar != null) &&
         ((upperFitRegionBar.getBarWidth() +
           lowerFitRegionBar.getBarWidth() +
           lineRegionBar.getBarWidth()) == _feBandWidthPixels)) {

        _fitRegionBars.remove(upperFitRegionBar);
        _fitRegionBars.remove(lowerFitRegionBar);
	_lineRegionBars.remove(lineRegionBar);

        _allRegionBars.remove(upperFitRegionBar);
        _allRegionBars.remove(lowerFitRegionBar);

        lineRegionBar.setRangeColor(FIT_REGION_BAR_COLOR);
	_combinedRegionBars.add(lineRegionBar);
      }
    }

    if(resetLayout) {
      resetLayout();
    }
  }

  /**
   * Removes all baseline fit and line regions.
   *
   * @param resetLayout The changes will only become visible if resetLayout == true.
   *                    However, if several calls to {@link addLineRegion(int,int,boolean)},
   *                    {@link #addBaselineFitRegion(int,int,boolean)} or removeAllRegions(boolean)
   *                    are made in a row then resetLayout should only be set to true in the last of
   *                    these calls.
   */
  public void removeAllRegions(boolean resetLayout) {
    _fitRegionBars.clear();
    _lineRegionBars.clear();
    _combinedRegionBars.clear();
    _allRegionBars.clear();

    if(resetLayout) {
      resetLayout();
    }
  }


  public void updateLineDisplay (
    double lRangeLimit, double uRangeLimit, 
    double feIF, double feBandWidth,
    double redshift)
  {

    _feBandWidth = feBandWidth;
    _feIF        = feIF;

    _lineDisplay.updateDisplay(lRangeLimit, uRangeLimit, feIF, feBandWidth, redshift);

    // Make sure _lineDisplay.getPixelsPerValue() is called after _lineDisplay.updateDisplay();
    _feBandWidthPixels = (int)((feBandWidth / 1.0E9) * _lineDisplay.getPixelsPerValue());

    _parent.pack();
  }

  public void updateBackendValues(double beIF, double beBandWidth) {
    updateBackendValues( beIF, beBandWidth, 0);
  }

  public void updateBackendValues(double beIF, double beBandWidth, int region) {
    // Reset everything if the region is 0
    if ( region == 0 ) {
	_beIF.clear();
	_beBandWidth.clear();
    }

    try {
        _beIF.setElementAt(new Double(beIF), region);
        _beBandWidth.setElementAt(new Double( beBandWidth ), region );
    }
    catch (ArrayIndexOutOfBoundsException ex) {
	// Just add the elements
	_beIF.add(region, new Double(beIF));
	_beBandWidth.add(region, new Double( beBandWidth ) );
    }
  }

  /**
   * Calculates the offsets of the backend sidband range relative to the frontend sidband in pixels.
   *
   * {@link _feIF}, {@link _beIF} and {@link _beBandWidth} must have been set correctly for
   * this method to work.
   *
   * @see #updateLineDisplay(double,double,double,double,double)
   * @see #updateBackendValues(double,double)
   *
   * @param feIF        Frontend IF in Hz.
   * @param beIF        Backend IF (sampler centre frequency) in Hz.
   * @param beBandWidth Backend bandwidth (sampler bandwidth) in Hz.
   */
  public int [] getBeUpperSideBandPixels() {

    int [] result = new int [_beIF.size()*2];
    for ( int i=0; i<_beIF.size(); i++ ) {
        double beIF = ((Double)_beIF.get(i)).doubleValue();
        double beBandWidth = ((Double)_beBandWidth.get(i)).doubleValue();
        // Min, max frequencies in range of upper backend sideband.
        double beMinUSB = _lineDisplay.getLO1() + (beIF - (0.5 * beBandWidth));
        double beMaxUSB = _lineDisplay.getLO1() + (beIF + (0.5 * beBandWidth));

        // Min frequency in range of upper frontend sideband.
        double feMinUSB = _lineDisplay.getLO1() + (_feIF - (0.5 * _feBandWidth));

        double pixelsPerValue = _lineDisplay.getPixelsPerValue();

	result[i*2]     = (int)(((beMinUSB - feMinUSB) / 1.0E9) * pixelsPerValue);
	result[i*2 + 1] = (int)(((beMaxUSB - feMinUSB) / 1.0E9) * pixelsPerValue);
	
    }

    return result;
  }

  public void setMainLine(double mainLine) {
    _mainLine = mainLine;
  
    resetMainLine();
  }

  /**
   * Resets pixel value of main line in EmissionLines display.
   */
  public void resetMainLine() {
    _lineDisplay.setMainLine(_mainLine);
  }

  public void resetLayout() {
    _rangeBarPanel.removeAll();

    _rangeBarPanel.removeAll();
    _rangeBarPanel.setLayout(new GridLayout(_allRegionBars.size(), 1));

    for(int i = 0; i < _allRegionBars.size(); i++) {
      _rangeBarPanel.add((DoubleSideBandRangeBar)_allRegionBars.get(i));
      ((DoubleSideBandRangeBar)_allRegionBars.get(i)).setId(i);
    }

    _scrollPane.setPreferredSize(new Dimension(820, _rangeBarLineDisplayPanel.getPreferredSize().height + 20));

    _parent.pack();
  }

  public double [][] getLineRegions() {
    double [][] result = new double[_combinedRegionBars.size() + _lineRegionBars.size()][];

    int barX;
    int barWidth;

    double pixelsPerValue = _lineDisplay.getPixelsPerValue();

    int k = 0;
    for(int i = 0; i < _combinedRegionBars.size(); i++) {
      barX     = ((DoubleSideBandRangeBar)_combinedRegionBars.get(i)).getUpperBarX();
      barWidth = ((DoubleSideBandRangeBar)_combinedRegionBars.get(i)).getBarWidth();

      result[k++] = getRegion(barX, barWidth, pixelsPerValue, _lineDisplay.getLO1());
    }

    for(int i = 0; i < _lineRegionBars.size(); i++) {
      barX     = ((DoubleSideBandRangeBar)_lineRegionBars.get(i)).getUpperBarX();
      barWidth = ((DoubleSideBandRangeBar)_lineRegionBars.get(i)).getBarWidth();

      result[k++] = getRegion(barX, barWidth, pixelsPerValue, _lineDisplay.getLO1());
    }

    return result;
  }

  public double [][] getBaselineFitRegions() {
    //double [][] result = new double[(2 * _combinedRegionBars.size()) + _fitRegionBars.size()][];
      int numRegions = 0;
      for ( int i=0; i<_fitRegionBars.size(); i++ ) {
	  numRegions += ((DoubleSideBandRangeBar)_fitRegionBars.get(i)).getNumRanges();
      }
    double [][] result = new double[(2 * _combinedRegionBars.size()) + numRegions][];

    int barX;
    int barWidth;

    double pixelsPerValue = _lineDisplay.getPixelsPerValue();

    int k = 0;
    for(int i = 0; i < _combinedRegionBars.size(); i++) {
      barX     = 0;
      barWidth = ((DoubleSideBandRangeBar)_combinedRegionBars.get(i)).getUpperBarX();

      result[k++] = getRegion(barX, barWidth, pixelsPerValue, _lineDisplay.getLO1());


      barX     = ((DoubleSideBandRangeBar)_combinedRegionBars.get(i)).getUpperBarX() +
                 ((DoubleSideBandRangeBar)_combinedRegionBars.get(i)).getBarWidth();
      barWidth = _feBandWidthPixels - barX;

      result[k++] = getRegion(barX, barWidth, pixelsPerValue, _lineDisplay.getLO1());
    }

    for(int i = 0; i < _fitRegionBars.size(); i++) {
	for ( int n=0; n<((DoubleSideBandRangeBar)_fitRegionBars.get(i)).getNumBars(); n++ ) {
          barX     = ((DoubleSideBandRangeBar)_fitRegionBars.get(i)).getUpperBarX(n);
          barWidth = ((DoubleSideBandRangeBar)_fitRegionBars.get(i)).getBarWidth(n);

          result[k++] = getRegion(barX, barWidth, pixelsPerValue, _lineDisplay.getLO1());
	}
    }

    return result;
  }

  public double [] getRegion(int barX, int barWidth, double pixelsPerValue, double lo1) {
    double [] result = new double[2];

    result[0] = ((barX / pixelsPerValue) * 1.0E9) + (_feIF - (0.5 * _feBandWidth)) + lo1;
    result[1] = result[0] + ((barWidth / pixelsPerValue) * 1.0E9);

    // If the region should appear in the lower sideband than reflect the values on LO1.
    if(_sideBand == DoubleSideBandRangeBar.SIDE_BAND_LSB) {
      result[0] -= 2.0 * (result[0] - lo1);
      result[1] -= 2.0 * (result[1] - lo1);

      // Now swap the values
      double result0 = result[0];
      result[0] = result[1];
      result[1] = result0;
    }

    return result;
  }

  public void actionPerformed(ActionEvent e) {
    Object source = e.getSource();

    if(source == _addFitRegionButton) {
      addBaselineFitRegion(false);
    }

    if((source == _removeFitRegionButton) && (_fitRegionBars.size() > 0)) {
      Object obj = _fitRegionBars.lastElement();
      _fitRegionBars.remove(obj);
      _allRegionBars.remove(obj);
    }

/*
    if(source == _addLineRegionButton) {
      addLineRegion(false);
    }

    if((source == _removeLineRegionButton) && (_lineRegionBars.size() > 0)) {
      Object obj = _lineRegionBars.lastElement();
      _lineRegionBars.remove(obj);
      _allRegionBars.remove(obj);
    }

    if(source == _addCombinedRegionsButton) {
      addLineRegion(false, true);
    }

    if((source == _removeCombinedRegionsButton) && (_combinedRegionBars.size() > 0)) {
      Object obj = _combinedRegionBars.lastElement();
      _combinedRegionBars.remove(obj);
      _allRegionBars.remove(obj);
    }
*/

    if(source == _zoomOptions) {
      int zoom = Integer.parseInt((String)_zoomOptions.getSelectedItem());

      _lineDisplay.setDisplayWidth(LineDisplay.DEFAULT_DISPLAY_WIDTH * zoom);

      _feBandWidthPixels = (int)((_feBandWidth / 1.0E9) * _lineDisplay.getPixelsPerValue());

      for(int i = 0; i < _allRegionBars.size(); i++) {
        ((DoubleSideBandRangeBar)_allRegionBars.get(i)).resetRangeBars(zoom);
      }

      _usbViewPosition.move(_lineDisplay.getDisplayWidth() - 800, 0);

      if(_sideBand == DoubleSideBandRangeBar.SIDE_BAND_LSB) {
        _scrollPane.getViewport().setViewPosition(_lsbViewPosition);
      }
      else {
        _scrollPane.getViewport().setViewPosition(_usbViewPosition);
      }

      resetMainLine();
    }

    if(source == _viewLSB) {
      _scrollPane.getViewport().setViewPosition(_lsbViewPosition);
    }

    if(source == _viewUSB) {
      _scrollPane.getViewport().setViewPosition(_usbViewPosition);
    }

    resetLayout();
  }

  public int getNumRegions() {
      int numRegions = 0;
      if ( _allRegionBars != null ) {
	  numRegions = _allRegionBars.size();
      }
      return numRegions;
  }

  public static void main(String [] args) {
    if(((args.length != 3) && (args.length != 4)) || ((args.length == 1) && args[0].equals("-h"))) {
      System.out.println("Usage: SpectralRegionEditor lRangeLimit uRangeLimit redshift [dsb | lsb | usb]");
      System.out.println("  where lRangeLimit and uRangeLimit are in GHz");
      System.exit(0);
    }

    double lRangeLimit = Double.parseDouble(args[0]) * 1.0E9;
    double uRangeLimit = Double.parseDouble(args[1]) * 1.0E9;
    double redshift    = Double.parseDouble(args[2]);

    double feIF        = 4.0E9;
    double feBandWidth = 1.8E9;

    int sideBand = DoubleSideBandRangeBar.SIDE_BAND_USB;
    boolean dsb = true;

    if(args.length == 4) {
      dsb = false;

      if(args[3].equalsIgnoreCase("usb")) {
        sideBand = DoubleSideBandRangeBar.SIDE_BAND_USB;
      }

      if(args[3].equalsIgnoreCase("lsb")) {
        sideBand = DoubleSideBandRangeBar.SIDE_BAND_LSB;
      }
    }

    System.out.println("Using");
    System.out.println("lRangeLimit = " + lRangeLimit);
    System.out.println("uRangeLimit = " + uRangeLimit);
    System.out.println("feIF        = " + feIF);
    System.out.println("feBandWidth = " + feBandWidth);
    System.out.println("redshift    = " + redshift);
    System.out.println("sideBand    = " + sideBand);
    
    System.out.println("\nWorking Example: SpectralRegionEditor 200 220 0");

    JFrame frame = new JFrame("Spectral Region Editor");

    SpectralRegionEditor spectralRegionEditor = new SpectralRegionEditor(frame);
    spectralRegionEditor.setSideBand(sideBand);

    spectralRegionEditor.updateLineDisplay(lRangeLimit, uRangeLimit,
                                           feIF, feBandWidth,
                                           redshift);

    frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

    frame.getContentPane().add(spectralRegionEditor);

    frame.setLocation(100, 100);
    frame.pack();
    frame.setVisible(true);
  }

}

