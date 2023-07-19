package com.mycompany.firebase.CRUD;

import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.IOException;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.cloud.storage.Blob;
import com.google.cloud.storage.Storage;
import com.google.cloud.storage.StorageOptions;

public class downloaderImg {

        

        public static byte[] downloadImageBytes(String bucketName, String imagePath) throws IOException {
            FileInputStream serviceAccount = new FileInputStream("tutorial.json");
        
            // Inicializa Firebase Admin SDK con las credenciales del servicio
            GoogleCredentials credentials = GoogleCredentials.fromStream(serviceAccount);
            Storage storage = StorageOptions.newBuilder().setCredentials(credentials).build().getService();
        
            // Obtiene la referencia al objeto Blob de la imagen en Firebase Storage
            Blob blob = storage.get(bucketName, imagePath);
        
            // Lee los bytes del Blob y los almacena en un ByteArrayOutputStream
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            blob.downloadTo(outputStream);
        
            return outputStream.toByteArray();
        }
    
}
