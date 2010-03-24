/**
 * Copyright (C) 2009 - 2010 Science and Technology Facilities Council.
 * All Rights Reserved.
 */

package edu.hawaii.jach.omp.toml.util ;

import static javax.xml.XMLConstants.W3C_XML_SCHEMA_NS_URI ;

import java.io.FileNotFoundException;
import java.io.IOException ;
import java.io.InputStream ;
import java.io.OutputStream ;
import java.net.MalformedURLException;
import java.net.URL ;
import java.util.List ;
import java.util.Vector ;

import javax.xml.bind.Binder ;
import javax.xml.bind.JAXBContext ;
import javax.xml.bind.JAXBException ;
import javax.xml.bind.Marshaller ;
import javax.xml.bind.ValidationEventHandler ;
import javax.xml.parsers.DocumentBuilder ;
import javax.xml.parsers.DocumentBuilderFactory ;
import javax.xml.parsers.ParserConfigurationException ;
import javax.xml.validation.Schema ;
import javax.xml.validation.SchemaFactory ;
import javax.xml.xpath.XPath ;
import javax.xml.xpath.XPathConstants ;
import javax.xml.xpath.XPathExpression ;
import javax.xml.xpath.XPathExpressionException ;
import javax.xml.xpath.XPathFactory ;

import org.w3c.dom.Node ;
import org.w3c.dom.Document ;
import org.w3c.dom.NodeList ;
import org.xml.sax.SAXException ;

/**
 * General class for creating and manipulating the relationships between 
 * DOM trees and their associated JAXB objects.
 *
 *
 */
public class TreeHugger
{
	private SchemaDetails schemaDetails ;

	private Object objectFactory ;
	private JAXBContext jc ;
	private Schema schema ;
	private Binder<Node> binder ;
	protected ValidationEventHandler validationHandler = null ;

	protected Document document ;

/////////////  TEST

	public static void main( String[] args ) throws ClassNotFoundException, InstantiationException, IllegalAccessException, FileNotFoundException
	{
		System.setProperty( SchemaManager.CFG_KEY , "file:///export/data/shart/current/ingot-git/cfg/schemas.cfg" ) ;
		java.io.FileInputStream fis = new java.io.FileInputStream( "/home/shart/ukirtcal.xml" ) ;
		TreeHugger treeHugger = new TreeHugger( "ukirt" , fis ) ;
		Object factory = treeHugger.getObjectFactory() ;
		System.out.println( factory.getClass().getName() ) ;
		List<?> list = treeHugger.findAllObjectsOfType( "SpProg" ) ;
	}

///////////// CONSTRUCTORS

	/**
	 * Create a new instance, schema must be set manually for manipulation
	 */
	public TreeHugger()
	{
		newDocument() ;
	}

	/**
	 * Create a new instance from an InputStream, schema must be set manually for manipulation
	 * @param is - InputStream
	 */
	public TreeHugger( InputStream is )
	{
		unmarshalDocument( is ) ;
	}

	/**
	 * Create a new instance
	 * @param schemaName - name of schema
	 */
	public TreeHugger( String schemaName )
	{
		try
		{
			setSchema( schemaName ) ;
			newDocument() ;
		}
		catch( Exception e )
		{
			throw new RuntimeException( "Schema and initialisation failed." ) ;
		}
	}

	/**
	 * Create a new instance from an InputStream
	 * @param schemaName - name of schema
	 * @param is - InputStream
	 */
	public TreeHugger( String schemaName , InputStream is )
	{
		try
		{
			setSchema( schemaName ) ;
			unmarshalDocument( is ) ;
		}
		catch( Exception e )
		{
			throw new RuntimeException( "Schema and initialisation failed." ) ;
		}
	}

/////////////////// DOCUMENT METHODS

	/**
	 * Get the document object or null.
	 */
	public Document getDocument()
	{
		return document ;
	}

	/**
	 * Create a new document for this instance
	 */
	protected void newDocument()
	{
		if( document != null )
			throw new RuntimeException( "Document already initialised." ) ;
	
		DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance() ;
		dbf.setNamespaceAware( true ) ;
		try
		{
			DocumentBuilder db = dbf.newDocumentBuilder() ;
			document = db.newDocument() ;
		}
		catch( ParserConfigurationException e ){ e.printStackTrace() ; }
	}

	/**
	 * Unmarshal a document from an input stream for this instance
	 * @param is - InputStream to read from
	 */
	protected void unmarshalDocument( InputStream is )
	{
		if( document != null )
			throw new RuntimeException( "Document already initialised." ) ;

		if( is != null )
		{
			DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance() ;
			dbf.setNamespaceAware( true ) ;
			try
			{
				DocumentBuilder db = dbf.newDocumentBuilder() ;
				document = db.parse( is ) ;
			}
			catch( ParserConfigurationException pce ){ pce.printStackTrace() ; }
			catch( SAXException se ){ se.printStackTrace() ; }
			catch( IOException ioe ){ ioe.printStackTrace() ; }
		}
	}

	/**
	 * Marshal the document for this instance to an output stream
	 * @param os - OutputStream to write to
	 */
	public void marshalDocument( OutputStream os )
	{
		try
		{
			Marshaller marshaller = getContext().createMarshaller() ;
			marshaller.setProperty( Marshaller.JAXB_FORMATTED_OUTPUT , Boolean.TRUE ) ;
			marshaller.setEventHandler( validationHandler ) ;
			Object object = nodeToObject( document ) ;
			marshaller.marshal( object , os ) ;
		}
		catch( JAXBException je )
		{
			je.printStackTrace() ;
		}
	}

//////////////// DOCUMENT MANIPULATION TOOLS

	/**
	 * Convert a Node into a JAXB object, updating it's descendants
	 * @param node - XML node
	 * @return JAXB object representing that node, or null
	 */
	protected Object nodeToObject( Node node )
	{
		Object object = null ;
		if( node != null )
			object = getBinder().getJAXBNode( node ) ;
		if( object == null )
		{
			try
			{
				object = getBinder().updateJAXB( node ) ;
			}
			catch( JAXBException e ){ e.printStackTrace() ; }
		}
		return object ;
	}

	/**
	 * Convert a JAXB object into a node, updating it's descendants
	 * @param object - JAXB object
	 * @return XML node representing that object, or null
	 */
	protected Node objectToNode( Object object )
	{
		Node node = null ;
		try
		{
			node = getBinder().updateXML( object ) ;
		}
		catch( JAXBException e ){ e.printStackTrace() ; }
		catch( NullPointerException npe ){ npe.printStackTrace() ; }
		return node ;
	}
	
	/**
	 * Unmarshal an XML node to a JAXB object, with validation
	 * @param XML node
	 * @return JAXB object representing that node or null
	 */
	protected Object unmarshalNodeToObject( Node node )
	{
		Object unmarshalled = null ;
		try
		{
			unmarshalled = getBinder().unmarshal( node ) ;
		}
		catch( JAXBException e ){ e.printStackTrace() ; }
		return unmarshalled ;
	}

	/**
	 * Marshal an XML node into a JAXB object
	 * @param JAXB object
	 * @return XML node representing that JAXB object or null
	 */
	protected Node marshalObjectToNode( Object object )
	{
		Node node = null ;
		DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance() ;
		dbf.setNamespaceAware( true ) ;
		try
		{
			DocumentBuilder db = dbf.newDocumentBuilder() ;
			node = db.newDocument() ;
		}
		catch( ParserConfigurationException e ){ e.printStackTrace() ; }
		try
		{
			getBinder().marshal( object , node ) ;
		}
		catch( JAXBException e ){ e.printStackTrace() ; }
		return node ;
	}

	/**
	 * Finds all objects of a given type for the whole document
	 * @param type -  String name of Class of type to find
	 * @return List<?> of found nodes cast to Class type
	 */
	public List<?> findAllObjectsOfType( String type )
	{
		return findAllObjectsOfType( type , document ) ;
	}

	/**
	 * Finds all objects of a given type for the whole document
	 * @param type - Class of type to find
	 * @return List<?> of found nodes cast to Class type
	 */
	public List<?> findAllObjectsOfType( Class<?> type )
	{
		return findAllObjectsOfType( type.getSimpleName() , document ) ;
	}

	/**
	 * Finds all objects of a given type, from a given node
	 * @param type - Class of type to find
	 * @param startNode - XML node to start search from
	 * @return List<?> of found nodes cast to Class type
	 */
	public List<?> findAllObjectsOfType( Class<?> type , Node startNode )
	{
		return findAllObjectsOfType( type.getSimpleName() , startNode ) ;
	}

	/**
	 * Finds all objects of a given type, from a given node
	 * @param type - String name of Class of type to find
	 * @param startNode - XML node to start search from
	 * @return List<?> of found nodes cast to Class type
	 */
	public List<?> findAllObjectsOfType( String type , Node startNode )
	{
		List<Object> results = new Vector<Object>() ;
		if( startNode != null && type != null )
		{
			XPathFactory factory = XPathFactory.newInstance() ;
			XPath xpath = factory.newXPath() ;
			xpath.setNamespaceContext( schemaDetails.getNSContext() ) ;
			String prefix = "//" + schemaDetails.getNSContext().getPrefix( schemaDetails.getURL() ) + ":" ;
			try
			{
				XPathExpression expr = xpath.compile( prefix + type ) ;
				Object result = expr.evaluate( startNode , XPathConstants.NODESET ) ;
				NodeList nodes = ( NodeList )result ;
				Object converted = null ;
				Node node = null ;
				for( int i = 0 ; i < nodes.getLength() ; i++ )
				{
					node = nodes.item( i ) ;
					converted = nodeToObject( node ) ;
					if( converted != null )
						results.add( converted ) 
;
				}
			}
			catch( XPathExpressionException xpee )
			{
				System.out.println( xpee ) ;
			}
		}
		return results ;
	}

//////////////// BIND POST SETUP	

	/**
	 * @return Binder object if schema has been initialised.
	 */	
	protected Binder<Node> getBinder()
	{
		if( binder == null )
		{
			binder = getContext().createBinder() ;
			binder.setSchema( getSchema() ) ;
			setValidationHandler( validationHandler ) ;
		}
		return binder ;
	}

	/**
	 * Sets the default validation handler, only one validation handler at a time can be used
	 * so this effectively replaces the previous validation handler, and affects all validation.
	 * @param handler instance of ValidationEventHandler to be used.
	 */
	public void setValidationHandler( ValidationEventHandler handler )
	{
		validationHandler = handler ;
		if( binder != null )
		{
			try
			{
				binder.setEventHandler( validationHandler ) ;
			}
			catch( JAXBException e ){ e.printStackTrace() ; }
		}
	}

	/**
	 * Returns the current validation handler.
	 * @return ValidationEventHandler currently in use, 
	 * or the last one registered should it not be currently in use.
	 */
	public ValidationEventHandler getValidationHandler()
	{
		ValidationEventHandler handler = validationHandler ;
		if( binder != null )
		{
			try
			{
				handler = binder.getEventHandler() ;
			}
			catch( JAXBException e ){ e.printStackTrace() ; }
		}
		return handler ;
	}

	/**
	 * Convenience method combining {get,set}ValidationHandler.
	 * The ValidationEventHandler will not be replaced if they are the same object,
	 * however new instances of the same class will get set as the new handler.
	 * @param handler new ValidationEventHandler to use for future validation.
	 * @return ValidationEventHandler being replaced.
	 */
	public ValidationEventHandler replaceValidationHandler( ValidationEventHandler handler )
	{
		ValidationEventHandler oldhandler = getValidationHandler() ;
		if( oldhandler != handler )
			setValidationHandler( handler ) ;
		return oldhandler ;
	}

////////////// POST SETUP

	/**
	 * @return Schema object if schema has been initialised.
	 */
	protected Schema getSchema()
	{
		if( schemaDetails == null )
			throw new RuntimeException( "Schema not initialised" ) ;
		
		if( schema == null )
		{
			SchemaFactory sf = SchemaFactory.newInstance( W3C_XML_SCHEMA_NS_URI ) ;
			
			try
			{
				URL url = new URL( schemaDetails.getSchema() ) ;
				schema = sf.newSchema( url ) ;
			}
			catch( SAXException se ){ se.printStackTrace() ; }
			catch( MalformedURLException mue ){ mue.printStackTrace() ; }
		}
		return schema ;
	}

	/**
	 * @return JAXBContext object if schema has been initialised.
	 */
	protected JAXBContext getContext()
	{
		if( jc == null )
		{
			if( schemaDetails == null )
				throw new RuntimeException( "Schema not defined" ) ;
			
		try
		{
			String classpath = schemaDetails.getClassPath() ;
			jc = JAXBContext.newInstance( classpath ) ;
		}
		catch( JAXBException e ){ e.printStackTrace() ; }
		}
		return jc ;
	}

////////////// SET UP

	/**
	 * Set the schema for this document, required for schema context and object creation
	 * @param schemaName - name of schema
	 */
	public void setSchema( String schemaName ) throws Exception
	{
		if( schemaDetails != null )
			throw new RuntimeException( "Schema already initialised." ) ;

		schemaDetails = SchemaManager.setSchema( schemaName ) ;
	}

	/**
	 * Get the object factory associated with this schema.
	 * @return Object factory associated with this schema.
	 * @throws ClassNotFoundException - schema unknown to class loader
	 * @throws InstantiationException - schema object factory could not be instantiated
	 * @throws IllegalAccessException - method does not have access to instantiate object factory
	 */
	public Object getObjectFactory() throws ClassNotFoundException, InstantiationException, IllegalAccessException
	{
		if( objectFactory == null )
		{
			if( schemaDetails != null )
			{
				Class<?> klass = null ;
				System.out.println( "Loading => " + schemaDetails.getClassPath() + ".ObjectFactory" ) ;
				klass = Class.forName( schemaDetails.getClassPath() + ".ObjectFactory" ) ;
				if( klass != null )
					objectFactory = klass.newInstance() ;
			}
			else
			{
				throw new RuntimeException( "Schema not initialised." ) ;
			}
		}
		return objectFactory ;
	}
}
