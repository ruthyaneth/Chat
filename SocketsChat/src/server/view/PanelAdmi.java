package server.view;

import java.awt.Color;

import javax.swing.BorderFactory;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.JTextPane;

import server.Constants.ConstantsView;

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
	
	private JTextArea txClientCnnect;
	private JScrollPane scrollPane;
	
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
	
	}
	
	public void initTx(){
		
		this.txClientCnnect = new JTextArea(24,12);
		this.setBorder( BorderFactory.createTitledBorder(ConstantsView.DEFAULT_BORDER_ROOM));
		this.scrollPane = new JScrollPane(txClientCnnect);
		this.add(scrollPane);
	}
	

}
