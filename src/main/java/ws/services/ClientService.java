package ws.services;

import java.sql.Timestamp;
import java.math.BigDecimal;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import core.afQuery;
import ws.exception.TokenException;
import ws.model.Client;
import ws.model.Count;
import ws.model.CustomConfig;
import ws.model.MvolaOrCreditTotal;
import ws.model.MvolaWithNumero;
import ws.model.PageClient;
import ws.model.StatusClient;
import ws.model.Token;
import ws.model.TokenFullData;
import ws.template_json.ConnexionJSON;
import ws.template_json.InscriptionJSON;
import ws.tools.Tools;

public class ClientService {
    /**
     * Verifie l'existence de la paire numero/sha1(password) dans la BDD
     * puis throw en cas d'echec
     * @param con
     * @param input
     * @return
     * @throws Exception
     */
    public static Client checkAndGetUser(Connection con, ConnexionJSON input) throws Exception {
        afQuery query = afQuery.use(con);
        String sql = "SELECT * FROM Client WHERE numero = ? AND password = SHA1('" + input.getPassword() + "')";
        String[] values = new String[] { input.getNumero() };

        ArrayList<Client> result = query.run(sql, values).get(new Client());

        if (result.size() == 0)
            throw new Exception("Echec login pour " + input.getNumero());

        return result.get(0);
    }

    /**
     * Verifie un utilisateur en utilisant son numero/password
     * Retourne un token si la verification est ok
     * @param con
     * @param input
     * @return
     * @throws Exception
     */
    public static Token checkUserPassAndFetchToken(Connection con, ConnexionJSON input) throws Exception {
        Client client = checkAndGetUser(con, input);
        Token token = TokenService.addToken(con, client);
        // token avec un attribut client
        TokenFullData ftoken = token.convertAsFullDataUsing(client);
        return ftoken;
    }

    /**
     * Verifie un token inclu dans les headers et throw directement en cas d'echec de verification
     * @param con
     * @return
     * @throws Exception
     */
    public static void tokenValidateOrThrows(Connection connection, Map<String, String> headers) throws Exception {
        String token = TokenService.extractToken(headers);
        if ( !TokenService.checkToken(connection, token) )
            throw new TokenException("Token recu mais invalide");
    }

    /**
     * Recupere une liste de tous les clients
     * @param con
     * @return
     * @throws Exception
     */
    public static ArrayList<Client> fetchAll (Connection con) throws Exception {
        afQuery query = afQuery.use(con);
        return query.of(new Client()).select().<Client>get();
    }

    /**
     * Ajoute/Inscrit un nouveau Client en fonction des donnes formulaires
     * @param input donnees formulaire
     * @param code_type type Client (admin ou user classique) (config dans CustomConfig.java)
     * @throws Exception
     */
	public static void addClientUsing(InscriptionJSON input, int code_type) throws Exception {
        Connection con = null;
        try {
            con = PGService.getConnection();
        
            Client client = new Client();
            client.setIdclient(afQuery.PG_DEFAULT);
            client.setNom(input.getNom());
            client.setPrenom(input.getPrenom());
            client.setPassword(input.getPassword());
            client.setNumero(input.getNumero());
            client.setCode_type(code_type); // admin 1, client normal 0
            Timestamp date = Tools.str_to_Date(input.getDate_naissance());
            client.setDate_naissance(date);
    
            client.save(con);
        } catch (Exception ex) {
            throw ex;
        } finally {
            PGService.close(con);
        }
    }

    /**
     * Recupere un resume d'un client en particulier en fonction de son id
     * @param idclient
     * @return
     * @throws Exception
     */
    public static StatusClient getStatusClientComplet (Connection con, String idclient) throws Exception {
        Client client = getClientById(con, idclient);

        MvolaOrCreditTotal total_mvola = MvolaService.getMvolaTotalFor(con, client.getNumero());
        ArrayList<MvolaWithNumero> invalides = MvolaService.getAllMvolaRowsFor(con, client.getNumero(), "invalide");
        int invalid_count = invalides.size();

        MvolaOrCreditTotal total_credit = CreditService.getCreditTotalFor(con, client.getNumero());

        StatusClient stat_client = new StatusClient();
        stat_client.buildUsing(client, total_mvola.getTotal(), invalid_count, total_credit.getTotal());

        return stat_client;
    }

    /**
     * Recupere un StatusClient en fonction de son numero
     * @param con
     * @param numero
     * @return
     * @throws Exception
     */
    public static StatusClient getStatusClientCompletByNumero (Connection con, String numero) throws Exception {        
        Client client = getClientByNumero(con, numero);

        MvolaOrCreditTotal total_mvola = MvolaService.getMvolaTotalFor(con, client.getNumero());
        ArrayList<MvolaWithNumero> invalides = MvolaService.getAllMvolaRowsFor(con, client.getNumero(), "invalide");
        int invalid_count = invalides.size();

        MvolaOrCreditTotal total_credit = CreditService.getCreditTotalFor(con, client.getNumero());

        StatusClient stat_client = new StatusClient();
        stat_client.buildUsing(client, total_mvola.getTotal(), invalid_count, total_credit.getTotal());

        return stat_client;
    }

    /**
     * Recupere un client en fonction de son numero
     * @param con
     * @param numero
     * @return
     * @throws Exception
     */
    public static Client getClientByNumero (Connection con, String numero) throws Exception {
        afQuery query = afQuery.use(con);
        ArrayList<Client> clients = query.of(new Client())
                                        .select()
                                        .where("numero = ?", new String[] {numero})
                                        .<Client>get();
        if (clients.size() == 0)
            throw new Exception("Client numero = " + numero + " n'existe pas");
        return clients.get(0);
    }

    /**
     * Recupere un client en fonction de son id
     * @param con
     * @param id
     * @return
     * @throws Exception
     */
    public static Client getClientById (Connection con, String id) throws Exception {
        afQuery query = afQuery.use(con);
        ArrayList<Client> clients = query.of(new Client())
                                        .select()
                                        .where("idclient = ?", new String[] {id})
                                        .<Client>get();
        if (clients.size() == 0)
            throw new Exception("Client id = " + id + " n'existe pas");
        return clients.get(0);
    }


    public static Count getCountClientLike (Connection con, String key) throws Exception {
        afQuery query = afQuery.use(con);
        String format_key = "%" + key + "%";
        String[] where = new String[] {format_key, format_key, format_key};
        ArrayList<Count> count = query.run("select count(*) from client where nom like ? or prenom like ? or numero like ?", where)
                                        .<Count>get(new Count());
        
        return count.get(0);
    }

    public static int countPage(int count, int by) {
        ArrayList<Integer> p = Tools.paging(count, by);
        return p.size();
    }

    public static ArrayList<Client> getClientLikePaginer (Connection con, String key, int limit, int offset) throws Exception {
        afQuery query = afQuery.use(con);
        String format_key = "%" + key + "%";
        Object[] where = new Object[] {format_key, format_key, format_key, limit, offset};
        ArrayList<Client> clients = query.of(new Client())
                                        .select()
                                        .where("nom like ? or prenom like ? or numero like ? limit ? offset ?", where)
                                        .<Client>get();
        if (clients.size() == 0)
            throw new Exception("Aucun resultat");
        return clients;
    }


    /**
     * Verifie si le client possedant ce numero est admin ou non
     * @param con
     * @param numero
     * @throws Exception
     */
	public static void checkIfAdmin(Connection con, String numero) throws Exception {
        afQuery query = afQuery.use(con);
        ArrayList<Client> admins = query.of(new Client())
                                        .select()
                                        .where("numero = ? AND code_type = 1", new String[] {numero})
                                        .<Client>get();
        if (admins.size() == 0)
            throw new Exception("Votre numero " + numero + " n'est pas admin");
	}

    public static PageClient getPageSearch(Connection con, String keyword, int page) throws Exception {
        int res_per_page = CustomConfig.RESULT_PER_PAGE;
        BigDecimal res_count = ClientService.getCountClientLike(con, keyword).getCount();
        int nb_page = ClientService.countPage(res_count.intValue(), res_per_page);
        ArrayList<Client> clients = ClientService.getClientLikePaginer(con, keyword, res_per_page, page * res_per_page);
        PageClient p = new PageClient();
        p.setCount(nb_page - 1);
        p.setList(clients);
        p.setCurrent_page(page + 1);

        return p;
    }
}