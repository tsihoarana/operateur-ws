package ws.services;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.*;

import core.afQuery;

import ws.model.CustomConfig;
import ws.model.CustomConfigData;
import ws.model.CustomMapListData;

/**
 * Petit service pour recuperer une connexion BDD
 */
public class PGService {
    /**
     * Ouvre une nouvelle connexion
     */
    public static Connection getConnection () throws SQLException {
        String url = CustomConfig.pg_url;
        String user = CustomConfig.pg_user;
        String password = CustomConfig.pg_password;
        
		return DriverManager.getConnection(url, user, password);
    }

    /**
     * Ferme une connexion
     */
    public static void close (Connection connection) {
        if (connection != null) {
            try {
                connection.close();
            } catch (SQLException e) {
                System.out.println(e.getMessage());
            }
        }
    }

    /**
     * Rollback une transaction
     */
    public static void rollback (Connection connection) {
        if (connection != null) {
            try {
                connection.rollback();
            } catch (SQLException e) {
                System.out.println(e.getMessage());
            }
        } 
    }

    /**
     * Recupere toutes les configurations stockees sur la BDD liees aux offres
     * du genre cle => prix_defaut
     * -- Note : Utile pour determiner les valeurs par defaut de certaines cles
     * @throws Exception
     */
    public static Map<String, BigDecimal> getAllConfig(Connection con) throws Exception {
        afQuery query = afQuery.use(con);
        ArrayList<CustomConfigData> datas = query.of(new CustomConfigData())
                                                .select()
                                                .<CustomConfigData>get();
        Map<String, BigDecimal> map = new HashMap<>();
        for (CustomConfigData row : datas)
            map.put(row.getKey(), row.getValue());
        return map;
    }

    /**
     * Recupere toutes les configs du type une cle => List<String> 
     * -- Note : on ne devrait utiliser ceci que lors d'un ajout offre ou similaire
     * @throws Exception
     */
    public static Map<String, List<String>> getAllConfigHavingValueList(Connection con) throws Exception {
        afQuery query = afQuery.use(con);
        ArrayList<CustomMapListData> datas = query.of(new CustomMapListData())
                                                .select()
                                                .<CustomMapListData>get();
        Map<String, List<String>> map = new HashMap<>();
        for (CustomMapListData row : datas) {
            String key = row.getKey(), value = row.getValue();
            if (map.getOrDefault(key, null) == null) {
                ArrayList<String> list = new ArrayList<>();
                map.put(key, list);
            }
            map.get(key).add(value);
        }

        // map.forEach((key, value) -> {
        //     System.out.println(key + " = " + value);
        // });
        return map;
    }

    /**
     * Recupere une cle particuliere
     *  PRIX_APPEL_INTERNE, PRIX_APPEL_EXTERNE
     * , PRIX_APPEL_INTERNE_CREDIT, PRIX_APPEL_EXTERNE_CREDIT
     * , PRIX_SMS_CREDIT,  PRIX_INTERNET_CREDIT
     * @throws Exception
     */
    public static BigDecimal getConfigKeyOrNull(Connection con, String key) throws Exception {
        return getAllConfig(con).getOrDefault(key, null);
    }

}