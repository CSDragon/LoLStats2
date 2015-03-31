package lolstats2;


import java.awt.*;
import java.awt.event.*;
import java.awt.image.*;
import javax.swing.*;
import javax.imageio.ImageIO;
import java.io.IOException;
import java.io.File;

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
    private statSelectPane ssp;
    
    
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
                    LolStats2.run(nameInputLine.getText());
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
        
        
        lookupButton = new JButton("Find Summoner");
        lookupButton.setFont(new Font(Font.SANS_SERIF, Font.PLAIN, 9));
        lookupButton.setSize(100, 20);
        lookupButton.setLocation(440, 30);  
        lookupButton.addMouseListener(new MouseListener()
        {
            public void mousePressed(MouseEvent e) 
            {
                
            }

            public void mouseReleased(MouseEvent e) 
            {
                LolStats2.run(nameInputLine.getText());
            }

            public void mouseClicked(MouseEvent e) 
            {
                
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
        

        try 
        {
            topBar = ImageIO.read(new File("assets/TopBar.png"));
            bottomBar = ImageIO.read(new File("assets/BottomBar.png"));
        } 
        catch (IOException e)
        {System.out.println("bluh" + e);}
        
    
	}


    public void createGraph(SwingLineGraph sli) 
    {
      GPMPMGraph = sli;
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
		g2.fillRect(0, 0, this.getWidth(), HEIGHT);
        
       

        
        g2.drawImage(topBar, 0, 0, null);
        g2.drawImage(bottomBar,0,459,null);
         

        
		//draw Strings
		g2.setColor(Color.decode("#000000"));
        g2.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 12));
		g2.drawString("Status", 5, 475);
		g2.drawString("Ver 1.0.0.1", 565, 475);//Figure Out a way to align this later.
        
        //draw headder string
		g2.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 24));
        g2.drawString("Look Up A Summoner", 200, 25);//Figure out how to aligh this later
        
        


        Integer.parseInt("1");
        
		//Toolkit.getDefaultToolkit().sync();
    }
    
    
    public void showInner()
    {
        if(ssp!=null)
            ssp.setVisible(true);
    }
    
    public void hideInner()
    {
        if(ssp!=null)
            ssp.setVisible(false);
    }
    
    
    public static void main(String[] args) 
    {
        GUI hGUI = new GUI();
    
    	hGUI.setVisible(true);

		//hGUI.drawScene();
    }
    
    
   

    
}