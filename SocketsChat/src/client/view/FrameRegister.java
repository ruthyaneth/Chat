package client.view;

import java.io.IOException;

import javax.swing.JFrame;

import client.Controller;
import client.constants.ConstantsView;
import config.HandlerLanguage;
import config.HandlerProperties;

public class FrameRegister  extends JFrame {

	private static final long serialVersionUID = 4104457692748315426L;
	private PanelRegister panelRegister;
	private Controller controller;
	
	public FrameRegister(Controller controller) {
		super();
		this.setSize(ConstantsView.SIZE_HEIGHT_FRAME_REGISTER, ConstantsView.SIZE_WIDTH_FRAME_REGISTER);
		this.setLocationRelativeTo(null);
		this.controller= controller;
		panelRegister = new PanelRegister(controller);
		this.add(panelRegister);
		try {
			changeLenguage();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	public PanelRegister getPanelRegister() {
		return panelRegister;
	}

	public void changeLenguage() throws IOException {

		HandlerProperties handlerProperties = new HandlerProperties(HandlerLanguage.language);
		handlerProperties.load();
		this.setTitle(handlerProperties.getProperty(ConstantsView.TITLE_FRAME_REGISTER));
	}
}
