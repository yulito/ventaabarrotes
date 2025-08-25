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
public class Producto {
    private int id_producto;
    private String codigo;
    private String producto;
    private String unidad;
    private double stock;
    private double valor;
    private double descuento;
    private String imagen;
    private int id_categoria;
    private String namecat;
    
    public Producto(){  }

    public int getId_producto() {
        return id_producto;
    }

    public void setId_producto(int id_producto) {
        this.id_producto = id_producto;
    }

    public String getCodigo() {
        return codigo;
    }

    public void setCodigo(String codigo) {
        
        if (codigo != null) {
            this.codigo = codigo.trim().toUpperCase();
        } else {
            // valor por defecto
            this.codigo = ""; 
        }
    }

    public String getProducto() {
        return producto.trim().toUpperCase();
    }

    public void setProducto(String producto) {
        this.producto = producto.trim().toUpperCase();
    }

    public String getUnidad() {
        return unidad;
    }

    public void setUnidad(String unidad) {
        this.unidad = unidad;
    }

    public double getStock() {
        return stock;
    }

    public void setStock(double stock) {
        this.stock = stock;
    }

    public double getValor() {
        return valor;
    }

    public void setValor(double valor) {
        this.valor = valor;
    }

    public double getDescuento() {
        return descuento;
    }

    public void setDescuento(double descuento) {
        this.descuento = descuento;
    }

    public String getImagen() {
        return imagen;
    }

    public void setImagen(String imagen) {        
        if (imagen != null) {
            this.imagen = imagen;
        } else {
            // valor por defecto
            this.imagen = ""; 
        }
    }

    public int getId_categoria() {
        return id_categoria;
    }

    public void setId_categoria(int id_categoria) {
        this.id_categoria = id_categoria;
    }
    
    public String getNamecat() {
        return namecat;
    }
    public void setNamecat(String namecat){
        this.namecat = namecat;
    }
}
