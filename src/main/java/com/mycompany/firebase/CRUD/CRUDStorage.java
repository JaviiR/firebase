package com.mycompany.firebase.CRUD;

import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import com.google.api.gax.paging.Page;
import com.google.auth.oauth2.GoogleCredentials;
import com.google.cloud.firestore.FirestoreException;
import com.google.cloud.storage.Blob;
import com.google.cloud.storage.BlobId;
import com.google.cloud.storage.BlobInfo;
import com.google.cloud.storage.Bucket;
import com.google.cloud.storage.Storage;
import com.google.cloud.storage.StorageOptions;
import com.google.firebase.cloud.StorageClient;
import com.mycompany.firebase.utilidades.idGenerator;

public class CRUDStorage {

    private static final String rutaCredenciales = "tutorial.json";//ruta en donde guardas las credenciales que te da firebase de tu proyect
    private static final String bucketName = "fir-java-dbb1c.appspot.com";//nombre del bucket del Firebase Storage ejemplo: 'NOMBRE_DEL_PROYECTO.appspot.com'

    /**
     * 
     * @param rutaArchivoLocal Ruta y nombre del archivo (local)
     * @return devuelve el nombre de la img subida al storage
     */
    public static String uploader(String rutaArchivoLocal) throws Exception {

        try {
            FileInputStream servicioCuentas = new FileInputStream(rutaCredenciales);
            // Crea una instancia de Storage utilizando las credenciales
            StorageOptions opcionesStorage = StorageOptions.newBuilder()
                    .setCredentials(GoogleCredentials.fromStream(servicioCuentas))
                    .build();
            Storage storage = opcionesStorage.getService();
            // Sube la imagen a Firebase Storage
            Blob blob = storage.create(
                    BlobInfo.newBuilder(bucketName,
                            String.valueOf(idGenerator.generar()))// dandole su nombre a la imagen subida generada
                                                                  // automaticamente
                            .setContentType("image/" + rutaArchivoLocal
                                    .substring(rutaArchivoLocal.length() - 3, rutaArchivoLocal.length()).toString())
                            .build(),
                    Files.readAllBytes(Paths.get(rutaArchivoLocal)));
            servicioCuentas.close();
            System.out.println("Imagen subida exitosamente a Firebase Storage");
            // link para descarga de la img desde storage
            return blob.getName();
        } catch (FirestoreException e) {
            System.out.println("**************************************************************");
            System.out.println("Error: " + e.getMessage() + "(uploader)");
            System.out.println("**************************************************************");
            return null;
        }
    }

    /**
     * 
     * @param projectId  id del proyecto (buscar en la consola de firebase)
     * @param objectName nombre que va a tener el archivo en el storage en Firebase
     * @param filePath   Ruta y nombre del archivo (local)
     */
    public static void otraFormaSubirImg(String projectId, String objectName, String filePath) {
        try {
            FileInputStream servicioCuentas = new FileInputStream(rutaCredenciales);
            Storage storage = StorageOptions.newBuilder().setCredentials(GoogleCredentials.fromStream(servicioCuentas))
                    .build().getService();
            BlobId blobId = BlobId.of(bucketName, objectName);
            BlobInfo blobInfo = BlobInfo.newBuilder(blobId).setContentType("image/jpeg").build();
            Storage.BlobWriteOption precondition;
            if (storage.get(bucketName, objectName) == null) {
                precondition = Storage.BlobWriteOption.doesNotExist();
            } else {
                precondition = Storage.BlobWriteOption.generationMatch(
                        storage.get(bucketName, objectName).getGeneration());
            }
            storage.createFrom(blobInfo, Paths.get(filePath), precondition);
            servicioCuentas.close();
        } catch (Exception e) {
            System.out.println("**************************************************************");
            System.out.println("Error: " + e.getMessage() + "(otraFormaSubirImg)");
            System.out.println("**************************************************************");
        }
        System.out.println("File " + filePath + " uploaded to bucket " + bucketName + " as " + objectName);
    }

    public static void listBuckets(String projectId) {
        Storage storage = null;
        try {
            FileInputStream servicioCuentas = new FileInputStream(rutaCredenciales);
            storage = StorageOptions.newBuilder().setCredentials(GoogleCredentials.fromStream(servicioCuentas)).build()
                    .getService();
        } catch (Exception e) {
            System.out.println("Error en el storage de listBuckets: " + e.getMessage());
        }
        Page<Bucket> buckets = storage.list();
        for (Bucket bucket : buckets.iterateAll()) {
            System.out.println("******************************");
            System.out.println(bucket.getName());
            System.out.println("******************************");
        }
    }

    /**
     *
     * @return devuelve una lista con los nombres de las imagenes
     */
    public static List<String> listNamesImgs() {
        List<String> imgNames = null;
        Storage storage = null;
        try {

            imgNames = new ArrayList<>();
            FileInputStream servicioCuentas = new FileInputStream(rutaCredenciales);
            storage = StorageOptions.newBuilder().setCredentials(GoogleCredentials.fromStream(servicioCuentas)).build()
                    .getService();
            Iterator<Blob> iterator = storage.get(bucketName).list().iterateAll().iterator();
            while (iterator.hasNext()) {
                Blob blob = iterator.next();
                imgNames.add(blob.getName());
            }
            servicioCuentas.close();
            return imgNames;
        } catch (Exception e) {
            System.out.println("Error en listNamesImgs: " + e.getCause());
            return null;
        }
    }

    /**
     * 
     * @param imagePath ruta de la img en remoto o nombre de la img
     * @return retorna la imagen descargada del Firebase Storage convertida a un arreglo de bytes
     */
    public static byte[] downloadImageBytes(String imagePath) {

        try {
            FileInputStream servicioCuentas = new FileInputStream(rutaCredenciales);
            // Inicializa Firebase Admin SDK con las credenciales del servicio
            GoogleCredentials credentials = GoogleCredentials.fromStream(servicioCuentas);
            Storage storage = StorageOptions.newBuilder().setCredentials(credentials).build().getService();
            // Obtiene la referencia al objeto Blob de la imagen en Firebase Storage
            Blob blob = storage.get(bucketName, imagePath);
            // Lee los bytes del Blob y los almacena en un ByteArrayOutputStream
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            blob.downloadTo(outputStream);
            return outputStream.toByteArray();
        } catch (Exception e) {
            System.out.println("no se pudo cargar la imagen del Storage");
            byte[] byteImgDefault = null;
            try {
                byteImgDefault = Files.readAllBytes(Paths.get("imgs/images.png"));
            } catch (Exception ev) {
                System.out.println("no se pudo cargar la img por defecto(downloadImageBytes)");
            }
            return byteImgDefault;
        }

    }

    /**
     * 
     * @param name nombre de la imagen en el Storage
     * @return retorna true si se pudo eliminar la imagen y false si ocurrio algun
     *         error al intentar eliminar la imagen
     */
    public static boolean deleteImg(String name) {
        StorageOptions opciones = null;
        try {
            FileInputStream servicioCuentas = new FileInputStream(rutaCredenciales);
            opciones = StorageOptions.newBuilder().setCredentials(GoogleCredentials.fromStream(servicioCuentas))
                    .build();
        } catch (Exception e) {
            System.out.println("ERROR(deleteImg): " + e.getMessage());
        }
        Storage storage = opciones.getService();
        BlobId blobid = BlobId.of(bucketName, name);
        boolean validar = storage.delete(blobid);
        if (validar == true) {
            System.out.println("imagen " + name + " eliminada Correctamente.");
            return true;
        } else {
            System.out.println("no se pudo eliminar la imagen " + name);
            return false;
        }

    }

    /**
     * 
     * @param name  ruta o nombre de la imagen remoto
     * @param bytes imagen que se va a enviar al Firebase Storage convertida a un arreglo de bytes
     * @return      retorna el nombre de la imagen
     */
    public static String actualizarImg(String name, byte[] bytes) {
        StorageOptions opciones = null;
        try {
            FileInputStream servicioCuentas = new FileInputStream(rutaCredenciales);
            opciones = StorageOptions.newBuilder().setCredentials(GoogleCredentials.fromStream(servicioCuentas))
                    .build();
        } catch (Exception e) {
            System.out.println("ERROR(actulizarImg 1): " + e.getMessage());
        }
        // BlobId blobId = BlobId.of(bucketName, name);
        Storage storage = opciones.getService();
        try {
            storage.create(
                    BlobInfo.newBuilder(bucketName,
                            String.valueOf(name))
                            .setContentType("image/jpg")
                            .build(),
                    bytes);
            System.out.println("Imagen " + name + " actulizada correctamente");
            return name;
        } catch (Exception e) {
            System.out.println("ERROR(actualizarImg 2): " + e.getMessage());
            return name;
        }

    }

    /**
     * 
     * @param nameImg nombre del imagen
     * @param newtimeRefresh  DateTime de la ultima ves que fue modificado
     * @return retorna true si no hubo cambios en la hora que fue modificada
     */
    public static boolean validarCambioimg(String nameImg,String oldTimeRefresh){
        try {
        String timeRefresh=StorageClient.getInstance().bucket(bucketName).get(nameImg).getUpdateTimeOffsetDateTime().toString();    
        if(timeRefresh.equals(oldTimeRefresh)){
            return true;
        }else{
            return false;
        }
        } catch (Exception e) {
            System.out.println("ERROR(validarCambioImg): "+e.getMessage());
            return false;
        }
        

        
        
    }

}
