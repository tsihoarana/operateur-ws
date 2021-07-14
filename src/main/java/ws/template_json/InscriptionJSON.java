package ws.template_json;

import ws.tools.Tools;

public class InscriptionJSON implements InputValidateInterf {
	String numero = null;
	String nom = null;
	String prenom = null;
	String password = null;
	String date_naissance = null;

    @Override
    public void validateOrThrows() throws Exception {
        Tools.numeroChecker(getNumero());
        Tools.emptyOrNullChecker(numero, "numero");
        Tools.emptyOrNullChecker(nom, "nom");
        Tools.emptyOrNullChecker(prenom, "prenom");
        Tools.emptyOrNullChecker(password, "password");
        Tools.emptyOrNullChecker(date_naissance, "date_naissance");

        // throws if the specified value is not valid
        Tools.str_to_Date(date_naissance);
    }

    public String getNumero() {
        return numero;
    }

    public void setNumero(String numero) {
        this.numero = numero;
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public String getPrenom() {
        return prenom;
    }

    public void setPrenom(String prenom) {
        this.prenom = prenom;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getDate_naissance() {
        return date_naissance;
    }

    public void setDate_naissance(String date_naissance) {
        this.date_naissance = date_naissance;
    }
}