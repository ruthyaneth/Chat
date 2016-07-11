package server.view;

import java.awt.Color;
import java.awt.GridBagConstraints;

import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.JTextPane;

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

	private JTextArea txArea;
	private JScrollPane scrollPane;

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
	
	}
	
	
	public void intiTx(){
		
		this.txArea = new JTextArea(24,32);
		this.scrollPane = new JScrollPane(txArea);
		this.add(scrollPane);
		
		
	}

}
