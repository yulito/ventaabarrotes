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
public class DaoDetalle {
    
    private Detalle obj;
    
    public DaoDetalle(){}
    
    public boolean store(int id, List<Detalle> detalle) throws SQLException{
        Conexion conexion = Conexion.getInstance();
        Connection conn = conexion.getConnection();        
        //guardamos la venta
        String call = "{CALL insertDetails(?,?,?,?)}";
        
        try(CallableStatement stmt = conn.prepareCall(call)){
            //quitamos el commit auto generado para poder usar insert batch
            conn.setAutoCommit(false);
            //llenar el lote
            for(Detalle cursor: detalle){
                stmt.setDouble(1, cursor.getCantidad());
                stmt.setDouble(2, cursor.getTotal_detalle());
                stmt.setString(3, cursor.getNombreProd());
                stmt.setInt(4, id);
                stmt.addBatch();
            }
            
            // Ejecutar el lote de inserciones
            int[] filasAfectadas = stmt.executeBatch();

            // Confirmar la transacción
            conn.commit();
            
            System.out.println("filas afectadas: "+filasAfectadas.length);
         
            return true;
                        
        }catch(SQLException e){
            JOptionPane.showMessageDialog(null, "Error al ejecutar procedimiento o conexión con base de datos!!"+e.getMessage());
            // Si algo sale mal, revertir la transacción
            conn.rollback();
            System.err.println("Error en la inserción por lotes. Se revirtió la transacción.");
            e.printStackTrace();

            return false;
        }        
    }
    
    public List<Detalle> show(int id){
        Conexion conexion = Conexion.getInstance();
        Connection conn = conexion.getConnection();
        ArrayList<Detalle> list = new ArrayList();
        
        //call procedure
        String call = "{CALL showSaleDetails(?)}";
        try (CallableStatement stmt = conn.prepareCall(call)){
            stmt.setInt(1, id);
            //mostrando los resultados
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    //instancear un objeto venta por cada dato recogido por el cursor "rs"
                    this.obj = new Detalle();
                    //metiendo los datos en el objeto
                    this.obj.getProd().setCodigo(rs.getString("codigo"));
                    this.obj.getProd().setProducto(rs.getString("prod"));
                    this.obj.getProd().setValor(rs.getDouble("valor"));
                    this.obj.getProd().setDescuento(rs.getDouble("descuento"));
                    this.obj.setCantidad(rs.getDouble("cantidad"));
                    this.obj.setTotal_detalle(rs.getDouble("totaldetalle"));
                    //agregar los objetos a la lista
                    list.add(this.obj);
                }
            }
        }catch (SQLException e) {
            System.out.println("Error al ejecutar el procedimiento getSales: " + e.getMessage());
            JOptionPane.showMessageDialog(null, "Error al ejecutar procedimiento o conexión con base de datos!!"+e.getMessage());
        }
        return list;
    }
}
