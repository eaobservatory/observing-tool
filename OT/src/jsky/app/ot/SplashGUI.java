
/**
 * Title:        JSky<p>
 * Description:  <p>
 * Copyright:    Copyright (c) Allan Brighton<p>
 * Company:      <p>
 * @author Allan Brighton
 * @version 1.0
 */
package jsky.app.ot;

import java.awt.*;
import javax.swing.*;
import javax.swing.border.*;
import jsky.app.ot.gui.*;

import java.net.URL;

public class SplashGUI extends JPanel {
    GridBagLayout gridBagLayout1 = new GridBagLayout();
    JLabel splashImage = new JLabel();
    JLabel redistLabel = new JLabel();
    JButton dismissButton = new JButton();
    TitledBorder titledBorder1;
    JScrollPane jScrollPane1 = new JScrollPane();
    RichTextBoxWidgetExt messageRTBW = new RichTextBoxWidgetExt();
    JButton openButton = new JButton();
    JButton newButton = new JButton();

    public SplashGUI() {
        try {
            jbInit();
        }
        catch(Exception ex) {
            ex.printStackTrace();
        }
    }

    void jbInit() throws Exception {
        titledBorder1 = new TitledBorder(new EtchedBorder(EtchedBorder.RAISED,Color.white,new Color(142, 142, 142)),"Observing Tool Release Notes");
        
	String resourceCfgDir = System.getProperty("ot.resource.cfgdir", "ot/cfg/");
        URL url = ClassLoader.getSystemClassLoader().getResource(resourceCfgDir + "images/splash.gif");
	if(url != null) {
          splashImage.setIcon(new ImageIcon(url));
	}
        else {
	  splashImage.setIcon(new ImageIcon(SplashGUI.class.getResource("cfg/splash.gif")));
	}  
        this.setMinimumSize(new Dimension(733, 311));
        this.setPreferredSize(new Dimension(733, 311));
        this.setLayout(gridBagLayout1);
        redistLabel.setForeground(new Color(0, 37, 133));
        redistLabel.setText("Please do not redistribute this software.");
        dismissButton.setText("Dismiss");
        messageRTBW.setBorder(null);
        messageRTBW.setBackground(new Color(204, 204, 204));
        jScrollPane1.setBorder(titledBorder1);
        openButton.setText("Open Existing Program");
        newButton.setText("Create New Program");
        this.add(splashImage, new GridBagConstraints(0, 0, 1, 4, 0.0, 0.0
            ,GridBagConstraints.NORTHWEST, GridBagConstraints.NONE, new Insets(16, 5, 0, 5), 0, 0));
        this.add(redistLabel, new GridBagConstraints(1, 1, 2, 1, 0.0, 0.0
            ,GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
        this.add(dismissButton, new GridBagConstraints(2, 3, 1, 2, 0.0, 0.0
            ,GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(5, 0, 5, 5), 0, 0));
        this.add(jScrollPane1, new GridBagConstraints(1, 0, 2, 1, 1.0, 1.0
            ,GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(10, 5, 5, 7), 0, 0));
        this.add(openButton, new GridBagConstraints(0, 4, 1, 1, 0.0, 0.0
            ,GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(5, 0, 5, 0), 0, 0));
        this.add(newButton, new GridBagConstraints(1, 4, 1, 1, 0.0, 0.0
            ,GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(5, 0, 5, 0), 0, 0));
        jScrollPane1.getViewport().add(messageRTBW, null);
    }
}
