/*==============================================================*/
/*                                                              */
/*                UK Astronomy Technology Centre                */
/*                 Royal Observatory, Edinburgh                 */
/*                 Joint Astronomy Centre, Hilo                 */
/*                   Copyright (c) PPARC 1999                   */
/*                                                              */
/*==============================================================*/

package orac.util;

import java.util.*;
import orac.util.LookUpTable;

/**
 * This implements the parsing of instrument config information
 * @author Alan Bridger, UKATC
 * @version 0.9
 */
public class InstCfg
{
    private String keyword;
    private String value;
    
    // Helper shorthand method to match attributes
    public static boolean matchAttr( InstCfg info, String attr ) {
	return info.getKeyword().equalsIgnoreCase( attr );
    }
    
    /**
     * The constructor splits the given String block into a keyword and
     * "value string". The value string may be a single value or arrays.
     */
    public InstCfg (String infoBlock)
    {
	StringTokenizer st = new StringTokenizer (infoBlock, "=");
	keyword = st.nextToken().trim();
	value = st.nextToken().trim();
    }
    
    /**
     * Get the keyword for this config block
     */   
    public String getKeyword () {
	return keyword;
    }

    /**
     * Get the value for this block
     */
    public String getValue () {
	return _clean(value);
    }

    /**
     * Return this block's value as an  array
     */
    public String[] getValueAsArray () {
	
	StringTokenizer st = new StringTokenizer (value, ",");
	String[] result = new String[st.countTokens()];
	int i =0;
	while (st.hasMoreTokens()) {
	    result[i++] = _clean(st.nextToken());
	}
	return result;
    }

    /**
     * Return this block's values as a 2D array
     */
    public String[][] getValueAs2DArray () {
	
	String temp;
	
	// try to tokenize with "{" or "}" as delimiters. This should give us
	// the "rows"
	StringTokenizer st = new StringTokenizer (value, "{}");
	int size = st.countTokens();
	String[] rows = new String[size];
	int i = 0;
	while (st.hasMoreTokens()) {
	    temp = st.nextToken().trim();
	    if (temp.length() != 0) rows[i++] = temp;
	}
	int numRows = i;
	
	// now split each "row". Encode as strings.
	st = new StringTokenizer (rows[0], ",");
	int numCols;
	String[][] result = new String[numRows][];
	
	for (i=0; i<numRows; i++) {
	    st = new StringTokenizer (rows[i], ",");
	    numCols = st.countTokens();
	    result[i] = new String[numCols];
	    int j = 0;
	    while (st.hasMoreTokens()) {
		temp = st.nextToken().trim();
		try {
		  result[i][j++] = _clean(temp);
		}
		catch(ArrayIndexOutOfBoundsException e) {
                  System.out.println("    ArrayIndexOutOfBoundsException occured with i = " + i + " and j = " + j);
		}
	    }
	}
	return result;
    }
    
    /**
     * Return this block's value as a Vector 
     */   
    public Vector getValueAsVector () {
	StringTokenizer st = new StringTokenizer (value, ",");
	Vector result = new Vector();
	int i =0;
	while (st.hasMoreTokens()) {
	    result.addElement (_clean(st.nextToken()));
	}
	return result;
    }

    /**
     * Return this block's value as a LookUpTable
     */
    public LookUpTable getValueAsLUT () {
	String temp;
	
	// try to tokenize with "{" or "}" as delimiters. This should give us
	// the "rows"
	StringTokenizer st = new StringTokenizer (value, "{}");
	Vector rows = new Vector();
	while (st.hasMoreTokens()) {
	    temp = st.nextToken().trim();
	    if (temp.length() != 0) rows.addElement (temp);
	}
	
	int numRows = rows.size();
	
	LookUpTable result = new LookUpTable ();
	
	for (int i=0; i<numRows; i++) {
	    st = new StringTokenizer ((String) rows.elementAt(i), ",");
	    Vector row = new Vector();
	    while (st.hasMoreTokens()) {
		row.addElement (_clean(st.nextToken()));
	    }
	    result.addRow(row);
	}
	return result;
    }
    
    // "Clean" a string, i.e. remove {}" characters and trim it
    private String _clean (String string) {
	String retstr;
	retstr = string.replace('{',' ');
	retstr = retstr.replace('}',' ');
	retstr = retstr.replace('\"',' ');
	retstr = retstr.trim();
	return retstr;
    }

}









