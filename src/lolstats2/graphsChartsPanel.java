/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package lolstats2;

import javax.swing.JPanel;
/**
 *
 * @author Chris
 */
public class graphsChartsPanel 
{
    private JPanel OptionsPane;
    private SwingLineGraph GPMGraph;
    private SwingLineGraph CPMGraph;
    private JPanel GPMChart;
    private JPanel CPMChart;
    
    public graphsChartsPanel(goldAnalyst data)
    {
        GPMGraph = new SwingLineGraph(data.getGPMPerMin(), 100);
        CPMGraph = new SwingLineGraph(data.getCPMPerMin(),  1);
    }
}
