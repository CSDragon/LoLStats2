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
import javax.swing.Timer;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

/**
 * Contains the Graphs, Charts and options.
 * 
 * @author Chris
 */
public class GraphsChartsPanel extends JPanel
{
    private String name;
    private String region;
    private OptionsPane optionsPane;
    private SwingLineGraph GPMGraph;
    private SwingLineGraph CPMGraph;
    private ChartPanel GPMChart;
    private ChartPanel CPMChart;
    private BufferedImage bluebkg;
    private BufferedImage gold;
    private BufferedImage minion;
    private int minipic = 1;
    private GoldAnalyst data;

    private Timer updateTimer;
    
    /**
     * Creates the panel and sets up the contents
     * @param _data the GoldAnalyst we're turning into graphs
     * @param n the name of the summoner
     * @param r the region of the summoner
     */
    public GraphsChartsPanel(GoldAnalyst _data, String n, String r)
    {
        data = _data;
        name = n;
        region = r;
        
        try 
        {
            bluebkg = ImageIO.read(new File("assets/bluebkg.png"));
            gold = ImageIO.read(new File("assets/gold.png"));
            minion = ImageIO.read(new File("assets/minion.png"));
        } 
        catch (IOException e) {}
        
        
        setLayout(null);
        optionsPane = new OptionsPane(this);
        GPMGraph = new SwingLineGraph(data.getGPMPerMin(), 100);
        CPMGraph = new SwingLineGraph(data.getCPMPerMin(),   1);
        GPMChart = new ChartPanel(data.getGPMPerMin(), data.getTotalGoldPerMin());
        CPMChart = new ChartPanel(data.getCPMPerMin(), data.getTotalCreepsPerMin());
        
        optionsPane.setLocation(0,0);
        optionsPane.setSize(640,360);
        GPMGraph.setLocation(0,0);
        GPMGraph.setSize(640,360);
        CPMGraph.setLocation(0,0);
        CPMGraph.setSize(640,360);
        GPMChart.setLocation(0,0);
        GPMChart.setSize(640,360);
        CPMChart.setLocation(0,0);
        CPMChart.setSize(640,360);
        
        add(optionsPane);
        add(GPMGraph);
        add(CPMGraph);
        add(GPMChart);
        add(CPMChart);
        
        optionsPane.setVisible(false);
        GPMGraph.setVisible(true);
        CPMGraph.setVisible(false);
        GPMChart.setVisible(false);
        CPMChart.setVisible(false);
        
        setOpaque(false);
        
        
        
        updateTimer = new Timer(21600000, new ActionListener()
        {
            public void actionPerformed(ActionEvent e)
            {
                update();
            }
        });
        updateTimer.setInitialDelay(2000);
        updateTimer.start();
    }
    
    /**
     * Changes the active chart or graph.
     * @param paneNo The number we want. 0: Options, 1: GPMGraph, 2: CPMGraph, 3: GPMChart, 4:CPMChart
     */
    public void paneChanged(int paneNo)
    {
        minipic = paneNo;
        
        if(paneNo == 0)
        {
            optionsPane.setVisible(true);
            GPMGraph.setVisible(false);
            CPMGraph.setVisible(false);
            GPMChart.setVisible(false);
            CPMChart.setVisible(false);
        }
        else if(paneNo == 1)
        {
            optionsPane.setVisible(false);
            GPMGraph.setVisible(true);
            CPMGraph.setVisible(false);
            GPMChart.setVisible(false);
            CPMChart.setVisible(false);
        }
        else if(paneNo == 2)
        {
            optionsPane.setVisible(false);
            GPMGraph.setVisible(false);
            CPMGraph.setVisible(true);
            GPMChart.setVisible(false);
            CPMChart.setVisible(false);
        }
        else if(paneNo == 3)
        {
            optionsPane.setVisible(false);
            GPMGraph.setVisible(false);
            CPMGraph.setVisible(false);
            GPMChart.setVisible(true);
            CPMChart.setVisible(false);
        }
        else if(paneNo == 4)
        {
            optionsPane.setVisible(false);
            GPMGraph.setVisible(false);
            CPMGraph.setVisible(false);
            GPMChart.setVisible(false);
            CPMChart.setVisible(true);
        }
    }
    
    /**
     * Draws the gold and minion icons, as well as this pane's children
     * 
     * @param g Graphics
     */
    protected void paintComponent(Graphics g) 
    {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D)g;
        g2.drawImage(bluebkg, 0, 0, null);
        
        if(minipic == 1 || minipic == 3)
            g2.drawImage(gold, -10, 300, null);
        else if(minipic == 2 || minipic == 4)
            g2.drawImage(minion, -25, 280, null);
    }
    
    /**
     * Refactors data to have different specifications
     * @param wl both, win or loss
     * @param lowcpm low cpm bound
     * @param highcpm high cpm bound
     */
    public void replaceCharts(int wl, int lowcpm, int highcpm, long lowTime, long highTime)
    {
        data.recast(wl, lowcpm, highcpm, lowTime, highTime);
        
        remove(GPMGraph);
        remove(CPMGraph);
        remove(GPMChart);
        remove(CPMChart);
        
        GPMGraph = new SwingLineGraph(data.getGPMPerMin(), 100);
        CPMGraph = new SwingLineGraph(data.getCPMPerMin(),   1);
        GPMChart = new ChartPanel(data.getGPMPerMin(), data.getTotalGoldPerMin());
        CPMChart = new ChartPanel(data.getCPMPerMin(), data.getTotalCreepsPerMin());
        
        GPMGraph.setLocation(0,0);
        GPMGraph.setSize(640,360);
        CPMGraph.setLocation(0,0);
        CPMGraph.setSize(640,360);
        GPMChart.setLocation(0,0);
        GPMChart.setSize(640,360);
        CPMChart.setLocation(0,0);
        CPMChart.setSize(640,360);

        GPMGraph.setVisible(false);
        CPMGraph.setVisible(false);
        GPMChart.setVisible(false);
        CPMChart.setVisible(false);
        
        add(GPMGraph);
        add(CPMGraph);
        add(GPMChart);
        add(CPMChart);

    }
    
    /**
     * fetches new matches for the currently displayed summoner
     */
    public void update()
    {
        data = LolStats2.run(name, region);
        
        this.setVisible(false);
        
        remove(GPMGraph);
        remove(CPMGraph);
        remove(GPMChart);
        remove(CPMChart);
        
        GPMGraph = new SwingLineGraph(data.getGPMPerMin(), 100);
        CPMGraph = new SwingLineGraph(data.getCPMPerMin(),   1);
        GPMChart = new ChartPanel(data.getGPMPerMin(), data.getTotalGoldPerMin());
        CPMChart = new ChartPanel(data.getCPMPerMin(), data.getTotalCreepsPerMin());
        
        GPMGraph.setLocation(0,0);
        GPMGraph.setSize(640,360);
        CPMGraph.setLocation(0,0);
        CPMGraph.setSize(640,360);
        GPMChart.setLocation(0,0);
        GPMChart.setSize(640,360);
        CPMChart.setLocation(0,0);
        CPMChart.setSize(640,360);

        GPMGraph.setVisible(false);
        CPMGraph.setVisible(false);
        GPMChart.setVisible(false);
        CPMChart.setVisible(false);
        
        add(GPMGraph);
        add(CPMGraph);
        add(GPMChart);
        add(CPMChart);
        
        paneChanged(minipic);
        
        this.setVisible(true);
        
        ((GUI)this.getParent()).resetStatus();
        
        
    }
    
    /**
     * Stops the update timer
     */
    public void stopTimer()
    {
        updateTimer.stop();
    }
    
    /**
     * Gets region
     * @return region
     */
    public String getRegion()
    {
        return region;
    }
    
}
