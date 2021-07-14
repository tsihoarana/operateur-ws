package ws.services;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.sql.Connection;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Map;

import core.afQuery;
import ws.exception.TokenException;
import ws.model.Client;
import ws.model.CustomConfig;
import ws.model.Token;
import ws.tools.TimeManipulation;

public class TokenService {
    /**
     * Digest SHA1 de base
     * @param str
     * @return
     * @throws Exception
     */
    public static String hash(String str) throws Exception {
        MessageDigest digest = MessageDigest.getInstance("SHA-1");
        digest.reset();
        digest.update(str.getBytes("utf8"));
        return String.format("%040x", new BigInteger(1, digest.digest()));
    }

    /**
     * Construit un token en fonction d'un message
     * + la date actuelle
     * @param message
     * @return
     * @throws Exception
     */
    public static String buildUsing (String message) throws Exception  {
        String now = TimeManipulation.getNow().toString();
        return hash (now + message);
    }

    /**
     * Ajoute un token directement dans la base de donnee
     * @param connection
     * @param client
     * @return
     * @throws Exception
     */
    public static Token addToken (Connection connection, Client client) throws Exception {
        String token_value = buildUsing (client.getNumero() + client.getPassword());
        Token token = new Token();
        Timestamp now = TimeManipulation.getNow();
        
        token.setIdtoken(afQuery.PG_DEFAULT);
        token.setIdclient(client.getIdclient());
        token.setToken_value(token_value);
        token.setCode_type(client.getCode_type());
        int days = CustomConfig.TOKEN_DAYS;
        token.setExpiration(TimeManipulation.addJour(now, days));

        token.save(connection);
        
        return token;
    }
    
    /**
     * Verifie un token en fonction de l'idclient
     * La date d'expiration entre en jeu et sera jugee en fonction de la date
     * de la BDD
     * @param connection
     * @param idclient
     * @param token
     * @return
     * @throws Exception
     */
    public static boolean checkToken (Connection connection, String token) throws Exception {
        afQuery query = afQuery.use(connection);
        String[] input = new String []{token};
        ArrayList<Token> result = query.of(new Token(), "TokenValidView")
                                    .select()
                                    .where("token_value = ?", input)
                                    .<Token>get();
        System.out.println("LENGTH " + result.size());
        return result.size() > 0;
    }

    /**
     * Extrait un token parmis les headers
     * 
     * @param headers
     * @return
     * @throws TokenException
     */
    public static String extractToken(Map<String, String> headers) throws TokenException {
        String key = "authorization";
        if ( !headers.containsKey(key) )
            throw new TokenException("Token introuvable dans le header");
        String auth = headers.get(key);
        String[] chunks = auth.split(" ");
        if (chunks.length != 2)
            throw new TokenException("Bearer introuvable dans " + key);
        if (!chunks[0].toLowerCase().equals("bearer"))
            throw new TokenException("Bearer introuvable dans " + key);
        return chunks[1];
    }
}