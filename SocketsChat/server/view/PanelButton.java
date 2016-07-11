package server.view;

import java.awt.Color;
import java.awt.GridLayout;

import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextPane;

import server.Constants.ConstantsView;

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
	private JTextArea txPane;
	private JScrollPane scrollPane;
	//------Builder------
	
	public PanelButton() {
	inti();
	}
	
	//------Methods-------
	
	public void inti(){
		intiThis();
		initComponent();
	}
	
	public void intiThis(){
	
		this.setLayout(new GridLayout(1,2));
	}
	
	public void initComponent(){
		
		this.btnSend = new JButton(ConstantsView.DEFAULT_NAME_BUTTON);
		this.add(btnSend);
		this.txPane = new JTextArea(2,12);
		this.scrollPane = new JScrollPane(txPane);
		this.add(scrollPane);
		
	}
}
