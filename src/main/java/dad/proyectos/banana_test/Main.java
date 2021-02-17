package dad.proyectos.banana_test;

import dad.proyectos.banana_test.db.Intermedio;
import dad.proyectos.banana_test.model.preguntas.PreguntaTestMultiple;
import dad.proyectos.banana_test.model.preguntas.PreguntaTestSimple;

public class Main {

	public static void main(String[] args) {
		String[] respuestas = {"patata", "zanahoria", "legumbre", "lechuga"};
		boolean[] correctas = {true, false, true, false};
		String[] error = {"error"};
		//App.main(args);
		PreguntaTestSimple simple = new PreguntaTestSimple("Prueba", respuestas);
		PreguntaTestMultiple multiple = new PreguntaTestMultiple("Prueba", respuestas, correctas);
		Intermedio.crearPregunta(simple, error);
		Intermedio.crearPregunta(multiple, error);

	}

}
