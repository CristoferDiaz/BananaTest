package dad.proyectos.banana_test.utils;

import javafx.scene.control.ButtonType;
import javafx.scene.control.Dialog;
import javafx.scene.control.ButtonBar.ButtonData;
import javafx.scene.image.Image;
import javafx.stage.Stage;

public abstract class Dialogo<T> extends Dialog<T> {
	
	protected ButtonType btAceptar;
	protected ButtonType btCancelar;

	public Dialogo(String titulo, String aceptar, String cancelar) {
		setTitle(titulo);
		
		// Cargamos el tema css escogido por el usuario
		if (Preferencias.getTema() != Preferencias.TEMAS.DEFAULT)
			getDialogPane().getStylesheets().add(Preferencias.cargarTema());
		
		Stage stage = (Stage) getDialogPane().getScene().getWindow();
		stage.getIcons().add(new Image("/images/logo/bananatest_logo_16.png"));
		stage.getIcons().add(new Image("/images/logo/bananatest_logo_32.png"));
		stage.getIcons().add(new Image("/images/logo/bananatest_logo_64.png"));
		stage.setMinWidth(550);
		stage.setMinHeight(200);
		
		btAceptar = new ButtonType(aceptar, ButtonData.OK_DONE);
		btCancelar = new ButtonType(cancelar, ButtonData.CANCEL_CLOSE);
		getDialogPane().getButtonTypes().addAll(btAceptar, btCancelar);
	}

}
