/*
 * AttributeTableModel.java
 *
 */

package om.sciProgram;

import gemini.sp.*;
import gemini.util.*;
import orac.ukirt.inst.*;
import orac.ukirt.iter.*;
import java.awt.*;
import java.awt.event.*;
import java.util.*;

/**
 *
 * @author  ab
 */
public class AttributeTableModel extends javax.swing.table.AbstractTableModel {

  final String [] columnNames = {
    "Attribute", "Value"
  };
  
  final Object [][] data;
  final boolean [] isNumber;
  final boolean [] isChanged;
  final Object  [] originalValue;
  private boolean isEditable = true;
  
  private boolean isANumber(String s) {
    try {
      double d = Double.valueOf(s).doubleValue();
      return true;
    } catch (Exception x) {
      return false;
    }
  }

  public AttributeTableModel (String instName,
			      Vector avPairs, 
                              Vector iterAivTriplets) {

    super();
    //    System.out.println ("-- AttributeTableModel() --");
    data = new Object[avPairs.size() + iterAivTriplets.size()][2];
    isNumber  = new boolean[avPairs.size() + iterAivTriplets.size()];
    isChanged = new boolean[avPairs.size() + iterAivTriplets.size()];
    originalValue = new String[avPairs.size() + iterAivTriplets.size()];
    int index=0;
    Enumeration pairs;
    Enumeration triplets;

    pairs = avPairs.elements();
    while (pairs.hasMoreElements()) {
      AVPair pair = (AVPair) pairs.nextElement();
      //      System.out.println(index + "\t" + pair);
      data[index][0] = pair.attribute();
      data[index][1] = pair.value();
      isNumber[index] = isANumber((String)pair.value());
      isChanged[index] = false;
      originalValue[index] = (String) pair.value();
      index++;
    }

    triplets = iterAivTriplets.elements();
    while (triplets.hasMoreElements()) {
      AIVTriplet triplet = (AIVTriplet) triplets.nextElement();
      //      System.out.println(index + "\t" + triplet);
      data[index][0] = triplet.iterator() + ":" + triplet.attribute();
      data[index][1] = triplet.value();
      isNumber[index] = isANumber((String)triplet.value());
      isChanged[index] = false;
      originalValue[index] = (String) triplet.value();
      index++;
    }

    //    System.out.println ("---------------------------");

  }

  public int getColumnCount() {
    return columnNames.length;
  }

  public int getRowCount() {
    return data.length;
  }

  public String getColumnName(int col) {
    return columnNames[col];
  }

  public Object getValueAt(int row, int col) {
    return data[row][col];
  }

  public Object getOriginalValueAt(int row, int col) {
    return originalValue[row];
  }

  public boolean isChangedAt(int row) {
    return isChanged[row];
  }

  public boolean isNumberAt(int row) {
    return isNumber[row];
  }

  public void setEditable(boolean editable) {
    isEditable = editable;
  }

  //   public Class getColumnClass(int c) {
  //     return getValueAt(0, c).getClass();
  //   }

  /*
   * Don't need to implement this method unless your table's
   * editable.
   */
  public boolean isCellEditable(int row, int col) {
    //Note that the data/cell address is constant,
    //no matter where the cell appears onscreen.
    if (col == 1) {
      return isEditable;
    } else {
      return false;
    }
  }


  /**
   * public void scaleValuesBy(double factor)
   *
   * Scale all the numerical values by the given factor.
   *
   * @param double factor
   * @author David Clarke
   *
   **/
  public void scaleValuesBy(double factor) {
    for (int row = 0; row < getRowCount(); row++) {
      if (isNumberAt(row)) {
	double d = Double.valueOf((String)getValueAt(row, 1)).doubleValue();
	d = d * factor;
	_setValueAt(Double.toString(d), row, 1);
      }
    }
  }
  

  /**
   * public void setValueAt(Object value, int row, int col)
   *
   * The user has edited a cell, so update the model accordingly. This
   * public method does whatever checking we do on the new data, and
   * calls the private methods _setValueAt(...) and _resetValueAt()
   * (according to whether the checks are passed or not) to do the
   * actual updating.
   *
   * The data checking is fairly primitive. If the field was
   * originally a number (as denoted by the isNumber[] flags), then
   * insist that the new value is also a number. If the field was not
   * a number before, then don't apply any checking at all.
   *
   * @param Object value
   * @param int row
   * @param int col
   * @author David Clarke
   *
   **/
  public void setValueAt(Object value, int row, int col) {
    if (isNumberAt(row)) {
      if (isANumber((String) value)) {
	_setValueAt(value, row, col);
      } else {
	_resetValueAt(value, row, col);
      }
    } else {
      _setValueAt(value, row, col);
    }
  }
  

  /**
   * private void _setValueAt(Object value, int row, int col)
   *
   * Do the actual updates to the model requested by the call to the
   * public setValueAt(...) -- remember the new data and note that the
   * data is changed. Also triggers the display refresh.
   *
   * @param Object value
   * @param int row
   * @param int col
   * @author David Clarke
   *
   **/
  private void _setValueAt(Object value, int row, int col) {
    data[row][col] = value;
    isChanged[row] = true;
    fireTableCellUpdated(row, col);
  }    

  /**
   * private void _resetValueAt(int row, int col)
   *
   * This is for whan an update fails due to data checking
   * failing. Simply update the display without updating the model.
   *
   * @param int row
   * @param int col
   * @author David Clarke
   *
   **/
  private void _resetValueAt(Object value, int row, int col) {
    ErrorBox eb = new ErrorBox(value + " is an invalid value for the " + data[row][0] +
			       " attribute.");
    fireTableCellUpdated(row, col);
  }    


}
