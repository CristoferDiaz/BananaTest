package dad.proyectos.banana_test.model.preguntas;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.ResourceBundle;

import dad.proyectos.banana_test.model.Pregunta;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextArea;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;

/**
 * Clase gestora del componente correspondiente a una pregunta de
 * tipo test de selección simple con 4 respuestas posibles.
 * 
 * @author Crmprograming
 */
public class PreguntaTestSimple extends Pregunta {
	
	private final int TOTAL_RESPUESTAS = 4;
	
	// model
	private StringProperty respuestaValida = new SimpleStringProperty();
	private StringProperty respuesta2 = new SimpleStringProperty();
	private StringProperty respuesta3 = new SimpleStringProperty();
	private StringProperty respuesta4 = new SimpleStringProperty();
	
	// view
	@FXML
	private BorderPane view;
	
	@FXML
	private VBox contenedorRespuestas;
		
	@FXML
	private Label lbPregunta;
		
	private ToggleGroup grupoRespuestas;
	
	private RadioButton rbResp1, rbResp2, rbResp3, rbResp4;

	/**
	 * Constructor básico de la clase.
	 * 
	 * @param textoPregunta String con el texto de la pregunta
	 * @param respuestas Array de String donde la posición 0 se corresponde a la respuesta válida y las otras 3 a respuestas falsas
	 */
	public PreguntaTestSimple(String textoPregunta, String[] respuestas) {
		super();
		
		tipoPregunta = TIPO_PREGUNTA.TEST_RESPUESTA_SIMPLE;
		
		if (respuestas.length != TOTAL_RESPUESTAS)
			throw new IllegalArgumentException("Se esperaban un total de " + TOTAL_RESPUESTAS + " respuestas.");
		
		try {
			FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/componentes/PlantillaPreguntaTipoTest.fxml"));
			loader.setController(this);
			loader.setRoot(this);
			loader.load();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		pregunta.set(textoPregunta);
		respuestaValida.set(respuestas[0]);
		respuesta2.set(respuestas[1]);
		respuesta3.set(respuestas[2]);
		respuesta4.set(respuestas[3]);
	}

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		construirRadioButtons();
		
		lbPregunta.textProperty().bind(pregunta);
		grupoRespuestas.selectedToggleProperty().addListener((o, ov, nv) -> {
			if (nv != null) {
				RadioButton radioButton = (RadioButton) nv;
				correcta.set(radioButton.getText().equals(respuestaValida.get()));
			}
		});
	}

	/**
	 * Método encargado de construir los radiobutton de las respuestas posibles
	 * e inyectarlo en el contenedor principal del componente.
	 */
	private void construirRadioButtons() {
		List<StringProperty> respuestas = new ArrayList<StringProperty>();
		respuestas.addAll(Arrays.asList(respuestaValida, respuesta2, respuesta3, respuesta4));
		Collections.shuffle(respuestas);
		
		grupoRespuestas = new ToggleGroup();
		
		rbResp1 = new RadioButton();
		rbResp1.textProperty().bind(respuestas.get(0));
		rbResp1.setToggleGroup(grupoRespuestas);
		
		rbResp2 = new RadioButton();
		rbResp2.textProperty().bind(respuestas.get(1));
		rbResp2.setToggleGroup(grupoRespuestas);
		
		rbResp3 = new RadioButton();
		rbResp3.textProperty().bind(respuestas.get(2));
		rbResp3.setToggleGroup(grupoRespuestas);
		
		rbResp4 = new RadioButton();
		rbResp4.textProperty().bind(respuestas.get(3));
		rbResp4.setToggleGroup(grupoRespuestas);
		
		contenedorRespuestas.getChildren().addAll(rbResp1, rbResp2, rbResp3, rbResp4);
	}

	@Override
	public boolean esCorrecta() {
		return correcta.get();
	}
	
	@Override
	public StringProperty[] obtenerRespuestas() {		
		return new StringProperty[] {
				respuestaValida,
				respuesta2,
				respuesta3,
				respuesta4
		};
	}

	@Override
	public Node construirFormularioEditable() {
		Label lbPregunta = new Label("Pregunta:");
		TextArea taPregunta = new TextArea(pregunta.get());
		Label lbRespuestaValida = new Label("Respuesta válida:");
		TextArea taRespuestaValida = new TextArea();
		Label lbRespuesta2 = new Label("Respuesta 2:");
		TextArea taRespuesta2 = new TextArea();
		Label lbRespuesta3 = new Label("Respuesta 3:");
		TextArea taRespuesta3 = new TextArea();
		Label lbRespuesta4 = new Label("Respuesta 4:");
		TextArea taRespuesta4 = new TextArea();
		
		taPregunta.textProperty().bindBidirectional(pregunta);
		taRespuestaValida.textProperty().bindBidirectional(respuestaValida);
		taRespuesta2.textProperty().bindBidirectional(respuesta2);
		taRespuesta3.textProperty().bindBidirectional(respuesta3);
		taRespuesta4.textProperty().bindBidirectional(respuesta4);
				
		GridPane root = new GridPane();
		root.add(lbPregunta, 0, 0);
		root.add(taPregunta, 0, 1, 2, 1);
		root.add(lbRespuestaValida, 0, 2);
		root.add(taRespuestaValida, 0, 3, 2, 1);
		root.add(lbRespuesta2, 0, 4);
		root.add(taRespuesta2, 0, 5, 2, 1);
		root.add(lbRespuesta3, 0, 6);
		root.add(taRespuesta3, 0, 7, 2, 1);
		root.add(lbRespuesta4, 0, 8);
		root.add(taRespuesta4, 0, 9, 2, 1);
		
		root.setMaxWidth(Double.MAX_VALUE);
		root.setPadding(new Insets(5));
		taPregunta.setPrefWidth(Double.MAX_VALUE);		
		taPregunta.setPrefHeight(100);
		return root;
	}

	/**
	 * Método getter del atributo view.
	 * @return instancia de BorderPane asociado
	 */
	public BorderPane getView() {
		return view;
	}

	/**
	 * Método setter del atributo view
	 * @param view Instancia de BorderPane asociado
	 */
	public void setView(BorderPane view) {
		this.view = view;
	}

	/**
	 * Método getter del property asociado a respuestaValida.
	 * @return instancia de StringProperty asociada
	 */
	public final StringProperty respuestaValidaProperty() {
		return this.respuestaValida;
	}

	/**
	 * Método getter del atributo respuestaValida.
	 * @return instancia de String asociada
	 */
	public final String getRespuestaValida() {
		return this.respuestaValidaProperty().get();
	}

	/**
	 * Método setter del atributo respuestaValida.
	 * @param respuestaValida Instancia de String asociado
	 */
	public final void setRespuestaValida(final String respuestaValida) {
		this.respuestaValidaProperty().set(respuestaValida);
	}

	/**
	 * Método getter del property asociado a respuesta2.
	 * @return instancia de StringProperty asociada
	 */
	public final StringProperty respuesta2Property() {
		return this.respuesta2;
	}

	/**
	 * Método getter del atributo respuesta2.
	 * @return instancia de String asociada
	 */
	public final String getRespuesta2() {
		return this.respuesta2Property().get();
	}

	/**
	 * Método setter del atributo respuesta2.
	 * @param respuesta2 Instancia de String asociado
	 */
	public final void setRespuesta2(final String respuesta2) {
		this.respuesta2Property().set(respuesta2);
	}

	/**
	 * Método getter del property asociado a respuesta3.
	 * @return instancia de StringProperty asociada
	 */
	public final StringProperty respuesta3Property() {
		return this.respuesta3;
	}

	/**
	 * Método getter del atributo respuesta3.
	 * @return instancia de String asociada
	 */
	public final String getRespuesta3() {
		return this.respuesta3Property().get();
	}

	/**
	 * Método setter del atributo respuesta3.
	 * @param respuesta3 Instancia de String asociado
	 */
	public final void setRespuesta3(final String respuesta3) {
		this.respuesta3Property().set(respuesta3);
	}

	/**
	 * Método getter del property asociado a respuesta4.
	 * @return instancia de StringProperty asociada
	 */
	public final StringProperty respuesta4Property() {
		return this.respuesta4;
	}

	/**
	 * Método getter del atributo respuesta4.
	 * @return instancia de String asociada
	 */
	public final String getRespuesta4() {
		return this.respuesta4Property().get();
	}

	/**
	 * Método setter del atributo respuesta4.
	 * @param respuesta4 Instancia de String asociado
	 */
	public final void setRespuesta4(final String respuesta4) {
		this.respuesta4Property().set(respuesta4);
	}
	
}
