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

  /**
   * This constructor creates the table based attribute editor.
   */
  public AttributeEditor(java.awt.Frame parent,
			 boolean modal) {
    super(parent, "Attribute Editor", modal);
    initTableComponents();
    doingScale = false;
  }

  /**
   * This constructor creates the table based attribute editor.
   */
  public AttributeEditor(SpObs observation, java.awt.Frame parent,
                         boolean modal) {
    super(parent, "Attribute Editor", modal);
    commonInitialisation(observation);
    initTableComponents();
    doingScale = false;
  }

  /**
   * This constructor creates the attribute scaling form.
   */
  public AttributeEditor(SpObs          observation,
			 java.awt.Frame parent,
			 boolean        modal,
			 String         configAttribute,
			 boolean        haveScaledThisObs,
			 Double         oldFactor,
			 boolean        rescale) {
    super(parent, "Attribute Scaling", modal);
    commonInitialisation(observation);
    initScaleComponents(configAttribute, haveScaledThisObs, oldFactor.doubleValue(), rescale);
    doingScale = true;
  }


  /**
   * private void commonInitialisation()
   *
   * Does the common initialisation required by all AttributeEditors,
   * regardless of which type they are.
   **/
  private void commonInitialisation(SpObs observation) {
    obs = observation;
    inst = (SpItem) SpTreeMan.findInstrument(obs);
    try {
      instName = inst.type().getReadable();
    } catch (NullPointerException nex) {
      System.out.println ("No instrument in scope!");
      instName = "None";
    }
    sequence = findSequence(obs);
  }

  /**
   * private Vector getConfigNames(String configItem)
   *
   * Look for the given configuration item (accessed via
   * System.getProperty()). This should have a value which is a space
   * separated list of tokens. Return a Vector of these tokens.
   *
   * @param String configItem
   * @author David Clarke
   **/
  private Vector getConfigNames(String configItem) {
    String list = System.getProperty(configItem);
    //    if (list.length() == 0) list = "None";

    if (list == null) {
      return new Vector();
    }

    StringTokenizer tok = new StringTokenizer (list);
    Vector names = new Vector(tok.countTokens());

    while (tok.hasMoreElements()) {
      String name = tok.nextToken();
      names.addElement(name);
    }

    return names;
  }


  /**
   * private SpItem findSequence(SpObs observation)
   *
   * Search the children of the given observation for the sequence
   * component. Returns the first one it finds (kind of assuming that
   * there can be only one), or null if it doesn't find one at all.
   *
   * @param SpObs observation
   * @author David Clarke
   **/
  private SpItem findSequence(SpObs observation) {
    Enumeration children = observation.children();

    while (children.hasMoreElements()) {
      SpItem child = (SpItem) children.nextElement();
      if (child.type().equals(SpType.SEQUENCE)) {
	return child;
      }
    }
    return null;
  }


  /**
   * private String concatPath(String path, SpItem newBit)
   *
   * Add the representation of newBit to the given path and return the
   * result.
   *
   * @param String path
   * @param SpItem newBit
   * @author David Clarke
   **/
  private String concatPath(String path, SpItem newBit) {
    String result;

    if (path.equals("")) {
      // Path is empty, no need for a separator.
      if ((newBit == obs) || (newBit == sequence)) {
	// Skip over the observation and the sequence if at the start
	// of the path. This stops all the tree attributes beginning
	// with Observation/Sequence and keeps the dialogue box down
	// to a reasonable width.
	result = "";
      } else {
	// Otherwise, just start with the new bit
	result = newBit.getTitle();
      }
    } else {
      // Path is not empty, bung on the new bit regardless, using a
      // separator
      result = path + "/" + newBit.getTitle();
    }
    return result;
  }


  /**
   * private void initTableComponents()
   *
   * Create the tabular attribute editor components
   */
  private void initTableComponents() {

    System.out.println ("Opening attribute editor");

    configAttributes = getConfigNames(instName + "_ATTRIBS");
    configIterators  = getConfigNames(instName + "_ITERATORS");

    // Get the instrument attributes that are editable, along with
    // their current values.
    avPairs = getInstAttValues (inst);

    // Look for any iterators that may need addressing too, get those
    // attributes and values.
    iavTriplets = new Vector();
    getIterAttValues(sequence);

    //create the table model   
    model = new AttributeTableModel(instName, avPairs, iavTriplets);
    editorTable = new javax.swing.JTable(model);
    buttonPanel = new javax.swing.JPanel();
    cancel = new javax.swing.JButton();
    OK = new javax.swing.JButton();
    
    addWindowListener(new java.awt.event.WindowAdapter() {
      public void windowClosing(java.awt.event.WindowEvent evt) {
        closeDialog();
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
    initColumnSizes();
    pack();
  }


  /**
   * private void initScaleComponents(String attribute,
   *                                  boolean haveScaledThisObs,
   *                                  double oldFactor,
   *                                  boolean rescale)
   *
   * Create the scaling components
   **/
  private void initScaleComponents(String attribute,
				   boolean haveScaledThisObs,
				   double oldFactor,
				   boolean rescale) {

    boolean showTable = true;	// Set to true for debug

    System.out.println ("Applying scaling factor");

    configAttributes = getConfigNames(instName + "_" + attribute);
    if (configAttributes.size() == 0) {
      configAttributes.addElement("exposureTime");
    }
    configIterators  = getConfigNames(instName + "_ITERATORS");

    // Get the instrument attributes that are editable, along with
    // their current values.
    avPairs = getInstAttValues(inst);

    // Look for any iterators that may need addressing too, get those
    // attributes and values.
    iavTriplets = new Vector();
    getIterAttValues(sequence);

    // Create the table model. Still used in the scaling mode even
    // though it is not displayed.
    model = new AttributeTableModel(instName, avPairs, iavTriplets);
    if (showTable) {
      editorTable = new javax.swing.JTable(model);
    }
    buttonPanel = new javax.swing.JPanel();
    if (haveScaledThisObs) {
      scaleWarning = new javax.swing.JLabel("Warning: ");
      scaleWarning.setForeground(Color.red);
      buttonPanel.add(scaleWarning);
      scaleWarning = new javax.swing.JLabel("This observation has already been scaled.");
      buttonPanel.add(scaleWarning);
    }

    if (rescale) {
      scaleLabel = new javax.swing.JLabel("Scale " + configAttributes.elementAt(0) +
					  " attributes by " + oldFactor);
      scaleFactor = new javax.swing.JTextField(Double.toString(oldFactor), 10);
      buttonPanel.add(scaleLabel);
    } else {
      scaleLabel = new javax.swing.JLabel("Scale " + configAttributes.elementAt(0) +
					  " attributes by ");
      scaleFactor = new javax.swing.JTextField(Double.toString(oldFactor), 10);
      buttonPanel.add(scaleLabel);
      buttonPanel.add(scaleFactor);
    }

    cancel = new javax.swing.JButton();
    OK = new javax.swing.JButton();
    
    addWindowListener(new java.awt.event.WindowAdapter() {
      public void windowClosing(java.awt.event.WindowEvent evt) {
        closeDialog();
      }
    });

    if (showTable) {
      getContentPane().add(editorTable.getTableHeader(), java.awt.BorderLayout.NORTH);
      getContentPane().add(editorTable, java.awt.BorderLayout.CENTER);
    }
        
    //        panel.setLayout(new java.awt.GridLayout(1, 0));
    
    OK.setText("OK");
    OK.addActionListener (this);
    buttonPanel.add(OK);
    
    cancel.setText("Cancel");
    cancel.addActionListener (this);
    buttonPanel.add(cancel,java.awt.BorderLayout.SOUTH);
    
    getContentPane().add(buttonPanel, java.awt.BorderLayout.SOUTH);
    if (showTable) {
      initColumnSizes();
    }
    pack();
    //      makeChanges();
    //      closeDialog();
  }


  /**
   * public void actionPerformed(ActionEvent evt)
   *
   * Called whenever a button on the editor has been pressed. If
   * appropriate (ie. it is the OK button which has been pressed)
   * write any updated values back to the observation sequence.
   *
   * @param ActionEvent evt
   **/
  public void actionPerformed (ActionEvent evt) {
    
    Object source = evt.getSource();
    
    if (source == cancel) {
      // Do nothing
      if (doingScale) {
	_scaleFactorUsed = -1.0;
      }
    } else if (source == OK) {
      makeChanges();
    }

    closeDialog();
  }
  
  
  /**
   * public void makeChanges()
   *
   * Effect any scaling and then write back all changes to the
   * observation
   **/
  private void makeChanges() {
    if (doingScale) {
      System.out.println("Scale factor = " + scaleFactor.getText());
      _scaleFactorUsed = Double.valueOf(scaleFactor.getText()).doubleValue();
      model.scaleValuesBy(_scaleFactorUsed);
    }
    
    boolean doneSome = false;
    
    // Here we get changed attribute values from the table model and
    // set them in the SpItem.
    int row = 0;
    Enumeration pairs    = avPairs.elements();
    Enumeration triplets = iavTriplets.elements();
    
    while (pairs.hasMoreElements()) {
      AVPair pair = (AVPair) pairs.nextElement();
      if (model.isChangedAt(row)) {
	if (!doneSome) {
	  System.out.println ("Updating attributes");
	  doneSome = true;
	}
	System.out.println(model.getValueAt(row, 0) + " = '" + model.getValueAt(row, 1) + "'");
	pair.origin().setValue((String)model.getValueAt(row, 1));
      }
      row ++;
    }
    while (triplets.hasMoreElements()) {
      AIVTriplet triplet = (AIVTriplet) triplets.nextElement();
      if (model.isChangedAt(row)) {
	if (!doneSome) {
	  System.out.println ("Updating attributes");
	  doneSome = true;
	}
	System.out.println(model.getValueAt(row, 0) + " = '" + model.getValueAt(row, 1) + "'");
	triplet.origin().setValue((String)model.getValueAt(row, 1));
      }
      row ++;
    }
    
    if (!doneSome) {
      System.out.println ("No changes made");
    }
    
  }

  /** Closes the dialog */
  private void closeDialog() {
    setVisible(false);
    dispose();
  }
  
  /**
   * private Vector getInstAttValues (SpItem instrument) gets
   * the current attribute values (for editable attributes) for 
   * the instrument.
   *
   * @param SpItem instrument
   * @return Vector
   * @author Alan Bridger
   */
  private Vector getInstAttValues (SpItem instrument) {

    // Get the current values of the editable attributes
    Enumeration attributes = configAttributes.elements();
    Vector      av = new Vector(configAttributes.size());

    avTable = inst.getTable();

    while (attributes.hasMoreElements()) {
      String att;
      String val;
      AVPair avp;

      att = (String) attributes.nextElement();
      if (att != null) {
	val = avTable.get(att);
      } else {
	val = "No such attribute";
      }
      if ((val != null) && (!val.equals(""))) {
	avp = new AVPair(att, val, instrument, att, 0);
	av.addElement(avp);
      }
    }

    //    System.out.println(av.size() + " attributes");
    return av;

  }

  /**
   * private void getIterAttValues(SpObs observation)
   *
   * Walk the observation tree looking for things to edit. We are
   * interested in the attributes referred to in the config file. The top
   * level attributes should have been found and put into the avPairs
   * vector prior to invoking this method (currently done by
   * getInstAttValues()). This method works in the following phases:
   *
   *   Find, from the config file, the names of the iterator types we want to edit 
   *   Find, from the avPairs, the names of the attributes we want to edit 
   *   Recursively walk the tree finding (iterator, attribute, value) triplets thus specified.
   *
   * @param SpObs observation
   * @author David Clarke
   */
  private void getIterAttValues (SpItem root) {
    System.out.println("Attributes = " + configAttributes);
    System.out.println("Iterators  = " + configIterators);
    getIterAttValues(root, "", "");
  }

  /**
   * private void getLocalIterAttValues(SpItem root, String indent)
   *
   * Get the local attributes corresponding to the configAttributes
   *
   * @param SpItem root
   * @param String subtype
   * @param String path
   * @param String indent
   * @author David Clarke
   *
   */
  private void getLocalIterAttValues(SpItem root, String subtype, String path, String indent) {
    final String iterSuffix = "Iter";
    
    // Iterate over root's attributes
    SpAvTable   table = root.getTable();
    Enumeration keys = table.attributes();
    
    //    System.out.println(indent +
    //		       root.typeStr() + "_" + subtype +
    //		       " (" + root.name() + ") " +
    //		       " (" + root.getTitle() + ") " +
    //		       root);
    while (keys.hasMoreElements()) {
      String key;
      String lookupKey;

      key = (String) keys.nextElement();
      if (key.endsWith(iterSuffix)) {
	lookupKey = key.substring(0, key.length()-iterSuffix.length());
      } else {
	lookupKey = key;
      }

      if (configAttributes.contains(lookupKey)) {
	Vector val = table.getAll(key);
	//	System.out.println(indent + "   YES " + key + " (as " + lookupKey + ") = " + val);
	if (val.size() == 1) {
	  iavTriplets.addElement(new AIVTriplet(concatPath(path, root),
						lookupKey,
						(String) val.elementAt(0),
						new AttributeOrigin(root, key, 0))); 
	} else {
	  for (int i = 0; i < val.size(); i++) {
	    iavTriplets.addElement(new AIVTriplet(concatPath(path, root),
						  lookupKey + "[" + i + "]",
						  (String) val.elementAt(i),
						  new AttributeOrigin(root, key, i))); 
	  }
	}
	//      } else {
	//	System.out.println(indent +  "   no  " + key + " (as " + lookupKey + ")");
      }
    }
  }



  /**
   * private void getIterAttValues(SpItem root, String indent)
   *
   * Recursively walk the observation tree (in pre-order) looking for
   * attributes specified in avPairs that are in iterators specified
   * by configIterators. Get the value of any such attribute and
   * append to the iavTriplets vector.
   *
   * @param SpItem root
   * @param String path
   * @param String indent
   * @author David Clarke
   *
   */
  private void getIterAttValues (SpItem root,
				 String path,
				 String indent) {
    //    System.out.println(indent + "-- getIterAttValues(" +
    //		       root.subtypeStr() + ", " +
    //		       root.getTitle() + ", " +
    //		       root.name() + ")"
    //		       );

    if (root != null) {

      String subtype = root.subtypeStr();

      // If root is a suitable iterator, look through its attributes
      // for ones specified in avPairs
      if (configIterators.contains(subtype)) {
	getLocalIterAttValues(root, subtype, path, indent + "   ");
      }

      // Recurse over root's children
      Enumeration children = root.children();
      while (children.hasMoreElements()) {
	SpItem child = (SpItem) children.nextElement();
	getIterAttValues(child, concatPath(path, root), indent+"   ");
      }
    }
  }

  /**
   * private void initColumnSizes() picks good column sizes for the table.
   *
   * @author David Clarke
   */
  private void initColumnSizes() {
    
    final int minWidth = 80;
    final int padding  = 20;

    javax.swing.table.TableColumn column = null;
    Component comp = null;
    int width;
    int maxWidth;
    
    for (int i = 0; i < model.getColumnCount(); i++) {
      maxWidth = minWidth;	// sic - set a lower bound for the width
      column = editorTable.getColumnModel().getColumn(i);

      comp = column.getHeaderRenderer().
	getTableCellRendererComponent(null, column.getHeaderValue(),
				      false, false, 0, 0);
      width = comp.getPreferredSize().width;
      maxWidth = Math.max(width, maxWidth);
      
      for (int row = 0; row < model.getRowCount(); row++) {
	comp = editorTable.getDefaultRenderer(model.getColumnClass(i)).
	  getTableCellRendererComponent(editorTable, model.getValueAt(row, i),
					false, false, 0, i);
	width = comp.getPreferredSize().width;
	maxWidth = Math.max(width, maxWidth);
      }
      
      //      System.out.println("Initializing width of column " + i +
      //			 " to " + maxWidth);
      
      //XXX: Before Swing 1.1 Beta 2, use setMinWidth instead.
      // Add some padding to the column, this is purely aesthetic
      column.setPreferredWidth(maxWidth + padding);
    }
  } 


  /**
   * @param args the command line arguments
   */
  public static void main(String args[]) {
    new AttributeEditor(new javax.swing.JFrame(), true).show();
  }

  public static double scaleFactorUsed() {
    return _scaleFactorUsed;
  }

  private static double _scaleFactorUsed = 1.0;

  private javax.swing.JTable editorTable;
  private javax.swing.JPanel buttonPanel;
  private javax.swing.JButton cancel;
  private javax.swing.JButton OK;
  private javax.swing.JPanel scalePanel;
  private javax.swing.JTextField scaleFactor;
  private javax.swing.JLabel scaleLabel;
  private javax.swing.JLabel scaleWarning;
  private SpObs obs;
  private SpItem sequence;
  private SpItem inst, child;
  private String instName;
  private Vector configAttributes;
  private Vector configIterators;
  private Vector avPairs;
  private Vector iavTriplets;
  private SpAvTable avTable;
  private AttributeTableModel model;
  private boolean doingScale;

}
