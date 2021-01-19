package dad.proyectos.banana_test;

import dad.proyectos.banana_test.controller.MainController;
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
