package client.view;

import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;

public class PanelStatusInfo extends JPanel {

	private static final long serialVersionUID = 8244830442483670484L;

	JLabel userStatusLabel = new JLabel();
	String[] listStatus = { "Available", "Busy", "Invisible" };
	JComboBox selectStatus = new JComboBox(listStatus); 

	public PanelStatusInfo(String username) {
		super();
		selectStatus.setSelectedItem("Available");
		this.add(userStatusLabel);
		this.add(selectStatus);
		userStatusLabel.setText("Nickname: " + username + "                   Status: ");
	}

	public void sendTxtUserStatus(String text) {
		userStatusLabel.setText(text);
	}
}
