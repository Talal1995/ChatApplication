package gruppuppgift;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

/*
 * Server-klassen sköter all kommunikation med uppkopplade klienter
 */
public class Server {
	private ServerController controller;

	/*
	 * Konstruktor.
	 * port = den port servern som ska lyssna på anslutande klienter vid.
	 */
	public Server(int port, ServerController controller) {
		this.controller = controller;
		controller.addServer(this);
		new ConnectionListener(port);
	}

	/*
	 * Lyssnar efter anslutande klienter. Om en klient vill ansluta, skapas ett ClientHandler-objekt.
	 */
	private class ConnectionListener extends Thread {
		private int port;
		private SimpleDateFormat simpleDate = new SimpleDateFormat("yyyy/MM/dd HH:mm");
		private Date date;

		public ConnectionListener(int port) {
			this.port = port;
			start();
		}

		public void run() {
			Socket socket = null;
			try (ServerSocket serverSocket = new ServerSocket(port)) {
				while (true) {
					try {
						socket = serverSocket.accept();
						new ClientHandler(socket);
						date = new Date();
						controller
								.logToFile(simpleDate.format(date) + ": " + socket.getInetAddress() + " har anslutit");
					} catch (IOException e) {
						if (socket != null) {
							socket.close();
						}
						System.err.println(e);
					}
				}
			} catch (IOException e) {
				System.err.println(e);
			}
		}
	}
	
	/*
	 * Lyssnar efter input från dess respektive klient.
	 */
	private class ClientHandler extends Thread {
		private Socket socket;
		private User user;
		private SimpleDateFormat simpleDate = new SimpleDateFormat("yyyy/MM/dd HH:mm");
		private Date date;

		public ClientHandler(Socket socket) throws IOException {
			this.socket = socket;
			start();
		}

		public void run() {
			System.out.println("ClientHandler startar " + Thread.currentThread().getName() + socket.getInetAddress());
			try (ObjectInputStream ois = new ObjectInputStream(socket.getInputStream());
					ObjectOutputStream oos = new ObjectOutputStream(socket.getOutputStream())) {
				oos.flush();
				user = (User) ois.readObject();
				date = new Date();
				controller.logToFile(simpleDate.format(date) + ": " + socket.getInetAddress() + ": Användare överförd");
				controller.newClient(user, oos, socket);
				while (true) {
					controller.newMessage((Message) ois.readObject());
					date = new Date();
					controller
							.logToFile(simpleDate.format(date) + ": Nytt meddelande från: " + socket.getInetAddress());
				}
			} catch (ClassNotFoundException | IOException e) {
			}
			try {
				System.out.println("ClientHandler stänger " + Thread.currentThread().getName());
				date = new Date();
				controller.logToFile(simpleDate.format(date) + ": " + socket.getInetAddress() + ": har kopplat ner");
				socket.close();
				controller.removeClient(user);
			} catch (Exception e2) {
				System.out.println(e2);
			}
		}
	}

	/*
	 * Skapar ett nytt MessageSender-objekt, anropar dess run-metod.
	 */
	public void newMessageSender(Message message, ClientAddress client) {
		new MessageSender(message, client.getOos(), client.getSocket()).run();
	}
	
	/*
	 * Skapar ett nytt OnlineUserSender-objekt, anropar dess run-metod.
	 */
	public void newOnlineUserSender(ArrayList<User> userList, ClientAddress client) {
		new OnlineUserSender(userList, client.getOos(), client.getSocket()).run();
	}

	/*
	 * Skickar meddelande till klient
	 */
	private class MessageSender extends Thread {
		private Message message;
		private ObjectOutputStream oos;
		private Socket socket;
		private SimpleDateFormat simpleDate = new SimpleDateFormat("yyyy/MM/dd HH:mm");
		private Date date;

		public MessageSender(Message message, ObjectOutputStream oos, Socket socket) {
			this.message = message;
			this.oos = oos;
			this.socket = socket;
		}

		public void run() {
			try {
				oos.writeObject(message);
				date = new Date();
				controller.logToFile(simpleDate.format(date) + ": Meddelande skickat till: " + socket.getInetAddress());
			} catch (IOException e) {
				System.err.println(e);
			}
		}
	}

	/*
	 * Skickar ny lista över uppkopplade användare till klient
	 */
	private class OnlineUserSender extends Thread {
		private ArrayList<User> userList;
		private ObjectOutputStream oos;
		private SimpleDateFormat simpleDate = new SimpleDateFormat("yyyy/MM/dd HH:mm");
		private Date date;
		private Socket socket;

		public OnlineUserSender(ArrayList<User> userList, ObjectOutputStream oos, Socket socket) {
			this.userList = userList;
			this.oos = oos;
			this.socket = socket;
		}

		public void run() {
			try {
				oos.writeObject(userList);
				date = new Date();
				controller
						.logToFile(simpleDate.format(date) + ": Onlinelista skickad till: " + socket.getInetAddress());
			} catch (IOException e) {
				System.err.println(e);
			}
		}
	}
}
