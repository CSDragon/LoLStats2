package lolstats2;


import java.awt.*;
import java.awt.event.*;
import java.awt.image.*;
import javax.swing.*;
import javax.imageio.ImageIO;
import java.io.IOException;
import java.io.File;
import javax.swing.JComboBox;

/**
 * Handles interactions with the game's Graphical User Interface
 */
public class GUI extends JPanel
{
	private BufferedImage topBar;
    private BufferedImage bottomBar;
    private GraphsChartsPanel gcpPane;
    private JTextField nameInputLine;
    private JButton lookupButton;
    private BufferedImage lookupButtonArt;
    private BufferedImage lookupButtonPressedArt;
    private StatSelectPane ssp;
    private ArtPanel artp;
    private JComboBox<String> regionPicker;
    private String status = "Status";
  
    public static final int WIDTH = 640, HEIGHT = 480;
    

	/**
	 * Creates and sets up an instance of the game's Graphical User Interface
	 */
	public GUI() 
    {       
        setPreferredSize(new Dimension(640,480));
        setLayout(null);
        
        ImageIcon button = new ImageIcon("assets/SummonerLookupButton.png");
        ImageIcon buttonPressed = new ImageIcon("assets/SummonerLookupButtonPressed.png");
        
        try 
        {
            topBar = ImageIO.read(new File("assets/TopBar.png"));
            bottomBar = ImageIO.read(new File("assets/BottomBar.png"));
        } 
        catch (IOException e) {}
        
        nameInputLine = new JTextField();
        nameInputLine.setLocation(100, 30);
        nameInputLine.setSize(320, 20);  
        nameInputLine.addKeyListener(new KeyListener()
        {
            public void actionPerformed(KeyEvent e) {}

            public void keyReleased(KeyEvent e)
            {
                //If "enter"
                if(e.getKeyCode() == 10)
                {
                    if(!nameInputLine.getText().equals(""))
                        LolStats2.run(nameInputLine.getText(),(String)regionPicker.getSelectedItem());
                    else
                        hideInner();
                }
            }
            
            public void keyPressed(KeyEvent e) {}
            public void keyTyped(KeyEvent e){}
        });
        
        add(nameInputLine);
        
        
        lookupButton = new JButton(button);
        lookupButton.setPressedIcon(buttonPressed);
        lookupButton.setOpaque(false);
        lookupButton.setBorderPainted(false);
        lookupButton.setContentAreaFilled(false);
        lookupButton.setSize(101, 22);
        lookupButton.setLocation(440, 30);  
        lookupButton.addMouseListener(new MouseListener()
        {
            public void mousePressed(MouseEvent e) {}
            public void mouseReleased(MouseEvent e) {}
            
            public void mouseClicked(MouseEvent e) 
            {
                if(!nameInputLine.getText().equals(""))
                    LolStats2.run(nameInputLine.getText(),(String)regionPicker.getSelectedItem());
                else
                    hideInner();
            }

            public void mouseEntered(MouseEvent e) {}
            public void mouseExited(MouseEvent e) {}
        });
        
        add(lookupButton);
        
        
        ssp = new StatSelectPane();
        ssp.setSize(640,39);
        ssp.setLocation(0, 58);
        ssp.setVisible(false);
        add(ssp);
        
        artp = new ArtPanel();
        artp.setSize(640,400);
        artp.setLocation(0, 59);
        artp.setVisible(true);
        add(artp);
        
        
        regionPicker = new JComboBox<>();
        
        regionPicker.addItem("NA"); //BR, EUNE, EUW, KR, LAS, LAN, NA, OCE, TR, RU, PBE, GLOBAL
        regionPicker.addItem("EUW");
        regionPicker.addItem("ENE");
        regionPicker.addItem("KR");
        regionPicker.addItem("LAS");
        regionPicker.addItem("LAN");
        regionPicker.addItem("OCE");
        regionPicker.addItem("TR");
        regionPicker.addItem("RU");
        
        regionPicker.setSize(55, 20);
        regionPicker.setLocation(30, 30);
        regionPicker.setVisible(true);
        add(regionPicker);
        
        
        this.addFocusListener(new FocusListener()
        {
            public void focusLost(FocusEvent e)
            {
                
            }
            
            public void focusGained(FocusEvent e)
            {
                nameInputLine.requestFocus();
            }
        });
        
	}

    /**
     * Creates the GraphsChartsPanel, and makes it create the graphs and charts
     * 
     * @param gpmem The GoldAnalyst for the summoner we looked up
     */
    public void createGraph(GoldAnalyst gpmem) 
    {
        if(gcpPane != null)
            this.remove(gcpPane);
        gcpPane = new GraphsChartsPanel(gpmem);
        ssp.setTarget(gcpPane);
        gcpPane.setLocation(0,100);
        gcpPane.setSize(640,359);
        add(gcpPane);
        gcpPane.setVisible(true);
    }
    
    /**
     * Draws the GUI object
     * 
     * @param g The Graphics
     */
    protected void paintComponent(Graphics g) 
    {
        Graphics2D g2 = (Graphics2D)g;
        
        //Fill background
		g2.setColor(Color.decode("#03151C")); 
		g2.fillRect(0, 0, 640, 480);
        
       
        //artp.reOrderPaint(g);
        
        g2.drawImage(topBar, 0, 0, null);
        g2.drawImage(bottomBar,0,459,null);
         

        
		//draw Strings
		g2.setColor(Color.decode("#FFFFFF"));
        g2.setFont(new Font(Font.SANS_SERIF, Font.PLAIN, 12));
		g2.drawString(status, 5, 475);
		g2.drawString("Ver 1.0.0.1", 565, 475);//Figure Out a way to align this later.


    }
    
    /**
     * Shows the GraphsChartsPanel and the StatSelectPane, while hiding the artPanel. Showing the "inner" content, and hiding the "outer" content. 
     */
    public void showInner()
    {
        ssp.setVisible(true);
        gcpPane.setVisible(true);
        artp.setVisible(false);
        
    }
    
    /**
     * Hides the GraphsChartsPanel and the StatSelectPane, while showing the artPanel. Showing the "outer" content, and hiding the "inner" content.
     */
    public void hideInner()
    {
        ssp.setVisible(false);
        if(gcpPane != null)
            gcpPane.setVisible(false);
        artp.setVisible(true);
    }
    
    /**
     * Changes the status message
     * 
     * @param newStatus the new string we want the status message to say.
     */
    public void changeStatus(String newStatus)
    {
        status = newStatus;
    }
    
    /**
     * Changes the status message back to "Status"
     */
    public void resetStatus()
    {
        status = "Status";
    }
    
}