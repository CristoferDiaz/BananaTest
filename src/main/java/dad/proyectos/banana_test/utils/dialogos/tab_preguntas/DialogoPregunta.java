package dad.proyectos.banana_test.utils.dialogos.tab_preguntas;

import dad.proyectos.banana_test.model.Pregunta;
import dad.proyectos.banana_test.model.preguntas.PreguntaTestMultiple;
import dad.proyectos.banana_test.model.preguntas.PreguntaTestSimple;
import dad.proyectos.banana_test.utils.dialogos.Dialogo;
import javafx.beans.value.ObservableValue;
import javafx.scene.Node;
import javafx.scene.control.ComboBox;
import javafx.scene.layout.BorderPane;

public class DialogoPregunta extends Dialogo<Pregunta> {

	private Pregunta pregunta;
	
	public DialogoPregunta() {
		super("Crear nueva pregunta", "Crear Pregunta", "Cancelar");
		
		setResizable(true);
		getDialogPane().setPrefHeight(500);
		
		BorderPane root = new BorderPane();
		ComboBox<String> comboBox = new ComboBox<String>();		
		
		comboBox.setPromptText("Seleccione el tipo de pregunta");
		comboBox.getItems().addAll(
				"Pregunta Test Simple",
				"Pregunta Test Múltiple"
		);
		
		comboBox.getSelectionModel().selectedIndexProperty().addListener((o, ov, nv) -> onActualizarPregunta(o, ov, nv, root));
		root.setTop(comboBox);
		getDialogPane().setContent(root);
		
		Node nodeBtAceptar = getDialogPane().lookupButton(btAceptar);
		nodeBtAceptar.setDisable(true);
		
		nodeBtAceptar.disableProperty().bind(comboBox.getSelectionModel().selectedItemProperty().isNull());
		
		setResultConverter(dialogButton -> {
			if (dialogButton == btAceptar) {
				return pregunta;
			}
			return null;
		});
	}

	private void onActualizarPregunta(ObservableValue<? extends Number> o, Number ov, Number nv, BorderPane root) {
		if (ov.intValue() != -1) {
			root.setCenter(null);
		}
		
		if (nv.intValue() != -1) {
			if (nv.intValue() == Pregunta.TIPO_PREGUNTA.TEST_RESPUESTA_SIMPLE.ordinal()) {
				pregunta = new PreguntaTestSimple(
						"Texto de la pregunta",
						new String[] {
								"Respuesta válida",
								"Respuesta 2",
								"Respuesta 3",
								"Respuesta 4"
						}
				);
			} else if (nv.intValue() == Pregunta.TIPO_PREGUNTA.TEST_RESPUESTA_MULTIPLE.ordinal()) {
				pregunta = new PreguntaTestMultiple(
						"Texto de la pregunta",
						new String[] {
								"Texto Respuesta",
								"Texto Respuesta",
								"Texto Respuesta",
								"Texto Respuesta"
						},
						new boolean[] {
								false,
								false,
								false,
								false
						}
				);
			}
			root.setCenter(pregunta.construirFormularioEditable());
		}
	}

}
