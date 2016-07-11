package server.main;

import server.view.WindowServer;

/**
 * UNIVERSIDAD PEDAGOGICA Y TECNOLOGICA DE COLOMBIA
 * FACULTAD DE INGENIERIA.
 * ESCUELA DE INGENIERIA DE SISTEMAS Y COMPUTACION.
 * PRESENTADO A: Ing Helver Valero.
 * PROGRAMACION III
Clase pirncipal donde se corre la ventanda  del administardor de la sala del chat. 
 * @author  Jenny Quesada , Ruth Rojas
 */
public class TestServer {

	//------Methods-------
	
	public static void main(String[] args) {
		
		WindowServer windowServer = new WindowServer();
		windowServer.setVisible(true);
	}
}
