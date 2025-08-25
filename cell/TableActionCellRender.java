/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cell;

import java.awt.Component;
import javax.swing.JLabel;
import javax.swing.JTable;
import javax.swing.table.DefaultTableCellRenderer;

/**
 *
 * @author yulia
 */
public class TableActionCellRender extends DefaultTableCellRenderer {

    @Override
    public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected,
                                                   boolean hasFocus, int row, int column) {

        // Validamos que la fila y columna existan antes de renderizar
        if (row >= table.getRowCount() || column >= table.getColumnCount()) {
            return null;//new JLabel(); // o null si preferís no mostrar nada
        }

        // Si todo está bien, devolvemos el panel de acción
        return new PanelAction();
    }
}
