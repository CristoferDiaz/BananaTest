package dad.proyectos.banana_test.controller;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.layout.VBox;

/**
 * Clase gestora de la ventana de Acerca de.
 * @author Crmprograming
 *
 */
public class AcercaDeController implements Initializable {

	// view
	@FXML
	private VBox view;

	/**
	 * Constructor principal de la clase.
	 * 
	 * @throws IOException si no consigue encontrar el recurso AcercaDeView.fxml
	 */
	public AcercaDeController() throws IOException {
		FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/AcercaDeView.fxml"));
		loader.setController(this);
		loader.load();
	}

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		// Nada
	}

	/**
	 * Método getter del atributo view.
	 * 
	 * @return instancia del VBox asociado
	 */
	public VBox getView() {
		return view;
	}

	/**
	 * Método setter del atributo view.
	 * 
	 * @param view instancia del VBox asociado
	 */
	public void setView(VBox view) {
		this.view = view;
	}

}
