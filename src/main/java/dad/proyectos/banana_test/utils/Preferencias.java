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

public abstract class Preferencias {

	public static enum IDIOMAS {
		ES,
		EN
	}
	
	public static enum TEMAS {
		DEFAULT ("DEFAULT"),
		DARK ("DARK"),
		BANANA("BANANA"),
		PASTEL("PASTEL");
		
		private final String name;
		
		private TEMAS(String string) {
			this.name = string;
		}
		
		@Override
		public String toString() {
			return this.name;
		}
		
		public static List<String> getAll() {
			List<String> listado = new ArrayList<String>();
			for (TEMAS tema : TEMAS.values())
				listado.add(tema.name);
			return listado;
		}
		
	}
	
	public static Properties properties = new Properties();
	
	public static void cargarPreferencias() throws IOException, FileNotFoundException {
		String rutaPerfil = System.getProperty("user.home");
		File file = new File(rutaPerfil + "\\.BananaTest\\config.properties");
		
		if (!file.getParentFile().exists() && !file.getParentFile().mkdirs())
			throw new IllegalStateException("No se pudo crear la carpeta .BananaTest");
		
		if (file.createNewFile()) {
			crearConfiguracionInicial(file);
		}
		
		Preferencias.properties.load(new FileInputStream(file));
	}
	
	private static void crearConfiguracionInicial(File file) throws IOException {
		BufferedWriter bWriter = new BufferedWriter(new FileWriter(file));
		String configuracionInicial = "";
		
		configuracionInicial += "idioma=es\n";
		configuracionInicial += "tema=default\n";
		
		bWriter.write(configuracionInicial);
		bWriter.close();
	}
	
	public static void guardarPreferencias() throws IOException {
		String rutaPerfil = System.getProperty("user.home");
		File file = new File(rutaPerfil + "\\.BananaTest\\config.properties");
	
		if (!file.getParentFile().exists() && !file.getParentFile().mkdirs())
			throw new IllegalStateException("No se pudo crear la carpeta .BananaTest");
		
		// Creamos el fichero en caso de no existir
		file.createNewFile();
		
		Preferencias.properties.store(new FileWriter(file), null);
	}
	
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
	
	public static void setTema(TEMAS tema) {
		properties.setProperty("tema", tema.name.toLowerCase());
	}
	
	public static String cargarTema() {
		return Preferencias.class.getResource("/css/" + properties.getProperty("tema") + ".css").toExternalForm();
	}

}
