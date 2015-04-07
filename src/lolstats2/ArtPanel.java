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
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;
import javax.swing.JComponent;

/**
 *
 * @author Chris
 */
public class ArtPanel extends JComponent
{
	private BufferedImage CenterArt;
    
    public ArtPanel()
    {
        try 
        {
            CenterArt = ImageIO.read(new File("assets/QuinnArt.png"));
        } 
        catch (IOException e)
        {System.out.println("bluh" + e);}
    }
    
    
    @Override
    protected void paintComponent(Graphics g) 
    {
        Graphics2D g2 = (Graphics2D)g;

        g2.drawImage(CenterArt, 0, 0, null);

    }
    
}
