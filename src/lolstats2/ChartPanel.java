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
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;
import javax.swing.JPanel;
import javax.swing.JTable;

/**
 * Draws the charts
 * 
 * @author Chris
 */
public class ChartPanel  extends JPanel
{
    private BufferedImage selectBar;
    private int[] intpm;
    private int[] intTotal;
    private double[] doublepm;
    private double[] doubleTotal;
    private boolean doubleSwitch;

    /**
     * Create a new int-based chart
     * @param _intpm The per-minute column
     * @param _intTotal  the total column
     */
    public ChartPanel(int[] _intpm, int[] _intTotal)
    {
        intpm = _intpm;
        intTotal = _intTotal;
        
        try 
        {
            selectBar = ImageIO.read(new File("assets/charts.png"));
        } 
        catch (IOException e){}
        setLayout(null);
        setOpaque(false);
        
        doubleSwitch = false;
    }
    
    /**
     * Create a new double-based chart
     * @param _doublepm the per-minute column
     * @param _doubleTotal the total column
     */
    public ChartPanel(double[] _doublepm, double[] _doubleTotal)
    {
        doublepm = _doublepm;
        doubleTotal = _doubleTotal;
        
        try 
        {
            selectBar = ImageIO.read(new File("assets/charts.png"));
        } 
        catch (IOException e){}
        setLayout(null);
        setOpaque(false);
        
        doubleSwitch = true;
    }
        
    
    /**
     * Draws the chart
     * 
     * @param g Graphics
     */
    protected void paintComponent(Graphics g) 
    {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D)g;
        g2.drawImage(selectBar, 0, 0, null);
        
        g2.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_LCD_HRGB);
        g2.setColor(Color.decode("#CFCFCF"));
        g2.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 12));
        
        g2.drawString("Time", 92, 32);
        g2.drawString("Per Min", 133, 32);
        g2.drawString("Total", 200, 32);
        
        g2.drawString("Time", 379, 32);
        g2.drawString("Per Min", 419, 32);
        g2.drawString("Total", 483, 32);
        
        int length;
        if(!doubleSwitch)
            length = intpm.length;
        else
            length = doublepm.length;
        
        for(int i = 0; i<18 && i < length; i++)
        {
            g2.drawString(Integer.toString(i), 96, (i*16)+48);
            if(!doubleSwitch)
            {
                g2.drawString(Integer.toString(intpm[i]), 140, (i*16)+48);
                g2.drawString(Integer.toString(intTotal[i]), 194, (i*16)+48);
            }

            else
            {
                g2.drawString(Double.toString(doublepm[i]), 140, (i*16)+48);
                g2.drawString(Double.toString(doubleTotal[i]), 194, (i*16)+48);
            }
        }

        for(int i = 0; i<18 && i < length-18; i++)
        {
            g2.drawString(Integer.toString(i+18), 379, (i*16)+48);
            if(!doubleSwitch)
            {
                g2.drawString(Integer.toString(intpm[i+18]), 423, (i*16)+48);
                g2.drawString(Integer.toString(intTotal[i+18]), 477, (i*16)+48);
            }

            else
            {
                g2.drawString(Double.toString(doublepm[i+18]), 423, (i*16)+48);
                g2.drawString(Double.toString(doubleTotal[i+18]), 477, (i*16)+48);
            }
        }
    }
    
}
