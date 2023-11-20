package com.mycompany.firebase.utilidades;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.mycompany.firebase.CRUD.CRUDStorage;

public class idGenerator {
    static final String bucketName = "fir-java-dbb1c.appspot.com";
    private static CRUDStorage strageCRUD=CRUDStorage.getCRUDStorage();

    /**
     * 
     * @return genera un nombre automaticamente tipo ID y si hay imagenes borradas
     *         completa con ids faltantes
     */
    public static String generar() {
        String idGenerado = "";
        List<String> listaRemota = null;
        listaRemota = strageCRUD.listNamesImgs();// lista de ids que estan en
                                                      // la firestore
        List<String> listVacios = new ArrayList<>();// lista de ids que no estan siendo usados

        if (listaRemota.size() > 1) {
            Integer[] listaSinCeros = new Integer[listaRemota.size()];

            for (int i = 0; i <= listaRemota.size() - 1; i++) {
                listaSinCeros[i] = Integer.parseInt(separator.separarId(listaRemota.get(i)));
            }
            // ordena la lista
            Arrays.sort(listaSinCeros);
            // creando lista con numeros faltantes
            for(int i=1;i<=listaSinCeros[listaSinCeros.length-1];i++){
                boolean validar=true;
                for(int y=0;y<listaSinCeros.length;y++){
                    if(i==listaSinCeros[y]){
                        validar=false;
                        break;
                    }
                    
                }
                if(validar){
                        listVacios.add(""+i);
                    }
            }
            if (listVacios.size() != 0) {
                // valido que el id se complete con ceros hasta llegar a 6 digitos
                for (int i = 0; i < 6 - listVacios.get(0).length(); i++) {
                    idGenerado += "0";
                }
                idGenerado += listVacios.get(0);
            } else {
                int idActual = 0;
                idActual = listaSinCeros[listaSinCeros.length - 1] + 1;
                String idActualString = String.valueOf(idActual);
                for (int i = 0; i < 6 - idActualString.length(); i++) {
                    idGenerado += "0";
                }
                idGenerado += idActualString;
            }

        } else if (listaRemota.size() == 1) {

            String idSinCeros = separator.separarId(listaRemota.get(0));
            int idActual = Integer.parseInt(idSinCeros) + 1;
            String idActualString = String.valueOf(idActual);

            for (int i = 0; i < 6 - idActualString.length(); i++) {
                idGenerado += "0";
            }
            idGenerado += idActualString;
        } else {
            idGenerado = "000001";
        }

        return idGenerado;
    }


    /**
     * 
     * @param lista lista que tiene todos los ids que no tengan cb: al principio en la base de datos
     * @return retorna un id que sigue en orden a la lista previamente ingresada o un id faltante dentro de la lista
     */
    public static String generarIdProduct(List<String> lista){
        String idGenerado = "";
        List<String> listVacios = new ArrayList<>();// lista de ids que no estan siendo usados
        if (lista.size() > 1) {
            Integer[] listaSinCeros = new Integer[lista.size()];

            for (int i = 0; i <= lista.size() - 1; i++) {
                listaSinCeros[i] = Integer.parseInt(separator.separarId(lista.get(i)));
            }
            // ordena la lista
            Arrays.sort(listaSinCeros);
            // creando lista con numeros faltantes
            for(int i=1;i<=listaSinCeros[listaSinCeros.length-1];i++){
                boolean validar=true;
                for(int y=0;y<listaSinCeros.length;y++){
                    if(i==listaSinCeros[y]){
                        validar=false;
                        break;
                    }
                    
                }
                if(validar){
                        listVacios.add(""+i);
                    }
            }
            if (listVacios.size() != 0) {
                // valido que el id se complete con ceros hasta llegar a 6 digitos
                for (int i = 0; i < 6 - listVacios.get(0).length(); i++) {
                    idGenerado += "0";
                }
                idGenerado += listVacios.get(0);
            } else {
                int idActual = 0;
                idActual = listaSinCeros[listaSinCeros.length - 1] + 1;
                String idActualString = String.valueOf(idActual);
                for (int i = 0; i < 6 - idActualString.length(); i++) {
                    idGenerado += "0";
                }
                idGenerado += idActualString;
            }

        } else if (lista.size() == 1) {

            String idSinCeros = separator.separarId(lista.get(0));
            int idActual = Integer.parseInt(idSinCeros) + 1;
            String idActualString = String.valueOf(idActual);

            for (int i = 0; i < 6 - idActualString.length(); i++) {
                idGenerado += "0";
            }
            idGenerado += idActualString;
        } else {
            idGenerado = "000001";
        }

        return idGenerado;
    }
}
