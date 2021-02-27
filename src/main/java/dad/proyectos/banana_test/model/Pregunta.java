package dad.proyectos.banana_test.model;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.layout.BorderPane;

/**
 * Clase plantilla de las preguntas que tendrá la aplicación. Será la clase hija
 * la encargada de implementar la construcción de los componentes, los bindeos y
 * demás aspectos específicos de cada componente.
 * 
 * @author Crmprograming
 */
public abstract class Pregunta extends BorderPane implements Initializable {

	public static enum TIPO_PREGUNTA {
		TEST_RESPUESTA_SIMPLE, TEST_RESPUESTA_MULTIPLE
	}

	protected TIPO_PREGUNTA tipoPregunta;

	// model

	protected IntegerProperty idPregunta = new SimpleIntegerProperty();
	protected StringProperty pregunta = new SimpleStringProperty();
	protected IntegerProperty peso = new SimpleIntegerProperty(0); // [0, 100] valores posibles
	protected BooleanProperty correcta = new SimpleBooleanProperty(false);
	protected IntegerProperty creador = new SimpleIntegerProperty();

	/**
	 * Constructor por defecto de la clase. Limitado a protegido para que nadie
	 * salvo las clases hijas puedan invocarlo y así evitar que se construyan
	 * objetos Pregunta sin tener valores asignados.
	 * 
	 * Inicializa el identificador de pregunta a un valor por defecto (-1).
	 */
	protected Pregunta() {
		this(-1);
	}

	/**
	 * Constructor extendido de la clase. Limitado a protegido para que nadie salvo
	 * las clases hijas puedan invocarlo y así evitar que se construyan objetos
	 * Pregunta sin tener valores asignados.
	 */
	protected Pregunta(int id) {
		idPregunta.set(id);
		creador.set(-1);
	}

	/**
	 * Método abstracto encargado de especificar si una pregunta se encuentra como
	 * correcta o no. Cada clase hija será la encargada de implementar cómo se
	 * evaluará si se encuentra en estado correcto o no.
	 * 
	 * @return true si la pregunta es correcta.
	 */
	public abstract boolean esCorrecta();

	/**
	 * Método abstracto encargado de extraer las respuestas posibles en forma de
	 * String. Cada clase hija será la encargada de implementar cómo se obtienen
	 * dichas respuestas.
	 * 
	 * @return array de String con los textos de cada una de las respuestas
	 */
	public abstract StringProperty[] obtenerRespuestas();

	/**
	 * Método encargado de generar un formulario editable asociado
	 * a la pregunta y las respuestas que la forman. Cada clase hija será la
	 * encargada de implementar cómo se obtienen dicho formulario.
	 * @return instancia de Node con el componente elaborado
	 */
	public abstract Node construirFormularioEditable();

	/**
	 * Método getter del atributo tipoPregunta.
	 * @return instancia del enum TIPO_PREGUNTA
	 */
	public TIPO_PREGUNTA getTipoPregunta() {
		return tipoPregunta;
	}

	/**
	 * Método getter del property asociado a idPregunta.
	 * @return instancia de IntegerProperty asociada
	 */
	public final IntegerProperty idPreguntaProperty() {
		return this.idPregunta;
	}

	/**
	 * Método getter del atributo idPregunta.
	 * @return int con el id de la pregunta asociada
	 */
	public final int getIdPregunta() {
		return this.idPreguntaProperty().get();
	}

	/**
	 * Método setter del atributo idPregunta.
	 * @param idPregunta nuevo valor del atributo idPregunta
	 */
	public final void setIdPregunta(final int idPregunta) {
		this.idPreguntaProperty().set(idPregunta);
	}

	/**
	 * Método getter del property asociado a pregunta.
	 * @return instancia de StringProperty asociada.
	 */
	public final StringProperty preguntaProperty() {
		return this.pregunta;
	}

	/**
	 * Método getter del atributo pregunta.
	 * @return string con el texto de la pregunta asociada.
	 */
	public final String getPregunta() {
		return this.preguntaProperty().get();
	}

	/**
	 * Método setter del atributo pregunta.
	 * @param pregunta nuevo valor del atributo pregunta
	 */
	public final void setPregunta(final String pregunta) {
		this.preguntaProperty().set(pregunta);
	}

	/**
	 * Método getter del property asociado a peso.
	 * @return instancia de IntegerProperty asociada.
	 */
	public final IntegerProperty pesoProperty() {
		return this.peso;
	}

	/**
	 * Método getter del atributo peso.
	 * @return int con el valor del peso asociado.
	 */
	public final int getPeso() {
		return this.pesoProperty().get();
	}

	/**
	 * Método setter del atributo peso.
	 * @param peso nuevo valor del atributo peso
	 */
	public final void setPeso(final int peso) {
		this.pesoProperty().set(peso);
	}

	/**
	 * Método getter del property asociado a correcta.
	 * @return instancia de BooleanProperty asociada.
	 */
	public final BooleanProperty correctaProperty() {
		return this.correcta;
	}

	/**
	 * Método getter del atributo correcta.
	 * @return boolean con el estado de la pregunta asociada.
	 */
	public final boolean isCorrecta() {
		return this.correctaProperty().get();
	}

	/**
	 * Método setter del atributo correcta.
	 * @param correcta nuevo valor del atributo correcta
	 */
	public final void setCorrecta(final boolean correcta) {
		this.correctaProperty().set(correcta);
	}

	/**
	 * Método getter del property asociado a creador.
	 * @return instancia de IntegerProperty asociada.
	 */
	public final IntegerProperty creadorProperty() {
		return this.creador;
	}

	/**
	 * Método getter del atributo creador.
	 * @return int con el identificador del creador asociado.
	 */
	public final int getCreador() {
		return this.creadorProperty().get();
	}

	/**
	 * Método setter del atributo creador.
	 * @param creador nuevo valor del atributo creador
	 */
	public final void setCreador(final int creador) {
		this.creadorProperty().set(creador);
	}

}
