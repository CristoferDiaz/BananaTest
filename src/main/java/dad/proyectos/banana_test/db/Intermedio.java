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


	
}
