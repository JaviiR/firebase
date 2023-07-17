package com.mycompany.firebase.utilidades;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.mycompany.firebase.CRUD.uploaderImg;

public class idGenerator {

    /**
     * 
     * @return genera un nombre automaticamente tipo ID y si hay imagenes borradas
     *         completa con ids faltantes
     */
    public static String generar() {
        String idGenerado = "";
        List<String> listaRemota = uploaderImg.listNamesImgs("fir-java-dbb1c.appspot.com");// lista de ids que estan en
                                                                                           // la firestore
        List<String> listVacios = new ArrayList<>();// lista de ids que no estan siendo usados

        if (listaRemota.size() > 1 && listaRemota != null) {
            Integer[] listaSinCeros = new Integer[listaRemota.size()];

            for (int i = 0; i <= listaRemota.size() - 1; i++) {
                listaSinCeros[i] = Integer.parseInt(separator.separarId(listaRemota.get(i)));
            }
            // ordena la lista
            Arrays.sort(listaSinCeros);
            // crea lista con numeros faltantes
            for (int i = 1; i <= listaSinCeros.length - 1; i++) {
                int actual = listaSinCeros[i];
                int anterior = listaSinCeros[i - 1];
                if (actual - 1 != anterior) {
                    int dif = actual - anterior;
                    for (int y = 1; y <= dif - 1; y++) {
                        listVacios.add(String.valueOf(anterior + y));
                    }
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

        /*
         * List<String> listaRemota = new ArrayList<>();
         * listaRemota.add("000001");
         * listaRemota.add("000002");
         * listaRemota.add("000003");
         * listaRemota.add("000004");
         * listaRemota.add("000007");
         * listaRemota.add("000012");
         * listaRemota.add("000005");
         * listaRemota.add("000088");
         * listaRemota.add("000032");
         * listaRemota.add("000010");
         * listaRemota.add("000022");
         * listaRemota.add("000006");
         * listaRemota.add("000008");
         * listaRemota.add("000009");
         */

        return idGenerado;
    }
}