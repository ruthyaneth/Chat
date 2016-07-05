package client.view;

import java.io.IOException;

import javax.swing.JMenuBar;
import javax.swing.JMenuItem;

import client.constants.ConstantsView;
import config.HandlerLanguage;
import config.HandlerProperties;

public class MenuChatRoom extends JMenuBar {

	private static final long serialVersionUID = -791152508593822380L;
	private JMenuItem fileTransfer;
	private JMenuItem voiceCall;
	private JMenuItem chatGroup;
	
	public MenuChatRoom() {
	super();
	addMenuFileTransfer();
	voiceCall = new JMenuItem("Voice Call");
	chatGroup = new JMenuItem("Create Group Chat");
	}
	
	private void addMenuFileTransfer(){
	fileTransfer = new JMenuItem();
	this.add(fileTransfer);
		
	}
	public void changeLenguage() throws IOException {

		HandlerProperties handlerProperties = new HandlerProperties(HandlerLanguage.language);
		handlerProperties.load();
		this.fileTransfer.setText(handlerProperties.getProperty(ConstantsView.T_MENU_FILE_TRANSFER));
	}

}
