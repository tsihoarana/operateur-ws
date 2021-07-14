package ws.template_json;

import ws.tools.Tools;

public class SmsJSON implements InputValidateInterf {
    String numeroEnvoyeur;
    String numeroCible;
    String objet;

    @Override
    public void validateOrThrows() throws Exception {
        Tools.emptyOrNullChecker(getNumeroEnvoyeur(), "numeroEnvoyeur");
        Tools.emptyOrNullChecker(getNumeroCible(), "numeroCible");
        Tools.emptyOrNullChecker(getObjet(), "objet");
        if (numeroEnvoyeur.equalsIgnoreCase(numeroCible)) {
            throw new Exception("Le numero a envoye un sms est votre numero");
        }
   }

    public String getNumeroEnvoyeur() {
        return numeroEnvoyeur;
    }

    public void setNumeroEnvoyeur(String numeroEnvoyeur) {
        this.numeroEnvoyeur = numeroEnvoyeur;
    }

    public String getNumeroCible() {
        return numeroCible;
    }

    public void setNumeroCible(String numeroCible) {
        this.numeroCible = numeroCible;
    }

    public String getObjet() {
        return objet;
    }

    public void setObjet(String objet) {
        this.objet = objet;
    }
    
}