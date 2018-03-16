package com.myshop.andreea.myshop;

/**
 * Created by Andreea on 5/17/2017.
 */

public class Comanda {

    public long idComandaClient;
    public long numarComanda;
    public long idClient;
    public double valoareComanda;
    public int idStatus;

    public String textStatus = "-";

    public long getIdComandaClient(){
        return idComandaClient;
    }

    public void setIdComandaClient(long idComandaClient){
        this.idComandaClient = idComandaClient;
    }

    public long getNumarComanda(){
        return numarComanda;
    }

    public void setNumarComanda(long numarComanda){
        this.numarComanda = numarComanda;
    }

    public long getIdClient() {
        return idClient;
    }

    public void setIdClient(long idClient){
        this.idClient = idClient;
    }

    public double getValoareComanda(){
        return valoareComanda;
    }

    public void setValoareComanda(double valoareComanda){
        this.valoareComanda = valoareComanda;
    }

    public int getIdStatus(){
        return idStatus;
    }

    public void setIdStatus(int idStatus){
        this.idStatus = idStatus;
    }

    public String getTextStatus(){
        return textStatus;
    }

    public void setTextStatus(String textStatus){
        this.textStatus = textStatus;
    }

    public Comanda(){

    }
}
