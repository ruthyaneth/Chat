package client;

import java.awt.BorderLayout;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;

public class FrameChatPrivate extends JFrame {

	private static final long serialVersionUID = 4284944818008206115L;
	private JLabel privateChatLabel = new JLabel("Private Chat");
	private JTextArea privateTextArea = new JTextArea();
	private JScrollPane privateChatScroll = new JScrollPane(privateTextArea);
	
	JPanel privateChatPanel = new JPanel(new BorderLayout());
	JTextField privateChatTextField = new JTextField();

	public FrameChatPrivate() {
		super();
		this.add(privateChatPanel, BorderLayout.SOUTH);
		privateChatPanel.add(privateChatTextField, BorderLayout.CENTER);
	}

	public void sendTxtLabelPrivateChat(String txt, String userRequest) {
		privateChatLabel.setText(txt + " " + userRequest);
	}

	public void sendAppend(String userRequest, String content) {
		privateTextArea.append(userRequest + ": " + content + "\n");
	}

	public void sendTxtArea(String txt) {
		privateTextArea.setText(txt);
	}
	public void sendTextPrivateChatTextFiel(String text){
		privateChatTextField.setText(text);
	}
	public void sendTextArea(String txt){
		privateTextArea.setText("");
	}
}
