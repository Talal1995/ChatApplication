package gruppuppgift;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.ParseException;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridLayout;

/*
 * ServerUI. Visar all loggad trafik mellan två valda tidpunkter.
 */

public class ServerUI extends JPanel {

	private JTextArea textArea = new JTextArea();
	private JButton btnupdate = new JButton("Visa trafik");
	private JLabel txtFromDate = new JLabel("Från:");
	private JLabel txtToDate = new JLabel("Till:");
	private JTextField fromDate = new JTextField("yyyy/MM/dd HH:mm");
	private JTextField toDate = new JTextField("yyyy/MM/dd HH:mm");
	private ServerController serverController;
	private JScrollPane scrollPane = new JScrollPane(textArea);
	private JPanel btnPanel = new JPanel(new GridLayout(1, 5));
	private Font font = new Font("MONOSPACED", Font.PLAIN, 8);

	public ServerUI(ServerController serverController) {
		this.serverController = serverController;
		serverController.addServerUi(this);
		init();
	}

	/*
	 * Initierar UI med layout, knappar osv.
	 */
	private void init() {
		setSize(500, 500);
		setLayout(new BorderLayout());

		scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
		scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
		scrollPane.setPreferredSize(new Dimension(400, 400));
		textArea.setEditable(false);
		textArea.setSize(400, 400);
		textArea.setLineWrap(true);
		textArea.setWrapStyleWord(true);
		add(scrollPane, BorderLayout.CENTER);

		fromDate.setFont(font);
		toDate.setFont(font);

		btnPanel.add(txtFromDate);
		btnPanel.add(fromDate);
		btnPanel.add(txtToDate);
		btnPanel.add(toDate);
		btnPanel.add(btnupdate);

		add(btnPanel, BorderLayout.SOUTH);
		btnupdate.addActionListener(new ButtonListener());
		

	}

	public void updateTextArea(String str) {
		textArea.append(str + "\n");
	}

	public void clearTextArea() {
		textArea.setText("");
	}

	public void noTraffic() {
		textArea.setText("Ingen trafik mellan valda tidpunkter");
	}

	private class ButtonListener implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			try {
				serverController.getTraffic(fromDate.getText(), toDate.getText());
			} catch (ParseException e1) {
				JOptionPane.showMessageDialog(null, "Datum måste vara i format: yyyy/MM/dd HH:mm");
			}
		}
	}
}