package dad.proyectos.banana_test.utils.dialogos;

import de.jensd.fx.glyphs.GlyphsDude;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIcon;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.effect.DropShadow;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;

public class DialogoConfirmar extends Dialogo<Boolean> {

	public DialogoConfirmar(String titulo, String contenido, String aceptar, String cancelar) {
		super(titulo, aceptar, cancelar);
		
		HBox root = new HBox();
		root.setSpacing(5);
		root.setAlignment(Pos.CENTER);
		
		Label lbContenido = new Label(contenido);
		lbContenido.setStyle("-fx-font-weight:bold;");
		Text warningIcon = GlyphsDude.createIcon(FontAwesomeIcon.WARNING, "120px");
		DropShadow dropShadow = new DropShadow();
		dropShadow.setOffsetX(3.0f);
		dropShadow.setColor(Color.color(0.4f, 0.4f, 0.4f));
		warningIcon.setFill(Color.rgb(232, 178, 36));
		warningIcon.setEffect(dropShadow);
		
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
