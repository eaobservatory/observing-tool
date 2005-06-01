
package gemini.sp;

import java.util.Vector;

public interface SpTranslatable {

    /**
      * Classes that implement this method must provide a translate method.
      * Used to convert XML into sequences
      */
    public void translate( Vector sequence ) throws SpTranslationNotSupportedException;

}
