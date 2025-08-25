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
public class Categoria {
    
    private int id_categoria;
    private String categoria;
    
    public Categoria(){  }

    public int getId_categoria() {
        return id_categoria;
    }

    public void setId_categoria(int id_categoria) {
        this.id_categoria = id_categoria;
    }

    public String getCategoria() {
        return categoria.trim().toUpperCase();
    }

    public void setCategoria(String categoria) {
        this.categoria = categoria.trim().toUpperCase();
    }
    
    
}
