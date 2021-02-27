package dad.proyectos.banana_test.model;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.ListProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleListProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

/**
 * Clase gestora de un examen con sus correspondientes preguntas y demás datos
 * relevantes según el diagrama correspondiente.
 * 
 * Se contempla que un Examen pueda tener 0 o más preguntas.
 * 
 * Todo examen debe tener un id asociado que puede ser:
 * 
 * <ul>
 * <li>-1 Si es un nuevo examen que no existe todavía en la BD</li>
 * <li>Un número entero asociado a su identificador en la BD</li>
 * </ul>
 *
 * @author Crmprograming
 *
 */
public class Examen {

	private IntegerProperty idExamen = new SimpleIntegerProperty();
	private StringProperty nombre = new SimpleStringProperty();
	private StringProperty descripcion = new SimpleStringProperty();
	private ListProperty<Pregunta> preguntas = new SimpleListProperty<Pregunta>(FXCollections.observableArrayList());
	protected IntegerProperty creador = new SimpleIntegerProperty();

	/**
	 * Constructor por defecto de la clase. Delega la instanciación correcta al
	 * constructor extendido, inicializándolo con un identificador de -1.
	 * 
	 * @param tituloExamen     String con el título del examen
	 * @param textoDescripcion String con el texto cabecera del examen
	 * @param listaPreguntas   Conjunto de instancias de la clase Pregunta (puede
	 *                         ser vacío)
	 */
	public Examen(String tituloExamen, String textoDescripcion, Pregunta... listaPreguntas) {
		this(-1, tituloExamen, textoDescripcion, -1, listaPreguntas);
	}

	/**
	 * Constructor extendido de la clase. Instancia un objeto de tipo Examen
	 * especificando los valores indicados por parámetro.
	 * 
	 * @param identificador    Integer con el identificador asignado a dicho examen
	 * @param tituloExamen     String con el título del examen
	 * @param textoDescripcion String con el texto cabecera del examen
	 * @param idUsuario        Integer con el identificador del usuario creador de
	 *                         dicho examen
	 * @param listaPreguntas   Conjunto de instancias de la clase Pregunta (puede
	 *                         ser vacío)
	 */
	public Examen(int identificador, String tituloExamen, String textoDescripcion, int idUsuario, Pregunta... listaPreguntas) {
		idExamen.set(identificador);
		nombre.set(tituloExamen);
		descripcion.set(textoDescripcion);
		creador.set(idUsuario);
		if (listaPreguntas != null)
			preguntas.get().addAll(listaPreguntas);
	}

	/**
	 * Método getter del property asociado a idExamen.
	 * @return instancia de IntegerProperty asociada
	 */
	public final IntegerProperty idExamenProperty() {
		return this.idExamen;
	}

	/**
	 * Método getter del atributo idExamen.
	 * @return int con el id del examen asociado
	 */
	public final int getIdExamen() {
		return this.idExamenProperty().get();
	}

	/**
	 * Método setter del atributo idExamen.
	 * @param idExamen nuevo valor del atributo idExamen
	 */
	public final void setIdExamen(final int idExamen) {
		this.idExamenProperty().set(idExamen);
	}

	/**
	 * Método getter del property asociado a nombre.
	 * @return instancia de StringProperty asociada
	 */
	public final StringProperty nombreProperty() {
		return this.nombre;
	}

	/**
	 * Método getter del atributo nombre.
	 * @return String con el nombre del examen asociado
	 */
	public final String getNombre() {
		return this.nombreProperty().get();
	}

	/**
	 * Método setter del atributo nombre.
	 * @param nombre nuevo valor del atributo nombre
	 */
	public final void setNombre(final String nombre) {
		this.nombreProperty().set(nombre);
	}

	/**
	 * Método getter del property asociado a descripcion.
	 * @return instancia de StringProperty asociada
	 */
	public final StringProperty descripcionProperty() {
		return this.descripcion;
	}

	/**
	 * Método getter del atributo descripcion.
	 * @return String con la descripción del examen asociado
	 */
	public final String getDescripcion() {
		return this.descripcionProperty().get();
	}

	/**
	 * Método setter del atributo descripcion.
	 * @param descripcion nuevo valor del atributo descripcion
	 */
	public final void setDescripcion(final String descripcion) {
		this.descripcionProperty().set(descripcion);
	}

	/**
	 * Método getter del property asociado a creador.
	 * @return instancia de IntegerProperty asociada
	 */
	public final IntegerProperty creadorProperty() {
		return this.creador;
	}

	/**
	 * Método getter del atributo creador.
	 * @return int con el id del creador del examen asociado
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

	/**
	 * Método getter del property asociado a preguntas.
	 * @return instancia de ListProperty asociada
	 */
	public final ListProperty<Pregunta> preguntasProperty() {
		return this.preguntas;
	}

	/**
	 * Método getter del atributo preguntas.
	 * @return ObservableList con el listado de preguntas del examen asociado
	 */
	public final ObservableList<Pregunta> getPreguntas() {
		return this.preguntasProperty().get();
	}

	/**
	 * Método setter del atributo preguntas.
	 * @param preguntas nuevo valor del atributo preguntas
	 */
	public final void setPreguntas(final ObservableList<Pregunta> preguntas) {
		this.preguntasProperty().set(preguntas);
	}

}
