package gemini.util ;

import java.util.Vector ;
import java.util.Enumeration ;
import gemini.sp.SpTranslatable ;
import gemini.sp.SpTranslationNotSupportedException ;
import gemini.sp.SpItem ;

public class TranslationUtils
{
	public static void recurse( Enumeration e , Vector<String> v ) throws SpTranslationNotSupportedException
	{
			if( e == null )
				throw new SpTranslationNotSupportedException() ;
			if( v == null )
				throw new SpTranslationNotSupportedException() ;
			
			SpTranslatable translatable = null ;
			SpTranslatable previous = null ;
			while( e.hasMoreElements() )
			{
				SpItem child = ( SpItem )e.nextElement() ;
				if( child instanceof SpTranslatable )
				{
					translatable = ( SpTranslatable )child ;
					if( !translatable.equals( previous ) )
					{
						if( previous != null )
						{
							previous.translateEpilog( v ) ;
							previous = translatable ;
						}
						translatable.translateProlog( v ) ;
					}
					translatable.translate( v ) ;
				}
			}
			if( translatable != null  )
				translatable.translateEpilog( v ) ;
	}
}
