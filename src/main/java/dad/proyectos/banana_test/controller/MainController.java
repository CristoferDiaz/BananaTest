package dad.proyectos.banana_test.controller;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import dad.proyectos.banana_test.App;
import dad.proyectos.banana_test.model.Examen;
import dad.proyectos.banana_test.utils.CreadorPdf;
import dad.proyectos.banana_test.utils.Preferencias;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Tab;
import javafx.scene.image.Image;
import javafx.scene.layout.BorderPane;
import javafx.stage.FileChooser;
import javafx.stage.FileChooser.ExtensionFilter;
import javafx.stage.Modality;
import javafx.stage.Stage;

/**
 * Clase gestora del controlador principal de la aplicación.
 * 
 * @author Crmprograming
 *
 */
public class MainController implements Initializable {

	// model
	private TabExamenesController tabExamenesController;
	
	// view
	@FXML
	private BorderPane view;

	@FXML
	private Tab tbExamen, tbPregun;

	/**
	 * Constructor por defecto de la clase.
	 * 
	 * @throws IOException si se produce un error al cargar la plantilla de la vista
	 */
	public MainController() throws IOException {
		FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/MainView.fxml"), App.resourceBundle);
		loader.setController(this);
		loader.load();
	}

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		try {
			tabExamenesController = new TabExamenesController();
			tbExamen.setContent(tabExamenesController.getView());
			tbPregun.setContent(new TabPreguntasController().getView());
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Método asociado al evento del menú con la
	 * finalidad de exportar el examen seleccionado
	 * a un fichero PDF.
	 * 
	 * @param event Instancia de ActionEvent asociada al evento disparador
	 */
	@FXML
	void onExportarPDFAction(ActionEvent event) {
		// TODO: Comprobar que algún examen esté seleccionado
		FileChooser fileChooser = new FileChooser();
		fileChooser.setTitle("Guardar examen en un pdf");
		fileChooser.getExtensionFilters().add(new ExtensionFilter("PDF (*.pdf)", "*.pdf"));
    	fileChooser.getExtensionFilters().add(new ExtensionFilter("Todos los archivos (*.*)", "*.*"));
    	File file = fileChooser.showSaveDialog(App.primaryStage);
    	
    	if (file != null) {
    		Examen examen = tabExamenesController.getExamenSeleccionado();
    		
    		String[] error = new String[] {""};
    		CreadorPdf.generarPDF(examen, file, error);
    		// TODO: Mostrar diálogo de confirmación
    		System.out.println(error[0]);
    	}
    }

	/**
	 * Método asociado al evento del menú con la
	 * finalidad de mostrar la ventana de preferencias.
	 * 
	 * @param event Instancia de ActionEvent asociada al evento disparador
	 */
	@FXML
	void onEditarPreferenciasAction(ActionEvent event) {
		try {
			Stage stage = new Stage();
			PreferenciasController preferenciasController = new PreferenciasController(stage);
			Scene escena = new Scene(preferenciasController.getView());
			stage.setScene(escena);
			stage.setTitle("Editar Preferencias - BananaTest");
			stage.getIcons().add(new Image("/images/logo/bananatest_logo_16.png"));
			stage.getIcons().add(new Image("/images/logo/bananatest_logo_32.png"));
			stage.getIcons().add(new Image("/images/logo/bananatest_logo_64.png"));
			stage.initModality(Modality.APPLICATION_MODAL);

			if (Preferencias.getTema() != Preferencias.TEMAS.DEFAULT)
				escena.getStylesheets().add(Preferencias.cargarTema());

			stage.showAndWait();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Método asociado al evento del menú con la
	 * finalidad de cerrar la aplicación.
	 * 
	 * @param event Instancia de ActionEvent asociada al evento disparador
	 */
	@FXML
	void onSalirAction(ActionEvent event) {
		Platform.exit();
	}
	
	/**
	 * Método asociado al evento del menú con la
	 * finalidad de mostrar la ventana de acerca de.
	 * 
	 * @param event Instancia de ActionEvent asociada al evento disparador
	 */
	@FXML
    void onAcercaDeAction(ActionEvent event) {
		try {
			Stage stage = new Stage();
			Scene escena = new Scene(new AcercaDeController().getView());
			stage.setScene(escena);
			stage.setTitle("Acerca de - BananaTest");
			stage.getIcons().add(new Image("/images/logo/bananatest_logo_16.png"));
			stage.getIcons().add(new Image("/images/logo/bananatest_logo_32.png"));
			stage.getIcons().add(new Image("/images/logo/bananatest_logo_64.png"));
			stage.initModality(Modality.APPLICATION_MODAL);

			if (Preferencias.getTema() != Preferencias.TEMAS.DEFAULT)
				escena.getStylesheets().add(Preferencias.cargarTema());

			stage.showAndWait();
		} catch (IOException e) {
			e.printStackTrace();
		}
    }
	
	/**
	 * Método asociado al evento del menú con la
	 * finalidad de mostrar la página del proyecto
	 * 
	 * @param event Instancia de ActionEvent asociada al evento disparador
	 */
	@FXML
    void onVisitarWebAction(ActionEvent event) {
		App.hostServices.showDocument("https://github.com/dam-dad/BananaTest");
    }
	
	/**
	 * Método asociado al evento del menú con la
	 * finalidad de mostrar la página de la wiki.
	 * 
	 * @param event Instancia de ActionEvent asociada al evento disparador
	 */
	@FXML
    void onVerAyudaAction(ActionEvent event) {
		App.hostServices.showDocument("https://github.com/dam-dad/BananaTest/wiki");
    }

	/**
	 * Método getter del atributo view.
	 * 
	 * @return Instancia del BorderPane asociado
	 */
	public BorderPane getView() {
		return view;
	}

	/**
	 * Método setter del atributo view.
	 * 
	 * @param view Instancia del BorderPane asociado
	 */
	public void setView(BorderPane view) {
		this.view = view;
	}

}
