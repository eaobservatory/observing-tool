/*
 * Copyright (C) 2009 - 2010 Science and Technology Facilities Council.
 * All Rights Reserved.
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

package edu.hawaii.jach.omp.toml.util ;

import javax.xml.namespace.NamespaceContext ;
import javax.xml.XMLConstants ;

public class TOMLNamespaceContext implements NamespaceContext
{
	private static final TOMLNamespaceContext instance = new TOMLNamespaceContext() ;
	private static final String TOML_URI = "http://omp.jach.hawaii.edu/schema/TOML" ;
	private static final String TOML_PREFIX = "toml" ;
	private static final String XML_PREFIX = "xml" ;

	private TOMLNamespaceContext()
	{
		super() ;
	}

	public static TOMLNamespaceContext getInstance()
	{
		return instance ;
	}

	public String getNamespaceURI( String prefix )
	{
		if( prefix == null )
			throw new NullPointerException( "Null prefix" ) ;
		else if( TOML_PREFIX.equals( prefix ) )
			return TOML_URI ;
		else if( XML_PREFIX.equals( prefix ) )
			return XMLConstants.XML_NS_URI ;
		return XMLConstants.NULL_NS_URI ;
	}

	public String getPrefix( String uri )
	{
		if( uri == null )
			throw new NullPointerException( "Null URI" ) ;
		else if( TOML_URI.equals( uri ) )
			return TOML_PREFIX ;
		else if( XMLConstants.XML_NS_URI.equals( uri ) )
			return XML_PREFIX ;
		return null ;
	}

	/*
	 * The following method getPrefixes is currently not supported
	 */
	public java.util.Iterator<Object> getPrefixes( String uri )
	{
		throw new UnsupportedOperationException() ;
	}
}
