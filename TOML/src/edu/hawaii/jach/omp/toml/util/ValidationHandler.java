/**
 * Copyright (C) 2009 - 2010 Science and Technology Facilities Council.
 * All Rights Reserved.
 */

package edu.hawaii.jach.omp.toml.util ;

import java.lang.reflect.Method ;
import java.util.Vector ;

import javax.xml.bind.ValidationEvent ;
import javax.xml.bind.ValidationEventHandler ;
import javax.xml.bind.ValidationEventLocator ;

import org.w3c.dom.Node ;
import org.xml.sax.ErrorHandler ;
import org.xml.sax.SAXException ;
import org.xml.sax.SAXParseException ;

public class ValidationHandler implements ValidationEventHandler , ErrorHandler
{	
	public static enum severity
	{
		WARNING , ERROR , FATAL
	} ;

	private static final String TOML_URL = "http://omp.jach.hawaii.edu/schema/TOML" ;
	private static final String XXMMSS = "( +)?(\\+|\\-)?\\d{1,2}(:| )+\\d{1,2}(:| )+\\d{1,2}(\\.\\d+)?|( +)?(\\+|\\-)?\\d*\\.\\d*" ;

	TreeHugger treeHugger = null ;

	public ValidationHandler( TreeHugger th )
	{
		treeHugger = th ;
	}

	public boolean handleEvent( ValidationEvent ve )
	{
		ValidationEventLocator vel = ve.getLocator() ;
		System.out.println( "\n" ) ;
		if( vel.getLineNumber() == -1 && vel.getColumnNumber() == -1 )
			nodeEvent( vel , severity.values()[ ve.getSeverity() ] , ve.getMessage() ) ;
		else
			textEvent( vel , severity.values()[ ve.getSeverity() ] , ve.getMessage() ) ;

		return true ;
	}

	String[] named = { "SpMSB" , "SpAND" , "SpOR" , "SpObs" , "SpSurveyContainer" } ;

	private void nodeEvent( ValidationEventLocator vel , ValidationHandler.severity level , String message )
	{
		Node node = vel.getNode() ;
		Vector<Node> nodes = new Vector<Node>() ;
		while( node != null && node.getOwnerDocument() != null )
		{
			nodes.add( 0 , node ) ;
			node = node.getParentNode() ;
		}
		
		String errorMessage = "##########################\n" ;
		errorMessage += level + "\n" ;
		errorMessage += "Node : \n" ;
		String space = " " ;
		for( Node currentNode : nodes )
		{
			String name = currentNode.getNodeName() ;
			String title = "" ;

			for( String type : named )
			{
				if( type.equals( name ) )
				{
					try
					{
						Object obj = treeHugger.nodeToObject( currentNode ) ;
						Method getTitle = obj.getClass().getMethod( "getTitle" , new Class<?>[ 0 ] ) ;
						Object result = getTitle.invoke(  obj  , new Object[ 0 ] ) ;
						if( result != null )
							title += " - " + result ;
					}
					catch( Exception e ){}
					break ;
				}
			}

			errorMessage +=( space + "+" + name + title ) ;
			errorMessage +=( "\n" ) ;
			space += " " ;
		}
		errorMessage += "\n" + parseMessage( message ) + "\n" ;
		errorMessages.add( errorMessage ) ;
	}

	private void textEvent( ValidationEventLocator vel , ValidationHandler.severity level , String message )
	{
		String errorMessage = "##########################\n" ;
		errorMessage += "XML Line : " + vel.getLineNumber() ;
		errorMessage += "Column : " + vel.getColumnNumber() ;
		errorMessage += parseMessage( message )+ "\n" ;
		errorMessages.add( errorMessage ) ;
	}

	private Vector<String> errorMessages = new Vector<String>() ;

	public synchronized Vector<String> getErrors()
	{
		Vector<String> returnableMessages = errorMessages ;
		errorMessages = new Vector<String>() ;
		return returnableMessages ;
	}

	public void error( SAXParseException exception ) throws SAXException
	{
		handleSAXParseException( "Error" , exception ) ;
	}

	public void fatalError( SAXParseException exception ) throws SAXException
	{
		handleSAXParseException( "Fatal Error" , exception ) ;
	}

	public void warning( SAXParseException exception ) throws SAXException
	{
		handleSAXParseException( "Warning" , exception ) ;
	}

	private void handleSAXParseException( String type , SAXParseException exception ) throws SAXException
	{
		String message = "##############################\n" ;
		message += type + "\n" ;
		if( exception.getPublicId() != null )
			message += exception.getPublicId() + "\n" ;
		if( exception.getSystemId() != null )
			message += exception.getSystemId() + "\n" ;
		if( exception.getLineNumber() > 0 )
			message += "XML Line : " + exception.getLineNumber() + "\n" ;
		if( exception.getColumnNumber() > 0 )
			message += "Column : " + exception.getColumnNumber() + "\n" ;
		if( exception.getCause() != null )
			message += exception.getCause() + "\n" ;
		message += parseMessage( exception.getMessage() ) ;
		errorMessages.add( message ) ;
	}

	public String replacePatterns( String candidate )
	{
		return candidate.replace( XXMMSS , "XX:MM:SS pattern" ) ;
	}

	public String substitute( String candidate )
	{
		candidate = candidate.replaceAll( "\"" + TOML_URL + "\":" , "" ) ;
		return candidate.replaceAll( TOML_URL , "" ) ;
	}

	private String parseMessage( String exceptionMessage )
	{
		exceptionMessage = replacePatterns( exceptionMessage ) ;
		String[] messages = exceptionMessage.split( "[\\[\\]{}<>,']" ) ;
		String finalMessage = "" ;
		for( String message : messages )
		{
			if( !message.trim().equals( "" ) )
				finalMessage += substitute( message ) + "\n" ;
		}
		return finalMessage ;
	}
}
