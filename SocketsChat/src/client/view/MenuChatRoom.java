package client.view;

import java.io.IOException;

import javax.swing.JMenuBar;
import javax.swing.JMenuItem;

import client.Controller;
import client.constants.ConstantsListener;
import client.constants.ConstantsView;
import config.HandlerLanguage;
import config.HandlerProperties;

public class MenuChatRoom extends JMenuBar {

	private static final long serialVersionUID = -791152508593822380L;
	private JMenuItem fileTransfer;
	private JMenuItem voiceCall;
	private JMenuItem chatGroup;
	private Controller controller;

	public MenuChatRoom(Controller controller) {
		super();
		this.controller = controller;
		addMenuFileTransfer();
		addMenuVoiceCall();
		addMenuChatGoup();
		try {
			changeLenguage();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private void addMenuFileTransfer() {
		fileTransfer = new JMenuItem();
		fileTransfer.addActionListener(controller);
		fileTransfer.setActionCommand(ConstantsListener.A_MENU_FILE_TRANSFER);
		this.add(fileTransfer);
	}

	private void addMenuVoiceCall() {
		voiceCall = new JMenuItem();
		voiceCall.addActionListener(controller);
		voiceCall.setActionCommand(ConstantsListener.A_MENU_VOICE_CALL);
		this.add(voiceCall);
	}

	private void addMenuChatGoup() {
		chatGroup = new JMenuItem();
		chatGroup.addActionListener(controller);
		chatGroup.setActionCommand(ConstantsListener.A_MENU_CHAT_GROUP);
		this.add(chatGroup);
	}

	public JMenuItem getFileTransfer() {
		return fileTransfer;
	}

	public void changeLenguage() throws IOException {

		HandlerProperties handlerProperties = new HandlerProperties(HandlerLanguage.language);
		handlerProperties.load();
		this.fileTransfer.setText(handlerProperties.getProperty(ConstantsView.T_MENU_FILE_TRANSFER));
		this.voiceCall.setText(handlerProperties.getProperty(ConstantsView.T_MENU_VOICE_CALL));
		this.chatGroup.setText(handlerProperties.getProperty(ConstantsView.T_MENU_GROUP_CHAT));
	}

}
