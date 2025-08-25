/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package helper;

import java.io.File;
import java.io.IOException;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import model.Detalle;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.common.PDRectangle;
import org.apache.pdfbox.pdmodel.font.PDType1Font;

/**
 *
 * @author yulia
 */
public class PDFCreator {
    
    private DecimalFormat formato;
    
    public PDFCreator(){
        this.formato = new DecimalFormat("##,###.##");
    }
    
    public void create(List<Detalle> detalle ,double total, int id){
        
        //fecha
        DateFormat formateador= new SimpleDateFormat("dd-MM-yy"); //--d/mm/yyyy || d/mm/yy
        Date fecForm = new Date();
        //System.out.println(formateador.format(fecForm));
        
        PDDocument documento = null;
        try {
            documento = new PDDocument();
            PDPage pagina = new PDPage(PDRectangle.A6);
            documento.addPage(pagina);

            PDPageContentStream contenido = new PDPageContentStream(documento, pagina);

            // --- INICIO DE LÓGICA PARA MÚLTIPLES PÁGINAS ---
            float yPosition = pagina.getMediaBox().getHeight() - 20; // Posición Y inicial (arriba de la página)
            final float MARGEN_SUPERIOR = 20;
            final float MARGEN_INFERIOR = 40;
            final float ALTURA_DE_LINEA = 14.5f;

            contenido.beginText();
            contenido.setLeading(ALTURA_DE_LINEA);
            contenido.setFont(PDType1Font.TIMES_BOLD_ITALIC, 12);

            contenido.newLineAtOffset(20, yPosition);
            contenido.setNonStrokingColor(255,0,0); //rojo
            contenido.showText("Abarrotes Donde el Pelao");
            contenido.newLine();

            yPosition -= ALTURA_DE_LINEA; // Resta la altura del título
            contenido.newLineAtOffset(0, -ALTURA_DE_LINEA); // Mueve el cursor hacia abajo
            contenido.setFont(PDType1Font.TIMES_BOLD, 8);
            contenido.setNonStrokingColor(0,0,0); //negro                        
            
            contenido.showText("Las Guías, parcela 41-1B, Naltagua, Isla de Maipo");
            contenido.newLine();
            contenido.showText("Nro Boleta: "+id+"          Fecha: "+formateador.format(fecForm));
            contenido.newLine();
            
            contenido.setFont(PDType1Font.TIMES_ROMAN, 6);
            //CAMBIAR POR CURSOR
            int i = 1; //indice de producto
            for (Detalle cursor: detalle) { 
                // Verifica si hay suficiente espacio en la página
                if (yPosition < MARGEN_INFERIOR) {
                    // Cierra el contenido de la página actual
                    contenido.endText();
                    contenido.close();

                    // Crea una nueva página y la agrega al documento
                    pagina = new PDPage(PDRectangle.A6);
                    documento.addPage(pagina);

                    // Crea un nuevo stream de contenido para la nueva página
                    contenido = new PDPageContentStream(documento, pagina);

                    // Reinicia el texto en la nueva página
                    contenido.beginText();
                    contenido.setLeading(ALTURA_DE_LINEA);
                    contenido.setFont(PDType1Font.TIMES_ROMAN, 6);
                    yPosition = pagina.getMediaBox().getHeight() - MARGEN_SUPERIOR;
                    contenido.newLineAtOffset(20, yPosition);
                }

                if(cursor.getNombreProd().length() > 30){
                    contenido.showText(i+") " + cursor.getNombreProd().substring(0,30)+"..."
                            + "Cantidad: "+this.formato.format(cursor.getCantidad())+" - Total: $"+this.formato.format(cursor.getTotal_detalle()));
                }else{
                    contenido.showText(i+") " + cursor.getNombreProd()+" - "
                            + "Cantidad: "+this.formato.format(cursor.getCantidad())+" - Total: $"+this.formato.format(cursor.getTotal_detalle()));
                }
                contenido.newLine();
                i++;
                yPosition -= ALTURA_DE_LINEA; // Actualiza la posición Y
            }
            
            contenido.setFont(PDType1Font.TIMES_BOLD, 8);
            contenido.setNonStrokingColor(0,0,0);
            //mostrar total de venta
            String totalFormateado = "TOTAL COMPRA: $"+String.valueOf(this.formato.format(total));
            contenido.showText(totalFormateado);

            // Cierra el último stream de contenido
            contenido.endText();
            contenido.close();

            // --- FIN DE LÓGICA PARA MÚLTIPLES PÁGINAS ---

            // Obtiene la ruta del escritorio de forma dinámica
            String userHome = System.getProperty("user.home");
            String rutaEscritorio = userHome + File.separator + "Downloads" + File.separator + formateador.format(fecForm)+"_boleta_"+id+".pdf";

            documento.save(rutaEscritorio);
            System.out.println("PDF guardado con éxito en: " + rutaEscritorio);

        } catch (IOException ex) {
            Logger.getLogger(PDFCreator.class.getName()).log(Level.SEVERE, "Error al crear el PDF", ex);
        } finally {
            if (documento != null) {
                try {
                    documento.close();
                    System.out.println("Documento PDF cerrado.");
                } catch (IOException e) {
                    Logger.getLogger(PDFCreator.class.getName()).log(Level.SEVERE, "Error al cerrar el documento", e);
                }
            }
        }
        
    }
}
