/*
 * AttributeTableModel.java
 *
 */

package om.sciProgram;

import gemini.sp.*;
import orac.ukirt.inst.*;
import orac.ukirt.iter.*;
import java.awt.*;
import java.awt.event.*;

/**
 *
 * @author  ab
 */
public class AttributeTableModel extends javax.swing.table.AbstractTableModel {

  final String [] columnNames = {
    "Attribute", "value"
  };
  
  final Object [][] data;
  
  public AttributeTableModel (String instName, String [][] avPairs, 
                              String [][] iterAvPairs) {

    super();
    data = new Object[avPairs.length+iterAvPairs.length][2];
    int index=0;
    for (int i = 0; i < avPairs.length; i++) {
      data[index][0] = avPairs[index][0];
      data[index][1] = avPairs[index][1];
      index++;
    }
    for (int i = 0; i < iterAvPairs.length; i++) {
      data[index][0] = iterAvPairs[index][0]+":"+iterAvPairs[index][1];
      data[index][1] = iterAvPairs[index][2];
      index++;
    }

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
    if (col != 1) {
      return false;
    } else {
      return true;
    }
  }

  /*
   * Don't need to implement this method unless your table's
   * data can change.
   */

  public void setValueAt(Object value, int row, int col) {

    System.out.println ("In setValue at with value="+value+" at "+row+","+col);
    //debugging code not shown...
    //ugly special handling of Integers not shown...
    data[row][col] = value;
    fireTableCellUpdated(row, col);
    //debugging code not shown...
  }


}
