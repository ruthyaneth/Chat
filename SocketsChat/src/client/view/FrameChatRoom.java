package client.view;

import java.io.IOException;

import javax.swing.JFrame;

import client.constants.ConstantsView;
import config.HandlerLanguage;
import config.HandlerProperties;

public class FrameChatRoom extends JFrame {
	
	private static final long serialVersionUID = -6255597628694521753L;
	private MenuChatRoom menuChatRoom;
	
	public FrameChatRoom() {
		super();
		menuChatRoom = new MenuChatRoom();
		this.setJMenuBar(menuChatRoom);
		this.setVisible(true);
	}
	public void changeLenguage() throws IOException {

		HandlerProperties handlerProperties = new HandlerProperties(HandlerLanguage.language);
		handlerProperties.load();
		this.setTitle(handlerProperties.getProperty(ConstantsView.TITLE_FRAME_REGISTER));
	}
}
