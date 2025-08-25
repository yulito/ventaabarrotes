/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package database;

/**
 *
 * @author yulia
 */
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Conexion {

    // Variable estática para la única instancia de la clase
    private static Conexion instance;
    
    // Variable para la conexión a la base de datos
    private Connection connection;
    
    // Credenciales de la base de datos
    //private static final String JDBC_DRIVER = "com.mysql.cj.jdbc.Driver"; // Driver actualizado
    private static final String JDBC_DRIVER = "com.mysql.jdbc.Driver";
    private static final String DB_URL = "jdbc:mysql://localhost:3306/abarrotes";
    private static final String USER = "root";
    private static final String PASS = "";

    // Constructor privado para evitar que se instancie desde fuera
    private Conexion() {
        try {
            // Cargar el driver JDBC
            Class.forName(JDBC_DRIVER);
            
            // Establecer la conexión
            this.connection = DriverManager.getConnection(DB_URL, USER, PASS);
            
            System.out.println("Conexión a la base de datos establecida.");
            
        } catch (SQLException ex) {
            System.out.println("Error al conectar con la base de datos: " + ex.getMessage());
            Logger.getLogger(Conexion.class.getName()).log(Level.SEVERE, null, ex);
        } catch (ClassNotFoundException ex) {
            System.out.println("No se encontró el driver de la base de datos.");
            Logger.getLogger(Conexion.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**
     * Método estático para obtener la única instancia de la clase.
     * Si la instancia no existe, la crea.
     * @return La instancia de la clase Conexion.
     */
    public static Conexion getInstance() {
        if (instance == null) {
            instance = new Conexion();
        }
        return instance;
    }

    /**
     * Método para obtener la conexión a la base de datos.
     * @return El objeto Connection.
     */
    public Connection getConnection() {
        return this.connection;
    }

    /**
     * Método para cerrar la conexión.
     */
    public void closeConnection() {
        try {
            if (this.connection != null && !this.connection.isClosed()) {
                this.connection.close();
                System.out.println("Conexión a la base de datos cerrada.");
            }
        } catch (SQLException ex) {
            Logger.getLogger(Conexion.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}

