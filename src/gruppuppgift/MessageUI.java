package gruppuppgift;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import javax.swing.filechooser.FileNameExtensionFilter;

/*
 * UI för att skicka meddelande.
 */
public class MessageUI extends JPanel implements ActionListener {
	private JFileChooser fileChooser = new JFileChooser("Välj en bild");
	private JButton btnAddImage = new JButton("Lägg till en bild");
	private JTextArea txtMessage = new JTextArea();
	private JButton sendMessage = new JButton("Skicka meddelande");
	private JButton exit = new JButton("Avbryt");
	private ImageIcon image;
	private ClientController controller;
	private ArrayList<User> receiverList;
	private Font font = new Font("MONOSPACE", Font.PLAIN, 16);
	private JPanel pnlSouth = new JPanel();
	private JPanel pnlNorth = new JPanel(new GridLayout(2, 1));
	private JLabel receiversLabel = new JLabel("Mottagare: ");
	private JFrame frame;

	public MessageUI(ClientController controller, ArrayList<User> receiverList) {
		this.receiverList = receiverList;
		setPreferredSize(new Dimension(500, 500));
		setLayout(new BorderLayout());
		txtMessage.setLineWrap(true);
		txtMessage.setWrapStyleWord(true);
		fileChooser.addChoosableFileFilter(new FileNameExtensionFilter("Image files", ImageIO.getReaderFileSuffixes()));
		fileChooser.setAcceptAllFileFilterUsed(false);
		txtMessage.setFont(font);
		txtMessage.setBorder(new LineBorder(Color.BLACK, 5));
		pnlNorth.setBorder(new EmptyBorder(5, 5, 5, 5));
		pnlNorth.add(receiversLabel);
		pnlNorth.add(btnAddImage);
		add(pnlNorth, BorderLayout.NORTH);
		add(txtMessage, BorderLayout.CENTER);
		pnlSouth.add(sendMessage);
		pnlSouth.add(exit);
		add(pnlSouth, BorderLayout.SOUTH);
		btnAddImage.addActionListener((ActionListener) this);
		sendMessage.addActionListener((ActionListener) this);
		exit.addActionListener((ActionListener) this);
		setReceiversLabel(receiverList);
		this.controller = controller;
	}

	public void actionPerformed(ActionEvent e) {
		if (e.getSource() == btnAddImage) {
			if (fileChooser.showOpenDialog(null) == JFileChooser.APPROVE_OPTION) {
				String filename = fileChooser.getSelectedFile().getPath();
				System.out.println(filename);
				image = new ImageIcon(
						new ImageIcon(filename).getImage().getScaledInstance(300, 300, Image.SCALE_DEFAULT));
				btnAddImage.setText("Bild tillagd: " + filename);
			}
		}

		if (e.getSource() == sendMessage) {
			if (image == null && txtMessage != null) {
				controller.outGoingMessage(receiverList, txtMessage.getText());
				JOptionPane.showMessageDialog(null, "Meddelande skickat!");
			} else if (image != null && txtMessage == null) {
				controller.outGoingMessage(receiverList, image);
				JOptionPane.showMessageDialog(null, "Meddelande skickat!");
			} else if (image != null && txtMessage != null) {
				controller.outGoingMessage(receiverList, txtMessage.getText(), image);
				JOptionPane.showMessageDialog(null, "Meddelande skickat!");
			} else {
				JOptionPane.showMessageDialog(null, "Text och/eller bild måste anges");
			}
		}

		// Stänger frame om exit-knappen trycks
		if (e.getSource() == exit) {
			frame.dispose();
		}

	}

	/*
	 * Uppdaterar UI med namn på valda mottagare
	 */
	public void setReceiversLabel(ArrayList<User> receiverList) {
		String temp = "Mottagare: ";
		for (int i = 0; i < receiverList.size(); i++) {
			if (i < receiverList.size() - 1) {
				temp += receiverList.get(i).getUserName() + ", ";
			} else {
				temp += receiverList.get(i).getUserName();
			}
		}
		receiversLabel.setText(temp);
	}

	public void setFrame(JFrame frame) {
		this.frame = frame;
	}
}
