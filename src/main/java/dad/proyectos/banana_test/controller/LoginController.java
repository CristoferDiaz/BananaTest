package dad.proyectos.banana_test.controller;

import java.io.IOException;
import java.math.BigInteger;
import java.net.URL;
import java.security.MessageDigest;
import java.util.ResourceBundle;

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

	public LoginController(Stage stage) throws IOException {
		this.stage = stage;
		FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/LoginView.fxml"));
		loader.setController(this);
		loader.load();
	}

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		serverIP.bind(tfIPServer.textProperty());
		dbUser.bind(tfUsuServer.textProperty());
		dbPassword.bind(tfPasswordServer.textProperty());
		usuarioProfesor.bind(tfUsuarioApp.textProperty());
		usuarioPassword.bind(tfPasswordApp.textProperty());
	}

	@FXML
	void onCancelarAction(ActionEvent event) {
		stage.close();
	}

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
				// TODO: Cambiar esto para que haga la conexión verdadera
				setValidado(true); // Quitar esta línea cuando ya no haga falta
				funcional.set(false);
//				Connection conexion = Intermedio.conectarmysql();
//				if (conexion == null) {					
//					mensajeWarning.set("El servidor indicado no está disponible.");
//				} else {
//					setValidado(true);
//					conexion.close();
//				}
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
	
	private String generarSha1(String cadena) throws Exception {
		MessageDigest digest = MessageDigest.getInstance("SHA-1");
		digest.reset();
		digest.update(cadena.getBytes("utf8"));
		return String.format("%040x", new BigInteger(1, digest.digest()));
	}

	public BorderPane getView() {
		return view;
	}

	public void setView(BorderPane view) {
		this.view = view;
	}

	public final BooleanProperty validadoProperty() {
		return this.validado;
	}
	

	public final boolean isValidado() {
		return this.validadoProperty().get();
	}
	

	public final void setValidado(final boolean validado) {
		this.validadoProperty().set(validado);
	}

}