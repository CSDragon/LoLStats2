package LolStats2;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.event.*;
import javax.swing.text.*;

public class InvisibleTextField extends JTextField implements ActionListener, FocusListener, MouseListener, DocumentListener
{
    public InvisibleTextField()
    {
        setOpaque( false );
        setColumns( 1 );
        setSize( getPreferredSize() );
        setColumns( 0 );
        addActionListener( this );
        addFocusListener( this );
        addMouseListener( this );
        getDocument().addDocumentListener( this );
    }

//  Implement ActionListener

    public void actionPerformed(ActionEvent e)
    {
        setEditable( false );
    }

//  Implement FocusListener

    public void focusLost(FocusEvent e)
    {
        setEditable( false );
    }

    public void focusGained(FocusEvent e) {}

//  Implement MouseListener

    public void mouseClicked( MouseEvent e )
    {
        if (e.getClickCount() == 2)
            setEditable( true );
    }

    public void mouseEntered( MouseEvent e ) {}

    public void mouseExited( MouseEvent e ) {}

    public void mousePressed( MouseEvent e ) {}

    public void mouseReleased( MouseEvent e ) {}

//  Implement DocumentListener

    public void insertUpdate(DocumentEvent e)
    {
        updateSize();
    }

    public void removeUpdate(DocumentEvent e)
    {
        updateSize();
    }

    public void changedUpdate(DocumentEvent e) {}

    private void updateSize()
    {
        setSize( getPreferredSize() );
    }

    public static void main(String[] args)
    {
        JPanel panel = new JPanel();
        panel.setFocusable( true );
        panel.setLayout( null );

        JFrame frame = new JFrame();
        frame.setDefaultCloseOperation( JFrame.EXIT_ON_CLOSE );
        frame.add(new JLabel("Double Click to Add Text"), BorderLayout.NORTH);
        frame.add(panel);
        frame.setSize(650, 300);
        frame.setLocationRelativeTo( null );
        frame.setVisible(true);
    }
}