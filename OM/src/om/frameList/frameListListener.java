package om.frameList;

import java.util.*;

/** frameListListener class is not a java bean but an auxiliary interface
    used by a java bean

    @version 1.0 April 28, 2000
    @author M.Tan@roe.ac.uk
*/

public interface frameListListener extends EventListener
{
  public void frameListAdded (frameListEvent f);
  public void frameListRemoved (frameListEvent f);
  public void connectedInstrumentChanged(frameListEvent f);
}
