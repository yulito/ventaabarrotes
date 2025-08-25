/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package model;

/**
 *
 * @author yulia
 */
import database.Conexion;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JOptionPane;

public class DaoProducto {
    private Producto obj;
    
    public DaoProducto(){}
    
    public List<Producto> show(int op, int id){
        Conexion conexion = Conexion.getInstance();
        Connection conn = conexion.getConnection(); 
        ArrayList<Producto> list = new ArrayList();
        
        String call = "{CALL getProducts(?,?)}";
        
        try(CallableStatement stmt = conn.prepareCall(call)){
            stmt.setInt(1, op);
            stmt.setInt(2, id);
            
            //mostrando los resultados
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    //instancear un objeto categoria por cada dato recogido por el cursor "rs"
                    this.obj = new Producto();
                    //metiendo los datos en el objeto
                    this.obj.setId_producto(rs.getInt("idprod"));
                    this.obj.setCodigo(rs.getString("codigo"));
                    this.obj.setProducto(rs.getString("prod"));
                    this.obj.setUnidad(rs.getString("unidad"));
                    this.obj.setStock(rs.getDouble("stock"));
                    this.obj.setValor(rs.getDouble("valor"));
                    this.obj.setDescuento(rs.getDouble("descuento"));
                    this.obj.setImagen(rs.getString("img"));
                    this.obj.setId_categoria(rs.getInt("idcat"));
                    list.add(this.obj);                                        
                }
            }
            
        }catch (SQLException e) {
            System.out.println("Error al ejecutar el procedimiento getElement: " + e.getMessage());
            JOptionPane.showMessageDialog(null, "Error al ejecutar procedimiento o conexión con base de datos!!"+e.getMessage());
        }
        
        return list;
    }
    
    public List<Producto> search(int id, String cod, String prod){
        Conexion conexion = Conexion.getInstance();
        Connection conn = conexion.getConnection(); 
        List<Producto> list = new ArrayList();
    
        String call = "{CALL searchProduct(?,?,?)}";
        
        try (CallableStatement stmt = conn.prepareCall(call)) {
            //metiendo los parametros al procedure a traves de CallableStatement
            stmt.setInt(1, id);
            stmt.setString(2, cod);
            stmt.setString(3,prod);

            //mostrando los resultados
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    //instancear un objeto categoria por cada dato recogido por el cursor "rs"
                    this.obj = new Producto();
                    //metiendo los datos en el objeto
                    this.obj.setId_producto(rs.getInt("idprod"));
                    this.obj.setCodigo(rs.getString("codigo"));
                    this.obj.setProducto(rs.getString("prod"));
                    this.obj.setUnidad(rs.getString("unidad"));
                    this.obj.setStock(rs.getDouble("stock"));
                    this.obj.setValor(rs.getDouble("valor"));
                    this.obj.setDescuento(rs.getDouble("descuento"));
                    this.obj.setImagen(rs.getString("img"));
                    this.obj.setId_categoria(rs.getInt("idcat"));
                    this.obj.setNamecat(rs.getString("cat"));
                    //agregar los objetos a la lista
                    list.add(this.obj);
                }
            }

        } catch (SQLException e) {
            System.out.println("Error al ejecutar el procedimiento en el resultset: " + e.getMessage());
            JOptionPane.showMessageDialog(null, "Error al ejecutar procedimiento o conexión con base de datos!!"+e.getMessage());
        }
        
        return list;
    }
    
    public boolean insert(Producto data){
        Conexion conexion = Conexion.getInstance();
        Connection conn = conexion.getConnection();        
        boolean resp = false;
        //Preparando el call
        String call = "{CALL insertToProductMod2(?,?,?,?,?,?,?,?)}";
        
        //ingresar datos al procedure del call
        try(CallableStatement stmt = conn.prepareCall(call)){
            stmt.setString(1, data.getCodigo());
            stmt.setString(2, data.getProducto());
            stmt.setString(3, data.getUnidad());
            stmt.setDouble(4, data.getStock());
            stmt.setDouble(5, data.getValor());            
            stmt.setDouble(6, data.getDescuento());
            stmt.setString(7, data.getImagen());
            stmt.setInt(8, data.getId_categoria());            
            //ejecutar la query 
            //stmt.executeUpdate();
            
             // Ejecuta la llamada al procedimiento. Esto devolverá true si hay un ResultSet.
            boolean hasResultSet = stmt.execute();

            // Si hasResultSet es true, significa que el procedimiento devolvió algo.
            if (hasResultSet) {
                try (ResultSet rs = stmt.getResultSet()) {
                    // Lee el mensaje del ResultSet
                    if (rs.next()) {
                        String mensaje = rs.getString(1); // Obtiene el valor de la primera columna
                        System.out.println("Mensaje de la base de datos: " + mensaje);
                        JOptionPane.showMessageDialog(null, mensaje);
                        resp = false;
                    }
                }
            } else {
                // Si hasResultSet es false, la inserción fue exitosa y no hubo mensaje de error.
                System.out.println("El producto ha sido insertado correctamente.");
                resp = true;
            }  
            System.out.println("resultado booleano de DAO: "+resp);
            return resp;
        }catch(SQLException e){
            return resp;
        }        
    }
    
    public boolean update(Producto data){
        Conexion conexion = Conexion.getInstance();
        Connection conn = conexion.getConnection();        
        boolean resp = false;
        //Preparando el call
        String call = "{CALL updateProductMod(?,?,?,?,?,?,?,?,?)}";
        
        //ingresar datos al procedure del call
        try(CallableStatement stmt = conn.prepareCall(call)){
            stmt.setInt(1, data.getId_producto());
            stmt.setString(2, data.getCodigo());
            stmt.setString(3, data.getProducto());
            stmt.setString(4, data.getUnidad());
            stmt.setDouble(5, data.getStock());
            stmt.setDouble(6, data.getValor());            
            stmt.setDouble(7, data.getDescuento());
            stmt.setString(8, data.getImagen());
            stmt.setInt(9, data.getId_categoria());            
            //ejecutar la query 
            // Ejecuta la llamada al procedimiento. Esto devolverá true si hay un ResultSet.
            boolean hasResultSet = stmt.execute();

            // Si hasResultSet es true, significa que el procedimiento devolvió algo.
            if (hasResultSet) {
                try (ResultSet rs = stmt.getResultSet()) {
                    // Lee el mensaje del ResultSet
                    if (rs.next()) {
                        String mensaje = rs.getString(1); // Obtiene el valor de la primera columna
                        System.out.println("Mensaje de la base de datos: " + mensaje);
                        JOptionPane.showMessageDialog(null, mensaje);
                        resp = false;
                    }
                }
            } else {
                // Si hasResultSet es false, la inserción fue exitosa y no hubo mensaje de error.
                System.out.println("El producto ha sido insertado correctamente.");
                resp = true;
            }  
            System.out.println("resultado booleano de DAO: "+resp);
            return resp;
        }catch(SQLException e){
            return resp;
        }
    }
}
