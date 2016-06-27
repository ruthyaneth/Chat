package client.view;

import java.awt.BorderLayout;
import java.awt.GridBagConstraints;
import java.io.IOException;

import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.JTextPane;

import client.constants.ConstantsView;
import config.HandlerLanguage;
import config.HandlerProperties;

public class WindowClient extends JFrame {

	private static final long serialVersionUID = 8658573720484686524L;
	private JTextPane chatArea = new JTextPane();
	private JScrollPane chatScroll;

	public WindowClient() {
		super();
		this.setSize(ConstantsView.SIZE_WIDTH, ConstantsView.SIZE_HEIGHT);
		 chatScroll = new JScrollPane(chatArea);
		
		try {
			changeLenguage();
		} catch (IOException e) {
			e.printStackTrace();
		}
		this.setVisible(true);
		this.add(chatScroll, BorderLayout.SOUTH);
		this.setDefaultCloseOperation(EXIT_ON_CLOSE);
	}


	public JTextPane getChatArea() {
		return chatArea;
	}
	public void changeLenguage() throws IOException {
		
		HandlerProperties handlerProperties = new HandlerProperties(HandlerLanguage.language);
		handlerProperties.load();
		
		this.setTitle(handlerProperties.getProperty(ConstantsView.TITLE_WINDOW_CLIENT));
	}
}
