package ws.template_json;

import java.math.BigDecimal;

import ws.tools.Tools;

public class DetailOffreJSON implements InputValidateInterf {
    // type_detail |  valeur
    String type_detail = null;
    BigDecimal valeur = null;

    @Override
    public void validateOrThrows() throws Exception {
        Tools.emptyOrNullChecker(getType_detail(), "type_detail");
        Tools.nullChecker(getValeur(), "valeur");
		int value = getValeur().intValue();
		if (value <= 0) {
			if (value != -1) // not unlimited
				throw new Exception("Valeur " + type_detail + " invalide");
		}
    }

    public String getType_detail() {
        return type_detail;
    }

    public void setType_detail(String type_detail) {
        this.type_detail = type_detail;
    }

    public BigDecimal getValeur() {
        return valeur;
    }

    public void setValeur(BigDecimal valeur) {
        this.valeur = valeur;
    }
}