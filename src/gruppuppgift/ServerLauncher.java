package gruppuppgift;

import java.net.InetAddress;
import java.net.UnknownHostException;
import javax.swing.JFrame;

/*
 * Main-metod för serversidan
 */

public class ServerLauncher{
	public static void main(String[] args) {
		ServerController serverController = new ServerController();
		Server server = new Server(60000, serverController);
		ServerUI serverUi = new ServerUI(serverController);
		
		// Kontrollera utskrift vid start, den IP som skrivs ut måste justeras i ClientLauncher
		try {
			System.out.println(InetAddress.getLocalHost().getHostAddress());
		} catch (UnknownHostException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		JFrame frame = new JFrame("ServerUI");
		frame.add(serverUi);
		frame.setSize(400, 500);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setVisible(true);
		frame.setLocation(0, 0);
		frame.setResizable(false);
	}
}
