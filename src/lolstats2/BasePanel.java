/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package lolstats2;

import java.awt.*;
import java.awt.event.*;
import java.awt.image.*;
import javax.swing.*;
import javax.imageio.ImageIO;
import java.io.IOException;
import java.io.File;

/**
 *
 * @author Chris
 */
public class BasePanel extends JPanel
{
    
    
    private BufferedImage topBar;
    private BufferedImage bottomBar;
    
    
    
    public BasePanel()
    {
        super();        
        try 
        {
            topBar = ImageIO.read(new File("assets/TopBar.png"));
            bottomBar = ImageIO.read(new File("assets/BottomBar.png"));
        } 
        catch (IOException e)
        {System.out.println("bluh" + e);}
        
        setSize(640,480);
        
        
    }
 

    @Override
    public void paint(Graphics g) 
    {
        super.paint(g); 
        
        Graphics2D g2 = (Graphics2D)g;
        

        g2.setColor(Color.decode("#DEE4EA")); 
		g2.fillRect(0, 0, 640, 480);
        
        
        g2.drawImage(topBar, 0, 0, null);
        g2.drawImage(bottomBar,0,459,null);
        
    }

    
    
    
    public static void main(String args[])
    {
        int ike = 0x002B167F;
        int offset = 50;
        for(int i = 0; i<10; i++)
        {
            System.out.println("00"+(Integer.toHexString(ike+((i+offset)*0x280))).toUpperCase()+" 0000000F");
        }
    }
    
    
}
