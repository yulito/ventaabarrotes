/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package view;

import cell.TableActionCellEditor;
import cell.TableActionCellRender;
import cell.TableActionEvent;
import controller.ProductoController;
import controller.VentaController;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.io.File;
import java.net.URL;
import java.sql.SQLException;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.ImageIcon;
import javax.swing.JOptionPane;
import javax.swing.Timer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;
import model.Detalle;
import model.Producto;

/**
 *
 * @author yulia
 */
public class Ventas extends javax.swing.JFrame {

    /**
     * Creates new form Ventas
     */
    // 1. Declarar el temporizador
    private Timer searchTimer;
    private double totalPagar = 0;
    // Usar el Locale para Chile (es-CL)
    public NumberFormat formatoMoneda;
    public String totalFormateado;
    
    public Ventas() {
        initComponents();
        this.setLocationRelativeTo(null);
        this.setResizable(false);
        this.setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
                
        formatoMoneda = NumberFormat.getInstance(new Locale("es", "CL"));
        // Inicializa el temporizador. El segundo parámetro es el retraso en milisegundos.
        // El ActionListener es la lógica que se ejecutará cuando el temporizador termine.
        searchTimer = new Timer(300, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Esta lógica se ejecuta solo cuando el usuario deja de escribir por 300ms
                performProductSearchAndPrint();
            }
            
        });
        
        // No queremos que el temporizador se repita automáticamente
        searchTimer.setRepeats(false);
        
        TableActionEvent event = new TableActionEvent(){
            @Override
            public void onLess(int row) {                
                if(saleListTable.isEditing()){
                    saleListTable.getCellEditor().stopCellEditing();
                }
                DefaultTableModel tb =(DefaultTableModel)saleListTable.getModel();
                //primero obtenemos la cantidad y el total, para luego dividir y obtener
                //el valor por unidad. Ese valor lo enviamos para luego realizar la resta
                Number cantidad = (Number) tb.getValueAt(row, 2);
                Number total = (Number) tb.getValueAt(row, 3);

                if(cantidad.doubleValue() > 1){
                    double valorUnidad = total.doubleValue() / cantidad.doubleValue();
                    double valorResta = total.doubleValue() - valorUnidad;
                    //restamos 1 a la cantidad
                    cantidad = cantidad.doubleValue() - 1;
                    //agregamos los valores a la fila
                    tb.setValueAt(cantidad, row, 2);
                    tb.setValueAt(valorResta, row, 3);
                    //formateamos el label de total (con valor unidad ya que solo restamos de un valor)
                    formValueTable(valorUnidad, 1);
                }
            }

            @Override
            public void onPlus(int row) {
                if(saleListTable.isEditing()){
                    saleListTable.getCellEditor().stopCellEditing();
                }
                DefaultTableModel tb =(DefaultTableModel)saleListTable.getModel();
                //obtenemos valor unitario y luego realizamos la suma
                Number cantidad = (Number) tb.getValueAt(row, 2);
                Number total = (Number) tb.getValueAt(row, 3);

                if(cantidad.doubleValue() >= 1){
                    double valorUnidad = total.doubleValue() / cantidad.doubleValue();
                    double valorSuma = total.doubleValue() + valorUnidad;
                    //sumamos 1 a la cantidad
                    cantidad = cantidad.doubleValue() + 1;
                    //agregamos los valores a la fila
                    tb.setValueAt(cantidad, row, 2);
                    tb.setValueAt(valorSuma, row, 3);
                    //formateamos el label de total (con valor unidad ya que sumamos de un valor)
                    formValueTable(valorUnidad, 2);
                }
            }

            @Override
            public void onRemove(int row) {
                if(saleListTable.isEditing()){
                    saleListTable.getCellEditor().stopCellEditing();
                }
                DefaultTableModel tb =(DefaultTableModel)saleListTable.getModel();
                //obtenemos el valor total de la fila
                Number total = (Number) tb.getValueAt(row, 3);
                double valorObtenido = total.doubleValue();
                formValueTable(valorObtenido, 3);
                tb.removeRow(row);                
            }
        
        };
        
        //le decimos que el campo o columna 4 agregue el objeto que contiene el diseño de botones
        saleListTable.getColumnModel().getColumn(4).setCellRenderer(new TableActionCellRender());
        //esta permite editar la celda que contiene el panel con los botones
        saleListTable.getColumnModel().getColumn(4).setCellEditor(new TableActionCellEditor(event));
    }
    
    public void formValueTable(double value, int option){
        
        switch(option){
            case 1:                
                //modificamos restando valor individual de la fila
                this.totalPagar = this.totalPagar - value;
                this.totalFormateado = formatoMoneda.format(this.totalPagar);
                totalLabel.setText(String.valueOf(this.totalFormateado));
                break;                 
            case 2:                
                //modificamos sumando valor individual de la fila
                this.totalPagar = this.totalPagar + value;
                this.totalFormateado = formatoMoneda.format(this.totalPagar);
                totalLabel.setText(String.valueOf(this.totalFormateado));
                break;
            case 3:                
                //modificamos restando valor del total de la fila
                this.totalPagar = this.totalPagar - value;
                this.totalFormateado = formatoMoneda.format(this.totalPagar);
                totalLabel.setText(String.valueOf(this.totalFormateado));
                break;            
        }
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        jPanel2 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        searchProduct = new javax.swing.JTextField();
        checkBox = new javax.swing.JCheckBox();
        jLabel11 = new javax.swing.JLabel();
        jPanel3 = new javax.swing.JPanel();
        nameLabel = new javax.swing.JLabel();
        jPanel4 = new javax.swing.JPanel();
        photoprod = new javax.swing.JLabel();
        jPanel5 = new javax.swing.JPanel();
        jLabel4 = new javax.swing.JLabel();
        stockLabel = new javax.swing.JLabel();
        jPanel6 = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        saleListTable = new javax.swing.JTable();
        jPanel7 = new javax.swing.JPanel();
        jLabel5 = new javax.swing.JLabel();
        valueLabel = new javax.swing.JLabel();
        jPanel8 = new javax.swing.JPanel();
        jLabel8 = new javax.swing.JLabel();
        descLabel = new javax.swing.JLabel();
        jLabel10 = new javax.swing.JLabel();
        jPanel9 = new javax.swing.JPanel();
        jLabel12 = new javax.swing.JLabel();
        jLabel13 = new javax.swing.JLabel();
        totalLabel = new javax.swing.JLabel();
        btnAddList = new javax.swing.JButton();
        jPanel10 = new javax.swing.JPanel();
        btnPdf = new javax.swing.JButton();
        jLabel3 = new javax.swing.JLabel();
        cboxPay = new javax.swing.JComboBox<>();
        codText = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        jPanel1.setBackground(new java.awt.Color(255, 255, 255));

        jLabel1.setFont(new java.awt.Font("Arial", 3, 18)); // NOI18N
        jLabel1.setText("Buscar Producto:");

        searchProduct.setFont(new java.awt.Font("Arial", 1, 18)); // NOI18N
        searchProduct.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                searchProductKeyReleased(evt);
            }
        });

        checkBox.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        checkBox.setText("Código");

        jLabel11.setFont(new java.awt.Font("Arial", 3, 18)); // NOI18N
        jLabel11.setForeground(new java.awt.Color(153, 0, 0));
        jLabel11.setText("Sistema de Ventas");

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addComponent(jLabel1)
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel2Layout.createSequentialGroup()
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(checkBox))
                            .addGroup(jPanel2Layout.createSequentialGroup()
                                .addGap(142, 142, 142)
                                .addComponent(jLabel11, javax.swing.GroupLayout.PREFERRED_SIZE, 168, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(0, 278, Short.MAX_VALUE))))
                    .addComponent(searchProduct))
                .addContainerGap())
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGap(29, 29, 29)
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(checkBox)))
                    .addComponent(jLabel11, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(searchProduct, javax.swing.GroupLayout.DEFAULT_SIZE, 33, Short.MAX_VALUE)
                .addContainerGap())
        );

        nameLabel.setFont(new java.awt.Font("Arial", 1, 18)); // NOI18N
        nameLabel.setText("--");

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(nameLabel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(nameLabel, javax.swing.GroupLayout.DEFAULT_SIZE, 27, Short.MAX_VALUE)
                .addContainerGap())
        );

        jPanel4.setBackground(new java.awt.Color(0, 0, 0));

        javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
        jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(photoprod, javax.swing.GroupLayout.DEFAULT_SIZE, 220, Short.MAX_VALUE)
                .addContainerGap())
        );
        jPanel4Layout.setVerticalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(photoprod, javax.swing.GroupLayout.DEFAULT_SIZE, 176, Short.MAX_VALUE)
                .addContainerGap())
        );

        jLabel4.setFont(new java.awt.Font("Arial", 1, 18)); // NOI18N
        jLabel4.setText("Stock:");

        stockLabel.setFont(new java.awt.Font("Arial", 1, 18)); // NOI18N
        stockLabel.setForeground(new java.awt.Color(0, 51, 255));
        stockLabel.setText("--");

        javax.swing.GroupLayout jPanel5Layout = new javax.swing.GroupLayout(jPanel5);
        jPanel5.setLayout(jPanel5Layout);
        jPanel5Layout.setHorizontalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel4, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGap(31, 31, 31)
                .addComponent(stockLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 115, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );
        jPanel5Layout.setVerticalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel4, javax.swing.GroupLayout.DEFAULT_SIZE, 28, Short.MAX_VALUE)
                    .addComponent(stockLabel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );

        saleListTable.setFont(new java.awt.Font("Arial", 1, 18)); // NOI18N
        saleListTable.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Código", "Producto", "Cantidad", "Total", "Acción"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.String.class, java.lang.String.class, java.lang.Double.class, java.lang.Double.class, java.lang.Object.class
            };
            boolean[] canEdit = new boolean [] {
                false, false, false, false, true
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        saleListTable.setRowHeight(30);
        jScrollPane1.setViewportView(saleListTable);
        if (saleListTable.getColumnModel().getColumnCount() > 0) {
            saleListTable.getColumnModel().getColumn(1).setPreferredWidth(380);
            saleListTable.getColumnModel().getColumn(2).setPreferredWidth(40);
            saleListTable.getColumnModel().getColumn(4).setPreferredWidth(60);
        }

        javax.swing.GroupLayout jPanel6Layout = new javax.swing.GroupLayout(jPanel6);
        jPanel6.setLayout(jPanel6Layout);
        jPanel6Layout.setHorizontalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane1)
        );
        jPanel6Layout.setVerticalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel6Layout.createSequentialGroup()
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, Short.MAX_VALUE))
        );

        jLabel5.setFont(new java.awt.Font("Arial", 1, 18)); // NOI18N
        jLabel5.setText("Valor:      $");

        valueLabel.setFont(new java.awt.Font("Arial", 1, 18)); // NOI18N
        valueLabel.setForeground(new java.awt.Color(0, 102, 0));
        valueLabel.setText("--");

        javax.swing.GroupLayout jPanel7Layout = new javax.swing.GroupLayout(jPanel7);
        jPanel7.setLayout(jPanel7Layout);
        jPanel7Layout.setHorizontalGroup(
            jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel7Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel5, javax.swing.GroupLayout.DEFAULT_SIZE, 96, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(valueLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 113, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );
        jPanel7Layout.setVerticalGroup(
            jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel7Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel5, javax.swing.GroupLayout.DEFAULT_SIZE, 28, Short.MAX_VALUE)
                    .addComponent(valueLabel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );

        jLabel8.setFont(new java.awt.Font("Arial", 1, 18)); // NOI18N
        jLabel8.setText("Descuento:");

        descLabel.setFont(new java.awt.Font("Arial", 1, 18)); // NOI18N
        descLabel.setForeground(new java.awt.Color(51, 51, 255));
        descLabel.setText("0,0");

        jLabel10.setFont(new java.awt.Font("Arial", 1, 18)); // NOI18N
        jLabel10.setText("%");

        javax.swing.GroupLayout jPanel8Layout = new javax.swing.GroupLayout(jPanel8);
        jPanel8.setLayout(jPanel8Layout);
        jPanel8Layout.setHorizontalGroup(
            jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel8Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel8)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(descLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 36, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jLabel10)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel8Layout.setVerticalGroup(
            jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel8Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel8, javax.swing.GroupLayout.DEFAULT_SIZE, 28, Short.MAX_VALUE)
                    .addComponent(descLabel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jLabel10, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );

        jLabel12.setFont(new java.awt.Font("Arial", 1, 24)); // NOI18N
        jLabel12.setText("TOTAL VENTA:");

        jLabel13.setFont(new java.awt.Font("Arial", 1, 36)); // NOI18N
        jLabel13.setText("$");

        totalLabel.setFont(new java.awt.Font("Arial", 1, 36)); // NOI18N
        totalLabel.setText("--");

        btnAddList.setFont(new java.awt.Font("Arial", 1, 18)); // NOI18N
        btnAddList.setText("Agregar a la Lista");
        btnAddList.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnAddListActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel9Layout = new javax.swing.GroupLayout(jPanel9);
        jPanel9.setLayout(jPanel9Layout);
        jPanel9Layout.setHorizontalGroup(
            jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel9Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel12, javax.swing.GroupLayout.DEFAULT_SIZE, 301, Short.MAX_VALUE)
                    .addGroup(jPanel9Layout.createSequentialGroup()
                        .addComponent(jLabel13)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(totalLabel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addComponent(btnAddList, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );
        jPanel9Layout.setVerticalGroup(
            jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel9Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(btnAddList, javax.swing.GroupLayout.PREFERRED_SIZE, 51, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jLabel12, javax.swing.GroupLayout.PREFERRED_SIZE, 60, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addGroup(jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(totalLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 60, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel13, javax.swing.GroupLayout.PREFERRED_SIZE, 60, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(49, 49, 49))
        );

        btnPdf.setBackground(new java.awt.Color(255, 255, 0));
        btnPdf.setFont(new java.awt.Font("Arial", 3, 18)); // NOI18N
        btnPdf.setText("Boleta PDF");
        btnPdf.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnPdfActionPerformed(evt);
            }
        });

        jLabel3.setFont(new java.awt.Font("Arial", 1, 14)); // NOI18N
        jLabel3.setText("Medio de pago");

        cboxPay.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        cboxPay.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Efectivo", "Debito/credito", "Transferencia" }));

        javax.swing.GroupLayout jPanel10Layout = new javax.swing.GroupLayout(jPanel10);
        jPanel10.setLayout(jPanel10Layout);
        jPanel10Layout.setHorizontalGroup(
            jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel10Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(btnPdf, javax.swing.GroupLayout.DEFAULT_SIZE, 138, Short.MAX_VALUE)
                    .addGroup(jPanel10Layout.createSequentialGroup()
                        .addGap(16, 16, 16)
                        .addComponent(jLabel3)
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addComponent(cboxPay, 0, 0, Short.MAX_VALUE))
                .addContainerGap())
        );
        jPanel10Layout.setVerticalGroup(
            jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel10Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel3, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(cboxPay, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(btnPdf, javax.swing.GroupLayout.PREFERRED_SIZE, 80, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(31, 31, 31))
        );

        codText.setText("...");

        jLabel2.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        jLabel2.setForeground(new java.awt.Color(255, 0, 0));
        jLabel2.setText("Código:");

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(10, 10, 10)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jPanel6, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(jPanel4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jPanel5, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jPanel7, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jPanel8, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                                .addComponent(jLabel2)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(codText, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                        .addComponent(jPanel9, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jPanel10, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                .addContainerGap())
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(jPanel4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(codText, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addComponent(jLabel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jPanel5, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jPanel7, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jPanel8, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jPanel9, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jPanel10, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jPanel6, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                .addContainerGap())
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void searchProductKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_searchProductKeyReleased
        //System.out.println(searchProduct.getText());
        
        if (!checkBox.isSelected()) {
            
            // Si la tecla es ENTER, hacemos la búsqueda inmediatamente
            if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
                 searchTimer.stop(); // Detenemos cualquier temporizador pendiente
                 // Llamamos al método que realiza la búsqueda y el autocompletado.
                // Este método ahora también se encargará de imprimir los detalles del producto.
                performProductSearchAndPrint(); 
                return;
            }
            
            // Si es backspace o delete, no hacemos nada y simplemente reiniciamos el timer
            if (evt.getKeyCode() == KeyEvent.VK_BACK_SPACE || evt.getKeyCode() == KeyEvent.VK_DELETE) {
                // Puedes optar por no reiniciar el timer o reiniciarlo.
                // En este ejemplo, lo reiniciamos para que el autocompletado se active después
                searchTimer.restart();
                return;
            }

            // Si es cualquier otra tecla, reiniciamos el temporizador.
            // Esto detiene el temporizador si ya estaba corriendo y lo vuelve a iniciar.
            searchTimer.restart();

        } else {
            // Lógica para el checkbox seleccionado
            if (evt.getKeyCode() == KeyEvent.VK_ENTER) {                
                ProductoController ctrl = new ProductoController();
                List<Producto> obj = ctrl.mostrarProducto(0, searchProduct.getText().toString(), "");               
                for (Producto cursor : obj){
                    //System.out.println("ENTER: "+cursor.getProducto());
                    PaintLabelsWithValues(cursor);
                }                
            }
        }
    }//GEN-LAST:event_searchProductKeyReleased

    private void btnAddListActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAddListActionPerformed
        //validar si tenemos producto seleccionado
        if(nameLabel.getText().isEmpty() || nameLabel.getText().equalsIgnoreCase("--")){
            JOptionPane.showMessageDialog(this, "No has seleccionado ningun producto para agregar.");
        }else{
            //llamamos al modelo de la tabla
            DefaultTableModel tb =(DefaultTableModel)saleListTable.getModel();            
            
            //filas existentes:
            int rowCount = tb.getRowCount();
            //si es distinto a "0" verificar si existe dato en la fila y actualizar valores en caso de ser cierto
            if(rowCount != 0){
                //poner un contador de filas modificadas
                int filasModificadas = 0 ;
                String nombreProducto;
                this.totalPagar = 0; //-->importante ya que la recorrer los valores totales lo haremos desde 0 y no desde el valor que ya seteamos anteriormente.
                for(int i = 0; i < rowCount; i++){
                    nombreProducto = (String)tb.getValueAt(i, 1);
                    //comparamos el nombre de producto
                    if(nombreProducto.equalsIgnoreCase(nameLabel.getText())){
                        System.out.println("HAY COINCIDENCIA Y MODIFICAMOS LA FILA");
                        //Modificamos los valores de la fila
                        Number cantidadNumber = (Number) tb.getValueAt(i, 2);
                        double cantidad = cantidadNumber.doubleValue() + 1;
                        double valor = Double.parseDouble(valueLabel.getText()) * cantidad;
                        double total = valor -((valor * Double.parseDouble(descLabel.getText()))/100);
                        tb.setValueAt(total, i, 3);
                        tb.setValueAt(cantidad, i, 2);                                               
                        //agregamos al contador de filas modificas
                        filasModificadas++;
                    }
                    System.out.println("indice: "+i);
                    //MODIFICAR TOTALPAGAR                    
                    this.totalPagar = (this.totalPagar + (double)tb.getValueAt(i, 3));
                }
                //modificar totalLabel               
                // Formatear el número
                this.totalFormateado = formatoMoneda.format(this.totalPagar);
                totalLabel.setText(String.valueOf(this.totalFormateado));
                //si el contador es "0" filas modificadas, agregar nueva fila.
                if(filasModificadas == 0){
                    System.out.println("Agregamos nueva fila");
                    //pero primero calcular el valor si es que hay descuento
                    double total = Double.parseDouble(valueLabel.getText()) -((Double.parseDouble(valueLabel.getText()) * Double.parseDouble(descLabel.getText()))/100);       
                    //ingresar fila (en caso de que no exista el producto en la fila)
                    
                    tb.addRow(new Object[]{codText.getText(), nameLabel.getText(), 1, total, });
                    
                    //modificamos el label de total a pagar:
                    this.totalPagar = this.totalPagar + total; 
                    
                    // Formatear el número
                    this.totalFormateado = formatoMoneda.format(this.totalPagar);
                    totalLabel.setText(String.valueOf(this.totalFormateado));
                }
            }else{
                //pero primero calcular el valor si es que hay descuento
                double total = Double.parseDouble(valueLabel.getText()) -((Double.parseDouble(valueLabel.getText()) * Double.parseDouble(descLabel.getText()))/100);       
                //ingresar fila (en caso de que no exista el producto en la fila)
                tb.addRow(new Object[]{codText.getText(), nameLabel.getText(), 1, total, });        
                //modificamos el label de total a pagar:
                this.totalPagar = this.totalPagar + total; 
                
                // Formatear el número
                this.totalFormateado = formatoMoneda.format(this.totalPagar);
                totalLabel.setText(String.valueOf(this.totalFormateado));
            }
            //Limpiar panel de seleccion. Podemos dejarlo asi sin limpiar.
            
        }
    }//GEN-LAST:event_btnAddListActionPerformed

    private void btnPdfActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnPdfActionPerformed
        //creamos arreglo para guardar datos de lista formato modelo 
        List<Detalle>listaVenta = new ArrayList<>();
        //obtenemos los datos de la tabla
        DefaultTableModel tb = (DefaultTableModel)saleListTable.getModel();
        int contador = tb.getRowCount();
        for (int i = 0; i < contador; i++){
            // debemos de cambiar los valores numericos en double           
            Object val2 = tb.getValueAt(i, 2);
            Object val3 = tb.getValueAt(i, 3);

            double precio = (val2 instanceof Integer) ? ((Integer) val2).doubleValue() : (Double) val2;
            double cantidad = (val3 instanceof Integer) ? ((Integer) val3).doubleValue() : (Double) val3;

            //agregamos los productos seleccionados al arreglo
            listaVenta.add(new Detalle(tb.getValueAt(i, 1).toString(), precio, cantidad));
        }
        //enviar arreglo, total y medio de pago al controlador
        String pago = cboxPay.getSelectedItem().toString();
        VentaController vc = new VentaController();
        try {
            //registramos la venta enviando: arreglo con lista de productos, total y medio pago
            vc.registrarVenta(listaVenta, this.totalPagar, pago);
            //limpiar lista de detalles, todos los labels y el combobox de medio de pago
            cleanAll();
            //mensaje de exito
            JOptionPane.showMessageDialog(null,"Venta realizada correctamente!. ");
        } catch (SQLException ex) {
            Logger.getLogger(Ventas.class.getName()).log(Level.SEVERE, null, ex);
            JOptionPane.showMessageDialog(null,"ERROR: "+ex);
        }
    }//GEN-LAST:event_btnPdfActionPerformed

    public void cleanAll(){
        //limpiar tabla
        DefaultTableModel model = (DefaultTableModel) saleListTable.getModel();
        int rowCount = model.getRowCount();
        // Eliminar filas de atrás hacia adelante para evitar errores de índice
        for (int i = rowCount - 1; i >= 0; i--) {
            model.removeRow(i);
        }
        //limpiar labels
        nameLabel.setText("--");
        codText.setText("...");
        stockLabel.setText("--");
        valueLabel.setText("--");
        descLabel.setText("0,0");
        totalLabel.setText("--");
        //checkbox
        //checkBox.setSelected(false); //no es necesario ya que es preferible manual
        //listItems
        cboxPay.setSelectedItem("Efectivo");
        //busqueda
        searchProduct.setText("");
        //imagen
        photoprod.setIcon(null);
        //total
        this.totalPagar = 0;
    }
    
    private void performProductSearchAndPrint() {
    
    String searchText = searchProduct.getText();
    
    if (searchText.isEmpty()) {
        System.out.println("No hay texto para buscar.");
        return;
    }

    ProductoController ctrl = new ProductoController();
    List<Producto> productoList = ctrl.mostrarProducto(0, "", searchText);

    String to_check = searchText.toUpperCase();
    int to_check_len = to_check.length();

    // Lógica para el autocompletado y la impresión
    boolean foundMatch = false; // Variable para saber si encontramos una coincidencia
    for (Producto data : productoList) {
        String productText = data.getProducto();
        
        if (productText.toUpperCase().startsWith(to_check)) {
            // Autocompletado del campo de texto
            searchProduct.setText(productText);
            searchProduct.setSelectionStart(to_check_len);
            searchProduct.setSelectionEnd(productText.length());
            
            //pintamos los valores en los Labels
            PaintLabelsWithValues(data);
            
            foundMatch = true;
            break; // Salimos del bucle después de encontrar la primera coincidencia
        }
    }
    
    if (!foundMatch) {
        System.out.println("No se encontró ninguna coincidencia para autocompletar.");
    }
}
    public void PaintLabelsWithValues(Producto data){
         // Pintamos los detalles del producto autocompletado o codigo                        
            nameLabel.setText(data.getProducto());
            codText.setText(data.getCodigo());
            stockLabel.setText(String.valueOf(data.getStock()));
            valueLabel.setText(String.valueOf(data.getValor()));
            descLabel.setText(String.valueOf(data.getDescuento()));
            //añadimos la imagen photoprod
            loadImage(data.getImagen());           
    }
     
     public void loadImage(String urlImage){     
         //agregar imagen en la vista (por defecto o la que viene de la base de datos)
        if(urlImage.isEmpty() || urlImage == null){ 
            URL imageUrl = getClass().getResource(File.separator +"img"+ File.separator +"preg.png");//ruta relativa             
            Image imagen = new ImageIcon(imageUrl).getImage();
            ImageIcon mIcon = new ImageIcon(imagen.getScaledInstance(photoprod.getWidth(), photoprod.getHeight(), 0));
            //agregamos la imagen al label icono
            photoprod.setIcon(mIcon);
        }else{
            // Obtener la ruta del directorio de trabajo del proyecto (Proyecto/)
            String directorioRaiz = System.getProperty("user.dir");

            // Construir la ruta completa al archivo de imagen en la carpeta storage
            // Esto crea una ruta como "Proyecto/storage/imagen.png"
            String rutaCompletaImagen = directorioRaiz + File.separator + urlImage; 

            // Verificar si el archivo existe
            File archivoImagen = new File(rutaCompletaImagen);

            if (archivoImagen.exists()) {
                // Cargar la imagen y redimensionarla
                ImageIcon iconoOriginal = new ImageIcon(rutaCompletaImagen);
                Image imagen = iconoOriginal.getImage();

                // Asumiendo que tienes un JLabel llamado imageText
                Image imagenRedimensionada = imagen.getScaledInstance(photoprod.getWidth(), photoprod.getHeight(), Image.SCALE_SMOOTH);
                ImageIcon iconoFinal = new ImageIcon(imagenRedimensionada);

                photoprod.setIcon(iconoFinal);
            } else {
                // Si la imagen de storage no se encuentra, carga la imagen por defecto
                // en el classpath (src/img/preg.png)
                try {
                    Image imagenDefecto = new ImageIcon(getClass().getResource( File.separator +"img"+ File.separator +"preg.png")).getImage();
                    ImageIcon iconoDefecto = new ImageIcon(imagenDefecto.getScaledInstance(photoprod.getWidth(), photoprod.getHeight(), Image.SCALE_SMOOTH));
                    photoprod.setIcon(iconoDefecto);
                } catch (Exception e) {
                    System.err.println("No se pudo cargar la imagen por defecto.");
                }
            }
        }
     }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnAddList;
    private javax.swing.JButton btnPdf;
    private javax.swing.JComboBox<String> cboxPay;
    private javax.swing.JCheckBox checkBox;
    private javax.swing.JLabel codText;
    private javax.swing.JLabel descLabel;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel13;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel10;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JPanel jPanel6;
    private javax.swing.JPanel jPanel7;
    private javax.swing.JPanel jPanel8;
    private javax.swing.JPanel jPanel9;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JLabel nameLabel;
    private javax.swing.JLabel photoprod;
    private javax.swing.JTable saleListTable;
    private javax.swing.JTextField searchProduct;
    private javax.swing.JLabel stockLabel;
    private javax.swing.JLabel totalLabel;
    private javax.swing.JLabel valueLabel;
    // End of variables declaration//GEN-END:variables
}
