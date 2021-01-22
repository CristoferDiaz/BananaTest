package dad.proyectos.banana_test.db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Scanner;

import dad.proyectos.banana_test.model.Examen;
import dad.proyectos.banana_test.model.Pregunta;

public abstract class Intermedio {
	
	/**
	 * Clase abstracta intermedia entre el proyecto y la base de datos donde
	 * se haran operaciones basicas como crear, eliminar, modificar y visualizar
	 * tanto la tabla bt_examenes como la tabla bt_preguntas
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

	// Funcion para desconectarse de la base de datos
	static void desconectarBD() {
		try {
			conexion.close();
		} catch (SQLException ex) {
			System.out.println(ex.getMessage());
		}
	}

	/** Funcion con la que poder visualizar todas las preguntas de la tabla
	* bt_preguntas
	* @param resultado Boolean para comprobar si se pudo hacer o no la operación
	*/
	public static boolean visualizarPreguntas() {
		Connection con = conectarmysql();
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
			desconectarBD();
			rs.close();

		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		return resultado;
	}

	/** Funcion con la que poder visualizar todos los examenes de la tabla
	* bt_examenes
	* @param resultado Boolean para comprobar si se pudo hacer o no la operación
	*/
	public static boolean visualizarExamenes() {
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
			desconectarBD();
			rs.close();
			

		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		return resultado;
	}
	
	
	public static boolean crearPregunta(Pregunta pregunta) {
		Connection con = conectarmysql();
		boolean resultado = false;
		try {
			PreparedStatement stmt;
			stmt = con.prepareStatement("INSERT INTO bt_preguntas (tipoPregunta, contenido) VALUES (?,?)");
			stmt.setObject(1, pregunta.getTipoPregunta());
			stmt.setString(2, pregunta.getPregunta());
			
			stmt.executeUpdate();
			resultado = true;
			
			
		}catch(SQLException e) {
			e.printStackTrace();
		}
		
		
		return false;
		
	}
	
	public static boolean crearExamen(Examen examen) {
		Connection con = conectarmysql();
		boolean resultado = false;
		try {
			PreparedStatement stmt;
			stmt = con.prepareStatement("INSERT INTO bt_examenes (nombre, descripcionGeneral) VALUES (?,?)");
			stmt.setString(1, examen.getNombre());
			stmt.setString(2, examen.getDescripcion());
			
			stmt.executeUpdate();
			resultado = true;
			
			
		}catch(SQLException e) {
			e.printStackTrace();
		}
		
		return false;
		
	}
	
}
