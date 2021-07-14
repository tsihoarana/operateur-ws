package ws.template_json;

import ws.tools.*;

/**
 * ** Entite de meme structure que le JSON d'un body de requete
 * , s'utilisera dans la connexion
 */
public class ConnexionJSON implements InputValidateInterf {
    String numero;
    String password;

    public String getNumero() {
        return numero;
    }

    public void setNumero(String numero) {
        this.numero = numero;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    @Override
    public void validateOrThrows () throws Exception {
        Tools.emptyOrNullChecker(getPassword(), "password");
        Tools.numeroChecker(getNumero());
    }
}