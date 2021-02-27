package dad.proyectos.banana_test.controller;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import dad.proyectos.banana_test.App;
import dad.proyectos.banana_test.utils.Preferencias;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.ComboBox;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

/**
 * Clase gestora de la ventana de preferencias
 * y toda su lógica.
 * 
 * @author Crmprograming
 *
 */
public class PreferenciasController implements Initializable {
	
	private Preferencias.TEMAS temaOriginal;
	private Preferencias.TEMAS temaNuevo;
	private Preferencias.IDIOMAS idiomaOriginal;
	private Preferencias.IDIOMAS idiomaNuevo;
	private Stage stage;

	@FXML
	private VBox view;

	@FXML
	private ComboBox<String> cbIdioma;

	@FXML
	private ComboBox<String> cbTema;
	
	@FXML
    private ImageView ivTema;

	/**
	 * Constructor principal de la clase.
	 * 
	 * @param stage Referencia del Stage contenedor de esta vista
	 * @throws IOException si no consigue encontrar el recurso PreferenciasView.fxml
	 */
	public PreferenciasController(Stage stage) throws IOException {
		FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/PreferenciasView.fxml"), App.resourceBundle);
		loader.setController(this);
		loader.load();
		idiomaOriginal = Preferencias.getIdioma();
		temaOriginal = Preferencias.getTema();
		idiomaNuevo = idiomaOriginal;
		temaNuevo = temaOriginal;
		this.stage = stage;
	}

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		cbIdioma.getItems().addAll(Preferencias.IDIOMAS.getAll());
		cbTema.getItems().addAll(Preferencias.TEMAS.getAll());
		
		cbIdioma.getSelectionModel().selectedIndexProperty().addListener((o, ov, nv) -> actualizarIdioma(o, ov, nv));
		cbTema.getSelectionModel().selectedIndexProperty().addListener((o, ov, nv) -> actualizarTema(o, ov, nv));
		
		ivTema.setImage(new Image(getClass().getResource("/images/previewTemas/" + Preferencias.getTema().toString() + ".png").toExternalForm()));
	}
	
	/**
	 * Método listener asociado al ComboBox de idiomas.
	 * 
	 * @param o Instancia del valor observado
	 * @param ov Instancia del anterior valor observado
	 * @param nv Instancia del nuevo valor observado
	 */
	private void actualizarIdioma(ObservableValue<? extends Number> o, Number ov, Number nv) {
		idiomaNuevo = Preferencias.IDIOMAS.values()[nv.intValue()];
	}

	/**
	 * Método listener asociado al ComboBox de temas.
	 * 
	 * @param o Instancia del valor observado
	 * @param ov Instancia del anterior valor observado
	 * @param nv Instancia del nuevo valor observado
	 */
	private void actualizarTema(ObservableValue<? extends Number> o, Number ov, Number nv) {
		temaNuevo = Preferencias.TEMAS.values()[nv.intValue()];
		ivTema.setImage(new Image(getClass().getResource("/images/previewTemas/" + Preferencias.TEMAS.values()[nv.intValue()] + ".png").toExternalForm()));
	}
	
	/**
	 * Método asociado al evento del botón de cancelar.
	 * Al pulsarse, deja los valores a como estaban antes de
	 * mostrarse esta ventana.
	 * 
	 * @param event Instancia de ActionEvent asociada al evento
	 */
	@FXML
    void onCancelarAction(ActionEvent event) {
		Preferencias.setIdioma(idiomaOriginal);
		Preferencias.setTema(temaOriginal);
		stage.close();
    }
	
	/**
	 * Método asociado al evento del botón de guardar.
	 * Al pulsarse, cambia las preferencias de idioma
	 * y tema, además de aplicar el nuevo estilo de ser necesario.
	 * 
	 * @param event Instancia de ActionEvent asociada al evento
	 */
	@FXML
    void onGuardarAction(ActionEvent event) {
		Preferencias.setIdioma(idiomaNuevo);
		Preferencias.setTema(temaNuevo);
		stage.close();
		App.primaryStage.getScene().getStylesheets().clear();
		if (Preferencias.getTema() != Preferencias.TEMAS.DEFAULT)
			App.primaryStage.getScene().getStylesheets().add(Preferencias.cargarTema());
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
	 * @param view Instancai del VBox asociado
	 */
	public void setView(VBox view) {
		this.view = view;
	}

}
