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
 * Clase gestora de un examen con sus correspondientes preguntas y demás
 * datos relevantes según el diagrama correspondiente.
 * 
 * Se contempla que un Examen pueda tener 0 o más preguntas.
 * 
 * Todo examen debe tener un id asociado que puede ser:
 * 
 * <ul>
 * 	<li>-1 Si es un nuevo examen que no existe todavía en la BD</li>
 * <li> Un número entero asociado a su identificador en la BD</li>
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
	
	/**
	 * Constructor por defecto de la clase. Delega la instanciación correcta al constructor extendido,
	 * inicializándolo con un identificador de -1.
	 * 
	 * @param tituloExamen String con el título del examen
	 * @param textoDescripcion String con el texto cabecera del examen
	 * @param listaPreguntas Conjunto de instancias de la clase Pregunta (puede ser vacío)
	 */
	public Examen(String tituloExamen, String textoDescripcion, Pregunta ... listaPreguntas) {
		this(-1, tituloExamen, textoDescripcion, listaPreguntas);
	}
	
	/**
	 * Constructor extendido de la clase. Instancia un objeto de tipo Examen especificando los
	 * valores indicados por parámetro.
	 * 
	 * @param identificador Integer con el identificador asignado a dicho examen
	 * @param tituloExamen String con el título del examen
	 * @param textoDescripcion String con el texto cabecera del examen
	 * @param listaPreguntas Conjunto de instancias de la clase Pregunta (puede ser vacío)
	 */
	public Examen(int identificador, String tituloExamen, String textoDescripcion, Pregunta ... listaPreguntas) {
		idExamen.set(identificador);
		nombre.set(tituloExamen);
		descripcion.set(textoDescripcion);
		preguntas.get().addAll(listaPreguntas);
	}

	public final IntegerProperty idExamenProperty() {
		return this.idExamen;
	}

	public final int getIdExamen() {
		return this.idExamenProperty().get();
	}

	public final void setIdExamen(final int idExamen) {
		this.idExamenProperty().set(idExamen);
	}

	public final StringProperty nombreProperty() {
		return this.nombre;
	}

	public final String getNombre() {
		return this.nombreProperty().get();
	}

	public final void setNombre(final String nombre) {
		this.nombreProperty().set(nombre);
	}

	public final StringProperty descripcionProperty() {
		return this.descripcion;
	}

	public final String getDescripcion() {
		return this.descripcionProperty().get();
	}

	public final void setDescripcion(final String descripcion) {
		this.descripcionProperty().set(descripcion);
	}

	public final ListProperty<Pregunta> preguntasProperty() {
		return this.preguntas;
	}

	public final ObservableList<Pregunta> getPreguntas() {
		return this.preguntasProperty().get();
	}

	public final void setPreguntas(final ObservableList<Pregunta> preguntas) {
		this.preguntasProperty().set(preguntas);
	}

}
