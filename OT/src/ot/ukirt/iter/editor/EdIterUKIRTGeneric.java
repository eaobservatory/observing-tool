package ot.ukirt.iter.editor;

import jsky.app.ot.editor.EdIterGenericConfig;

/**
 * @author M.Folger@roe.ac.uk
 */
public class EdIterUKIRTGeneric extends EdIterGenericConfig
{
	/**
	 * Prevents instrument aperture attributes to be displayed in the editor.
	 *
	 * Instrument aperture config items depend only on other config items
	 * (such as readout area) and should not be set by the user directly.
	 */
	protected boolean isUserEditable( String attribute )
	{
		if( attribute.toLowerCase().startsWith( "instaper" ) )
			return false;
		else
			return super.isUserEditable( attribute );
	}
}
