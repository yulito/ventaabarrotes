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
public class Detalle {
    private int id_detalle;
    private double cantidad;
    private double total_detalle;
    private int id_venta;
    private int id_producto;
    private String nombreProd;
    private Producto prod;
    
    public Detalle(){ 
        this.prod = new Producto();
    }
    
    public Detalle(String nombreProd, double cantidad, double total_detalle){
        this.nombreProd = nombreProd;
        this.cantidad = cantidad;
        this.total_detalle = total_detalle;
    }

    public int getId_detalle() {
        return id_detalle;
    }

    public void setId_detalle(int id_detalle) {
        this.id_detalle = id_detalle;
    }

    public double getCantidad() {
        return cantidad;
    }

    public void setCantidad(double cantidad) {
        this.cantidad = cantidad;
    }

    public double getTotal_detalle() {
        return total_detalle;
    }

    public void setTotal_detalle(double total_detalle) {
        this.total_detalle = total_detalle;
    }

    public int getId_venta() {
        return id_venta;
    }

    public void setId_venta(int id_venta) {
        this.id_venta = id_venta;
    }

    public int getId_producto() {
        return id_producto;
    }

    public void setId_producto(int id_producto) {
        this.id_producto = id_producto;
    }
    
    public String getNombreProd(){
        return nombreProd;
    }
    
    public Producto getProd(){
        return prod;
    }
}
