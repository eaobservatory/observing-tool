package om.console;                                                                         
import java.util.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.JPanel;
import javax.swing.event.*;
import javax.swing.border.*;
import om.util.UT;
import om.util.SdfFiles;
import java.io.FileNotFoundException;
import om.sciProgram.AlertBox;

/** movie class is about showing movie control.
    Please note this is serialized for RMI calls
    even I was told that I can't serialize a swing object.
    since it is host-dependent. but there is only ONE type of host anyway

    @version 1.0 1st Sept 1999
    @author M.Tan@roe.ac.uk
*/
public final class movie extends JFrame 
  implements ActionListener, ItemListener, java.io.Serializable
{
  /** public movie(sendCmds c) is
    the class constructor.

    @param sendCmds
    @return  none
    @throws none
  */
  public movie(sendCmds c)
  {
    this(c, false);
  }

  /**
   * @param spectrometer true if instrument can take data in spectroscopy mode.
   */
  public movie(sendCmds c, boolean spectrometer)
  {
      cSent = c;
      setTitle("Quick Look Feature Control");

      setLocation(800,400);

      addWindowListener(new WindowAdapter()
         {  public void windowClosing(WindowEvent e)
            {  //System.exit(0);
            }
         } );

      Border border = BorderFactory.createMatteBorder(8, 8,
            8,8, Color.lightGray);
      Border buttonBorder = new BevelBorder(BevelBorder.RAISED);

      Container contentPane  =  getContentPane();
      contentPane.setLayout(new BorderLayout(10,10));

      Start = new JButton("Start");
      Stop = new JButton("Stop");
      Dismiss = new JButton("Dismiss");

      Stop.setEnabled(false);
      Start.setBorder(buttonBorder);
      Start.setBackground(Color.green);
      //      Start.setMinimumSize (new Dimension (120,30));
      Stop.setBorder(buttonBorder);
      Stop.setBackground(Color.red);
      //      Stop.setMinimumSize (new Dimension (120,30));
      Dismiss.setBorder(buttonBorder);
      //      Dismiss.setMinimumSize (new Dimension (120,30));

      JPanel controlButtons = new JPanel();
      controlButtons.setBorder(border);
      controlButtons.setLayout(new GridLayout(1,3,20,10));

      controlButtons.add(Start,"West");
      controlButtons.add(Stop);
      controlButtons.add(Dismiss,"East");
      contentPane.add(controlButtons);

      expTime = new statusPanel("Movie Time:","");
      expTime.setBorder(border);
      expTime.getValu().setEditable(true);
      expTime.getValu().setBorder (new BevelBorder(BevelBorder.LOWERED));
      contentPane.add(expTime,"North");

      JPanel bPanel = new JPanel();
      bPanel.setBorder(border);
      bPanel.setLayout(new GridLayout(6,1,4,4));

      cutRow = new statusPanel("Display Cut Row:","");
      
      cutRow.setBorder(border);

      JPanel backGround = new JPanel();
      backGround.setLayout(new GridLayout(1,2,4,4));

      JPanel maskPixel = new JPanel();
      maskPixel.setLayout(new GridLayout(1,2,4,4));

      bGroundOn = new JCheckBox("Frame to subtract:");
      bGround = new JButton("Load");
      
      backGround.add(bGroundOn);
      backGround.add(bGround);
      backGround.setBorder(border);
      
      bPixelOn = new JCheckBox("Bad pixel mask:");
      bPixel = new JButton("Load");
      maskPixel.add(bPixelOn);
      maskPixel.add(bPixel);
      maskPixel.setBorder(border);

      Apply = new JButton("Apply Changes");

      modePanel = new statusPanel("Mode", _modes[_currentMode]);
      modePanel.getValu().setEditable(false);
      modePanel.setBorder(border);

      switchModeButton = new JButton("Switch to \"" + _modes[(_currentMode+1)%2] + "\"");

      // if the instrument is (also) a spectrometer (the instrument can take data in spectroscopy mode)
      // then set widgets enabled/editable according to _currentMode and leave switchModeButton enabled.
      if(spectrometer) {
        cutRow.getValu().setEditable(_currentMode == SPECTROSCOPY);
        cutRow.getValu().setEnabled(_currentMode == SPECTROSCOPY);
        bGround.setEnabled(_currentMode == SPECTROSCOPY);
        bPixel.setEnabled(_currentMode == SPECTROSCOPY);
        bGroundOn.setEnabled(_currentMode == SPECTROSCOPY);
        bPixelOn.setEnabled(_currentMode == SPECTROSCOPY);
      }
      // If the instrument does not have a spectroscopy mode, then disable widgets AND switchModeButton
      else {
        cutRow.getValu().setEditable(false);
        cutRow.getValu().setEnabled(false);
        bGround.setEnabled(false);
        bPixel.setEnabled(false);
        bGroundOn.setEnabled(false);
        bPixelOn.setEnabled(false);

        switchModeButton.setEnabled(false);
      }
      
      bPanel.add(cutRow);
      bPanel.add(backGround);
      bPanel.add(maskPixel);
      bPanel.add(Apply);
      bPanel.add(modePanel);
      bPanel.add(switchModeButton);

      contentPane.add(bPanel,"South");

      Start.addActionListener(this);
      Stop.addActionListener(this);
      Dismiss.addActionListener(this);
      Apply.addActionListener(this);
      switchModeButton.addActionListener(this);
      bGround.addActionListener(this);
      bPixel.addActionListener(this);

      bGroundOn.addItemListener(this);
      bPixelOn.addItemListener(this);

      pack();
      setResizable(false);
   
      // make sure the QL_instrument task is set to the same mode as the 
      // java interface (it might still be running and could be in any mode)
      // Put this back when we get Michelle running - but need to ensure
      // do *NOT* send commands to CGS4 or IRCAM as a result.
      //      cSent.switchMode(_modes[_currentMode]);

   }


   /**  public void actionPerformed(ActionEvent evt) is a public method
    to react button actions i.e. sending commands out

    @param ActionEvent
    @return  none
    @throws none

  */
  public void actionPerformed (ActionEvent evt)
  {
     Object source =evt.getSource();

     if (source==Start) {
       period = new String(expTime.getValu().getText());

       //add in a error catching here so that the observer cannot attempt
       //to start it without a value being entered.so that the observer can
       //not attempt to start
       //this is a requirement from gsw on 16 March 2000
       // Rewritten to allow double values. AB 6May00
       
       double time = 0.0;

       try {
	 time = (Double.valueOf(period)).doubleValue();
       } catch (NumberFormatException e) {
	 ErrorBox err = new ErrorBox("Error starting movie, illegal exptime: "+e);
	 return;
       }

       if (time<=0) {
	 ErrorBox err = new ErrorBox("Error in starting movie, exp. time= "+time);
	 return;
       }

       cSent.startMovie(period);
       //        Stop.setEnabled(true);
       //        expTime.getValu().setEditable(false);
       //        Dismiss.setEnabled(false);
       //        Start.setEnabled(false);
       return;
     }

     if (source==Stop) {
       cSent.stopMovie();
       //        Stop.setEnabled(false);
       //        Start.setEnabled(true);
       //        Dismiss.setEnabled(true);
       //        expTime.getValu().setEditable(true);
       
       return;
     }

     if (source==Dismiss) {
       this.setVisible(false);
     }

     if (source==Apply) {
       try {
         int tmpCutRow = Integer.parseInt(cutRow.getValu().getText());
	 if(tmpCutRow < 0) {
           cutRow.getValu().setText("0");
	 }
       } catch(NumberFormatException e) {
         cutRow.getValu().setText("0");
       }

       cSent.applyQlook(cutRow.getValu().getText(), _currentBackground, getMode());
     }
     
     if (source==switchModeButton) { 
       if(switchModeButton.getActionCommand().equals(_modes[IMAGING])) {
	 _currentMode = IMAGING;
       } else {
         _currentMode = SPECTROSCOPY;
       }

       cutRow.getValu().setEditable(_currentMode == SPECTROSCOPY);
       cutRow.getValu().setEnabled(_currentMode == SPECTROSCOPY);
       bGround.setEnabled(_currentMode == SPECTROSCOPY);
       bPixel.setEnabled(_currentMode == SPECTROSCOPY);
       bGroundOn.setEnabled(_currentMode == SPECTROSCOPY);
       bPixelOn.setEnabled(_currentMode == SPECTROSCOPY);
       
       cSent.switchMode(_modes[_currentMode]);
       modePanel.getValu().setText(_modes[_currentMode]);
	 
       switchModeButton.setActionCommand(_modes[(_currentMode+1)%2]);
       switchModeButton.setText("Switch to \"" + _modes[(_currentMode+1)%2] + "\"");
     }

     if (source==bGround) {
     
       // Create an instance of the file chooser dialog
       Properties temp = System.getProperties();
      
       String dataBaseDir      = temp.getProperty("DATA_BASE_DIR",     "");
       String dataTypeSubDir   = temp.getProperty("DATA_TYPE_SUB_DIR", "");
      
       String exec_path = dataBaseDir + "/" + UT.getUT() + "/" + dataTypeSubDir;

      if (System.getProperty("DBUG_MESS").equals("ON"))
        System.out.println("EXEC_PATH:"+exec_path);

      myFileChooser fileChooser =  new myFileChooser(exec_path);

      fileChooser.setDialogTitle("Open frame to subtract");
      int returnValue = fileChooser.showOpenDialog(null);

      // User selected OK
      if ( returnValue == 0 ) {

	// Figure out what file the user selected
	_currentBackground = fileChooser.getSelectedFile().getPath();
        bGround.setText(fileChooser.getSelectedFile().getName());
      } else {
        if (System.getProperty("DBUG_MESS").equals("ON"))
	  System.out.println("User cancelled operation" );
      }
      
      fileChooser = null;

      if(bGroundOn.isSelected()) {
        if(_currentMode == IMAGING) {
          cSent.call_Back(_currentBackground);
        }
        else {
          cSent.call_s_Back(_currentBackground);
        }
      }
     }

     if (source==bPixel) {
       // Create an instance of the file chooser dialog
       Properties temp = System.getProperties();

       String exec_path = temp.getProperty("EXEC_PATH");

       if (System.getProperty("DBUG_MESS").equals("ON"))
	 System.out.println("EXEC_PATH:"+exec_path);

       myFileChooser fileChooser =  new myFileChooser(exec_path);

       fileChooser.setDialogTitle("Open bad pixel mask");
       int returnValue = fileChooser.showOpenDialog(null);
		
       // User selected OK
       if ( returnValue == 0 ) {

	 // Figure out what file the user selected
	 _currentBadPixelMask = fileChooser.getSelectedFile().getPath();
	 bPixel.setText(fileChooser.getSelectedFile().getName());
       } else {
	 if(System.getProperty("DBUG_MESS").equals("ON"))
	   System.out.println("User cancelled operation" );
       }
        
       fileChooser = null;

       if (bPixelOn.isSelected()) {
	 if(_currentMode == IMAGING) {
	   cSent.call_Mask(_currentBadPixelMask);
	 } else {
	   cSent.call_s_Mask(_currentBadPixelMask);
	 }
       }
     }
  }

  public void itemStateChanged(ItemEvent evt) {
    Object source  = evt.getSource();

    if (source==bGroundOn) {
      if (bGroundOn.isSelected()) {
        if (_currentMode == IMAGING) {
          cSent.call_Back(_currentBackground);
        } else {
          cSent.call_s_Back(_currentBackground);
        }
      } else {
        if (_currentMode == IMAGING) {
          cSent.call_Back(NO_FILE_SPECIFIED);
        } else {
          cSent.call_s_Back(NO_FILE_SPECIFIED);
        }
	
      }
    }

    if (source==bPixelOn) {
      if (bPixelOn.isSelected()) {
        if (_currentMode == IMAGING) {
          cSent.call_Mask(_currentBadPixelMask);
        } else {
          cSent.call_s_Mask(_currentBadPixelMask);
        }
      } else {
        if (_currentMode == IMAGING) {
          cSent.call_Mask(NO_FILE_SPECIFIED);
        } else {
          cSent.call_s_Mask(NO_FILE_SPECIFIED);
        }
      }
    }
  }

 /** private getFileName  is a private method to
     stripe off the filename extension "exec".

    @param String
    @return none
    @throws String
  */
  private String getFileName (String file)
  {
    int i = 0;
    int j;

    if (file.length()>5)
      for(i=0;i<file.length()-5;i++)
	if (file.substring(i,i+5).equals(".exec"))
	  break;

    for (j=i;j>0;j--)
      if (file.substring(j-1,j).equals("/"))
	break;

    return file.substring(j);
  }

  /** Set screen according to whether movie has been turned on or off.
      @param boolean
      @return none
  */
  public void setMovieOn (boolean on)
  {
    if (on) {
      Stop.setEnabled(true);
      Start.setEnabled(false);
      Dismiss.setEnabled(false);
      expTime.getValu().setEditable(false);
    } else {
      Stop.setEnabled(false);
      Start.setEnabled(true);
      Dismiss.setEnabled(true);
      expTime.getValu().setEditable(true);
    }        

  }

  /** set the exposure time to be used by movies
      @param String
      @return none
  */
  public void setExposureTime(String t)
  {
    expTime.getValu().setText(t);
  }

  
  /**
   * Mode: IMAGING or SPECTROSCOPY.
   * 
   * Is initialised to IMAGING assuming that in dhdriver too "mode" is
   * initially set to "imaging".
   *
   * @see console.movie.IMAGING
   * @see console.movie.SPECTROSCOPY
   */
  private int _currentMode = IMAGING;

  /**
   * Mode strings.
   *
   * These strings are also used in dhdriver.
   * 
   * _modes[IMAGING]      : "imaging"
   * _modes[SPECTROSCOPY] : "spectroscopy"
   *
   * @see console.movie.getMode()
   */
  private String [] _modes = { "imaging", "spectroscopy" };
  
  /**
   * Integer constant representing imaging mode.
   */
  public static final int IMAGING      = 0;

  /**
   * Integer constant representing spectroscopy mode.
   */
  public static final int SPECTROSCOPY = 1;

  /**
   * Returns current mode as String.
   */
  public String getMode() {
    return _modes[_currentMode];
  }

  /**
   * The string "None corresponds to a string used in dhdriver.c"
   */
  public static final String NO_FILE_SPECIFIED = "None";

  private String _currentBackground   = NO_FILE_SPECIFIED;
  private String _currentBadPixelMask = NO_FILE_SPECIFIED;

  private String period;
  private sendCmds cSent;
  private statusPanel expTime,cutRow;
  private JButton Start, Stop, Dismiss,Apply,bPixel, bGround;
  private JCheckBox bGroundOn;
  private JCheckBox bPixelOn;
  private statusPanel modePanel;
  private JButton switchModeButton;
  
  /**
   * Utility for accessing sdf files in a given directory.
   */
  private SdfFiles sdfFiles = new SdfFiles();
}






