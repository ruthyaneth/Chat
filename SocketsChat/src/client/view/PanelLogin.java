package client.view;

import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
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

/**
 * 
 * @author Ruth Rojas, Jenny Quesada Componentes graficos del registro del
 *         cliente
 */
public class PanelLogin extends JPanel {

	private static final long serialVersionUID = 3938774627595679499L;

	JTextField usernameTextFieldLogin;
	JPasswordField passwordLogin;
	JButton loginButton;
	JButton buttonRegister;
	JLabel labelUserName;
	JLabel labelPassword;
	Controller controller;

	public PanelLogin(Controller controller) {
		super();
		this.controller = controller;
		init();
		this.setLayout(new FlowLayout(FlowLayout.CENTER));
		try {
			changeLenguage();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private void init() {
		addLabelUserName();
		addUserNameFieldLogin();
		addLabelPassword();
		addPasswordLogin();
		addButtonLogin();
		addButtonRegister();
	}

	public void sendTextPassword(String text) {
		passwordLogin.setText(text);
	}

	public void sendTextUserName(String text) {
		usernameTextFieldLogin.setText(text);
	}

	private void addButtonLogin() {
		loginButton = new JButton();
		loginButton.setActionCommand(ConstantsListener.A_LOGIN_BUTTON);
		loginButton.addActionListener(controller);
		this.add(loginButton);
	}

	private void addButtonRegister() {
		buttonRegister = new JButton();
		buttonRegister.setActionCommand(ConstantsListener.A_REGISTER_BUTTON);
		buttonRegister.addActionListener(controller);
		this.add(buttonRegister);
	}

	private void addUserNameFieldLogin() {
		usernameTextFieldLogin = new JTextField(20);
		add(usernameTextFieldLogin);
	}

	private void addPasswordLogin() {
		passwordLogin = new JPasswordField(20);
		this.add(passwordLogin);
	}

	private void addLabelUserName() {
		labelUserName = new JLabel();
		this.add(labelUserName);
	}

	private void addLabelPassword() {
		labelPassword = new JLabel();
		this.add(labelPassword);

	}

	public void changeLenguage() throws IOException {

		HandlerProperties handlerProperties = new HandlerProperties(HandlerLanguage.language);
		handlerProperties.load();
		loginButton.setText(handlerProperties.getProperty(ConstantsView.BUTTON_LOGIN));
		buttonRegister.setText(handlerProperties.getProperty(ConstantsView.BUTTON_REGISTER));
		labelUserName.setText(handlerProperties.getProperty(ConstantsView.LABEL_USER_NAME));
		labelPassword.setText(handlerProperties.getProperty(ConstantsView.LABEL_LOGIN_PASSWORD));
	}
}
