/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package lolstats2;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;
import javax.swing.JPanel;
/**
 *
 * @author Chris
 */
public class GraphsChartsPanel extends JPanel
{
    private JPanel optionsPane;
    private SwingLineGraph GPMGraph;
    private SwingLineGraph CPMGraph;
    private ChartPanel GPMChart;
    private ChartPanel CPMChart;
    private BufferedImage bluebkg;
    
    public GraphsChartsPanel(GoldAnalyst data)
    {
        try 
        {
            bluebkg = ImageIO.read(new File("assets/bluebkg.png"));
        } 
        catch (IOException e) {}
        
        
        setLayout(null);
        GPMGraph = new SwingLineGraph(data.getGPMPerMin(), 100);
        CPMGraph = new SwingLineGraph(data.getCPMPerMin(),   1);
        GPMChart = new ChartPanel(data.getGPMPerMin(), data.getTotalGoldPerMin());
        CPMChart = new ChartPanel(data.getCPMPerMin(), data.getTotalCreepsPerMin());
        
        //optionsPane.setLocation(0,0);
        //optionsPane.setSize(640,360);
        GPMGraph.setLocation(0,0);
        GPMGraph.setSize(640,360);
        CPMGraph.setLocation(0,0);
        CPMGraph.setSize(640,360);
        GPMChart.setLocation(0,0);
        GPMChart.setSize(640,360);
        CPMChart.setLocation(0,0);
        CPMChart.setSize(640,360);
        
        //add(optionsPane);
        add(GPMGraph);
        add(CPMGraph);
        add(GPMChart);
        add(CPMChart);
        
        //optionsPane.setVisible(false);
        GPMGraph.setVisible(true);
        CPMGraph.setVisible(false);
        GPMChart.setVisible(false);
        CPMChart.setVisible(false);
        
        setOpaque(false);
        
        
    }
    
    public void paneChanged(int paneNo)
    {
        if(paneNo == 0)
        {
            //optionsPane.setVisible(true);
            GPMGraph.setVisible(false);
            CPMGraph.setVisible(false);
            GPMChart.setVisible(false);
            CPMChart.setVisible(false);
        }
        else if(paneNo == 1)
        {
            //optionsPane.setVisible(false);
            GPMGraph.setVisible(true);
            CPMGraph.setVisible(false);
            GPMChart.setVisible(false);
            CPMChart.setVisible(false);
        }
        else if(paneNo == 2)
        {
            //optionsPane.setVisible(false);
            GPMGraph.setVisible(false);
            CPMGraph.setVisible(true);
            GPMChart.setVisible(false);
            CPMChart.setVisible(false);
        }
        else if(paneNo == 3)
        {
            //optionsPane.setVisible(false);
            GPMGraph.setVisible(false);
            CPMGraph.setVisible(false);
            GPMChart.setVisible(true);
            CPMChart.setVisible(false);
        }
        else if(paneNo == 4)
        {
            //optionsPane.setVisible(false);
            GPMGraph.setVisible(false);
            CPMGraph.setVisible(false);
            GPMChart.setVisible(false);
            CPMChart.setVisible(true);
        }
    }
    
    @Override
    protected void paintComponent(Graphics g) 
    {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D)g;
        g2.drawImage(bluebkg, 0, 0, null);
    }
    
}
