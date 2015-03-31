package lolstats2;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.RenderingHints;
import java.awt.Stroke;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import javax.swing.*;

@SuppressWarnings("serial")
public class SwingLineGraph extends JPanel 
{
   private int MAX_SCORE = 2000;
   private int PREF_W = 640;
   private int PREF_H = 400;
   private int BORDER_GAP = 30;
   private Color GRAPH_COLOR = Color.decode(Integer.toString(0xB8B800));
   private Color GRAPH_POINT_COLOR = new Color(150, 50, 50, 180);
   private Stroke GRAPH_STROKE = new BasicStroke(3f);
   private int GRAPH_POINT_WIDTH = 6;
   private static int Y_HATCH_CNT = 20;
   private int[] scores;
   private int maxVal;
   private int maxX = 36;

   public SwingLineGraph(int[] _scores) 
   {
      scores = _scores;
      
      if (scores.length < maxX)
          maxX = scores.length;
      
      for(int i = 0; i<maxX; i++)
      {
          if (scores[i] > maxVal)
              maxVal = scores[i];
      }
      
      MAX_SCORE = ((int)(maxVal * 1.2))/100 * 100;
      Y_HATCH_CNT = MAX_SCORE / 100;
      
      
   }

    @Override
    protected void paintComponent(Graphics g) 
    {
        Graphics2D g2 = (Graphics2D)g;
        
        g2.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 12));
        
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        double xScale = ((double) getWidth() - 2 * BORDER_GAP) / (maxX - 1);
        double yScale = ((double) getHeight() - 2 * BORDER_GAP) / (MAX_SCORE - 1);

        List<Point> graphPoints = new ArrayList<Point>();

        for (int i = 0; i < maxX; i++) 
        {
            int x1 = (int) (i * xScale + BORDER_GAP);
            int y1 = (int) ((MAX_SCORE - scores[i]) * yScale + BORDER_GAP);
            graphPoints.add(new Point(x1, y1));
        }

        // create x and y axes 
        g2.drawLine(BORDER_GAP, getHeight() - BORDER_GAP, BORDER_GAP, BORDER_GAP);
        g2.drawLine(BORDER_GAP, getHeight() - BORDER_GAP, getWidth() - BORDER_GAP, getHeight() - BORDER_GAP);

        // create hatch marks for y axis. 
        for (int i = 0; i < Y_HATCH_CNT; i++) 
        {
            int x0 = BORDER_GAP;
            int x1 = BORDER_GAP + GRAPH_POINT_WIDTH;
            int y0 = getHeight() - (((i + 1) * (getHeight() - BORDER_GAP * 2)) / Y_HATCH_CNT + BORDER_GAP);
            int y1 = y0;
            g2.drawLine(x0, y0, x1, y1);

            g2.drawString(Integer.toString((i+1)*100), 7, y0+6);
        }

        // and for x axis
        for (int i = 0; i < maxX - 1; i++) 
        {
            int x0 = (i + 1) * (getWidth() - BORDER_GAP * 2) / (maxX - 1) + BORDER_GAP;
            int x1 = x0;
            int y0 = getHeight() - BORDER_GAP;
            int y1;
            if((i+1)%5 == 0)
                y1 = y0 - GRAPH_POINT_WIDTH*2;
            else
                y1 = y0 - GRAPH_POINT_WIDTH;
            g2.drawLine(x0, y0, x1, y1);

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
        g2.setColor(GRAPH_COLOR);
        g2.setStroke(GRAPH_STROKE);
        for (int i = 0; i < graphPoints.size() - 1; i++) 
        {
            int x1 = graphPoints.get(i).x;
            int y1 = graphPoints.get(i).y;
            int x2 = graphPoints.get(i + 1).x;
            int y2 = graphPoints.get(i + 1).y;
            g2.drawLine(x1, y1, x2, y2);         
        }

        g2.setStroke(oldStroke);      
        g2.setColor(GRAPH_POINT_COLOR);
        for (int i = 0; i < graphPoints.size(); i++) 
        {
            int x = graphPoints.get(i).x - GRAPH_POINT_WIDTH / 2;
            int y = graphPoints.get(i).y - GRAPH_POINT_WIDTH / 2;;
            int ovalW = GRAPH_POINT_WIDTH;
            int ovalH = GRAPH_POINT_WIDTH;
            g2.fillOval(x, y, ovalW, ovalH);
        }
       
    }

   @Override
   public Dimension getPreferredSize() {
      return new Dimension(PREF_W, PREF_H);
   }

   private static void createAndShowGui() 
   {
      int[] scores = new int[16];
      Random random = new Random();
      int maxDataPoints = 16;
      int maxScore = 20;
      for (int i = 0; i < maxDataPoints ; i++) {
         scores[i] = random.nextInt(maxScore);
      }
      SwingLineGraph mainPanel = new SwingLineGraph(scores);

      JFrame frame = new JFrame("SwingLineGraph");
      frame.getContentPane().add(mainPanel);
      frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
      frame.pack();
      frame.setVisible(true);
   }
   
      public static void createAndShowGui(SwingLineGraph sli) {

      JFrame frame = new JFrame("SwingLineGraph");
      frame.getContentPane().add(sli);
      frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
      frame.pack();
      frame.setVisible(true);
   }

   public static void main(String[] args) {
      SwingUtilities.invokeLater(new Runnable() {
         public void run() {
            createAndShowGui();
         }
      });
   }
   
   
   public int convertYVal(int y)
   {
       double yScale = ((double) getHeight() - 2 * BORDER_GAP) / (MAX_SCORE - 1);
       return (int)((BORDER_GAP - y)/yScale + MAX_SCORE);
   }
   
}