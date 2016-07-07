package server.view;

import java.awt.BorderLayout;

import javax.swing.BorderFactory;
import javax.swing.JFrame;
import javax.swing.JPanel;

import server.Constants.ConstantsView;

/**
 * UNIVERSIDAD PEDAGOGICA Y TECNOLOGICA DE COLOMBIA
 * FACULTAD DE INGENIERIA.
 * ESCUELA DE INGENIERIA DE SISTEMAS Y COMPUTACION.
 * PRESENTADO A: Ing Helver Valero.
 * PROGRAMACION III
 * Clase de ventana principal donde  se podra ver los clientes  que se han conectado al chat. 
 * @author  Jenny Quesada , Ruth Rojas
 */

public class WindowServer extends JFrame {
	
	//-------Atributtes--------
	private PanelAdmi panelAdmi;
	private PanelRoom panelRoom;
	private PanelButton panelButton;
	//------Builder-------
	
	public WindowServer() {
		init();
	}
	
	//------Methods--------
	
	public void init(){
		initThis();
		intiPanelAdmi();
		intiPanelRoom();
		initPanelButton();
	}
	
	public void initThis(){
		
		this.setTitle(ConstantsView.DEFAULT_NAME);
		this.setSize(ConstantsView.DEFAULT_SIZE, ConstantsView.DEFAULT_SIZE);
		this.setResizable(false);
		this.setLocationRelativeTo(null);
		this.setDefaultCloseOperation(EXIT_ON_CLOSE);
	}
	
	public void intiPanelAdmi(){
		
		this.panelAdmi = new PanelAdmi();
		this.panelAdmi.setBorder(BorderFactory.createTitledBorder(ConstantsView.DEFAULT_BORDER_ADMI));
		this.add(panelAdmi,BorderLayout.WEST);
	}
	
	public void intiPanelRoom(){
		
		this.panelRoom = new PanelRoom();
		this.add(panelRoom, BorderLayout.EAST);
	}
	
	public void initPanelButton(){
		
		this.panelButton = new PanelButton();
		this.add(panelButton,BorderLayout.SOUTH);
	}
}
