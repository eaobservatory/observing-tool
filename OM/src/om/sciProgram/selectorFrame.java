package om.sciProgram;

import om.console.*;
import java.util.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.event.*;
import javax.swing.border.*;
import java.rmi.registry.*;
import java.rmi.Remote.*;
import om.util.OracColor;

/** final class selectorFrame is a seperate frame
    to select/fetch a science program and send out an observation

    @version 1.0 1st June 1999
    @version 1.1 1st March 2000 adding more buttons in this panel required by gsw
    @author M.Tan@roe.ac.uk
*/
final class selectorFrame extends JFrame implements KeyListener,ActionListener
{
/** public public selectorFrame(Vector v,spConnector sp,String name) is
    the constructor. The class has only one constructor so far.
    a few thing are done during the construction. They are mainly
    about to set GUI bits and setting up listeners
    It also starts up a RMI registry, defalut port number 4321

    @param  Vector v,spConnector sp,String name
    @return none
    @throws RemoteException
*/
  public selectorFrame(Vector v,spConnector sp,String name)
  {
      _spConnector=sp;
      data=v;
      
      // reversing order of elements in data vector
      for(int i = 0; i < data.size() - 1; i++) {
	data.insertElementAt(data.lastElement(), i);
        data.removeElementAt(data.size() - 1);
      }
       
      setTitle("ORAC-OM Science Programs for user '"+name+"'");
      setSize(480, 380);
      setLocation(400,200);
      addWindowListener(new WindowAdapter()
      {  public void windowClosing(WindowEvent e)
            {
            }

      	    public void windowSizeChanged(WindowEvent e)
            {
                 validate();
            }

       } );

      Container contentPane = getContentPane();

      menuSele menu=new menuSele(sp,this);
      setJMenuBar(menu);

      list=new JList(data);
      scrollPane = new JScrollPane(list);
      list.setCellRenderer(new CustomCellRenderer());

      JPanel progPanel=new JPanel(new BorderLayout());

      Border border=BorderFactory.createMatteBorder(2, 2,
            2,2, Color.white);
      progPanel.setBorder(new TitledBorder(border,
      "Program List",0,0,
      new Font("Roman",Font.BOLD,12),Color.black));

      progPanel.add(scrollPane);

      Fetch=new JButton("Fetch");
      Fetch.setMargin(new Insets(5,30,5,30));
      Fetch.setBackground(OracColor.green);

      Stop=new JButton("Stop the fetch");
      Stop.setMargin(new Insets(5,30,5,30));
      Stop.setBackground(OracColor.green);

      Refresh=new JButton("Refresh");
      Refresh.setMargin(new Insets(5,30,5,30));
      Refresh.setBackground(OracColor.green);

      JPanel buttonPanel=new JPanel(new GridLayout(3,1));

      buttonPanel.add(Fetch);
      buttonPanel.add(Stop);
      buttonPanel.add(Refresh);

      progPanel.add(buttonPanel,"South");

      JPanel keyPanel=new JPanel();
      Border border1=BorderFactory.createMatteBorder(4, 4,
            4,4, Color.lightGray);
      keyPanel.setBorder(border1);

      keyPanel.add(new JLabel("Key:"));

      Insets m=new Insets(5,5,5,5);
      key.setBorder(new EmptyBorder(m));
      key.setColumns(10);
      key.setText(name+"-0");
      keyPanel.add(key);

      progPanel.add(keyPanel,"North");

      contentPane.add(progPanel,"West");

      program=new programTree(menu);
      contentPane.add(program);

      statusBar=new myLabel("Current Status:  No science program fetched");
      statusBar.setBorder(border1);
      statusBar.setHorizontalAlignment(0);
      statusBar.setBounds(10,10,10,10);
      contentPane.add(statusBar,"South");

      Fetch.addActionListener(this);
      Stop.addActionListener(this);
      Refresh.addActionListener(this);
      list.addKeyListener(this);

      int rmiPort=Integer.parseInt(System.getProperty("RMIS_PORT"));
      try {
            //if(LocateRegistry.getRegistry(rmiPort).list())
              LocateRegistry.createRegistry(rmiPort);  //starting a rmi registry
      } catch(Exception e)
      {
        System.out.println("Fail to creat a RMI Registry due to" + e.toString());
      }

  }

  /**  public void actionPerformed(ActionEvent evt) is a public method
    to react button actions. The reaction is mainly about to fetch
    a science program

    @param ActionEvent
    @return  none
    @throws none

  */
  public void actionPerformed(ActionEvent evt)
  {
     Object source=evt.getSource();

     if(source==Fetch)
      startFetch();
     else if(source==Stop)
      stopFetch();
     else if(source==Refresh)
      getSpConnector().fetchListing();

  }

/**  public void stopFetch() is a public method
    to react a call from a menuSele object. The reaction is mainly about to stop
    fetching a science program

    @param ActionEvent
    @return  none
    @throws none

  */
  public void stopFetch()
  {

    if (_buttonFlush == null || !_buttonFlush.isAlive() ) {
      errorBox=new ErrorBox("You are not fetching a Science Program.");
      return;
    }

//     if(!_buttonFlush.isAlive())
//       return;
    
    _spConnector.stopAction();
    
    if(_buttonFlush.isAlive())
      _buttonFlush.stop();
    
    Fetch.setText("Fetch");
    Fetch.setForeground(Color.black);
    statusBar.setText("Current Status: fetching program has been interrupted");
  }

   /**  public void stopFLuch() is a public method
    to react a call from a spConnector object. The reaction is mainly about to stop
    flushing the button.

    @param ActionEvent
    @return  none
    @throws none

  */
  public void stopFlush()
  {
    Fetch.setText("Fetch");
    Fetch.setForeground(Color.black);
    _buttonFlush.stop();
  }

    /** public void keyReleased(KeyEvent evt) is a public method
    to react key input.

    @param KeyEvent
    @return  none
    @throws none

  */
  public void keyReleased(KeyEvent evt)
  {
    if(list.hasFocus())
    if(evt.getKeyChar()=='\n')
      startFetch();

  }

  public void keyPressed(KeyEvent evt) {}
  public void keyTyped(KeyEvent evt) {}

  public void reFreshSpList(Vector v)
  {

    list.setListData(v);
    data=v;

    // reversing order of elements in data vector
    for(int i = 0; i < data.size() - 1; i++) {
      data.insertElementAt(data.lastElement(), i);
      data.removeElementAt(data.size() - 1);
    }   

    program.removeTree();

  }
  public programTree getTree(){return program;}
  public myLabel getStatusBar(){return statusBar;}
  public JPasswordField getKey() {return key;}
  public spConnector getSpConnector() {return _spConnector;}


 /**private void startFetch () is a private method
    to start fetch a science program from orac ot via rmi

    @param ActionEvent
    @return  none
    @throws none
  */
  private void startFetch ()
  {
       int index=list.getSelectedIndex();

       if(index<0||index>data.size()-1)
       {
          errorBox=new ErrorBox("You have not selected a Science Program.");
          return;
       }

        if(_spConnector.fetchProg(data.elementAt(index).toString()))
        ;
        else
          return;

       if(Fetch.getText().equals("Fetch"))
        Fetch.setText("Waiting");
       else
        return;

       _buttonFlush=new buttonFlush(Fetch);
       _buttonFlush.start();
       statusBar.setText("Current Status: Fetching the science program:"+data.elementAt(index).toString());
  }

  private JButton Fetch,Stop,Refresh;
  private JScrollPane scrollPane;
  private JList list;
  private spConnector _spConnector;
  private Vector data;
  private buttonFlush _buttonFlush;
  private ErrorBox errorBox;
  private programTree program;
  private myLabel statusBar;
  private JPasswordField key=new JPasswordField("");
}
