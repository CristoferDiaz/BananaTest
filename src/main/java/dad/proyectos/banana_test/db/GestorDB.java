package dad.proyectos.banana_test.db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import dad.proyectos.banana_test.model.Categoria;
import dad.proyectos.banana_test.model.Examen;
import dad.proyectos.banana_test.model.Pregunta;
import dad.proyectos.banana_test.model.Pregunta.TIPO_PREGUNTA;
import dad.proyectos.banana_test.model.preguntas.PreguntaTestMultiple;
import dad.proyectos.banana_test.model.preguntas.PreguntaTestSimple;
import dad.proyectos.banana_test.utils.Preferencias;
import javafx.beans.property.StringProperty;

public abstract class GestorDB {

	/**
	 * Clase abstracta intermedia entre el proyecto y la base de datos donde se
	 * haran operaciones basicas como crear, eliminar, modificar y visualizar tanto
	 * la tabla bt_examenes como la tabla bt_preguntas
	 * 
	 * @author SamirElKharrat
	 */

	static Connection conexion = null;
	public static String driver = "com.mysql.cj.jdbc.Driver";

	// Funcion para conectarse a la base de datos que estamos usando
	public static Connection conectarmysql() {

		try {
			Class.forName(driver);
			conexion = DriverManager.getConnection(
					"jdbc:mysql://" + Preferencias.properties.getProperty("direccion_servidor") + "/bananatest",
					Preferencias.usuarioServidor, Preferencias.passwordServidor);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return conexion;

	}

	/**
	 * Método encargado de comprobar si el usuario dado existe en la BD y puede
	 * loggearse en la aplicación con los datos dados.
	 * 
	 * @param connection Parametro que realiza la conexion
	 * @param usuario    String del codUsuario
	 * @param passwd     String de la contraseña de dicho usuario
	 * @param error      String[] donde se gaurdaran los errores
	 * @return login int que retornara -1 si el login falló y el id del usuario en
	 *         caso de login válido
	 */
	public static int comprobarLogin(Connection connection, String usuario, String passwd, String[] error) {
		int login = -1;

		try {
			PreparedStatement stmt;
			stmt = connection.prepareStatement("SELECT id FROM bt_usuarios where codUsuario = ? and passwd = ?");
			stmt.setString(1, usuario);
			stmt.setString(2, passwd);

			ResultSet rs = stmt.executeQuery();
			if (rs.next()) {
				login = rs.getInt("id");
			} else {
				error[0] = "El par usuario-contraseña dado no es válido";
			}

			connection.close();

		} catch (SQLException e) {
			error[0] = e.getLocalizedMessage();
		}

		return login;
	}

	// Preguntas

	/**
	 * Funcion con la que poder visualizar todas las preguntas de la tabla
	 * bt_preguntas
	 * 
	 * @param creador    INT id del usuario
	 * @param categorias array donde estan las id de las categorias
	 * @param error      Array encargada de la gestion de los errores o excepciones
	 * @return resultado que retornara true si la operacion se hace y false si no se
	 *         cumple
	 */
	public static ArrayList<Pregunta> visualizarPreguntas(int creador, int[] categorias, String[] error) {
		Connection con = conectarmysql();
		ArrayList<Pregunta> pre = new ArrayList<Pregunta>();

		String tipoSimple = "SIMP";
		String tipoMultiple = "MULT";
		String tipo, contenido, descripcion;
		boolean valida;
		int id;
		try {
			PreparedStatement stmt = con.prepareStatement(
					"SELECT id, tipoPregunta, contenido, creador FROM bt_preguntas WHERE creador = ?");
			stmt.setInt(1, creador);
			ResultSet rs = stmt.executeQuery();
			while (rs.next()) {
				if (categorias.length != 0) {
					stmt = con.prepareStatement("SELECT idPregunta FROM bt_pertenece WHERE idCategoria = ?");
					ResultSet rs2 = stmt.executeQuery();
					for (int i = 0; i < categorias.length; i++) {
						stmt.setInt(1, categorias[i]);
					}
					id = rs2.getInt("idPregunta");
				} else {
					id = rs.getInt("id");
				}
				tipo = rs.getString("tipoPregunta");
				contenido = rs.getString("contenido");

				stmt = con.prepareStatement(
						"SELECT id, descripcion, valida, idPregunta FROM" + "bt_respuestas WHERE idPregunta = ?");
				stmt.setInt(1, id);
				ResultSet rs3 = stmt.executeQuery();
				String[] respuestas = new String[4];
				String[] textosRespuestas = new String[4];
				boolean[] correctas = new boolean[4];
				while (rs3.next()) {
					rs3.getInt("id");
					descripcion = rs3.getString("descripcion");
					valida = rs3.getBoolean("valida");
					if (valida == true) {
						respuestas[0] = descripcion;
					} else {
						respuestas[1] = descripcion;
						respuestas[2] = descripcion;
						respuestas[3] = descripcion;
					}

					for (int i = 0; i < correctas.length; i++) {
						correctas[i] = valida;
					}

					for (int i = 0; i < textosRespuestas.length; i++) {
						textosRespuestas[i] = descripcion;
					}

				}

				if (tipo == tipoSimple) {
					PreguntaTestSimple preguntaSimple = new PreguntaTestSimple(contenido, respuestas);
					preguntaSimple.setIdPregunta(id);
					pre.add(preguntaSimple);
				} else if (tipo == tipoMultiple) {
					PreguntaTestMultiple preguntaMultiple = new PreguntaTestMultiple(contenido, textosRespuestas,
							correctas);
					preguntaMultiple.setIdPregunta(id);
					pre.add(preguntaMultiple);
				}

			}

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
		String tipoSimple = "PSIMP";
		String tipoMultiple = "PMULT";
		StringProperty[] respuestas = pregunta.obtenerRespuestas();
		Boolean[] valido = ((PreguntaTestMultiple) pregunta).obtenerValidez();
		String query = "INSERT INTO bt_preguntas (tipoPregunta, contenido, creador) VALUES (?,?,?)";

		try {
			PreparedStatement stmt;

			if (pregunta.getTipoPregunta() == TIPO_PREGUNTA.TEST_RESPUESTA_MULTIPLE) {
				stmt = con.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
				stmt.setString(1, tipoMultiple);
				stmt.setString(2, pregunta.getPregunta());
				// stmt.setInt(3, pregunta.setCreador());

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
					for (int a = 0; a < respuestas.length; a++) {
						stmt.setString(1, respuestas[a].toString());
					}
					for (int e = 0; e < valido.length; e++) {
						stmt.setBoolean(2, valido[e]);
					}
					stmt.setInt(3, pregunta.getIdPregunta());
					stmt.addBatch();
				}
				stmt.executeBatch();

			} else if (pregunta.getTipoPregunta() == TIPO_PREGUNTA.TEST_RESPUESTA_SIMPLE) {
				stmt = con.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
				stmt.setString(1, tipoSimple);
				stmt.setString(2, pregunta.getPregunta());
				// stmt.setInt(3, pregunta.setCreador());

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
					for (int a = 0; a < respuestas.length; a++) {
						stmt.setString(1, respuestas[a].toString());
					}
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
	public static ArrayList<Examen> visualizarExamenes(int creador, String[] error) {
		Connection con = conectarmysql();
		ArrayList<Examen> listadoExamenes = new ArrayList<Examen>();
		try {
			PreparedStatement stmt = con.prepareStatement(
					"SELECT id, nombre, descripcionGeneral, creador FROM bt_examenes WHERE creador = ?");
			stmt.setInt(1, creador);
			ResultSet rs = stmt.executeQuery();

			while (rs.next()) {

				Examen examen = new Examen(rs.getInt("id"), rs.getString("nombre"), rs.getString("descripcionGeneral"),
						rs.getInt("creador"));

				PreparedStatement stmtExamen = con
						.prepareStatement("SELECT idPregunta, peso, tipoPregunta, contenido, creador FROM bt_contiene"
								+ " INNER JOIN bt_preguntas ON bt_contiene.idPregunta = bt_preguntas.id"
								+ " WHERE idExamen = ?");
				stmtExamen.setInt(1, examen.getIdExamen());
				ResultSet rsPreguntas = stmtExamen.executeQuery();

				while (rsPreguntas.next()) {
					Pregunta p;

					PreparedStatement stmtRespuestas = con.prepareStatement(
							"SELECT descripcion, valida FROM bt_respuestas WHERE idPregunta = ? ORDER BY id ASC");
					stmtRespuestas.setInt(1, rsPreguntas.getInt("idPregunta"));
					ResultSet rsRespuestas = stmtRespuestas.executeQuery();
					String[] listaRespuestas = new String[4];
					boolean[] listaValidas = new boolean[listaRespuestas.length];
					int i = 0;

					while (rsRespuestas.next()) {
						listaRespuestas[i] = rsRespuestas.getString("descripcion");
						listaValidas[i] = rsRespuestas.getBoolean("valida");
						i++;
					}

					if (rsPreguntas.getString("tipoPregunta").equals("PSIMP")) {
						p = new PreguntaTestSimple(rsPreguntas.getString("contenido"), listaRespuestas);
					} else {
						p = new PreguntaTestMultiple(rsPreguntas.getString("contenido"), listaRespuestas, listaValidas);
					}

					p.setIdPregunta(rsPreguntas.getInt("idPregunta"));
					p.setPeso(rsPreguntas.getInt("peso"));
					p.setCreador(rsPreguntas.getInt("creador"));
					examen.getPreguntas().add(p);
				}

				listadoExamenes.add(examen);

			}
			con.close();

		} catch (SQLException e) {
			error[0] = e.getLocalizedMessage();
		}

		return listadoExamenes;
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
		String query = "INSERT INTO bt_examenes (nombre, descripcionGeneral, clave, creador) VALUES (?,?,'',?)";
		try {
			stmt = con.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
			stmt.setString(1, examen.getNombre());
			stmt.setString(2, examen.getDescripcion());
			stmt.setInt(3, examen.getCreador());

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

		return resultado;

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
			stmt.setInt(3, examen.getIdExamen());

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
			stmt = con.prepareStatement("DELETE FROM bt_contiene WHERE idExamen=?");
			stmt.setInt(1, examen.getIdExamen());
			stmt.execute();
			stmt = con.prepareStatement("DELETE FROM bt_examenes where id=?");
			stmt.setInt(1, examen.getIdExamen());
			resultado = (stmt.executeUpdate() > 0);
		} catch (SQLException e) {
			error[0] = e.getLocalizedMessage();
		} finally {
			try {
				con.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
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

	// Respuestas

	/**
	 * Funcion para actualizar las respuestas
	 * 
	 * @param pregunta Objeto de la clase Preungtas
	 * @param error    Array encargada de la gestion de los errores o excepciones
	 * @return resultado que retornara true si la operacion se hace y false si no se
	 *         cumple
	 */
	public static boolean actualizarRespuestas(Pregunta pregunta, String[] error) {
		boolean resultado = false;
		Connection con = conectarmysql();
		PreparedStatement stmt;
		StringProperty[] respuestas;
		Boolean[] valido;
		try {
			pregunta.setCorrecta(true);
			stmt = con.prepareStatement("SELECT id FROM bt_respuestas WHERE idPregunta = ?");
			ResultSet rs = stmt.executeQuery();
			stmt.setInt(1, pregunta.getIdPregunta());

			stmt = con.prepareStatement("DELETE FROM bt_respuestas where idPregunta = ?");
			stmt.setInt(1, pregunta.getIdPregunta());
			stmt.executeUpdate();

			while (rs.next()) {
				respuestas = pregunta.obtenerRespuestas();
				if (pregunta.getTipoPregunta() == TIPO_PREGUNTA.TEST_RESPUESTA_SIMPLE) {

					stmt = con.prepareStatement(
							"INSERT INTO bt_respuestas (descripcion, valida, idPregunta) VALUES (?,?,?)");
					for (int i = 0; i < respuestas.length; i++) {
						stmt.setString(1, respuestas[i].toString());
					}
					stmt.setBoolean(2, pregunta.esCorrecta());
					stmt.setInt(3, pregunta.getIdPregunta());
					pregunta.setCorrecta(false);
					stmt.addBatch();

				} else if (pregunta.getTipoPregunta() == TIPO_PREGUNTA.TEST_RESPUESTA_MULTIPLE) {
					valido = ((PreguntaTestMultiple) pregunta).obtenerValidez();
					stmt = con.prepareStatement(
							"INSERT INTO bt_respuestas (descripcion, valida, idPregunta) VALUES (?,?,?)");
					for (int i = 0; i < respuestas.length; i++) {
						stmt.setString(1, respuestas[i].toString());
					}
					for (int i = 0; i < valido.length; i++) {
						stmt.setBoolean(2, valido[i]);
					}
					stmt.setInt(3, pregunta.getIdPregunta());
					stmt.addBatch();

					stmt.executeBatch();

				}

				resultado = true;
				stmt.close();

			}

		} catch (SQLException e) {
			error[0] = e.getLocalizedMessage();
		}

		return resultado;
	}

	// Categorias

	/**
	 * Funcion para crear Categorias
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
	 * Funcion para borrar Categorias
	 * 
	 * @param id    Int id de la categoría
	 * @param error Array encargada de la gestion de los errores o excepciones
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
	 * Funcion para modificar Categorias
	 * 
	 * @param id     Int id de la Categoría
	 * @param nombre String nombre de la Categoría
	 * @param error  Array encargada de la gestion de los errores o excepciones
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

	/**
	 * Funcion para asignar categorias
	 * 
	 * @param id         Int id de la Categoría
	 * @param categorias Array donde se guardan las id de categorias
	 * @param error      Array encargada de la gestion de los errores o excepciones
	 * @return resultado que retornara true si la operacion se hace y false si no se
	 *         cumple
	 */
	public static boolean asignarCategorias(int[] categorias, int id, String[] error) {
		boolean resultado = false;
		Connection con = conectarmysql();
		PreparedStatement stmt;
		try {
			stmt = con.prepareStatement("INSERT INTO bt_pertenece (idPregunta, idCategoria) VALUES (?,?)");
			for (int i = 0; i < categorias.length; i++) {
				stmt.setInt(1, id);
				stmt.setInt(2, categorias[i]);
				stmt.addBatch();

			}
			stmt.executeBatch();
			if (categorias.length == 0) {
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
	 * Funcion para obtener categorias
	 * 
	 * @param creador Int id del usuario
	 * @param error   Array encargada de la gestion de los errores o excepciones
	 * @return resultado que retornara true si la operacion se hace y false si no se
	 *         cumple
	 */
	public static ArrayList<Categoria> obtenerCategorias(int creador, String[] error) {
		Connection con = conectarmysql();
		ArrayList<Categoria> ct = new ArrayList<Categoria>();
		try {
			PreparedStatement stmt = con
					.prepareStatement("SELECT id, nombre, creador FROM bt_categoria WHERE creador = ?");
			stmt.setInt(1, creador);
			ResultSet rs = stmt.executeQuery();
			while (rs.next()) {

				Categoria categoria = new Categoria(rs.getInt("id"), rs.getString("nombre"), rs.getInt("creador"));

				ct.add(categoria);

			}
		} catch (SQLException e) {
			error[0] = e.getLocalizedMessage();
		}
		return ct;
	}

}
