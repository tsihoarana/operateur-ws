package ws.model;

import java.sql.Timestamp;

public class Appel {
    String numeroAppelant;
    String numeroCible;
    double duree;
    Timestamp date;

    public String getNumeroAppelant() {
        return numeroAppelant;
    }

    public void setNumeroAppelant(String numeroAppelant) {
        this.numeroAppelant = numeroAppelant;
    }

    public String getNumeroCible() {
        return numeroCible;
    }

    public void setNumeroCible(String numeroCible) {
        this.numeroCible = numeroCible;
    }

    public double getDuree() {
        return duree;
    }

    public void setDuree(double duree) {
        this.duree = duree;
    }

    public Timestamp getDate() {
        return date;
    }

    public void setDate(Timestamp date) {
        this.date = date;
    }

    public static double getPrixAppel(double t) {
        // return t * CustomConfig.PRIX_APPEL;
        // EFFACER LA LIGNE CI APRES SI NOUVELLE VERSION IMPLEMENTEE
        try {
            throw new UnsupportedOperationException("[ALERTE : TODO] GET Appel.getPrixAppel A DEFINIR");
        } catch (Exception ex) { 
            ex.printStackTrace(); 
            return -1;
        }
    }

    @Override
    public String toString() {
        return "Appel [date=" + date + ", duree=" + duree + ", numeroAppelant=" + numeroAppelant + ", numeroCible="
                + numeroCible + "]";
    }
}