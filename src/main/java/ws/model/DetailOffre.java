package ws.model;

import java.math.BigDecimal;
import java.sql.Connection;
import java.util.Arrays;
import java.util.stream.Stream;

import core.afQuery;
import core.afQueryable;
import ws.services.OffreService;

public class DetailOffre implements afQueryable {
    String iddetailoffre = null;
    String idoffre = null;
    String type_detail = null;
    BigDecimal valeur = null;
    BigDecimal prix_unitaire = null;

    public void setIdoffre(String idoffre) {
        this.idoffre = idoffre;
    }

    public void setType_detail(String type_detail) {
        this.type_detail = type_detail;
    }

    public void setValeur(BigDecimal valeur) {
        this.valeur = valeur;
    }

    public String getIddetailoffre() {
        return this.iddetailoffre;
    }

    public String getIdoffre() {
        return this.idoffre;
    }

    public String getType_detail() {
        return this.type_detail;
    }

    public BigDecimal getValeur() {
        return this.valeur;
    }


    public void selfValidate (Connection con) throws Exception {
        String [] a = CustomConfig.OFFRE_TYPE_DETAILS;
        String [] b = OffreService.getAllSpecialMobile(con);
        String [] all_allowed = Stream.of(a, b)
                                .flatMap(Stream::of)
                                .toArray(String[]::new);

        String joined = Arrays.stream(all_allowed)
                            .reduce("", (acc, x) -> acc + ", " + x);

        int fail_count = 0;
        for (String allowed : all_allowed)
            if (allowed.compareToIgnoreCase(type_detail) != 0)
                fail_count++;
        if (fail_count == all_allowed.length)
            throw new Exception ("type_detail doit etre " + joined + " et non " + type_detail);
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

    public void setIddetailoffre(String iddetailoffre) {
        this.iddetailoffre = iddetailoffre;
    }

    public BigDecimal getPrix_unitaire() {
        return prix_unitaire;
    }

    public void setPrix_unitaire(BigDecimal prix_unitaire) {
        this.prix_unitaire = prix_unitaire;
    }

    @Override
    public String toString() {
        return "DetailOffre [iddetailoffre=" + iddetailoffre + ", idoffre=" + idoffre + ", prix_unitaire="
                + prix_unitaire + ", type_detail=" + type_detail + ", valeur=" + valeur + "]";
    }
}