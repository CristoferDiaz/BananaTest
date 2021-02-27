package dad.proyectos.banana_test;

import java.io.IOException;
import java.util.ResourceBundle;

import dad.proyectos.banana_test.controller.InstaladorController;
import dad.proyectos.banana_test.controller.LoginController;
import dad.proyectos.banana_test.controller.MainController;
import dad.proyectos.banana_test.utils.Preferencias;
import javafx.application.Application;
import javafx.application.HostServices;
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
	public static HostServices hostServices;
	public static ResourceBundle resourceBundle;

	/**
	 * Método encargado de cargar las preferencias de
	 * la aplicación cuando esta se inice.
	 */
	@Override
	public void init() throws Exception {
		Preferencias.cargarPreferencias();
	}

	/**
	 * Método encargado de guardar las preferencias
	 * de la aplicación cuando esta finalice.
	 */
	@Override
	public void stop() throws Exception {
		Preferencias.guardarPreferencias();
	}

	/**
	 * Método encargado de llevar a cabo la ejecución
	 * principal de la aplicación.
	 */
	@Override
	public void start(Stage primaryStage) throws Exception {
		if (Preferencias.esPrimerArranque()) {
			gestionarPrimerArranque();
		}

		if (gestionarLogin()) {
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
			primaryStage.setMinHeight(550);
			primaryStage.setMinWidth(900);
			primaryStage.show();
			App.primaryStage = primaryStage;
			App.hostServices = getHostServices();
		}
	}

	/**
	 * Método encargado de gestionar el login del usuario
	 * en el sistema de la aplicación.
	 * 
	 * @return true si se pudo hacer login
	 * @throws IOException si alguno de los componentes JavaFX no pudo ser cargado
	 */
	private static boolean gestionarLogin() throws IOException {
		Stage stage = new Stage();
		LoginController loginController = new LoginController(stage);
		Scene escena = new Scene(loginController.getView());

		stage.setScene(escena);
		stage.setTitle("BananaTest");
		stage.getIcons().add(new Image("/images/logo/bananatest_logo_16.png"));
		stage.getIcons().add(new Image("/images/logo/bananatest_logo_32.png"));
		stage.getIcons().add(new Image("/images/logo/bananatest_logo_64.png"));
		stage.setResizable(false);
		stage.showAndWait();

		return (loginController.isValidado());
	}

	/**
	 * Método encargado de gestionar el primer arranque de la
	 * aplicación. Con esto, conseguimos que se cree
	 * la base de datos de la aplicación.
	 * 
	 * @throws IOException si alguno de los componentes JavaFX no pudo ser cargado
	 */
	private static void gestionarPrimerArranque() throws IOException {
		Preferencias.cambiarPrimerArranque();
		Stage stage = new Stage();
		InstaladorController instaladorController = new InstaladorController(stage);

		Scene escena = new Scene(instaladorController.getView());

		stage.setScene(escena);
		stage.setTitle("BananaTest");
		stage.getIcons().add(new Image("/images/logo/bananatest_logo_16.png"));
		stage.getIcons().add(new Image("/images/logo/bananatest_logo_32.png"));
		stage.getIcons().add(new Image("/images/logo/bananatest_logo_64.png"));
		stage.setResizable(false);
		stage.showAndWait();
	}

	/**
	 * Método main encargado de iniciar los componentes
	 * de JavaFX y su hilo correspondiente
	 * @param args Array de String con los parámetros de entrada
	 */
	public static void main(String[] args) {
		launch(args);
	}
}
