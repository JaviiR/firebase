package com.mycompany.firebase.conection;

import java.sql.Connection;

import com.google.cloud.firestore.Firestore;
import com.google.firebase.cloud.FirestoreClient;

public class ConexionFactory {
    private static SqlServerConexion conexionSql = null;
    private static conexion conexionFirebase = null;

    public static Firestore getConexionFirebase() {

        if (conexionFirebase == null) {
            conexionFirebase = new conexion();
        }else{
            System.out.println("conexion a firebase ya existe");
            
        }
        return FirestoreClient.getFirestore();
    }

    public static Connection getConexionSqlServer() {

        if (conexionSql == null) {
            conexionSql = new SqlServerConexion();
        }else{
            System.out.println("conexion a sqlServer ya existe");
            
        }
        return conexionSql.getConexion();
    }

}
