package ws.model;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.Timestamp;

import core.afQuery;
import core.afQueryable;

public class Credit implements afQueryable {
	String idcredit = null;
	String idclient = null;
	BigDecimal montant = null;
	int sens = 0;
	Timestamp date_transac = null;

	public Credit() {
	}

	public Credit(String idcredit, String idclient, BigDecimal montant, int sens, Timestamp date_transac) {
		setIdcredit(idcredit);
		setIdclient(idclient);
		setMontant(montant);
		setSens(sens);
		setDate_transac(date_transac);
	}

	public void setIdcredit(String idcredit) {
		this.idcredit = idcredit;
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

	public void setDate_transac(Timestamp date_transac) {
		this.date_transac = date_transac;
	}

	public String getIdcredit() {
		return this.idcredit;
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

	public Timestamp getDate_transac() {
		return this.date_transac;
	}

	@Override
	public String toString() {
		return "[ idcredit = " + idcredit + " idclient = " + idclient + " montant = " + montant + " sens = " + sens
				+ " date_transac = " + date_transac + " ]";
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