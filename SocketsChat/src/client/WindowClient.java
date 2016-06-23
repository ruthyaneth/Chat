package client;

import java.io.IOException;

import javax.swing.JFrame;

import config.HandlerLanguage;
import config.HandlerProperties;

public class WindowClient  extends JFrame {

	/**
	 * 
	 */
	private static final long serialVersionUID = 8658573720484686524L;

	public WindowClient() {
		super();
		this.setSize(ConstantsView.SIZE_WIDTH, ConstantsView.SIZE_HEIGHT);
		this.setDefaultCloseOperation(EXIT_ON_CLOSE);
		try {
			changeLenguage();
		} catch (IOException e) {
			e.printStackTrace();
		}
		this.setVisible(true);
	}
	public void changeLenguage() throws IOException{

		HandlerProperties handlerProperties = new HandlerProperties(HandlerLanguage.language);
		handlerProperties.load();

		this.setTitle(handlerProperties.getProperty(ConstantsView.TITLE_WINDOW_CLIENT));
	}

}
