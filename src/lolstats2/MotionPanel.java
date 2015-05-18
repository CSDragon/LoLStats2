/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package lolstats2;

import java.awt.Frame;
import javax.swing.JPanel;
import javax.swing.JFrame;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;
import java.awt.Point;

public class MotionPanel extends JPanel
{
    private Point initialClick;
    private JFrame parent;

    public MotionPanel(final JFrame parent)
    {
        this.parent = parent;
        this.setOpaque(false);
        
        addMouseListener(new MouseAdapter() 
        {
            public void mousePressed(MouseEvent e) 
            {
                initialClick = e.getPoint();
                getComponentAt(initialClick);
            }
            
            public void mouseClicked(MouseEvent e)
            {
                if(e.getX() > 590 && e.getX() <= 615)
                    parent.setState(Frame.ICONIFIED);
                if(e.getX() > 615 && e.getX() <= 640)
                {
                    parent.dispose();
                    System.exit(1);
                }
            }
        });

        addMouseMotionListener(new MouseMotionAdapter() 
        {
            @Override
            public void mouseDragged(MouseEvent e) 
            {

                // get location of Window
                int thisX = parent.getLocation().x;
                int thisY = parent.getLocation().y;

                // Determine how much the mouse moved since the initial click
                int xMoved = (thisX + e.getX()) - (thisX + initialClick.x);
                int yMoved = (thisY + e.getY()) - (thisY + initialClick.y);

                // Move window to this position
                int X = thisX + xMoved;
                int Y = thisY + yMoved;
                parent.setLocation(X, Y);
            }
        });
    }
}