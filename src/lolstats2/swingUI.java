/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package lolstats2;

import javax.swing.BoxLayout;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.*;
import java.awt.*;
import static java.awt.Frame.ICONIFIED;
import static java.awt.Frame.MAXIMIZED_BOTH;
import static java.awt.Frame.NORMAL;
import java.awt.event.*;
import javax.swing.UIManager;

/**
*
 * @author Chris
 */
public class swingUI
{
    private static JFrame appFrame;
    private static GUI guiPanel;
    private static JButton nameButton;
    private static JTextField nameTextField;
    private static JLabel statusLabel;
    
    private TrayIcon trayIcon;
    private SystemTray tray;
    
    
    public swingUI()
    {
        appFrame = new JFrame("LoL Gold Data");
      
        
        setUpTray();
       
        
        // Display GUI
        appFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		appFrame.setResizable(false);
        
        guiPanel = new GUI();
        
        appFrame.add(guiPanel);
        
		appFrame.pack();
        //appFrame.setSize(646,508);
        

        appFrame.setVisible(true);
        
    }
    
    public void setUpTray()
    {
        if(SystemTray.isSupported())
        {
            tray=SystemTray.getSystemTray();

            Image trayImage=Toolkit.getDefaultToolkit().getImage("assets/traytemp.png");
            
            trayIcon=new TrayIcon(trayImage, "SystemTray Demo", null);
            trayIcon.addMouseListener(new MouseListener()
            {
                public void mousePressed(MouseEvent e) {}
                public void mouseReleased(MouseEvent e) {}
                public void mouseClicked(MouseEvent e) 
                {
                    appFrame.setVisible(true);
                    appFrame.setExtendedState(JFrame.NORMAL);
                }
                public void mouseEntered(MouseEvent e) {}
                public void mouseExited(MouseEvent e) {}
            });
            
            trayIcon.setImageAutoSize(true);
        }
        
        else
        {
            //System.out.println("system tray not supported");
        }
        
        
        appFrame.addWindowStateListener((WindowEvent e) -> 
        {
            if(e.getNewState()==ICONIFIED) 
            {
                try
                {
                    tray.add(trayIcon);
                    appFrame.setVisible(false);
                }
                catch (AWTException ex) {}
            }
            
            if(e.getNewState()==NORMAL)
            {
                tray.remove(trayIcon);
                appFrame.setVisible(true);
            }
        });
        
        appFrame.setIconImage(Toolkit.getDefaultToolkit().getImage("assets/traytemp.png"));
    }
    
   
    public static GUI getGui()
    {
        return guiPanel;
    }
    
    public static void updateStatus(String s)
    {
        statusLabel.setText(s);
        
        appFrame.repaint();
    }
}


/* Goes where the null is in the trayIcon



        PopupMenu trayMenu=new PopupMenu();
            
            MenuItem openItem=new MenuItem("Open");
            openItem.addActionListener((ActionEvent e) -> 
            {
                appFrame.setVisible(true);
                appFrame.setExtendedState(JFrame.NORMAL);
            });
            trayMenu.add(openItem);
            
            MenuItem exitItem=new MenuItem("Exit");
            exitItem.addActionListener((ActionEvent e) -> 
            {
                System.exit(0);
            });
            trayMenu.add(exitItem);
*/