package fr.et3.polytech.JavaIHM;

import java.awt.*;
import java.awt.event.*;
import java.io.File;

import javax.swing.*;
 
public class Loading extends JFrame
 {
 
  JProgressBar barre_progression;
  static final int MINIMUM=0;
  static final int MAXIMUM=100;
  private static JPanel pnl;
  private static JFileChooser filechooser;

  public Loading( )
 {
 	 // Créer un objet de la Barre de progression
     barre_progression = new JProgressBar( );
 
     // Définir la valeur initiale de la barre de progression
     barre_progression.setMinimum(MINIMUM);
     // Définir la valeur maximale de la barre de progression
     barre_progression.setMaximum(MAXIMUM);
     
     JLabel label= new JLabel("WELCOME TO OUR GAME");
     // Créer un JPanel et ajouter la barre de progression dans le JPanel
     pnl  =new JPanel();
     JPanel pnl0 =new JPanel();
     //pnl.setLayout(new BoxLayout(label, BoxLayout.Y_AXIS));
     pnl0.add(label);
     pnl.setLayout(new BoxLayout(pnl, BoxLayout.Y_AXIS));
     pnl.add(pnl0);
     
     ImageIcon image = new ImageIcon("soccer-online.jpeg");
     JLabel label1 = new JLabel("", image, JLabel.CENTER);
     label1.setLayout(new FlowLayout());
     pnl.add( label1);
     
     JLabel labele= new JLabel("THE GAME IS LOADING...");
     pnl.add(labele);
     pnl.add(barre_progression);
     
     JButton b = new JButton("Browse...");
     b.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				filechooser = new JFileChooser();
				int returnvalue = filechooser.showOpenDialog(null);
				if (returnvalue == JFileChooser.APPROVE_OPTION) {
					File selectedFile = filechooser.getSelectedFile();
					System.out.println(CatchDatas.getNbSavings(selectedFile.getName()));
					
				}
			}
		});
     
     pnl.add(b);
     setSize(650,500);
     setLocationRelativeTo(null);
     
     
     
     setTitle("SOCCER STATS");
     setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
     setContentPane(pnl);
     pack( );
     setVisible(true);
 
   
     
     for (int i = MINIMUM; i <= MAXIMUM; i++)
    {
       final int percent=i;
      try
      {
         SwingUtilities.invokeLater(new Runnable( ) {
             public void run( ) {
               updateBar(percent);
               if(percent>33)
            	   labele.setText("PRAYING TO GOD IT WILL WORK");
               if(percent>66)
            	   labele.setText("HOPING THE BUGS WILL FLY AWAY");
               if(percent>90)
            	   labele.setText("THANK YOU FOR WAITING!!!");
             }
         });
         java.lang.Thread.sleep(100);
       } catch (InterruptedException e) {;}
     } 
     
     dispose();
     
    
 
  }
  
  public static String getFileChooser() {
	  return filechooser.getSelectedFile().toString();
  }
 
  public void updateBar(int newValue)
  {
    barre_progression.setValue(newValue);
  }
 
  
}
