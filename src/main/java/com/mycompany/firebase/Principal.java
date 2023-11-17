/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.firebase;

import javax.swing.UIManager;


import com.mycompany.firebase.views.listProducts;

/**
 * @apiNote si la aplicacion no corre talvez se deba que falta la carpeta target
 *          o le falta algo a la carpeta target(creo que es donde estan las
 *          clases compiladas aun estoy investigando), en ese caso atravez de la
 *          consola/terminal de tu IDE escribir el comando "mvn clean
 *          install"(sin las comillas)(esto borra la carpeta target y la crea
 *          denuevo con los recursos necesarios)
 */
public class Principal {

    public static void main(String[] args) {
        
        
         try {
         // Establecer el Look and Feel que prefieras (por ejemplo, Nimbus)
         UIManager.setLookAndFeel("javax.swing.plaf.nimbus.NimbusLookAndFeel");
         } catch (Exception e) {
         e.printStackTrace();
         }
         new listProducts().setVisible(true);
        

        /*
         * conexion.Conectar();
         * try {
         * Date date=FirestoreClient.getFirestore().collection("products").document(
         * "cb:423424234,435232343,").get().get().getUpdateTime().toDate();
         * Date date2=FirestoreClient.getFirestore().collection("products").document(
         * "cb:4234242341,4352323432,").get().get().getUpdateTime().toDate();
         * boolean b=date.toString().equals(date2.toString());
         * System.out.println("date: "+date);
         * System.out.println("date: "+date2);
         * System.out.println("hora sistema: "+Calendar.getInstance().getTime());
         * } catch (Exception e) {
         * System.out.println("ERROR: "+e.getMessage());
         * }
         */

    }

}
