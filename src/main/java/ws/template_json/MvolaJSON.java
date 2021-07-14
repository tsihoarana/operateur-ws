package ws.template_json;

import ws.tools.Tools;

public class MvolaJSON implements InputValidateInterf {
	String idclient = null;
	double montant = 0;

    @Override
    public void validateOrThrows() throws Exception {
        Tools.emptyOrNullChecker(getIdclient(), "idclient");
        Tools.negatifOrZeroChecker(getMontant(), "montant");
   }

    public String getIdclient() {
        return idclient;
    }

    public void setIdclient(String idclient) {
        this.idclient = idclient;
    }

    public double getMontant() {
        return montant;
    }

    public void setMontant(double montant) {
        this.montant = montant;
    }
}