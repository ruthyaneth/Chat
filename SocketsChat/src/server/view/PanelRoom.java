package server.view;

import java.awt.Color;

import javax.swing.JPanel;
import javax.swing.JTextField;

/**
 * UNIVERSIDAD PEDAGOGICA Y TECNOLOGICA DE COLOMBIA
 * FACULTAD DE INGENIERIA.
 * ESCUELA DE INGENIERIA DE SISTEMAS Y COMPUTACION.
 * PRESENTADO A: Ing Helver Valero.
 * PROGRAMACION III
 * Clase  
 * @author  Jenny Quesada , Ruth Rojas
 */
public class PanelRoom extends JPanel{

	//-------Atributtes--------

	private JTextField txWrite;

	//------Builder------
	
	public PanelRoom() {
	init();
	}
	
	//------Methods--------
	
	public void init(){
	 initThis();
	 intiTx();
	}
	public void initThis(){
		this.setBackground(Color.blue);
	}
	
	
	public void intiTx(){
		this.txWrite = new JTextField();
		this.add(txWrite);
	}

}
