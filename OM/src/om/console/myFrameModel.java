package om.console;
import om.dramaSocket.*;
import java.util.*;
import java.lang.*;

/** class myFrameModel is about to have a "model" for a sequeneFrame object for a rmi call.
    Ideally, there should not be any swing compoents in it, these swing compoents are not
    the trouble makes in a rmi call on the same type of hosts. In other words, this class
    only contains "good" attributes which do not casue any problem in RMI calls

    @version 1.0 1st Feb. 2000
    @author M.Tan@roe.ac.uk
*/
final public class myFrameModel implements java.io.Serializable
{

    public myFrameModel ()
    {
    }

  public void setInstrument(String i) {instrument=i;}
  public void setObservation(String o) {observation=o;}

  public void setStatusPanel(Vector j) {status=j;}
  //public void setItemsShown (itemsShown item) {items=item;}
  //public void setSequencePanel(sequencePanel seq) {sDisplay=seq;}
  public void setDhsPanel (Vector d) {dhs=d;}
  public void setUpperPanel (Vector u) {upper=u;}
  public void setMenuBar(Vector m) {menu=m;}

  public String getInstrument() {return instrument;}
  public String getObservation() {return observation;}
  //public sendCmds getSendComands () {return comSent;}
  public Vector getStatusPanel() {return status;}
  //public itemsShown getItemsShown () {return items;}
  //public sequencePanel getSequencePanel() {return sDisplay;}
  public Vector getDhsPanel () {return dhs;}
  public Vector getUpperPanel () {return upper;}
  public Vector getMenuBar() {return menu;}

  private String instrument, observation;
  private Vector dhs;
  private Vector upper;
  //private itemsShown items;
  //private sequencePanel sDisplay;
  private Vector status;
  private Vector menu;
  //private int sPanelIndex;
  
}
