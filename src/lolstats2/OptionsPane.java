/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package lolstats2;

import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import javax.swing.JComboBox;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.JButton;

/**
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
        creepScoreLow.setLocation(30, 70);
        add(creepScoreLow);
        
        creepScoreHigh = new JTextField();
        creepScoreHigh.setSize(25,20);
        creepScoreHigh.setLocation(70, 70);
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
        
        setOpaque(false);
        
    }
    
    
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
}
