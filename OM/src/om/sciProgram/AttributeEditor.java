/*
 * AttributeEditor.java
 *
 * Created on 06 December 2001, 12:24
 */

package om.sciProgram;

import gemini.sp.*;
import gemini.sp.iter.*;
import orac.ukirt.inst.*;
import orac.ukirt.iter.*;
import java.awt.*;
import java.awt.event.*;
import java.util.*;

/**
 *
 * @author  ab
 */
public class AttributeEditor extends javax.swing.JDialog 
  implements ActionListener {

  /** Creates new form AttributeEditor */
  public AttributeEditor(java.awt.Frame parent,
                           boolean modal) {
    super(parent, modal);
    initComponents();
  }

  public AttributeEditor(SpObs observation, java.awt.Frame parent,
                         boolean modal) {
    super(parent, modal);
    obs = observation;
    inst = (SpItem) SpTreeMan.findInstrument(obs);
    try {
      instName = inst.type().getReadable();
    } catch (NullPointerException nex) {
      System.out.println ("No instrument in scope!");
      instName = "None";
    }
    initComponents();
    
  }

  private void initComponents() {

    // Get the instrument attributes that are editable, along with
    // their current values.
    avpairs = getInstAttValues (inst);

    // Look for any iterators that may need addressing too, get those
    // attributes and values.
    iteravpairs = getIterAttValues (obs);

    //create the table model   
    model = new AttributeTableModel(instName, avpairs, iteravpairs);
    editorTable = new javax.swing.JTable(model);
    buttonPanel = new javax.swing.JPanel();
    cancel = new javax.swing.JButton();
    OK = new javax.swing.JButton();
    
    addWindowListener(new java.awt.event.WindowAdapter() {
      public void windowClosing(java.awt.event.WindowEvent evt) {
        closeDialog(evt);
      }
    });

    getContentPane().add(editorTable.getTableHeader(), java.awt.BorderLayout.NORTH);
    getContentPane().add(editorTable, java.awt.BorderLayout.CENTER);
        
    //        panel.setLayout(new java.awt.GridLayout(1, 0));
    
    OK.setText("OK");
    OK.addActionListener (this);
    buttonPanel.add(OK);
    
    cancel.setText("Cancel");
    cancel.addActionListener (this);
    buttonPanel.add(cancel,java.awt.BorderLayout.SOUTH);
    
    getContentPane().add(buttonPanel, java.awt.BorderLayout.SOUTH);
    
    pack();
  }
  
  public void actionPerformed (ActionEvent evt) {
    
    Object source = evt.getSource();
    
    if (source == cancel) {
        // Do nothing
      setVisible (false);
      dispose();
      
    }else if (source == OK) {
      System.out.println ("Changing the attributes...");
      
      // Here we get changed attribute values from the table model and
      // set them in the SpItem.
      for (int i = 0; i < avpairs.length; i++) {
        String att = (String) model.getValueAt(i,0);
        String val = (String) model.getValueAt(i,1);
        System.out.println (att + "=" + val);
        avTable.set (att, val);
      }

      // Then we run through the observation to find any "DARK" 
      // components and ask if they should also be updated... 
      // THIS IS UKIRT specific.
      
      setVisible (false);
      dispose();
      
    }
    
  }
  
  
  /** Closes the dialog */
  private void closeDialog(java.awt.event.WindowEvent evt) {
    setVisible(false);
    dispose();
  }
  
  /**
   * private String[[] getInstAttValues (SpItem instrument) gets
   * the current attribute values (for editable attributes) for 
   * the instrument.
   *
   * @param SpItem instrument
   * @return String[][] 
   * @author Alan Bridger
   */
  private String[][] getInstAttValues (SpItem instrument) {

    // Get the attributes that are editable.
    String attList = System.getProperty(instName+"_ATTRIBS", "None");
    if (attList.length() == 0) attList="None";
    StringTokenizer tok = new StringTokenizer (attList);
    String[][] av = new String[tok.countTokens()][2];

    // Get the current values of these attributes
    avTable = inst.getTable();

    int i = 0;
    while (tok.hasMoreElements()) {
      av[i][0] = tok.nextToken();
      av[i][1] = avTable.get(av[i][0]);
      if (av[i][1] == null) av[i][1] = "No such attribute";
      i++;
    }

    return av;

  }

  /**
   * private String[][] getIterAttValues (SpObs observation) gets
   * the current attribute values for any iterators that are in the 
   * sequence and have attributes the same as those editable for the
   * instrument.
   *
   * @param SpObs observation
   * @return String[][] 
   * @author Alan Bridger
   */
  private String[][] getIterAttValues (SpObs observation) {

    // Get the iterators that are editable.
    String attList = System.getProperty(instName+"_ATTRIBS", "None");
    if (attList.length() == 0) attList="None";
    StringTokenizer tokal = new StringTokenizer (attList);

    // See if any of the specified iterators are present and
    // if they have the attributes
    String iterList = "darkObs instUFTI"; // should get from property.

    StringTokenizer tokil = new StringTokenizer (iterList);
    String[][] av = new String[tokil.countTokens()*tokal.countTokens()][3];

    String[] al = new String[tokal.countTokens()];
    int i = 0;
    while (tokal.hasMoreElements()) {
        al[i] = tokal.nextToken();
        i++;
    }

    i = 0;
    while (tokil.hasMoreElements()) {
      String iterName = tokil.nextToken();
      System.out.println (iterName);
      for (int j=0; j<al.length; j++) {
        av[i][0] = iterName;
        av[i][1] = al[j];
        System.out.println ("Iter: "+av[i][0]+":"+av[i][1]);
        i++;
      }
    }

    // Get the current values of these attributes

    // First find the sequence component in the observation
    SpIterFolder itf = null;
    for ( child = observation.child(); child != null; child = child.next() ) {
      if ( child.type().equals( SpType.SEQUENCE ) ) {
        itf = (SpIterFolder) child;
        break;
      }
    }
    
    // Now look through it for the relevant iterators that might need
    // checking
    if ( itf != null ) {

      // Get an Enumeration of all the children of the Sequence.
      Enumeration eseq = itf.children();

      // Go through the children. Ignore all except the ones we want.
      while ( eseq.hasMoreElements() ) {
        child = (SpItem) eseq.nextElement();

        // Step through the iterators we are interested in, checking to
        // see if this is one of them. If it is then check the attribute.

        for (i=0; i<av.length; i++) {

          String iterClass = "SpIter"+av[i][0];
          System.out.println ("Title is "+child.getTitle());
          System.out.println ("Type is "+child.typeStr());
          System.out.println ("subType is "+child.subtypeStr());

          if (child.subtypeStr().equals(av[i][0])) {
          
            System.out.println ("Found "+av[i][0]);
            SpIterComp sic = (SpIterComp) child;
            SpIterEnumeration sie = sic.elements();
               
            // For each element check if the attribute matches
            // and if so then get its value.
            while ( sie.hasMoreElements() ) {
              Vector v = (Vector) sie.nextElement();

              for (int j = 0; j < v.size(); ++j ) {
                SpIterStep sis = (SpIterStep) v.elementAt(j);
                
                // Obtain the attribute and value pairs.
                for (int k = 0; k < sis.values.length; ++k ) {
                  SpIterValue siv = (SpIterValue) sis.values[ k ];
                  
                  // check the attribute
                  if (siv.attribute.equals(av[i][1])) {
                    // put values (This is an array...!)
                    av[i][2] = siv.values[0];
                    System.out.println ("Doing "+av[i][0]+":"+av[i][1]+"="+av[i][2]);

                  }
                }
              }
            }
          }
        }
      }
    }

    return av;

  }

  
  /**
   * @param args the command line arguments
   */
  public static void main(String args[]) {
    new AttributeEditor(new javax.swing.JFrame(), true).show();
  }
  
  private javax.swing.JTable editorTable;
  private javax.swing.JPanel buttonPanel;
  private javax.swing.JButton cancel;
  private javax.swing.JButton OK;
  private SpObs obs;
  private SpItem inst, child;
  private String instName;
  private String[][] avpairs;
  private String[][] iteravpairs;
  private SpAvTable avTable;
  private AttributeTableModel model;

}
