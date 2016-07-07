package server.view;

import java.awt.Color;

import javax.swing.BorderFactory;
import javax.swing.JPanel;
import javax.swing.JTextField;

/**
 * UNIVERSIDAD PEDAGOGICA Y TECNOLOGICA DE COLOMBIA
 * FACULTAD DE INGENIERIA.
 * ESCUELA DE INGENIERIA DE SISTEMAS Y COMPUTACION.
 * PRESENTADO A: Ing Helver Valero.
 * PROGRAMACION III
 * Clase  de panel que contiene los usuarios conectados a la aplicacion
 * @author  Jenny Quesada , Ruth Rojas
 */
public class PanelAdmi  extends JPanel{
	
	//------Atributtes------
	
	private JTextField txClientCnnect;
	
	//------Builder-------
	
	public PanelAdmi() {
		init();
	}
	
	//------Methods-------
	
	public void  init(){
		initThis();
		initTx();
	}
	
	public void initThis(){
		this.setBackground(Color.green);
	}
	
	public void initTx(){
		
		this.txClientCnnect = new JTextField("Prueba");
		this.setBorder( BorderFactory.createTitledBorder("Prueba1"));
		this.add(txClientCnnect);
	}
	

}
