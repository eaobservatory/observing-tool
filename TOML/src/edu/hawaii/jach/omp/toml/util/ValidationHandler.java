/**
 * Copyright (C) 2009 - 2010 Science and Technology Facilities Council.
 * All Rights Reserved.
 */

package edu.hawaii.jach.omp.toml.util ;

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

	String regexStart = "\"http://omp.jach.hawaii.edu/schema/TOML\":" ;

	public boolean handleEvent( ValidationEvent ve )
	{
		ValidationEventLocator vel = ve.getLocator() ;
		System.out.println( "\n" ) ;
		System.out.println( severity.values()[ ve.getSeverity() ] ) ;
		if( vel.getLineNumber() == -1 && vel.getColumnNumber() == -1 )
			nodeEvent( vel ) ;
		else
			textEvent( vel ) ;
		
		printMessage( ve.getMessage() ) ;

		return true ;
	}

	private void printMessage( String exceptionMessage )
	{
		System.out.println() ;
		String[] messages = exceptionMessage.split( "[,']" ) ;
		for( String message : messages )
			System.out.println( substitute( message ) ) ;
	}

	private void nodeEvent( ValidationEventLocator vel )
	{
		Node node = vel.getNode() ;
		Vector<Node> nodes = new Vector<Node>() ;
		while( node != null && node.getOwnerDocument() != null )
		{
			nodes.add( 0 , node ) ;
			node = node.getParentNode() ;
		}
		
		System.out.println( "Node : " ) ;
		String space = " " ;
		for( Node currentNode : nodes )
		{
			String name = currentNode.getNodeName() ;
			System.out.print( space + "+" + name ) ;
			System.out.print( "\n" ) ;
			space += " " ;
		}
	}

	private void textEvent( ValidationEventLocator vel )
	{
		System.out.println( "XML Line : " + vel.getLineNumber() ) ;
		System.out.println( "Column : " + vel.getColumnNumber() ) ;
	}

	public void error( SAXParseException exception ) throws SAXException
	{
		System.out.println( "\n" ) ;
		System.out.println( "Error" ) ;
		handleSAXParseException( exception ) ;
	}

	public void fatalError( SAXParseException exception ) throws SAXException
	{
		System.out.println( "Fatal Error" ) ;
		handleSAXParseException( exception ) ;
	}

	public void warning( SAXParseException exception ) throws SAXException
	{
		System.out.println( "Warning" ) ;
		handleSAXParseException( exception ) ;
	}

	private void handleSAXParseException( SAXParseException exception ) throws SAXException
	{
		if( exception.getPublicId() != null )
			System.out.println( exception.getPublicId() ) ;
		if( exception.getSystemId() != null )
			System.out.println( exception.getSystemId() ) ;
		if( exception.getLineNumber() > 0 )
			System.out.println( "XML Line : " + exception.getLineNumber() ) ;
		if( exception.getColumnNumber() > 0 )
			System.out.println( "Column : " + exception.getColumnNumber() ) ;
		if( exception.getCause() != null )
			System.out.println( exception.getCause() ) ;
		printMessage( exception.getMessage() ) ;
	}

	public String substitute( String candidate )
	{
		candidate = candidate.replaceAll( regexStart , "" ) ;
		return candidate ;
	}
}
