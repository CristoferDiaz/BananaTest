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
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
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
	 * Método encargado de mostrar una ventana de diálogo
	 * informando de un procedimiento exitoso.
	 * 
	 * @param titulo String con el título de la ventana
	 * @param contenido String con el contenido de la ventana
	 */
	public static void mostrarMensajeExito(String titulo, String contenido) {
		Alert alert = new Alert(AlertType.INFORMATION);
		alert.setTitle(titulo);
		alert.setHeaderText(null);
		alert.setContentText(contenido);
		
		Stage stage = (Stage) alert.getDialogPane().getScene().getWindow();
		stage.getIcons().add(new Image("/images/logo/bananatest_logo_16.png"));
		stage.getIcons().add(new Image("/images/logo/bananatest_logo_32.png"));
		stage.getIcons().add(new Image("/images/logo/bananatest_logo_64.png"));
		
		alert.showAndWait();
	}
	
	/**
	 * Método encargado de mostrar una ventana de diálogo
	 * informando de un procedimiento fallido.
	 * 
	 * @param titulo String con el título de la ventana
	 * @param cabecera String con el texto de la cabecera
	 * @param error Array con el posible conjunto de valores
	 */
	public static void mostrarMensajeError(String titulo, String cabecera, String[] error) {
		Alert alert = new Alert(AlertType.ERROR);
		alert.setTitle(titulo);
		alert.setHeaderText(cabecera);
		alert.setContentText(error[0]);
		
		Stage stage = (Stage) alert.getDialogPane().getScene().getWindow();
		stage.getIcons().add(new Image("/images/logo/bananatest_logo_16.png"));
		stage.getIcons().add(new Image("/images/logo/bananatest_logo_32.png"));
		stage.getIcons().add(new Image("/images/logo/bananatest_logo_64.png"));
		
		alert.showAndWait();
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
