package dad.proyectos.banana_test.utils;

import de.jensd.fx.glyphs.GlyphsDude;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIcon;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.text.Text;

public class DialogoConfirmar extends Dialogo<Boolean> {

	public DialogoConfirmar(String titulo, String contenido, String aceptar, String cancelar) {
		super(titulo, aceptar, cancelar);
		
		HBox root = new HBox();
		root.setSpacing(5);
		root.setAlignment(Pos.CENTER);
		
		Label lbContenido = new Label(contenido);
		lbContenido.setStyle("-fx-font-weight:bold;");
		// TODO: Cambiar color de icono
		Text warningIcon = GlyphsDude.createIcon(FontAwesomeIcon.WARNING, "120px");
		
		root.getChildren().addAll(lbContenido, warningIcon);
		
		getDialogPane().setContent(root);
		
		setResultConverter(dialogButton -> {
			if (dialogButton == btAceptar) {
				return true;
			}
			return null;
		});
	}

}
