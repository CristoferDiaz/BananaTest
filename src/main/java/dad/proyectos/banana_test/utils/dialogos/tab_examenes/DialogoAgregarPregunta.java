package dad.proyectos.banana_test.utils.dialogos.tab_examenes;

import dad.proyectos.banana_test.model.Pregunta;
import dad.proyectos.banana_test.model.preguntas.PreguntaTestMultiple;
import dad.proyectos.banana_test.model.preguntas.PreguntaTestSimple;
import dad.proyectos.banana_test.utils.dialogos.Dialogo;
import javafx.beans.property.ListProperty;
import javafx.beans.property.SimpleListProperty;
import javafx.collections.FXCollections;
import javafx.collections.transformation.FilteredList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;

public class DialogoAgregarPregunta extends Dialogo<Pregunta> {
	// Filtro Buscador
	private TextField txBuscador = new TextField();

	// root
	private VBox root = new VBox();

	// listaview de preguntas
	private ListView<Pregunta> lista = new ListView<Pregunta>();
	private ListProperty<Pregunta> listaProperty = new SimpleListProperty<Pregunta>(FXCollections.observableArrayList());
	
	/**
	 * Constructor principal de la clase.
	 * Crea un diálogo que contiene una lista con todas las preguntas disponibles de la base de datos,
	 * de las cuales podemos elegir una para añadir al examen seleccionado
	 * 
	 * @param titulo String que contiene el título de la ventana del diálogo
	 * @param aceptar String que contiene el título del botón de tipo OK_DONE
	 * @param cancelar String que contiene el título del botón de tipo CANCEL_CLOSE
	 */
	public DialogoAgregarPregunta(String titulo, String aceptar, String cancelar) {
		super(titulo, aceptar, cancelar);

		txBuscador.setPromptText("Buscador de preguntas...");

		// Contenedor
		GridPane grid = new GridPane();
		grid.add(txBuscador, 0, 0);
		grid.add(lista, 0, 1);
		grid.setVgap(8);
		grid.setHgap(10);

		root.setPadding(new Insets(10, 10, 10, 10));
		root.setSpacing(5);
		root.setAlignment(Pos.CENTER);

		root.getChildren().addAll(grid);
		getDialogPane().setContent(root);

		// Preguntas
		// TODO: Cargar preguntas de la BD
		Pregunta[] array_preguntas = {
				new PreguntaTestSimple("Texto de la primera pregunta",
						new String[] { "Respuesta válida 1", "Respuesta 2", "Respuesta 3", "Respuesta 4" }),
				new PreguntaTestMultiple("Texto de la tercera pregunta",
						new String[] { "Respuesta 1", "Respuesta 2", "Respuesta 3", "Respuesta 4" },
						new boolean[] { true, true, false, false }),
				new PreguntaTestMultiple("Texto de la cuarta pregunta",
						new String[] { "Respuesta 1", "Respuesta 2", "Respuesta 3", "Respuesta 4" },
						new boolean[] { true, true, false, false }),
				new PreguntaTestSimple("Texto de la quinta pregunta",
						new String[] { "Respuesta válida 1", "Respuesta 2", "Respuesta 3", "Respuesta 4" }) };

		for (int i = 0; i < array_preguntas.length; i++) {
			listaProperty.add(array_preguntas[i]);
		}

		lista.setItems(listaProperty);
		crearFiltroBuscador();

		// MOSTRAR OBJETO COMO STRING EN UN LISTVIEW (LISTA DE PREGUNTAS)
		lista.setCellFactory(v -> new ListCell<Pregunta>() {
			@Override
			protected void updateItem(Pregunta item, boolean empty) {
				super.updateItem(item, empty);

				if (empty || item == null || item.getPregunta() == null) {
					setText(null);
				} else
					setText(item.getPregunta());
			}
		});

		setResultConverter(dialogButton -> {
			if (dialogButton == btAceptar) {
				Pregunta preguntaSeleccionada = lista.getSelectionModel().getSelectedItem();
				return preguntaSeleccionada;
			}
			return null;
		});

	}

	/**
	 * Método encargado de la creación de un filtro de texto para los buscadores de la aplicación.
	 */
	private void crearFiltroBuscador() {
		FilteredList<Pregunta> filteredData = new FilteredList<Pregunta>(listaProperty, s -> true);
		txBuscador.textProperty().addListener((o, ov, nv) -> {
			String filtro = txBuscador.getText();

			if (filtro == null || filtro.length() == 0) {
				filteredData.setPredicate(s -> true);
			} else {
				filteredData.setPredicate(s -> s.getPregunta().contains(filtro));
			}

		});

		lista.setItems(filteredData);
	}

}
