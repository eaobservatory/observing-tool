package jsky.app.ot.util; 

/**
 * A class that describes an anchor in an HTML document.  This class groups
 * the text that is displayed inside the anchor with the anchor string
 * itself.
 */
public class HTMLAnchor
{
   public String anchorString;
   public String text;

public HTMLAnchor() {}

public HTMLAnchor(String anchorString, String text)
{
   this.anchorString = anchorString;
   this.text         = text;
}

}
