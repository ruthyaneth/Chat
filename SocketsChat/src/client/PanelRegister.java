package client;

import java.awt.FlowLayout;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;

public class PanelRegister extends JPanel {

	private static final long serialVersionUID = 3215392914306456931L;
	
	JTextField usernameRegister = new JTextField(20);
	JPasswordField passwordRegister = new JPasswordField(20);
	JPasswordField passwordRetypeRegister = new JPasswordField(20);
	JButton signupButton02 = new JButton("Register");
	JButton backButton = new JButton("Back");
	
	
	public PanelRegister() {
		super();
		new FlowLayout(FlowLayout.LEFT);
		this.add(new JLabel("New Username"));
		this.add(usernameRegister);
		this.add(new JLabel("New password"));
		this.add(passwordRegister);
		this.add(new JLabel("Re-enter password"));
		this.add(passwordRetypeRegister);
		this.add(signupButton02);
		this.add(backButton);
	}
	
	public void sendTxtUserNameRegister(String text){
		usernameRegister.setText(text);
	}
	
	public void sendTxtPasswordRegister(String text){
		passwordRegister.setText(text);
	}
	
	public void sendTextPasswordRetypeRegister(String text){
		passwordRetypeRegister.setText(text);
	}
}
