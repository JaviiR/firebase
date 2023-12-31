package com.mycompany.firebase.CRUD;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.google.api.core.ApiFuture;
import com.google.cloud.firestore.DocumentReference;
import com.google.cloud.firestore.DocumentSnapshot;
import com.google.cloud.firestore.Firestore;
import com.google.cloud.firestore.WriteResult;
import com.google.cloud.storage.Blob;
import com.google.firebase.cloud.StorageClient;
import com.mycompany.firebase.utilidades.CodEliminacion;
import com.mycompany.firebase.utilidades.idGenerator;
import com.mycompany.firebase.conection.ConexionFactory;
import com.mycompany.firebase.utilidades.idGenerator;

public class CRUDFireStore {
    private static final String CollectionName = "products";
    private static final String bucketName = "fir-java-dbb1c.appspot.com";// nombre del bucket del Firebase Storage
                                                                         // ejemplo: 'NOMBRE_DEL_PROYECTO.appspot.com'
    private static Firestore firestore = null;
    private static CRUDFireStore firestoreCrud=null;
    private static CRUDStorage storageCRUD=null;

    private CRUDFireStore() {
        firestore=ConexionFactory.getConexionFirebase();
        storageCRUD=CRUDStorage.getCRUDStorage();
    }

    public static CRUDFireStore getCRUDFireStore() {
        if(firestoreCrud==null){
            firestoreCrud=new CRUDFireStore();
        }
        return firestoreCrud;
    }
    





    /**
     * 
     * @param NameProduct      nombre del producto
     * @param Price            precio del producto
     * @param rutaArchivoLocal ruta del archivo local con nombre de la imagen al
     *                         ultimo
     * @param Stock            stock del producto
     * @param ListBarCodes     lista con los codigo de barras del producto
     * @throws Exception
     */
    public void uploaderOnFireStore(String NameProduct, double Price, String rutaArchivoLocal, int Stock,
            List<String> ListBarCodes) {
        DocumentReference docRef = null;
        String nameDocument = null;
        if (ListBarCodes == null || ListBarCodes.size() == 0) {
            List<String> listaIdGenerados = null;
            List<String> listaDocuments = getAllDocuments();// lista que tiene todos los nombres de los
                                                                          // documentos
            if (listaDocuments != null) {
                listaIdGenerados = new ArrayList<>();// lista que va a tener los nombres de los documentos que no tengan
                                                     // cb: al principio
                for (String nombre : listaDocuments) {
                    if (!nombre.substring(0, 3).equals("cb:")) {
                        listaIdGenerados.add(nombre);
                    }
                }
            }
            nameDocument = idGenerator.generarIdProduct(listaIdGenerados);
        } else {

            String PreDocumentName = "";

            for (String code : ListBarCodes) {

                PreDocumentName += code + ",";

            }
            nameDocument = "cb:" + PreDocumentName;
        }

        try {
            docRef = firestore.collection(CollectionName).document(nameDocument);
        } catch (Exception e) {
            System.out.println("ERROR al obtener la coleccion: " + e.getMessage());
        }

        // Add document data with id "alovelace" using a hashmap
        Map<String, Object> data = new HashMap<>();
        data.put("nombre", NameProduct);
        data.put("precio", Price);
        try {
            data.put("img", storageCRUD.uploader(rutaArchivoLocal));
        } catch (Exception e) {
            System.out.println("img no se pudo cargar");
        }
        data.put("stock", Stock);
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


    public void editarProduct(double precio, byte[] bytesImg, int stock, List<String> OldlistBarCodes,
        List<String> NewlistBarCodes,
        String nameImg, String nameProduct) {
        String OldIdProduct = "";
        String NewIdProduct = "";
        int contadorOld = 0;// contador del id antiguo
        int contadorNew = 0;// contador del id nuevo

        // paso el id antiguo a un string para comparación
        for (String s : OldlistBarCodes) {
            if (!s.substring(0, 2).equals("IG")) {
                if (contadorOld == 0) {
                    OldIdProduct += "cb:" + s + ",";
                } else {
                    OldIdProduct += s + ",";
                }
                contadorOld++;
            } else {
                OldIdProduct = s.substring(3, s.length());
            }
        }

        // paso el id nuevo a un string para comparación
        for (String s : NewlistBarCodes) {
            if (!s.substring(0, 2).equals("IG")) {
                if (contadorNew == 0) {
                    NewIdProduct += "cb:" + s + ",";
                } else {
                    NewIdProduct += s + ",";
                }
                contadorNew++;
            } else {
                NewIdProduct = s.substring(3, s.length());
            }
        }

        DocumentReference oldDocRef = firestore.collection(CollectionName).document(OldIdProduct);

        if (NewIdProduct.equals(OldIdProduct)) {
            Map<String, Object> datos = new HashMap<>();
            datos.put("nombre", nameProduct);
            datos.put("precio", precio);
            datos.put("img", storageCRUD.actualizarImg(nameImg, bytesImg));
            datos.put("stock", stock);
            ApiFuture<WriteResult> oldResult = oldDocRef.update(datos);
            try {
                System.out.println("edit time: " + oldResult.get().getUpdateTime());
            } catch (Exception e) {
                System.out.println("ERROR(editarProduct): " + e.getMessage());
            }
        } else {
            ApiFuture<WriteResult> Result;
            Map<String, Object>  beforeDelete= new HashMap<>();
            beforeDelete.put("nombre", CodEliminacion.COD_ELIMINACION);
            Result=oldDocRef.update(beforeDelete);
            try {
                System.out.println("update before deleting time: " + Result.get().getUpdateTime());
            } catch (Exception e) {
                System.out.println("ERROR(editarProduct): " + e.getMessage());
            }
            Result =oldDocRef.delete();
            try {
                System.out.println("delete time: " + Result.get().getUpdateTime());
            } catch (Exception e) {
                System.out.println("ERROR(editarProduct): " + e.getMessage());
            }
            DocumentReference newDocRef = firestore.collection(CollectionName).document(NewIdProduct);
            Map<String, Object> datos = new HashMap<>();
            datos.put("nombre", nameProduct);
            datos.put("precio", precio);
            datos.put("img", storageCRUD.actualizarImg(nameImg, bytesImg));
            datos.put("stock", stock);
            Result = newDocRef.create(datos);
            try {
                System.out.println("edit time: " + Result.get().getUpdateTime());
            } catch (Exception e) {
                System.out.println("ERROR(editarProduct): " + e.getMessage());
            }
        }
    }


    public boolean deleteProduct(String nameProduct) {// en un futuro cambiar el nombre del producto por el

                                                             // barcode
                                                             // o nombre de la img

        try {

            
            DocumentReference referencia = firestore.collection(CollectionName).document(nameProduct);// creo una

                                                                                                    // referencia al
                                                                                                    // producto
            String nameImg = referencia.get().get().getData().get("img").toString();// obtengo el nombre de la img
                                                                                    // asociada al producto
            ApiFuture<WriteResult> resultado = referencia.delete();// elimino el producto en el firestore
            storageCRUD.deleteImg(nameImg);// elimino la imagen en el storage
            System.out.println("Producto eliminado: " + resultado.get().getUpdateTime());
            return true;
        } catch (Exception e) {
            System.out.println("ERROR(deleteProduct): " + e.getMessage());
            return false;
        }
    }

    /**
     * 
     * @param NameCollection nombre de la coleccion
     * @return trae una lista con los nombres de los documentos que contiene
     *         <a >{@code NameCollection}</a>
     *         estados:
     *         * devuelve una lista con uno o mas datos
     *         * devuelve una lista con 0 datos
     *         * devuelve una lista null
     */
    public List<String> getAllDocuments() {
        List<String> listaDocumentos = null;
        try {
            Iterable<DocumentReference> documentos = firestore.collection(CollectionName)
                    .listDocuments();
            if (documentos.iterator().hasNext()) {
                listaDocumentos = new ArrayList<>();
                for (DocumentReference reference : documentos) {
                    listaDocumentos.add(reference.getId());
                }
            } else {
                listaDocumentos = new ArrayList<>();
            }
            return listaDocumentos;
        } catch (Exception e) {
            System.out.println("lista nula");
            return listaDocumentos;
        }
    }

    /**
     * 
     * @return retorna una lista con los datos del producto traido desde Firestore
     *         que tenga el Id <a>{@code idProduct}<a/>
     */
    public List<String> DatosProducto(String idProduct) {

        List<String> listaTemp = new ArrayList<>();

        Map<String, Object> data = null;
        try {
            DocumentReference reference = firestore.collection(CollectionName).document(idProduct);
            ApiFuture<DocumentSnapshot> future = reference.get();
            DocumentSnapshot document = future.get();
            Blob Dataimg = StorageClient.getInstance().bucket(bucketName).get(document.get("img").toString());
            if (document.exists()) {
                data = document.getData();

            } else {
                System.out.println("el documento no existe");
            }
            String nombre = data.get("nombre").toString();
            String precio = data.get("precio").toString();
            String img = data.get("img").toString();
            String stock = data.get("stock").toString();
            String timeRefreshFStore = document.getUpdateTime().toDate().toString();// time de la ultima actualizacion
                                                                                    // del producto
            String timeRefreshStorage = Dataimg.getUpdateTimeOffsetDateTime().toString();// time de la ultima
                                                                                         // actualizacion de la img

            listaTemp.add(nombre);
            listaTemp.add(precio);
            listaTemp.add(img);
            listaTemp.add(stock);
            listaTemp.add(timeRefreshFStore);
            listaTemp.add(timeRefreshStorage);

        } catch (Exception e) {
            System.out.println("ERROR(DatosProducto): " + e.getMessage());
        }
        return listaTemp;
    }

    /**
     * 
     * @param IdProduct id del producto antes que sea modificado
     * @return retorna false si no encuentra un id igual en la firestore
     */
    public boolean validarCambiosId(String IdProduct) {
        List<String> Documents = getAllDocuments();
        if (Documents != null) {
            boolean validar = false;
            for (String s : Documents) {
                if (IdProduct.equals(s)) {
                    validar = true;
                }
            }
            return validar;
        } else {
            return false;
        }
    }


    public boolean validarCambiosDatos(String IdProduct, String oldtimeRefreshFStore) {


        try {
            ApiFuture<DocumentSnapshot> future = firestore.collection(CollectionName)
                    .document(IdProduct).get();
            try {
                String timeRefreshFStore = future.get().getUpdateTime().toDate().toString();
                if (timeRefreshFStore.equals(oldtimeRefreshFStore)) {
                    return true;
                } else {
                    return false;
                }
            } catch (Exception e) {
                System.out.println("ERROR(validarCambiosDatos1): " + e.getMessage());
                return false;
            }
        } catch (Exception e) {
            System.out.println("ERROR(validarCambiosDatos2): " + e.getMessage());
            return false;
        }

    }

    /**
     * 
     * @param Collection
     * @param Document
     * @return orden de los objetos que trae: nombre, precio, stock, img, idproduct
     */
    public List<Object> DownloadDataByCollection(String Document) {
        List<Object> listaTemp = new ArrayList<>();
        Map<String, Object> data = null;
        try {

            DocumentReference reference = firestore.collection("products").document(Document);
            ApiFuture<DocumentSnapshot> future = reference.get();
            DocumentSnapshot document = future.get();
            if (document.exists()) {
                data = document.getData();

            } else {
                System.out.println("el documento no existe");
            }
            String idProduct = document.getId();
            String nombre = data.get("nombre").toString();
            String img = data.get("img").toString();
            double precio = Double.parseDouble(data.get("precio").toString());
            String stock = data.get("stock").toString();
            listaTemp.add(nombre);
            listaTemp.add(precio);
            listaTemp.add(stock);
            listaTemp.add(img);
            listaTemp.add(idProduct);
            System.out.println("Datos descargados exitosamente");
        } catch (Exception e) {
            System.out.println("ERROR: " + e.getMessage());
        }

        return listaTemp;
    }

}
    

