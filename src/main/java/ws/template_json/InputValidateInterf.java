package ws.template_json;

/**
 * Toute classe implementant cette interface devrait etre logiquement une classe pour recuperer les donnees
 * d'un formulaire.
 */
public interface InputValidateInterf {
    /**
     * Valide la classe qui implemente cette interface et throw en cas d'echec
     * @throws Exception
     */
    public void validateOrThrows () throws Exception;
}