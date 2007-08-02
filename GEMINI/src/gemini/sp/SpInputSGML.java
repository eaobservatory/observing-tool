// Copyright 1997 Association for Universities for Research in Astronomy, Inc.,
// Observatory Control System, Gemini Telescopes Project.
// See the file COPYRIGHT for complete details.
//
// $Id$
//
package gemini.sp;

import gemini.util.Assert;
import java.io.StreamTokenizer ;
import java.io.Reader ;
import java.io.IOException ;
import java.io.InputStreamReader ;
import java.io.InputStream ;
import java.io.BufferedReader ;
import java.util.Vector;

//
// Extends StreamTokenizer to add a few SGML specific tokens. Warning
// though, this really isn't very advanced.
//
final class SgmlStreamTokenizer extends StreamTokenizer
{

	/**
     * An invalid state was detected.
     */
	public static final int TT_INVALID = -101;

	/**
     * TT_TAG_START consists of < followed by a tag name. sval is set to the tag
     * name.
     */
	public static final int TT_TAG_START = -102;

	/**
     * TT_TAG_DEF_END is returned when the > character is matched.
     */
	public static final int TT_TAG_DEF_END = -103;

	/**
     * TT_TAG_END consists of <\ followed by a tag name, followed by >. The sval
     * variable is set to the tag name.
     */
	public static final int TT_TAG_END = -104;

	SgmlStreamTokenizer( Reader r )
	{
		super( r );

		// Treat almost everything, including numbers, as alphabetic. The
		// Only tokens that should be left are the string quote characters
		// and the subset of SGML characters that this tokenizer recognizes.
		ordinaryChars( '!' , '~' );
		wordChars( '!' , '~' );

		ordinaryChar( '\'' );
		quoteChar( '\'' );
		ordinaryChar( '"' );
		quoteChar( '"' );
		ordinaryChar( '/' );
		ordinaryChar( '<' );
		ordinaryChar( '=' );
		ordinaryChar( '>' );
	}

	public int nextToken() throws IOException
	{
		int nextTok = super.nextToken();
		switch( nextTok )
		{
			case '<' :
			{
				nextTok = super.nextToken();
				switch( nextTok )
				{
					case TT_WORD :
						// This is the start of a new item
						return TT_TAG_START;
					case '/' :
					{
						// This had better be an end item tag: </tagname>
						if( super.nextToken() != TT_WORD )
							return TT_INVALID;
						String tag = sval;
						if( super.nextToken() != '>' )
							return TT_INVALID;
						sval = tag;
						return TT_TAG_END;
					}
					default :
						// < must be followed by a tagname or \tagname>
						return TT_INVALID;
				}
			}

			case '>' :
				return TT_TAG_DEF_END;

			case '\'' :
			case '"' :
				// Return quoted strings as TT_WORD matches
				return TT_WORD;
		}
		return nextTok;
	}

}

//
// An implementation class that serves as a container for the three elements
// required to define a Science Program item: name, type, and subtype.
//
final class SpItemDef
{
	public String name;
	public String type;
	public String subtype;

	public String toString()
	{
		return "name=" + name + ", type=" + type + ", subtype=" + subtype;
	}
}

//
// An implementation class that serves as a container for the two elements
// required to define a Science Program item attribute: name and description.
//
final class SpAttributeDef
{

	public String name;
	public String descr;

	public String toString()
	{
		return "name=" + name + ", descr=" + descr;
	}

}

/**
 * This is a simple recursive decent, top-down parser for Science Programs. The
 * grammar recognized is given below where <tt> tag </tt> stands for any generic
 * tag, <tt> e </tt> is the nil string, and <tt> id </tt> is any identifier.
 * 
 * <pre>
 * Document  -&gt; Item |
 *                 &quot;&lt;spDocument&gt;&quot; Item &quot;&lt;/spDocument&gt;&quot; |
 *                 &quot;&lt;spDocument&gt;&quot; Phase1Item ProgItem &quot;&lt;/spDocument&gt;&quot;
 * Phase1Item-&gt; &quot;&lt;p1&quot;  Def &quot;&gt;&quot; Content &quot;&lt;/p1&gt;&quot;
 * ProgItem  -&gt; &quot;&lt;pr&quot;  Def &quot;&gt;&quot; Content &quot;&lt;/pr&gt;&quot;
 * Item      -&gt; &quot;&lt;tag&quot; Def &quot;&gt;&quot; Content &quot;&lt;/tag&gt;&quot;
 * Def       -&gt; id &quot;=&quot; id Def | e
 * Content   -&gt; AvList Children 
 * AvList    -&gt; Attribute AvList | e
 * Attribute -&gt; &quot;&lt;av&quot; AvDef &quot;&gt;&quot; Value ValueList &quot;&lt;/av&gt;&quot;
 * AvDef     -&gt; id &quot;=&quot; id AvDef | e
 * Value     -&gt; &quot;&lt;val&quot; &quot;value&quot; &quot;=&quot; string-value &quot;&gt;&quot;
 * ValList   -&gt; Value ValList | e
 * Children  -&gt; Item Children | e
 * The Phase1Item and ProgItem productions are just instances of the
 * general Item production where the &quot;tag&quot; is specified.
 * Def consists of the id's &quot;name&quot;, &quot;type&quot;, and &quot;subtype&quot;.
 * AvDef consists of &quot;name&quot; and &quot;descr&quot;.
 * </pre>
 */
public final class SpInputSGML
{

	private SgmlStreamTokenizer _st;
	private int _curToken = -1;
	private int _lineNo = -1;
	private String _problem;

	/**
     * Constructs a parser for the SGML code for a Science Program in the given
     * InputStream.
     */
	public SpInputSGML( InputStream is )
	{
		this( new InputStreamReader( is ) );
	}

	/**
     * Constructs a parser for the SGML code for a Science Program in the given
     * Reader.
     */
	public SpInputSGML( Reader r )
	{
		_st = new SgmlStreamTokenizer( new BufferedReader( r ) );
	}

	private String _tokenToString( int token )
	{
		switch( token )
		{
			case SgmlStreamTokenizer.TT_TAG_START :
				return "<tag";
			case SgmlStreamTokenizer.TT_TAG_DEF_END :
				return ">";
			case SgmlStreamTokenizer.TT_TAG_END :
				return "</tag>";
			case SgmlStreamTokenizer.TT_INVALID :
				return "INVALID SGML";
			case StreamTokenizer.TT_WORD :
				return "IDENTIFIER";
			case StreamTokenizer.TT_EOF :
				return "END OF FILE";
			default :
				char[] c = { ( char )token };
				return new String( c );
		}
	}

	/**
     * If the parsing failed, this method may be called get the problem
     * description, including the line of the Science Program file on which the
     * error occured.
     */
	public String getProblemDescr()
	{
		return "Line " + _st.lineno() + ": " + _problem;
	}

	//
	// "Expect" the given token. If the current token isn't the expected
	// token, then set the problem description and return false. Otherwise,
	// advance the current token to the next one in the input stream.
	//
	private boolean _expect( int token )
	{
		if( token != _curToken )
		{
			_problem = "Expected " + _tokenToString( token ) + ", but got " + _tokenToString( _curToken );
			return false;
		}

		try
		{
			_curToken = _st.nextToken();
		}
		catch( IOException ex )
		{
			_problem = "IOException!";
			return false;
		}
		return true;
	}

	//
	// This method is called to set the problem description string
	// when one of the given tokens were expected in the input, but
	// the actual token read was not one of them.
	//
	private void _expected( int[] tokens , int actualToken )
	{
		_problem = "Expected " + _tokenToString( tokens[ 0 ] );
		for( int i = 1 ; i < tokens.length ; ++i )
			_problem += ", or " + _tokenToString( tokens[ i ] );
		_problem += ", but got " + _tokenToString( actualToken );
	}

	//
	// Parse this production:
	// Item -> "<tag" Def ">" Content "</tag>"
	//
	private SpItem _parseItem()
	{

		if( !_expect( SgmlStreamTokenizer.TT_TAG_START ) )
			return null;

		SpItemDef itemDef = new SpItemDef();
		if( !_parseDef( itemDef ) )
			return null;

		if( ( itemDef.name == null ) || ( itemDef.type == null ) || ( itemDef.subtype == null ) )
		{
			_problem = "The item is missing a name, type, and/or subtype attribute.";
			return null;
		}

		// ---- temporary fix to read in old science programs with note
        // components
		if( itemDef.type.equals( "oc" ) && itemDef.subtype.equals( "note" ) )
		{
			itemDef.name = "new";
			itemDef.type = "no";
			itemDef.subtype = "none";
		}
		// ---- end temporary fix --- this code will be removed

		// Create the item, but do not allow it to initialize itself. This will
		// be done in the subsequent parsing of the input file.
		SpType spType = SpType.get( itemDef.type , itemDef.subtype );
		if( spType == null )
		{
			_problem = "Couldn't create an item of type '" + itemDef.type + "' and subtype '" + itemDef.subtype + "'.";
			return null;
		}
		SpItem newItem = SpFactory.createShallow( spType );
		Assert.notNull( newItem );
		newItem.name( itemDef.name );

		if( !_expect( SgmlStreamTokenizer.TT_TAG_DEF_END ) )
			return null;

		if( !_parseContent( newItem ) )
			return null;

		if( !_expect( SgmlStreamTokenizer.TT_TAG_END ) )
			return null;

		return newItem;
	}

	//
	// Parse this production:
	// Def -> id "=" id Def | e
	//
	private boolean _parseDef( SpItemDef itemDef )
	{
//		 Finished parsing the item definition
		if( _curToken != StreamTokenizer.TT_WORD )
			return true;

		String attr = _st.sval;

		if( !_expect( StreamTokenizer.TT_WORD ) )
			return false;

		if( !_expect( '=' ) )
			return false;

		String value = _st.sval;

		if( !_expect( StreamTokenizer.TT_WORD ) )
			return false;

		if( attr.equals( "name" ) )
		{
			itemDef.name = value;
		}
		else if( attr.equals( "type" ) )
		{
			itemDef.type = value;
		}
		else if( attr.equals( "subtype" ) )
		{
			itemDef.subtype = value;
		}
		else
		{
			_problem = "Unknown attribute in item definition: " + attr;
			return false;
		}

		// Parse any remaining attributes.
		return _parseDef( itemDef );
	}

	//
	// Parse this production:
	// Content -> AvList Children
	//
	private boolean _parseContent( SpItem spItem )
	{
		if( !_parseAvList( spItem ) )
			return false;

		return _parseChildren( spItem );
	}

	//
	// Parse this production:
	// AvList -> Attribute AvList | e
	//
	private boolean _parseAvList( SpItem spItem )
	{
		// This corresponds to the "AvList -> e" production
		if( ( _curToken != SgmlStreamTokenizer.TT_TAG_START ) || !( _st.sval.equals( "av" ) ) )
			return true;

		if( !_parseAttribute( spItem ) )
			return false;

		return _parseAvList( spItem );
	}

	//
	// Parse this production:
	// Attribute -> "<av" AvDef ">" Value ValueList "</av>"
	//
	private boolean _parseAttribute( SpItem spItem )
	{
		if( !( _st.sval.equals( "av" ) ) )
		{
			_problem = "Expected an attribute start tag: <av";
			return false;
		}

		if( !_expect( SgmlStreamTokenizer.TT_TAG_START ) )
			return false;

		SpAttributeDef attrDef = new SpAttributeDef();
		if( !_parseAvDef( attrDef ) )
			return false;

		if( ( attrDef.name == null ) || ( attrDef.descr == null ) )
		{
			_problem = "The attribute is missing a name and/or descr attribute.";
			return false;
		}

		// Add the attribute with its description to the item's table
		SpAvTable avTab = spItem.getTable();
		avTab.noNotifySetDescription( attrDef.name , attrDef.descr );

		if( !_expect( SgmlStreamTokenizer.TT_TAG_DEF_END ) )
			return false;

		// Parse the first value, there must be at least one
		if( !_parseValue( spItem , attrDef.name , 0 ) )
			return false;

		// Parse any remaining values
		if( !_parseValueList( spItem , attrDef.name , 1 ) )
			return false;

		// We should be at the closing tag: </av>
		if( !( _st.sval.equals( "av" ) ) )
		{
			_problem = "Expected to see the attribute close tag: </av>";
			return false;
		}

		return _expect( SgmlStreamTokenizer.TT_TAG_END );
	}

	//
	// Parse this production:
	// AvDef -> id "=" id AvDef | e
	//
	private boolean _parseAvDef( SpAttributeDef attrDef )
	{
		// Finished parsing the attribute definition
		if( _curToken != StreamTokenizer.TT_WORD )
			return true;

		String lhs = _st.sval;

		if( !_expect( StreamTokenizer.TT_WORD ) )
			return false;

		if( !_expect( '=' ) )
			return false;

		String rhs = _st.sval;

		if( !_expect( StreamTokenizer.TT_WORD ) )
			return false;

		if( lhs.equals( "name" ) )
		{
			attrDef.name = rhs;
		}
		else if( lhs.equals( "descr" ) )
		{
			attrDef.descr = rhs;
		}
		else
		{
			_problem = "Unknown attribute in the item attribute definition: " + lhs;
			return false;
		}

		// Parse any remaining attribute defn attributes
		return _parseAvDef( attrDef );
	}

	//
	// Parse this production:
	// Value -> "<val" "value" "=" string-value ">"
	//
	private boolean _parseValue( SpItem spItem , String attrName , int index )
	{
		if( !( _st.sval.equals( "val" ) ) )
		{
			_problem = "Expected the start of a value definition: '<val'";
			return false;
		}

		if( !_expect( SgmlStreamTokenizer.TT_TAG_START ) )
			return false;

		String lhs = _st.sval;

		if( !_expect( StreamTokenizer.TT_WORD ) )
			return false;

		if( !( lhs.equals( "value" ) ) )
		{
			_problem = "Expected the 'value' attribute, but got: " + lhs;
			return false;
		}

		if( !_expect( '=' ) )
			return false;

		String rhs = _st.sval;

		if( !_expect( StreamTokenizer.TT_WORD ) )
			return false;

		// Add the attribute value definition to the table.
		spItem.getTable().noNotifySet( attrName , rhs , index );

		return _expect( SgmlStreamTokenizer.TT_TAG_DEF_END );
	}

	//
	// Parse this production:
	// ValList -> Value ValList | e
	//
	private boolean _parseValueList( SpItem spItem , String attrName , int index )
	{
		// This corresponds to the "ValList -> e" production
		if( ( _curToken != SgmlStreamTokenizer.TT_TAG_START ) || !( _st.sval.equals( "val" ) ) )
			return true;

		// Parse the value
		if( !_parseValue( spItem , attrName , index ) )
			return false;

		// Parse any remaining values.
		return _parseValueList( spItem , attrName , ++index );
	}

	//
	// Parse this production:
	// Children -> Item Children | e
	//
	private boolean _parseChildren( SpItem spItem )
	{
		// System.out.println("_parseChildren(" + spItem.typeStr() + ")");

		Vector childV = null;
		while( _curToken == SgmlStreamTokenizer.TT_TAG_START )
		{
			// Parse the child and then add it to the vector
			SpItem child = _parseItem();
			if( child == null )
				return false;
			if( childV == null )
				childV = new Vector();
			childV.addElement( child );
		}

		if( childV == null )
			return true; // There were no children to parse

		SpItem[] spItemA = new SpItem[ childV.size() ];

		// Convert the vector into an array.
		for( int i = 0 ; i < spItemA.length ; ++i )
			spItemA[ i ] = ( SpItem )childV.elementAt( i );

		SpInsertData spID = SpTreeMan.evalInsertInside( spItemA , spItem );
		if( spID == null )
		{
			_problem = "Illegal program or plan.";
			return false;
		}
		SpTreeMan.insert( spID );
		return true;
	}

	private SpItem _parseDocument()
	{
		if( !_expect( SgmlStreamTokenizer.TT_TAG_START ) )
			return null;

		if( !_expect( SgmlStreamTokenizer.TT_TAG_DEF_END ) )
			return null;

		SpItem spItem = _parseItem();

		if( ( spItem != null ) && ( spItem instanceof SpPhase1 ) )
		{

			// Is there a program file after this?
			if( _curToken == SgmlStreamTokenizer.TT_TAG_START )
			{
				SpItem spProgItem = _parseItem();
				if( !( spProgItem instanceof SpProg ) )
				{
					_problem = "Only items of type '" + SpType.SCIENCE_PROGRAM.getReadable() + "' may follow phase 1 items.\n";
					_problem += "Instead this file contained an item of type: " + spProgItem.type().getReadable();
					return null;
				}
				( ( SpProg )spProgItem ).setPhase1Item( ( SpPhase1 )spItem );

				// Return the program that was read, not its phase 1 item.
				spItem = spProgItem;
			}
		}

		if( !_expect( SgmlStreamTokenizer.TT_TAG_END ) )
			return null;

		return spItem;
	}

	/**
     * Parse the Science Program file, constructing the Java representation and
     * returning a reference to the root SpItem.
     */
	public SpRootItem parseDocument()
	{
		// Just read the first token.
		if( !_expect( _curToken ) )
			return null;

		if( _curToken != SgmlStreamTokenizer.TT_TAG_START )
		{
			_problem = "Expected a science program tag: '<spDocument'";
			return null;
		}

		SpItem spItem;
		if( _st.sval.equals( "spDocument" ) )
			spItem = _parseDocument();
		else
			spItem = _parseItem();

		if( spItem != null )
		{
			if( !( spItem instanceof SpRootItem ) )
			{
				_problem = "Expected a top-level program item, but got: " + spItem.type().getReadable();
				return null;
			}
			spItem.getEditFSM().reset();
		}
		return ( SpRootItem )spItem;
	}

	/**
     * Um, a debugging method.
     */
	public void debug()
	{
		try
		{
			for( int tokenType = _st.nextToken() ; tokenType != StreamTokenizer.TT_EOF ; tokenType = _st.nextToken() )
			{

				int newLineNo = _st.lineno();
				if( newLineNo != _lineNo )
				{
					System.out.println( "" );
					System.out.print( newLineNo + ": " );
					_lineNo = newLineNo;
				}

				switch( tokenType )
				{
					case SgmlStreamTokenizer.TT_TAG_START :
						System.out.print( "{TAG START (" + _st.sval + ")}" );
						break;
					case SgmlStreamTokenizer.TT_TAG_DEF_END :
						System.out.print( "{TAG DEF END}" );
						break;
					case SgmlStreamTokenizer.TT_TAG_END :
						System.out.print( "{TAG END}" );
						break;
					case StreamTokenizer.TT_WORD :
						System.out.print( "{" + _st.sval + "}" );
						break;
					default :
						System.out.print( "{" + ( char )tokenType + "}" );
				}
			}
			System.out.println( "" );
		}
		catch( IOException e )
		{
			System.err.println( "IO Failure" );
		}
	}
}
