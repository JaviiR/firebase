package com.mycompany.firebase.conection;

import java.sql.Connection;
import java.sql.DriverManager;

public class SqlServerConexion {
    private static String url = "jdbc:sqlserver://DESKTOP-18A126S:1433;database=Productos;encrypt=false;trustServerCertificate=false";
    private static String user = "sa";
    private static String password = "Samuray7)";
    private static Connection conexion = null;

    public Connection getConexion() {
        getConnection();
        return conexion;
    }

    private static void getConnection() {
        try {
            if (conexion == null) {
                conexion = DriverManager.getConnection(url, user, password);
                System.out.println("conexion a sql exitosa");
            }
        } catch (Exception e) {
            System.out.println("ERROR(getConnection()): " + e.getMessage());
        }

    }

}
