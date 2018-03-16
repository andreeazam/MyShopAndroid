package com.myshop.andreea.myshop;

/**
 * Created by Andreea on 5/19/2017.
 */


public class LinieComanda {
    private long idLinieComanda;
    private long idComandaClient;
    private long idProdus;
    private int cantitate;
    private double pretProdus;
    private double lccValoare;

    private String denumireProdus;

    //Pentru update comanda:
    double valoareNoua;
    double valoareVeche;

    public long getIdLinieComanda(){
        return idLinieComanda;
    }

    public void setIdLinieComanda(long idLinieComanda){
        this.idLinieComanda= idLinieComanda;
    }

    public long getIdComandaClient(){
        return idComandaClient;
    }

    public void setIdComandaClient(long idComandaClient){
        this.idComandaClient = idComandaClient;

    }

    public long getIdProdus(){
        return idProdus;
    }

    public void setIdProdus(long idProdus){
        this.idProdus = idProdus;
    }

    public int getCantitate(){
        return cantitate;
    }

    public void setCantitate(int cantitate){
        this.cantitate = cantitate;
    }

    public double getPretProdus(){
        return pretProdus;
    }

    public void setPretProdus(double pretProdus){
        this.pretProdus = pretProdus;
    }

    public double getLccValoare(){
        return lccValoare;
    }

    public void setLccValoare(double lccValoare){
        this.lccValoare = lccValoare;
    }

    public String getDenumireProdus(){
        return denumireProdus;
    }

    public void setDenumireProdus(String denumireProdus){
        this.denumireProdus = denumireProdus;
    }

    public LinieComanda(){

    }
}
