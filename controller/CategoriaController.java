/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controller;

import java.util.ArrayList;
import java.util.List;
import model.Categoria;
import model.DaoCategoria;

/**
 *
 * @author yulia
 */
public class CategoriaController {
    
    //private Categoria cat = new Categoria();
    private DaoCategoria daocat;
    private Categoria cat;
    
    public CategoriaController(){
       //inicializando los obj de las clases 
       daocat = new DaoCategoria();
       cat = new Categoria();
    }
    //funcion para obtener categorias o categoria (si es que viene un id)
    public List<Categoria> showCategory(int id){
        //creamos la instancia de un arreglo
        List<Categoria> categorias;
        //validar si hay o no id
        if (id == 0){
             categorias = daocat.getCategoria(1, false);
        }else{
            categorias = daocat.getCategoria(id, true);
        }                       
        return categorias;
    }
    
    //funcion insert
    public void insertCategory(String category){
        this.cat.setCategoria(category);
        this.daocat.insert(this.cat);
    }
    
    //funcion editar categoria
    public void editCategory(int id, String category){ //Categoria
        //validar datos
        this.cat.setId_categoria(id);
        this.cat.setCategoria(category);
        this.daocat.update(this.cat);
        
        //return obj;
    }    
}
