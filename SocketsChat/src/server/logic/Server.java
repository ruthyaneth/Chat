package server.logic;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashSet;

import client.logic.Client;

/**
 * UNIVERSIDAD PEDAGOGICA Y TECNOLOGICA DE COLOMBIA
 * FACULTAD DE INGENIERIA.
 * ESCUELA DE INGENIERIA DE SISTEMAS Y COMPUTACION.
 * PRESENTADO A: Ing Helver Valero.
 * PROGRAMACION III
 * Clase principal del servidor el cual se va a conectar
 * @author  Jenny Quesada , Ruth Rojas
 */
public class Server {
	
	//-------Atributtes--------
	
	private ServerSocket serverSocket;
	private PrintWriter serverIn; 
	private PrintWriter serverOut;
	private HashSet<PrintWriter> acoount;
	private int port;
	private String ip ;
	
	//------Builder------
	
	public Server() {
	
		this.serverSocket = null;
		this.serverIn = null; 
		this.serverOut = null;
		this.acoount = new HashSet<PrintWriter>();
		
	}
	
	
	//------Methods--------
	
	public void initThis(){
		
		try {
			this.serverSocket = new ServerSocket(300);
			System.out.println(InetAddress.getLocalHost().getAddress());
			System.out.println(serverSocket.getLocalPort());
			while(true){
//			Runnable client =  new Client(serverSocket.accept(), this);
//			Thread clientThread = new Thread(client);
//			clientThread.start();
			}
		} catch (IOException e) {
			
			}
		
		}
		
	
	

}
