package lolstats2;


import java.awt.*;
import java.awt.event.*;
import java.awt.image.*;
import javax.swing.*;
import javax.imageio.ImageIO;
import java.io.IOException;
import java.io.File;
import javax.swing.JTabbedPane;
import javax.swing.JTable;
import javax.swing.table.*;

/**
 * Handles interactions with the game's Graphical User Interface
 */
@SuppressWarnings("serial") // Don't care to maintain serials
public class GUI extends JPanel
{
	private BufferedImage topBar;
    private BufferedImage bottomBar;
    private SwingLineGraph GPMPMGraph;
    private JTextField nameInputLine;
    private JButton lookupButton;
    private BufferedImage lookupButtonArt;
    private statSelectPane ssp;
    private ArtPanel artp;
    
    public static final int WIDTH = 640, HEIGHT = 480;
    

	/**
	 * Creates and sets up an instance of the game's Graphical User Interface
	 */
	public GUI() 
    {       
        setPreferredSize(new Dimension(640,480));
        setLayout(null);
        
        nameInputLine = new JTextField();
        nameInputLine.setLocation(100, 30);
        nameInputLine.setSize(320, 20);  
        nameInputLine.addKeyListener(new KeyListener()
        {
            public void actionPerformed(KeyEvent e)
            {
                System.out.println(e.getKeyCode());
            }
            
            @Override
            public void keyReleased(KeyEvent e)
            {
                //If "enter"
                if(e.getKeyCode() == 10)
                {
                    if(!nameInputLine.getText().equals(""))
                        LolStats2.run(nameInputLine.getText());
                    else
                        hideInner();
                }
            }
            
            @Override
            public void keyPressed(KeyEvent e)
            {
              //doNothing
            }
            
            @Override
            public void keyTyped(KeyEvent e)
            {
                //doNothing
            }
        });
        
        add(nameInputLine);
        
        
        lookupButton = new JButton();
        lookupButton.setSize(100, 20);
        lookupButton.setOpaque(false);
        lookupButton.setContentAreaFilled(false);
        lookupButton.setBorderPainted(false);
        lookupButton.setLocation(440, 30);  
        lookupButton.addMouseListener(new MouseListener()
        {
            public void mousePressed(MouseEvent e) 
            {
                
            }

            public void mouseReleased(MouseEvent e) 
            {
                
            }

            public void mouseClicked(MouseEvent e) 
            {
                if(!nameInputLine.getText().isEmpty())
                    LolStats2.run(nameInputLine.getText());
                else
                    hideInner();
            }

            public void mouseEntered(MouseEvent e) 
            {
                
            }

            public void mouseExited(MouseEvent e) 
            {
                
            }
        });
        
        add(lookupButton);
        
        
        ssp = new statSelectPane();
        ssp.setSize(640,39);
        ssp.setLocation(0, 58);
        ssp.setVisible(false);
        add(ssp);
        
        artp = new ArtPanel();
        artp.setSize(640,402);
        artp.setLocation(0, 57);
        artp.setVisible(true);
        add(artp);
        
        
        JTable jt = new JTable(16,3);
        jt.setSize(640,200);
        jt.setLocation(0, 260);
        jt.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
        jt.getColumnModel().getColumn(0).setPreferredWidth(1);
        jt.getColumnModel().getColumn(0).setWidth(1);
        jt.getColumnModel().getColumn(1).setPreferredWidth(10);
        jt.setAutoResizeMode(JTable.AUTO_RESIZE_LAST_COLUMN);
        jt.setVisible(true);
        jt.setEnabled(false);
        
        //add(jt);
        
        

        try 
        {
            topBar = ImageIO.read(new File("assets/TopBar.png"));
            bottomBar = ImageIO.read(new File("assets/BottomBar.png"));
            lookupButtonArt = ImageIO.read(new File("assets/SummonerLookupButton.png"));
        } 
        catch (IOException e)
        {System.out.println("bluh" + e);}
        
    
	}


    public void createGraph(goldAnalyst gpmem) 
    {
        //GPMPMGraph =  new SwingLineGraph(gpmem.getCPMPerMin(),1);
        //GPMPMGraph =  new SwingLineGraph(gpmem.getGPMPerMin(),100);
        int asdf[] = {0,1,2,3,4,5,6,7,8,9,10,0,1,2,3,4,5,6,7,8,9,10,0,1,2,3,4,5,6,7,8,9,10,0,1,2,3,4,5,6,7,8,9,10,0,1,2,3,4,5,6,7,8,9,10,};
        GPMPMGraph = new SwingLineGraph(asdf,1);
        GPMPMGraph.setLocation(0,100);
        GPMPMGraph.setSize(640,360);
        add(GPMPMGraph);
        GPMPMGraph.setVisible(true);
    }
    
    
    @Override
    protected void paintComponent(Graphics g) 
    {
        Graphics2D g2 = (Graphics2D)g;
        
        //Fill background
		g2.setColor(Color.decode("#DEE4EA")); 
		g2.fillRect(0, 0, 640, 480);
        
       
        //artp.reOrderPaint(g);
        
        g2.drawImage(topBar, 0, 0, null);
        g2.drawImage(bottomBar,0,459,null);
        g2.drawImage(lookupButtonArt, 440, 29, null);
         

        
		//draw Strings
		g2.setColor(Color.decode("#FFFFFF"));
        g2.setFont(new Font(Font.SANS_SERIF, Font.PLAIN, 12));
		g2.drawString("Status", 5, 475);
		g2.drawString("Ver 1.0.0.1", 565, 475);//Figure Out a way to align this later.


    }
    
    
    public void showInner()
    {
        ssp.setVisible(true);
        artp.setVisible(false);
        
    }
    
    public void hideInner()
    {
        ssp.setVisible(false);
        artp.setVisible(true);
    }

    public void paneChanged(int ChangedTo)
    {
        GPMPMGraph.setVisible(false);
        
        if (ChangedTo == 1)
            GPMPMGraph.setVisible(true);
    }
    

    
}