package dad.proyectos.banana_test.utils;

import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.util.Pair;

public class DialogoCrear extends Dialogo<Pair<String, String>>{

	@SuppressWarnings("static-access")
	public DialogoCrear(String titulo, String aceptar, String cancelar) {
		super(titulo, aceptar, cancelar);
		
		//campos
		Label lbNombre = new Label("Nombre del examen: ");
		Label lbDescripcion = new Label("Descripcion: ");
		TextField tfNombre = new TextField();
		TextField tfDescripcion = new TextField();
		
		//root
		VBox root = new VBox();
		
		//contenedor
		GridPane grid = new GridPane();
		grid.setConstraints(lbNombre,0,0);
		grid.setConstraints(tfNombre,1,0);
		grid.setConstraints(lbDescripcion,0,1);
		grid.setConstraints(tfDescripcion,1,1);
		grid.setVgap(8);
		grid.setHgap(10);
		
		
		root.setPadding(new Insets(10,10,10,10));
		root.setSpacing(5);
		root.setAlignment(Pos.CENTER);
		
		grid.getChildren().addAll(lbNombre, tfNombre, lbDescripcion, tfDescripcion);
		root.getChildren().addAll(grid);
		getDialogPane().setContent(root);
		
		//Focus en el nombre del examen
		Platform.runLater(() -> tfNombre.requestFocus());
		
		
		setResultConverter(dialogButton -> {
			if (dialogButton == btAceptar) {
				Pair<String, String> pair = new Pair<String, String>(tfNombre.getText(), tfDescripcion.getText());
				return pair;
			}
			return null;
		});
		
		
		
	}

}
