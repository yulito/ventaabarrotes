/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package helper;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;
import model.Detalle;

/**
 *
 * @author yulia
 */
public class TextDocument {
    
    private DateFormat formateador;
    private Date fecForm;
    private DecimalFormat formato;
    
    public TextDocument(){
        this.formateador= new SimpleDateFormat("dd-MM-yy");
        this.fecForm = new Date();
        this.formato = new DecimalFormat("##,###.##");
    }
    
    public void createTextNeto(List<Detalle> detalle, int id){
        System.out.println("Procesando Texto de valores NETOS");
        
        String userHome = System.getProperty("user.home");
        String rutaEscritorio = userHome + File.separator + "Downloads" + File.separator + "NETO_"+this.formateador.format(this.fecForm)+"_boleta_"+id+".txt";
        double value, neto, total_neto, total_final = 0;
        //crear texto plano de valores netos
        try(FileWriter fw = new FileWriter(rutaEscritorio, true);
            BufferedWriter bw = new BufferedWriter(fw);
            PrintWriter out = new PrintWriter(bw))
            {
                out.println("BOLETA NRO: "+id);
                out.println("Lista productos vendidos:");
                out.println();
                for(Detalle c: detalle){                     
                    value = c.getTotal_detalle() / c.getCantidad(); //valor individual
                    neto = Math.round(value / 1.19);
                    total_neto = Math.round(neto * c.getCantidad());
                    out.println(c.getNombreProd()+" - Valor neto: $"+this.formato.format(neto)+""
                            + " - Cantidad: "+this.formato.format(c.getCantidad())+" - Total Neto: $"+this.formato.format(total_neto));
                    total_final = total_final + total_neto;
                }
                
                out.println();
                out.print("TOTAL NETO: $"+this.formato.format(total_final));
                
                //out.close(); //no es necesario ya que estamos usando parametro en try
                
        } catch (IOException e) {
            //exception handling left as an exercise for the reader
            System.out.println("ERROR: "+e);
        }
    }
    
    public boolean createCSVListProduct(String cat, DefaultTableModel tb){
        //definiendo la ruta de descarga
        String userHome = System.getProperty("user.home");
        String rutaEscritorio = userHome + File.separator + "Downloads" + File.separator + this.formateador.format(this.fecForm)+"_Lista_"+cat+".csv";        
        //crear CSV con lista de productos segun CATEGORIA
        try(FileWriter fw = new FileWriter(rutaEscritorio, true);
            BufferedWriter bw = new BufferedWriter(fw);
            PrintWriter out = new PrintWriter(bw))
            {
                out.println("nro,Código,Producto,Valor,Cantidad_check");
                for(int i = 0; i < tb.getRowCount(); i++){
                    out.println((i+1)+","+tb.getValueAt(i, 1)+","+tb.getValueAt(i, 2)+","+tb.getValueAt(i, 3)+", ");
                }  
                return true;
            } catch (IOException e) {
            //exception handling left as an exercise for the reader
            System.out.println("ERROR: "+e);
            return false;
        }
    }
}
