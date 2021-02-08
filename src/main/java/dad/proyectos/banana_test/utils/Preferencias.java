package dad.proyectos.banana_test.utils;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

/**
 * Clase gestora de las preferencias del usuario
 * respecto a la aplicación. Se encarga de no sólo
 * almacenar dichas preferencias, sino también
 * de gestionar el fichero config.properties
 * correspondiente.
 * 
 * La ruta por defecto de dicho fichero
 * estará en el "user.home\.BananaTest".
 * 
 * @author Crmprograming
 *
 */
public abstract class Preferencias {

	/**
	 * Enum con los distintos idiomas disponibles
	 * en la aplicación.
	 */
	public static enum IDIOMAS {
		ES,
		EN
	}
	
	/**
	 * Enum con los distintos temas disponibles
	 * en la aplicación.
	 */
	public static enum TEMAS {
		DEFAULT ("DEFAULT"),
		DARK ("DARK"),
		BANANA("BANANA"),
		PASTEL("PASTEL");
		
		private final String name;
		
		/**
		 * Constructor del Enum de tal forma
		 * que cada tema tenga asociado su nombre
		 * en formato String.
		 *
		 * @param string El nombre del tema
		 */
		private TEMAS(String string) {
			this.name = string;
		}
		
		@Override
		public String toString() {
			return this.name;
		}
		
		/**
		 * Método encargado de devolver todos
		 * los nombres de los temas.
		 *
		 * @return List de String con los nombres.
		 */
		public static List<String> getAll() {
			List<String> listado = new ArrayList<String>();
			for (TEMAS tema : TEMAS.values())
				listado.add(tema.name);
			return listado;
		}
		
	}
	
	public static Properties properties = new Properties();
	
	/**
	 * Método encargado de leer las preferencias en el fichero
	 * "config.properties".
	 * 
	 *  En caso de no existir dicho fichero, creará uno nuevo
	 *  con valores por defecto.
	 *
	 * @throws IOException Si hubo problemas manipulando el fichero.
	 * @throws FileNotFoundException Si el fichero no se encontró.
	 */
	public static void cargarPreferencias() throws IOException, FileNotFoundException {
		String rutaPerfil = System.getProperty("user.home");
		File file = new File(rutaPerfil + File.separatorChar + ".BananaTest" + File.separatorChar + "config.properties");
		
		if (!file.getParentFile().exists() && !file.getParentFile().mkdirs())
			throw new IllegalStateException("No se pudo crear la carpeta .BananaTest");
		
		if (file.createNewFile()) {
			crearConfiguracionInicial(file);
		}
		
		Preferencias.properties.load(new FileInputStream(file));
	}
	
	/**
	 * Método encargado de crear las preferencias por defecto en el fichero
	 * de configuración.
	 * 
	 * Los valores por defecto son:
	 * 
	 * <ul>
	 * 	<li><strong>Idioma: </strong> Español</li>
	 * 	<li><strong>Tema: </strong> Default</li>
	 * </ul>
	 *
	 * @param file the file
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	private static void crearConfiguracionInicial(File file) throws IOException {
		BufferedWriter bWriter = new BufferedWriter(new FileWriter(file));
		String configuracionInicial = "";
		
		configuracionInicial += "idioma=es\n";
		configuracionInicial += "tema=default\n";
		
		bWriter.write(configuracionInicial);
		bWriter.close();
	}
	
	/**
	 * Método encargado de guardar las preferencias actuales
	 * en el fichero de configuración.
	 * 
	 * En caso de no existir el fichero, lo creará con los
	 * valores actuales.
	 *
	 * @throws IOException Si se produce algún error con el fichero.
	 */
	public static void guardarPreferencias() throws IOException {
		String rutaPerfil = System.getProperty("user.home");
		File file = new File(rutaPerfil + "\\.BananaTest\\config.properties");
	
		if (!file.getParentFile().exists() && !file.getParentFile().mkdirs())
			throw new IllegalStateException("No se pudo crear la carpeta .BananaTest");
		
		// Creamos el fichero en caso de no existir
		file.createNewFile();
		
		Preferencias.properties.store(new FileWriter(file), null);
	}
	
	/**
	 * Método encargado de retornar el tema actualmente
	 * escogido por el usuario en base a sus preferencias.
	 *
	 * @return the tema
	 */
	public static TEMAS getTema() {
		String tema = properties.getProperty("tema").toUpperCase();
		TEMAS[] listado = TEMAS.values();
		int i = 0;
		
		while (i < listado.length && !tema.equals(listado[i].name))
			i++;
		
		if (i < listado.length)
			return listado[i];
		else {
			// Si se ha manipulado la preferencia de tema a uno no válido
			// se cambiará al tema por defecto
			properties.setProperty("tema", TEMAS.DEFAULT.toString());
			return TEMAS.DEFAULT;
		}
	}
	
	/**
	 * Sets the tema.
	 *
	 * @param tema the new tema
	 */
	public static void setTema(TEMAS tema) {
		properties.setProperty("tema", tema.name.toLowerCase());
	}
	
	/**
	 * Cargar tema.
	 *
	 * @return the string
	 */
	public static String cargarTema() {
		return Preferencias.class.getResource("/css/" + properties.getProperty("tema") + ".css").toExternalForm();
	}

}
