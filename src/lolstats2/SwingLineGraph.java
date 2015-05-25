package lolstats2;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.RenderingHints;
import java.awt.Stroke;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import javax.imageio.ImageIO;
import javax.swing.*;

/**
 * Creates a line graph in Swing
 * Using some code form StackExchange User "Hovecraft Full of Eels" http://stackoverflow.com/a/8693635/4373567
 * 
 * @author Chris
 */
public class SwingLineGraph extends JPanel 
{
    private int yMax;
    private int edgeOffset = 30;
    private int tickSize = 6;
    private int yTickCount;
    private int maxX = 36;

    private int[] intScores;
    private double[] doubleScores;
    private boolean doubleFlag;
    private int yScaleNum;
    private BufferedImage graphBkg;
    
    private Color lineColor = Color.decode("0xB8B800");
    private Color pointColor = Color.decode("0x963232");
    private Color goldTrim = Color.decode("0xdcb64a");
    private Color whiteText = Color.decode("0xCFCFCF");
    
    /**
     * Creates a graph using ints.
     * 
     * @param _intScores The points we will be graphing, as ints
     * @param byNum The Y Scale of the graph
     */
    public SwingLineGraph(int[] _intScores, int byNum) 
    {
        //Load in the art assets
        try 
        {
            graphBkg = ImageIO.read(new File("assets/graphbkg.png"));
        } 
        catch (IOException e){}

       intScores = _intScores;
       yScaleNum = byNum;

       if (intScores.length < maxX)
           maxX = intScores.length;

       int maxVal = 0;
       for(int i = 0; i<maxX; i++)
       {
           if (intScores[i] > maxVal)
               maxVal = intScores[i];
       }

       yMax = ((int)Math.ceil(maxVal * 1.2))/yScaleNum * yScaleNum;
       yTickCount = yMax / yScaleNum;

       this.setOpaque(false);

       doubleFlag = false;
    }

    /**
     * Creates a graph using doubles
     * 
     * @param _doubleScores The points we will graph, as doubles
     * @param byNum The Y Scale of the graph
     */
    public SwingLineGraph(double[] _doubleScores, int byNum) 
    {
        try 
        {
            graphBkg = ImageIO.read(new File("assets/graphbkg.png"));
        } 
        catch (IOException e){}
        
        doubleScores = _doubleScores;
        yScaleNum = byNum;


        if (doubleScores.length < maxX)
            maxX = doubleScores.length;

        double maxVal = 0;
        for(int i = 0; i<maxX; i++)
        {
            if (doubleScores[i] > maxVal)
                maxVal = doubleScores[i];
        }

        yMax = ((int)Math.ceil(maxVal * 1.2))/yScaleNum * yScaleNum;
        yTickCount = yMax / yScaleNum;

        this.setOpaque(false);

        doubleFlag = true;
    }
   

    /**
     * Paints the graph
     * 
     * @param g Graphics sent by the parent object.
     */
    protected void paintComponent(Graphics g) 
    {
        Graphics2D g2 = (Graphics2D)g;
        
        g2.drawImage(graphBkg, 0, 0, null);
        
        g2.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 12));
        g2.setColor(goldTrim);
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        double xScale = ((double) getWidth() - 2 * edgeOffset) / (maxX - 1);
        double yScale = ((double) getHeight() - 2 * edgeOffset) / (yMax);

        List<Point> graphPoints = new ArrayList<Point>();

        for (int i = 0; i < maxX; i++) 
        {
            int x1 = (int) (i * xScale + edgeOffset);
            int y1;
            if(!doubleFlag)
                y1 = (int) ((yMax - (intScores[i])) * yScale + edgeOffset);
            else
                y1 = (int) ((yMax - (doubleScores[i])) * yScale + edgeOffset);
            graphPoints.add(new Point(x1, y1));
        }

        // create x and y axes 
        g2.drawLine(edgeOffset, getHeight() - edgeOffset, edgeOffset, edgeOffset);
        g2.drawLine(edgeOffset, getHeight() - edgeOffset, getWidth() - edgeOffset, getHeight() - edgeOffset);

        // create hatch marks for y axis. 
        for (int i = 0; i < yTickCount; i++) 
        {
            g2.setColor(goldTrim);
            int x0 = edgeOffset;
            int x1 = edgeOffset + tickSize;
            int y0 = getHeight() - (((i + 1) * (getHeight() - edgeOffset * 2)) / yTickCount + edgeOffset);
            int y1 = y0;
            g2.drawLine(x0, y0, x1, y1);

            g2.setColor(whiteText);
            g2.drawString(Integer.toString((i+1)*yScaleNum), 7, y0+6);
        }

        // and for x axis
        for (int i = 0; i < maxX - 1; i++) 
        {
            g2.setColor(goldTrim);
            int x0 = (int) ((i+1) * xScale + edgeOffset);
            int x1 = x0;
            int y0 = getHeight() - edgeOffset;
            int y1;
            if((i+1)%5 == 0)
                y1 = y0 - tickSize*2;
            else
                y1 = y0 - tickSize;
            g2.drawLine(x0, y0, x1, y1);

            g2.setColor(whiteText);
            if((i+1)%5 == 0)
            {
                if(i==4)
                    g2.drawString(Integer.toString(i+1), x0-3, y0+12);
                else
                {
                    g2.drawString(Integer.toString(i+1), x0-6, y0+12);
                }
            }
        }

        Stroke oldStroke = g2.getStroke();
        g2.setColor(whiteText);
        g2.setStroke(new BasicStroke(3f));
        for (int i = 0; i < graphPoints.size() - 1; i++) 
        {
            int x1 = graphPoints.get(i).x;
            int y1 = graphPoints.get(i).y;
            int x2 = graphPoints.get(i + 1).x;
            int y2 = graphPoints.get(i + 1).y;
            g2.drawLine(x1, y1, x2, y2);         
        }

        g2.setStroke(oldStroke);      
        g2.setColor(goldTrim);
        for (int i = 0; i < graphPoints.size(); i++) 
        {
            int x = graphPoints.get(i).x - tickSize / 2;
            int y = graphPoints.get(i).y - tickSize / 2;;
            int ovalW = tickSize;
            int ovalH = tickSize;
            g2.fillOval(x, y, ovalW, ovalH);
        }
       
    }

}