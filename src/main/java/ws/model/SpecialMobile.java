package ws.model;

/**
 * Classe permettant juste de recuperer les lignes listant les offres special mobile possible
 * (INSTAGRAM, FACEBOOK, WHATSAPP, ...etc)
 */
public class SpecialMobile {
    String idspecialmobile;
    String nom;

    public String getIdspecialmobile() {
        return idspecialmobile;
    }

    public void setIdspecialmobile(String idspecialmobile) {
        this.idspecialmobile = idspecialmobile;
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    @Override
    public String toString() {
        return "SpecialMobile [idspecialmobile=" + idspecialmobile + ", nom=" + nom + "]";
    }
}