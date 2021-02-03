package dad.proyectos.banana_test.controller;

import java.io.IOException;
import java.net.URL;
import java.util.Collections;
import java.util.Optional;
import java.util.ResourceBundle;

import dad.proyectos.banana_test.model.Examen;
import dad.proyectos.banana_test.model.Pregunta;
import dad.proyectos.banana_test.model.preguntas.PreguntaTestMultiple;
import dad.proyectos.banana_test.model.preguntas.PreguntaTestSimple;
import dad.proyectos.banana_test.utils.DialogoAgregarPregunta;
import dad.proyectos.banana_test.utils.DialogoCrear;
import dad.proyectos.banana_test.utils.DialogoModificar;
import javafx.beans.binding.Bindings;
import javafx.beans.property.ListProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleListProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.FXCollections;
import javafx.collections.transformation.FilteredList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.SplitPane;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.util.Pair;
import javafx.util.converter.NumberStringConverter;

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

	// Lista de preguntas
	private ListProperty<Pregunta> listadoPreguntas = new SimpleListProperty<Pregunta>(FXCollections.observableArrayList());
	private ObjectProperty<Pregunta> preguntaSeleccionada = new SimpleObjectProperty<Pregunta>();

	// CONSTRUCTOR
	public TabExamenesController() throws IOException {
		FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/examenes/TabExamenesView.fxml"));
		loader.setController(this);
		loader.load();
	}

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		// TODO: Cargar Exámenes de la BD
		Examen examen1 = new Examen("Examen 1", "Primer trimestre");
		examen1.getPreguntas()
				.addAll(new PreguntaTestSimple("Texto de la primera pregunta",
						new String[] { "Respuesta válida 1", "Respuesta 2", "Respuesta 3", "Respuesta 4" }),
						new PreguntaTestSimple("Texto de la segunda pregunta",
								new String[] { "Respuesta válida 1", "Respuesta 2", "Respuesta 3", "Respuesta 4" }),
						new PreguntaTestMultiple("Texto de la tercera pregunta",
								new String[] { "Respuesta 1", "Respuesta 2", "Respuesta 3", "Respuesta 4" },
								new boolean[] { true, true, false, false }),
						new PreguntaTestMultiple("Texto de la cuarta pregunta",
								new String[] { "Respuesta 1", "Respuesta 2", "Respuesta 3", "Respuesta 4" },
								new boolean[] { true, true, false, false }));

		Examen examen2 = new Examen("Examen 2", "Segundo Trimestre");

		listadoExamenes.addAll(examen1, examen2);
		lvExamenes.setItems(listadoExamenes);

		crearFiltroBuscador();

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
		
		txPuntuacion.disableProperty().bind(preguntaSeleccionada.isNull());
		btActPuntuacion.disableProperty().bind(preguntaSeleccionada.isNull());
	}

	// FILTRADOR DE TEXTFIELD A UN LISTVIEW
	private void crearFiltroBuscador() {
		FilteredList<Examen> filteredData = new FilteredList<Examen>(listadoExamenes, s -> true);
		tfBuscador.textProperty().addListener((o, ov, nv) -> {
			String filtro = tfBuscador.getText();

			if (filtro == null || filtro.length() == 0) {
				filteredData.setPredicate(s -> true);
			} else {
				filteredData.setPredicate(s -> s.getNombre().contains(filtro));
			}

		});

		lvExamenes.setItems(filteredData);
	}

	// BOTONES DEL PANEL CENTRAL
	@FXML
	void onAbajoAction(ActionEvent event) {
		int actual = lvPreguntas.getSelectionModel().getSelectedIndex();
		Collections.swap(listadoPreguntas, actual, actual + 1);
	}

	@FXML
	void onArribaAction(ActionEvent event) {
		int actual = lvPreguntas.getSelectionModel().getSelectedIndex();
		Collections.swap(listadoPreguntas, actual, actual - 1);
	}

	@FXML
	void onQuitarAction(ActionEvent event) {
		int seleccionado = lvPreguntas.getSelectionModel().getSelectedIndex();
		lvPreguntas.getItems().remove(seleccionado);
	}

	@FXML
	void onAgregarAction(ActionEvent event) {
		DialogoAgregarPregunta diag_agregar = new DialogoAgregarPregunta("Agregar pregunta", "Aceptar", "Cancelar");
		Optional<Pregunta> result = diag_agregar.showAndWait();
		if (result.isPresent()) {
			Pregunta preguntaSeleccionada;
			preguntaSeleccionada = result.get();
			examenSeleccionado.get().getPreguntas().add(preguntaSeleccionada);
		}

	}

	// PANEL IZQUIERDO

	@FXML
	void onCrearAction(ActionEvent event) {
		DialogoCrear diag_crear = new DialogoCrear("Crear nuevo examen", "Aceptar", "Cancelar");
		Optional<Pair<String, String>> result = diag_crear.showAndWait();
		if (result.isPresent()) {
			Pair<String, String> pair;
			pair = result.get();
			Examen examen = new Examen(pair.getKey(), pair.getValue());
			listadoExamenes.add(examen);
			lvExamenes.setItems(listadoExamenes);
			crearFiltroBuscador();
		}
	}

	@FXML
	void onModificarAction(ActionEvent event) {
		// Creamos un objectproperty donde metemos el examen a modificar seleccionado
		DialogoModificar diag_mod = new DialogoModificar(
				"Modificar examen", "Aceptar", "Cancelar",
				examenSeleccionado.get()
		);
		
		Optional<Pair<String, String>> result = diag_mod.showAndWait();
		if (result.isPresent()) {
			examenSeleccionado.get().setNombre(result.get().getKey());
			examenSeleccionado.get().setDescripcion(result.get().getValue());
		}

	}

	@FXML
	void onBorrarAction(ActionEvent event) {
		Alert alert = new Alert(AlertType.CONFIRMATION);
		alert.setTitle("Borrar examen.");
		alert.setContentText(
				"Está a punto de borrar " + examenSeleccionado.get().getNombre());

		// CONTROLAMOS LA DECISION DEL USUARIO
		Optional<ButtonType> result = alert.showAndWait();
		if (result.isPresent() && result.get() == ButtonType.OK) {
			listadoExamenes.remove(examenSeleccionado.get()); // ELIMINAMOS EL EXAMEN SELECCIONADO
		}
	}

	// PANEL DERECHO
	@FXML
	void onRefrescarAction(ActionEvent event) {
		// TODO: Actualizar puntuación de la pregunta en la BD
	}

	public SplitPane getView() {
		return ViewExamen;
	}

}