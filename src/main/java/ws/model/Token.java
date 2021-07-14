package ws.model;

import java.sql.Connection;
import java.sql.Timestamp;

import core.afQuery;
import core.afQueryable;

public class Token implements afQueryable {
	String idtoken = null;
	String idclient = null;
	int code_type = 0;
	String token_value = null;
	Timestamp expiration = null;

	public Token() {
	}

	public void setIdtoken(String idtoken) {
		this.idtoken = idtoken;
	}

	public void setIdclient(String idclient) {
		this.idclient = idclient;
	}

	public void setToken_value(String token_value) {
		this.token_value = token_value;
	}

	public void setExpiration(Timestamp expiration) {
		this.expiration = expiration;
	}

	public String getIdtoken() {
		return this.idtoken;
	}

	public String getIdclient() {
		return this.idclient;
	}

	public String getToken_value() {
		return this.token_value;
	}

	public Timestamp getExpiration() {
		return this.expiration;
	}

	@Override
	public String toString() {
		return "[ idtoken = " + idtoken + " idclient = " + idclient + " token_value = " + token_value + " expiration = "
				+ expiration + " ]";
	}

	@Override
	public int save(Connection connection) throws Exception {
		return afQuery.use(connection).of(this).insert().end();
	}

	@Override
	public int remove(Connection connection) throws Exception {
		// TODO Auto-generated method stub
		throw new UnsupportedOperationException("UNSUPPORTED METHOD");
	}

	/**
	 * Retourne un obet token identique mais avec un attribut client
	 */
	public TokenFullData convertAsFullDataUsing (Client client) {
		TokenFullData ftoken = new TokenFullData();
		ftoken.setClient(client);
		ftoken.setIdtoken(getIdtoken());
		ftoken.setIdclient(getIdclient());
		ftoken.setToken_value(getToken_value());
		ftoken.setExpiration(getExpiration());
		return ftoken;
	}

	public int getCode_type() {
		return code_type;
	}

	public void setCode_type(int code_type) {
		this.code_type = code_type;
	}
}