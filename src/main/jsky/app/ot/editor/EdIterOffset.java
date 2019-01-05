/*
 * Copyright (c) 1997 Association of Universities for Research in Astronomy, Inc. (AURA)
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without modification,
 * are permitted provided that the following conditions are met:
 *
 * 1) Redistributions of source code must retain the above copyright notice,
 *   this list of conditions and the following disclaimer.
 * 2) Redistributions in binary form must reproduce the above copyright notice,
 *   this list of conditions and the following disclaimer in the documentation
 *   and/or other materials provided with the distribution.
 * 3) The names of AURA and its representatives may not be used to endorse or
 *   promote products derived from this software without specific prior written
 *   permission.
 *
 * THIS SOFTWARE IS PROVIDED BY AURA "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES,
 * INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND
 * FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL AURA BE
 * LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR
 * CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE
 * GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION)
 * HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT
 * LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF
 * THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */

package jsky.app.ot.editor;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.Enumeration;
import java.util.Vector;

import javax.swing.ImageIcon;

import jsky.app.ot.gui.TableWidgetExt;
import jsky.app.ot.gui.TableWidgetWatcher;
import jsky.app.ot.gui.TextBoxWidgetExt;
import jsky.app.ot.gui.TextBoxWidgetWatcher;
import jsky.util.gui.DialogUtil;

import gemini.sp.SpItem;
import gemini.sp.SpOffsetPos;
import gemini.sp.SpOffsetPosList;
import gemini.sp.SpTelescopePos;
import gemini.sp.SpTreeMan;
import gemini.sp.iter.SpIterOffset;
import gemini.sp.obsComp.SpTelescopeObsComp;

import orac.ukirt.iter.SpIterSky;

import jsky.app.ot.tpe.TpeManager;
import jsky.app.ot.OtCfg;

import gemini.util.ObservingToolUtilities;
import gemini.util.TelescopePos;
import gemini.util.TelescopePosWatcher;
import gemini.util.RADecMath;
import orac.jcmt.iter.SpIterRasterObs;

import orac.util.TelescopeUtil;

/**
 * This is the editor for Offset Iterator component.
 *
 * It allows a list of offset positions to be entered and ordered.
 *
 * @see SpOffsetPos
 *
 * @author modified for JCMT OT by Martin Folger (MFO, M.Folger@roe.ac.uk)
 */
public final class EdIterOffset extends OtItemEditor implements
        TableWidgetWatcher, TextBoxWidgetWatcher, TelescopePosWatcher,
        ActionListener, MouseListener {
    private TextBoxWidgetExt _xOffTBW;
    private TextBoxWidgetExt _yOffTBW;
    private OffsetPosTableWidget _offTW;
    private SpOffsetPos _curPos; // Position being edited
    private SpOffsetPosList _opl; // Position list being edited
    private IterOffsetGUI _w; // the GUI layout panel
    private String BUTTON_TEXT_ROTATION_FALSE = "Display Derotated Offsets";
    private String BUTTON_TEXT_ROTATION_TRUE = "Offsets Derotated";

    /**
     * The constructor initializes the title, description, and presentation
     * source.
     */
    public EdIterOffset() {
        _title = "Offset Iterator";
        _presSource = _w = new IterOffsetGUI();
        _description = "Construct offset based patterns with this iterator.";

        // JBuilder has some problems with image buttons...
        _w.topButton.setIcon(new ImageIcon(ObservingToolUtilities
                .resourceURL("jsky/app/ot/images/top.gif")));
        _w.upButton.setIcon(new ImageIcon(ObservingToolUtilities
                .resourceURL("jsky/app/ot/images/up.gif")));
        _w.bottomButton.setIcon(new ImageIcon(ObservingToolUtilities
                .resourceURL("jsky/app/ot/images/bottom.gif")));
        _w.downButton.setIcon(new ImageIcon(ObservingToolUtilities
                .resourceURL("jsky/app/ot/images/down.gif")));
        _w.pqItem.setIcon(new ImageIcon(ObservingToolUtilities
                .resourceURL("jsky/app/ot/images/pq.gif")));

        if (!OtCfg.telescopeUtil.supports(
                TelescopeUtil.FEATURE_OFFSET_GRID_PA)) {
            _w.paLabel.setVisible(false);
            _w.paTextBox.setVisible(false);
            _w.displayRotatedOffsets.setVisible(false);
            _w.setSpacingButton.setVisible(false);
        }

        _w.newButton.addActionListener(this);
        _w.removeAllButton.addActionListener(this);
        _w.removeButton.addActionListener(this);
        _w.topButton.addActionListener(this);
        _w.upButton.addActionListener(this);
        _w.downButton.addActionListener(this);
        _w.bottomButton.addActionListener(this);
        _w.createGridButton.addActionListener(this);
        _w.centreOnBaseButton.addActionListener(this);
        _w.displayRotatedOffsets.addMouseListener(this);

        _w.paTextBox.addWatcher(this);
        _w.setSpacingButton.addActionListener(this);
    }

    /**
     * This method initializes the widgets in the presentation to reflect the
     * current values of the items attributes.
     */
    protected void _init() {
        TextBoxWidgetExt tbwe = _w.titleTBW;
        tbwe.addWatcher(this);

        _xOffTBW = _w.xOffset;
        _xOffTBW.addWatcher(this);
        _xOffTBW.setValue("");

        _yOffTBW = _w.yOffset;
        _yOffTBW.addWatcher(this);
        _yOffTBW.setValue("");

        _offTW = _w.offsetTable;
        _offTW.addWatcher(this);
        _offTW.setColumnHeaders(new String[]{"#", "p Offset", "q Offset"});
        _offTW.setBackground(_w.getBackground());
    }

    /**
     * Show the given SpOffsetPos
     */
    public void showPos(SpOffsetPos op) {
        _xOffTBW.setValue(op.getXaxisAsString());
        _yOffTBW.setValue(op.getYaxisAsString());
    }

    /**
     * Implements the _updateWidgets method from OtItemEditor in order
     * to setup the widgets to show the current values of the item.
     */
    protected void _updateWidgets() {
        TextBoxWidgetExt tbwe;
        SpIterOffset sio = (SpIterOffset) _spItem;

        // Get the title
        tbwe = _w.titleTBW;
        tbwe.setText(sio.getTitleAttr());

        // Get the current offset list and fill in the table widget
        _opl = sio.getPosList();
        _offTW.reinit(_opl);

        // Select the position that was previously selected.
        int selIndex = _avTab.getInt(".gui.selectedOffsetPos", 0);
        _offTW.selectPos(selIndex);

        _w.paTextBox.setValue(sio.getPosAngle());

        // Added by SDW - Sept. 2004.
        createSkyOffsets();

        // Try to create offset positions around the guide as well...
        createGuideOffsets();
    }

    private void createGuideOffsets() {
        _opl.resetGuidePositions();
        SpTelescopeObsComp obsComp = SpTreeMan.findTargetList(_spItem);

        if (obsComp != null) {
            if (obsComp.getPosList().getPosition("GUIDE") != null) {
                SpTelescopePos guidePos = (SpTelescopePos)
                        obsComp.getPosList().getPosition("GUIDE");
                SpTelescopePos basePos = obsComp.getPosList().getBasePosition();

                double[] guideOffset = RADecMath.getOffset(
                        guidePos.getXaxis(), guidePos.getYaxis(),
                        basePos.getXaxis(), basePos.getYaxis(),
                        _opl.getPosAngle());

                guideOffset[0] *= (Math.cos(Math.toRadians(
                        obsComp.getPosList().getBasePosition().getYaxis())));

                // Create a offset position around the guide for each offset
                for (int count = 0; count < _opl.size(); count++) {
                    double[] thisGuideOffset = new double[2];

                    SpOffsetPos currPos = (SpOffsetPos)
                            _opl.getPositionAt(count);

                    thisGuideOffset[0] = guideOffset[0] - currPos.getXaxis();
                    thisGuideOffset[1] = guideOffset[1] - currPos.getYaxis();

                    _opl.createGuideOffset(thisGuideOffset[0],
                            thisGuideOffset[1]);
                }
            }
        }
    }

    /**
     * Create sky offsets.
     *
     * See if we have any sky eyes amongst the children, and check if
     * they are following the base offset. If they are, get the obsComp
     * and calculate the offset from base of the sky offsets.
     */
    private void createSkyOffsets() {
        _opl.resetSkyPositions();

        // See if we can get the obsComp. If not, just return since we can't
        // do anything without it.
        SpTelescopeObsComp obsComp = SpTreeMan.findTargetList(_spItem);

        if (obsComp != null) {
            Vector<SpIterSky> children = new Vector<SpIterSky>();
            getOffsetSkyChildren((SpIterOffset) _spItem, children);

            // Loop through all the children
            for (int i = 0; i < children.size(); i++) {
                SpIterSky sky = children.get(i);

                // Get the associated scale factor
                double scale = sky.getScaleFactor();

                // Get the corrsponding position entry from the obsComp
                SpTelescopePos tp = (SpTelescopePos)
                        obsComp.getPosList() .getPosition(sky.getSky());

                // See if the sky position is specified as an offset
                // or absolute
                double[] childOffset;

                if (!tp.isOffsetPosition()) {
                    // To simplify things, we convert this to an offset
                    // position internally
                    SpTelescopePos base =
                            obsComp.getPosList().getBasePosition();
                    childOffset = RADecMath.getOffset(
                            tp.getXaxis(), tp.getYaxis(),
                            base.getXaxis(), base.getYaxis(),
                            _opl.getPosAngle());
                    childOffset[0] = childOffset[0]
                            * Math.cos(Math.toRadians(base.getYaxis()));
                } else {
                    childOffset = new double[2];
                    childOffset[0] = tp.getXaxis();
                    childOffset[1] = tp.getYaxis();
                }

                // Loop over all the current offset positions
                for (int count = 0; count < _opl.size(); count++) {
                    double[] skyOffset = new double[2];

                    // Get the current position and its coordinates
                    SpOffsetPos currPos = (SpOffsetPos)
                            _opl.getPositionAt(count);
                    skyOffset[0] = childOffset[0] + scale * currPos.getXaxis();
                    skyOffset[1] = childOffset[1] + scale * currPos.getYaxis();

                    // Now create the sky offset position
                    _opl.createSkyPosition(skyOffset[0], skyOffset[1]);
                }
            }
        }
    }

    /**
     * Get all the sky children that are following offset.
     */
    private void getOffsetSkyChildren(SpItem parent,
            Vector<SpIterSky> offsetSkys) {
        Enumeration<SpItem> children = parent.children();

        while (children.hasMoreElements()) {
            SpItem child = children.nextElement();

            if (child instanceof SpIterSky
                    && ((SpIterSky) child).getFollowOffset()) {
                offsetSkys.add((SpIterSky) child);
            } else {
                getOffsetSkyChildren(child, offsetSkys);
            }
        }
    }

    /**
     * A table row was selected, so show the selected position.
     *
     * @see TableWidgetWatcher
     */
    public void tableRowSelected(TableWidgetExt twe, int rowIndex) {
        if (twe != _offTW) {
            return; // shouldn't happen
        }

        // Show the selected position
        if (_curPos != null) {
            _curPos.deleteWatcher(this);
        }

        _curPos = _offTW.getSelectedPos();
        _curPos.addWatcher(this);
        showPos(_curPos);

        _avTab.set(".gui.selectedOffsetPos", _curPos.getTag());
    }

    /**
     * As part of the TableWidgetWatcher interface, this method must
     * be supported, though we don't care about TableWidget actions.
     *
     * @see TableWidgetWatcher
     */
    public void tableAction(TableWidgetExt twe, int colIndex, int rowIndex) {
    }

    /**
     * Watch changes to the x and y offset text boxes.
     * @see TextBoxWidgetWatcher
     */
    public void textBoxKeyPress(TextBoxWidgetExt tbwe) {
        SpIterOffset iterOffset = (SpIterOffset) _spItem;

        if (tbwe == _w.xOffset) {
            double nVal = _xOffTBW.getDoubleValue(0.0);

            if (_curPos != null) {
                _curPos.deleteWatcher(this);
                _curPos.setXY(nVal, _curPos.getYaxis());
                _curPos.addWatcher(this);
            }

        } else if (tbwe == _w.yOffset) {
            double nVal = _yOffTBW.getDoubleValue(0.0);

            if (_curPos != null) {
                _curPos.deleteWatcher(this);
                _curPos.setXY(_curPos.getXaxis(), nVal);
                _curPos.addWatcher(this);
            }

        } else if (tbwe == _w.titleTBW) {
            _spItem.setTitleAttr(tbwe.getText().trim());

        } else if (tbwe == _w.paTextBox) {
            iterOffset.setPosAngle(tbwe.getText().trim());
        }
    }

    /**
     * Text box action, ignore.
     *
     * @see TextBoxWidgetWatcher
     */
    public void textBoxAction(TextBoxWidgetExt tbwe) {
    }

    /**
     * The current position location has changed.
     *
     * @see TelescopePosWatcher
     */
    public void telescopePosLocationUpdate(TelescopePos tp) {
        telescopePosGenericUpdate(tp);
    }

    /**
     * The current position has been changed in some way.
     *
     * @see TelescopePosWatcher
     */
    public void telescopePosGenericUpdate(TelescopePos tp) {
        // This shouldn't happen ...
        if (tp != _curPos) {
            System.out.println(getClass().getName() + ": received a position "
                    + " update for a position other than the current one: "
                    + tp);
        } else {
            showPos(_curPos);
        }
    }

    private double _getGridXOffset() {
        return _w.gridXOffset.getDoubleValue(0.);
    }

    private double _getGridYOffset() {
        return _w.gridYOffset.getDoubleValue(0.);
    }

    private double _getGridXSpacing() {
        return _w.gridXSpacing.getDoubleValue(0.);
    }

    private double _getGridYSpacing() {
        return _w.gridYSpacing.getDoubleValue(0.);
    }

    private int _getGridRows() {
        return _w.gridRows.getIntegerValue(1);
    }

    private int _getGridCols() {
        return _w.gridCols.getIntegerValue(1);
    }

    /**
     * Add offset positions in a grid pattern, according to the specifications
     * in the "Create Grid" group box.
     */
    private void _createGrid() {
        if (_w.overwrite.isSelected()) {
            _opl.removeAllPositions();
        }

        double xOff = _getGridXOffset();
        double yOff = _getGridYOffset();

        double xSpace = _getGridXSpacing();
        double ySpace = _getGridYSpacing();

        int rows = _getGridRows();
        int cols = _getGridCols();

        int sign = -1;

        for (int y = 0; y < rows; ++y) {
            for (int x = 0; x < cols; ++x) {
                _opl.createPosition(xOff, yOff);
                xOff += ((sign == -1) ? -xSpace : xSpace);
            }

            sign = -sign;
            xOff += ((sign == -1) ? -xSpace : xSpace);
            yOff -= ySpace;
        }

        _offTW.reinit(_opl);
        _offTW.selectRowAt(0);

        // Added by MFO, 7 December 2001
        // I think this is implemented in a different way in
        // Gemini ot-2000B.12.
        try {
            TpeManager.get(_spItem).reset(_spItem);
        } catch (NullPointerException e) {
            // ignore
        }
    }

    /**
     * Get the selected position.
     */
    public SpOffsetPos getSelectedPos() {
        return _offTW.getSelectedPos();
    }

    /**
     * Handle button presses.
     */
    public void actionPerformed(ActionEvent evt) {
        Object w = evt.getSource();

        if (w == _w.newButton) {
            // Create a new offset position

            if (_curPos == null) {
                _opl.createPosition();

            } else {
                int i = _opl.getPositionIndex(_curPos);
                _curPos = _opl.createPosition(i + 1);
                _avTab.set(".gui.selectedOffsetPos", i + 1);
            }

        } else if (w == _w.removeAllButton) {
            // Remove an offset position

            _opl.removeAllPositions();
            _avTab.rm(".gui.selectedOffsetPos");

        } else if (w == _w.removeButton) {
            // Remove an offset position

            if (_curPos != null) {
                int i = _opl.getPositionIndex(_curPos);

                if (i > 0 && i == _opl.size() - 1) {
                    i--;
                }

                _avTab.set(".gui.selectedOffsetPos", i);
                _opl.removePosition(_curPos);
            }

        } else if (w == _w.topButton) {
            // Move an offset position to the top

            if (_curPos != null) {
                _opl.positionToFront(_curPos);
            }

        } else if (w == _w.upButton) {
            // Move an offset position up

            if (_curPos != null) {
                _opl.decrementPosition(_curPos);
            }

        } else if (w == _w.downButton) {
            // Move an offset position down

            if (_curPos != null) {
                _opl.incrementPosition(_curPos);
            }

        } else if (w == _w.bottomButton) {
            // Move an offset position to the back

            if (_curPos != null) {
                _opl.positionToBack(_curPos);
            }

        } else if (w == _w.createGridButton) {
            // Grid creation

            _createGrid();

        } else if (w == _w.centreOnBaseButton) {
            // Centre grid on base position (MFO, 24 August 2001)

            setGridOffsets();

        } else if (w == _w.setSpacingButton) {
            // Take Scan Area width and height as spacing paramters.

            SpIterRasterObs iterRaster = getRasterObsIterator(_spItem);

            if (iterRaster == null) {
                DialogUtil.error(_w, "No Scan/Raster Observe found.");
            } else {
                _w.gridXSpacing.setValue(iterRaster.getWidth());
                _w.gridYSpacing.setValue(iterRaster.getHeight());
            }
        }
        _updateWidgets();
    }

    // Added by MFO (20 February 2002)
    public void mouseClicked(MouseEvent e) {
    }

    public void mouseEntered(MouseEvent e) {
    }

    public void mouseExited(MouseEvent e) {
    }

    public void mousePressed(MouseEvent e) {
        setOffsetsRotation(true);
        _w.displayRotatedOffsets.setText(BUTTON_TEXT_ROTATION_TRUE);
    }

    public void mouseReleased(MouseEvent e) {
        setOffsetsRotation(false);
        _w.displayRotatedOffsets.setText(BUTTON_TEXT_ROTATION_FALSE);
    }

    /**
     * Centers the pattern around (0, 0).
     *
     * Added by MFO (22 August 2001).
     */
    protected void setGridOffsets() {
        // x
        double gridOffset = 0;
        double gridSpacing = 60;
        double gridSteps = 2;

        gridSpacing = _w.gridXSpacing.getDoubleValue(gridSpacing);
        gridSteps = _w.gridCols.getDoubleValue(gridSteps);

        gridOffset = (gridSpacing / 2.0) * (gridSteps - 1);
        _w.gridXOffset.setValue(gridOffset);

        // y
        gridOffset = 0;
        gridSpacing = 60;
        gridSteps = 2;

        gridSpacing = _w.gridYSpacing.getDoubleValue(gridSpacing);
        gridSteps = _w.gridRows.getDoubleValue(gridSteps);

        gridOffset = (gridSpacing / 2.0) * (gridSteps - 1);
        _w.gridYOffset.setValue(gridOffset);

        _createGrid();
    }

    public void setOffsetsRotation(boolean rotated) {
        if (rotated) {
            _w.paTextBox.setValue("0.0");

            // Get the current offset list
            double posAngle, rotatedX, rotatedY;
            TelescopePos tp;

            for (int i = 0; i < _opl.size(); i++) {
                tp = _opl.getPositionAt(i);
                posAngle = Math.toRadians(_opl.getPosAngle());
                double cos = Math.cos(posAngle);
                double sin = Math.sin(posAngle);
                double xAxis = tp.getXaxis();
                double yAxis = tp.getYaxis();

                rotatedX = (xAxis * cos) + (yAxis * sin);
                rotatedY = (xAxis * -sin) + (yAxis * cos);

                _offTW.setValueAt("" + (Math.rint(rotatedX * 10.0) / 10.0),
                        i, 1);
                _offTW.setValueAt("" + (Math.rint(rotatedY * 10.0) / 10.0),
                        i, 2);
            }

        } else {
            // Undo the rotation

            _updateWidgets();
        }
    }

    /**
     * Checks whether this offset iterator contains a Scan/Raster map.
     *
     * Returns true if there is an instance of
     * {@link orac.jcmt.iter.SpIterRasterObs}
     * inside the offset iterator item.
     */
    private static SpIterRasterObs getRasterObsIterator(SpItem spItem) {
        Enumeration<SpItem> children = spItem.children();
        SpItem child = null;
        SpIterRasterObs result = null;

        while (children.hasMoreElements() && (result == null)) {
            child = children.nextElement();

            if (child instanceof SpIterRasterObs) {
                result = (SpIterRasterObs) child;
            } else {
                result = getRasterObsIterator(child);
            }
        }

        return result;
    }
}
