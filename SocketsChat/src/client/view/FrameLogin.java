package client.view;

import java.io.IOException;

import javax.swing.JFrame;

import client.Controller;
import client.constants.ConstantsView;
import config.HandlerLanguage;
import config.HandlerProperties;
/**
 * 
 * @author Ruth Rojas, Jenny Quesada
 * Ventana del logeo de un cliente
 */
public class FrameLogin extends JFrame {
	

	private static final long serialVersionUID = -301099811934446277L;
	private PanelLogin panelLogin;
	private Controller controller;

	public FrameLogin(Controller controller) {
		super();
		this.setSize(ConstantsView.SIZE_HEIGHT_FRAME_LOGIN, ConstantsView.SIZE_WIDTH_FRAME_LOGIN);
		this.setLocationRelativeTo(null);
		this.controller = controller;
		panelLogin = new PanelLogin(controller);
		this.add(panelLogin);
		try {
			changeLenguage();
		} catch (IOException e) {
			e.printStackTrace();
		}
		this.setVisible(true);
	}
	
	public PanelLogin getPanelLogin() {
		return panelLogin;
	}

	public void sendVisible(boolean visible) {
		this.setVisible(visible);
	}
	public void changeLenguage() throws IOException {

		HandlerProperties handlerProperties = new HandlerProperties(HandlerLanguage.language);
		handlerProperties.load();
		this.setTitle(handlerProperties.getProperty(ConstantsView.TITLE_FRAME_LOGIN));
	}
}
