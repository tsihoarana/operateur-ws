package ws.services;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import core.afQuery;
import operation.afReadOperation;
import ws.model.*;
import ws.template_json.MvolaJSON;
import ws.tools.TimeManipulation;
import ws.tools.Tools;

public class MvolaService {
    /**
     * Recupere tous les mvolas presents groupees par idclient, numero
     * @param connection
     * @return
     * @throws Exception
     */
    public static ArrayList<MvolaOrCreditTotal> getAllMvolaTotal (Connection connection) throws Exception {
        afQuery query = afQuery.use(connection);
        afReadOperation read_op = query.of(new MvolaOrCreditTotal(), "MvolaTotal").select();
        ArrayList<MvolaOrCreditTotal> result = read_op.<MvolaOrCreditTotal>get();

        // should occur only and only if there is no user
        if (result.size() == 0)
            throw new Exception("Aucun MvolaTotal correspondant");

        return result;
    }

    /**
     * Recupere le total mvola d'un numero en particulier
     * @param con
     * @param numero
     * @return
     * @throws Exception
     */
    public static MvolaOrCreditTotal getMvolaTotalFor (Connection con, String numero) throws Exception {
        Tools.numeroChecker(numero);
        afQuery query = afQuery.use(con);
        ArrayList<MvolaOrCreditTotal> result = query.of(new MvolaOrCreditTotal(), "MvolaTotal")
                                            .select()
                                            .where("numero = ?", new String[] { numero })
                                            .<MvolaOrCreditTotal>get();

        // should occur only and only if the given number doesnt exist
        if (result.size() == 0)
            throw new Exception("Aucun MvolaTotal correspondant au numero = " + numero);

        return result.get(0);
    }

    /**
     * Recupere toutes les transactions Mvola d'un numero en fonction d'un filtre
     * @param con
     * @param numero peut etre nulle (filtre tout)
     * @param filtre peut avoir la valeur "all", "valide" ou "invalide"
     * @return
     * @throws Exception
     */
    public static ArrayList<MvolaWithNumero> getAllMvolaRowsFor (Connection con, String numero, String filtre) throws Exception {
        afQuery query = afQuery.use(con);
        afReadOperation read_op = query.of(new MvolaWithNumero()).select();
        String[] values = new String[] {};
        String where = "";
        if (numero != null) {
            Tools.numeroChecker(numero);
            where += "numero = ?";
            values = new String[] {numero};
        }

        if ("all".equals(filtre)) {
            if (numero != null)
                read_op.where(where, values);
        } else if ("valide".equals(filtre)) {
            if (!where.equals(""))
                where += " AND ";
            read_op.where(where + "etat = 1", values);
        } else if ("invalide".equals(filtre)) {
            if (!where.equals(""))
                where += " AND ";
            read_op.where(where + "etat = 0", values);
        } else {
            throw new Exception("Erreur. le filtre doit etre parmi all, valide ou invalide");
        }
        
        ArrayList<MvolaWithNumero> result = read_op.<MvolaWithNumero>get();
        return result; // size () == 0 is allowed
    }

    /**
     * Valide un mvola en fonction de son idmvola (effectue un update)
     * @param con
     * @param idmvola
     * @throws Exception
     */
	public static void valider (Connection con, String idmvola) throws Exception {
        Map<String, Object> new_values = new HashMap<>();
        new_values.put("etat", CustomConfig.STATE_VALID);

        afQuery query = afQuery.use(con);
        query.of(new Mvola())
            .update(new_values)
            .where("idmvola = ?", new String[] { idmvola })
            .end();
    }

    /**
     * Valide un mvola en fonction de son idmvola (effectue un update)
     * @param con
     * @param idmvola
     * @throws Exception
     */
	public static void annuler (Connection con, String idmvola) throws Exception {
        Map<String, Object> new_values = new HashMap<>();
        new_values.put("etat", CustomConfig.STATE_REJECTED);

        afQuery query = afQuery.use(con);
        query.of(new Mvola())
            .update(new_values)
            .where("idmvola = ?", new String[] { idmvola })
            .end();
    }
    
    /**
     * Effectuer le depot ou retrait d'un mvola en fonction d'un JSON de meme structure que MvolaJSON
     * @param con
     * @param input donnees formulaire
     * @param action peut etre "depot" ou "retrait"
     * @throws Exception
     */
	public static void operer(Connection con, MvolaJSON input, String action, int config_validation) throws Exception {
        input.validateOrThrows(); // valide les donnees si correct

        Mvola mvola = new Mvola();
        mvola.setIdmvola(afQuery.PG_DEFAULT);
        mvola.setIdclient(input.getIdclient());
        mvola.setMontant(new BigDecimal(input.getMontant()));
        if (config_validation == CustomConfig.IGNORE_VALIDATION) {
            mvola.setEtat(CustomConfig.STATE_VALID);
        } else {
            mvola.setEtat(CustomConfig.STATE_NOT_VALID_YET);
        }

        if ("depot".equals(action))
            mvola.setSens(CustomConfig.DEPOT);
        else if ("retrait".equals(action))
            mvola.setSens(CustomConfig.RETRAIT);
        else
            throw new Exception("L'action doit etre soit retrait soit depot");

        operer(con, mvola);
    }

    /**
     * Effectuer l'ajout direct d'un mvola
     * @param con
     * @param input donnees formulaire
     * @param action peut etre "depot" ou "retrait"
     * @throws Exception
     */
	public static void operer(Connection con, Mvola mvola) throws Exception {
        Timestamp now = TimeManipulation.getNow();
        mvola.setDate_transac(now);
        mvola.save(con);
	}
}