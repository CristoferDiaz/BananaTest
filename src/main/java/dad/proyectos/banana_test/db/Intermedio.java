package dad.proyectos.banana_test.db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Scanner;

import dad.proyectos.banana_test.model.Examen;
import dad.proyectos.banana_test.model.Pregunta;
import dad.proyectos.banana_test.model.Pregunta.TIPO_PREGUNTA;
import dad.proyectos.banana_test.model.preguntas.PreguntaTestSimple;

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
	public static boolean visualizarPreguntas(String[] error) {
		Connection con = conectarmysql();
		ArrayList<Pregunta> pregunta = new ArrayList<Pregunta>();
		boolean resultado = false;
		int id;
		String tipoPregunta, contenido;
		try {
			Statement stmt = con.createStatement();
			ResultSet rs = stmt.executeQuery("SELECT id, tipoPregunta, contenido FROM bt_preguntas");

			while (rs.next()) {

				id = rs.getInt("id");
				tipoPregunta = rs.getString("tipoPregunta");
				contenido = rs.getString("contenido");

			}

			resultado = true;
			con.close();
			rs.close();

		} catch (SQLException e) {
			error[0] = e.getLocalizedMessage();
		}

		return resultado;
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
	 * @param id Int donde se guardara la id de la pregunta creada para poder crear las respuestas adecuadas
	 * @return resultado que retornara true si la operacion se hace y false si no se
	 *         cumple
	 */
	public static boolean crearPregunta(Pregunta pregunta, String[] error) {

		Connection con = conectarmysql();
		boolean resultado = false;
		String tipoSimple = "SIMP";
		String tipoMultiple = "MULT";
		String query = "INSERT INTO bt_preguntas (tipoPregunta, contenido) VALUES (?,?)";

		try {
			PreparedStatement stmt;

			if (pregunta.getTipoPregunta() == TIPO_PREGUNTA.TEST_RESPUESTA_MULTIPLE) {
				stmt = con.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
				stmt.setString(1, tipoMultiple);
				stmt.setString(2, pregunta.getPregunta());

				stmt.executeUpdate();
				
				ResultSet rs = stmt.getGeneratedKeys();
				int id;
				if(rs.next()) {
					id = rs.getInt(1);
					pregunta.setIdPregunta(id);
				}

			} else if (pregunta.getTipoPregunta() == TIPO_PREGUNTA.TEST_RESPUESTA_SIMPLE) {
				stmt = con.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
				stmt.setString(1, tipoSimple);
				stmt.setString(2, pregunta.getPregunta());

				stmt.executeUpdate();
				
				ResultSet rs = stmt.getGeneratedKeys();
				int id;
				if(rs.next()) {
					id = rs.getInt(1);
					pregunta.setIdPregunta(id);
				}
				
				
			}

			stmt = con.prepareStatement("INSERT INTO bt_respuestas (descripcion, valida, idPregunta) VALUES(?,?,?)");
			stmt.setString(1, pregunta.getPregunta());
			stmt.setBoolean(2, pregunta.isCorrecta());
			stmt.setInt(3, pregunta.getIdPregunta());

			stmt.executeUpdate();

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
			stmt = con.prepareStatement("DELETE FROM bt_contiene where idPregunta=?");
			stmt = con.prepareStatement("DELETE FROM bt_respuestas where idPregunta=?");
			stmt = con.prepareStatement("DELETE FROM bt_preguntas where id=?");

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
	public static boolean visualizarExamenes(String[] error) {
		Connection con = conectarmysql();
		boolean resultado = false;
		int id;
		String nombre, descGeneral;
		try {
			Statement stmt = con.createStatement();
			ResultSet rs = stmt.executeQuery("SELECT id, nombre, descripcionGeneral FROM bt_examenes");

			while (rs.next()) {

				id = rs.getInt("id");
				nombre = rs.getString("nombre");
				descGeneral = rs.getString("descripcionGeneral");
			}

			resultado = true;
			con.close();
			rs.close();

		} catch (SQLException e) {
			error[0] = e.getLocalizedMessage();
		}

		return resultado;
	}

	/**
	 * Funcion con la que poder crear examenes
	 * 
	 * @param examen Objeto de la clase Examen
	 * @param error  Array encargada de la gestion de los errores o excepciones
	 * @param id Int donde se guardara la id del examen creado
	 * @return resultado que retornara true si la operacion se hace y false si no se
	 *         cumple
	 */
	public static boolean crearExamen(Examen examen, String[] error) {
		Connection con = conectarmysql();
		PreparedStatement stmt;
		boolean resultado = false;
		String query ="INSERT INTO bt_examenes (nombre, descripcionGeneral) VALUES (?,?)";
		try {
			stmt = con.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
			stmt.setString(1, examen.getNombre());
			stmt.setString(2, examen.getDescripcion());

			stmt.executeUpdate();
			
			ResultSet rs = stmt.getGeneratedKeys();
			int id;
			if(rs.next()) {
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

}
