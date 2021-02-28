package dad.proyectos.banana_test.controller;

import java.io.IOException;
import java.math.BigInteger;
import java.net.URL;
import java.security.MessageDigest;
import java.sql.Connection;
import java.util.ResourceBundle;

import dad.proyectos.banana_test.App;
import dad.proyectos.banana_test.db.GestorDB;
import dad.proyectos.banana_test.utils.Preferencias;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.ProgressIndicator;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

/**
 * Clase gestora de la ventana de login en el
 * sistema. La ventana al iniciarse
 * mostrará los valores guardados por defecto 
 * del usuario tales como la dirección IP
 * del servidor.
 * 
 * @author Crmprograming
 *
 */
public class LoginController implements Initializable {
	
	private Stage stage;

	// model
	private StringProperty serverIP = new SimpleStringProperty();
	private StringProperty dbUser = new SimpleStringProperty();
	private StringProperty dbPassword = new SimpleStringProperty();
	private StringProperty usuarioProfesor = new SimpleStringProperty();
	private StringProperty usuarioPassword = new SimpleStringProperty();
	private BooleanProperty validado = new SimpleBooleanProperty(false);
	private StringProperty mensajeWarning = new SimpleStringProperty();
	private BooleanProperty funcional = new SimpleBooleanProperty(true);
	
	// view
	
	@FXML
	private BorderPane view;

	@FXML
	private HBox hbBotonesControl;

	@FXML
	private VBox vbContent;

	@FXML
	private Label lbMensajeWarning;

	@FXML
	private TextField tfIPServer;

	@FXML
	private TextField tfUsuServer;

	@FXML
	private PasswordField tfPasswordServer;

	@FXML
	private TextField tfUsuarioApp;

	@FXML
	private PasswordField tfPasswordApp;

	@FXML
	private ProgressIndicator progressIndicator;

	/**
	 * Constructor principal de la clase.
	 * 
	 * @param stage Referencia al Stage contenedor de esta vista
	 * @throws IOException si falla al cargar la plantilla de la vista
	 */
	public LoginController(Stage stage) throws IOException {
		this.stage = stage;
		FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/LoginView.fxml"), App.resourceBundle);
		loader.setController(this);
		loader.load();
	}

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		tfIPServer.setText(Preferencias.properties.getProperty("direccion_servidor"));
		serverIP.bind(tfIPServer.textProperty());
		dbUser.bind(tfUsuServer.textProperty());
		dbPassword.bind(tfPasswordServer.textProperty());
		usuarioProfesor.bind(tfUsuarioApp.textProperty());
		usuarioPassword.bind(tfPasswordApp.textProperty());
	}

	/**
	 * Método asociado al evento de pulsar el botón de cancelar.
	 * 
	 * @param event Instancia de ActionEvent asociada al evento de hacer click
	 */
	@FXML
	void onCancelarAction(ActionEvent event) {
		stage.close();
	}

	/**
	 * Método asociado al evento de pulsar el botón de conectarse.
	 * El proceso de login se hace en segundo plano, de modo que
	 * al darle al botón deshabilita toda interfaz y deja a la vista
	 * un ProgressIndicator.
	 * 
	 * En caso de fallar la operación, actualizará el label de mensaje
	 * de error con el error producido.
	 * 
	 * @param event Instancia de ActionEvent asociada al evento de hacer click
	 */
	@FXML
	void onConectarseAction(ActionEvent event) {
		mensajeWarning.set("");
		lbMensajeWarning.setText(mensajeWarning.get());
		vbContent.setDisable(true);
		hbBotonesControl.setDisable(true);
		progressIndicator.setVisible(true);
		
		Task<Void> task = new Task<Void>() {

			@Override
			protected Void call() throws Exception {
				int idUsuario = -1;
				String[] error = {""};
				String usuarioApp = "";
				String passwordApp = "";
				Preferencias.properties.setProperty("direccion_servidor", tfIPServer.getText());
				Preferencias.usuarioServidor = tfUsuServer.getText();
				Preferencias.passwordServidor = tfPasswordServer.getText();
				try {
					usuarioApp = generarSha1(tfUsuarioApp.getText());
					passwordApp = generarSha1(tfPasswordApp.getText());
				} catch (Exception e) {
					e.printStackTrace();
				}
				Connection conexion = GestorDB.conectarmysql();
				if (conexion == null) {					
					error[0] = "El servidor indicado no está disponible.";
				} else if ((idUsuario = GestorDB.comprobarLogin(conexion, usuarioApp, passwordApp, error)) != -1) {
					Preferencias.idUsuario = idUsuario; 
					funcional.set(false);
					setValidado(true);
					conexion.close();
				}
				
				if (!error[0].equals("")) {
					mensajeWarning.set(error[0]);
				}
				return null;
			}
			
		};
		
		task.setOnSucceeded(e -> {
			vbContent.setDisable(false);
			hbBotonesControl.setDisable(false);
			progressIndicator.setVisible(false);
			lbMensajeWarning.setText(mensajeWarning.get());			
			if (!funcional.get())
				stage.close();
		});
		
		new Thread(task).start();
	}
	
	/**
	 * Método encargado de cifrar un String pasado
	 * por parámetro utilizando el algoritmo de cifrado SHA1.
	 * 
	 * Este método se utiliza para cifrar los datos de
	 * usuario en el sistema.
	 * 
	 * @param cadena String a cifrar
	 * @return String ya cifrado con el algoritmo
	 * @throws Exception si no encuentra el algoritmo indicado
	 */
	private String generarSha1(String cadena) throws Exception {
		MessageDigest digest = MessageDigest.getInstance("SHA-1");
		digest.reset();
		digest.update(cadena.getBytes("utf8"));
		return String.format("%040x", new BigInteger(1, digest.digest()));
	}

	/**
	 * Método getter del atributo view de la clase.
	 * 
	 * @return Instancia del BorderPane asociado
	 */
	public BorderPane getView() {
		return view;
	}

	/**
	 * Método setter del atributo view de la clase.
	 * 
	 * 
	 * @param view Nuevo BorderPane asociado
	 */
	public void setView(BorderPane view) {
		this.view = view;
	}

	/**
	 * Método getter de la property
	 * asociada al atributo validado.
	 * 
	 * @return Instancia de BooleanProperty asociado
	 */
	public final BooleanProperty validadoProperty() {
		return this.validado;
	}
	

	/**
	 * Método getter del atributo validado de la clase.
	 * 
	 * @return boolean que indica si está validado el login o no
	 */
	public final boolean isValidado() {
		return this.validadoProperty().get();
	}
	

	/**
	 * Método setter del atributo validado de la clase.
	 * 
	 * @param validado Nuevo valor booleano para el atributo validado
	 */
	public final void setValidado(final boolean validado) {
		this.validadoProperty().set(validado);
	}

}
