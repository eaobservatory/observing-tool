package om.console;

/** public interface messageInterface is a interface
    it defines an interface for taking messages from a socket
    This interface is realized by a number of "Panel" classes 

    @version 1.0 1st June 1999
    @author M.Tan@roe.ac.uk
*/
public interface messageInterface
{

  public void messageString(String name, String value);
  public void close();

}


