package om.dramaMessage;

/**
 * Messages that start with "0:", "1:" or "2:".
 *
 * @see DramaMessage
 * @author M.Folger@roe.ac.uk
 */
public class CompletionMessage extends AbstractMessage {


  /**
   * Can take one of the following values.
   * 0		success
   * 1		error
   * 2		error
   */
  public int status = -1;

  
  /**
   * Can take one of the following values.
   * "obeyw" 
   * "cancel"
   * "set" 
   * "get" 
   * "cntr"
   * "mget"
   * "??"
   */
  public String type         = "";

  public String taskName     = "";

  /**
   * Can take one of the following values.
   *
   * "init"
   * "load"
   * "run"
   * "DUMP"
   * "pause"
   * "stop"
   * "abort"
   * "breakPoint"
   * "cancelStop"
   * "clear"
   * "continue"
   * "next"
   * "test"
   * "setPath"
   * "setVerbose"
   * "target"
   * "exit"
   */
  public String action       = "";

  /**
   * Probably not used.
   */
  public String reply = "";
  public int actionIndex  = 0;

  public String toString() {

    StringBuffer result = new StringBuffer("CompletionMessage\n");
  
    result.append  ("  Status:         " + status);
    switch(status) {
      case -1: result.append(" (undefined)\n"); break;
      case  0: result.append(" (success)\n");   break;
      case  1: result.append(" (error)\n");     break;
      case  2: result.append(" (error)\n");     break;
      default: result.append(" (unknown)\n");   break;
    }
    
    result.append("  Type:           " + type + "\n");
    result.append("  Task Name:      " + taskName + "\n");
    result.append("  Action:         " + action + "\n");
    result.append("  Reply:          " + reply + "\n\n");
    
    return result.toString();
  }

  /**
   * Converts the message back into the string in has been created from (for debugging)
   */
  public String toMsgString() {
    if((reply != null) && !reply.equals(""))
      return "" + status + ": " + type + " " + taskName + " " + action + " " + reply;

    else
      return "" + status + ": " + type + " " + taskName + " " + action;
  }

}


