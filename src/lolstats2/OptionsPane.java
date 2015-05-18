/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package lolstats2;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.sql.Date;
import javax.swing.JComboBox;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.JButton;
import org.jdatepicker.impl.*;
import java.util.Properties;


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
    private JDatePickerImpl startTime;
    private JDatePickerImpl endTime;
       
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
        
    
        startTime = createDatePicker(true);
        endTime = createDatePicker(false);
        startTime.setLocation(30, 130);
        endTime.setLocation(135, 130);
        add(startTime);
        add(endTime);
        
        applyOptions = new JButton("Apply");
        applyOptions.setSize(70,20);
        applyOptions.setLocation(30,170);
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
        int creepLow;
        int creepHigh;
        long timeLow;
        long timeHigh;
        
        wl = winLossPicker.getSelectedIndex();
        
        try
        {
            creepLow = Integer.valueOf(creepScoreLow.getText());
        }
        catch(Exception E)
        {
            creepLow = -1;
        }
        
        try
        {
            creepHigh = Integer.valueOf(creepScoreHigh.getText());
        }
        catch(Exception E)
        {
            creepHigh = -1;
        }
        
        timeLow = getTime(startTime.getJFormattedTextField().getText(),true);
        timeHigh = getTime(endTime.getJFormattedTextField().getText(),false);
        
        if(creepHigh > creepLow || (creepHigh == -1))
            parent.replaceCharts(wl, creepLow, creepHigh, timeLow, timeHigh);
        
        
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
        g2.drawString("Date Filter", 30, 125);
        g2.drawString("—", 118, 142);
    }
    
    
    JDatePickerImpl createDatePicker(boolean startOrEnd)
    {
        Properties p = new Properties();
        p.put("text.today", "Today");
        p.put("text.month", "Month");
        p.put("text.year", "Year");
        
        UtilDateModel model = new UtilDateModel();
        JDatePanelImpl datePanel = new JDatePanelImpl(model, p);
        JDatePickerImpl datePicker;
        if(parent.getRegion().equals("NA"))
            datePicker = new JDatePickerImpl(datePanel, new DateFormatNA());
        else if(parent.getRegion().equals("KR"))
            datePicker = new JDatePickerImpl(datePanel, new DateFormatKOR());
        else
            datePicker= new JDatePickerImpl(datePanel, new DateFormatEU());
        
        datePicker.setLayout(null);
        
        JTextField dpfield = (JTextField)datePicker.getComponent(0);
        JButton dpbutton = (JButton)datePicker.getComponent(1);
        
        dpfield.setPreferredSize(new Dimension(40,20));
        dpfield.setSize(70,20);
        dpfield.setLocation(0,0);
        if(startOrEnd)
            dpfield.setText("Start Date");
        else
            dpfield.setText("End Date");
        
        dpbutton.setPreferredSize(new Dimension(40,20));
        dpbutton.setSize(15,20);
        dpbutton.setLocation(70,0);
        dpbutton.setText("");
        
        
        datePicker.setPreferredSize(new Dimension(90,20));
        datePicker.setSize(100, 20);
        datePicker.setOpaque(false);
        
        return datePicker;
    }
    
    
    long getTime(String s, boolean startOrStop)
    {
        try
        {
            int month;
            int day;
            int year;
            
            if(parent.getRegion().equals("NA"))
            {
                day = Integer.valueOf(s.substring(3,5));
                month = Integer.valueOf(s.substring(0,2));
                year = Integer.valueOf(s.substring(6));
            }
            
            else if(parent.getRegion().equals("KR"))
            {
                day = Integer.valueOf(s.substring(8));
                month = Integer.valueOf(s.substring(5,7));
                year = Integer.valueOf(s.substring(0,4));
            }
                        
            else
            {
                day = Integer.valueOf(s.substring(0,2));
                month = Integer.valueOf(s.substring(3,5));
                year = Integer.valueOf(s.substring(6));
            }
            
            return(Date.valueOf(year+"-"+month+"-"+day).getTime());
            
            
        }
        catch(Exception e)
        {
            if(startOrStop)
                return -1;
            else
                return Long.MAX_VALUE;
        }
    }
    
}
