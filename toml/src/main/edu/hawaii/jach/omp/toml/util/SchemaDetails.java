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

/**
 * Class encapsulating the details of an active schema.
 *
 */
public class SchemaDetails
{
	private String CLASSPATH ;
	private String SCHEMA ;
	private String URL ;
	private String schemaName ;
	private NamespaceContext nsContext ;

	/**
	 * Default constructor.
	 * Once an instance is created we do not expect the details to change.
	 * @param schemaName Name of the schema
	 * @param SCHEMA Path to the schema
	 * @param CLASSPATH Classpath of the schema
	 * @param URL URL of the schema
	 * @param nsContext NamespaceContext object
	 */
	public SchemaDetails( String schemaName , String SCHEMA , String CLASSPATH , String URL , NamespaceContext nsContext )
	{
		this.CLASSPATH = CLASSPATH ;
		this.SCHEMA = SCHEMA ;
		this.URL = URL ;
		this.schemaName = schemaName ;
		this.nsContext = nsContext ;
	}

	public String getClassPath()
	{
		return CLASSPATH ;
	}

	public String getSchema()
	{
		return SCHEMA ;
	}

	public String getURL()
	{
		return URL ;
	}

	public String getSchemaName()
	{
		return schemaName ;
	}

	public NamespaceContext getNSContext()
	{
		return nsContext ;
	}
}
