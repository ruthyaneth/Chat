package config;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;
/**
 * UNIVERSIDAD PEDAGOGICA Y TECNOLOGICA DE COLOMBIA
 * FACULTAD DE INGENIERIA.
 * ESCUELA DE INGENIERIA DE SISTEMAS Y COMPUTACION.
 * PRESENTADO A: Ing Helver Valero.
 * PROGRAMACION III
 * Clase donde configura cada una de los lenguajes que escoge el  ususario 
 * @author Adrian Parra, Jenny Quesada, Daniel Reyes , Ruth Rojas
 */

public class HandlerProperties extends Properties {

	//------Atributtes------
	
	private static final long serialVersionUID = 1L;
	private String fileName;

	//-----Builder------
	
	public HandlerProperties(String fileName) {
		this.fileName = fileName;
	}

	//------Methods-------
	
	public void load() throws IOException {
		InputStream input;
		try {
			input = new FileInputStream(fileName);
			load(input);
			input.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
	}
	
	public void save() throws IOException{
		FileOutputStream output = new FileOutputStream(fileName);
		store(output, null);
		output.close();
	}
}
