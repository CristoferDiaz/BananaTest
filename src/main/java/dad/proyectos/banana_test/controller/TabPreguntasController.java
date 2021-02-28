package dad.proyectos.banana_test.controller;

import java.io.IOException;
import java.net.URL;
import java.util.Optional;
import java.util.ResourceBundle;

import dad.proyectos.banana_test.App;
import dad.proyectos.banana_test.db.GestorDB;
import dad.proyectos.banana_test.model.Pregunta;
import dad.proyectos.banana_test.utils.Preferencias;
import dad.proyectos.banana_test.utils.dialogos.DialogoConfirmar;
import dad.proyectos.banana_test.utils.dialogos.tab_preguntas.DialogoPregunta;
import javafx.beans.property.ListProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleListProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.transformation.FilteredList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.ScrollPane.ScrollBarPolicy;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

/**
 * Clase gestora del panel de preguntas.
 * 
 * @author Crmprograming
 *
 */
public class TabPreguntasController implements Initializable {

	// model
	
	private ListProperty<Pregunta> listadoPreguntas = new SimpleListProperty<Pregunta>(FXCollections.observableArrayList());
	private ObjectProperty<Pregunta> preguntaSeleccionada = new SimpleObjectProperty<Pregunta>();
	private ChangeListener<String> listenerFiltro;
	
	// view

	@FXML
	private HBox view;

	@FXML
	private Button btCrear;

	@FXML
	private Button btBorrar;
	
	@FXML
	private Button btAplicarCambios;

	@FXML
	private TextField tfBuscador;

	@FXML
	private ListView<Pregunta> lvPreguntas;

	@FXML
    private BorderPane bpEditarPregunta;
	
	@FXML
    private VBox vbVistaPrevia;

	/**
	 * Constructor principal de la clase.
	 * 
	 * @throws IOException si falla a la hora de cargar la vista del fichero fxml
	 */
	public TabPreguntasController() throws IOException {
		FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/preguntas/TabPreguntasView.fxml"), App.resourceBundle);
		loader.setController(this);
		loader.load();
	}

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		cargarListadoPreguntas();
		
		crearFiltroBuscador();
		
		lvPreguntas.setCellFactory(v -> new ListCell<Pregunta>() {
			@Override
			protected void updateItem(Pregunta item, boolean empty) {
				super.updateItem(item, empty);
				
				if (empty || item == null || item.getPregunta() == null) {
					setText(null);
				} else
					setText(item.getPregunta());
			}
		});
		preguntaSeleccionada.bind(lvPreguntas.getSelectionModel().selectedItemProperty());
		preguntaSeleccionada.addListener((o, ov, nv) -> onSeleccionadaChanged(o, ov, nv));
		btBorrar.disableProperty().bind(preguntaSeleccionada.isNull());
		btAplicarCambios.disableProperty().bind(preguntaSeleccionada.isNull());
	}
	
	/**
	 * Método encargado de cargar el listado de preguntas y volcarlo
	 * en el ListView correspondiente.
	 */
	private void cargarListadoPreguntas() {
		String[] error = {""};
		
		// Si no borramos el listener, a la hora de borrar o modificar
		// una pregunta varias veces, empieza a segregar errores uno tras otro
		if (listenerFiltro != null)
			tfBuscador.textProperty().removeListener(listenerFiltro);
		listadoPreguntas.setAll(GestorDB.visualizarPreguntas(Preferencias.idUsuario, new int[] {}, -1, error));
		
		if (!error[0].equals(""))
			App.mostrarMensajeError(
					"BananaTest - Actualizar listado de preguntas",
					"Se ha producido un error",
					error
			);		
		crearFiltroBuscador();
	}

	/**
	 * Método encargado de crear el filtro de búsqueda
	 * de preguntas para el TextField asociado.
	 */
	private void crearFiltroBuscador() {
		FilteredList<Pregunta> filteredData = new FilteredList<Pregunta>(listadoPreguntas, s -> true);
		tfBuscador.textProperty().addListener((o, ov, nv) -> {
			String filtro = tfBuscador.getText();
			
			if (filtro == null || filtro.length() == 0) {
				filteredData.setPredicate(s -> true);
			} else {
				filteredData.setPredicate(s -> s.getPregunta().contains(filtro));
			}
			
		});
		
		lvPreguntas.setItems(filteredData);
	}

	/**
	 * Método listener asociado al cambio de la pregunta seleccionada.
	 * @param o instancia del valor observable
	 * @param ov instancia del anterior valor observado
	 * @param nv instancia del nuevo valor observado
	 */
	private void onSeleccionadaChanged(ObservableValue<? extends Pregunta> o, Pregunta ov, Pregunta nv) {
		if (ov != null) {
			bpEditarPregunta.setCenter(null);
			vbVistaPrevia.getChildren().remove(ov);
		}
		
		if (nv != null) {
			ScrollPane scrollPane = new ScrollPane(nv.construirFormularioEditable());
			scrollPane.setVbarPolicy(ScrollBarPolicy.AS_NEEDED);
			scrollPane.setHbarPolicy(ScrollBarPolicy.NEVER);
			scrollPane.setFitToHeight(true);
			scrollPane.setFitToWidth(true);
			bpEditarPregunta.setCenter(scrollPane);
			vbVistaPrevia.getChildren().add(nv);
		}
	}
	
	@FXML
    void onCrearNuevaAction(ActionEvent event) {
		DialogoPregunta dialog = new DialogoPregunta();
		Optional<Pregunta> result = dialog.showAndWait();
		
		if (result.isPresent()) { 
			String[] error = {""};
			result.get().setCreador(Preferencias.idUsuario);
			if (GestorDB.crearPregunta(result.get(), error)) {
				cargarListadoPreguntas();
			} else {
				App.mostrarMensajeError(
						"BananaTest - Crear nueva pregunta",
						"Se ha producido un error",
						error
				);
			}
		}
    }
	
	@FXML
    void onBorrarAction(ActionEvent event) {
		DialogoConfirmar dialog = new DialogoConfirmar(
				"Borrar Pregunta",
				"Borrar una pregunta es una operación irreversible.\n¿Está seguro de querer continuar?",
				"Borrar",
				"Cancelar"
		);
		Optional<Boolean> result = dialog.showAndWait();
		
		if (result.isPresent()) {
			String[] error = {""};
			if (GestorDB.eliminarPregunta(preguntaSeleccionada.get(), error)) {
				cargarListadoPreguntas();
			} else {
				App.mostrarMensajeError(
						"BananaTest - Borrar pregunta existente",
						"Se ha producido un error",
						error
				);
			}
		}
    }
	
	@FXML
    void onAplicarCambios(ActionEvent event) {
		String[] error = {""};
		if (GestorDB.modificarPregunta(preguntaSeleccionada.get(), error)) {
			cargarListadoPreguntas();
		} else {
			App.mostrarMensajeError(
					"BananaTest - Modificar pregunta existente",
					"Se ha producido un error",
					error
			);
		}
    }

	/**
	 * Método getter del atributo view.
	 * @return instancia de HBox del atributo asociado
	 */
	public HBox getView() {
		return view;
	}

	/**
	 * Método setter del atributo view.
	 * @param view instancia de HBox asociada
	 */
	public void setView(HBox view) {
		this.view = view;
	}

}
