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

	@FXML
	void onCancelarAction(ActionEvent event) {
		stage.close();
	}

	@FXML
	void onSiguienteAction(ActionEvent event) {
		if (modo == 0) {
			modo = 1;
			vbPresentacion.setDisable(true);
			hbBotonesControl.setDisable(true);
			progressIndicator.setVisible(true);
			generarConfiguracion();
		} else if (modo == 1) {
			
			stage.close();
		}
		
	}
	
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
					Reader reader = new BufferedReader(new InputStreamReader(Main.class.getClassLoader().getResourceAsStream("script_crear_db.sql")));
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

	public BorderPane getView() {
		return view;
	}

	public void setView(BorderPane view) {
		this.view = view;
	}

}
