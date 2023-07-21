package com.mycompany.firebase.CRUD;

import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
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
import com.mycompany.firebase.utilidades.idGenerator;

public class CRUDStorage {

    static final String rutaCredenciales = "tutorial.json";

    /**
     * 
     * @param nameBucket       nombre del bucket al que se va a subir el archivo
     * @param rutaArchivoLocal Ruta y nombre del archivo (local)
     * @param contentType      tipo del archivo que va a subir al storage
     */
    public static String uploader(String nameBucket, String rutaArchivoLocal,
            String contentType) throws Exception {

        try {
            // Ruta al archivo de credenciales de Firebase
            String rutaCredenciales = "tutorial.json";

            // Carga las credenciales desde el archivo
            FileInputStream servicioCuentas = new FileInputStream(rutaCredenciales);

            // Crea una instancia de Storage utilizando las credenciales
            StorageOptions opcionesStorage = StorageOptions.newBuilder()
                    .setCredentials(GoogleCredentials.fromStream(servicioCuentas))
                    .build();
            Storage storage = opcionesStorage.getService();

            // Sube la imagen a Firebase Storage
            Blob blob = storage.create(
                    BlobInfo.newBuilder(nameBucket,
                            String.valueOf(idGenerator.generar()))// dandole su nombre a la imagen subida generada
                                                                  // automaticamente
                            .setContentType(contentType)
                            .build(),
                    Files.readAllBytes(Paths.get(rutaArchivoLocal)));

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
     * @param bucketName nombre del bucket al que se va a subir el archivo(buscar en
     *                   la consola ejemplo:'NOMBRE_DEL_PROYECTO.appspot.com')
     * @param objectName nombre que va a tener el archivo en el storage en Firebase
     * @param filePath   Ruta y nombre del archivo (local)
     */
    public static void otraFormaSubirImg(String projectId, String bucketName, String objectName, String filePath) {
        // Storage storage =
        // StorageOptions.newBuilder().setProjectId(projectId).build().getService();
        try {
            FileInputStream serviceAccount = new FileInputStream("tutorial.json");
            Storage storage = StorageOptions.newBuilder().setCredentials(GoogleCredentials.fromStream(serviceAccount))
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
        } catch (Exception e) {
            System.out.println("**************************************************************");
            System.out.println("Error: " + e.getMessage() + "(otraFormaSubirImg)");
            System.out.println("**************************************************************");
        }
        System.out.println("File " + filePath + " uploaded to bucket " + bucketName + " as " + objectName);
    }

    public static void listBuckets(String projectId) {
        // The ID of your GCP project
        // String projectId = "your-project-id";
        Storage storage = null;
        try {
            FileInputStream serviceAccount = new FileInputStream("tutorial.json");
            storage = StorageOptions.newBuilder().setCredentials(GoogleCredentials.fromStream(serviceAccount)).build()
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
     * @param nameBucket nombre del bucket
     * @return  devuelve una lista con los nombres de las imagenes
     */
    public static List<String> listNamesImgs(String nameBucket) {
        List<String> imgNames = null;
        Storage storage = null;
        try {
            imgNames = new ArrayList<>();
            FileInputStream serviceAccount = new FileInputStream("tutorial.json");
            storage = StorageOptions.newBuilder().setCredentials(GoogleCredentials.fromStream(serviceAccount)).build()
                    .getService();
            Iterator<Blob> iterator = storage.get(nameBucket).list().iterateAll().iterator();
            while (iterator.hasNext()) {
                Blob blob = iterator.next();
                imgNames.add(blob.getName());
            }
            return imgNames;
        } catch (Exception e) {
            System.out.println("Error en listNamesImgs: " + e.getCause());
            return null;
        }
    }


    /**
     * 
     * @param bucketName nombre del bucket
     * @param imagePath
     * @return
     * @throws IOException
     */
    public static byte[] downloadImageBytes(String bucketName, String imagePath) throws IOException {
        FileInputStream serviceAccount = new FileInputStream(rutaCredenciales);

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

    /**
     * 
     * @param bucketName nombre del bucket
     * @param name       nombre de la imagen
     * @return retorna true si se pudo eliminar la imagen y false si ocurrio algun
     *         error al intentar eliminar la imagen
     */
    public static boolean deleteImg(String bucketName, String name) {

        FileInputStream fileInput = null;
        StorageOptions opciones = null;
        try {
            fileInput = new FileInputStream(rutaCredenciales);
        } catch (Exception e) {
            System.out.println("ERROR(deleteImg): " + e.getMessage());
        }
        try {
            opciones = StorageOptions.newBuilder().setCredentials(GoogleCredentials.fromStream(fileInput))
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
     * @param bucketName nombre del bucket del storage
     * @param name ruta o nombre de la imagen remoto
     * @param contentType tipo de imagen que se va a subir
     * @param rutaImgLocal ruta de la imagen local
     * @return
     */
    public static boolean actualizarImg(String bucketName, String name, String contentType, String rutaImgLocal) {
        FileInputStream file = null;
        StorageOptions opciones = null;
        try {
            file = new FileInputStream(rutaCredenciales);
        } catch (Exception e) {
            System.out.println("ERROR(actualizarImg 1): " + e.getMessage());
        }
        try {
            opciones = StorageOptions.newBuilder().setCredentials(GoogleCredentials.fromStream(file))
                    .build();
        } catch (Exception e) {
            System.out.println("ERROR(actulizarImg 2): " + e.getMessage());
        }
        // BlobId blobId = BlobId.of(bucketName, name);
        Storage storage = opciones.getService();

        try {
            storage.create(
                    BlobInfo.newBuilder(bucketName,
                            String.valueOf(name))// dandole su nombre a la imagen subida generada
                                                 // automaticamente
                            .setContentType(contentType)
                            .build(),
                    Files.readAllBytes(Paths.get(rutaImgLocal)));
            System.out.println("Actulizado correctamente");
            return true;
        } catch (Exception e) {
            System.out.println("ERROR(actualizarImg 3): " + e.getMessage());
            return false;
        }

    }

}
