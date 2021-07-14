package ws.model;

import java.sql.Connection;
import java.sql.Timestamp;

import core.afQuery;
import core.afQueryable;

public class Client implements afQueryable {
	String idclient = null;
	String numero = null;
	String nom = null;
	String prenom = null;
	String password = null;
	Timestamp date_naissance = null;
	int code_type = 0;


	public void setIdclient(String idclient) {
		this.idclient = idclient;
	}

	public void setNumero(String numero) {
		this.numero = numero;
	}

	public void setNom(String nom) {
		this.nom = nom;
	}

	public void setPrenom(String prenom) {
		this.prenom = prenom;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public void setDate_naissance(Timestamp date_naissance) {
		this.date_naissance = date_naissance;
	}

	public String getIdclient() {
		return this.idclient;
	}

	public String getNumero() {
		return this.numero;
	}

	public String getNom() {
		return this.nom;
	}

	public String getPrenom() {
		return this.prenom;
	}

	public String getPassword() {
		return this.password;
	}

	public Timestamp getDate_naissance() {
		return this.date_naissance;
	}
	public int getCode_type() {
		return code_type;
	}

	public void setCode_type(int code_type) {
		this.code_type = code_type;
	}


	@Override
	public int save(Connection connection) throws Exception {
		afQuery query = afQuery.use(connection);
		String sql = "INSERT INTO Client VALUES (DEFAULT, ?, ?, ?, SHA1('" + getPassword() + "'), ?, ?)";
		Object[] values = new Object[]{
			getNumero(), getNom(), getPrenom(), getDate_naissance(), getCode_type()
		};
		System.out.println(sql);
		return query.run(sql, values).end();
	}

	@Override
	public int remove(Connection connection) throws Exception {
		throw new UnsupportedOperationException("TODO : METHODE NON DEFINIE");
	}

	@Override
	public String toString() {
		return "Client [code_type=" + code_type + ", date_naissance=" + date_naissance + ", idclient=" + idclient
				+ ", nom=" + nom + ", numero=" + numero + ", password=" + password + ", prenom=" + prenom + "]";
	}
}