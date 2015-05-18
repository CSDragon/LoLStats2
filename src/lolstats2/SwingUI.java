/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package lolstats2;

import javax.swing.JFrame;
import java.awt.*;
import static java.awt.Frame.ICONIFIED;
import static java.awt.Frame.NORMAL;
import java.awt.event.*;

/**
 * A class that acts as the container for the GUI, manages the JFrame, and manages the trayIcon
 * 
 * @author Chris
 */
public class SwingUI
{
    private JFrame appFrame;
    private GUI guiPanel;
    private TrayIcon trayIcon;
    private SystemTray tray;
    private int lastX;
    private int lastY;
    
    /**
     * Creates the SwingUI object.
     * 
     */
    public SwingUI()
    {
        appFrame = new JFrame("LoL Gold Data");
      
        
        //For clarity and reusability purposes, this was moved to its own method.
        setUpTray();
       
        
        // Display GUI
        appFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		appFrame.setResizable(false);
        appFrame.setUndecorated(true);
        
        MotionPanel mp = new MotionPanel(appFrame);
        mp.setLocation(0,0);
        mp.setSize(640, 25);
        mp.setPreferredSize(new Dimension(640,15));
        appFrame.add(mp);
        
        guiPanel = new GUI();
        appFrame.add(guiPanel);

        appFrame.pack();
        appFrame.setLocationRelativeTo(null);

        appFrame.setVisible(true);
        guiPanel.requestFocus();
        
    }
    
    /**
     * If minimize-to-tray is available on the desktop configuration, minimise to tray.
     * Final to avoid overridable method in constructor problems
     * 
     */
    public final void setUpTray()
    {
        if(SystemTray.isSupported())
        {
            tray=SystemTray.getSystemTray();

            Image trayImage=Toolkit.getDefaultToolkit().getImage("assets/traytemp.png");
            
            trayIcon=new TrayIcon(trayImage, "LoL GPM Stats", null);
            trayIcon.addMouseListener(new MouseListener()
            {
                public void mousePressed(MouseEvent e) {}
                public void mouseReleased(MouseEvent e) {}
                public void mouseClicked(MouseEvent e) 
                {
                    appFrame.setLocation(lastX, lastY);
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
                    lastX = appFrame.getX();
                    lastY = appFrame.getY();
                    appFrame.setVisible(false);
                }
                catch (AWTException ex) {}
            }
            
            if(e.getNewState()==NORMAL)
            {
                tray.remove(trayIcon);
                appFrame.setLocation(lastX, lastY);
                appFrame.setVisible(true);
            }
        });
        
        appFrame.setIconImage(Toolkit.getDefaultToolkit().getImage("assets/traytemp.png"));
    }
    
   
    /**
     * 
     * @return guiPanel
     */
    public GUI getGui()
    {
        return guiPanel;
    }
    
    
    

}


/* Goes where the null is in the trayIcon if we want a popup menu instead



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
