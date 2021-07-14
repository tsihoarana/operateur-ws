package ws.model;

/**
 * Cette classe servira pour stocker les configurations
 */
public class CustomConfig {

    public static final int TOKEN_DAYS = 365;
    // mvola et credit
    public static final int DEPOT = 1;
    public static final int RETRAIT = -1;
    // mvola
	public static final int STATE_REJECTED = -1;
	public static final int STATE_NOT_VALID_YET = 0;
	public static final int STATE_VALID = 1;
	public static final int IGNORE_VALIDATION = 0;
	public static final int WITH_VALIDATION = 1;
	public static final int CODE_ADMIN = 1;
    public static final int CODE_BASIC_USER = 0;
    
    // offre types
    public static final String[] OFFRE_TYPE_DETAILS = new String [] {
        "INTERNET", "SMS", "APPEL_INTERNE", "APPEL_EXTERNE", 
        "SPECIAL_MOBILE" // rassemble les offres special mobile
    };

	public static final String CODE_ID_OFFRE_MORA = "TYPEOF1"; // only last 24h (can be or not a full day)


    
    // nombre resultat recherche
    public static final int RESULT_PER_PAGE = 5; 


    public static final String mongo_url = "mongodb://localhost:27017";
    public static final String pg_url = "jdbc:postgresql://localhost/operateur";
    public static final String pg_user = "postgres";
    public static final String pg_password = "1234";
    
    // HEROKU credentials, DO NOT REMOVE !
    // public static final String pg_url = "jdbc:postgresql://ec2-54-87-34-201.compute-1.amazonaws.com:5432/dcqn790s08mh2e";
    // public static final String pg_user = "yevuumhstdtaiz";
    // public static final String pg_password = "e31fcf1387aae666846a053cda301ec46e05cb60b0b41477a890902097a0c0e7";
	
    // ATLAS MONGODB, DO NOT REMOVE
    // public static final String mongo_url = "mongodb+srv://operateur:1234@operateur-cluster.xuzpo.mongodb.net/operateur?retryWrites=true&w=majority";
}