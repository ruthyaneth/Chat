package client;

import javax.swing.JLabel;
import javax.swing.JPanel;

public class FirstPanel extends JPanel {

	private static final long serialVersionUID = 8244830442483670484L;

	JLabel userStatusLabel = new JLabel();

	public FirstPanel() {
		super();
	}

	public void sendTxtUserStatus(String text) {
		userStatusLabel.setText(text);
	}
}
