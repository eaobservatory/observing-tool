package orac.validation ;

import java.io.IOException ;
import java.io.StringReader ;

import java.util.Calendar ;
import java.util.Enumeration ;
import java.util.HashMap ;
import java.util.Vector ;

import gemini.sp.SpProg ;
import gemini.sp.SpObs ;
import gemini.sp.SpMSB ;
import gemini.sp.SpItem ;
import gemini.sp.SpNote ;
import gemini.sp.SpSurveyContainer ;
import gemini.sp.SpTelescopePosList ;
import gemini.sp.SpTelescopePos ;
import gemini.sp.SpTreeMan ;
import gemini.sp.obsComp.SpTelescopeObsComp ;
import gemini.sp.obsComp.SpSiteQualityObsComp ;
import gemini.sp.obsComp.SpSchedConstObsComp ;
import gemini.util.CoordSys ;
import gemini.util.DDMMSS ;
import gemini.util.HHMMSS ;
import gemini.util.RADec ;
import gemini.util.TelescopePos ;

import orac.util.CoordConvert;
import orac.util.SpItemUtilities ;

import org.apache.xerces.parsers.SAXParser ;
import org.apache.xerces.parsers.DOMParser ;
import org.w3c.dom.Document ;
import org.w3c.dom.Element ;
import org.w3c.dom.NodeList ;
import org.w3c.dom.Node ;
import org.xml.sax.SAXException ;
import org.xml.sax.SAXParseException ;
import org.xml.sax.SAXNotRecognizedException ;
import org.xml.sax.SAXNotSupportedException ;
import org.xml.sax.InputSource ;
import org.xml.sax.ErrorHandler ;
import org.xml.sax.ContentHandler ;
import org.xml.sax.Attributes ;
import org.xml.sax.Locator ;

import orac.util.OracUtilities ;
import orac.util.TimeUtils ;
import java.util.Date ;

/**
 * Validation Tool.
 * 
 * This class is used for checking whether the values and settings in a Science Program or
 * Observation are sensible.
 *
 * Errors and warnings are issued otherwise.
 *
 * @author M.Folger@roe.ac.uk UKATC
 */
public class SpValidation
{
	protected static String separator = "------------------------------------------------------\n" ;
	
	public void checkSciProgram( SpProg spProg , Vector<ErrorMessage> report )
	{
		if( report == null )
			report = new Vector<ErrorMessage>() ;

		// Change the SpProg to a document, since this is more easily checked
		String xmlStr = spProg.toXML() ;
		DOMParser parser = new DOMParser() ;
		Document doc = null ;
		try
		{
			// Turn off validation
			parser.setFeature( "http://xml.org/sax/features/validation" , false ) ;
			parser.parse( new InputSource( new StringReader( xmlStr ) ) ) ;
			doc = parser.getDocument() ;
		}
		catch( IOException e )
		{
			System.out.println( "Unable to read string" ) ;
		}
		catch( SAXException e )
		{
			System.out.println( "Unable to create document: " + e.getMessage() ) ;
		}

		checkSciProgramRecursively( spProg , report ) ;
		checkSurveyContainer( spProg , report ) ;

		if( doc != null )
		{
			// I use one method for each 'rule'
			// Rule 1: Each SpObs and MSB must either contain the following
			// or inherit it from higher up:
			// SpInst...
			// SpSiteQualityObsComp
			// SptelescopeObsComp
			checkInheritedComponents( doc , "SpMSB" , "SpSiteQualityObsComp" , "Site Quality" , report ) ;
			checkInheritedComponents( doc , "SpMSB" , "SpInst" , "Instrument" , report ) ;
			checkInheritedComponents( doc , "SpObs" , "SpSiteQualityObsComp" , "Site Quality" , report ) ;
			checkInheritedComponents( doc , "SpObs" , "SpInst" , "Instrument" , report ) ;

			// Rule2: Each iterator must contain either another iterator or an observe eye
			// Exception: For UKIRT, the offset iterator does not have this requirement
			checkIterator( doc , "SpIterFolder" , "Sequence Iterator" , report ) ;
			checkIterator( doc , "SpIterChop" , "Chop Iterator" , report ) ;
			checkIterator( doc , "SpIterPOL" , "POL Iterator" , report ) ;
			checkIterator( doc , "SpIterRepeat" , "Repeat Iterator" , report ) ;
			if( System.getProperty( "ot.cfgdir" ).indexOf( "ukirt" ) == -1 )
				checkIterator( doc , "SpIterOffset" , "Offset Iterator" , report ) ;
			checkIterator( doc , "SpIterFrequency" , "Frequency Iterator" , report ) ;

			// Other rules:
			// SpAND must contain an SpObs or SpMSB but NOT and SpOR
			// SpOR must contain and SpObs, SpMSB or SpOR
			// SpMSB must contain an SpObs
			// SpObs must contain only 1 sequence
			Vector<String> mandatoryChildren = new Vector<String>() ;
			Vector<String> excludedChildren = new Vector<String>() ;
			mandatoryChildren.add( "SpObs" ) ;
			mandatoryChildren.add( "SpMSB" ) ;
			excludedChildren.add( "SpOR" ) ;
			excludedChildren.add( "SpAND" ) ;
			checkForChildren( doc , "SpAND" , mandatoryChildren , excludedChildren , report ) ;
			mandatoryChildren.add( "SpAND" ) ;
			excludedChildren.clear() ;
			excludedChildren.add( "SpOR" ) ;
			checkForChildren( doc , "SpOR" , mandatoryChildren , excludedChildren , report ) ;
		}
	}

	protected void checkSciProgramRecursively( SpItem spItem , Vector<ErrorMessage> report )
	{
		Enumeration<SpItem> children = spItem.children() ;
		SpItem child ;

		while( children.hasMoreElements() )
		{
			child = children.nextElement() ;

			if( child instanceof SpMSB )
				checkMSB( ( SpMSB )child , report ) ;
			else if( child instanceof SpSchedConstObsComp )
				checkSchedComp( ( SpSchedConstObsComp )child , report ) ;
			else
				checkSciProgramRecursively( child , report ) ;
		}
	}

	public void checkMSB( SpMSB spMSB , Vector<ErrorMessage> report )
	{
		checkMSBgeneric( spMSB , report ) ;
	}

	protected void checkMSBgeneric( SpMSB spMSB , Vector<ErrorMessage> report )
	{
		/*MFO DEBUG*/
		if( report == null )
			report = new Vector<ErrorMessage>() ;

		if( SpItemUtilities.findSiteQuality( spMSB ) == null )
			report.add( new ErrorMessage( ErrorMessage.ERROR , "MSB \"" + spMSB.getTitle() + "\"" , "Site quality component list is missing." ) ) ;

		// Check whether there are more than one observe instruction notes.
		Vector<SpItem> notes = SpTreeMan.findAllItems( spMSB , SpNote.class.getName() ) ;

		boolean observeNoteFound = false ;
		int numberOfObserveInstructions = 0 ;

		// How may notes are inside the MSB and are any of then observer instructions
		if( notes.size() > 0 )
		{
			for( int i = 0 ; i < notes.size() ; i++ )
			{
				if( ( ( SpNote )notes.get( i ) ).isObserveInstruction() )
				{
					numberOfObserveInstructions++ ;
					observeNoteFound = true ;
				}
			}
		}
		// If we havent found an observer note, go through the parents and check if there is one there
		if( !observeNoteFound )
		{
			SpItem parent = spMSB.parent() ;
			while( parent != null )
			{
				SpNote note = SpTreeMan.findObserverNoteInContext( parent ) ;
				if( note != null )
				{
					observeNoteFound = true ;
					break ;
				}
				parent = parent.parent() ;
			}
		}

		if( !observeNoteFound )
			report.add( new ErrorMessage( ErrorMessage.WARNING , "MSB \"" + spMSB.getTitle() + "\" does not have an Show-to Observer Note" , "Recommend MSBs have an observe instruction" ) ) ;

		// Add a check for MSB's > 2 hours long
		if( spMSB.getElapsedTime() > 2.0 * 3600 )
			report.add( new ErrorMessage( ErrorMessage.WARNING , "MSB \"" + spMSB.getTitle() + "\" is more than 2 hours long" , "MSBs should ideally be < 1.5 hours long" ) ) ;

		// Following check added - if the MSB parent is a survey container, and
		// the MSB has a TargetComponent as a child, this is an error
		if( spMSB.parent() instanceof SpSurveyContainer && !( spMSB instanceof SpObs ) )
		{
			Enumeration<SpItem> children = spMSB.children() ;
			while( children.hasMoreElements() )
			{
				SpItem child = children.nextElement() ;
				if( child instanceof SpTelescopeObsComp )
				{
					report.add( new ErrorMessage( ErrorMessage.ERROR , "An MSB within a Survey Container may not contain a Target Component" , "SC/MSB/Target not allowed" ) ) ;
					break ;
				}
			}
		}

		if( spMSB instanceof SpObs )
		{
			checkObservation( ( SpObs )spMSB , report ) ;
		}
		else
		{
			Enumeration<SpItem> children = spMSB.children() ;
			SpItem child ;

			while( children.hasMoreElements() )
			{
				child = children.nextElement() ;

				if( child instanceof SpObs )
					checkObservation( ( SpObs )child , report ) ;
			}
		}
	}

	public static String titleString( SpItem spItem )
	{
		String returnString = "" ;
		SpItem parent = spItem ;
		while( parent != null )
		{
			if( parent instanceof SpObs )
			{
				returnString += "\"" + (( SpObs )parent).getTitle() + "\"" ;
			}
			else if( parent instanceof SpMSB )
			{
				returnString += " in MSB \'" + (( SpMSB )parent).getTitleAttr() + "\'" ;
				break ;
			}
			parent = parent.parent() ;
		}
		return returnString ;
	}
	
	public void checkObservation( SpObs spObs , Vector<ErrorMessage> report )
	{
		if( report == null )
			report = new Vector<ErrorMessage>() ;
		
		String titleString = titleString( spObs ) ;

		Enumeration<SpItem> children ;
		SpItem child ;

		if( SpTreeMan.findInstrument( spObs ) == null )
			report.add( new ErrorMessage( ErrorMessage.ERROR , "Observation " + titleString  , "Instrument component is missing." ) ) ;

		if( !spObs.isOptional() && SpTreeMan.findSurveyContainerInContext( spObs ) == null )
			checkTargetList( SpTreeMan.findTargetList( spObs ) , report ) ;

		if( !spObs.isMSB() )
		{
			children = spObs.children() ;

			while( children.hasMoreElements() )
			{
				child = children.nextElement() ;
				if( child instanceof SpSiteQualityObsComp )
					report.add( new ErrorMessage( ErrorMessage.ERROR , "Observation " + titleString , "Observations inside MSBs must use the site quality component of the parent MSB." ) ) ;

				if( child instanceof SpSchedConstObsComp )
					report.add( new ErrorMessage( ErrorMessage.ERROR , "Observation " + titleString , "Observations inside MSBs must use the scheduling constraints component of the parent MSB." ) ) ;
			}
		}
	}

	private void checkInheritedComponents( Document doc , String folderName , String componentName , String description , Vector<ErrorMessage> report )
	{
		String title = null ;
		// Check all the children of the top level element
		Element root = doc.getDocumentElement() ;
		// Each MSB and SpObs must contain an SpTelescopeObsComp, or inherit it from a parent.
		// Check each one for this
		NodeList nl = root.getElementsByTagName( folderName ) ;
		boolean found = false ;
		for( int i = 0 ; i < nl.getLength() ; i++ )
		{
			Node thisNode = nl.item( i ) ;
			// For each MSB check if we have the required info.
			NodeList requiredTag = ( ( Element )thisNode ).getElementsByTagName( "*" ) ;
			title = null ;
			if( requiredTag != null )
			{
				for( int j = 0 ; j < requiredTag.getLength() ; j++ )
				{
					Node currentNode = requiredTag.item( j ) ;
					if( currentNode != null )
					{
						String nodeName = currentNode.getNodeName() ;
						if( nodeName == null )
							nodeName = "" ;
						if( nodeName.equalsIgnoreCase( "title" ) )
						{
							// if we have reached here requiredTag.item( j ) != null
							Node tempNode = currentNode.getFirstChild() ;
							if( tempNode != null )
								title = tempNode.getNodeValue() ;
						}
						// I am sure this should be `else if` and not `if` ...
						else if( !found && nodeName.startsWith( componentName ) )
						{
							found = true ;
						}
					}
				}
			}
			if( !found )
			{
				// Check whether any of the proginy contain an autoTarget flag.  
				// In this case, we have a calibration, so we are OK
				NodeList autoTargets = ( ( Element )thisNode ).getElementsByTagName( "autoTarget" ) ;
				if( autoTargets != null && autoTargets.getLength() > 0 )
					found = true ;
			}
			if( !found )
			{
				// We don't so recuse up the document tree to see if any parents have it
				while( ( thisNode = thisNode.getParentNode() ) != null && !found )
				{
					NodeList childList = thisNode.getChildNodes() ;
					for( int child = 0 ; child < childList.getLength() ; child++ )
					{
						if( childList.item( child ).getNodeName().startsWith( componentName ) )
						{
							found = true ;
							break ;
						}
					}
				}
			}

			if( found )
			{
				// Check the next one
				found = false ;
				continue ;
			}
			else
			{
				if( title != null )
					report.add( new ErrorMessage( ErrorMessage.ERROR , "MSB or Observation" , "MSB/Observation \"" + title + "\" does not contain or inherit a " + description ) ) ;
				else
					report.add( new ErrorMessage( ErrorMessage.ERROR , "MSB or Observation" , "Unnamed MSB/Observation does not contain or inherit a " + description ) ) ;

				break ;
			}
		}
	}

	private void checkIterator( Document doc , String iteratorType , String description , Vector<ErrorMessage> report )
	{
		boolean found = false ;
		// Get the root element
		Element root = doc.getDocumentElement() ;
		// Get all the nodes which we are going to check
		NodeList nl = root.getElementsByTagName( iteratorType ) ;
		// For each of these, get all of its children
		for( int i = 0 ; i < nl.getLength() ; i++ )
		{
			NodeList children = nl.item( i ).getChildNodes() ;
			// Check each child looking for another SpIter...
			for( int j = 0 ; j < children.getLength() ; j++ )
			{
				if( children.item( j ).getNodeName().startsWith( "SpIter" ) )
				{
					found = true ;
					break ;
				}
			}
			if( found )
			{
				found = false ;
				continue ;
			}
			else
			{
				report.add( new ErrorMessage( ErrorMessage.ERROR , description , "A " + description + " does not contain another iterator or observe" ) ) ;
				break ;
			}
		}
	}

	private void checkForChildren
	( 
			Document doc , 
			String component , // Tag name of child to check
			Vector<String> mandatory , // The tag must have at least one of these types
			Vector<String> excluded , // List of children that are prohibited from appearing in the tag
			Vector<ErrorMessage> report // Errors returned through this
	)
	{
		boolean mandatoryElementFound = false ;
		boolean excludedElementFound = false ;

		Element root = doc.getDocumentElement() ;
		// Het all of the required components
		NodeList nl = root.getElementsByTagName( component ) ;
		// For each of these, make sure we have at least one mandatory tag as a direct child, and that the exclusion does not appear
		for( int i = 0 ; i < nl.getLength() ; i++ )
		{
			NodeList children = nl.item( i ).getChildNodes() ;
			for( int j = 0 ; j < children.getLength() ; j++ )
			{
				// Check if this is a madatory tag
				if( mandatory != null )
				{
					for( int tag = 0 ; tag < mandatory.size() ; tag++ )
					{
						if( children.item( j ).getNodeName().equals( mandatory.elementAt( tag ) ) )
						{
							mandatoryElementFound = true ;
							break ;
						}
					}
				}
				if( excluded != null )
				{
					for( int tag = 0 ; tag < excluded.size() ; tag++ )
					{
						if( children.item( j ).getNodeName().equals( excluded.elementAt( tag ) ) )
							excludedElementFound = true ;
					}
				}
			}
			if( mandatoryElementFound && !excludedElementFound )
			{
				// This is OK, so reset and go to the next in the list
				mandatoryElementFound = false ;
				excludedElementFound = false ;
				continue ;
			}
			else
			{
				// Something is wrong
				if( !mandatoryElementFound )
				{
					String message = component.substring( 2 ) + " does not contain at least one of { " ;
					for( int tag = 0 ; tag < mandatory.size() ; tag++ )
						message = message + mandatory.elementAt( tag ).substring( 2 ) + " " ;

					message = message + "}" ;
					report.add( new ErrorMessage( ErrorMessage.ERROR , component , message ) ) ;
					break ;
				}
				else
				{
					String message = component.substring( 2 ) + " contains one of { " ;
					for( int tag = 0 ; tag < excluded.size() ; tag++ )
						message = message + excluded.elementAt( tag ).substring( 2 ) + " " ;

					message = message + "} which is not allowed" ;
					report.add( new ErrorMessage( ErrorMessage.ERROR , component , message ) ) ;
					break ;
				}
			}
		}
	}

	protected void checkTargetList( SpTelescopeObsComp obsComp , Vector<ErrorMessage> report )
	{
		if( obsComp != null )
		{
			String titleString = titleString( obsComp ) ;
			SpTelescopePosList list = obsComp.getPosList() ;
			TelescopePos[] position = list.getAllPositions() ;

			for( int i = 0 ; i < position.length ; i++ )
			{
				SpTelescopePos pos = ( SpTelescopePos )position[ i ] ;
				if( ( pos.getSystemType() == SpTelescopePos.SYSTEM_SPHERICAL ) && pos.isBasePosition() )
				{
					String itemString =  "Telescope target " + pos.getName() + " in " + titleString ;
					
					double Xaxis = pos.getXaxis() ;
					double Yaxis = pos.getYaxis() ;
					
					if( Xaxis == 0 && Yaxis == 0 )
						report.add( new ErrorMessage( ErrorMessage.WARNING , itemString , "Both Dec and RA are 0:00:00" ) ) ;
					
					int coordSystem = pos.getCoordSys() ;
					
					if( coordSystem == CoordSys.FK4 )
					{
						RADec raDec = CoordConvert.Fk45z( Xaxis , Yaxis ) ;
						Xaxis = raDec.ra ;
						Yaxis = raDec.dec ;
					}
					else if( coordSystem == CoordSys.GAL )
					{
						RADec raDec = CoordConvert.gal2fk5( Xaxis , Yaxis ) ;
						Xaxis = raDec.ra ;
						Yaxis = raDec.dec ;
					}
					
					String hhmmss = HHMMSS.valStr( Xaxis ) ;
					String ddmmss = DDMMSS.valStr( Yaxis ) ;	
					
					if( !HHMMSS.validate( hhmmss ) )
						report.add( new ErrorMessage( ErrorMessage.WARNING , itemString , "RA not valid" ) ) ;
					if( !DDMMSS.validate( ddmmss ) )
						report.add( new ErrorMessage( ErrorMessage.WARNING , itemString , "Dec not valid" ) ) ;
				}
			}
		}
		else
		{
			report.add( new ErrorMessage( ErrorMessage.ERROR , "Observation has no target component" , "" ) ) ;
		}
	}

	private void checkSurveyContainer( SpProg prog , Vector<ErrorMessage> report )
	{
		Vector<SpItem> containers = SpTreeMan.findAllInstances( prog , SpSurveyContainer.class.getName() ) ;
		for( int count = 0 ; count < containers.size() ; count++ )
		{
			SpSurveyContainer container = ( SpSurveyContainer )containers.get( count ) ;
			for( int i = 0 ; i < container.size() ; i++ )
				checkTargetList( container.getSpTelescopeObsComp( i ) , report ) ;
		}
	}

	/**
	 * Perform validation against a schema.  The SpItem class already writes out
	 * a part of the default namesapce in its toXML method.  In this class we
	 * apply the actual location of the schema.  A SAX parser is used to parse
	 * the string.
	 * @param xmlString  The current XML representation of the science project
	 * @param schema     Fully qualified file name for the schema.
	 * @return           A vector of strings containing the validation errors
	 */
	public void schemaValidate( String xmlString , String schemaURL , String schema , Vector<ErrorMessage> report )
	{
		// Make sure the schema exists
		if( schema == null )
		{
			report.add( new ErrorMessage( ErrorMessage.WARNING , "Schema Validation not performed" , "Unable to locate both web service and local version" ) ) ;
			return ;
		}
		// Make sure we can get an http connection to the schema.  If not, default to using the provided one
		String schemaLoc ;
		try
		{
			schemaLoc = schemaURL + " " + schema ;
		}
		catch( Exception e )
		{
			// Any exception, assume we cant access the URL, so try to use a file based solution
			StringBuffer cfgDir = new StringBuffer( System.getProperty( "ot.cfgdir" ) ) ;
			// First make sure the last character is a path separator, then
			// We need to go up one level and then enter the schema directory
			char lastChar = cfgDir.charAt( cfgDir.length() - 1 ) ;
			if( !( lastChar == '/' ) )
				cfgDir.append( '/' ) ;

			cfgDir.append( ".." + '/' + "schema" + '/' + "JAC" + '/' ) ;
			// Now work out which telescope we are using
			if( cfgDir.indexOf( "ukirt" ) != -1 )
			{
				// We are using ukirt
				cfgDir.append( "UKIRT" + '/' ) ;
				cfgDir.append( "UKIRT.xsd" ) ;
			}
			else if( cfgDir.indexOf( "jcmt" ) != -1 )
			{
				// we are using jcmt
				cfgDir.append( "JCMT" + '/' ) ;
				cfgDir.append( "JCMT.xsd" ) ;
			}
			else
			{
				// no idea - just return
				report.add( new ErrorMessage( ErrorMessage.WARNING , "Schema Validation not performed" , "Unable to work out which telescope is being used" ) ) ;
				return ;
			}
			// Convert to file URL
			report.add( new ErrorMessage( ErrorMessage.WARNING , "Using local schema version" , "This may give some invalid error messages, please reverify before submission" ) ) ;
			cfgDir.insert( 0 , "file:" ) ;
			schemaLoc = schemaURL + " " + cfgDir.toString() ;
		}

		SAXParser parser = new SAXParser() ;
		SchemaErrorHandler handler = new SchemaErrorHandler( xmlString ) ;
		SchemaContentHandler contentHandler = new SchemaContentHandler() ;
		parser.setErrorHandler( handler ) ;
		parser.setContentHandler( contentHandler ) ;
		try
		{
			parser.setFeature( "http://xml.org/sax/features/validation" , true ) ;
			parser.setFeature( "http://apache.org/xml/features/validation/schema" , true ) ;
			parser.setFeature( "http://apache.org/xml/features/validation/schema-full-checking" , true ) ;
			parser.setProperty( "http://apache.org/xml/properties/schema/external-schemaLocation" , schemaLoc ) ;
			parser.parse( new InputSource( new StringReader( xmlString ) ) ) ;
		}
		catch( SAXNotRecognizedException e )
		{
			System.out.println( "Unrecognized feature" ) ;
		}
		catch( SAXNotSupportedException e )
		{
			System.out.println( "Unsupported feature" ) ;
		}
		catch( IOException e )
		{
			System.out.println( "Unable to read string" ) ;
		}
		catch( SAXException e )
		{
			System.out.println( "Unable to create document: " + e.getMessage() ) ;
		}

		Vector<String> schemaErrors = handler.getErrors() ;
		for( int i = 0 ; i < schemaErrors.size() ; i++ )
			report.add( new ErrorMessage( ErrorMessage.ERROR , "Schema validation error" , schemaErrors.get( i ) ) ) ;
	}

	public class SchemaErrorHandler implements ErrorHandler
	{
		public Vector<String> errorMessages = new Vector<String>() ;
		
		private String[] xml = null ;

		public SchemaErrorHandler( String xmlString )
		{
			xml = xmlString.split( "\n" ) ;
		}
		
		public void error( SAXParseException e )
		{
			int lineNumber = e.getLineNumber() - 1 ;
			
			String errorMessage = separator ;
			errorMessage += "Validation error in MSB<" + SchemaContentHandler.getCurrentMSB() + ">\n" ;
			errorMessage += "XML line number : " + ( lineNumber + 1 ) + " column " + e.getColumnNumber() + "\n" ;
			
			errorMessage += "\n" ;
			errorMessage += xml[ lineNumber - 1 ] + "\n" ;
			errorMessage += "--> " + xml[ lineNumber ] + "  <--\n" ;
			if( lineNumber + 1 < xml.length )
				errorMessage += xml[ lineNumber + 1 ] + "\n" ;
			errorMessage += "\n" ;
			
			errorMessage += "Obs:<" + SchemaContentHandler.getCurrentObs() + "> \n" ;
			errorMessage += "component<" + SchemaContentHandler.getCurrentClass() + ">: \n" ;
			errorMessage += "message:<" + e.getMessage() + ">\n" ;
			errorMessages.add( errorMessage ) ;
		}

		public void fatalError( SAXParseException e )
		{
			String errorMessage = "Fatal parse error in <" + e.getSystemId() + ">: " + e.getMessage() + "\n" ;
			errorMessages.add( errorMessage ) ;
		}

		public void warning( SAXParseException e )
		{
			String errorMessage = "Validation warning in <" + e.getSystemId() + ">: " + e.getMessage() + "\n" ;
			errorMessages.add( errorMessage ) ;
		}

		public Vector<String> getErrors()
		{
			return errorMessages ;
		}
	}

	public static class SchemaContentHandler implements ContentHandler
	{
		public static String currentClass ;
		public ClassLoader loader = new ComponentLoader() ;
		private HashMap<String,String> classPathMap = new HashMap<String,String>() ;
		public static StringBuffer currentMSB = null ;
		public static StringBuffer currentObs = null ;
		boolean readingMSB = false ;
		boolean readingObs = false ;
		boolean getContents = false ;

		public SchemaContentHandler()
		{
			initClasspaths() ;
		}

		private void initClasspaths()
		{
			classPathMap.put( "SpProg" , "gemin.sp." ) ;

			classPathMap.put( "SpTelescopeObsComp" , "gemini.sp.obsComp." ) ;
			classPathMap.put( "SpSchedConstObsComp" , "gemini.sp.obsComp." ) ;
			classPathMap.put( "SpSiteQualityObsComp" , "gemini.sp.obsComp." ) ;

			classPathMap.put( "SpAnd" , "gemini.sp." ) ;
			classPathMap.put( "SpOR" , "gemini.sp." ) ;
			classPathMap.put( "SpMSB" , "gemini.sp." ) ;
			classPathMap.put( "SpObs" , "gemini.sp." ) ;
			classPathMap.put( "SpNote" , "gemini.sp." ) ;

			classPathMap.put( "SpIterFolder" , "gemini.sp.iter." ) ;
			classPathMap.put( "SpIterRepeat" , "gemini.sp.iter." ) ;
			classPathMap.put( "SpIterPOL" , "orac.jcmt.iter." ) ;
			classPathMap.put( "SpIterOffset" , "gemini.sp.iter." ) ;
			classPathMap.put( "SpIterFrequency" , "orac.jcmt.iter." ) ;
			classPathMap.put( "SpIterChop" , "gemini.sp.iter." ) ;

			classPathMap.put( "SpIterFocusObs" , "orac.jcmt.iter." ) ;
			classPathMap.put( "SpIterJiggleObs" , "orac.jcmt.iter." ) ;
			classPathMap.put( "SpIterNoiseObs" , "orac.jcmt.iter." ) ;
			classPathMap.put( "SpIterStareObs" , "orac.jcmt.iter." ) ;
			classPathMap.put( "SpIterPointingObs" , "orac.jcmt.iter." ) ;
			classPathMap.put( "SpIterRasterObs" , "orac.jcmt.iter." ) ;
			classPathMap.put( "SpIterSkydipObs" , "orac.jcmt.iter." ) ;
		}

		public void startElement( String namespace , String localName , String qName , Attributes attr )
		{
			if( localName.equals( "SpMSB" ) )
			{
				currentMSB = null ;
				currentObs = null ;
				readingMSB = true ;
			}
			else if( localName.equals( "SpObs" ) )
			{
				currentObs = null ;
				readingObs = true ;
			}
			if( localName.startsWith( "Sp" ) )
			{
				currentClass = localName ;
				try
				{
					SpItem myClass = ( SpItem )loader.loadClass( classPathMap.get( localName ) + localName ).newInstance() ;
					String readableName = myClass.type().getReadable() ;
					currentClass = readableName ;
				}
				catch( ClassNotFoundException e ){}
				catch( InstantiationException e ){}
				catch( IllegalAccessException e ){}
			}
			else if( localName.equals( "title" ) && ( readingMSB || readingObs ) )
			{
				getContents = true ;
			}
		}

		public void characters( char[] ch , int start , int length )
		{
			if( getContents )
			{
				if( readingMSB )
				{
					if( currentMSB == null )
						currentMSB = new StringBuffer() ;
					
					currentMSB.append( ch , start , length ) ;
				}
				else if( readingObs )
				{
					if( currentObs == null )
						currentObs = new StringBuffer() ;

					currentObs.append( ch , start , length ) ;
				}
			}
		}

		public void endDocument(){}

		public void endElement( String namespace , String localName , String qName )
		{
			if( getContents && localName.equals( "title" ) )
			{
				getContents = false ;
				if( readingMSB )
					readingMSB = false ;
				if( readingObs )
					readingObs = false ;
			}
		}

		public void endPrefixMapping( String prefix ){}

		public void ignorableWhitespace( char[] ch , int start , int length ){}

		public void processingInstruction( String target , String data ){}

		public void setDocumentLocator( Locator l ){}

		public void skippedEntity( String name ){}

		public void startDocument(){}

		public void startPrefixMapping( String prefix , String uri ){}

		public static String getCurrentClass()
		{
			return currentClass ;
		}

		public static String getCurrentMSB()
		{
			if( currentMSB != null )
				return currentMSB.toString() ;

			return "" ;
		}

		public static String getCurrentObs()
		{
			if( currentObs != null )
				return currentObs.toString() ;

			return "" ;
		}
	}

	public static class ComponentLoader extends ClassLoader{}

	public void checkSchedComp( SpSchedConstObsComp schedule , Vector<ErrorMessage> report )
	{
		checkStartEndTimes( schedule , report ) ;
		maxGreaterThanMin( schedule , report ) ;
	}
	
	public void maxGreaterThanMin( SpSchedConstObsComp schedule , Vector<ErrorMessage> report )
	{
		String min = schedule.getMinElevation() ;
		String max = schedule.getMaxElevation() ;
		try
		{
			double minEl = new Double( min ) ;
			double maxEl = new Double( max ) ;
			if( minEl > maxEl )
				report.add( new ErrorMessage( ErrorMessage.ERROR , "Min elevation in schedule constraint is greater than max elevation" , "" ) ) ;
		}
		catch( NumberFormatException nfe )
		{
			report.add( new ErrorMessage( ErrorMessage.ERROR , "Values for elevation in schedule constraint not numeric." , "" ) ) ;
		}
		catch( NullPointerException npe )
		{
			report.add( new ErrorMessage( ErrorMessage.ERROR , "No values for elevation in schedule constraint." , "" ) ) ;			
		}
	}
	
	public void checkStartEndTimes( SpSchedConstObsComp schedule , Vector<ErrorMessage> report )
	{
		String earliest = schedule.getEarliest() ;
		String latest = schedule.getLatest() ;
		Date before = OracUtilities.parseISO8601( earliest ) ;
		Date after = OracUtilities.parseISO8601( latest ) ;
		if( before.after( after ) )
			report.add( new ErrorMessage( ErrorMessage.WARNING , "The earliest scheduled start time is after the latest scheduled end time" , "" ) ) ;

		Calendar latestCalendar = Calendar.getInstance() ;
		latestCalendar.set( Calendar.YEAR , 2035 ) ;
		latestCalendar.set( Calendar.MONTH , Calendar.JANUARY ) ;
		latestCalendar.set( Calendar.DAY_OF_MONTH , 1 ) ;
		latestCalendar.set( Calendar.HOUR_OF_DAY , 1 ) ;
		latestCalendar.set( Calendar.MINUTE , 0 ) ;
		latestCalendar.set( Calendar.SECOND , 0 ) ;

		if( before.before( TimeUtils.getCurrentUTCDate() ) )
			report.add( new ErrorMessage( ErrorMessage.ERROR , "The earliest scheduled start time is before the current UTC date" , "" ) ) ;

		if( after.after( latestCalendar.getTime() ) )
			report.add( new ErrorMessage( ErrorMessage.ERROR , "The latest scheduled end time is after the latest schedulable end time" , "" ) ) ;
	}
}
