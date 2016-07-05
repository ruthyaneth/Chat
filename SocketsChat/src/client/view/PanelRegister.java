package client.view;

import java.awt.FlowLayout;
import java.io.IOException;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;

import client.Controller;
import client.constants.ConstantsListener;
import client.constants.ConstantsView;
import config.HandlerLanguage;
import config.HandlerProperties;

public class PanelRegister extends JPanel {

	private static final long serialVersionUID = 3215392914306456931L;
	private Controller controller;
	
	private JLabel labelUsername;
	private JTextField usernameRegister;
	private JLabel labelNewPassword;
	private JPasswordField passwordRegister;
	private JLabel labelRePassword;
	private JPasswordField passwordRetypeRegister;
	private JButton buttonRegisterOk ;
	private JButton backButton;
	
	public PanelRegister(Controller controller) {
		super();
		this.controller = controller;
		init();
		new FlowLayout(FlowLayout.LEFT);
		try {
			changeLenguage();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	private void init(){
		addLabelUsername();
		addFielUserNameRegister();
		addLabelPassword();
		addFielPasswordReg();
		addLabelRePassword();
		addPasswordRePassReg();
		addButtonRegisterOk();
		addBackButton();
	}
	private void addLabelUsername(){
		labelUsername = new JLabel();
		this.add(labelUsername);
	}
	private void addFielUserNameRegister(){
		usernameRegister = new JTextField(20);
		this.add(usernameRegister);
	}
	
	private void addButtonRegisterOk(){
		buttonRegisterOk = new JButton();
		buttonRegisterOk.setActionCommand(ConstantsListener.A_BUTTON_REGISTER_OK);
		buttonRegisterOk.addActionListener(this.controller);
		this.add(buttonRegisterOk);
	}
	private void addBackButton(){
		backButton = new JButton();
		backButton.setActionCommand(ConstantsListener.A_BUTTON_BACK);
		backButton.addActionListener(controller);
		this.add(backButton);
	}
	private void addFielPasswordReg(){
		passwordRegister = new JPasswordField(20);
		this.add(passwordRegister);
	}
	private void addPasswordRePassReg(){
		passwordRetypeRegister = new JPasswordField(20);
		this.add(passwordRetypeRegister);
	}
	private void addLabelPassword(){
		labelNewPassword = new JLabel();
		this.add(labelNewPassword);
	}
	private void addLabelRePassword(){
		labelRePassword = new JLabel();
		this.add(labelRePassword);
	}
	public JPasswordField getPasswordRegister() {
		return passwordRegister;
	}
	public JPasswordField getPasswordRetypeRegister() {
		return passwordRetypeRegister;
	}
	public JTextField getUsernameRegister() {
		return usernameRegister;
	}
	
	public void changeLenguage() throws IOException {

		HandlerProperties handlerProperties = new HandlerProperties(HandlerLanguage.language);
		handlerProperties.load();
		buttonRegisterOk.setText(handlerProperties.getProperty(ConstantsView.BUTTON_REGISTER_OK));
		backButton.setText(handlerProperties.getProperty(ConstantsView.BUTTON_BACK));
		labelUsername.setText(handlerProperties.getProperty(ConstantsView.LABEL_USERNAME));
		labelNewPassword.setText(handlerProperties.getProperty(ConstantsView.LABEL_PASSWORD));
		labelRePassword.setText(handlerProperties.getProperty(ConstantsView.LABEL_RE_PASSWORD));
	}
}
