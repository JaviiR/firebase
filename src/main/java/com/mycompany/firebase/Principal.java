/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.firebase;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.swing.JOptionPane;
import javax.swing.UIManager;

import com.google.api.core.ApiFuture;
import com.google.cloud.firestore.DocumentSnapshot;
import com.google.firebase.cloud.FirestoreClient;
import com.google.firebase.cloud.StorageClient;
import com.mycompany.firebase.CRUD.CRUDStorage;
import com.mycompany.firebase.conection.conexion;
import com.mycompany.firebase.views.listProducts;

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
