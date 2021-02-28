package dad.proyectos.banana_test.utils.reports;

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
 * Clase encargada de crear el pdf mediante JasperReports
 * 
 * @author Jefferson Rodriguez Gomez//Thesouljeff
 *
 */
public class CreadorPdf {

	public static final String JRXML_FILE = "/reports/plantillaexamen.jrxml";

	/**
	 * Metodo encargado de compilar, generar los parametros y finalmente imprimer el
	 * parametro.
	 * 
	 * @param examen El objeto examen
	 * @param fichero String con la ruta al fichero destino
	 * @throws JRException en caso de fallo en la entrada o salida de
	 *                     datos
	 * @throws IOException en caso de fallo en la entrada o salida de
	 *                     datos
	 */
	public static void generarPdf(Examen examen, String fichero) throws JRException, IOException {

		// compila el informe
		JasperReport report = JasperCompileManager.compileReport(CreadorPdf.class.getResourceAsStream(JRXML_FILE));

		// mapa de parámetros para el informe
		Map<String, Object> parameters = new HashMap<String, Object>();
		parameters.put("descripcion", examen.getDescripcion());

		// generamos el informe (combinamos el informe compilado con los datos)
		JasperPrint print = JasperFillManager.fillReport(report, parameters,
				new JRBeanCollectionDataSource(PreguntasDataProvider.getPreguntas(examen.getPreguntas())));

		// exporta el informe a un fichero PDF
		JasperExportManager.exportReportToPdfFile(print, fichero);

	}

}
