package com.mycompany.firebase.CRUD;

import java.util.HashMap;
import java.util.Map;

import com.google.api.core.ApiFuture;
import com.google.cloud.firestore.DocumentReference;
import com.google.cloud.firestore.Firestore;
import com.google.cloud.firestore.WriteResult;
import com.google.firebase.cloud.FirestoreClient;
import com.mycompany.firebase.conection.conexion;

public class uploaderFireStore {
    public static void uploaderOnFireStore() throws Exception{
        conexion.Conectar();
        Firestore db = FirestoreClient.getFirestore();

        DocumentReference docRef = db.collection("products").document("Mentitas 21g");
        // Add document data with id "alovelace" using a hashmap
        Map<String, Object> data = new HashMap<>();
        data.put("precio", 0.80);
        data.put("img", uploaderImg.uploader("fir-java-dbb1c.appspot.com", "peliculas.jpg", "image/jpeg"));
        data.put("stock", 100);
        data.put("cb", "1025403232");
        // asynchronously write data
        ApiFuture<WriteResult> result = docRef.set(data);
        // ...
        // result.get() blocks on response
        try {
            System.out.println("Update time : " + result.get().getUpdateTime());
        } catch (Exception e) {
            System.out.println("nose pudo obtener los datos");
        }

    }
}
