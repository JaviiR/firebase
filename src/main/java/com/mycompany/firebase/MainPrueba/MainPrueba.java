package com.mycompany.firebase.MainPrueba;

import com.google.cloud.firestore.CollectionReference;
import com.google.cloud.firestore.DocumentChange;
import com.google.cloud.firestore.Firestore;
import com.google.cloud.firestore.ListenerRegistration;
import com.google.firebase.cloud.FirestoreClient;
import com.mycompany.firebase.conection.conexion;

public class MainPrueba {
    public static void main(String[] args) {
        int i = 1;
        conexion.getConexion();
        Firestore db = FirestoreClient.getFirestore();

        CollectionReference docRef = db.collection("products");

        // Registrar un listener para cambios en el documento
        ListenerRegistration reg = docRef.addSnapshotListener((snapshots, e) -> {
            if (e != null) {
                System.err.println("Error al escuchar cambios: " + e);
                return;
            }

            if (snapshots != null) {
                for (DocumentChange change : snapshots.getDocumentChanges()) {
                    switch (change.getType()) {
                        case ADDED:
                            System.out.println("-----------------Elemento agregado:" + change.getDocument().getId()
                                    + "---------------");
                            System.out.println("IMG: " + change.getDocument().getString("img"));
                            System.out.println("NOMBRE: " + change.getDocument().getString("nombre"));
                            System.out.println("PRECIO: " + change.getDocument().get("precio"));
                            System.out.println("STOCK: " + change.getDocument().get("stock"));
                            System.out.println("-------------------------------------------------");
                            break;
                        case MODIFIED:
                            System.out.println("-----------------Elemento actualizado:" + change.getDocument().getId()
                                    + "---------------");
                            System.out.println("IMG: " + change.getDocument().getString("img"));
                            System.out.println("NOMBRE: " + change.getDocument().getString("nombre"));
                            System.out.println("PRECIO: " + change.getDocument().get("precio"));
                            System.out.println("STOCK: " + change.getDocument().get("stock"));
                            System.out.println("-------------------------------------------------");
                            break;
                        case REMOVED:
                            System.out.println("-----------------Elemento eliminado:" + change.getDocument().getId()
                                    + "---------------");
                            System.out.println("IMG: " + change.getDocument().getString("img"));
                            System.out.println("NOMBRE: " + change.getDocument().getString("nombre"));
                            System.out.println("PRECIO: " + change.getDocument().get("precio"));
                            System.out.println("STOCK: " + change.getDocument().get("stock"));
                            System.out.println("-------------------------------------------------");
                            break;
                    }
                }
            }

            
        });
        while (i != 0) {
            }
    }
}
