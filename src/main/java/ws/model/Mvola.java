package ws.model;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.Timestamp;

import core.afQuery;
import core.afQueryable;

public class Mvola implements afQueryable {
	String idmvola = null;
	String idclient = null;
	BigDecimal montant = null;
	int sens = 0;
	int etat = 0;
	Timestamp date_transac = null;

	public Mvola() {
	}

	public Mvola(String idmvola, String idclient, BigDecimal montant, int sens, int etat, Timestamp date_transac) {
		setIdmvola(idmvola);
		setIdclient(idclient);
		setMontant(montant);
		setSens(sens);
		setEtat(etat);
		setDate_transac(date_transac);
	}

	public void setIdmvola(String idmvola) {
		this.idmvola = idmvola;
	}

	public void setIdclient(String idclient) {
		this.idclient = idclient;
	}

	public void setMontant(BigDecimal montant) {
		this.montant = montant;
	}

	public void setSens(int sens) {
		this.sens = sens;
	}

	public void setEtat(int etat) {
		this.etat = etat;
	}

	public void setDate_transac(Timestamp date_transac) {
		this.date_transac = date_transac;
	}

	public String getIdmvola() {
		return this.idmvola;
	}

	public String getIdclient() {
		return this.idclient;
	}

	public BigDecimal getMontant() {
		return this.montant;
	}

	public int getSens() {
		return this.sens;
	}

	public int getEtat() {
		return this.etat;
	}

	public Timestamp getDate_transac() {
		return this.date_transac;
	}

	@Override
	public String toString() {
		return "[ idmvola = " + idmvola + " idclient = " + idclient + " montant = " + montant + " sens = " + sens
				+ " etat = " + etat + " date_transac = " + date_transac + " ]";
	}

	@Override
	public int save(Connection connection) throws Exception {
		return afQuery.use(connection)
					.of(this)
					.insert()
					.end();
	}

	@Override
	public int remove(Connection connection) throws Exception {
		throw new UnsupportedOperationException("TODO : METHODE A DEFINIR");
	}
}