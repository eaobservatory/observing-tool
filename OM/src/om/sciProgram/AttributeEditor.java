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
import javax.swing.*;
import javax.swing.table.*;

/**
 *
 * @author  ab
 */
public class AttributeEditor extends JDialog 
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
   * private void createComponents()
   *
   * Create all the various components used by the dialogues. Don't
   * lay them out yet, that is done by the initXXXComponents()
   * methods.
   **/
  private void createComponents(String attributes, String iterators, double oldFactor) {

    System.out.println("Editing attributes.");
    configAttributes = getConfigNames(attributes);
    configIterators  = getConfigNames(iterators);

    // Get the instrument attributes that are editable, along with
    // their current values.
    avPairs = getInstAttValues (inst);

    // Look for any iterators that may need addressing too, get those
    // attributes and values.
    iavTriplets = new Vector();
    getIterAttValues(sequence);

    // Create the table model and a suitable sized scrolling view on it
    model = new AttributeTableModel(instName, avPairs, iavTriplets);
    editorTable = new JTable(model);
    scrollPane  = new JScrollPane(editorTable);
    initColumnSizes();
    editorTable.setPreferredScrollableViewportSize(editorTable.getPreferredSize());

    buttonPanel = new JPanel();
    buttonPanel.setLayout(new BoxLayout(buttonPanel, BoxLayout.X_AXIS));
    buttonPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
    cancel = new JButton();
    cancel.setText("Cancel");
    cancel.addActionListener (this);
    OK = new JButton();
    OK.setText("OK");
    OK.addActionListener (this);
    
    addWindowListener(new java.awt.event.WindowAdapter() {
      public void windowClosing(java.awt.event.WindowEvent evt) {
        closeDialog();
      }
    });

    warnPanel = new JPanel();
    warnPanel.setLayout(new BoxLayout(warnPanel, BoxLayout.X_AXIS));
    warn = new JLabel("Warning: ");
    warn.setForeground(Color.red);
    warnEmpty   = new JLabel("No attributes found which match the config spec.");
    warnAlready = new JLabel("This observation has already been scaled.");

    scalePanel = new JPanel();
    scalePanel.setLayout(new BoxLayout(scalePanel, BoxLayout.X_AXIS));
    scaleLabel = new JLabel("Scale " + configAttributes.elementAt(0) +
			    " attributes by ");
    scaleFactor = new JTextField(Double.toString(oldFactor), 10);
    rescaleLabel = new JLabel("Scale " + configAttributes.elementAt(0) +
			      " attributes by " + oldFactor);
    

    // Fix the height of the scaleFactor box to that of the
    // scaleLabel. This keeps the layout sensible when resizing the
    // dialog window (without it, the scaleFactor is the component
    // which grows with the window, which is OK width wise but bogging
    // height wise)
    int height = scaleLabel.getMaximumSize().height; 
    scaleFactor.setMinimumSize(new Dimension(scaleFactor.getMinimumSize().width, height));
    scaleFactor.setPreferredSize(new Dimension(scaleFactor.getPreferredSize().width, height));
    scaleFactor.setMaximumSize(new Dimension(scaleFactor.getMaximumSize().width, height));

    getContentPane().setLayout(new BoxLayout(getContentPane(), BoxLayout.Y_AXIS));
  }


  /**
   * private void initTableComponents()
   *
   * Layout the editing form.
   */
  private void initTableComponents() {

    boolean tableIsEmpty;

    createComponents(instName + "_ATTRIBS", instName + "_ITERATORS", -1.0);
    tableIsEmpty = (model.getRowCount() == 0);
    
    if (tableIsEmpty) {
      // No attributes to edit. Just put a warning to this effect and an OK button
      warnPanel.add(warn);
      warnPanel.add(warnEmpty);
      getContentPane().add(warnPanel);
      // Rather than using the OK button, make the cancel button say
      // OK and use that instead. this stops the action listener from
      // trying to do anything with the empty table.
      cancel.setText("OK");
      buttonPanel.add(cancel);
      getContentPane().add(buttonPanel);
    } else {
      // There are attributes to edit. Show the table, the OK button
      // and the Cancel button.
      //      getContentPane().add(editorTable.getTableHeader());
      //      getContentPane().add(editorTable);
      getContentPane().add(scrollPane);
      model.setEditable(true);
      buttonPanel.add(OK);
      buttonPanel.add(cancel);
      getContentPane().add(buttonPanel);
    }
        
    pack();
  }


  /**
   * private void initScaleComponents(String attribute,
   *                                  boolean haveScaledThisObs,
   *                                  double oldFactor,
   *                                  boolean rescale)
   *
   * Layout the scaling form.
   **/
  private void initScaleComponents(String attribute,
				   boolean haveScaledThisObs,
				   double oldFactor,
				   boolean rescale) {

    boolean tableIsEmpty;

    createComponents(instName + "_" + attribute, instName + "_ITERATORS", oldFactor);
    tableIsEmpty = (model.getRowCount() == 0);

    if (tableIsEmpty) {
      // No attributes to scale. Just put a warning to this effect and an OK button
      warnPanel.add(warn);
      warnPanel.add(warnEmpty);
      getContentPane().add(warnPanel);
      // Rather than using the OK button, make the cancel button say
      // OK and use that instead. This stops the action listener from
      // trying to do anything with the empty table.
      cancel.setText("OK");
      buttonPanel.add(cancel);
      getContentPane().add(buttonPanel);
    } else {
      // There are attributes to edit. Show the table, the OK button
      // and the Cancel button.
      getContentPane().add(scrollPane);
      model.setEditable(false);
      if (haveScaledThisObs) {
	warnPanel.add(warn);
	warnPanel.add(warnAlready);
	getContentPane().add(warnPanel);
      }
      if (rescale) {
	scalePanel.add(rescaleLabel);
      } else {
	scalePanel.add(scaleLabel);
	scalePanel.add(scaleFactor);
      }
      getContentPane().add(scalePanel);
      buttonPanel.add(OK);
      buttonPanel.add(cancel);
      getContentPane().add(buttonPanel);
    }
        
    pack();
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
      closeDialog();
    } else if (source == OK) {
      if (makeChanges()) {
	closeDialog();
      }
    }
  }
  
  
  /**
   * private boolean makeChanges()
   *
   * Effect any scaling and then write back all changes to the
   * observation. Return value denotes whether or not the method
   * completed normally.
   **/
  private boolean makeChanges() {
    if (doingScale) {
      ErrorBox eb;
      try {
	_scaleFactorUsed = Double.valueOf(scaleFactor.getText()).doubleValue();
	if (_scaleFactorUsed <= 1e-3) {
	  eb = new ErrorBox("Negative or Zero scale factor (" + scaleFactor.getText() + ").");
	  return false;
	} else {
	  model.scaleValuesBy(_scaleFactorUsed);
	}
      } catch (Exception e) {
	eb = new ErrorBox("Invalid scale factor (" + scaleFactor.getText() + ").");
	return false;
      }
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

    return true;
    
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

    TableColumn column = null;
    Component comp = null;
    int width;
    int maxWidth;
    
    for (int i = 0; i < model.getColumnCount(); i++) {
      maxWidth = minWidth;	// sic - set a lower bound for the width
      column = editorTable.getColumnModel().getColumn(i);

      TableCellRenderer r = column.getHeaderRenderer();
      if (r == null) {
	comp = editorTable.getDefaultRenderer(model.getColumnClass(i)).
	  getTableCellRendererComponent(editorTable, column.getHeaderValue(),
					false, false, 0, i);
      } else {
	comp = r.getTableCellRendererComponent(null, column.getHeaderValue(),
					       false, false, 0, 0);
      }
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
    new AttributeEditor(new JFrame(), true).show();
  }

  public static double scaleFactorUsed() {
    return _scaleFactorUsed;
  }

  private static double _scaleFactorUsed = 1.0;

  private JScrollPane scrollPane;
  private JTable editorTable;
  private JPanel buttonPanel;
  private JPanel warnPanel;
  private JPanel scalePanel;
  private JButton cancel;
  private JButton OK;
  private JLabel warn;
  private JLabel warnEmpty;
  private JLabel warnAlready;
  private JLabel scaleLabel;
  private JLabel rescaleLabel;
  private JTextField scaleFactor;

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
