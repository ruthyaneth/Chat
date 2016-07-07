package server.view;

import java.awt.Color;

import javax.swing.JButton;
import javax.swing.JPanel;

/**
 * UNIVERSIDAD PEDAGOGICA Y TECNOLOGICA DE COLOMBIA
 * FACULTAD DE INGENIERIA.
 * ESCUELA DE INGENIERIA DE SISTEMAS Y COMPUTACION.
 * PRESENTADO A: Ing Helver Valero.
 * PROGRAMACION III
 * Clase  de constantes de la parte visual. 
 * @author  Jenny Quesada , Ruth Rojas
 */
public class PanelButton extends JPanel {
	
	//-------Atributtes-------
	private JButton btnSend;
	
	//------Builder------
	
	public PanelButton() {
	inti();
	}
	
	//------Methods-------
	
	public void inti(){
		intiThis();
	}
	
	public void intiThis(){
		this.setBackground(Color.red);
	}
}
