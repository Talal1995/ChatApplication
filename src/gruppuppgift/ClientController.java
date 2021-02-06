package gruppuppgift;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.Date;
import javax.swing.ImageIcon;
import javax.swing.JFrame;

/*
 * Klassen hanterar logik för klientsidan, all kommunikation mellan Client och UI, läser/skriver kontakter till fil osv.
 */

public class ClientController {
	private User user;
	private Client client;
	private ListOfUsersUI listOfUsersUI;
	private ArrayList<User> onlineUsersList;
	private ArrayList<User> contacts = new ArrayList<>();

	public void addListOfUsersUI(ListOfUsersUI ui) {
		this.listOfUsersUI = ui;
	}

	public void addClient(Client client) {
		this.client = client;
	}

	/*
	 * Tar emot listor på mottagare, sorterar ut dubletter, skapar ett MessageUI-objekt och anropar writeMessage().
	 */
	public void writeMessagePanel(ArrayList<Integer> onlineIndexes, ArrayList<Integer> contactIndexes) {
		ArrayList<User> receiverList = new ArrayList<User>();
		for (int i = 0; i < onlineIndexes.size(); i++) {
			int index = onlineIndexes.get(i);
			receiverList.add(onlineUsersList.get(index));
		}
		for (int i = 0; i < contactIndexes.size(); i++) {
			if (!receiverList.contains(contacts.get(contactIndexes.get(i)))) {
				receiverList.add(contacts.get(contactIndexes.get(i)));
			}
		}
		MessageUI ui = new MessageUI(this, receiverList);
		writeMessage(ui);

	}

	/*
	 * Skapar Message-objekt av inparametrar och skickar vidare till Client.
	 */
	public void outGoingMessage(ArrayList<User> recieverList, String text, ImageIcon image) {
		client.addNewMessage(new Message(user, recieverList, text, image));
	}

	public void outGoingMessage(ArrayList<User> recieverList, String text) {
		client.addNewMessage(new Message(user, recieverList, text));
	}

	public void outGoingMessage(ArrayList<User> recieverList, ImageIcon image) {
		client.addNewMessage(new Message(user, recieverList, image));
	}

	/*
	 * Skapar ett User-objekt av inparametrar, initierar Client och ListOfUsersUI 
	 * samt läser in användarens sparade kontakter från hårddisk.
	 */
	public void newUser(String userName, ImageIcon image) {
		user = new User(userName.toLowerCase(), image);
		client.connect();
		listOfUsersUI = new ListOfUsersUI(this);
		showTerminalInFrame(listOfUsersUI);
		readContacts();
	}

	public User getUser() {
		return user;
	}

	/*
	 * Skapar ett nytt IncomingMessageUI med Message-objektets olika attribut, anropar showMessageInFrame().
	 */
	public void incomingMessage(Message message) {
		IncomingMessageUI imUI;
		message.setTimeToReciever(new Date());
		if (message.getImage() == null) {
			imUI = new IncomingMessageUI(message.getSender().getUserName(), message.getSender().getImage(),
					message.getText());
		} else if (message.getText() == null) {
			imUI = new IncomingMessageUI(message.getSender().getUserName(), message.getSender().getImage(),
					message.getImage());
		} else {
			imUI = new IncomingMessageUI(message.getSender().getUserName(), message.getSender().getImage(),
					message.getText(), message.getImage());
		}
		showMessageInFrame(imUI);
	}

	/*
	 * Uppdaterar UI med ny lista med uppkopplade användare
	 */
	public void updateOnlineUsersList(ArrayList<User> onlineUsersList) {
		this.onlineUsersList = onlineUsersList;
		listOfUsersUI.addOnlineUserLabels(onlineUsersList);
	}

	/*
	 * Läser in användarens kontakter från hårddisk.
	 */
	public void readContacts() {
		int size;
		ArrayList<User> temp = new ArrayList<User>();
		File contactFile = new File("files/contacts" + user.getUserName() + ".dat");
		if (contactFile.exists()) {
			try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(contactFile))) {
				size = ois.readInt();
				for (int i = 0; i < size; i++) {
					temp.add((User) ois.readObject());
				}
				contacts.addAll(temp);
				listOfUsersUI.addContactListLabels(contacts);
			} catch (IOException | ClassNotFoundException e) {
				e.printStackTrace();
			}
		}
	}

	/*
	 * Skriver användarens kontakter till hårddisk.
	 */
	public void saveContacts() {
		try (ObjectOutputStream oos = new ObjectOutputStream(
				new FileOutputStream("files/contacts" + user.getUserName() + ".dat"))) {
			oos.writeInt(contacts.size());
			for (int i = 0; i < contacts.size(); i++) {
				oos.writeObject(contacts.get(i));
			}
			System.out.println("Kontakter sparade");
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/*
	 * Lägger till User-objekt till contacts
	 */
	public void addContact(User user) {
		contacts.add(user);
		listOfUsersUI.addContactListLabels(contacts);
	}

	/*
	 * Tar bort User-objekt från contacts
	 */
	public void removeContact(int index) {
		contacts.remove(index);
		listOfUsersUI.addContactListLabels(contacts);
	}

	public void disconnect() {
		client.disconnect();
		saveContacts();
	}

	/*
	 * Visar IncomingMessageUI i JFrame
	 */
	private void showMessageInFrame(final IncomingMessageUI im) {
		JFrame frame = new JFrame("Nytt Meddelande!!");
		frame.add(im);
		frame.setSize(500, 500);
		frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		frame.setVisible(true);
		frame.setLocation(0, 0);
		frame.setResizable(false);
		im.setFrame(frame);
	}
	
	/*
	 * Visar MessageUI i JFrame
	 */
	private void writeMessage(final MessageUI nmUI) {
		JFrame frame = new JFrame("Skicka meddelande");
		frame.add(nmUI);
		frame.setSize(500, 500);
		frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		frame.setVisible(true);
		frame.setLocation(0, 0);
		frame.setResizable(false);
		nmUI.setFrame(frame);
	}

	/*
	 * Visar ListOfUsersUI i JFrame
	 */
	private void showTerminalInFrame(final ListOfUsersUI ui) {
		JFrame frame = new JFrame("Terminal");
		frame.add(ui);
		frame.pack();
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setVisible(true);
		frame.setLocation(0, 0);
		frame.setResizable(false);
	}
}
