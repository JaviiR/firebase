package com.mycompany.firebase.utilidades;


public class separator {

    public static String separarId(String IdCompleto) {
        int ceros = 0;
        String id;
        try {
            for (int i = IdCompleto.length() - 1; i > 0; i--) {
                String indice = IdCompleto.substring(i - 1, i);
                if (indice.equals("0")) {
                    ceros++;
                }
            }
        } catch (Exception e) {
            System.out.println("ERROR en separator.separadorId()");
            System.out.println("no se encuentra id");
        }

        id = IdCompleto.substring(ceros, IdCompleto.length());
        return id;

    }
}
