package ws.model;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.Timestamp;
import java.util.ArrayList;

import ws.services.OffreClientService;

public class OffreActif {
    String nom;
    String priorite;
    String numero;
    String idoffreclient;
    String idoffre;
    String idclient;
    Timestamp date_achat;
    Timestamp date_expiration;
    // BigDecimal prix_unitaire;
    private ArrayList<DetailOffreClientComplet> details;

    public void getDetailsInDb(Connection con) throws Exception {
        OffreClient offre = OffreClientService.getOffreClientById(con, getIdoffreclient());
        ArrayList<DetailOffreClientComplet> docomplets = offre.getAllDetailOffreClientComplet(con);
        setDetails(docomplets);
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public String getPriorite() {
        return priorite;
    }

    public void setPriorite(String priorite) {
        this.priorite = priorite;
    }

    public String getIdoffreclient() {
        return idoffreclient;
    }

    public void setIdoffreclient(String idoffreclient) {
        this.idoffreclient = idoffreclient;
    }

    public String getIdoffre() {
        return idoffre;
    }

    public void setIdoffre(String idoffre) {
        this.idoffre = idoffre;
    }

    public String getIdclient() {
        return idclient;
    }

    public void setIdclient(String idclient) {
        this.idclient = idclient;
    }

    public Timestamp getDate_achat() {
        return date_achat;
    }

    public void setDate_achat(Timestamp date_achat) {
        this.date_achat = date_achat;
    }

    public Timestamp getDate_expiration() {
        return date_expiration;
    }

    public void setDate_expiration(Timestamp date_expiration) {
        this.date_expiration = date_expiration;
    }


    public String getNumero() {
        return numero;
    }

    public void setNumero(String numero) {
        this.numero = numero;
    }

    @Override
    public String toString() {
        return "OffreActif [date_achat=" + date_achat + ", date_expiration=" + date_expiration + ", details=" + details
                + ", idclient=" + idclient + ", idoffre=" + idoffre + ", idoffreclient=" + idoffreclient + ", nom="
                + nom + ", numero=" + numero + ", priorite=" + priorite + "]";
    }

    public ArrayList<DetailOffreClientComplet> getDetails() {
        return details;
    }

    public void setDetails(ArrayList<DetailOffreClientComplet> details) {
        this.details = details;
    }

    // public BigDecimal getPrix_unitaire() {
    //     return prix_unitaire;
    // }

    // public void setPrix_unitaire(BigDecimal prix_unitaire) {
    //     this.prix_unitaire = prix_unitaire;
    // }
}