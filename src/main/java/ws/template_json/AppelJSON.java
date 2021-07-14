package ws.template_json;

import ws.services.ClientService;
import ws.tools.Tools;

public class AppelJSON implements InputValidateInterf {
    String numeroAppelant;
    String numeroCible;
    double duree;

    @Override
    public void validateOrThrows() throws Exception {
        Tools.emptyOrNullChecker(getNumeroAppelant(), "numeroAppelant");
        Tools.emptyOrNullChecker(getNumeroCible(), "numeroCible");
        Tools.negatifOrZeroChecker(getDuree(), "duree");
        if (numeroAppelant.equalsIgnoreCase(numeroCible)) {
            throw new Exception("Le numero a appelle est votre numero");
        }
   }

    public String getNumeroAppelant() {
        return numeroAppelant;
    }

    public void setNumeroAppelant(String numeroAppelant) {
        this.numeroAppelant = numeroAppelant;
    }

    public String getNumeroCible() {
        return numeroCible;
    }

    public void setNumeroCible(String numeroCible) {
        this.numeroCible = numeroCible;
    }

    public double getDuree() {
        return duree;
    }

    public void setDuree(double duree) {
        this.duree = duree;
    }

}