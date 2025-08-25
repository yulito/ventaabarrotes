/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package main;

import view.Home;

/**
 *
 * @author yulia
 */
public class App {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {    
        //ejecutamos el programa para abrir el menu de home
        Home home = new Home();
        home.setVisible(true);
    }
}