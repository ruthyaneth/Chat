package client.view;

import java.awt.BorderLayout;
import java.awt.Color;
import java.io.IOException;

import javax.swing.BorderFactory;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

import client.Controller;
import client.constants.ConstantsView;
import config.HandlerLanguage;
import config.HandlerProperties;

public class FrameChatRoom extends JFrame {
	
	private static final long serialVersionUID = -6255597628694521753L;
	private MenuChatRoom menuChatRoom;
	private PanelStatusInfo panelStatusInfo;
	private JPanel panelListUserOnline;
	private JLabel labelUsers;
	private Controller controller;
	
	public FrameChatRoom(Controller controller,String username) {
		super();
		this.controller = controller;
		this.setSize(600, 600);
		panelStatusInfo = new PanelStatusInfo(username);
		menuChatRoom = new MenuChatRoom(controller);
		this.setJMenuBar(menuChatRoom);
		this.add(panelStatusInfo, BorderLayout.NORTH);
		addPanelListUserOnLine();
		this.setLocationRelativeTo(null);
		this.setVisible(true);
		try {
			changeLenguage();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	private void addPanelListUserOnLine(){
		panelListUserOnline = new JPanel();
		labelUsers = new JLabel();
		panelListUserOnline.add(labelUsers);
		panelListUserOnline.setBorder(BorderFactory.createLineBorder(Color.BLUE));
		this.add(panelListUserOnline, BorderLayout.EAST );
	}
	public void changeLenguage() throws IOException {

		HandlerProperties handlerProperties = new HandlerProperties(HandlerLanguage.language);
		handlerProperties.load();
		this.setTitle(handlerProperties.getProperty(ConstantsView.TITLE_FRAME_CHAT_ROOM));
		this.labelUsers.setText(handlerProperties.getProperty(ConstantsView.LABEL_LIST_USER_ONLINE));
	}
}
