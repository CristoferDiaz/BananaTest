package dad.proyectos.banana_test.utils.dialogos.tab_examenes;

import dad.proyectos.banana_test.utils.dialogos.Dialogo;
import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.util.Pair;

/**
 * 
 * @author Daniel Pérez Pimienta
 * 
 * Crea un diálogo que permite crear un examen, añadiendo su nombre y descripción
 * y luego añadiendolo a la lista de examenes.
 *
 */
public class DialogoCrearExamen extends Dialogo<Pair<String, String>>{
	
	/**
	 * Constructor principal de la clase.
	 * 
	 * @param titulo String que contiene el título de la ventana del diálogo
	 * @param aceptar String que contiene el título del botón de tipo OK_DONE
	 * @param cancelar String que contiene el título del botón de tipo CANCEL_CLOSE
	 */
	public DialogoCrearExamen(String titulo, String aceptar, String cancelar) {
		super(titulo, aceptar, cancelar);

		// campos
		Label lbNombre = new Label("Nombre del examen: ");
		Label lbDescripcion = new Label("Descripcion: ");
		TextField tfNombre = new TextField();
		TextField tfDescripcion = new TextField();

		// root
		VBox root = new VBox();

		// contenedor
		GridPane grid = new GridPane();
		grid.add(lbNombre, 0, 0);
		grid.add(tfNombre, 1, 0);
		grid.add(lbDescripcion, 0, 1);
		grid.add(tfDescripcion, 1, 1);
		grid.setVgap(8);
		grid.setHgap(10);

		root.setPadding(new Insets(10, 10, 10, 10));
		root.setSpacing(5);
		root.setAlignment(Pos.CENTER);
		root.getChildren().addAll(grid);
		getDialogPane().setContent(root);

		// Focus en el nombre del examen
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
