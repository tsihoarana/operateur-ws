package ws.model;


import java.math.BigDecimal;
import java.sql.Timestamp;

/**
 * Classe map attaquant une vue
 */

public class OffreClientDetails {
	String idoffreclient;
	String numero;
	String idoffre;
	String nom;
	int duree_heure;
	Timestamp date_achat;
	Timestamp fin_offre;
	BigDecimal valeur;

	public OffreClientDetails () {}

	public String getNumero() {
		return numero;
	}
	public void setNumero(String numero) {
		this.numero = numero;
	}

	public String getIdoffre () {
		return this.idoffre;
	}
	public void setIdoffre (String idoffre) {
		this.idoffre = idoffre;
	}

	public String getNom () {
		return this.nom;
	}
	public void setNom (String nom) {
		this.nom = nom;
	}

	public int getDuree_heure () {
		return this.duree_heure;
	}
	public void setDuree_heure (int duree_heure) {
		this.duree_heure = duree_heure;
	}

	public Timestamp getDate_achat () {
		return this.date_achat;
	}
	public void setDate_achat (Timestamp date_achat) {
		this.date_achat = date_achat;
	}

	public Timestamp getFin_offre () {
		return this.fin_offre;
	}
	public void setFin_offre (Timestamp fin_offre) {
		this.fin_offre = fin_offre;
	}

	public BigDecimal getValeur() {
		return valeur;
	}

	public void setValeur(BigDecimal valeur) {
		this.valeur = valeur;
	}

	public String getIdoffreclient() {
		return idoffreclient;
	}

	public void setIdoffreclient(String idoffreclient) {
		this.idoffreclient = idoffreclient;
	}
}