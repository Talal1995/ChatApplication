package gruppuppgift;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.ObjectOutputStream;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

/*
 * Hanterar logik på serversidan samt kommunikation mellan Server, ServerUI och diverse hjälpklasser (objektsamlingar etc)
 */

public class ServerController {
	private Clients clients = new Clients();
	private UnsentMessages unsentMessages = new UnsentMessages();
	private Server server;
	private ServerUI serverUi;

	public void addServer(Server server) {
		this.server = server;
	}

	public void addServerUi(ServerUI serverUi) {
		this.serverUi = serverUi;
	}

	/*
	 * Anropas när en ny klient ansluter.
	 */
	public void newClient(User user, ObjectOutputStream oos, Socket socket) {
		clients.put(user, new ClientAddress(oos, socket));
		updateUserList(); // Uppdaterar lista över uppkopplade användare
		checkUnsentMessages(user); // Kollar om det finns osända meddelanden för användaren
	}

	/*
	 * Anropar server med ny lista över uppkopplade användare
	 */
	public void updateUserList() {
		ArrayList<User> temp = getConnectedUsers();
		for (int i = 0; i < temp.size(); i++) {
			ClientAddress tempAddress = clients.getClient(temp.get(i));
			server.newOnlineUserSender(temp, tempAddress);
		}
	}

	/*
	 * Returnerar alla uppkopplade användare, d.v.s. alla nycklar i Clients.
	 */
	public ArrayList<User> getConnectedUsers() {
		return clients.getKeySet();
	}

	/*
	 * Tar bort användare från Clients när denne kopplar ner.
	 */
	public void removeClient(User user) {
		clients.remove(user);
		updateUserList();
	}

	/*
	 * Kontrollerar om det finns några osända meddelande för nyligen uppkopplad
	 * klient/användare
	 */
	public void checkUnsentMessages(User user) {
		if (unsentMessages.contains(user)) {
			ArrayList<Message> temp = unsentMessages.getUnsentMessageList(user);
			for (int i = 0; i < temp.size(); i++) {
				sendMessage(temp.get(i), user);
			}
			unsentMessages.remove(user);
		}
	}

	/*
	 * Kontrollerar vilka av ett meddelandes mottagare som är online. Sparar
	 * meddelande i unsentMessages om en användare är offline.
	 */
	public void newMessage(Message message) {
		message.setTimeToServer(new Date());
		ArrayList<User> receivers = message.getRecieverList();
		for (int i = 0; i < receivers.size(); i++) {
			if (clients.contains(receivers.get(i))) {
				sendMessage(message, receivers.get(i));
			} else {
				unsentMessages.put(receivers.get(i), message);
			}
		}
	}

	/*
	 * Anropar server som skickar meddelande till angiven klient/användare
	 */
	public void sendMessage(Message message, User user) {
		server.newMessageSender(message, clients.getClient(user));
	}

	/*
	 * Läser in och returnerar all loggad trafik från hårddisk.
	 */
	private ArrayList<String> readTrafficFromFile() {
		ArrayList<String> res = new ArrayList<String>();
		String str = "";
		try (BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream("files/traffic.log")))) {
			str = br.readLine();
			while (str != null) {
				res.add(str);
				str = br.readLine();
			}
		} catch (IOException e) {
		}
		return res;
	}

	/*
	 * Sorterar ut den loggade trafik som ligger inom det angivna tidsintervallet.
	 */
	public void getTraffic(String fromDate, String toDate) throws ParseException {
		ArrayList<String> temp = readTrafficFromFile();
		ArrayList<String> res = new ArrayList<String>();
		Date start = new SimpleDateFormat("yyyy/MM/dd HH:mm").parse(fromDate);
		Date end = new SimpleDateFormat("yyyy/MM/dd HH:mm").parse(toDate);
		Date tempDate;

		for (int i = 0; i < temp.size(); i++) {
			tempDate = new SimpleDateFormat("yyyy/MM/dd HH:mm").parse(temp.get(i).substring(0, 17));
			if ((!tempDate.before(start)) && (!tempDate.after(end))) {
				res.add(temp.get(i));
			}
		}
		setTrafficToUI(res);
	}

	/*
	 * Uppdaterar UI med loggad trafik
	 */
	private void setTrafficToUI(ArrayList<String> traffic) {
		if (traffic.size() > 0) {
			serverUi.clearTextArea();
			for (int i = 0; i < traffic.size(); i++) {
				serverUi.updateTextArea(traffic.get(i));
			}
		} else {
			serverUi.noTraffic();
		}
	}

	/*
	 * Skriver loggad trafik till hårddisk
	 */
	public synchronized void logToFile(String str) {
		try (BufferedWriter bw = new BufferedWriter(
				new OutputStreamWriter(new FileOutputStream("files/traffic.log", true)))) {
			bw.write(str);
			bw.newLine();
			bw.flush();
		} catch (IOException e) {
			System.out.println(e);
		}
	}

}
