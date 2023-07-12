/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.firebase;

import java.io.FileInputStream;
import java.io.IOException;

import com.google.api.gax.paging.Page;
import com.google.auth.oauth2.GoogleCredentials;
import com.google.cloud.storage.BlobId;
import com.google.cloud.storage.BlobInfo;
import com.google.cloud.storage.Bucket;

import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.cloud.StorageClient;
import com.mycompany.firebase.conection.conexion;

import com.google.cloud.storage.Storage;
import com.google.cloud.storage.StorageOptions;

import java.nio.file.Paths;

import javax.annotation.processing.FilerException;

public class Principal {

    public static void main(String[] args) {
        // conexion.Conectar();
       

           




       /*   FileInputStream serviceAccount;
        try {
         serviceAccount = new FileInputStream("tutorial.json");
        
        
        System.out.println("id proyecto: "+StorageOptions.newBuilder().setCredentials(GoogleCredentials.fromStream(serviceAccount)).build().getProjectId());
        } catch (Exception e) {
            System.out.println("Error en el segundo try: "+e.getMessage());
        }
        */

        try {
            subirImg("fir-java-dbb1c", "fir-java-dbb1c.appspot.com",
                    "pelivulas para ver.jpg", "pelivulas para ver.jpg");
        } catch (Exception e) {
            System.out.println("Error debido a: " + e.getMessage());
        }

        /*
         * try {
         * listBuckets("fir-java-dbb1c");
         * } catch (Exception e) {
         * System.out.println("Error: " + e.getMessage());
         * }
         */

    }

    private static void subirImg(String projectId, String bucketName, String objectName, String filePath)
            throws IOException {
        //Storage storage = StorageOptions.newBuilder().setProjectId(projectId).build().getService();
        FileInputStream serviceAccount = new FileInputStream("tutorial.json");
        Storage storage=StorageOptions.newBuilder().setCredentials(GoogleCredentials.fromStream(serviceAccount)).build().getService();
        BlobId blobId = BlobId.of(bucketName, objectName);
        BlobInfo blobInfo = BlobInfo.newBuilder(blobId).build();

        Storage.BlobWriteOption precondition;
        if (storage.get(bucketName, objectName) == null) {
            precondition = Storage.BlobWriteOption.doesNotExist();
        } else {
            precondition = Storage.BlobWriteOption.generationMatch(
                    storage.get(bucketName, objectName).getGeneration());
        }
        storage.createFrom(blobInfo, Paths.get(filePath), precondition);

        System.out.println(
                "File " + filePath + " uploaded to bucket " + bucketName + " as " + objectName);
    }

    public static void listBuckets(String projectId) {
        // The ID of your GCP project
        // String projectId = "your-project-id";
        Storage storage=null;
        try {
        FileInputStream serviceAccount = new FileInputStream("tutorial.json");
        storage = StorageOptions.newBuilder().setCredentials(GoogleCredentials.fromStream(serviceAccount)).build().getService();    
        } catch (Exception e) {
             System.out.println("Error en el storage de listBuckets: "+e.getMessage());
        }
        
        Page<Bucket> buckets = storage.list();

        for (Bucket bucket : buckets.iterateAll()) {
            System.out.println("******************************");
            System.out.println(bucket.getName());
            System.out.println("******************************");
        }
    }

    public static Bucket Defaultbucket() throws FilerException, IOException {
        FileInputStream serviceAccount = new FileInputStream("tutorial.json");

        FirebaseOptions options = FirebaseOptions.builder()
                .setCredentials(GoogleCredentials.fromStream(serviceAccount))
                .setStorageBucket("fir-java-dbb1c.appspot.com")
                .build();
        FirebaseApp.initializeApp(options);

        Bucket bucket = StorageClient.getInstance().bucket();
        return bucket;
    }
}
