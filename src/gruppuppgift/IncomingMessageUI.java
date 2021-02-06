package gruppuppgift;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;

/*
 * UI för inkommande meddelanden. 
 */
public class IncomingMessageUI extends JPanel implements ActionListener {
	private JLabel sender = new JLabel(" ", JLabel.CENTER);
	private JTextArea txtMessage = new JTextArea();
	private JLabel imageLbl = new JLabel(" ", JLabel.CENTER);
	private JButton exitBtn = new JButton("Avbryt");
	private JLabel senderPic = new JLabel(" ", JLabel.CENTER);
	private JPanel pnlNorth = new JPanel(new GridLayout(1, 2));
	private JPanel pnlCenter = new JPanel(new GridLayout(1, 2));
	private JPanel pnlSouth = new JPanel(new GridLayout(1, 2));
	private Font font = new Font("MONOSPACE", Font.PLAIN, 16);
	private JFrame frame;

	/*
	 * Konstruktor för meddelande med både bild och text
	 */
	public IncomingMessageUI(String username, ImageIcon profilePic, String message, ImageIcon imageIcon) {
		setLayout(new BorderLayout());
		sender.setText("Nytt meddelande från: " + username);
		sender.setFont(font);
		senderPic.setIcon(profilePic);
		
		pnlNorth.setBorder(new LineBorder(Color.BLACK, 5));
		pnlCenter.setBorder(new EmptyBorder(10, 10, 10, 10));
		
		txtMessage.setText(message);
		txtMessage.setFont(font);
		txtMessage.setLineWrap(true);
		txtMessage.setEditable(false);
		txtMessage.setBackground(null);
		
		Image image = imageIcon.getImage().getScaledInstance(200, 200, Image.SCALE_SMOOTH);
		imageIcon = new ImageIcon(image);
		imageLbl.setIcon(imageIcon);
		
		pnlNorth.add(sender);
		pnlNorth.add(senderPic);
		pnlCenter.add(txtMessage);
		pnlCenter.add(imageLbl);
		pnlSouth.add(exitBtn);
		
		add(pnlNorth, BorderLayout.NORTH);
		add(pnlCenter);
		add(pnlSouth, BorderLayout.SOUTH);
		
		exitBtn.addActionListener(this);
	}

	/*
	 * Konstruktor för meddelande med bara text
	 */
	public IncomingMessageUI(String username, ImageIcon profilePic, String message) {
		setLayout(new BorderLayout());
		sender.setText("Nytt meddelande från: " + username);
		sender.setFont(font);
		senderPic.setIcon(profilePic);
		
		txtMessage.setText(message);
		txtMessage.setFont(font);
		txtMessage.setLineWrap(true);
		txtMessage.setEditable(false);
		txtMessage.setBackground(null);
		
		pnlNorth.setBorder(new LineBorder(Color.BLACK, 5));
		pnlCenter.setBorder(new EmptyBorder(10, 10, 10, 10));
		
		pnlNorth.add(sender);
		pnlNorth.add(senderPic);		
		pnlCenter.add(txtMessage);
		pnlSouth.add(exitBtn);
		
		add(pnlCenter);
		add(pnlNorth, BorderLayout.NORTH);
		add(pnlSouth, BorderLayout.SOUTH);
		
		exitBtn.addActionListener(this);
	}
	
	/*
	 * Konstruktor för meddelande med bara bild
	 */
	public IncomingMessageUI(String username, ImageIcon profilePic, ImageIcon imageIcon) {
		setLayout(new BorderLayout());
		sender.setText("Nytt meddelande från: " + username);
		sender.setFont(font);
		senderPic.setIcon(profilePic);
		
		pnlNorth.setBorder(new LineBorder(Color.BLACK, 5));
		pnlCenter.setBorder(new EmptyBorder(10, 10, 10, 10));
		
		Image image = imageIcon.getImage().getScaledInstance(300, 300, Image.SCALE_SMOOTH);
		imageIcon = new ImageIcon(image);
		imageLbl.setIcon(imageIcon);
		
		pnlNorth.add(sender);
		pnlNorth.add(senderPic);
		pnlSouth.add(exitBtn);
		pnlCenter.add(imageLbl);
		
		add(pnlCenter);
		add(pnlNorth, BorderLayout.NORTH);
		add(pnlSouth, BorderLayout.SOUTH);
		
		exitBtn.addActionListener(this);
	}
	
	// Stänger frame om exitBtn klickas
	public void actionPerformed(ActionEvent e) {
			frame.dispose();
	}
	
	public void setFrame(JFrame frame) {
		this.frame = frame;
	}
}
