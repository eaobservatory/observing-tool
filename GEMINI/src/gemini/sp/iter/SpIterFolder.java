// Copyright 1997 Association for Universities for Research in Astronomy, Inc.,
// Observatory Control System, Gemini Telescopes Project.
// See the file COPYRIGHT for complete details.
//
// $Id$
//
package gemini.sp.iter;
 
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Enumeration;
import java.util.Vector;

import gemini.sp.SpItem;
import gemini.sp.SpType;
import gemini.sp.SpTranslatable;
import gemini.sp.SpTranslationNotSupportedException;
import gemini.sp.SpTreeMan;
import gemini.sp.obsComp.SpInstObsComp;
import gemini.sp.obsComp.SpInstObsComp.IterationTracker;


/**
 * The Iterator Folder (or "Sequence") item.  The job of the folder is to
 * hold iterators for an Observation Context.
 */
public class SpIterFolder extends SpItem implements SpTranslatable
{
   /**
    * Time needed by the telescope to move to another offset position.
    *
    * 5 seconds.
    */
   private static final double OFFSET_TIME = 5.0;

/**
 * Default constructor.
 */
public SpIterFolder()
{
   super(SpType.SEQUENCE);
}

/**
 * "Compiles" the iterators contained within the folder.  This method
 * steps through all the elements produced by its contained iterators
 * and appends them into a vector.
 *
 * @return A Vector of Vectors of SpIterStep
 *
 * @see SpIterEnumeration
 * @see SpIterStep
 */
public Vector
compile()
{
   Vector code = new Vector();
   
   Enumeration e = children();
   while (e.hasMoreElements()) {
      SpItem   child = (SpItem) e.nextElement();
      if (!(child instanceof SpIterComp)) {
         continue;
      }

      SpIterComp sic = (SpIterComp) child;

       SpIterEnumeration sie = sic.elements();
       while (sie.hasMoreElements()) {
	 //	 SpIterStep sis = (SpIterStep) sie.nextElement();
	 //	 System.out.println (sis.stepCount);
          code.addElement( sie.nextElement() );
       }
      // For now, lets forget the cleanup idea
      //if (sie.hasCleanup()) {
      //   SpIterValue siv = (SpIterValue) sie.cleanup();
      //   code.addElement(siv);
      //}
   }

   return code;
}

/**
 * Debugging method.
 */
public void
printSummary()
{

   Enumeration e = children();
   while (e.hasMoreElements()) {
      SpIterComp sic = (SpIterComp) e.nextElement();

      System.out.println("#########");
      SpIterEnumeration sie = sic.elements();
      while (sie.hasMoreElements()) {
         System.out.println("----------");
         Vector v = (Vector) sie.nextElement();

         for (int i=0; i<v.size(); ++i) {
            SpIterStep sis = (SpIterStep) v.elementAt(i);
            //SpIterValue siv = (SpIterValue) v.elementAt(i);
            System.out.print(sis.title);
            try {
               if (sis.stepCount != 1) {
                  System.out.print(" (" + sis.stepCount + ")");
               }
            } finally {
               System.out.println();
            }
            for (int j=0; j<sis.values.length; ++j) {
               SpIterValue siv = (SpIterValue) sis.values[j];
               System.out.println('\t' + siv.attribute + " = " + siv.values[0]);
            }
         }
      }

      if (sie.hasCleanup()) {
         System.out.println("----------");
         SpIterValue siv = (SpIterValue) sie.cleanup();
         System.out.println(siv.attribute);
         for (int j=0; j<siv.values.length; ++j) {
            System.out.println('\t' + siv.values[j]);
         }
      }

      System.out.println("^^^^^^^^^");
   }
}

	public double getElapsedTime()
	{
		SpInstObsComp instrument = SpTreeMan.findInstrument( this );

		if( instrument == null )
		{
			return 0.0;
		}

		Vector iterStepVector = compile();
		Vector iterStepSubVector = null;
		SpIterStep spIterStep = null;
		IterationTracker iterationTracker = instrument.createIterationTracker();
		double elapsedTime = 0.0;

		int nPol = 0;
		boolean photomSample = false ;
		boolean jiggle = false ;
		for( int i = 0 ; i < iterStepVector.size() ; i++ )
		{
			iterStepSubVector = ( Vector ) iterStepVector.get( i );

			for( int j = 0 ; j < iterStepSubVector.size() ; j++ )
			{
				spIterStep = ( SpIterStep ) iterStepSubVector.get( j );
				if( spIterStep.item.getClass().getName().endsWith( "SpIterPOL" ) )
					nPol++;
				if(  spIterStep.item.getClass().getName().endsWith( "SpIterStareObs" ) )
					photomSample = true ;
				if(  spIterStep.item.getClass().getName().endsWith( "SpIterJiggleObs" ) )
					jiggle = true ;
					
				iterationTracker.update( spIterStep );

				if( spIterStep.item instanceof SpIterObserveBase )
					elapsedTime += iterationTracker.getObserveStepTime();

				if( instrument.getClass().getName().indexOf( "WFCAM" ) == -1 )
				{
					if( spIterStep.item instanceof SpIterOffset )
					{						
						if( ( OFFSET_TIME - instrument.getExposureOverhead() ) > 0.0 )
						{
							// If for each OFFSET_TIME added an exposure overhead can be
							// subtracted since this is done while the telescope moves.
							elapsedTime += ( OFFSET_TIME - instrument.getExposureOverhead() );
						}
					}
				}
			}
		}

		if( photomSample )
		{
			try
			{
				Class spIterStareObsClass = Class.forName( "orac.jcmt.iter.SpIterStareObs" ) ;
				Method getSwitchingMode = spIterStareObsClass.getMethod( "getSwitchingMode" , new Class[]{} ) ;
				Object spIterStareObs = spIterStep.item ;
				Object switchingMode = getSwitchingMode.invoke( spIterStareObs , new Object[]{} ) ;
				if( switchingMode != null )
				{
					Field beamSwitchField = spIterStareObsClass.getField( "SWITCHING_MODE_BEAM" ) ;
					Object beamSwitch = beamSwitchField.get( spIterStareObs ) ;
					if( switchingMode.equals( beamSwitch ) )
						elapsedTime += 30. ;
					else
						elapsedTime += 82. ;
				}
			}
			catch( ClassNotFoundException cnfe )
			{
				System.out.println( "Could not find class " + cnfe ) ;
			}
			catch( IllegalAccessException iae )
			{
				System.out.println( "Could not access " + iae ) ;					
			}
			catch( NoSuchMethodException nsme )
			{
				System.out.println( "Could not find method " + nsme ) ;
			}
			catch( InvocationTargetException ite )
			{
				System.out.println( "Could not invoke method " + ite ) ;
			}
			catch( NoSuchFieldException nsfe )
			{
				System.out.println( "Could not find field " + nsfe ) ;
			}
		}
	
		if( jiggle )
			elapsedTime += 30. ;
		
		if( nPol > 1 )
		{
			if( instrument.getClass().getName().endsWith( "SCUBA" ) )
			{
				// Take off some of the extra overhead
				elapsedTime -= ( nPol - 1 ) * 40.0;
			}
		}

		return elapsedTime;
	}

public void translate(Vector v) throws SpTranslationNotSupportedException {
    Enumeration e = this.children();
    while ( e.hasMoreElements() ) {
        SpItem child = (SpItem)e.nextElement();
        if ( child instanceof SpTranslatable ) {
            ((SpTranslatable)child).translate(v);
        }
    }
}
}
