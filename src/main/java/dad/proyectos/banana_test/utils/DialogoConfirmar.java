package dad.proyectos.banana_test.utils;

import javafx.scene.control.Label;
import javafx.scene.layout.HBox;

public class DialogoConfirmar extends Dialogo<Boolean> {

	public DialogoConfirmar(String titulo, String contenido, String aceptar, String cancelar) {
		super(titulo, aceptar, cancelar);
		
		HBox root = new HBox();
		Label lbContenido = new Label(contenido);
		lbContenido.setStyle("-fx-font-weight:bold;");
		
		root.getChildren().add(lbContenido);
						
		getDialogPane().setContent(root);
		
		setResultConverter(dialogButton -> {
			if (dialogButton == btAceptar) {
				return true;
			}
			return null;
		});
	}

}
