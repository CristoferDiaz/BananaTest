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
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.BorderPane;
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
				System.out.println(correcta);
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

	public BorderPane getView() {
		return view;
	}

	public void setView(BorderPane view) {
		this.view = view;
	}

	public final StringProperty respuestaValidaProperty() {
		return this.respuestaValida;
	}

	public final String getRespuestaValida() {
		return this.respuestaValidaProperty().get();
	}

	public final void setRespuestaValida(final String respuestaValida) {
		this.respuestaValidaProperty().set(respuestaValida);
	}

	public final StringProperty respuesta2Property() {
		return this.respuesta2;
	}

	public final String getRespuesta2() {
		return this.respuesta2Property().get();
	}

	public final void setRespuesta2(final String respuesta2) {
		this.respuesta2Property().set(respuesta2);
	}

	public final StringProperty respuesta3Property() {
		return this.respuesta3;
	}

	public final String getRespuesta3() {
		return this.respuesta3Property().get();
	}

	public final void setRespuesta3(final String respuesta3) {
		this.respuesta3Property().set(respuesta3);
	}

	public final StringProperty respuesta4Property() {
		return this.respuesta4;
	}

	public final String getRespuesta4() {
		return this.respuesta4Property().get();
	}

	public final void setRespuesta4(final String respuesta4) {
		this.respuesta4Property().set(respuesta4);
	}
	
}
