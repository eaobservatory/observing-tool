package om.dramaMessage;

/**
 * Exception is thrown when a message contains unexpected values or
 * cannot be parsed.
 * 
 * @author M.Folger@roe.ac.uk
 */
public class MessageParseException extends Exception {
  MessageParseException(String s) {
    super(s);
  }
}


