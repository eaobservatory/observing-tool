/*==============================================================*/
/*                                                              */
/*                UK Astronomy Technology Centre                */
/*                 Royal Observatory, Edinburgh                 */
/*                 Joint Astronomy Centre, Hilo                 */
/*                   Copyright (c) PPARC 2001                   */
/*                                                              */
/*==============================================================*/
// $Id$

package ot;

import java.awt.Font;
import javax.swing.JTextArea;

/**
 * Frame for displaying formatted ASCII text.
 *
 * Text is displayed in a monospaced font in order to
 * keep the layout (tables etc.) of the ASCII text.
 *
 * @author Martin Folger (M.Folger@roe.ac.uk)
 */
public class FormattedStringBox extends ReportBox {

  /**
   * JTextArea is used instead of JTextPane of the super class.
   *
   * The reason is that the pack() method does not always use the space required
   * by the displayed text to adjust the the overall size of the the frame.
   * JTextArea works better with pack(). So _textPane of the super class is
   * removed from the frame and _textArea is added instead.
   */
  protected JTextArea _textArea = new JTextArea();

  protected FormattedStringBox() {
  
    super();

    _textArea.setFont(new Font("Monospaced", 0, 10));

    getContentPane().remove(_textPane);
    getContentPane().add(_textArea);
  }

  public FormattedStringBox(String message) {
    this();

    _textArea.setText(message);

    // Set _textPane text as well for saving
    // as this is used for saving and printing.
    _textPane.setText(message);


    setLocation(100, 300);
    pack();
    setVisible(true);
  }

  public FormattedStringBox(String message, String title) {
    this(message);
    
    setTitle(title);
  }


}

