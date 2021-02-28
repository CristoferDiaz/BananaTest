package dad.proyectos.banana_test.controller;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.URL;
import java.sql.Connection;
import java.sql.DriverManager;
import java.util.ResourceBundle;

import org.apache.ibatis.jdbc.ScriptRunner;

import dad.proyectos.banana_test.Main;
import dad.proyectos.banana_test.utils.Preferencias;
import javafx.beans.binding.Bindings;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.ProgressIndicator;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

/**
 * Clase gestora de la ventana de instalador.
 * Sólo se mostrará la primera vez que
 * se ejecute la aplicación.
 * 
 * @author Crmprograming
 *
 */
public class InstaladorController implements Initializable {

	private Stage stage;
	private int modo = 0;
	
	// model
	private StringProperty serverIP = new SimpleStringProperty();
	private StringProperty dbUser = new SimpleStringProperty();
	private StringProperty dbPassword = new SimpleStringProperty();

	// view

	@FXML
	private BorderPane view;
	
	@FXML
    private HBox hbBotonesControl;

	@FXML
	private Button btnCancelar;

	@FXML
	private Button btnSiguiente;

	@FXML
	private StackPane spContenedor;

	@FXML
	private VBox vbPresentacion;

	@FXML
	private TextField tfDireccion;

	@FXML
	private TextField tfUsuarioBD;

	@FXML
	private PasswordField pfPassword;

	@FXML
	private ProgressIndicator progressIndicator;

	@FXML
	private VBox vbFinalizado;

	/**
	 * Constructor por defecto de la clase.
	 * 
	 * @param stage Referencia del Stage contenedor de este componente
	 * @throws IOException si se produce un error al cargar la plantilla de la vista
	 */
	public InstaladorController(Stage stage) throws IOException {
		FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/instalador/InstaladorMainView.fxml"));
		loader.setController(this);
		loader.load();
		this.stage = stage;
	}

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		serverIP.bind(tfDireccion.textProperty());
		dbUser.bind(tfUsuarioBD.textProperty());
		dbPassword.bind(pfPassword.textProperty());
		btnSiguiente.disableProperty().bind(
				Bindings.or(tfDireccion.textProperty().isEmpty(), tfUsuarioBD.textProperty().isEmpty())
		);
	}

	/**
	 * Método asociado al evento de pulsar el botón de cancelar.
	 * Si el usuario lo pulsa, entendemos que se quiere saltar
	 * la configuración de la base de datos porque esta
	 * ya está configurada en el servidor.
	 * 
	 * @param event Instancia de ActionEvent asociada al evento de hacer click
	 */
	@FXML
	void onCancelarAction(ActionEvent event) {
		stage.close();
	}

	/**
	 * Método asociado al evento de pulsar el botón de siguiente.
	 * La primera vez que se pulse, deshabilita toda la interfaz y
	 * comienza el proceso de instalación.
	 * La segunda vez, cierra la ventana y da comienzo la aplicación.
	 * 
	 * @param event Instancia de ActionEvent asociada al evento de hacer click
	 */
	@FXML
	void onSiguienteAction(ActionEvent event) {
		if (modo == 0) {
			modo = 1;
			vbPresentacion.setDisable(true);
			hbBotonesControl.setDisable(true);
			progressIndicator.setVisible(true);
			btnCancelar.disabledProperty();
			generarConfiguracion();
		} else if (modo == 1) {
			stage.close();
		}
		
	}
	
	/**
	 * Método encargado de generar la configuración
	 * en la base de datos para el correcto funcionamiento
	 * de la aplicación.
	 * 
	 * Este proceso se realiza en segundo plano ejecutando
	 * el script de instalación.
	 */
	private void generarConfiguracion() {
		Task<Void> task = new Task<Void>() {
			@Override
			protected Void call() throws Exception {
				try {
					String urlConexion = "jdbc:mysql://" + tfDireccion.getText();
					String user = tfUsuarioBD.getText();
					String password = pfPassword.getText();
					String driver = "com.mysql.cj.jdbc.Driver";
					Class.forName(driver);
					Connection conexion = DriverManager.getConnection(urlConexion, user, password);
					ScriptRunner scriptRunner = new ScriptRunner(conexion);
					Reader reader = new BufferedReader(new InputStreamReader(Main.class.getClassLoader().getResourceAsStream("bd/script_crear_db.sql")));
					scriptRunner.runScript(reader);
					conexion.close();
					Preferencias.properties.setProperty("direccion_servidor", tfDireccion.getText());
				} catch (Exception e) {
					e.printStackTrace();
				}
				return null;
			}
		};
		
		task.setOnSucceeded(e -> {
			vbPresentacion.setVisible(false);
			vbFinalizado.setVisible(true);
			progressIndicator.setVisible(false);
			btnSiguiente.textProperty().set("Finalizar");
			hbBotonesControl.setDisable(false);
		});
		
		new Thread(task).start();
	}

	/**
	 * Método getter del atributo view de la clase.
	 * @return Instancia del BorderPane asociado
	 */
	public BorderPane getView() {
		return view;
	}

	/**
	 * Método setter del atributo view de la clase.
	 * @param view Nueva instancia del BorderPane asociado
	 */
	public void setView(BorderPane view) {
		this.view = view;
	}

}
