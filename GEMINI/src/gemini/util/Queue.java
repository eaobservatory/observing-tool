// Copyright 1997 Association for Universities for Research in Astronomy, Inc.,
// Observatory Control System, Gemini Telescopes Project.
// See the file COPYRIGHT for complete details.
//
// $Id$
//
/*
 * Gary Cornell and Cay S. Horstmann, Core Java (Book/CD-ROM)
 * Published By SunSoft Press/Prentice-Hall
 * Copyright (C) 1996 Sun Microsystems Inc.
 * All Rights Reserved. ISBN 0-13-565755-5
 *
 * Permission to use, copy, modify, and distribute this
 * software and its documentation for NON-COMMERCIAL purposes
 * and without fee is hereby granted provided that this
 * copyright notice appears in all copies. 
 * 
 * THE AUTHORS AND PUBLISHER MAKE NO REPRESENTATIONS OR 
 * WARRANTIES ABOUT THE SUITABILITY OF THE SOFTWARE, EITHER 
 * EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE 
 * IMPLIED WARRANTIES OF MERCHANTABILITY, FITNESS FOR A 
 * PARTICULAR PURPOSE, OR NON-INFRINGEMENT. THE AUTHORS
 * AND PUBLISHER SHALL NOT BE LIABLE FOR ANY DAMAGES SUFFERED 
 * BY LICENSEE AS A RESULT OF USING, MODIFYING OR DISTRIBUTING 
 * THIS SOFTWARE OR ITS DERIVATIVES.
 */
package gemini.util;

import java.util.Enumeration;

/**
 * A simple queue class modified from the following author.
 * @version 1.00 07 Feb 1996 
 * @author Cay Horstmann
 */
public class Queue
{
   private LinkedList _data = new LinkedList();

public void
append(Object o)
{
   _data.append(o);
}

public Object
remove()
{
   return _data.remove();
}

public int
size()
{
   return _data.size();
}

public Enumeration
elements()
{
   return _data.elements();
}

public void
clear()
{
   _data.clear();
}

}
