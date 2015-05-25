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
import java.text.DecimalFormat;
import javax.swing.JComboBox;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.JButton;
import org.jdatepicker.impl.*;
import java.util.Properties;
import javax.swing.JCheckBox;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;


/**
 * Handles the options for the stats panels
 *
 * @author Chris
 */
public class OptionsPane extends JPanel
{
    private DecimalFormat df;
    
    private JComboBox<String> winLossPicker;
    private JButton applyOptions;
    private GraphsChartsPanel parent;
    private JDatePickerImpl startTime;
    private JDatePickerImpl endTime;
    private JCheckBox topBox;
    private JCheckBox jungleBox;
    private JCheckBox midBox;
    private JCheckBox adcBox;
    private JCheckBox supportBox;
    private JComboBox<String> mapPicker;
    
    private BufferedImage bgImage;
       
    /**
     * Creates the options pane
     * @param _parent the GraphsChartsPanel that made this OptionsPane
     */
    public OptionsPane(GraphsChartsPanel _parent)
    {
        parent = _parent;
        df = new DecimalFormat("#.00");
        setLayout(null);
        
        //Date Pickers
        startTime = createDatePicker(true);
        endTime = createDatePicker(false);
        startTime.setLocation(83, 76);
        endTime.setLocation(188, 76);
        add(startTime);
        add(endTime);
        
        
        //Win-Loss Picker
        winLossPicker = new JComboBox<>();
        winLossPicker.addItem("Both");
        winLossPicker.addItem("Victory");
        winLossPicker.addItem("Defeat");
        
        winLossPicker.setSize(70, 20);
        winLossPicker.setLocation(83, 158);
        winLossPicker.setVisible(true);
        add(winLossPicker);
          
        
        //Map Picker
        mapPicker = new JComboBox<>();
        mapPicker.addItem("Summoner's Rift");
        mapPicker.addItem("Howling Abyss");
        mapPicker.addItem("Twisted Treeline");
        mapPicker.addItem("Dominion");
        mapPicker.addItem("All");
        
        mapPicker.setSize(130, 20);
        mapPicker.setLocation(83, 241);
        mapPicker.setVisible(true);
        add(mapPicker);        
        
        //Role Checkboxes
        topBox = new JCheckBox();
        topBox.setLocation(467, 50);
        topBox.setSize(50, 20);
        topBox.setPreferredSize(new Dimension(240,100));
        topBox.setOpaque(false);
        topBox.setSelected(true);
        add(topBox);
        
        jungleBox = new JCheckBox();
        jungleBox.setLocation(467, 70);
        jungleBox.setSize(50, 20);
        jungleBox.setPreferredSize(new Dimension(240,100));
        jungleBox.setOpaque(false);
        jungleBox.setSelected(true);
        add(jungleBox);
        
        midBox = new JCheckBox();
        midBox.setLocation(467, 90);
        midBox.setSize(50, 20);
        midBox.setPreferredSize(new Dimension(240,100));
        midBox.setOpaque(false);
        midBox.setSelected(true);
        add(midBox);
        
        adcBox = new JCheckBox();
        adcBox.setLocation(467, 110);
        adcBox.setSize(50, 20);
        adcBox.setPreferredSize(new Dimension(240,100));
        adcBox.setOpaque(false);
        adcBox.setSelected(true);
        add(adcBox);
        
        supportBox = new JCheckBox();
        supportBox.setLocation(467, 130);
        supportBox.setSize(20, 20);
        supportBox.setPreferredSize(new Dimension(240,100));
        supportBox.setOpaque(false);
        add(supportBox);
        
        
        //Apply Button
        applyOptions = new JButton("Apply");
        applyOptions.setSize(70,20);
        applyOptions.setLocation(465, 247);
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
        
        
        try 
        {
            bgImage = ImageIO.read(new File("assets/OptionsBkg.png"));
        } 
        catch (IOException e) {}
        
        
        setOpaque(false);
        
        
        
    }
    
    /**
     * Read the option fields and pass it to parent to refactor the graphs and charts
     */
    public void replaceCharts()
    {
        long timeLow = getTime(startTime,true);
        long timeHigh = getTime(endTime,false);
        
        parent.replaceCharts(winLossPicker.getSelectedIndex(), timeLow, timeHigh, topBox.isSelected(), jungleBox.isSelected(), midBox.isSelected(), adcBox.isSelected(), supportBox.isSelected(), mapPicker.getSelectedIndex());
    }
    
    /**
     * Formats a JDatePickerImpl so it works in our system
     * @param startOrEnd a bool determining if we're working with the start or end time picker.
     * @return A fresh new JDatePickerImpl primed and ready to go
     */
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
        dpfield.setFont(new Font("Sans-Serif", Font.BOLD, 12));
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
    
    /**
     * Takes the time string from a JDatePickerImpl and turns it into a time long
     * @param target the JDatePickerImpl we're reading
     * @param startOrStop a bool telling if this is for the start or end time.
     * @return a long for the time in the picker
     */
    long getTime(JDatePickerImpl target, boolean startOrStop)
    {
        try
        {
            String s = target.getJFormattedTextField().getText();
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
    
    /**
     * Paints the component
     */
    protected void paintComponent(Graphics g) 
    {
        Graphics2D g2 = (Graphics2D)g;
    
        g2.drawImage(bgImage, 31, 0, null);

        g2.setColor(Color.decode("#CFCFCF"));
        g2.setFont(new Font(Font.SANS_SERIF, Font.PLAIN, 12));
        g2.drawString("Victory/Defeat", 83, 152);
        g2.drawString("Date", 83, 69);
        g2.drawString("—", 172, 89);
        g2.drawString("Map", 83, 235);
        
        g2.drawString("Top",490,64);
        g2.drawString("Jungle",490,84);
        g2.drawString("Mid",490,104);
        g2.drawString("ADC",490,124);
        g2.drawString("Support",490,144);
        
        g2.drawString("Role Accuracy", 462, 179);
        g2.drawString(df.format(parent.getData().getAccuracyRating()*100)+"%", 482, 193);
        
        super.paintComponent(g);
    }
    
    
    
  
}
