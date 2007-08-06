package orac.ukirt.iter;

import gemini.sp.SpFactory;
import gemini.sp.SpItem;
import gemini.sp.SpTranslatable;
import gemini.sp.SpTranslationNotSupportedException;
import gemini.sp.SpTreeMan;
import gemini.sp.SpType;

import gemini.sp.iter.IterConfigItem;

import gemini.util.ConfigWriter;

import orac.ukirt.inst.SpInstWFS;

import java.util.Enumeration;
import java.util.Hashtable;
import java.util.List;
import java.util.Vector;

public final class SpIterWFS extends SpIterConfigObsUKIRT implements SpTranslatable
{
	public static final SpType SP_TYPE = SpType.create( SpType.ITERATOR_COMPONENT_TYPE , "iterWFS" , "WFS" );
	static
	{
		SpFactory.registerPrototype( new SpIterWFS() );
	}

	/** Deafault ctor */
	public SpIterWFS()
	{
		super( SP_TYPE );
	}

	/**
	 * Get the array containing the IterConfigItems offered by WFS
	 */
	public IterConfigItem[] getAvailableItems()
	{
		IterConfigItem icilensPos = new IterConfigItem( "LensPos" , SpInstWFS.ATTR_LENS_POS + "Iter" , SpInstWFS.LENS_POS );
		IterConfigItem[] iciA = { icilensPos , getExposureTimeConfigItem() , getCoaddsConfigItem() };

		return iciA;
	}

	/**
	 * Get the name of the item being iterated over.
	 */
	public String getItemName()
	{
		return "WFS";
	}

	public void translate( Vector v ) throws SpTranslationNotSupportedException
	{
		SpInstWFS inst;
		try
		{
			inst = ( SpInstWFS )SpTreeMan.findInstrument( this );
		}
		catch( Exception e )
		{
			throw new SpTranslationNotSupportedException( "No WFS instrument in scope" );
		}

		List iterList = getConfigAttribs();
		int nConfigs = getConfigSteps( ( String )iterList.get( 0 ) ).size();
		for( int i = 0 ; i < nConfigs ; i++ )
		{
			Hashtable configTable = inst.getConfigItems();
			for( int j = 0 ; j < iterList.size() ; j++ )
			{
				if( iterList.contains( "exposureTimeIter" ) )
					configTable.put( "expTime" , ( String )getConfigSteps( "exposureTimeIter" ).get( i ) );

				if( iterList.contains( "coaddsIter" ) )
					configTable.put( "objNumExp" , ( String )getConfigSteps( "coaddsIter" ).get( i ) );

				if( iterList.contains( "lensPosIter" ) )
					configTable.put( "lensPos" , ( String )getConfigSteps( "lensPosIter" ).get( i ) );
			}

			try
			{
				ConfigWriter.getCurrentInstance().write( configTable );
			}
			catch( Exception e )
			{
				throw new SpTranslationNotSupportedException( "Unable to write config file for WFS iterator:" + e.getMessage() );
			}
			v.add( "loadConfig " + ConfigWriter.getCurrentInstance().getCurrentName() );

			Enumeration e = this.children();
			while( e.hasMoreElements() )
			{
				SpItem child = ( SpItem )e.nextElement();
				if( child instanceof SpTranslatable )
					( ( SpTranslatable )child ).translate( v );
			}
		}
	}
}
