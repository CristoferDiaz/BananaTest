package dad.proyectos.banana_test.db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Scanner;

import dad.proyectos.banana_test.model.Categoria;
import dad.proyectos.banana_test.model.Examen;
import dad.proyectos.banana_test.model.Pregunta;
import dad.proyectos.banana_test.model.Pregunta.TIPO_PREGUNTA;
import dad.proyectos.banana_test.model.preguntas.PreguntaTestMultiple;
import dad.proyectos.banana_test.model.preguntas.PreguntaTestSimple;
import javafx.beans.property.StringProperty;

public abstract class Intermedio {

	/**
	 * Clase abstracta intermedia entre el proyecto y la base de datos donde se
	 * haran operaciones basicas como crear, eliminar, modificar y visualizar tanto
	 * la tabla bt_examenes como la tabla bt_preguntas
	 * 
	 * 
	 * 
	 * @author SamirElKharrat
	 */

	static Connection conexion = null;
	static Scanner sc = new Scanner(System.in);
	public static String driver = "com.mysql.cj.jdbc.Driver";

	// Funcion para conectarse a la base de datos que estamos usando
	public static Connection conectarmysql() {

		try {
			Class.forName(driver);
			conexion = DriverManager.getConnection("jdbc:mysql://localhost:3306/bananatest", "root", "");
		} catch (Exception e) {

		}
		return conexion;

	}

	// Preguntas

	/**
	 * Funcion con la que poder visualizar todas las preguntas de la tabla
	 * bt_preguntas
	 * 
	 * @param resultado Boolean para comprobar si se pudo hacer o no la operación
	 */
	public static ArrayList<Pregunta> visualizarPreguntas(String[] error) {
		Connection con = conectarmysql();
		ArrayList<Pregunta> pre = new ArrayList<Pregunta>();

		boolean resultado = false;
		String tipoSimple = "SIMP";
		String tipoMultiple = "MULT";
		String tipo, contenido, descripcion;
		boolean valida;
		int id;
		try {
			Statement stmt = con.createStatement();
			PreparedStatement stmt2;
			ResultSet rs = stmt.executeQuery("SELECT id, tipoPregunta, contenido FROM bt_preguntas");

			while (rs.next()) {
				id = rs.getInt("id");
				tipo = rs.getString("tipoPregunta");
				contenido = rs.getString("contenido");

				stmt2 = con.prepareStatement(
						"SELECT id, descripcion, valida, idPregunta FROM" + "bt_respuestas WHERE idPregunta = ?");
				stmt2.setInt(1, id);
				rs.getInt("id");
				descripcion = rs.getString("descripcion");
				valida = rs.getBoolean("valida");
				String[] respuestas = new String[4];
				if (valida == true) {
					respuestas[0] = descripcion;
				} else {
					respuestas[1] = descripcion;
					respuestas[2] = descripcion;
					respuestas[3] = descripcion;
				}

				if (tipo == tipoSimple) {
					PreguntaTestSimple preguntaSimple = new PreguntaTestSimple(contenido, respuestas);
					preguntaSimple.setIdPregunta(id);
					pre.add(preguntaSimple);
				}

			}

			resultado = true;
			con.close();
			rs.close();

		} catch (SQLException e) {
			error[0] = e.getLocalizedMessage();
		}

		return pre;
	}

	/**
	 * Funcion con la que poder crear preguntas
	 * 
	 * @param pregunta     Objeto de la clase Pregunta
	 * @param error        Array encargada de la gestion de los errores o
	 *                     excepciones
	 * @param tipoSimple   String que contiene el id de bt_tipopregunta para las
	 *                     PreguntasTestSimple
	 * @param tipoMultiple String que contiene el id de bt_tipopregunta para las
	 *                     PreguntasTestMultiple
	 * @param id           Int donde se guardara la id de la pregunta creada para
	 *                     poder crear las respuestas adecuadas
	 * @param row          Numero total de respuestas que hay
	 * 
	 * @return resultado que retornara true si la operacion se hace y false si no se
	 *         cumple
	 */
	public static boolean crearPregunta(Pregunta pregunta, String[] error) {

		Connection con = conectarmysql();
		boolean resultado = false;
		String tipoSimple = "SIMP";
		String tipoMultiple = "MULT";
		String query = "INSERT INTO bt_preguntas (tipoPregunta, contenido, creador) VALUES (?,?,?)";

		try {
			PreparedStatement stmt;

			if (pregunta.getTipoPregunta() == TIPO_PREGUNTA.TEST_RESPUESTA_MULTIPLE) {
				stmt = con.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
				stmt.setString(1, tipoMultiple);
				stmt.setString(2, pregunta.getPregunta());
				stmt.setInt(3, pregunta.setCreador());

				stmt.executeUpdate();

				ResultSet rs = stmt.getGeneratedKeys();

				int id;
				if (rs.next()) {
					id = rs.getInt(1);
					pregunta.setIdPregunta(id);
				}

				stmt = con
						.prepareStatement("INSERT INTO bt_respuestas (descripcion, valida, idPregunta) VALUES(?,?,?)");
				int row = 4;
				for (int i = 0; i < row; i++) {
					stmt.setString(1, pregunta.getPregunta());
					stmt.setBoolean(2, pregunta.esCorrecta());
					stmt.setInt(3, pregunta.getIdPregunta());
					stmt.addBatch();
				}
				stmt.executeBatch();

			} else if (pregunta.getTipoPregunta() == TIPO_PREGUNTA.TEST_RESPUESTA_SIMPLE) {
				stmt = con.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
				stmt.setString(1, tipoSimple);
				stmt.setString(2, pregunta.getPregunta());
				stmt.setInt(3, pregunta.setCreador());

				stmt.executeUpdate();

				ResultSet rs = stmt.getGeneratedKeys();
				int id;
				if (rs.next()) {
					id = rs.getInt(1);
					pregunta.setIdPregunta(id);
				}

				stmt = con
						.prepareStatement("INSERT INTO bt_respuestas (descripcion, valida, idPregunta) VALUES(?,?,?)");
				int row = 4;
				pregunta.setCorrecta(true);
				for (int i = 0; i < row; i++) {
					stmt.setString(1, pregunta.getPregunta());
					stmt.setBoolean(2, pregunta.esCorrecta());
					stmt.setInt(3, pregunta.getIdPregunta());
					pregunta.setCorrecta(false);
					stmt.addBatch();
				}
				stmt.executeBatch();

			}

			resultado = true;
			con.close();

		} catch (SQLException e) {
			error[0] = e.getLocalizedMessage();
		}

		return resultado;

	}

	/**
	 * Funcion con la que poder modificar preugntas
	 * 
	 * @param pregunta Objeto de la clase Pregunta
	 * @param error    Array encargada de la gestion de los errores o excepciones
	 * @return resultado que retornara true si la operacion se hace y false si no se
	 *         cumple
	 */
	public static boolean modificarPregunta(Pregunta pregunta, String[] error) {
		Connection con = conectarmysql();
		boolean resultado = false;
		try {
			PreparedStatement stmt;
			stmt = con.prepareStatement("UPDATE bt_preguntas SET contenido=? " + "WHERE id = ?");
			stmt.setString(1, pregunta.getPregunta());
			stmt.setInt(2, pregunta.getIdPregunta());

			stmt.executeUpdate();
			resultado = true;
			con.close();

		} catch (SQLException e) {
			error[0] = e.getLocalizedMessage();
		}

		return resultado;
	}

	/**
	 * Funcion con la que poder eliminar preguntas
	 * 
	 * @param pregunta Objeto de la clase Preungtas
	 * @param error    Array encargada de la gestion de los errores o excepciones
	 * @return resultado que retornara true si la operacion se hace y false si no se
	 *         cumple
	 */
	public static boolean eliminarPregunta(Pregunta pregunta, String[] error) {
		Connection con = conectarmysql();
		boolean resultado = false;
		try {
			PreparedStatement stmt;
			stmt = con.prepareStatement("DELETE FROM bt_contiene where idPregunta = ?");
			stmt = con.prepareStatement("DELETE FROM bt_pertenece where idPregunta = ?");
			stmt = con.prepareStatement("DELETE FROM bt_respuestas where idPregunta = ?");
			stmt = con.prepareStatement("DELETE FROM bt_preguntas where id = ?");

			stmt.setInt(1, pregunta.getIdPregunta());

			stmt.executeUpdate();

			resultado = true;
			con.close();

		} catch (SQLException e) {
			error[0] = e.getLocalizedMessage();
		}

		return resultado;
	}

	// Examenes

	/**
	 * Funcion con la que poder visualizar todos los examenes de la tabla
	 * bt_examenes
	 * 
	 * @param resultado Boolean para comprobar si se pudo hacer o no la operación
	 */
	public static ArrayList<Examen> visualizarExamenes(String[] error) {
		Connection con = conectarmysql();
		boolean resultado = false;
		ArrayList<Examen> ex = new ArrayList<Examen>();
		try {
			Statement stmt = con.createStatement();
			ResultSet rs = stmt.executeQuery("SELECT id, nombre, descripcionGeneral FROM bt_examenes");

			while (rs.next()) {

				Examen examen = new Examen(rs.getInt("id"), rs.getString("nombre"), rs.getString("descripcionGeneral"));

				ex.add(examen);

			}

			resultado = true;
			con.close();
			rs.close();

		} catch (SQLException e) {
			error[0] = e.getLocalizedMessage();
		}

		return ex;
	}

	/**
	 * Funcion con la que poder crear examenes
	 * 
	 * @param examen Objeto de la clase Examen
	 * @param error  Array encargada de la gestion de los errores o excepciones
	 * @param id     Int donde se guardara la id del examen creado
	 * @return resultado que retornara true si la operacion se hace y false si no se
	 *         cumple
	 */
	public static boolean crearExamen(Examen examen, String[] error) {
		Connection con = conectarmysql();
		PreparedStatement stmt;
		boolean resultado = false;
		String query = "INSERT INTO bt_examenes (nombre, descripcionGeneral) VALUES (?,?)";
		try {
			stmt = con.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
			stmt.setString(1, examen.getNombre());
			stmt.setString(2, examen.getDescripcion());

			stmt.executeUpdate();

			ResultSet rs = stmt.getGeneratedKeys();
			int id;
			if (rs.next()) {
				id = rs.getInt(1);
				examen.setIdExamen(id);
			}
			resultado = true;
			con.close();

		} catch (SQLException e) {
			error[0] = e.getLocalizedMessage();
		}

		return false;

	}

	/**
	 * Funcion con la que poder modificar examenes
	 * 
	 * @param examen Objeto de la clase Examen
	 * @param error  Array encargada de la gestion de los errores o excepciones
	 * @return resultado que retornara true si la operacion se hace y false si no se
	 *         cumple
	 */
	public static boolean modificarExamen(Examen examen, String[] error) {
		Connection con = conectarmysql();
		boolean resultado = false;
		try {
			PreparedStatement stmt;
			stmt = con.prepareStatement("UPDATE bt_examenes SET nombre=?, descripcionGeneral=? " + "WHERE id = ?");
			stmt.setString(1, examen.getNombre());
			stmt.setString(2, examen.getDescripcion());

			stmt.executeUpdate();
			resultado = true;
			con.close();

		} catch (SQLException e) {
			error[0] = e.getLocalizedMessage();
		}

		return resultado;
	}

	/**
	 * Funcion con la que poder eliminar Examenes
	 * 
	 * @param examen Objeto de la clase Examen
	 * @param error  Array encargada de la gestion de los errores o excepciones
	 * @return resultado que retornara true si la operacion se hace y false si no se
	 *         cumple
	 */
	public static boolean eliminarExamen(Examen examen, String[] error) {
		Connection con = conectarmysql();
		boolean resultado = false;
		try {
			PreparedStatement stmt;
			stmt = con.prepareStatement("DELETE FROM bt_contiene where idExamen=?");
			stmt = con.prepareStatement("DELETE FROM bt_examenes where id=?");

			stmt.setInt(1, examen.getIdExamen());

			stmt.executeUpdate();
			resultado = true;
			con.close();

		} catch (SQLException e) {
			error[0] = e.getLocalizedMessage();
		}

		return resultado;
	}

	/**
	 * 
	 * @param idExamen   id de la tabla bt_examenes
	 * @param idPregunta id de la tabla bt_preguntas
	 * @param peso       atributo de la tabla bt_contiene
	 * @param error      Array encargada de la gestion de los errores o excepciones
	 * @return resultado que retornara true si la operacion se hace y false si no se
	 *         cumple
	 */
	public static boolean actualizarPesoPregunta(int idExamen, int idPregunta, int peso, String[] error) {
		Connection con = conectarmysql();
		boolean resultado = false;
		try {
			PreparedStatement stmt;
			stmt = con.prepareStatement("UPDATE bt_contiene SET peso=? " + "WHERE idExamen = ? and idPregunta = ?");
			stmt.setInt(1, peso);
			stmt.setInt(2, idExamen);
			stmt.setInt(3, idPregunta);

			stmt.executeUpdate();
			resultado = true;
			con.close();

		} catch (SQLException e) {
			error[0] = e.getLocalizedMessage();

		}

		return resultado;
	}

	// Usuario

	/**
	 * 
	 * @param connection Parametro que realiza la conexion
	 * @param usuario    String del codUsuario
	 * @param passwd     String de la contraseña de dicho usuario
	 * @param error      String[] donde se gaurdaran los errores
	 * @return login int que retornara -1 si el login falló y el id del usuario en
	 *         caso de login válido
	 */
	public static int comprobarLogin(Connection connection, String usuario, String passwd, String[] error) {
		int login = 0;
		connection = conectarmysql();

		try {
			PreparedStatement stmt;
			stmt = connection.prepareStatement(
					"SELECT id, codUsuario, passwd FROM bt_usuarios where codUsuario = ? and passwd = ?");
			stmt.setString(1, usuario);
			stmt.setString(2, passwd);

			ResultSet rs = stmt.executeQuery();
			login = rs.getInt("id");

			if (usuario != rs.getString("usuario")) {
				error[0] = "El usuario indicado no  existe";
				login = -1;
			} else if (usuario == rs.getString("usuario") && passwd != rs.getString("passwd")) {
				error[0] = "La contraseña no es válida";
				login = -1;
			}

			stmt.execute();
			rs.close();
			connection.close();

		} catch (SQLException e) {
			error[0] = e.getLocalizedMessage();
		}

		return login;
	}

	// Respuestas
	// TODO debe crear nuevas respuestas en base a
	// las que están en el objeto pregunta asignándoles el id de pregunta como su
	// pregunta "padre"
	public static boolean actualizarRespuestas(Pregunta pregunta, String[] error) {
		boolean resultado = false;
		Connection con = conectarmysql();
		PreparedStatement stmt;
		try {
			stmt = con.prepareStatement("SELECT id FROM bt_respuestas WHERE idPregunta = ?");
			ResultSet rs = stmt.executeQuery();
			stmt.setInt(1, pregunta.getIdPregunta());

			stmt = con.prepareStatement("DELETE FROM bt_respuestas where idPregunta = ?");
			stmt.setInt(1, pregunta.getIdPregunta());

			stmt.executeUpdate();

		} catch (SQLException e) {
			error[0] = e.getLocalizedMessage();
		}

		return resultado;
	}

	// Categorias

	/**
	 * 
	 * @param nombre  String nombre de la categoria
	 * @param creador Int id del usuario
	 * @param error   Array encargada de la gestion de los errores o excepciones
	 * @return resultado que retornara true si la operacion se hace y false si no se
	 *         cumple
	 */
	public static boolean crearCategoria(String nombre, int creador, String[] error) {
		boolean resultado = false;
		Connection con = conectarmysql();
		PreparedStatement stmt;
		try {
			stmt = con.prepareStatement("INSERT INTO bt_categorias (nombre, creador) VALUES (?,?)");
			stmt.setString(1, nombre);
			stmt.setInt(2, creador);

			stmt.executeUpdate();
			resultado = true;
			con.close();

		} catch (SQLException e) {
			error[0] = e.getLocalizedMessage();
		}
		return resultado;
	}

	/**
	 * 
	 * @param id    Int id de la categoría
	 * @param error   Array encargada de la gestion de los errores o excepciones
	 * @return resultado que retornara true si la operacion se hace y false si no se
	 *         cumple
	 */
	public static boolean borrarCategoria(int id, String[] error) {
		boolean resultado = false;
		Connection con = conectarmysql();
		PreparedStatement stmt;
		try {
			stmt = con.prepareStatement("DELETE FROM bt_categorias WHERE id = ?");
			stmt = con.prepareStatement("DELETE FROM bt_pertenece WHERE idCategoria = ?");
			stmt.setInt(1, id);

			stmt.executeUpdate();
			resultado = true;
			con.close();

		} catch (SQLException e) {
			error[0] = e.getLocalizedMessage();
		}

		return resultado;

	}
	
	
	/**
	 * 
	 * @param id Int id de la Categoría
	 * @param nombre String nombre de la Categoría
	 * @param error   Array encargada de la gestion de los errores o excepciones
	 * @return resultado que retornara true si la operacion se hace y false si no se
	 *         cumple
	 */
	public static boolean modificarCategoria(int id, String nombre, String[] error) {
		boolean resultado = false;
		Connection con = conectarmysql();
		PreparedStatement stmt;
		try {
			stmt = con.prepareStatement("UPDATE bt_categorias SET nombre=?" + "WHERE id = ?");
			stmt.setString(1, nombre);
			stmt.setInt(2, id);
			stmt.executeUpdate();
			resultado = true;
			con.close();

		} catch (SQLException e) {
			error[0] = e.getLocalizedMessage();
		}

		return resultado;
		}
	
	
	public static boolean asignarCategorias(int[] categorias, int id, String[] error) {
		boolean resultado = false;
		Connection con = conectarmysql();
		PreparedStatement stmt;
		try {
			if(categorias == null) {
				stmt = con.prepareStatement("DELETE FROM bt_pertenece WHERE idPregunta = ?");
				stmt.setInt(1, id);
				stmt.executeUpdate();
				resultado = true;
			}
			
			
			con.close();
		} catch (SQLException e) {
			error[0] = e.getLocalizedMessage();
		}

		return resultado;
		}
	
	/**
	 * 
	 * @param creador Int id del usuario
	 * @param error   Array encargada de la gestion de los errores o excepciones
	 * @return resultado que retornara true si la operacion se hace y false si no se
	 *         cumple
	 */
	public static ArrayList<Categoria> obtenerCategorias(int creador, String[] error){
		Connection con = conectarmysql();
		ArrayList<Categoria> ct = new ArrayList<Categoria>();
		try {
			PreparedStatement stmt = con.prepareStatement("SELECT id, nombre, creador FROM bt_categoria WHERE creador = ?");
			stmt.setInt(1, creador);
			ResultSet rs = stmt.executeQuery();
			while(rs.next()) {
				
				Categoria categoria = new Categoria(rs.getInt("id"), rs.getString("nombre"), rs.getInt("creador"));
				
				ct.add(categoria);
				
			}
		} catch (SQLException e) {
			error[0] = e.getLocalizedMessage();
		}
		return ct;
	}

}
