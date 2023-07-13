/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.firebase;
import com.mycompany.firebase.CRUD.uploaderImg;

public class Principal {

    public static void main(String[] args) {
        // conexion.Conectar();

        uploaderImg.uploader("fir-java-dbb1c.appspot.com", "peliculas.jpg", "peliculas.jpg", "image/jpeg");
    }
}
