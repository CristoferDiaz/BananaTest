package dad.proyectos.banana_test.utilities;

import java.io.File;
import java.io.FileOutputStream;
import java.util.Collections;

import com.itextpdf.text.Document;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.PdfWriter;

import dad.proyectos.banana_test.model.Examen;
import dad.proyectos.banana_test.model.Pregunta;
import javafx.beans.property.StringProperty;

public class CreadorPdf {
	
	
	
	public static boolean generarPDF (Examen examen, File fichero, Pregunta pregunta , String [] error ) {
		
		
		try {
			
		Document prueba = new Document ();
		
		//Instanciacion 
		//Añadir ruta y nombre de archivo
		String nombreArchivo= "";
		PdfWriter.getInstance (prueba, new FileOutputStream(nombreArchivo));
		
		prueba.open();
		
											//examen.descripcion
		Paragraph encabezado = new Paragraph(examen.getDescripcion());
		prueba.add(encabezado);
		
		//Espacios, fecha y puntuacion
		Paragraph Fecha = new Paragraph ("Fecha de realizacion");
		Paragraph Puntuacion = new Paragraph ("Puntuacion conseguida");
		prueba.add(Fecha);
		prueba.add(Puntuacion);
		
		
		for (Pregunta preguntaN : examen.getPreguntas()) {
		
		
		if (pregunta.getTipoPregunta() == Pregunta.TIPO_PREGUNTA.TEST_RESPUESTA_SIMPLE ) {
			//Imprimir pregunta de tipo simple
			
			
			Paragraph Simple = new Paragraph (preguntaN.getPregunta());
			prueba.add(Simple);
			
			
			for (StringProperty respuesta : preguntaN.obtenerRespuestas()) {
				
				
				Paragraph opciones = new Paragraph (respuesta.get());
				prueba.add(opciones);
			}
		}
		
		else {
			//Imprimir pregunta tipo multiple
			//TODO formato de posibles respuestas: añadir checkbox
			
			if (pregunta.getTipoPregunta() == Pregunta.TIPO_PREGUNTA.TEST_RESPUESTA_MULTIPLE ) {
				//Imprimir pregunta de tipo simple
				
				
				Paragraph Simple = new Paragraph (preguntaN.getPregunta());
				prueba.add(Simple);
				
				
				for (StringProperty respuesta : preguntaN.obtenerRespuestas()) {
					
					
					Paragraph opciones = new Paragraph (respuesta.get());
					prueba.add(opciones);
	
					
				}
		}
		
	}
		
		prueba.close();
		
		
		//Cosas
		
		
		
		//Toy chiquito, hay un error
		}
		
	}
		
		
		catch (Exception e) {
			error[0] = e.getLocalizedMessage();
			
		}
		return true;
	
	


		
		
	}
}
	

		
		
		