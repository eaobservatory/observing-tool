// Copyright 1997 Association for Universities for Research in Astronomy, Inc.,
// Observatory Control System, Gemini Telescopes Project.
// See the file COPYRIGHT for complete details.
//
// $Id$
//
package gemini.util;

import java.net.URLEncoder;

/**
 * A simple CGI script query string encoder.  It can be used to
 * incrementally build a query string that is properly encoded in
 * x-www-form-urlencoded format.
 *
 * @author Elliotte Rusty Harold (JAVA Network Programming)
 */
public class QueryString
{
   String query;

public QueryString()
{
   query = "";
}

public QueryString(Object value)
{
   query = URLEncoder.encode(value.toString());
}

public QueryString(Object name, Object value)
{
   query = URLEncoder.encode(name.toString()) + "=" +
           URLEncoder.encode(value.toString());
}

public void
add(Object name, Object value)
{
   if (!query.equals("")) {
      query += "&";
   }
   query += URLEncoder.encode(name.toString()) + "=" +
            URLEncoder.encode(value.toString());
}

public String
toString()
{
   return query;
}

}
