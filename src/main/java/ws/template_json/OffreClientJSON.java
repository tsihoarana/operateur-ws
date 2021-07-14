package ws.template_json;

import ws.tools.Tools;
import java.math.BigDecimal;
import java.sql.Timestamp;

public class OffreClientJSON implements InputValidateInterf {
    String idoffre = null;
    String idclient = null;
    String date_achat = null;

    @Override
    public void validateOrThrows() throws Exception {
        Tools.emptyOrNullChecker(getIdoffre(), "idoffre");
        Tools.emptyOrNullChecker(getIdclient(), "idclient");
        Tools.emptyOrNullChecker(getDate_achat(), "date_achat");
        Tools.str_to_Date(getDate_achat());
   }

    public String getIdoffre() {
        return idoffre;
    }

    public void setIdoffre(String idoffre) {
        this.idoffre = idoffre;
    }

    public String getIdclient() {
        return idclient;
    }

    public void setIdclient(String idclient) {
        this.idclient = idclient;
    }

    public String getDate_achat() {
        return date_achat;
    }

    public void setDate_achat(String date_achat) {
        this.date_achat = date_achat;
    }

    
}