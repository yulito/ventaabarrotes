/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controller;

/**
 *
 * @author yulia
 */
import java.awt.Image;
import java.io.File;
import java.io.IOException;
import java.nio.file.DirectoryNotEmptyException;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import model.DaoProducto;
import model.Producto;

public class ProductoController {
    private DaoProducto dao;
    private Producto obj;
    
    public ProductoController(){
        this.dao = new DaoProducto();
        this.obj = new Producto();
    }
    
    //CALL searchProduct (CALL searchProduct(id,"","") / CALL searchProduct(0,"xasxa","nomproduct"))
    public List<Producto> mostrarProducto(int id, String cod, String prod){
        List<Producto> producto;
        producto = this.dao.search(id,cod,prod);
        return producto;
    }
    
    public List<Producto> listarProductos(int op, int id){
        List<Producto> obj;
        //obteniendo resultado de consulta y metiendo datos al objeto
        obj = this.dao.show(op, id);
        return obj;
    }
   
    
    public boolean guardar(Producto producto, File img, String nombreFoto) throws IOException {
        boolean bool = false;

        // Obtener ruta absoluta del proyecto
        String rutaProyecto = System.getProperty("user.dir");
        File carpetaStorage = new File(rutaProyecto, "storage");

        // Crear carpeta si no existe
        if (!carpetaStorage.exists()) {
            carpetaStorage.mkdirs();
        }

        // Formatear nombre de la imagen
        String nameFormat = (nombreFoto != null && !nombreFoto.isEmpty())
            ? nombreFoto.replace(" ", "_")
            : "";

        // Guardar ruta relativa en el producto
        producto.setImagen(nameFormat.isEmpty() ? "" : "storage"+ File.separator  + nameFormat);

        // Insertar en la base de datos
        if (dao.insert(producto)) {
            // Validar si la imagen existe antes de intentar copiarla
            if (img != null && !nameFormat.isEmpty()) {
                File destino = new File(carpetaStorage, nameFormat);
                Files.copy(img.toPath(), destino.toPath(), StandardCopyOption.REPLACE_EXISTING);
            }
            bool = true;
        }

        return bool;
    }
    
    //++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++    
    // REVISAR ESTO
    public boolean editar(Producto producto, File img, String nombreFoto, String old) throws IOException {
        boolean bool = false;

        // Obtener ruta absoluta del proyecto
        String rutaProyecto = System.getProperty("user.dir");
        File carpetaStorage = new File(rutaProyecto, "storage");

        // Crear carpeta si no existe
        if (!carpetaStorage.exists()) {
            carpetaStorage.mkdirs();
        }

        String nameFormat = "";
        String subOld = "";

        // Validar nombre de imagen nuevo
        if (nombreFoto != null && !nombreFoto.isEmpty()) {
            nameFormat = nombreFoto.replace(" ", "_");
        }

        // Si hay imagen nueva
        if (img != null && nameFormat != null && !nameFormat.isEmpty()) {
            File destino = new File(carpetaStorage, nameFormat);

            // Solo copiar si el archivo no existe o es distinto
            if (!destino.exists() || !Files.isSameFile(img.toPath(), destino.toPath())) {
                Files.copy(img.toPath(), destino.toPath(), StandardCopyOption.REPLACE_EXISTING);
            }

            producto.setImagen("storage"+ File.separator + nameFormat);

            // Eliminar imagen antigua si es distinta
            if (old != null && !old.isEmpty()) {
                subOld = old.substring(8); // quitar "storage/"
                if (!subOld.equalsIgnoreCase(nameFormat)) {
                    File oldFile = new File(rutaProyecto, old);
                    if (oldFile.exists()) {
                        Files.delete(oldFile.toPath());
                    }
                }
            }
        } else {
            // No hay imagen nueva, mantener la antigua si existe
            producto.setImagen(old != null ? old : "");
        }

        // Actualizar en base de datos
        if (dao.update(producto)) {
            bool = true;
        }

        return bool;
    } 
        
}
