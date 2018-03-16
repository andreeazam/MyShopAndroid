package com.myshop.andreea.myshop;

public class Produs {
    private long idProdus;
    private String denumireProdus;
    private double pretProdus;
    private int cantitateInStoc;

    public boolean existaProdus = false;
    public boolean existaInStoc = false;

    public Produs(long idProdus, String denumireProdus, double pretProdus, int cantitateInStoc) {
        this.idProdus = idProdus;
        this.denumireProdus = denumireProdus;
        this.pretProdus = pretProdus;
        this.cantitateInStoc = cantitateInStoc;
    }

    public Produs() {

    }

    public long getIdProdus() {
        return idProdus;
    }

    public void setIdProdus(long idProdus) {
        this.idProdus = idProdus;
    }

    public String getDenumireProdus() {
        return denumireProdus;
    }

    public double getPretProdus() {
        return pretProdus;
    }

    public int getCantitateInStoc() {
        return cantitateInStoc;
    }

    public void setCantitateInStoc(int cantitateInStoc) {
        cantitateInStoc = this.cantitateInStoc;
    }

    public boolean getExistaInStoc() {
        return existaInStoc;
    }

    public void setExistaInStoc(boolean existaInStoc) {
        existaInStoc = this.existaInStoc;
    }

    public Produs incarcaDetaliiProdus(long idProdus) {
        Produs pr = new Produs();
        return pr;

    }
}