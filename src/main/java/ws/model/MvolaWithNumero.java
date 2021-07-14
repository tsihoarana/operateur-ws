package ws.model;

import java.math.BigDecimal;
import java.sql.Timestamp;

/**
 * Classe map attaquant une vue
 */
public class MvolaWithNumero {
	String numero = null;
	String idmvola = null;
	String idclient = null;
	BigDecimal montant = null;
	int sens = 0;
	int etat = 0;
	Timestamp date_transac = null;

	public String getNumero() {
		return numero;
	}

	public void setNumero(String numero) {
		this.numero = numero;
	}

	public String getIdmvola() {
		return idmvola;
	}

	public void setIdmvola(String idmvola) {
		this.idmvola = idmvola;
	}

	public String getIdclient() {
		return idclient;
	}

	public void setIdclient(String idclient) {
		this.idclient = idclient;
	}

	public BigDecimal getMontant() {
		return montant;
	}

	public void setMontant(BigDecimal montant) {
		this.montant = montant;
	}

	public int getSens() {
		return sens;
	}

	public void setSens(int sens) {
		this.sens = sens;
	}

	public int getEtat() {
		return etat;
	}

	public void setEtat(int etat) {
		this.etat = etat;
	}

	public Timestamp getDate_transac() {
		return date_transac;
	}

	public void setDate_transac(Timestamp date_transac) {
		this.date_transac = date_transac;
	}

	@Override
	public String toString() {
		return "MvolaWithNumero [date_transac=" + date_transac + ", etat=" + etat + ", idclient=" + idclient
				+ ", idmvola=" + idmvola + ", montant=" + montant + ", numero=" + numero + ", sens=" + sens + "]";
	}
}