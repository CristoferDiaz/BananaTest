package dad.proyectos.banana_test.model.preguntas;

import java.io.IOException;
import java.net.URL;
import java.util.Collections;
import java.util.ResourceBundle;

import dad.proyectos.banana_test.model.Pregunta;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.ListProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleListProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.util.Pair;

/**
 * Clase gestora del componente correspondiente a una pregunta de
 * tipo test de selección múltiple con 4 respuestas posibles.
 * 
 * @author Crmprograming
 */
public class PreguntaTestMultiple extends Pregunta {
	
	private final int TOTAL_RESPUESTAS = 4;
	
	// model
	private ListProperty<Pair<CheckBox, BooleanProperty>> respuestas = new SimpleListProperty<Pair<CheckBox, BooleanProperty>>(FXCollections.observableArrayList());
	
	// view
	@FXML
	private BorderPane view;
	
	@FXML
	private VBox contenedorRespuestas;
		
	@FXML
	private Label lbPregunta;

	/**
	 * Constructor básico de la clase. Las posiciones a true del array correctas especifican
	 * las posiciones de aquellas respuestas válidas para la pregunta.
	 * 
	 * Si por ejemplo se tiene un array correctas tal que así:
	 * 
	 * [false, true, false, true]
	 * 
	 * la solución a esta pregunta serían las respuestas 2 y 4.
	 * 
	 * @param textoPregunta String con el texto de la pregunta
	 * @param textosRespuestas Array de String con los textos de cada respuesta
	 * @param correctas Array de Boolean con concordancia entre respuesta y si es la correcta o no
	 */
	public PreguntaTestMultiple(String textoPregunta, String[] textosRespuestas, boolean[] correctas) {
		super();
		
		tipoPregunta = TIPO_PREGUNTA.TEST_RESPUESTA_MULTIPLE;
		
		if (textosRespuestas.length != TOTAL_RESPUESTAS || correctas.length != TOTAL_RESPUESTAS)
			throw new IllegalArgumentException("Se esperaban un total de " + TOTAL_RESPUESTAS + " respuestas.");
		
		/*pregunta.set(textoPregunta);
		for (int i = 0; i < TOTAL_RESPUESTAS; i++) {
			CheckBox checkbox = new CheckBox(textosRespuestas[i]);
			Pair<CheckBox, BooleanProperty> pair = new Pair<CheckBox, BooleanProperty>(checkbox, new SimpleBooleanProperty(correctas[i]));
			respuestas.get().add(pair);
		}
		Collections.shuffle(respuestas.get());*/
		
		/*try {
			FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/componentes/PlantillaPreguntaTipoTest.fxml"));
			loader.setController(this);
			loader.setRoot(this);
			loader.load();
		} catch (IOException e) {
			e.printStackTrace();
		}*/
		
	}

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		lbPregunta.textProperty().bind(pregunta);
		for (int i = 0; i < TOTAL_RESPUESTAS; i++) {
			contenedorRespuestas.getChildren().add(respuestas.get(i).getKey());
			respuestas.get(i).getKey().selectedProperty().addListener(e -> actualizarCorrecta());
		}
	}
	
	/**
	 * Método encargado de actualizar si la pregunta es válida en su totalidad (todas las
	 * respuestas han sido marcadas o no según correspondía en la solución).
	 * Será invocado cuando cualquiera de los CheckBox cambie su estado.
	 */
	private void actualizarCorrecta() {
		int i = 0;
		
		while (i < TOTAL_RESPUESTAS && respuestas.get(i).getKey().isSelected() == respuestas.get(i).getValue().getValue())
			i++;
		
		correcta.set(i == TOTAL_RESPUESTAS);
	}

	@Override
	public boolean esCorrecta() {
		return correcta.get();
	}

	@Override
	public StringProperty[] obtenerRespuestas() {
		StringProperty[] lista = new StringProperty[TOTAL_RESPUESTAS];
		
		for (int i = 0; i < TOTAL_RESPUESTAS; i++)
			lista[i] = respuestas.get(i).getKey().textProperty();
		
		return lista;
	}
	
	@Override
	public Node construirFormularioEditable() {
		GridPane root = new GridPane();
		Label lbPregunta = new Label("Pregunta:");
		TextArea taPregunta = new TextArea(pregunta.get());
		
		root.add(lbPregunta, 0, 0);
		root.add(taPregunta, 0, 1, 3, 1);
		
		int j = 2;		
		for (int i = 0; i < TOTAL_RESPUESTAS; i++) {
			Label label = new Label("Respuesta " + (i + 1) + ":");
			CheckBox checkBox = new CheckBox();
			TextArea textArea = new TextArea();
			root.add(label, 0, j++);
			root.add(checkBox, 0, j);
			root.add(textArea, 1, j++, 2, 1);
			checkBox.selectedProperty().bindBidirectional(respuestas.get(i).getValue());
			GridPane.setHalignment(checkBox, HPos.CENTER);
			textArea.textProperty().bindBidirectional(respuestas.get(i).getKey().textProperty());
		}
		
		root.setMaxWidth(Double.MAX_VALUE);
		root.setPadding(new Insets(5));
		taPregunta.setPrefWidth(Double.MAX_VALUE);		
		taPregunta.setPrefHeight(100);
		
		ColumnConstraints [] cols = {
			new ColumnConstraints(),	
			new ColumnConstraints(),
			new ColumnConstraints(),
		};
		root.getColumnConstraints().setAll(cols);
		cols[2].setHgrow(Priority.ALWAYS);
		
		return root;
	}
	
	/**
	 * Método encargado de obtener el mapeo de validez de las respuestas.
	 * 
	 * @return Array de Boolean con la correspondencia de los estados de las respuestas
	 */
	public Boolean[] obtenerValidez() {
		Boolean[] lista = new Boolean[TOTAL_RESPUESTAS];
		
		for (int i = 0; i < TOTAL_RESPUESTAS; i++)
			lista[i] = respuestas.get(i).getValue().get();
		
		return lista;
	}
	
	public BorderPane getView() {
		return view;
	}

	public void setView(BorderPane view) {
		this.view = view;
	}

}
