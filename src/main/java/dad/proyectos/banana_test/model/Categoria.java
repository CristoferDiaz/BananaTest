package dad.proyectos.banana_test.model;

public class Categoria {

	private int id;
	private String nombre;
	private int creador;

	public Categoria(int id, String nombre, int creador) {
		this.id = id;
		this.nombre = nombre;
		this.creador = creador;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getNombre() {
		return nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	public int getCreador() {
		return creador;
	}

	public void setCreador(int creador) {
		this.creador = creador;
	}

}
