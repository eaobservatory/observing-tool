package om.dramaMessage;

/**
 * For messages beginning with "msg: " or "err: ".
 * 
 * @see CompletionMessage
 * @author M.Folger@roe.ac.uk
 */
public class DramaMessage extends AbstractMessage {

  /**
   * Task type: instrument task.
   * Messages beginning with "msg: PARA:INST@"
   */
  public static final int INSTRUMENT_TASK = 1;

  
  /**
   * Task type: dhs task.
   * Messages beginning with "msg: PARA:DHS@"
   */
  public static final int DHS_TASK = 2;
  
  
  /**
   * Task type: oos task.
   * Messages beginning with "msg: PARA:"
   */
  public static final int OOS_TASK = 3;
  
  /**
   * Plain text message.
   * Messages beginning with "msg: " followed by plain text
   */
  public static final int MESSAGE = 4;

  /**
   * Plain text message.
   * Messages beginning with "err: " followed by plain text
   */
  public static final int ERROR = 5;

  /**
   * Task type.
   */
  public int type     = 0;

  /**
   * Substring between "@" and "::" in message string.
   */
  public String name  = "";
  
  /**
   * Substring after "::" in message string.
   */
  public String value = "";

  /**
   * Pretty prints message.
   * This method has only ever been used for debugging.
   */
  public String toString() {
    StringBuffer result = new StringBuffer("DramaMessage\n");
  
    result.append  ("  Type:           " + type);
    switch(type) {
      case  0               : result.append(" (undefined)\n");       break;
      case  INSTRUMENT_TASK : result.append(" (INSTRUMENT_TASK)\n"); break;
      case  DHS_TASK        : result.append(" (DHS_TASK)\n");        break;
      case  OOS_TASK        : result.append(" (OOS_TASK)\n");        break;
      case  MESSAGE         : result.append(" (MESSAGE)\n");         break;
      case  ERROR           : result.append(" (ERROR)\n");           break;
      default: result.append(" (unknown)\n");         break;
    }
    
    result.append("  Name:           " + name + "\n");
    result.append("  Value:          " + value + "\n\n");

    return result.toString();
  }

  
  /**
   * Converts the message back into the string in has been created from (for debugging).
   */
  public String toMsgString() {
    String tmp = "";

    switch(type) {
      case INSTRUMENT_TASK : tmp = "msg: PARA:INST@"; break; 
      case DHS_TASK        : tmp = "msg: PARA:DHS@" ; break;
      case OOS_TASK        : tmp = "msg: PARA:"     ; break;
      case MESSAGE         : tmp = "msg: "          ; break;
      case ERROR           : tmp = "err: "          ; break;
    }

    if((name != null) && (!name.equals(""))) {
      // to make it resemble the original string exactly
      if(name.equals("EXECLIST") && ((value == null || value.equals(""))))
        return tmp + name;

      else
        return tmp + name + "::" + value;
    }
    else
      return tmp + value;
  }

}


