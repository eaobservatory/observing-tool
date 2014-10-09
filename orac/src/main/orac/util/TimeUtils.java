/*
 * Copyright (C) 2002-2008 Science and Technology Facilities Council.
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

package orac.util ;

import java.util.Calendar ;
import java.util.Date ;
import java.util.TimeZone ;
import java.text.SimpleDateFormat ;
import java.text.ParsePosition ;

/**
 * Time Utilities.
 */
public class TimeUtils
{
	private final static String dateFormat = "yyyy-MM-dd" ;
	private final static String timeFormat = "HH:mm:ss" ;
	private final static String isoFormat = "yyyy-MM-dd'T'HH:mm:ss" ;
	private final static SimpleDateFormat sdf = new SimpleDateFormat( dateFormat ) ;
	private final static SimpleDateFormat stf = new SimpleDateFormat( timeFormat ) ;
	private final static SimpleDateFormat sif = new SimpleDateFormat( isoFormat ) ;
	private final static TimeZone UTC = TimeZone.getTimeZone( "UTC" ) ;
	private final static TimeZone HST = TimeZone.getTimeZone( "HST" ) ;

	/**
	 * Constructor.
	 */
	public TimeUtils(){}

	/**
	 * Get the current local date.
	 * @return    Current date as yyyy-mm-dd
	 */
	public static String getLocalDate()
	{
		sdf.setTimeZone( HST ) ;
		return sdf.format( Calendar.getInstance( HST ).getTime() ) ;
	}
	
	/**
	 * Get the current UTC date.
	 * @return    Current date as yyyy-mm-dd
	 */
	public static String getUTCDate()
	{
		sdf.setTimeZone( UTC ) ;
		return sdf.format( Calendar.getInstance( UTC ).getTime() ) ;
	}
	
	/**
	 * Get the current UTC date.
	 * @return    Current UTC date as java.util.Date
	 */
	public static Date getCurrentUTCDate()
	{
		return Calendar.getInstance( UTC ).getTime() ;
	}

	/** 
	 * Get the current local time.
	 *
	 * @return    Current time in HH:MM:SS format.
	 */
	public static String getLocalTime()
	{
		stf.setTimeZone( HST ) ;
		return stf.format( Calendar.getInstance( HST ).getTime() ) ;
	}
	
	/** 
	 * Get the current UTC time.
	 *
	 * @return    Current time in HH:MM:SS format.
	 */
	public static String getUTCTime()
	{
		stf.setTimeZone( UTC ) ;
		return stf.format( Calendar.getInstance( UTC ).getTime() ) ;
	}

	/**
	 * Checks whether the date/time <code>String</code> is
	 * in ISO format.
	 * ISO format defined as YYYY-MM-DD'T'HH:MM:SS
	 *
	 * @param dateString    Date/Time string
	 * @return              <code>true</code> if valid ; <code>false</code> otherwise.
	 */
	public static boolean isValidDate( String dateString )
	{
		Date date = parseDate( dateString ) ;
		return ( date != null ) ;
	}

	/**
	 * Convert a date/time string to the corresponding UTC.
	 *
	 * @param isoDate    Local Date/Time <code>String</code> in ISO format
	 * @return           UTC in ISO format.
	 */
	public static String convertLocalISODatetoUTC( String isoDate )
	{
		// Parse the date to get the local Date
		Date date = parseDate( isoDate ) ;
		String convertedDate = null ;
		if( date != null )
		{
			TimeZone tz = TimeZone.getDefault() ;
			Calendar cal = toCalendar( isoDate , tz ) ;
			
			int hh = cal.get( Calendar.HOUR_OF_DAY ) ;
			int mm = cal.get( Calendar.MINUTE ) ;
			int ss = cal.get( Calendar.SECOND ) ;
			
			int millsecondsOfDay = ( hh * 3600 + mm * 60 + ss ) * 1000 ;
			
			int tzOffset = tz.getOffset( cal.get( Calendar.ERA ) , cal.get( Calendar.YEAR ) , cal.get( Calendar.MONTH ) , cal.get( Calendar.DAY_OF_MONTH ) , cal.get( Calendar.DAY_OF_WEEK ) , millsecondsOfDay ) ;
			cal.set( Calendar.MILLISECOND , cal.get( Calendar.MILLISECOND ) - tzOffset ) ;

			convertedDate = sif.format( cal.getTime() ) ;
		}
		return convertedDate ;
	}

	/**
	 * Convert an ISO format date/time string into a <code>Calendar</code>
	 * object.
	 * @see java.util.Calendar
	 *
	 * @param isoDateTime     Date/Time <code>String</code> is ISO format.
	 * @return                Corrsponding <code>Calendar</code> object
	 */
	public static Calendar toCalendar( String isoDateTime )
	{
		return toCalendar( isoDateTime , UTC ) ;	
	}
	
	/**
	 * Convert an ISO format date/time string into a <code>Calendar</code>
	 * object.
	 * @see java.util.Calendar
	 *
	 * @param isoDateTime     Date/Time <code>String</code> is ISO format.
	 * @param timeZone        <code>TimeZone</code>.
	 * @return                Corrsponding <code>Calendar</code> object
	 */
	public static Calendar toCalendar( String isoDateTime , TimeZone timeZone )
	{
		Calendar cal = null ;
		if( isValidDate( isoDateTime ) )
		{
			cal = Calendar.getInstance( timeZone ) ;
			String[] split = isoDateTime.split( "[-T:]" ) ;
			
			int yyyy = Integer.parseInt( split[ 0 ] ) ;
			int mn = Integer.parseInt( split[ 1 ] ) ;
			int dd = Integer.parseInt( split[ 2 ] ) ;
			int hh = Integer.parseInt( split[ 3 ] ) ;
			int mm = Integer.parseInt( split[ 4 ] ) ;
			int ss = Integer.parseInt( split[ 5 ] ) ;
			cal.set( Calendar.YEAR , yyyy ) ;
			cal.set( Calendar.MONTH , mn - 1 ) ; // months are 0 based
			cal.set( Calendar.DAY_OF_MONTH , dd ) ;
			cal.set( Calendar.HOUR_OF_DAY , hh ) ;
			cal.set( Calendar.MINUTE , mm ) ;
			cal.set( Calendar.SECOND , ss ) ;
		}
		return cal ;
	}

	/**
	 * Convert a date/time string to a <code>Date</code> object.
	 * date/time must be in ISO format.
	 * @see java.util.Date
	 * @param dateString       Date/Time string to convert
	 * @return                 Corresponding <code>Date</code> object or
	 *                         <code>null</code> on failure.
	 */
	private static Date parseDate( String dateString )
	{
		ParsePosition p = new ParsePosition( 0 ) ;
		sif.setLenient( false ) ;
		Date date = sif.parse( dateString , p ) ;
		return date ;
	}
}
