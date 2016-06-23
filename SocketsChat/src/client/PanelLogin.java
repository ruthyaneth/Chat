package client;

import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;

public class PanelLogin  extends JPanel{

	/**
	 * 
	 */
	private static final long serialVersionUID = 3938774627595679499L;
	
	JTextField usernameTextField = new JTextField(20);
	JPasswordField passwordLogin = new JPasswordField(20); 
	
	public PanelLogin() {
		super();
		this.add(usernameTextField);
	}
	public void sendTextPassword(String text){
		passwordLogin.setText(text);
	}
	public void sendTextUserName( String text){
		usernameTextField.setText(text);
	}

}
