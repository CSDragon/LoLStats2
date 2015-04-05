/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package lolstats2;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;
import javax.swing.JPanel;

/**
 *
 * @author Chris
 */


    

public class statSelectPane  extends JPanel
{
    private BufferedImage selectBar;
    private BufferedImage selected;
    private int selectedLoc = -3;
    private graphsChartsPanel target = null;
    
    public statSelectPane()
    {
        try 
        {
            selectBar = ImageIO.read(new File("assets/statsPaneSelectBarNoDetails.png"));
            selected = ImageIO.read(new File("assets/statsPaneSelected.png"));
        } 
        catch (IOException e){}
        
        addMouseListener(new MouseListener()
        {
            public void mousePressed(MouseEvent e) 
            {
                int x = e.getX();
                if(x < 104)
                {
                    selectedLoc = -3;
                    target.paneChanged(0);
                }
                else if(x > 108 && x < 211)
                {
                    selectedLoc = 102;
                    target.paneChanged(1);
                }
                else if(x > 215 && x < 318)
                {
                    selectedLoc = 208;
                    target.paneChanged(2);
                }
                else if(x > 322 && x < 424)
                {
                    selectedLoc = 314;
                    target.paneChanged(3);
                }
                else if(x > 428 && x < 531)
                {
                    selectedLoc = 421;
                    target.paneChanged(4);
                }
               /* else if(x > 535 && x < 638)
                {
                    selectedLoc = 527;
                    target.paneChanged(5);
                }*/
                repaint(); 
            }
            
            public void mouseReleased(MouseEvent e) {}
            public void mouseClicked(MouseEvent e) {}
            public void mouseEntered(MouseEvent e) {}
            public void mouseExited(MouseEvent e) {}
        });
        
        
        setSize(640,39);
        
    }
    
    public void setTarget(graphsChartsPanel _target)
    {
        target = _target;
    }
    
    
    
    @Override
    protected void paintComponent(Graphics g) 
    {
        Graphics2D g2 = (Graphics2D)g;
        
        g2.drawImage(selectBar, 0, 0, null);
        g2.drawImage(selected,selectedLoc,0,null);
        
        g2.setColor(Color.decode("#000000"));
        g2.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 12));
		g2.drawString("Options", 30, 22);
        g2.drawString("GPM Graph", 127, 22);
        g2.drawString("CPM Graph", 235, 22);
        g2.drawString("GPM Chart", 342, 22);
        g2.drawString("CPM Chart", 450, 22);
		g2.drawString("Match Details", 550, 22);
        
    }
   
}


