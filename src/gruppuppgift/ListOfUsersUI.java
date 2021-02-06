package gruppuppgift;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

import javax.swing.*;

/*
 * UI som visar uppkopplade användare, kontakter, knappar för att skicka meddelande, koppla från osv.
 */
public class ListOfUsersUI extends JPanel implements ActionListener {
	private ArrayList<JCheckBoxMenuItem> onlineUsersCheckBox = new ArrayList<>();
	private ArrayList<JCheckBoxMenuItem> contactListCheckBox = new ArrayList<>();
	private ArrayList<User> onlineUsersList = new ArrayList<>();
	private ClientController controller;
	private JButton sendMessageButton = new JButton("Nytt meddelande");
	private JButton addContactButton = new JButton("Lägg till kontakter");
	private JButton disconnectButton = new JButton("Koppla från");
	private JButton removeContactButton = new JButton("Ta bort kontakter");
	private JPanel onlinePanel = new JPanel();
	private JPanel contactPanel = new JPanel();
	private JPanel buttonPanel = new JPanel();
	private JLabel onlineLabel = new JLabel("Uppkopplade användare");
	private JLabel contactLabel = new JLabel("Kontakter");
	private JScrollPane onlineScroll;
	private JScrollPane contactScroll;
	private ImageIcon bgImage;
	private JLabel bgImageLabel = new JLabel();
	private Font labelFont = new Font("SansSerif", Font.PLAIN, 12);
	private JPanel mainPanel = new JPanel();
	private Font buttonFont = new Font("SansSerif", Font.PLAIN, 10);

	/*
	 * Konstruktor.
	 */
	public ListOfUsersUI(ClientController controller) {
		this.controller = controller;
		setVisible(true);
		setLayout(null);
		setPreferredSize(new Dimension(340, 700));
		add(mainPanel);
		mainPanel.setBounds(35, 70, 270, 610);
		mainPanel.setLayout(new FlowLayout());

		mainPanel.add(onlinePanel);
		onlinePanel.setLayout(new GridLayout(15, 1));

		onlineLabel.setFont(labelFont);
		onlineLabel.setBackground(Color.PINK);
		onlineLabel.setForeground(Color.BLACK);
		onlinePanel.add(onlineLabel);
		onlineScroll = new JScrollPane(onlinePanel);
		onlineScroll.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
		onlineScroll.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
		onlineScroll.setPreferredSize(new Dimension(260, 250));
		mainPanel.add(onlineScroll);

		mainPanel.add(contactPanel);
		contactPanel.setLayout(new GridLayout(15, 1));
		contactPanel.add(contactLabel);
		contactLabel.setFont(labelFont);
		contactLabel.setBackground(new Color(100, 0, 150));
		contactLabel.setForeground(Color.BLACK);
		contactScroll = new JScrollPane(contactPanel);
		contactScroll.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
		contactScroll.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
		contactScroll.setPreferredSize(new Dimension(260, 250));
		mainPanel.add(contactScroll);

		mainPanel.add(buttonPanel);
		buttonPanel.setLayout(null);
		buttonPanel.setVisible(true);
		buttonPanel.setPreferredSize(new Dimension(260, 90));
		buttonPanel.setBackground(Color.CYAN);
		sendMessageButton.setBounds(5, 10, 120, 30);
		sendMessageButton.setFont(buttonFont);
		addContactButton.setBounds(135, 10, 120, 30);
		addContactButton.setFont(buttonFont);
		disconnectButton.setBounds(135, 50, 120, 30);
		disconnectButton.setFont(buttonFont);
		removeContactButton.setBounds(5, 50, 120, 30);
		removeContactButton.setFont(buttonFont);
		buttonPanel.add(sendMessageButton);
		buttonPanel.add(addContactButton);
		buttonPanel.add(disconnectButton);
		buttonPanel.add(removeContactButton);
		addContactButton.addActionListener(this);
		sendMessageButton.addActionListener(this);
		disconnectButton.addActionListener(this);
		removeContactButton.addActionListener(this);
		loadBG();
	}

	/*
	 * Laddar in bakgrundsbild
	 */
	public void loadBG() {
		bgImage = new ImageIcon(
				new ImageIcon("images/userbg2.jpg").getImage().getScaledInstance(340, 700, Image.SCALE_DEFAULT));
		bgImageLabel.setIcon(bgImage);
		bgImageLabel.setBounds(0, 0, 340, 700);
		add(bgImageLabel);
	}

	/*
	 * Uppdaterar UI med ny lista över uppkopplade användare
	 */
	public void addOnlineUserLabels(ArrayList<User> onlineUsersList) {
		this.onlineUsersList = onlineUsersList;

		for (int i = 0; i < onlineUsersCheckBox.size(); i++) {
			onlineUsersCheckBox.remove(i);
		}

		onlinePanel.removeAll();

		if (onlineUsersList.size() > 0) {
			for (int i = 0; i < onlineUsersList.size(); i++) {
				onlineUsersCheckBox.add(i, new JCheckBoxMenuItem());
				onlineUsersCheckBox.get(i).setText(onlineUsersList.get(i).getUserName());
				onlineUsersCheckBox.get(i).setIcon(onlineUsersList.get(i).getImage());
				onlineUsersCheckBox.get(i).setIconTextGap(15);
				onlineUsersCheckBox.get(i).setBackground(new Color(200, 0, 150));
				onlineUsersCheckBox.get(i).setForeground(Color.WHITE);
				onlinePanel.add(onlineUsersCheckBox.get(i));
			}
		}
		updateUI();
	}

	/*
	 * Uppdaterar UI med ny kontaktlista
	 */
	public void addContactListLabels(ArrayList<User> contactList) {
		for (int i = 0; i < contactListCheckBox.size(); i++) {
			contactListCheckBox.remove(i);
		}

		contactPanel.removeAll();

		for (int i = 0; i < contactList.size(); i++) {
			contactListCheckBox.add(i, new JCheckBoxMenuItem());
			contactListCheckBox.get(i).setText(contactList.get(i).getUserName());
			contactListCheckBox.get(i).setIcon(contactList.get(i).getImage());
			contactListCheckBox.get(i).setIconTextGap(15);
			contactListCheckBox.get(i).setBackground(new Color(100, 0, 150));
			contactListCheckBox.get(i).setForeground(Color.WHITE);
			contactPanel.add(contactListCheckBox.get(i));
		}
		updateUI();
	}

	/*
	 * ActionListener. Lyssnar efter knapptryck.
	 */
	public void actionPerformed(ActionEvent e) {
		if (e.getSource() == sendMessageButton) {
			ArrayList<Integer> onlineIndexes = new ArrayList<Integer>();
			for (int i = 0; i < onlineUsersCheckBox.size(); i++) {
				if (onlineUsersCheckBox.get(i).isSelected()) {
					onlineIndexes.add(i);
				}
			}
			ArrayList<Integer> contactIndexes = new ArrayList<Integer>();
			if (contactListCheckBox.size() > 0) {
				for (int i = 0; i < contactListCheckBox.size(); i++) {
					if (contactListCheckBox.get(i).isSelected()) {
						contactIndexes.add(i);
					}
				}
			}
			controller.writeMessagePanel(onlineIndexes, contactIndexes);
		}
		if (e.getSource() == addContactButton) {
			for (int i = 0; i < onlineUsersCheckBox.size(); i++) {
				if (onlineUsersCheckBox.get(i).isSelected()) {
					System.out.println(onlineUsersList);
					controller.addContact(onlineUsersList.get(i));
					JOptionPane.showMessageDialog(null, "Kontakt tillagd");
				}
			}
		}

		if (e.getSource() == removeContactButton) {
			for (int i = 0; i < contactListCheckBox.size(); i++) {
				if (contactListCheckBox.get(i).isSelected()) {
					controller.removeContact(i);
					JOptionPane.showMessageDialog(null, "Kontakt borttagen");
				}
			}
		}

		if (e.getSource() == disconnectButton) {
			controller.disconnect();
		}
	}
}
