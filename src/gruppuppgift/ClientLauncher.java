package gruppuppgift;

import javax.swing.JFrame;
import javax.swing.SwingUtilities;

/*
 * Main-metod för klientsidan
 */
public class ClientLauncher {

	public static void main(String[] args) {
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				ClientController clientController = new ClientController();
				Client client = new Client("localhost", 60000, clientController); // Se till att port matchar i ServerLauncher
				LoginUI ui = new LoginUI(clientController);
				
				JFrame frame = new JFrame("Logga in!");
				frame.add(ui);
				frame.pack();
				frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
				frame.setVisible(true);
				frame.setLocation(0, 0);
				frame.setResizable(false);
				ui.setFrame(frame);
			}
		});
	}

}
