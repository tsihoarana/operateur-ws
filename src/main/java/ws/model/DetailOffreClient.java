package ws.model;

import java.math.BigDecimal;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import core.afQuery;

import core.afQueryable;

public class DetailOffreClient implements afQueryable {
    String iddetailoffreclient = null;
    String idoffreclient = null;
    String iddetailoffre = null;
    BigDecimal valeur_actuel = null;

    public DetailOffreClient() {
    }

    public DetailOffreClient(String iddetailoffreclient, String idoffreclient, String iddetailoffre,
        BigDecimal valeur_actuel) {
        setIddetailoffreclient(iddetailoffreclient);
        setIdoffreclient(idoffreclient);
        setIddetailoffre(iddetailoffre);
        setValeur_actuel(valeur_actuel);
    }

    public void setIddetailoffreclient(String iddetailoffreclient) {
        this.iddetailoffreclient = iddetailoffreclient;
    }

    public void setIdoffreclient(String idoffreclient) {
        this.idoffreclient = idoffreclient;
    }

    public void setIddetailoffre(String iddetailoffre) {
        this.iddetailoffre = iddetailoffre;
    }

    public void setValeur_actuel(BigDecimal valeur_actuel) {
        this.valeur_actuel = valeur_actuel;
    }

    public String getIddetailoffreclient() {
        return this.iddetailoffreclient;
    }

    public String getIdoffreclient() {
        return this.idoffreclient;
    }

    public String getIddetailoffre() {
        return this.iddetailoffre;
    }

    public BigDecimal getValeur_actuel() {
        return this.valeur_actuel;
    }

    public int updateValeur(Connection connection, BigDecimal valeur_actuel) throws Exception {
		afQuery query = afQuery.use(connection);
		Map<String, Object> new_values = new HashMap<>();
		new_values.put("valeur_actuel", valeur_actuel);
		
		int row_affected = query.of(new DetailOffreClient())
								.update(new_values)
								.where("iddetailoffreclient = ?", new String[]{iddetailoffreclient}) 
								.end();
		return row_affected;
		
	}

    
    @Override
    public String toString() {
        return "[ iddetailoffreclient = " + iddetailoffreclient + " idoffreclient = " + idoffreclient
                + " iddetailoffre = " + iddetailoffre + " valeur_actuel = " + valeur_actuel + " ]";
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



    public DetailOffre getDetailOffre(Connection con) throws Exception {
        afQuery query = afQuery.use(con);
        ArrayList<DetailOffre> offres = query.of(new DetailOffre())
                                    .select()
                                    .where("iddetailoffre = ?", new String[] { iddetailoffre })
                                    .<DetailOffre>get();
        
        return offres.get(0);
    }
}