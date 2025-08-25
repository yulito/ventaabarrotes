/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package model;

import database.Conexion;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JOptionPane;

/**
 *
 * @author yulia
 */
public class DaoCategoria {
    private Categoria obj;
    
    public DaoCategoria(){
    
    }
    
    public List<Categoria> getCategoria(int id, boolean bool){
        Conexion conexion = Conexion.getInstance();
        Connection conn = conexion.getConnection();
        ArrayList<Categoria> list = new ArrayList();
        
        //llamando al procedure
        String call = "{CALL getCategory(?,?)}";
        
        try (CallableStatement stmt = conn.prepareCall(call)) {
            //metiendo los parametros al procedure a traves de CallableStatement
            stmt.setInt(1, id);
            stmt.setBoolean(2, bool);

            //mostrando los resultados
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    //instancear un objeto categoria por cada dato recogido por el cursor "rs"
                    this.obj = new Categoria();
                    //metiendo los datos en el objeto
                    this.obj.setId_categoria(rs.getInt("idcat"));
                    this.obj.setCategoria(rs.getString("cat"));
                    //agregar los objetos a la lista
                    list.add(this.obj);
                }
            }

        } catch (SQLException e) {
            System.out.println("Error al ejecutar el procedimiento getElement: " + e.getMessage());
            JOptionPane.showMessageDialog(null, "Error al ejecutar procedimiento o conexión con base de datos!!"+e.getMessage());
        }
        return list;
    }
    
    public void insert(Categoria obj){
        Conexion conexion = Conexion.getInstance();
        Connection conn = conexion.getConnection();
        
        String call = "{CALL insertToCategory(?)}";
        
        try(CallableStatement stmt = conn.prepareCall(call)){
            stmt.setString(1, obj.getCategoria());
            try(ResultSet rs = stmt.executeQuery()){
                JOptionPane.showMessageDialog(null,"Guardado correctamente!!");
            }catch(SQLException e){
                JOptionPane.showMessageDialog(null,"Error de operación. No se ha podido actualizar los datos."+e.getMessage());
            }
        }catch(SQLException e){
            JOptionPane.showMessageDialog(null, "Error al ejecutar procedimiento o conexión con base de datos!!"+e.getMessage());
        }
    }
    
    public void update(Categoria cat){        
        //System.out.println("edit: "+cat.getCategoria());
        Conexion conexion = Conexion.getInstance();
        Connection conn = conexion.getConnection();
        
        String call = "{CALL updateCategory(?,?)}";
        
        try (CallableStatement stmt = conn.prepareCall(call)){
            stmt.setInt(1, cat.getId_categoria());
            stmt.setString(2, cat.getCategoria());
            
            try(ResultSet rs = stmt.executeQuery()){
                JOptionPane.showMessageDialog(null,"Actualizado correctamente!!");
            }catch(SQLException e){
                JOptionPane.showMessageDialog(null,"Error de operación. No se ha podido actualizar los datos."+e.getMessage());
            }
        }catch(SQLException e){
            JOptionPane.showMessageDialog(null, "Error al ejecutar procedimiento o conexión con base de datos!!"+e.getMessage());
        }
    }
}
