package gruppuppgift;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.ArrayList;

/*
 * Client hanterar all kommunikation med servern.
 */

public class Client {
	private Socket socket;
	private ClientController controller;
	private String host;
	private int port;
	private boolean connected;
	private ClientOutput clientOutput;
	private MessageListener messageListener;

	public Client(String host, int port, ClientController controller) {
		this.host = host;
		this.port = port;
		this.controller = controller;
		controller.addClient(this);
	}

	/*
	 * Kopplar upp mot server
	 */
	public void connect() {
		try {
			if (!connected) {
				socket = new Socket(host, port);
				clientOutput = new ClientOutput();
				messageListener = new MessageListener();
				messageListener.start();
				connected = true;
			}
		} catch (IOException e) {
		}
	}

	/*
	 * Kopplar ner från server
	 */
	public void disconnect() {
		if (connected) {
			try {
				socket.close();
				messageListener.interrupt();
				clientOutput.interrupt();
			} catch (IOException e) {
				System.err.println(e);
			}
			connected = false;
		}
	}

	/*
	 * Anropar den inre klassen ClientOutput med ett Message-objekt
	 */
	public void addNewMessage(Message message) {
		clientOutput.newMessage(message);
	}

	/*
	 * Hanterar all output till Server. D.v.s. User-objekt och meddelande.
	 */
	private class ClientOutput extends Thread {
		private MessageBuffer<Message> messageBuffer;

		public ClientOutput() {
			messageBuffer = new MessageBuffer<>();
			start();
		}

		public void run() {
			try (ObjectOutputStream oos = new ObjectOutputStream(socket.getOutputStream())) {
				oos.writeObject(controller.getUser());
				while (true) {
					oos.writeObject(messageBuffer.get());
				}
			} catch (IOException | InterruptedException e) {
				System.err.println(e);
			}
		}

		public void newMessage(Message message) {
			messageBuffer.put(message);
		}
	}

	/*
	 * Lyssnar efter input från servern i form av meddelande och listor med
	 * uppkopplade användare
	 */
	private class MessageListener extends Thread {
		public void run() {
			try (ObjectInputStream ois = new ObjectInputStream(socket.getInputStream())) {
				while (!Thread.interrupted()) {
					try {
						Object obj = ois.readObject();
						if (obj instanceof Message) {
							controller.incomingMessage((Message) obj);
						} else {
							controller.updateOnlineUsersList((ArrayList<User>) obj);
						}

					} catch (ClassNotFoundException | IOException e) {
						ois.close();
					}
				}
			} catch (IOException e2) {
			}
		}
	}
}
