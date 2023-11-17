package com.mycompany.firebase.tareasSegundoPlano;

import javax.swing.JTable;

import com.google.cloud.firestore.DocumentReference;
import com.google.cloud.firestore.Firestore;
import com.google.firebase.cloud.FirestoreClient;
import com.mycompany.firebase.CRUD.CRUDFireStore;
import com.mycompany.firebase.conection.conexion;

public class DataUpdaterListProducts extends Thread {
    private int i = 1;
    CRUDFireStore firestore=new CRUDFireStore();

    public void agregarObjetos(JTable tabla){
        conexion.getConexion();
        
        Firestore db=FirestoreClient.getFirestore();
        DocumentReference docRef=db.collection("products").document("");
        
    }
    
    @Override
    public void run() {
        try {
            while (i != 0) {
                Thread.sleep(3000);
                System.out.println("acciones funcionando en segundo plano");
            }
            System.out.println("hilo finalizado");
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
    /**
     * Finaliza el hilo de la clase DataUpdaterListProducts
     */
    public void Finalizar(){
        this.i=0;
    }

}
