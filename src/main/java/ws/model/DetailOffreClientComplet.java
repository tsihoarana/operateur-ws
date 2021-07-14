package ws.model;

import java.math.BigDecimal;

public class DetailOffreClientComplet {
    String iddetailoffreclient = null;
    String idoffreclient = null;
    String iddetailoffre = null;
    BigDecimal valeur_actuel = null;
    String type_detail = null;
    String nom = null;

    public String getIddetailoffreclient() {
        return iddetailoffreclient;
    }

    public void setIddetailoffreclient(String iddetailoffreclient) {
        this.iddetailoffreclient = iddetailoffreclient;
    }

    public String getIdoffreclient() {
        return idoffreclient;
    }

    public void setIdoffreclient(String idoffreclient) {
        this.idoffreclient = idoffreclient;
    }

    public String getIddetailoffre() {
        return iddetailoffre;
    }

    public void setIddetailoffre(String iddetailoffre) {
        this.iddetailoffre = iddetailoffre;
    }

    public BigDecimal getValeur_actuel() {
        return valeur_actuel;
    }

    public void setValeur_actuel(BigDecimal valeur_actuel) {
        this.valeur_actuel = valeur_actuel;
    }

    public String getType_detail() {
        return type_detail;
    }

    public void setType_detail(String type_detail) {
        this.type_detail = type_detail;
    }

    public DetailOffreClient toDetailOffreClient () {
        return new DetailOffreClient(iddetailoffreclient, idoffreclient, iddetailoffre, valeur_actuel);
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }
}