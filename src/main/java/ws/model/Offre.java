package ws.model;

import java.math.BigDecimal;
import java.sql.Connection;
import java.util.ArrayList;


import core.afQuery;
import core.afQueryable;

public class Offre implements afQueryable {
    String idoffre = null;
    String idtypeoffre = null;
    String nom = null;
    BigDecimal duree = null;
    BigDecimal prix = null;
    int priorite = 0;

    private ArrayList<DetailOffre> details = new ArrayList<DetailOffre> ();

    public Offre () {}
    public Offre (String idoffre, String idtypeoffre, String nom, BigDecimal duree, BigDecimal prix, int priorite) {
        setIdoffre (idoffre);
        setIdtypeoffre (idtypeoffre);
        setNom (nom);
        setDuree (duree);
        setPrix (prix);
        setPriorite (priorite);
    }

    public void setIdoffre (String idoffre) {
        this.idoffre = idoffre;
    }
    public void setIdtypeoffre (String idtypeoffre) {
        this.idtypeoffre = idtypeoffre;
    }
    public void setNom (String nom) {
        this.nom = nom;
    }
    public void setDuree (BigDecimal duree) {
        this.duree = duree;
    }
    public void setPrix (BigDecimal prix) {
        this.prix = prix;
    }
    public void setPriorite (int priorite) {
        this.priorite = priorite;
    }

    public String getIdoffre () {
        return this.idoffre;
    }
    public String getIdtypeoffre () {
        return this.idtypeoffre;
    }
    public String getNom () {
        return this.nom;
    }
    public BigDecimal getDuree () {
        return this.duree;
    }
    public BigDecimal getPrix () {
        return this.prix;
    }
    public int getPriorite () {
        return this.priorite;
    }
    public ArrayList<DetailOffre> getDetails() {
        return details;
    }
    public void setDetails(ArrayList<DetailOffre> details) {
        this.details = details;
    }

    /**
     * Recupere tous les details de cet offre
     * @param con
     * @return
     * @throws Exception
     */
    public ArrayList<DetailOffre> getAllDetailsOffre(Connection con) throws Exception {
        afQuery query = afQuery.use(con);
        return query.of(new DetailOffre())
                    .select()
                    .where("idoffre = ?", new String[] {getIdoffre()})
                    .<DetailOffre>get();
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
		query.run("DELETE FROM Offre WHERE idoffre = ?", new String[]{ getIdoffre() })
            .end();
		
		return 1;
	}


    @Override
    public String toString () {
        return "[ idoffre = " + idoffre + " idtypeoffre = " + idtypeoffre + " nom = " + nom + " duree = " + duree + " prix = " + prix + " priorite = " + priorite + " ]";
    }

}