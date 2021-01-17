package dad.proyectos.banana_test.model;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.fxml.Initializable;
import javafx.scene.layout.BorderPane;

/**
 * Clase plantilla de las preguntas que tendrá la aplicación.
 * Será la clase hija la encargada de implementar la construcción de los
 * componentes, los bindeos y demás aspectos específicos de cada componente.
 * 
 * @author Crmprograming
 */
public abstract class Pregunta extends BorderPane implements Initializable {

	public static enum TIPO_PREGUNTA {
		TEST_RESPUESTA_SIMPLE,
		TEST_RESPUESTA_MULTIPLE
	}
	
	// model
	
	protected IntegerProperty idPregunta = new SimpleIntegerProperty();
	protected StringProperty pregunta = new SimpleStringProperty();
	protected IntegerProperty peso = new SimpleIntegerProperty(0); // [0, 100] valores posibles
	protected BooleanProperty correcta = new SimpleBooleanProperty(false);
	
	/**
	 * Constructor por defecto de la clase. Limitado a protegido
	 * para que nadie salvo las clases hijas puedan invocarlo y así evitar
	 * que se construyan objetos Pregunta sin tener valores asignados.
	 * 
	 * Inicializa el identificador de pregunta a un valor por defecto (-1).
	 */
	protected Pregunta() {
		this(-1);
	}
	
	/**
	 * Constructor extendido de la clase. Limitado a protegido
	 * para que nadie salvo las clases hijas puedan invocarlo y así evitar
	 * que se construyan objetos Pregunta sin tener valores asignados.
	 */
	protected Pregunta(int id) {
		super();
		idPregunta.set(id);
	}
	
	/**
	 * Método abstracto encargado de especificar si una pregunta se encuentra como
	 * correcta o no. Cada clase hija será la encargada de implementar cómo se
	 * evaluará si se encuentra en estado correcto o no.
	 * @return true si la pregunta es correcta.
	 */
	public abstract boolean esCorrecta();
	
	/**
	 * Método abstracto encargado de extraer las respuestas posibles en forma de
	 * String. Cada clase hija será la encargada de implementar cómo se obtienen
	 * dichas respuestas.
	 * @return array de String con los textos de cada una de las respuestas
	 */
	public abstract StringProperty[] obtenerRespuestas();
	
	public final IntegerProperty idPreguntaProperty() {
		return this.idPregunta;
	}

	public final int getIdPregunta() {
		return this.idPreguntaProperty().get();
	}

	public final void setIdPregunta(final int idPregunta) {
		this.idPreguntaProperty().set(idPregunta);
	}

	public final StringProperty preguntaProperty() {
		return this.pregunta;
	}

	public final String getPregunta() {
		return this.preguntaProperty().get();
	}

	public final void setPregunta(final String pregunta) {
		this.preguntaProperty().set(pregunta);
	}

	public final IntegerProperty pesoProperty() {
		return this.peso;
	}

	public final int getPeso() {
		return this.pesoProperty().get();
	}

	public final void setPeso(final int peso) {
		this.pesoProperty().set(peso);
	}
	
	public final BooleanProperty correctaProperty() {
		return this.correcta;
	}

	public final boolean isCorrecta() {
		return this.correctaProperty().get();
	}

	public final void setCorrecta(final boolean correcta) {
		this.correctaProperty().set(correcta);
	}

}
