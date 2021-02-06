package gruppuppgift;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;

import javax.swing.ImageIcon;

/*
 * Message-klassen representerar ett meddelande som består av text + bild eller text/bild med tillhörande avsändare och mottagare.
 */

public class Message implements Serializable {
	private User sender;
	private ArrayList<User> recieverList;
	private String text;
	private ImageIcon image;
	private Date timeToServer; // Tidpunkt då meddelande togs emot av server
	private Date timeToReciever; //Tidpunkt då meddelande togs emot av mottagare

	public Message(User sender, ArrayList<User> recieverList, String text, ImageIcon image) {
		this.sender = sender;
		this.recieverList = recieverList;
		this.text = text;
		this.image = image;
	}

	public Message(User sender, ArrayList<User> recieverList, String text) {
		this.sender = sender;
		this.recieverList = recieverList;
		this.text = text;
	}

	public Message(User sender, ArrayList<User> recieverList, ImageIcon image) {
		this.sender = sender;
		this.recieverList = recieverList;
		this.image = image;
	}

	public Date getTimeToServer() {
		return timeToServer;
	}

	public void setTimeToServer(Date timeToServer) {
		this.timeToServer = timeToServer;
	}

	public User getSender() {
		return sender;
	}

	public ArrayList<User> getRecieverList() {
		return recieverList;
	}

	public String getText() {
		return text;
	}

	public ImageIcon getImage() {
		return image;
	}

	public Date getTimeToReciever() {
		return timeToReciever;
	}

	public void setTimeToReciever(Date timeToReciever) {
		this.timeToReciever = timeToReciever;
	}

}
