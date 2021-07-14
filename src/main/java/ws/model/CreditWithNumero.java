package ws.model;

import java.math.BigDecimal;
import java.sql.Timestamp;

/**
 * Classe map attaquant une vue
 */
public class CreditWithNumero {
	String numero = null;
	String idcredit = null;
	String idclient = null;
	BigDecimal montant = null;
	int sens = 0;
	Timestamp date_transac = null;

	public String getNumero() {
		return numero;
	}

	public void setNumero(String numero) {
		this.numero = numero;
	}

	public String getIdcredit() {
		return idcredit;
	}

	public void setIdcredit(String idcredit) {
		this.idcredit = idcredit;
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

	public Timestamp getDate_transac() {
		return date_transac;
	}

	public void setDate_transac(Timestamp date_transac) {
		this.date_transac = date_transac;
	}

	@Override
	public String toString() {
		return "CreditWithNumero [date_transac=" + date_transac + ", idclient=" + idclient + ", idcredit=" + idcredit
				+ ", montant=" + montant + ", numero=" + numero + ", sens=" + sens + "]";
	}
}