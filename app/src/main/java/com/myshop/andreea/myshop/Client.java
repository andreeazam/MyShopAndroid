package com.myshop.andreea.myshop;

/**
 * Created by Andreea Zamfir on 3/2/2017.
 */

import cz.msebera.android.httpclient.client.HttpClient;
import cz.msebera.android.httpclient.impl.client.DefaultHttpClient;

public class Client {
    String nume, prenume, username, parola, strada, email;
    int numarStrada;
    long telefon, latitudine, longitudine;
    boolean existaClient = false;

    //constructor
    public Client(String nume, String prenume, String username, String parola,
                  String strada, String email, int numarStrada, long telefon, long latitudine, long longitudine){
        this.nume= nume;
        this.prenume= prenume;
        this.parola= parola;
        this.strada= strada;
        this.email=email;
        this.username= username;
        this.numarStrada= numarStrada;
        this.telefon = telefon;
        this.latitudine = latitudine;
        this.longitudine = longitudine;

    }

    public Client() {
    }
}
