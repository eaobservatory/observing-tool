// Copyright 1997 Association for Universities for Research in Astronomy, Inc.,
// Observatory Control System, Gemini Telescopes Project.
// See the file COPYRIGHT for complete details.
//
// $Id$
//
package gemini.sp;

import java.io.*;
import java.util.*;

/**
 * Write a Science Program/Plan to a given OutputStream in the Science
 * Program SGML format.  See SpInputSGML for the grammar that describes
 * the possible outputs of this class.
 *
 * <p>
 * <em>Note</em>, this class has been left public, but it is anticipated
 * that clients will primarily be interested in the <code>printDocument()</code>
 * method of SpRootItem.  That method creates and uses this class to print
 * a science program document describing the item.
 *
 * @see SpRootItem#printDocument
 */
public final class SpOutputSGML
{
   private static final String _INDENT = "   ";
   private PrintWriter  _pr;

/**
 * Construct with the OutputStream to which to write.
 */
public SpOutputSGML(OutputStream os)
{
   _pr = new PrintWriter(os, false);
}

/**
 * Print an item.
 */
public void
printItem(SpItem spItem, String tag, String indent)
{
   String name    = " name="    + spItem.name();
   String type    = " type="    + spItem.typeStr();
   String subtype = " subtype=" + spItem.subtypeStr();
   
   _pr.println(indent + "<" + tag + name + type + subtype + ">");
   printAttributes(spItem, indent + _INDENT);

   printChildren(spItem.children(), indent + _INDENT);
   _pr.println(indent + "</" + tag + ">");
   _pr.flush();
}

///**
// * Replace all occurances of the given character in the given string
// * with a backslash followed by the character.  For instance,
// * <tt> _quoteChar("abc'd'", '\'') </tt> returns
// * <tt> abc\'d\' </tt>
// */
//private String
//_quoteChar(String s, char c)
//{
//   String quotedStr = "";
//   int j=0;
//   for (int i=s.indexOf(c); i != -1; i=s.indexOf(c,j)) {
//      quotedStr += s.substring(j,i) + "\\" + c;
//      j=i+1;
//   }
//
//   return quotedStr + s.substring(j);
//}

//
// Quote the characters in the character array, and replace newlines
// with the two character sequence "\n".
//
private String
_quoteValue(String s, char[] ca)
{
   int       caLen = ca.length;
   StringBuffer sb = new StringBuffer(s);

   // Examine every character of the string, inserting quote characters
   // as necessary.
   for (int i=0; i<sb.length(); ++i) {
      char c = sb.charAt(i);

      if (c == '\n') {
         sb.insert(i, '\\');
         ++i;
         sb.setCharAt(i, 'n');
         continue;
      }

      for (int j=0; j<caLen; ++j) {
         if (c == ca[j]) {
            sb.insert(i, '\\');  // Insert the quote backslash
            ++i;                 // Move past the backslash
            break;
         }
      }
   }

   return sb.toString();
}

/**
 * Write the attributes of the given item.
 */
public void
printAttributes(SpItem spItem, String indent)
{
   SpAvTable avTab = spItem.getTable();
   Enumeration e = avTab.attributes();
   if (!e.hasMoreElements()) {
      return;
   }
   
   // These are the characters that will be quoted
   char[] ca = {'\\', '\'', '"'};

   while (e.hasMoreElements()) {

      // Get the attribute name and its values
      String attr = (String) e.nextElement();
      Vector    v = avTab.getAll(attr);
      if ((v == null) || (v.size() == 0)) {
         continue;   // no values so skip it for now ...
      }

      // Print the name and description
      String descr = avTab.getDescription(attr);
      descr = _quoteValue(descr, ca);
      _pr.println(indent + "<av name=" + attr + " descr=\"" + descr + "\">");
      
 
      // Print all the values of the attribute
      String common = indent + indent + "<val value=";
      for (int i=0; i<v.size(); ++i) {
         String value = avTab.get(attr, i);
         if (value == null) {
            _pr.println(common + "\"\">");
         } else {
            value = _quoteValue(value, ca);
            _pr.println(common + "\"" + value + "\">");
         }
      } 

      // No more values
      _pr.println(indent + "</av>");
   }
   _pr.flush();
}

/**
 * Write the children of the given item.
 */
public void
printChildren(Enumeration children, String indent)
{
   while (children.hasMoreElements()) {
      SpItem child = (SpItem) children.nextElement();
      print(child, indent);
   }
}


/**
 * Write the given item, presumably a Science Program root.
 *
 * @param spItem The item to print
 * @param indent
 *    A string, presumably containing whitespace, to print in front of
 *    each item.
 */
public void
print(SpItem spItem, String indent)
{

   // For now the tag is the same as the "type" attribute (i.e., its the
   // two letter type code).
   String tag = spItem.type().getType();
   printItem(spItem, tag, indent);
}

/**
 * Write the given item, presumably a Science Program root.
 */
public void
print(SpItem spItem)
{
   print(spItem, "");
}

/**
 * Write a program document given a generic root item.
 */
public void
printDocument(SpRootItem spRootItem)
{
   _pr.println("<spDocument>");

   // If this is a SpProg, then write its phase 1 item, if any.
   if (spRootItem instanceof SpProg) {
      SpPhase1 p1 = ((SpProg) spRootItem).getPhase1Item();
      if (p1 != null) {
         print(p1, "   ");
      }
   }

   print(spRootItem, "   ");

   _pr.println("</spDocument>");
   _pr.flush();
}

}

