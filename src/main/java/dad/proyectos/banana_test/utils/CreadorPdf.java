package dad.proyectos.banana_test.utils;

import java.io.File;
import java.io.FileOutputStream;

import com.itextpdf.text.Document;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.PdfWriter;

import dad.proyectos.banana_test.model.Examen;
import dad.proyectos.banana_test.model.Pregunta;
import javafx.beans.property.StringProperty;

public abstract class CreadorPdf {

	public static boolean generarPDF(Examen examen, File fichero, String[] error) {

		try {
			Document document = new Document();
			// Instanciacion

			PdfWriter.getInstance(document, new FileOutputStream(fichero));

			document.open();
			
			document.newPage();
	        
	        Paragraph paragraph = new Paragraph();

			// examen.descripcion
			Paragraph encabezado = new Paragraph(examen.getDescripcion());
			paragraph.add(encabezado);

			// Espacios, fecha y puntuacion
			Paragraph Fecha = new Paragraph("Fecha de realizacion");
			Paragraph Puntuacion = new Paragraph("Puntuacion conseguida");
			paragraph.add(Fecha);
			paragraph.add(Puntuacion);

			for (Pregunta pregunta : examen.getPreguntas()) {

				if (pregunta.getTipoPregunta() == Pregunta.TIPO_PREGUNTA.TEST_RESPUESTA_SIMPLE) {
					// Imprimir pregunta de tipo simple

					Paragraph Simple = new Paragraph(pregunta.getPregunta());
					paragraph.add(Simple);

					for (StringProperty respuesta : pregunta.obtenerRespuestas()) {

						Paragraph opciones = new Paragraph(respuesta.get());
						paragraph.add(opciones);
					}
				}

				else {
					// Imprimir pregunta tipo multiple
					// TODO formato de posibles respuestas: añadir checkbox

					if (pregunta.getTipoPregunta() == Pregunta.TIPO_PREGUNTA.TEST_RESPUESTA_MULTIPLE) {
						// Imprimir pregunta de tipo simple

						Paragraph Simple = new Paragraph(pregunta.getPregunta());
						paragraph.add(Simple);

						for (StringProperty respuesta : pregunta.obtenerRespuestas()) {

							Paragraph opciones = new Paragraph(respuesta.get());
							paragraph.add(opciones);

						}
					}

				}

				// Cosas

				// Toy chiquito, hay un error
			}
			
			document.add(paragraph);
			document.close();

		} catch (Exception e) {
			error[0] = e.getLocalizedMessage();
		}
		return true;
	}
}
