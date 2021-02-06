package gruppuppgift;

import java.io.ObjectOutputStream;
import java.net.Socket;

/*
 * Klassen lagrar Socket och ObjectOutputStream för en uppkopplad klient
 */
public class ClientAddress {
	private ObjectOutputStream oos;
	private Socket socket;

	public ClientAddress(ObjectOutputStream oos, Socket socket) {
		this.oos = oos;
		this.socket = socket;
	}
	
	public ObjectOutputStream getOos() {
		return this.oos;
	}
	
	public Socket getSocket() {
		return this.socket;
	}
}
