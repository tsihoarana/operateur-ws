package ws.model;

import java.math.BigDecimal;
import java.sql.Timestamp;

public class StatusClient {
	String idclient = null;
	String numero = null;
	String nom = null;
	String prenom = null;
    Timestamp date_naissance = null;
    
    BigDecimal total_mvola = new BigDecimal(0);
    int reste_mvola_invalide = 0;

    BigDecimal total_credit = new BigDecimal(0);

    public void buildUsing (Client client, BigDecimal total_mvola, int reste_mvola_invalide, BigDecimal total_credit) {
        this.setIdclient(client.getIdclient());
        this.setNumero(client.getNumero());
        this.setNom(client.getNom());
        this.setPrenom(client.getPrenom());
        this.setDate_naissance(client.getDate_naissance());

        this.setTotal_mvola(total_mvola);
        this.setReste_mvola_invalide(reste_mvola_invalide);

        this.setTotal_credit(total_credit);
    }

    public String getIdclient() {
        return idclient;
    }

    public void setIdclient(String idclient) {
        this.idclient = idclient;
    }

    public String getNumero() {
        return numero;
    }

    public void setNumero(String numero) {
        this.numero = numero;
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public String getPrenom() {
        return prenom;
    }

    public void setPrenom(String prenom) {
        this.prenom = prenom;
    }

    public Timestamp getDate_naissance() {
        return date_naissance;
    }

    public void setDate_naissance(Timestamp date_naissance) {
        this.date_naissance = date_naissance;
    }

    public BigDecimal getTotal_mvola() {
        return total_mvola;
    }

    public void setTotal_mvola(BigDecimal total_mvola) {
        this.total_mvola = total_mvola;
    }

    public int getReste_mvola_invalide() {
        return reste_mvola_invalide;
    }

    public void setReste_mvola_invalide(int reste_mvola_invalide) {
        this.reste_mvola_invalide = reste_mvola_invalide;
    }

    public BigDecimal getTotal_credit() {
        return total_credit;
    }

    public void setTotal_credit(BigDecimal total_credit) {
        this.total_credit = total_credit;
    }
}