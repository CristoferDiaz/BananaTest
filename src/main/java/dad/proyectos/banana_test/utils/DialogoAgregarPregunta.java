package dad.proyectos.banana_test.utils;

import dad.proyectos.banana_test.model.Pregunta;
import dad.proyectos.banana_test.model.preguntas.PreguntaTestMultiple;
import dad.proyectos.banana_test.model.preguntas.PreguntaTestSimple;
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

public class DialogoAgregarPregunta extends Dialogo<Pregunta>{
	//Filtro Buscador
	TextField txBuscador = new TextField();
	
	//root
	VBox root = new VBox();
	
	//listaview de preguntas
	ListView<Pregunta> lista = new ListView<Pregunta>();
	ListProperty<Pregunta> listaProperty =  new SimpleListProperty<Pregunta>(FXCollections.observableArrayList());
	
	

	public DialogoAgregarPregunta(String titulo, String aceptar, String cancelar) {
		super(titulo, aceptar, cancelar);
		
		
		txBuscador.setPromptText("Buscador de preguntas...");
		
		
		//Contenedor
		GridPane grid = new GridPane();
		grid.add(txBuscador,0,0);
		grid.add(lista,0,1);
		grid.setVgap(8);
		grid.setHgap(10);
		
		root.setPadding(new Insets(10,10,10,10));
		root.setSpacing(5);
		root.setAlignment(Pos.CENTER);
		
		root.getChildren().addAll(grid);
		getDialogPane().setContent(root);
		
		
		//Preguntas
		Pregunta[] array_preguntas = {
				new PreguntaTestSimple("Texto de la primera pregunta",
						new String[] {
								"Respuesta válida 1",
								"Respuesta 2",
								"Respuesta 3",
								"Respuesta 4"
                }),
				new PreguntaTestMultiple("Texto de la tercera pregunta",
						new String[] {
								"Respuesta 1",
								"Respuesta 2",
								"Respuesta 3",
								"Respuesta 4"
	                    },
	                    new boolean[] {
	                            true,
	                            true,
	                            false,
	                            false
	                    }
	            ),
				new PreguntaTestMultiple("Texto de la cuarta pregunta",
	                    new String[] {
	                            "Respuesta 1",
	                            "Respuesta 2",
	                            "Respuesta 3",
	                            "Respuesta 4"
	                    },
	                    new boolean[] {
	                            true,
	                            true,
	                            false,
	                            false
	                    }
				),
				new PreguntaTestSimple("Texto de la quinta pregunta",
						new String[] {
								"Respuesta válida 1",
								"Respuesta 2",
								"Respuesta 3",
								"Respuesta 4"
                })
		};
		
		for(int i=0; i < array_preguntas.length ; i++) {
			listaProperty.add(array_preguntas[i]);
		}
		
		lista.setItems(listaProperty);
		crearFiltroBuscador();
		
		//MOSTRAR OBJETO COMO STRING EN UN LISTVIEW (LISTA DE PREGUNTAS)
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
	
	//FILTRADOR DE TEXTFIELD A UN LISTVIEW
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
