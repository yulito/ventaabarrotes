/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cell;

/**
 *
 * @author yulia
 */
public interface TableActionEvent {
    public void onLess(int row);
    public void onPlus(int row);
    public void onRemove(int row);
}
