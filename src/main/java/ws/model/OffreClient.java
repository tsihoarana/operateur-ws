package ws.model;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.sql.Connection;

import core.afQuery;
import core.afQueryable;

public class OffreClient implements afQueryable {
    String idoffreclient = null;
    String idoffre = null;
    String idclient = null;
    Timestamp date_achat = null;
    Timestamp date_expiration = null;

    public OffreClient () {}
    public OffreClient (String idoffreclient, String idoffre, String idclient, Timestamp date_achat, Timestamp date_expiration) {
        setIdoffreclient (idoffreclient);
        setIdoffre (idoffre);
        setIdclient (idclient);
        setDate_achat (date_achat);
        setDate_expiration (date_expiration);
    }

    public void setIdoffreclient (String idoffreclient) {
        this.idoffreclient = idoffreclient;
    }
    public void setIdoffre (String idoffre) {
        this.idoffre = idoffre;
    }
    public void setIdclient (String idclient) {
        this.idclient = idclient;
    }
    public void setDate_achat (Timestamp date_achat) {
        this.date_achat = date_achat;
    }
    public void setDate_expiration (Timestamp date_expiration) {
        this.date_expiration = date_expiration;
    }

    public String getIdoffreclient () {
        return this.idoffreclient;
    }
    public String getIdoffre () {
        return this.idoffre;
    }
    public String getIdclient () {
        return this.idclient;
    }
    public Timestamp getDate_achat () {
        return this.date_achat;
    }
    public Timestamp getDate_expiration () {
        return this.date_expiration;
    }

    public ArrayList<DetailOffreClient> getAllDetailOffreClient(Connection con) throws Exception {
        afQuery query = afQuery.use(con);
        return query.of(new DetailOffreClient())
                    .select()
                    .where("idoffreclient = ?", new String[] {getIdoffreclient()})
                    .<DetailOffreClient>get();
    }

    public ArrayList<DetailOffreClientComplet> getAllDetailOffreClientComplet(Connection con) throws Exception {
        afQuery query = afQuery.use(con);
        String where = "idoffreclient = ?";
        ArrayList<DetailOffreClientComplet> detail = query.of(new DetailOffreClientComplet(), "v_DetailOffreClientComplet")
                                                    .select()
                                                    .where(where, new Object[]{getIdoffreclient()})
                                                    .<DetailOffreClientComplet>get();

        return detail;
    }
    
    @Override
    public String toString () {
        return "[ idoffreclient = " + idoffreclient + " idoffre = " + idoffre + " idclient = " + idclient + " date_achat = " + date_achat + " date_expiration = " + date_expiration + " ]";
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
		afQuery query = afQuery.use(connection);
		query.run("DELETE FROM OffreClient WHERE idoffre = ?", new String[]{ getIdoffreclient() })
            .end();
		
		return 1;
	}
}