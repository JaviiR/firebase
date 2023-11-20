package com.mycompany.firebase.conection;

import java.io.FileInputStream;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;




public class conexion {
    

    public conexion(){
try {
            FileInputStream serviceAccount = new FileInputStream("tutorial.json");
                FirebaseOptions options = FirebaseOptions.builder()
                    .setCredentials(GoogleCredentials.fromStream(serviceAccount))
                    .build();
                FirebaseApp.initializeApp(options);
                System.out.println("Conexión a firebase exitosa"); 
        } catch (Exception e) {
            System.out.println("Conexion fallida por: "+e.getMessage());
        }
    }

    

}
