package dad.proyectos.banana_test.controller;

import java.io.IOException;
import java.net.URL;
import java.util.Collections;
import java.util.Optional;
import java.util.ResourceBundle;

import dad.proyectos.banana_test.App;
import dad.proyectos.banana_test.db.GestorDB;
import dad.proyectos.banana_test.model.Examen;
import dad.proyectos.banana_test.model.Pregunta;
import dad.proyectos.banana_test.utils.Preferencias;
import dad.proyectos.banana_test.utils.dialogos.DialogoConfirmar;
import dad.proyectos.banana_test.utils.dialogos.tab_examenes.DialogoAgregarPregunta;
import dad.proyectos.banana_test.utils.dialogos.tab_examenes.DialogoCrearExamen;
import dad.proyectos.banana_test.utils.dialogos.tab_examenes.DialogoModificarExamen;
import javafx.beans.binding.Bindings;
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
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.SplitPane;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.util.Pair;
import javafx.util.converter.NumberStringConverter;

/**
 * 
 * @author Daniel Perez Pimienta
 * 
 * La clase TabExamenesController se encarga de controlar todas las funcionalidades 
 * de la pestaña Examenes, además de cargar la vista de la misma.
 *
 */
public class TabExamenesController implements Initializable {

	@FXML
	private SplitPane ViewExamen;

	@FXML
	private Button btCrearExamen;

	@FXML
	private Button btBorrarExamen;

	@FXML
	private Button btModExamen;

	@FXML
	private ListView<Examen> lvExamenes;

	@FXML
	private TextArea txResumenExamen;

	@FXML
	private Button btAgregarPregunta;

	@FXML
	private ListView<Pregunta> lvPreguntas;

	@FXML
	private Button btArriba;

	@FXML
	private Button btQuitar;

	@FXML
	private Button btAbajo;

	@FXML
	private TextField txPuntuacion;

	@FXML
	private Button btActPuntuacion;

	@FXML
	private TextArea txVistaPreviaPregunta;

	@FXML
	private TextField tfBuscador;

	@FXML
	private Label lbResumenExamen;

	@FXML
	private VBox vbVistaPreviaPregunta;

	/* PROPERTIES */

	// Lista de examenes
	private ListProperty<Examen> listadoExamenes = new SimpleListProperty<Examen>(FXCollections.observableArrayList());
	private ObjectProperty<Examen> examenSeleccionado = new SimpleObjectProperty<Examen>();
	private ChangeListener<String> listenerFiltro;

	// Lista de preguntas
	private ListProperty<Pregunta> listadoPreguntas = new SimpleListProperty<Pregunta>(FXCollections.observableArrayList());
	private ObjectProperty<Pregunta> preguntaSeleccionada = new SimpleObjectProperty<Pregunta>();

	/**
	 * Constructor de la clase.
	 * 
	 * @throws IOException si el archivo no existe o no esta disponible
	 */
	public TabExamenesController() throws IOException {
		FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/examenes/TabExamenesView.fxml"), App.resourceBundle);
		loader.setController(this);
		loader.load();
	}
	
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		cargarListadoExamenes();

		// MOSTRAR OBJETO COMO STRING EN UN LISTVIEW (LISTA DE EXAMENES)
		lvExamenes.setCellFactory(v -> new ListCell<Examen>() {
			@Override
			protected void updateItem(Examen item, boolean empty) {
				super.updateItem(item, empty);

				if (empty || item == null || item.getNombre() == null) {
					setText(null);
				} else
					setText(item.getNombre());
			}
		});

		// MOSTRAR OBJETO COMO STRING EN UN LISTVIEW (LISTA DE PREGUNTAS)
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

		// BINDEOS

		// mostramos las preguntas que contiene cada examen en el listPreguntas
		examenSeleccionado.bind(lvExamenes.getSelectionModel().selectedItemProperty());
		preguntaSeleccionada.bind(lvPreguntas.getSelectionModel().selectedItemProperty());
		examenSeleccionado.addListener((v, ov, nv) -> {
			if (ov != null) {
				listadoPreguntas.unbind();
				lbResumenExamen.textProperty().unbind();
				lbResumenExamen.textProperty().set("");
				lvPreguntas.itemsProperty().unbind();
				lvPreguntas.itemsProperty().set(null);
			}
			if (nv != null) {
				listadoPreguntas.bind(nv.preguntasProperty());
				lbResumenExamen.textProperty().bind(nv.descripcionProperty());
				lvPreguntas.itemsProperty().bind(listadoPreguntas);
			}
		});

		// mostramos la vista previa de cada pregunta

		preguntaSeleccionada.addListener((v, ov, nv) -> {
			if (ov != null) {
				vbVistaPreviaPregunta.getChildren().clear();
				Bindings.unbindBidirectional(txPuntuacion.textProperty(), ov.pesoProperty());
			}

			if (nv != null) {
				vbVistaPreviaPregunta.getChildren().add(nv);
				Bindings.bindBidirectional(txPuntuacion.textProperty(), nv.pesoProperty(), new NumberStringConverter());
			}
		});

		// BINDEOS DISPONIBILIDAD BOTONES
		
		btAgregarPregunta.disableProperty().bind(examenSeleccionado.isNull());
		
		btArriba.disableProperty().bind(
				Bindings.or(
						examenSeleccionado.isNull(),
						Bindings.or(
								lvPreguntas.getSelectionModel().selectedIndexProperty().isEqualTo(0),
								preguntaSeleccionada.isNull()
						)
				)
		);
		
		btAbajo.disableProperty().bind(
				Bindings.or(
						examenSeleccionado.isNull(),
						Bindings.or(
								lvPreguntas.getSelectionModel().selectedIndexProperty().add(1).isEqualTo(Bindings.size(listadoPreguntas)),
								preguntaSeleccionada.isNull()
						)
				)
		);
		
		btQuitar.disableProperty().bind(
				Bindings.or(
					examenSeleccionado.isNull(),
					preguntaSeleccionada.isNull()
				)
		);
		
		btBorrarExamen.disableProperty().bind(examenSeleccionado.isNull());
		btModExamen.disableProperty().bind(examenSeleccionado.isNull());
		
		txPuntuacion.disableProperty().bind(preguntaSeleccionada.isNull());
		btActPuntuacion.disableProperty().bind(preguntaSeleccionada.isNull());
	}

	/**
	 * Método encargado de descargar el listado de
	 * exámenes de la base de datos.
	 */
	private void cargarListadoExamenes() {
		String[] error = {""};
		
		// Si no borramos el listener, a la hora de borrar o modificar
		// un examen varias veces, empieza a segregar errores uno tras otro
		if (listenerFiltro != null)
			tfBuscador.textProperty().removeListener(listenerFiltro);
		listadoExamenes.setAll(GestorDB.visualizarExamenes(Preferencias.idUsuario, error));
		if (!error[0].equals(""))
			App.mostrarMensajeError(
					"BananaTest - Actualización del listado de exámenes",
					"Se ha producido un error",
					error
			);		
		crearFiltroBuscador();
	}
	
	/**
	 * Método encargado de la creación de un filtro de texto para los buscadores de la aplicación.
	 */
	private void crearFiltroBuscador() {
		FilteredList<Examen> filteredData = new FilteredList<Examen>(listadoExamenes, s -> true);
		
		listenerFiltro = new ChangeListener<String>() {

			@Override
			public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
				String filtro = tfBuscador.getText();

				if (filtro == null || filtro.length() == 0) {
					filteredData.setPredicate(s -> true);
				} else {
					filteredData.setPredicate(s -> s.getNombre().contains(filtro));
				}
			}
			
		};
		tfBuscador.textProperty().addListener(listenerFiltro);

		lvExamenes.setItems(filteredData);
	}

	// BOTONES DEL PANEL CENTRAL
	
	/**
	 * Método encargado de modificar la posición de la pregunta seleccionada. En este caso,
	 * la pregunta se trasladará a la posición posterior.
	 * @param event La acción de hacer click en el botón marcado con una flecha apuntando hacia abajo
	 */
	@FXML
	void onAbajoAction(ActionEvent event) {
		int actual = lvPreguntas.getSelectionModel().getSelectedIndex();
		Collections.swap(listadoPreguntas, actual, actual + 1);
	}
	
	/**
	 * Método encargado de modificar la posición de la pregunta seleccionada. En este caso,
	 * la pregunta se trasladará a la posición anterior.
	 * @param event La acción de hacer click en el botón marcado con una flecha apuntando hacia arriba
	 */
	@FXML
	void onArribaAction(ActionEvent event) {
		int actual = lvPreguntas.getSelectionModel().getSelectedIndex();
		Collections.swap(listadoPreguntas, actual, actual - 1);
	}
	
	/**
	 * Método encargado de eliminar la pregunta seleccionada de la lista de preguntas
	 * @param event La acción de hacer click en el botón marcado con una equis
	 */
	@FXML
	void onQuitarAction(ActionEvent event) {
		//int seleccionado = lvPreguntas.getSelectionModel().getSelectedIndex();
		//lvPreguntas.getItems().remove(seleccionado);
		String[] error = {""};
		Pregunta preguntaSeleccionada = lvPreguntas.getSelectionModel().getSelectedItem();
		if (GestorDB.eliminarPreguntaExamen(examenSeleccionado.get(), preguntaSeleccionada, error)) {
			examenSeleccionado.get().preguntasProperty().remove(preguntaSeleccionada);
		} else {
			App.mostrarMensajeError(
					"BananaTest - Quitar pregunta a examen",
					"Se ha producido un error",
					error
			);
		}
	}
	
	/**
	 * Método encargado de añadir una nueva pregunta al listado de preguntas.
	 * @param event La acción de hacer click en el botón agregar pregunta
	 */
	@FXML
	void onAgregarAction(ActionEvent event) {
		DialogoAgregarPregunta diag_agregar = new DialogoAgregarPregunta("Agregar pregunta", "Aceptar", "Cancelar", examenSeleccionado.get().getIdExamen());
		Optional<Pregunta> result = diag_agregar.showAndWait();
		if (result.isPresent()) {
			String[] error = {""};
			Pregunta preguntaSeleccionada = result.get();			
			if (GestorDB.asignarPreguntaExamen(examenSeleccionado.get(), preguntaSeleccionada, error)) {
				examenSeleccionado.get().getPreguntas().add(preguntaSeleccionada);
			} else {
				App.mostrarMensajeError(
						"BananaTest - Añadir pregunta a examen",
						"Se ha producido un error",
						error
				);
			}
			
		}

	}

	// PANEL IZQUIERDO
	
	/**
	 * Método encargado de crear un nuevo examen.
	 * @param event La acción de hacer click en el botón crear.
	 */
	@FXML
	void onCrearAction(ActionEvent event) {
		DialogoCrearExamen diag_crear = new DialogoCrearExamen("Crear nuevo examen", "Aceptar", "Cancelar");
		Optional<Pair<String, String>> result = diag_crear.showAndWait();
		if (result.isPresent()) {
			Pair<String, String> pair;
			pair = result.get();
			Examen examen = new Examen(pair.getKey(), pair.getValue());
			String[] error = {""};
			examen.setCreador(Preferencias.idUsuario);
			
			if (GestorDB.crearExamen(examen, error)) {
				cargarListadoExamenes();
			} else {
				App.mostrarMensajeError(
						"BananaTest - Crear nuevo examen",
						"Se ha producido un error",
						error
				);
			}
		}
	}
	
	/**
	 * Método encargado de modificar el examen que este seleccionado en ese momento.
	 * @param event La acción de hacer click en el botón modificar.
	 */
	@FXML
	void onModificarAction(ActionEvent event) {
		// Creamos un objectproperty donde metemos el examen a modificar seleccionado
		DialogoModificarExamen diag_mod = new DialogoModificarExamen(
				"Modificar examen", "Aceptar", "Cancelar",
				examenSeleccionado.get()
		);
		
		Optional<Pair<String, String>> result = diag_mod.showAndWait();
		if (result.isPresent()) {
			examenSeleccionado.get().setNombre(result.get().getKey());
			examenSeleccionado.get().setDescripcion(result.get().getValue());
			String[] error = {""};
			if (GestorDB.modificarExamen(examenSeleccionado.get(), error)) {
				cargarListadoExamenes();
			} else {
				App.mostrarMensajeError(
						"BananaTest - Modificar examen existente",
						"Se ha producido un error",
						error
				);
			}
		}

	}
	
	/**
	 * Método encargado de borrar el examen que este seleccionado en ese momento.
	 * @param event La acción de hacer click en el botón de borrar.
	 */
	@FXML
	void onBorrarAction(ActionEvent event) {
		DialogoConfirmar dialog = new DialogoConfirmar(
				"Borrar Examen",
				"Borrar un examen es una operación irreversible.\n¿Está seguro de querer continuar?",
				"Borrar",
				"Cancelar"
		);
		
		Optional<Boolean> result = dialog.showAndWait();
		
		if (result.isPresent()) {
			String[] error = {""};
			if (GestorDB.eliminarExamen(examenSeleccionado.get(), error)) {
				cargarListadoExamenes();
			} else {
				App.mostrarMensajeError(
						"BananaTest - Borrar examen existente",
						"Se ha producido un error",
						error
				);
			}
		}
	}

	// PANEL DERECHO
	
	/**
	 * Método que refresca el puntaje que contiene la pregunta seleccionada.
	 * @param event La acción de hacer click en el botón de refrescar.
	 */
	@FXML
	void onRefrescarAction(ActionEvent event) {
		int peso = Integer.valueOf(txPuntuacion.getText());
		String[] error = {""};
		if (GestorDB.actualizarPesoPregunta(examenSeleccionado.get().getIdExamen(), preguntaSeleccionada.get().getIdPregunta(), peso, error)) {
			App.mostrarMensajeExito(
					"BananaTest - Actualización del peso de la pregunta",
					"Puntuación de la pregunta actualizado con éxito."
			);
		} else {
			App.mostrarMensajeError(
					"BananaTest - Actualización del peso de la pregunta",
					"Se ha producido un error.",
					error
			);
		}
	}
	
	/**
	 * Método que devuelve la vista dentro del tab Examenes.
	 * @return Objeto SplitPane que contiene la vista de toda la tab Examenes.
	 */
	public SplitPane getView() {
		return ViewExamen;
	}

	/**
	 * Método encargado de devolver el examen actualmente seleccionado
	 * sólo para manejar, no para editar.
	 * @return Instancia de la clase Examen seleccionado
	 */
	public final Examen getExamenSeleccionado() {
		return examenSeleccionado.get();
	}

}
