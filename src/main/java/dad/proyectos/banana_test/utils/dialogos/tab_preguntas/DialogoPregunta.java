package dad.proyectos.banana_test.utils.dialogos.tab_preguntas;

import dad.proyectos.banana_test.model.Pregunta;
import dad.proyectos.banana_test.model.preguntas.PreguntaTestMultiple;
import dad.proyectos.banana_test.model.preguntas.PreguntaTestSimple;
import dad.proyectos.banana_test.utils.dialogos.Dialogo;
import javafx.beans.value.ObservableValue;
import javafx.scene.Node;
import javafx.scene.control.ComboBox;
import javafx.scene.layout.BorderPane;

/**
 * Clase gestora de los diálogos de pregunta
 * desde los cuales se puede crear un nuevo
 * objeto de tipo Pregunta.
 * 
 * @author Crmprograming
 *
 */
public class DialogoPregunta extends Dialogo<Pregunta> {

	private Pregunta pregunta;
	
	/**
	 * Constructor principal de la clase.
	 */
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

	/**
	 * Método listener asociado al ComboBox del tipo de pregunta a crear.
	 * 
	 * @param o referencia al valor observable
	 * @param ov antigo valor observado
	 * @param nv nuevo valor observado
	 * @param root panel sobre el cual se incrustará el formulario editable de la pregunta.
	 */
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
