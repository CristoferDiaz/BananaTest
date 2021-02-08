package dad.proyectos.banana_test.controller;

import java.io.IOException;
import java.net.URL;
import java.util.Optional;
import java.util.ResourceBundle;

import dad.proyectos.banana_test.model.Pregunta;
import dad.proyectos.banana_test.model.preguntas.PreguntaTestMultiple;
import dad.proyectos.banana_test.model.preguntas.PreguntaTestSimple;
import dad.proyectos.banana_test.utils.dialogos.DialogoConfirmar;
import dad.proyectos.banana_test.utils.dialogos.tab_preguntas.DialogoPregunta;
import javafx.beans.property.ListProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleListProperty;
import javafx.beans.property.SimpleObjectProperty;
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

public class TabPreguntasController implements Initializable {

	// model
	
	private ListProperty<Pregunta> listadoPreguntas = new SimpleListProperty<Pregunta>(FXCollections.observableArrayList());
	private ObjectProperty<Pregunta> preguntaSeleccionada = new SimpleObjectProperty<Pregunta>();
	
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

	public TabPreguntasController() throws IOException {
		FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/preguntas/TabPreguntasView.fxml"));
		loader.setController(this);
		loader.load();
	}

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		// TODO: Cargar listado de preguntas de la bd
		listadoPreguntas.addAll(
			new PreguntaTestSimple(
				"Texto de la primera pregunta",
				new String[] {
						"Respuesta válida 1",
						"Respuesta 2",
						"Respuesta 3",
						"Respuesta 4"
				}
			),
			new PreguntaTestSimple(
				"Texto de la segunda pregunta",
				new String[] {
						"Respuesta válida 1",
						"Respuesta 2",
						"Respuesta 3",
						"Respuesta 4"
				}
			),
			new PreguntaTestMultiple(
					"Texto de la tercera pregunta",
					new String[] {
							"Respuesta 1",
							"Respuesta 2",
							"Respuesta 3",
							"Respuesta 4"
					},
					new boolean[] {
							true,
							true,
							false,
							false
					}
			),
			new PreguntaTestMultiple(
					"Texto de la cuarta pregunta",
					new String[] {
							"Respuesta 1",
							"Respuesta 2",
							"Respuesta 3",
							"Respuesta 4"
					},
					new boolean[] {
							true,
							true,
							false,
							false
					}
			)
			
		);
		
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
			// TODO: Añadir pregunta a la BD y rescatar de nuevo el listado
			listadoPreguntas.add(result.get());
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
			// TODO: Borrar pregunta de la BD y rescatar de nuevo el listado
			listadoPreguntas.remove(preguntaSeleccionada.get());
		}
    }
	
	@FXML
    void onAplicarCambios(ActionEvent event) {
		// TODO: Actualizar la pregunta en la BD y rescatar de nuevo el listado
    }

	public HBox getView() {
		return view;
	}

	public void setView(HBox view) {
		this.view = view;
	}

}
