/**
 * Copyright (C) 2009 - 2010 Science and Technology Facilities Council.
 * All Rights Reserved.
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
