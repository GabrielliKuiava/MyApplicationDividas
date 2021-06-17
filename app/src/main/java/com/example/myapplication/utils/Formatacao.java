package com.example.myapplication.utils;

import java.text.NumberFormat;
import java.util.Locale;

public class Formatacao {
    public static String getValorFormatado(double valor) {
        String novoValor = "R$ 0,00";

        try {

            Locale ptBr = new Locale("pt", "BR");
            novoValor = NumberFormat.getCurrencyInstance(ptBr).format(valor);

        } catch (Exception ex) {

        }

        return novoValor;
    }
}
