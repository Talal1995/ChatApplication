package gruppuppgift;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.filechooser.FileNameExtensionFilter;

/*
 * UI för inloggning. Ber användaren mata in användarnamn och användarbild.
 */
public class LoginUI extends JPanel {
	private JTextField userNameTextField = new JTextField("");
	private JLabel inputUsernameLabel = new JLabel("Ange ett anv�ndarnamn:");
	private JFileChooser fileChooser = new JFileChooser();
	private JButton loginButton = new JButton("Logga in");
	private JButton pictureButton = new JButton("V�lj anv�ndarbild");
	private JLabel userPictureLabel = new JLabel(
			"<html><body><div style='text-align: center;'>Din anv�ndarbild</div></body></html>",
			(int) CENTER_ALIGNMENT);
	private JLabel bgImageLabel = new JLabel();
	private ImageIcon bgImage;
	private ImageIcon userImage = new ImageIcon("images/kramer.jpg");
	private ImageIcon checkBoxImage = new ImageIcon("images/lillkramer.jpg");
	private Font inputFont = new Font("SansSerif", Font.BOLD, 14);
	private Font smallFont = new Font("SansSerif", Font.PLAIN, 14);
	private ClientController clientController;
	private Clip clip;
	private JFrame frame;

	/*
	 * Konstruktor.
	 */
	public LoginUI(ClientController controller) {
		this.clientController = controller;
		setVisible(true);
		setPreferredSize(new Dimension(400, 500));
		setOpaque(true);
		setLayout(null);

		try {
			userNameTextField.getDocument().addDocumentListener(new DocumentListener() {
				@Override
				public void removeUpdate(DocumentEvent e) {
					enableLogin();
				}

				@Override
				public void insertUpdate(DocumentEvent e) {
					enableLogin();
				}

				@Override
				public void changedUpdate(DocumentEvent e) {
					enableLogin();

				}
			});

		} catch (NullPointerException e) {
			e.printStackTrace();
		}
		try {
			File soundFile = new File("sounds/Roadgame.mid"); //
			AudioInputStream audioIn = AudioSystem.getAudioInputStream(soundFile);
			clip = AudioSystem.getClip();
			clip.open(audioIn);
			clip.start();
		} catch (UnsupportedAudioFileException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (LineUnavailableException e) {
			e.printStackTrace();
		}

		userNameTextField.setBounds(100, 190, 200, 40);
		userNameTextField.setForeground(Color.GRAY);

		inputUsernameLabel.setBounds(100, 155, 200, 40);
		inputUsernameLabel.setFont(inputFont);
		inputUsernameLabel.setForeground(Color.WHITE);

		userPictureLabel.setBounds(150, 245, 100, 100);
		userPictureLabel.setFont(smallFont);
		userPictureLabel.setIcon(userImage);

		add(userNameTextField);
		add(inputUsernameLabel);
		add(pictureButton);
		add(userPictureLabel);
		add(loginButton);
		loadBG();

		PictureLoader pictureLoader = new PictureLoader();
		pictureButton.addActionListener(pictureLoader);
		pictureButton.setBounds(125, 360, 150, 40);

		Login loginListener = new Login();
		loginButton.addActionListener(loginListener);
		loginButton.setBounds(100, 420, 200, 60);
		loginButton.setBackground(new Color(102, 0, 102));
		loginButton.setForeground(Color.WHITE);
		loginButton.setEnabled(false);

	}

	/*
	 * Laddar in bakgrundsbild.
	 */
	public void loadBG() {
		bgImage = new ImageIcon(
				new ImageIcon("images/RetroChatt.png").getImage().getScaledInstance(400, 500, Image.SCALE_DEFAULT));
		bgImageLabel.setIcon(bgImage);
		bgImageLabel.setBounds(0, 0, 400, 500);
		add(bgImageLabel);
	}

	public void enableLogin() {
		if (userNameTextField.getText().length() != 0 && userNameTextField.getText().length() <= 30) {
			if (userImage.getImage() == null) {
				loginButton.setEnabled(false);
			}
			loginButton.setEnabled(true);
		} else {
			loginButton.setEnabled(false);
		}
	}

	private class PictureLoader implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			fileChooser.addChoosableFileFilter(
					new FileNameExtensionFilter("Image files", ImageIO.getReaderFileSuffixes()));
			fileChooser.setAcceptAllFileFilterUsed(false);
			if (fileChooser.showOpenDialog(null) == JFileChooser.APPROVE_OPTION) {
				String filename = fileChooser.getSelectedFile().getPath();
				System.out.println(filename);
				userImage = new ImageIcon(
						new ImageIcon(filename).getImage().getScaledInstance(100, 100, Image.SCALE_DEFAULT));
				checkBoxImage = new ImageIcon(
						new ImageIcon(filename).getImage().getScaledInstance(40, 40, Image.SCALE_DEFAULT));
				userPictureLabel.setIcon(userImage);
				userPictureLabel.setText("");
				enableLogin();
			}
		}
	}

	private class Login implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent e) {
			sendUserInfo();
			clip.stop();
			frame.dispose();
		}

	}

	public void setFrame(JFrame frame) {
		this.frame = frame;
	}

	public void sendUserInfo() {
		clientController.newUser(userNameTextField.getText(), checkBoxImage);
	}
}
