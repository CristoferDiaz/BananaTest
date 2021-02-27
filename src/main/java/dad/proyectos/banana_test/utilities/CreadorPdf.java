package dad.proyectos.banana_test.utilities;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import dad.proyectos.banana_test.model.Examen;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;


/**
 * 
 * Clase encargada de crear el pdf mediante JasperReports
 *
 */
public class CreadorPdf {
	
	
	public static final String JRXML_FILE = "/reports/plantillaexamen.jrxml";
    
	/**
	 * Metodo encargado de compilar, generar los parametros y finalmente imprimer el parametro
	 * @author Jefferson Rodriguez Gomez//Thesouljeff
	 * @param El objeto examen y una linea de texto fichero
	 * @throws JRException y IOException en caso de fallo en la entrada o salida de datos
	 */
	
    public static void generarPdf(Examen examen, String fichero) throws JRException, IOException {

    	
        // compila el informe
        JasperReport report = JasperCompileManager.compileReport(CreadorPdf.class.getResourceAsStream(JRXML_FILE));

        // mapa de parámetros para el informe
        Map<String, Object> parameters = new HashMap<String, Object>();
        parameters.put("descripcion", examen.getDescripcion()); 
        
        
        //generamos el informe (combinamos el informe compilado con los datos) 
        JasperPrint print  = JasperFillManager.fillReport(report, parameters, new JRBeanCollectionDataSource(PreguntasDataProvider.getPreguntas(examen.getPreguntas())));

        // exporta el informe a un fichero PDF
        JasperExportManager.exportReportToPdfFile(print, fichero);
        
    }
		
			
		
		
	}
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	/*
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
	
	*/


		
		
	

	

		
		
		