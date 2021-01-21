package dad.proyectos.banana_test.db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Scanner;

public abstract class Intermedio {

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
	void desconectarBD() {
		try {
			conexion.close();
		} catch (SQLException ex) {
			System.out.println(ex.getMessage());
		}
	}

	// Funcion con la que poder visualizar todas las preguntas de la tabla
	// bt_preguntas
	public boolean visualizarPreguntas() {
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
			rs.close();

		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		return resultado;
	}

	// Funcion con la que poder visualizar todos los examenes de la tabla
	// bt_examenes
	public void visualizarExamenes() {
		Connection con = conectarmysql();
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

			rs.close();

		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
}
