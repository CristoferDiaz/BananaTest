package dad.proyectos.banana_test.utils;

import java.util.Properties;

public abstract class Preferencias {

	public static Properties properties;
	
	static {
		properties = new Properties();
	}
	
	public static String getTheme() {
		return Preferencias.class.getResource(properties.getProperty("theme")).toExternalForm();
	}

}
