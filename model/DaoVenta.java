/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package model;

import database.Conexion;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JOptionPane;

/**
 *
 * @author yulia
 */
public class DaoVenta {
    
    private Venta obj;
    
    public DaoVenta(){}
    
    public int store(double total, String pago){
        Conexion conexion = Conexion.getInstance();
        Connection conn = conexion.getConnection();        
        //guardamos la venta
        String call = "{CALL insertSale(?,?,?)}";
        
        try(CallableStatement stmt = conn.prepareCall(call)){
            stmt.setDouble(1, total);
            stmt.setString(2, pago);
            
            // Se usa el índice 3, que corresponde al tercer ?
            // Types.INTEGER indica el tipo de dato que esperamos
            stmt.registerOutParameter(3, Types.INTEGER);

            // Ejecutar el procedimiento almacenado
            stmt.execute();

            // Obtener el valor del parámetro de salida
            // Con getInt() recuperamos el valor entero del índice 3
            int idVentaGenerado = stmt.getInt(3);
            
            return idVentaGenerado;
                        
        }catch(SQLException e){
            JOptionPane.showMessageDialog(null, "Error al ejecutar procedimiento o conexión con base de datos!!"+e.getMessage());
            return 0;
        }
    }
    
    public List<Venta> getSales(String before, String after){
        Conexion conexion = Conexion.getInstance();
        Connection conn = conexion.getConnection();
        ArrayList<Venta> list = new ArrayList();
        
        //llamando al procedure
        String call = "{CALL getSales(?,?)}";
        
        try (CallableStatement stmt = conn.prepareCall(call)) {
            //metiendo los parametros al procedure a traves de CallableStatement
            stmt.setString(1, before);
            stmt.setString(2, after);

            //mostrando los resultados
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    //instancear un objeto venta por cada dato recogido por el cursor "rs"
                    this.obj = new Venta();
                    //metiendo los datos en el objeto
                    this.obj.setId_venta(rs.getInt("nroventa"));
                    this.obj.setFecha(rs.getDate("fecha").toLocalDate());
                    this.obj.setTotal_venta(rs.getDouble("totalventa"));
                    this.obj.setMedio(rs.getString("medio_pago"));
                    //agregar los objetos a la lista
                    list.add(this.obj);
                }
            }

        } catch (SQLException e) {
            System.out.println("Error al ejecutar el procedimiento getSales: " + e.getMessage());
            JOptionPane.showMessageDialog(null, "Error al ejecutar procedimiento o conexión con base de datos!!"+e.getMessage());
        }
        return list;
    }
    
    public boolean changeState(int id){
        Conexion conexion = Conexion.getInstance();
        Connection conn = conexion.getConnection();
        
        //sql update
        String sql = "UPDATE venta SET estado = 1 WHERE nroventa = "+id;
        try(PreparedStatement pstm = conn.prepareStatement(sql)){
            pstm.executeUpdate();
            return true;
        }catch(SQLException e){
            JOptionPane.showMessageDialog(null, "Error al ejecutar procedimiento o conexión con base de datos!!"+e.getMessage());
            return false;
        }        
    }
}
