package dad.proyectos.banana_test.controller;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import dad.proyectos.banana_test.App;
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

public class MainController implements Initializable {
	
	// view
	@FXML
	private BorderPane view;

	@FXML
	private Tab tbExamen, tbPregun;

	public MainController() throws IOException {
		FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/MainView.fxml"));
		loader.setController(this);
		loader.load();
	}

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		// TODO Añadir paneles
		try {
			tbPregun.setContent(new TabPreguntasController().getView());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}
	
	@FXML
    void onExportarPDFAction(ActionEvent event) {
		FileChooser fileChooser = new FileChooser();
		fileChooser.setTitle("Guardar examen en un pdf");
		fileChooser.getExtensionFilters().add(new ExtensionFilter("PDF (*.pdf)", "*.pdf"));
    	fileChooser.getExtensionFilters().add(new ExtensionFilter("Todos los archivos (*.*)", "*.*"));
    	File file = fileChooser.showSaveDialog(App.primaryStage);
    	
    	if (file != null) {
    		// TODO: Mandar el fichero al exportador de PDF
    	}
    	
    }

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
			// TODO Auto-generated catch block
			e.printStackTrace();
		}		
	}

	@FXML
	void onSalirAction(ActionEvent event) {
		Platform.exit();
	}

	public BorderPane getView() {
		return view;
	}

	public void setView(BorderPane view) {
		this.view = view;
	}

}
