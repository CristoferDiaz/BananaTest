package dad.proyectos.banana_test;

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
	public static Stage primaryStage;
	
	@Override
	public void init() throws Exception {
		Preferencias.cargarPreferencias();
	}	
	
	@Override
	public void stop() throws Exception {
		Preferencias.guardarPreferencias();
	}	

	@Override
	public void start(Stage primaryStage) throws Exception {
		mainController = new MainController();
				
		Scene escena = new Scene(mainController.getView());
		
		// Cargamos el tema css escogido por el usuario
		if (Preferencias.getTema() != Preferencias.TEMAS.DEFAULT)
			escena.getStylesheets().add(Preferencias.cargarTema());
		
		primaryStage.setScene(escena);
		primaryStage.setTitle("BananaTest");
		primaryStage.getIcons().add(new Image("/images/logo/bananatest_logo_16.png"));
		primaryStage.getIcons().add(new Image("/images/logo/bananatest_logo_32.png"));
		primaryStage.getIcons().add(new Image("/images/logo/bananatest_logo_64.png"));
		primaryStage.show();
		App.primaryStage = primaryStage;
	}

	public static void main(String[] args) {
		launch(args);
	}
}
