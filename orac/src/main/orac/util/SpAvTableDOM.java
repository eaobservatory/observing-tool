/*
 * Copyright 2001-2002 United Kingdom Astronomy Technology Centre, an
 * establishment of the Science and Technology Facilities Council.
 *
 * This program is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation; either version 2 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License along
 * with this program; if not, write to the Free Software Foundation, Inc.,
 * 51 Franklin Street, Fifth Floor, Boston, MA 02110-1301 USA.
 */

package orac.util ;

import gemini.sp.SpAvTable ;
import org.w3c.dom.Element ;
import org.w3c.dom.Node ;
import org.w3c.dom.NodeList ;
import org.w3c.dom.traversal.TreeWalker ;
import org.apache.xerces.dom.ElementImpl ;
import org.apache.xerces.dom.DocumentImpl ;
import java.util.Vector ;
import java.util.StringTokenizer ;
import java.util.Enumeration ;
import java.util.Arrays ;

/**
 *
 * Attribute/value pairs are added to the DOM element as follows.
 * Examples:
 * 
 * example   attribute       value            DOM/XML
 * <!--
 * 
 * (1)       one.two.three   some text        <one>
 *                                              <two>
 *                                                <three>some text</three>
 *                                              </two>
 *                                            </one>
 * 
 * (2)       one.two:three   some text        <one>
 *                                              <two three="some text"></two>
 *                                            </one>
 * 
 * (3)                                        <one>
 *                                              <two>
 *           one.two#1.three some text            <three>some text</three>
 *                                              </two>
 *                                              <two>
 *           one.two#2.three some more text       <three>some more text</three>
 *                                              </two>
 *                                              <two>
 *           one.two#3:myAttr.three something else  <three myAttr="something else"/>
 *                                              </two>
 * -->
 *
 * <!-- for javadoc -->
 * (1)       one.two.three   some text        &lt;one&gt;
 *                                              &lt;two&gt;
 *                                                &lt;three&gt;some text&lt;/three&gt;
 *                                              &lt;/two&gt;
 *                                            &lt;/one&gt;
 * 
 * (2)       one.two:three   some text        &lt;one&gt;
 *                                              &lt;two three="some text"&gt;&lt;/two&gt;
 *                                            &lt;/one&gt;
 * 
 * (3)                                        &lt;one&gt;
 *                                              &lt;two&gt;
 *           one.two#1.three some text            &lt;three&gt;some text&lt;/three&gt;
 *                                              &lt;/two&gt;
 *                                              &lt;two&gt;
 *           one.two#2.three some more text       &lt;three&gt;some more text&lt;/three&gt;
 *                                              &lt;/two&gt;
 *                                              &lt;two&gt;
 *           one.two#3:myAttr.three something else  &lt;three myAttr="something else"/&gt;
 *
 * @author Martin Folger (M.Folger@roe.ac.uk)
 */
public class SpAvTableDOM
{
	public static final String META_DATA_TAG = "MetaData" ;

	/**
	 * The attribute value table that is represented by this class.
	 */
	protected SpAvTable _avTab ;

	/**
	 * Owner document that the attribute value table of this class and possibly all the other
	 * bits and pieces of a science program will be added to.
	 *
	 * @see #_avTab
	 */
	protected DocumentImpl _document ;// = new DocumentImpl() ;

	/**
	 * The root of the DOM sub tree that represents the attribute value table of this class.
	 *
	 * @see #_avTab
	 */
	protected ElementImpl _element ;
	protected TreeWalker _treeWalker ;

	/**
	 * This constuctor should be used in for processing entire the science programs.
	 */
	public SpAvTableDOM( SpAvTable avTab , ElementImpl element )
	{
		_element = element ;
		_document = ( DocumentImpl )element.getOwnerDocument() ;
		_treeWalker = _document.createTreeWalker( _element , ( short )0 , null ) ;

		_avTab = avTab ;

	}

	public void parseAvTable()
	{
		// Construct AvToDom classes using a sorted attribute array.

		Enumeration<String> e = _avTab.attributes() ;
		Vector<String> attributeVector = new Vector<String>() ;

		while( e.hasMoreElements() )
			attributeVector.add(e.nextElement());

		String[] attributeArray = new String[ attributeVector.size() ] ;
		attributeVector.toArray( attributeArray ) ;
		Arrays.sort( attributeArray ) ;

		AvToDom avToDom ;

		for( int i = 0 ; i < attributeArray.length ; i++ )
		{
			avToDom = new AvToDom( attributeArray[ i ] ) ;
			avToDom.parseAttributeValue() ;
		}
	}

	/**
	 * This class parses one attribute value pair of the table.
	 */
	class AvToDom
	{
		protected StringTokenizer _attrTokenizer ;
		protected Node _nodePointer ;
		protected String _xmlTag ;

		/**
		 * XML attribute.
		 * 
		 * <!-- XML attributes are placed insight xml tags: <tag my_attribute="my value"> -->
		 * <!-- for javadoc -->
		 *      XML attributes are placed insight xml tags: &lt;tag my_attribute="my value"&gt;
		 *
		 * @see #_avTabAttribute
		 */
		protected String _xmlAttribute ;

		/**
		 * SpAvTable attribute.
		 *
		 * An SpAvTable contains attribute/value pairs. Such an attribute is a string that can be a simple name
		 * (e.g. "expTime") or more complex (e.g. "spectralWindow.range#3.start")
		 *
		 * @see orac.util.SpAvTableDOM
		 * @see #_xmlAttribute
		 */
		protected String _avTabAttribute ;
		protected String _userData ;

		public AvToDom( String avTabAttribute )
		{
			_avTabAttribute = avTabAttribute ;

			// The SpAvTable attributes that start with a '.' are a special case.
			// This dot notations has been part of the gemini OT from the start and is
			// different from the kind of dot notation for SpAvTable attributes introduced
			// with this orac3 version of the OT (Merger of ATC/JAC freebongo OT in orac2 and JSky ot-0.5)
			// So the old attributes starting with '.' some of which contain further '.' in the middle
			// (e.g. ".gui.filename") should be converted in some way.
			// They don't have to be converted. If they are left as they are
			// 
			// ".version" 1.0 becomes
			// 
			//  <version>1.0</version>
			//  
			// and
			//
			// ".gui.filename" test.sp becomes
			//
			// <gui>
			//   <filename>test.sp</filename>
			// </gui>
			// 
			// A sensible thing to do is to preceed every SpAvTable attribute that starts with '.' with a
			// string like META_DATA_TAG turning the above examples into
			// 
			// META_DATA_TAG + ".version" 1.0 which becomes
			// 
			// <META_DATA_TAG>
			//   <version>1.0</version>
			// </META_DATA_TAG>  
			//  
			// and
			//
			// META_DATA_TAG + ".gui.filename" test.sp which becomes
			//
			// <META_DATA_TAG>  
			//   <gui>
			//     <filename>test.sp</filename>
			//   </gui>
			// </META_DATA_TAG>
			//
			// The converted attribute must onlygo into the StringTokenizer. The original is later needed to
			// retrieve the value.

			if( _avTabAttribute.startsWith( "." ) )
				_attrTokenizer = new StringTokenizer( META_DATA_TAG + _avTabAttribute , "." ) ;
			else
				_attrTokenizer = new StringTokenizer( _avTabAttribute , "." ) ;
		}

		protected void parseNextToken()
		{
			String token = _attrTokenizer.nextToken() ;

			// If token has the form tag:attr then break it up accordingly.
			if( token.indexOf( ':' ) != -1 )
			{
				_xmlTag = token.substring( 0 , token.indexOf( ':' ) ) ;
				_xmlAttribute = token.substring( token.indexOf( ':' ) + 1 ) ;
			}
			else
			{
				_xmlTag = token ;
			}

			// Special case. Token was of the form ":someToken"
			if( _xmlTag.equals( "" ) && ( _xmlAttribute != null ) )
			{
				( ( Element )_treeWalker.getCurrentNode() ).setAttribute( _xmlAttribute , _avTab.get( _avTabAttribute , 0 ) ) ;
				return ;
			}

			// user data is the token + '#' + a number but NOT ':' + attribute if these form the end of the token.
			_userData = _xmlTag ;

			// If this tag appears more then once has to have a unique attribute name in SpAvTable.
			// This is done by appending '#' and a numer to the tag.
			// In xml this is not needed and the tail of the _xmlTag ('#' followed by a number can be discarded.)
			if( _xmlTag.indexOf( '#' ) != -1 )
				_xmlTag = _xmlTag.substring( 0 , _xmlTag.indexOf( '#' ) ) ;

			NodeList nodeList = _treeWalker.getCurrentNode().getChildNodes() ;

			// If no multiple tags with the same name are intended in the parent tag then try whether a
			// tag with the name _xmlTag exists already, set the attribute, if there is one, set the
			// current node of the tree walker to it and return.
			for( int i = 0 ; i < nodeList.getLength() ; i++ )
			{
				if( ( nodeList.item( i ) instanceof ElementImpl ) && ( ( ( ElementImpl )nodeList.item( i ) ).getUserData() != null ) && ( ( ( String )( ( ElementImpl )nodeList.item( i ) ).getUserData() ).equals( _userData ) ) )
				{

					if( _xmlAttribute != null )
					{
						// NOTE that there can only be one xml attributes with the same name in a tag (?) and attributes have only one value.
						// Therefore only the first entry in the value vector is used and the others are ignored.
						( ( Element )nodeList.item( i ) ).setAttribute( _xmlAttribute , _avTab.get( _avTabAttribute , 0 ) ) ;
					}
					else
					{
						if( System.getProperty( "DEBUG" ) != null )
							System.out.println( "Used to creating text node (1) here: " + _avTab.get( _avTabAttribute , 0 ) + " as child of " + nodeList.item( i ).getNodeName() ) ;					
					}

					_treeWalker.setCurrentNode( nodeList.item( i ) ) ;
					return ;
				}
			}

			// Not returned yet? Or multiple tags intended? Then a node has to be created.
			Node child = null ;
			// Is not the final section of _avTabAttribute (Sections are separated by '.' in the attribute string _avTabAttribute.)
			if( _attrTokenizer.hasMoreTokens() )
			{
				child = _treeWalker.getCurrentNode().appendChild( _document.createElement( _xmlTag ) ) ;
				( ( ElementImpl )child ).setUserData( _userData ) ;
			}
			else
			{
				// Is the final section of the attribute string _avTabAttribute.
				Vector<String> v = _avTab.getAll( _avTabAttribute ) ;

				if( v != null && v.size() > 0 )
				{
					child = _treeWalker.getCurrentNode().appendChild( _document.createElement( _xmlTag ) ) ;
					( ( ElementImpl )child ).setUserData( _userData ) ;

					if( v.size() < 2 )
					{
						if( _xmlAttribute == null )
							child.appendChild( _document.createTextNode( _avTab.get( _avTabAttribute , 0 ) ) ) ;
					}
					else
					{
						for( int i = 0 ; i < v.size() ; ++i )
							child.appendChild( _document.createElement( "value" ) ).appendChild( _document.createTextNode( _avTab.get( _avTabAttribute , i ) ) ) ;
					}
				}
			}

			// As above, note that there can only be one xml attributes with the same name in a tag (?) and attributes have only one value.
			// Therefore only the first entry in the value vector is used and the others are ignored.
			if( _xmlAttribute != null )
			{
				try
				{
					( ( Element )child ).setAttribute( _xmlAttribute , _avTab.get( _avTabAttribute , 0 ) ) ;
				}
				catch( Exception e )
				{
					e.printStackTrace() ;
				}
			}

			_treeWalker.setCurrentNode( child ) ;
		}

		public void parseAttributeValue()
		{
			_treeWalker.setCurrentNode( _element ) ;
			while( _attrTokenizer.hasMoreTokens() )
				parseNextToken() ;
		}
	}
}
