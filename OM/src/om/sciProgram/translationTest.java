package om.sciProgram;
import java.util.*;
import java.awt.*;
import java.awt.event.*;
import gemini.sp.*;

import javax.swing.*;
import javax.swing.event.*;


/** final class translationTest is just a dummy class to simulate
    the spTranslate class when spTranslate was not avaiable.
    This is no use any more after Sept 1999.

    @version 1.0 1st June 1999
    @author M.Tan@roe.ac.uk
*/
final class translationTest
		extends 	JFrame
		implements  Runnable
 {

	public translationTest(SpItem sp)
	{
		setBackground( Color.cyan);

		topPanel = new JPanel();
		topPanel.setPreferredSize( new Dimension( 310, 130 ) );
		getContentPane().add( topPanel );

		// Create a label and progress bar
		label1 = new JLabel( "Waiting to start..." );
		label1.setPreferredSize( new Dimension( 280, 24 ) );
		topPanel.add( label1 );

		progress = new JProgressBar();
		progress.setPreferredSize( new Dimension( 300, 20 ) );
		progress.setMinimum( 0 );
		progress.setMaximum( 20 );
		progress.setValue( 0 );
		progress.setBounds( 20, 35, 260, 20 );
		topPanel.add( progress );

    if(!this.isVisible())
      show();
	}


	public void run()
	{
			// Perform all of our bogus tasks
			for( int iCtr = 1; iCtr < 21; iCtr++ )
			{
				// Do some sort of simulated task
				DoBogusTask( iCtr );

				// Update the progress indicator and label
				label1.setText( "Performing the task ");
        label1.setForeground(Color.black);
				Rectangle labelRect = label1.getBounds();
				labelRect.x = 0;
				labelRect.y = 0;
				label1.paintImmediately( labelRect );

				progress.setValue( iCtr );
				Rectangle progressRect = progress.getBounds();
				progressRect.x = 0;
				progressRect.y = 0;
				progress.paintImmediately( progressRect );
			}

        this.dispose();
 		}


  public String getFilename () {return filename;}

	private void DoBogusTask( int iCtr )
	{
		Random random = new Random( iCtr );

		// Waste some time
		for( long iValue = 0; iValue < random.nextFloat() * 3; iValue++ )
		{
			long temp=System.currentTimeMillis();

      while(temp>System.currentTimeMillis()-10)
      {
        ;
      }

		}
	}

  private	JProgressBar    progress;
	private	JButton         button;
	private	JLabel          label1;
	private	JPanel          topPanel;
  private String filename=new String("tan.exec");
}

