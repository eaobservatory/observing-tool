package om.dramaMessage;

import java.util.StringTokenizer;
import java.util.Vector;

/**
 * This class provides the static method parse for parsing drama messages.
 * 
 * @see DramaMessage
 * @see CompletionMessage
 * @author M.Folger@roe.ac.uk
 */
public class MessageParser {

  /**
   * Parsing method.
   */
  public static AbstractMessage parse(String string) throws MessageParseException {


    AbstractMessage message = null;

    Vector tokenVector = new Vector();
    String [] token;

    StringTokenizer st = new StringTokenizer(string, ": @", true);
    
    int i = 0;
    while (st.hasMoreTokens()) {
      tokenVector.addElement(st.nextToken());
    }

    token = new String[tokenVector.size()];    
    tokenVector.copyInto(token);


    if(token[0].equals("msg") || token[0].equals("err")) { // drama message
      if(!token[1].equals(":")) throw new MessageParseException("':' expected after \"" + token[0] + "\".");
      if(!token[2].equals(" ")) throw new MessageParseException("' ' expected after \"" + token[0] + ":\".");

      message = new DramaMessage();
      
      if(token[3].equals("PARA")) {
        
        if(token[5].equals("INST")) {
	  ((DramaMessage)message).type  = DramaMessage.INSTRUMENT_TASK;
	  ((DramaMessage)message).name  = token[7];
	  for(i = 10; i < token.length; i++)
	    ((DramaMessage)message).value += token[i];
	}
	else {
	  if(token[5].equals("DHS")) {
	    ((DramaMessage)message).type  = DramaMessage.DHS_TASK;
	    ((DramaMessage)message).name  = token[7];
	    for(i = 10; i < token.length; i++)
	      ((DramaMessage)message).value += token[i];
	  }
	  else {
            ((DramaMessage)message).type = DramaMessage.OOS_TASK;
	    ((DramaMessage)message).name  = token[5];
	    for(i = 8; i < token.length; i++)
	      ((DramaMessage)message).value += token[i];

	  }
	}
      }
      else {
	if(token[0].equals("msg")) {
          ((DramaMessage)message).type = DramaMessage.MESSAGE;
	}
	else {
          ((DramaMessage)message).type = DramaMessage.ERROR;
	}

        StringBuffer stringBuffer = new StringBuffer();
        for(i = 3; i < token.length; i++) {
          stringBuffer.append(token[i]);
        }

        ((DramaMessage)message).value = stringBuffer.toString();
      }
      
      return message;
    }
    
    try {
      int status = Integer.parseInt(token[0]);

      if(!token[1].equals(":"))
        throw new MessageParseException("':' expected after \"" + token[0] + "\".");
      
      if(!token[2].equals(" "))
        throw new MessageParseException("' ' expected after \"" + token[0] + token[1] + "\".");
	

      message = new CompletionMessage();
      
      ((CompletionMessage)message).status = status;
      
      
      if(token[3].equals("obeyw")) {
        ((CompletionMessage)message).type = "obeyw";
      }
      else {
        throw new MessageParseException("Unexpected message type: \"" + token[3] + "\"");
      }

      ((CompletionMessage)message).taskName = token[5];
      ((CompletionMessage)message).action   = token[7];

      if(token.length > 9) {
        for(i = 9; i < token.length; i++)
	  ((CompletionMessage)message).reply += token[i];
      }

      return message;
    }
    catch(NumberFormatException e) { }
    
    
    if(token[0].equals("EXECLIST")) {
      
      return null;
    }


    throw new MessageParseException("Unexpected message, starts with \"" + token[0] + "\"");
  
  }
  
  
  /**
   * Offline testing of messages.
   *
   * The main method can to be used to test messages offline by using the class as a command line application.
   */
  public static void main(String [] args) {
    if(args.length < 1) {
      System.err.println("Usage: MessageParser \"<drama message string>\"");
      System.exit(1);
    }
    
    AbstractMessage msg = null;
    
    
    try {
      msg = (new MessageParser()).parse(args[0]);
    }
    catch(MessageParseException e) {
      System.out.println(e);
    }

    System.out.println("msg is of type : " + msg.getClass().getName());

  }
}


