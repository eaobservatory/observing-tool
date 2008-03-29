package gemini.sp;

import java.util.Vector;

public interface SpTranslatable
{

	/**
     * Classes that implement this method must provide a translate method. Used
     * to convert XML into sequences
     */
	public void translate( Vector sequence ) throws SpTranslationNotSupportedException ;
	
	/**
	 * Called prior to main translation for iterators etc.
	 * This is useful for not repeating setup mid-observation.
	 * Although, due to SpTranslatable being an interface it needs to be implemented, 
	 * it is expected that these methods will remain empty in normal translation.
	 * @param sequence
	 * @throws SpTranslationNotSupportedException
	 */
	public void translateProlog( Vector sequence ) throws SpTranslationNotSupportedException ;
	
	/**
	 * Called after main translation for iterators etc.
	 * This is useful for not repeating tear-down mid-observation.
	 * Although, due to SpTranslatable being an interface it needs to be implemented, 
	 * it is expected that these methods will remain empty in normal translation. 
	 * @param sequence
	 * @throws SpTranslationNotSupportedException
	 */
	public void translateEpilog( Vector sequence ) throws SpTranslationNotSupportedException ;

}
