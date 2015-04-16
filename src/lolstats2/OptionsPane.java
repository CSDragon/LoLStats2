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
import javax.swing.JComboBox;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.JButton;

/**
 * Handles the options for the stats panels
 *
 * @author Chris
 */
public class OptionsPane extends JPanel
{
    private JComboBox<String> winLossPicker;
    private JTextField creepScoreLow;
    private JTextField creepScoreHigh;
    private JButton applyOptions;
    private GraphsChartsPanel parent;
       
    /**
     * Creates the options pane
     * @param _parent the GraphsChartsPanel that made this OptionsPane
     */
    public OptionsPane(GraphsChartsPanel _parent)
    {
        parent = _parent;
        
        setLayout(null);
        
        winLossPicker = new JComboBox<>();
        winLossPicker.addItem("Both");
        winLossPicker.addItem("Wins");
        winLossPicker.addItem("Losses");
        
        winLossPicker.setSize(70, 20);
        winLossPicker.setLocation(30, 30);
        winLossPicker.setVisible(true);
        add(winLossPicker);
        
        creepScoreLow = new JTextField();
        creepScoreLow.setSize(25,20);
        creepScoreLow.setLocation(30, 80);
        add(creepScoreLow);
        
        creepScoreHigh = new JTextField();
        creepScoreHigh.setSize(25,20);
        creepScoreHigh.setLocation(71, 80);
        add(creepScoreHigh);
        
        applyOptions = new JButton("Apply");
        applyOptions.setSize(70,20);
        applyOptions.setLocation(30,120);
        applyOptions.addMouseListener(new MouseListener()
        {
            public void mousePressed(MouseEvent e) {}
            public void mouseReleased(MouseEvent e) {}
            
            public void mouseClicked(MouseEvent e) 
            {
                replaceCharts();
            }

            public void mouseEntered(MouseEvent e) {}
            public void mouseExited(MouseEvent e) {}
        });
        add(applyOptions);
        
        creepScoreLow.setText("4");
        creepScoreHigh.setText("-");
        
        
        setOpaque(false);
        
    }
    
    /**
     * Read the option fields and pass it to parent to refactor the graphs and charts
     */
    public void replaceCharts()
    {
        int wl = -1;
        int lowc;
        int highc;
        
        wl = winLossPicker.getSelectedIndex();
        
        
        try
        {
            lowc = Integer.valueOf(creepScoreLow.getText());
        }
        catch(Exception E)
        {
            lowc = -1;
        }
        
        try
        {
            highc = Integer.valueOf(creepScoreHigh.getText());
        }
        catch(Exception E)
        {
            highc = -1;
        }
        
        
        if(highc > lowc || (highc == -1))
            parent.replaceCharts(wl, lowc, highc);
        
        
    }
    
    
    /**
     * Paints the component
     */
    protected void paintComponent(Graphics g) 
    {
        Graphics2D g2 = (Graphics2D)g;
        g2.setColor(Color.decode("#CFCFCF"));
        g2.setFont(new Font(Font.SANS_SERIF, Font.PLAIN, 12));
        g2.drawString("Victory and/or Defeat", 30, 23);
        g2.drawString("CPM Limit (Support Game Filter)", 30, 73);
        g2.drawString("—", 57, 92);
    }
    
}
