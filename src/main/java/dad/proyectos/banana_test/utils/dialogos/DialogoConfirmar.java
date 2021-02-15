package dad.proyectos.banana_test.utils.dialogos;

import org.kordamp.ikonli.javafx.FontIcon;

import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.effect.DropShadow;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;

public class DialogoConfirmar extends Dialogo<Boolean> {

	public DialogoConfirmar(String titulo, String contenido, String aceptar, String cancelar) {
		super(titulo, aceptar, cancelar);
		
		HBox root = new HBox();
		root.setSpacing(5);
		root.setAlignment(Pos.CENTER);
		
		Label lbContenido = new Label(contenido);
		lbContenido.setStyle("-fx-font-weight:bold;");
		FontIcon fontIcon = new FontIcon("fa-exclamation-triangle");
		fontIcon.setIconSize(120);
		DropShadow dropShadow = new DropShadow();
		dropShadow.setOffsetX(3.0f);
		dropShadow.setColor(Color.color(0.4f, 0.4f, 0.4f));
		fontIcon.setFill(Color.rgb(232, 178, 36));
		fontIcon.setEffect(dropShadow);
		
		root.getChildren().addAll(lbContenido, fontIcon);
		
		getDialogPane().setContent(root);
		
		setResultConverter(dialogButton -> {
			if (dialogButton == btAceptar) {
				return true;
			}
			return null;
		});
	}

}
