package dad.proyectos.banana_test;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;

import dad.proyectos.banana_test.controller.MainController;
import dad.proyectos.banana_test.utils.Preferencias;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

/**
 * Clase gestora de la configuración inicial de la clase.
 * 
 * @author Crmprograming
 */
public class App extends Application {
	
	private MainController mainController;
	
	private void crearConfiguracionInicial(File file) throws IOException {
		BufferedWriter bWriter = new BufferedWriter(new FileWriter(file));
		String configuracionInicial = "";
		
		configuracionInicial += "idioma=es\n";
		configuracionInicial += "tema=default\n";
		
		bWriter.write(configuracionInicial);
		bWriter.close();
	}
	
	@Override
	public void init() throws Exception {
		String rutaPerfil = System.getProperty("user.home");
		File file = new File(rutaPerfil + "\\.BananaTest\\config.properties");
		
		if (!file.getParentFile().exists() && !file.getParentFile().mkdirs())
			throw new IllegalStateException("No se pudo crear la carpeta .BananaTest");
		
		if (file.createNewFile()) {
			crearConfiguracionInicial(file);
		}
		
		Preferencias.properties.load(new FileInputStream(file));
	}
	
	@Override
	public void stop() throws Exception {
		String rutaPerfil = System.getProperty("user.home");
		File file = new File(rutaPerfil + "\\.BananaTest\\config.properties");
	
		if (!file.getParentFile().exists() && !file.getParentFile().mkdirs())
			throw new IllegalStateException("No se pudo crear la carpeta .VentanaConMemoria");
		
		// Creamos el fichero en caso de no existir
		file.createNewFile();
		
		Preferencias.properties.store(new FileWriter(file), null);
	}

	@Override
	public void start(Stage primaryStage) throws Exception {
		mainController = new MainController();
				
		Scene escena = new Scene(mainController.getView());
		primaryStage.setScene(escena);
		primaryStage.setTitle("BananaTest");
		primaryStage.getIcons().add(new Image("/images/logo/bananatest_logo_16.png"));
		primaryStage.getIcons().add(new Image("/images/logo/bananatest_logo_32.png"));
		primaryStage.getIcons().add(new Image("/images/logo/bananatest_logo_64.png"));
		primaryStage.show();
	}

	public static void main(String[] args) {
		launch(args);
	}
}
