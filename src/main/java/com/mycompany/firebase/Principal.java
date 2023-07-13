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
import com.mycompany.firebase.CRUD.uploaderImg;
import com.mycompany.firebase.conection.conexion;

import com.google.cloud.storage.Storage;
import com.google.cloud.storage.StorageOptions;

import java.nio.file.Paths;

import javax.annotation.processing.FilerException;

public class Principal {

    public static void main(String[] args) {
        // conexion.Conectar();

        uploaderImg.uploader("fir-java-dbb1c.appspot.com", "peliculas.jpg", "peliculas.jpg", "image/jpeg");
    }
}
