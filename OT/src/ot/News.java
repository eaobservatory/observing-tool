// Copyright 1997 Association for Universities for Research in Astronomy, Inc.,
// Observatory Control System, Gemini Telescopes Project.
// See the file COPYRIGHT for complete details.
//
// $Id$
//
//import gemini.gui.PresentationWindow;
//import gemini.gui.WindowManager;

package ot;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JButton;
import javax.swing.JScrollPane;

import java.awt.Color;
import java.awt.Event;
import java.awt.Font;
import java.awt.BorderLayout;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.IOException;

import java.net.URL;
import java.net.MalformedURLException;

import jsky.app.ot.gui.RichTextBoxWidgetExt;
import jsky.app.ot.gui.CommandButtonWidgetExt;

/**
 * This class has been moved to the package ot and modified for use of swing instead of
 * freebongo widget.
 *
 * @author M.Folger (based on the class News in orac2/OT/ot/src, freebongo OT)
 */
public final class News extends JFrame
{
   private static News          _news;
   private RichTextBoxWidgetExt _rt = new RichTextBoxWidgetExt();
   private JButton              _close = new JButton("Close");

public synchronized static void
showNews(URL url)
{
   if (_news == null) {
      _news = new News();

      _news._initTextBox();

      BufferedReader br = null;
      try {
         br = new BufferedReader(new InputStreamReader(url.openStream()));
         _news._readNews(br);
      } catch (IOException ex) {
         _news._warning("Couldn't read the news file!");
         System.out.println("IO EXCEPTION: " + ex);
      } finally {
         try { if (br != null) br.close(); } catch (Exception ex) {}
      }
   }
   _news.show();
   _news.toFront();
}

public synchronized static void
hideNews()
{
   if (_news == null) return;
   _news.hide();
}

private News()
{
   super("Observing Tool Release Notes");
   //setCloseBox(false);

   _rt.setEditable(false);

   _close.addActionListener(new ActionListener() {
     public void actionPerformed(ActionEvent e) {
       hideNews();
     }
   });

   getContentPane().add(BorderLayout.CENTER, new JScrollPane(_rt));

   JPanel bottomPanel = new JPanel();
   bottomPanel.add(_close);
   getContentPane().add(BorderLayout.SOUTH,  bottomPanel);
   

   //WindowManager.centerWin(this);
   setBounds(100, 100, 480, 640);
}

private void
_initTextBox()
{
//   _rt = (RichTextBoxWidgetExt) _pres.getWidget("newsBox");
   _rt.setText("");

   // XXX MFO I don't think rich text can be displayed in a swing JTextArea.
   //_rt.appendStyle("Gemini Observing Tool Release Notes", Font.BOLD + Font.ITALIC, Color.black);
   _rt.append("Gemini Observing Tool Release Notes");
   _rt.append("\n\n");
   _rt.append("This page will be updated frequently as new features are " +
   "incorporated and bugs are fixed.");
   _rt.append("\n\n");
}

private static boolean _firstTime = true;

public void
show()
{
   super.show();

   //WindowManager.centerWin(this);
}

private void
_warning(String warning)
{
   //_rt.appendStyle("WARNING: " + warning, Font.BOLD, Color.red);
   _rt.append("WARNING: ");
}

private void
_readNews(BufferedReader br)
   throws IOException
{
   String line;
   while ((line = br.readLine()) != null) {
      if (line.startsWith("+++")) {
         _rt.append("\n");
         //_rt.appendStyle(line.substring(4), Font.BOLD, Color.black);
         _rt.append(line.substring(4));
      } else {
         line = line.trim();
         if (line.equals("")) {
            _rt.append("\n\n");
         } else {
            _rt.append(line + " ");
         }
      }
   }
}

/*
public boolean
action(Event evt, Object arg)
{
   if (evt.target instanceof CommandButtonWidgetExt) {
      hide();
      return true;
   }
   return false;
}
*/
}

