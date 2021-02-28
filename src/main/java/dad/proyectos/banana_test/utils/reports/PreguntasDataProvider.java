package dad.proyectos.banana_test.utils.reports;

import java.util.ArrayList;
import java.util.List;

import dad.proyectos.banana_test.model.Pregunta;
import javafx.beans.property.StringProperty;
import javafx.collections.ObservableList;

/**
 * Clase gestora de formatear un listado de
 * objetos Pregunta del modelo de datos
 * a un listado de objetos PreguntaReport
 * requerido para la generación del pdf.
 * 
 * @author Jefferson Rodriguez Gomez//Thesouljeff
 *
 */
public class PreguntasDataProvider {

	/**
	 * Metodo encargado de setear las preguntas pedidas a la Base de Datos
	 * 
	 * @author Jefferson Rodriguez Gomez//Thesouljeff
	 * @param preguntas Listado de objetos Pregunta
	 * @return Listado de objetos PreguntaReport
	 */
	public static List<PreguntaReport> getPreguntas(ObservableList<Pregunta> preguntas) {

		List<PreguntaReport> pregunta = new ArrayList<>();
		for (int i = 0; i < preguntas.size(); i++) {

			PreguntaReport preguntaReport = new PreguntaReport();

			preguntaReport
					.setTexto(preguntas.get(i).getPregunta() + " (" + preguntas.get(i).getPeso() + " punto(s)) ");

			StringProperty[] respuestas = preguntas.get(i).obtenerRespuestas();

			preguntaReport.setRespuesta1(respuestas[0].get());
			preguntaReport.setRespuesta2(respuestas[1].get());
			preguntaReport.setRespuesta3(respuestas[2].get());
			preguntaReport.setRespuesta4(respuestas[3].get());

			pregunta.add(preguntaReport);
		}

		return pregunta;

	}

}
