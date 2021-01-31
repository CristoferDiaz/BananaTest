package dad.proyectos.banana_test.controller;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import com.sun.javafx.fxml.FXMLLoaderHelper;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.Tab;
import javafx.scene.layout.BorderPane;

public class MainController implements Initializable {
	
	//model
	private PestanaExamController examController = new PestanaExamController();
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
		if(tbExamen == null)
			System.out.print("Nulo");
		
		tbExamen.setContent(examController.getView());
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
