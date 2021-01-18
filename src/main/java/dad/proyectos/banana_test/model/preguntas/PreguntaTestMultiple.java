package dad.proyectos.banana_test.model.preguntas;

import java.io.IOException;
import java.net.URL;
import java.util.Collections;
import java.util.ResourceBundle;

import dad.proyectos.banana_test.model.Pregunta;
import javafx.beans.property.ListProperty;
import javafx.beans.property.SimpleListProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
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
	private ListProperty<Pair<CheckBox, Boolean>> respuestas = new SimpleListProperty<Pair<CheckBox, Boolean>>(FXCollections.observableArrayList());
	
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
		
		pregunta.set(textoPregunta);
		for (int i = 0; i < TOTAL_RESPUESTAS; i++) {
			CheckBox checkbox = new CheckBox(textosRespuestas[i]);
			Pair<CheckBox, Boolean> pair = new Pair<CheckBox, Boolean>(checkbox, correctas[i]);
			respuestas.get().add(pair);
		}
		Collections.shuffle(respuestas.get());
		
		try {
			FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/componentes/PlantillaPreguntaTipoTest.fxml"));
			loader.setController(this);
			loader.setRoot(this);
			loader.load();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
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
		
		while (i < TOTAL_RESPUESTAS && respuestas.get(i).getKey().isSelected() == respuestas.get(i).getValue())
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
	
	/**
	 * Método encargado de obtener el mapeo de validez de las respuestas.
	 * 
	 * @return Array de Boolean con la correspondencia de los estados de las respuestas
	 */
	public Boolean[] obtenerValidez() {
		Boolean[] lista = new Boolean[TOTAL_RESPUESTAS];
		
		for (int i = 0; i < TOTAL_RESPUESTAS; i++)
			lista[i] = respuestas.get(i).getValue();
		
		return lista;
	}
	
	public BorderPane getView() {
		return view;
	}

	public void setView(BorderPane view) {
		this.view = view;
	}

}
