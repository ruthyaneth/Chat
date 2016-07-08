package client.logic;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;


/**
 * UNIVERSIDAD PEDAGOGICA Y TECNOLOGICA DE COLOMBIA
 * FACULTAD DE INGENIERIA.
 * ESCUELA DE INGENIERIA DE SISTEMAS Y COMPUTACION.
 * PRESENTADO A: Ing Helver Valero.
 * PROGRAMACION III
 * Clase principal del servidor el cual se va a conectar
 * @author  Jenny Quesada , Ruth Rojas
 */
public class Client implements Runnable {
	
	//------Atributtes-------
	private Socket socket;
//	private Server server;
	private PrintWriter out;
	private String name;
	private String lastNmae;
	private String username;
	private BufferedReader bufferReader;
	
	//------Builder--------
	
//	public Client(Socket socket , Server server) {
//	
//		this.server = server;
//		this.socket = socket;
//	}

	
	//-------Methods--------
	
	@Override
	public void run() {
	
		try {
			
			bufferReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
			out = new PrintWriter(socket.getOutputStream(),true);
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}
	
//	public Client(Server server , Socket socket){
//		
//		this.server = server;
//		this.socket = socket;
//	}
//	

}
