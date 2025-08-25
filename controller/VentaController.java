/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controller;

import helper.PDFCreator;
import helper.TextDocument;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import model.DaoDetalle;
import model.DaoVenta;
import model.Detalle;

/**
 *
 * @author yulia
 */
public class VentaController {
    
    // Se usa un ExecutorService con un solo hilo para asegurar
    // que las tareas se ejecuten en orden y no saturen la base de datos.
    private final ExecutorService executor = Executors.newSingleThreadExecutor();
    private DaoVenta daoVenta;
    private DaoDetalle daoDetalle;
    private PDFCreator pdfObj;
    private TextDocument doc;
    
    public VentaController(){
        this.daoVenta = new DaoVenta();
        this.daoDetalle = new DaoDetalle();
        //this.pdfObj = new PdfGenerator(); // Inicializar el objeto PDF
    }
    
    public void registrarVenta(List<Detalle> detalle ,double total, String pago) throws SQLException{                  
        // Ejecutar toda la lógica en un hilo separado para no bloquear la UI o el hilo principal
        executor.submit(() -> {
            try {
                // Paso 1: Guardar la venta (puede ser tardado)
                System.out.println("Iniciando registro de venta...");
                int id = this.daoVenta.store(total, pago);

                if (id != 0) {
                    System.out.println("Venta guardada con ID: " + id);
                    
                    // Paso 2: Guardar el detalle, que depende del ID de la venta
                    System.out.println("Iniciando registro de detalles...");
                    if(this.daoDetalle.store(id, detalle)){
                        System.out.println("Detalles guardados exitosamente.");
                    }else{
                        System.out.println("Error al insertar los detalles de ventas.");
                    }

                    // Paso 3: Crear el PDF, que depende de la venta y sus detalles
                    System.out.println("Iniciando creación de PDF...");
                    this.pdfObj = new PDFCreator();
                    this.pdfObj.create(detalle, total, id);                    
                    System.out.println("PDF creado exitosamente.");
                    
                    // Paso 4: Crear documento texto plano (bloc de notas) con valores NETOS
                    this.doc = new TextDocument();
                    this.doc.createTextNeto(detalle, id);
                    System.out.println("DOCUMENTO de NETOS creado exitosamente");
                } else {
                    System.out.println("Error: No se pudo guardar la venta.");
                }

            } catch (SQLException e) {
                // Manejar cualquier excepción de la base de datos
                System.err.println("Error de base de datos durante la operación: " + e.getMessage());
                // Aquí puedes agregar lógica para notificar al usuario, escribir en un log, etc.
            }
        });
    }
    
    // Método para cerrar el executor al terminar la aplicación
    public void shutdownExecutor() {
        executor.shutdown();
    }
}
