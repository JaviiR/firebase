package com.mycompany.firebase.CRUD;

import java.util.HashMap;
import java.util.Map;

import com.google.api.core.ApiFuture;
import com.google.cloud.firestore.DocumentReference;
import com.google.cloud.firestore.Firestore;
import com.google.cloud.firestore.WriteResult;
import com.google.firebase.cloud.FirestoreClient;
import com.mycompany.firebase.conection.conexion;

public class CRUDFireStore {


    /*public static void main(String[] args) {
        conexion.Conectar();
        try {
            uploaderOnFireStore("Mentitas",0.80, "imgs/mentitas.jpg", 100, "93485723948", "image/jpg");
            System.out.println("datos guardados exitosamente");
            uploaderOnFireStore("Cereal Angel",3.20, "imgs/angel_frutt.jpg", 100, "934853534", "image/jpg");
            System.out.println("datos guardados exitosamente");
            uploaderOnFireStore("Chocapic",3.50, "imgs/chocapic.jpg", 100, "93485723948", "image/jpg");
            System.out.println("datos guardados exitosamente");
            uploaderOnFireStore("wafer_nik",0.80, "imgs/wafer_nik.jpg", 100, "93485723948", "image/jpg");
            System.out.println("datos guardados exitosamente");
            
        } catch (Exception e) {
            System.out.println("ERROR no se pudo cargar los datos a la firestore");
            System.out.println("ERROR: "+e.getMessage());
        }
    }*/
    /**
     * 
     * @param NameProduct      nombre del producto
     * @param Price            precio del producto
     * @param rutaArchivoLocal ruta del archivo local con nombre de la imagen al
     *                         ultimo
     * @param Stock            stock del producto
     * @param BarCode          codigo de barras del producto
     * @param imageType        tipo de imagen a subir al storage ejem: image/jpg o
     *                         image/gif
     * @throws Exception
     */
    public static void uploaderOnFireStore(String NameProduct, double Price, String rutaArchivoLocal, int Stock,
            String BarCode, String imageType) throws Exception {
        
        Firestore db = FirestoreClient.getFirestore();

        DocumentReference docRef = db.collection("products").document(NameProduct);
        // Add document data with id "alovelace" using a hashmap
        Map<String, Object> data = new HashMap<>();
        data.put("precio", Price);
        data.put("img", uploaderImg.uploader("fir-java-dbb1c.appspot.com", rutaArchivoLocal, imageType));
        data.put("stock", Stock);
        data.put("cb", BarCode);
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
