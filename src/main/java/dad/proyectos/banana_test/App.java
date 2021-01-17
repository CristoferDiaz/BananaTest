package dad.proyectos.banana_test;

import javafx.application.Application;
import javafx.scene.image.Image;
import javafx.stage.Stage;

/**
 * Clase gestora de la configuración inicial de la clase.
 * 
 * @author Crmprograming
 */
public class App extends Application {

	@Override
	public void start(Stage primaryStage) throws Exception {
		// TODO: Especificar controlador principal
						
//		Scene escena = new Scene(pts.getView());
//		primaryStage.setScene(escena);
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
