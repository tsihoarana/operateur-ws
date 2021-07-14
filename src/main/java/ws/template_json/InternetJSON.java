package ws.template_json;

import ws.tools.Tools;

public class InternetJSON implements InputValidateInterf  {
    String numero;
    double valeur;

    @Override
    public void validateOrThrows() throws Exception {
        Tools.emptyOrNullChecker(getNumero(), "numero");
        Tools.negatifOrZeroChecker(getValeur(), "valeur");
   }

    public String getNumero() {
        return numero;
    }

    public void setNumero(String numero) {
        this.numero = numero;
    }

    public double getValeur() {
        return valeur;
    }

    public void setValeur(double valeur) {
        this.valeur = valeur;
    }
}