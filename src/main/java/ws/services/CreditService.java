package ws.services;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.Timestamp;
import java.util.ArrayList;

import core.afQuery;
import operation.afReadOperation;
import ws.exception.NotEnoughMvolaException;
import ws.model.*;
import ws.template_json.CreditJSON;
import ws.template_json.MvolaJSON;
import ws.tools.TimeManipulation;
import ws.tools.Tools;

public class CreditService {
    /**
     * Recupere tous les credits presents groupees par idclient, numero
     * @param connection
     * @return
     * @throws Exception
     */
    public static ArrayList<MvolaOrCreditTotal> getAllCreditTotal (Connection connection) throws Exception {
        afQuery query = afQuery.use(connection);
        afReadOperation read_op = query.of(new MvolaOrCreditTotal(), "CreditTotal").select();
        ArrayList<MvolaOrCreditTotal> result = read_op.<MvolaOrCreditTotal>get();

        // should occur only and only if there is no user
        if (result.size() == 0)
            throw new Exception("Aucun CreditTotal correspondant");

        return result;
    }

    /**
     * Recupere le total credit d'un numero en particulier
     * @param con
     * @param numero
     * @return
     * @throws Exception
     */
    public static MvolaOrCreditTotal getCreditTotalFor (Connection con, String numero) throws Exception {
        Tools.numeroChecker(numero);
        afQuery query = afQuery.use(con);
        ArrayList<MvolaOrCreditTotal> result = query.of(new MvolaOrCreditTotal(), "CreditTotal")
                                            .select()
                                            .where("numero = ?", new String[] { numero })
                                            .<MvolaOrCreditTotal>get();

        // should occur only and only if the given number doesnt exist
        if (result.size() == 0)
            throw new Exception("Aucun CreditTotal correspondant au numero = " + numero);

        return result.get(0);
    }

    /**
     * Recupere toutes les transactions Credit d'un numero en fonction d'un filtre
     * @param con
     * @param numero
     * @return
     * @throws Exception
     */
    public static ArrayList<CreditWithNumero> getAllCreditRowsFor (Connection con, String numero) throws Exception {
        Tools.numeroChecker(numero);
        afQuery query = afQuery.use(con);
        afReadOperation read_op = query.of(new CreditWithNumero()).select();
        String[] values = new String[] { numero };
        
        read_op.where("numero = ?", values);
        
        ArrayList<CreditWithNumero> result = read_op.<CreditWithNumero>get();
        return result; // size () == 0 is allowed
    }

    /**
     * Effectuer le depot ou retrait d'un credit en fonction d'un JSON de meme structure que CreditJSON
     * @param con
     * @param input donnees formulaire
     * @param action peut etre "depot" ou "retrait"
     * @throws Exception
     */
	public static void operer(Connection con, CreditJSON input, String action) throws Exception {
        input.validateOrThrows(); // valide les donnees si correct

        Credit credit = new Credit();
        credit.setIdcredit(afQuery.PG_DEFAULT);
        credit.setIdclient(input.getIdclient());
        credit.setMontant(new BigDecimal(input.getMontant()));
        
        if ("depot".equals(action)) {
            credit.setSens(CustomConfig.DEPOT);

            // depot credit ===> minus mvola (si suffisant)
            StatusClient status = ClientService.getStatusClientComplet(con, input.getIdclient());
            BigDecimal current_mvola = status.getTotal_mvola();
            BigDecimal credit_to_take = credit.getMontant();
            double diff = current_mvola.subtract(credit_to_take).doubleValue();
            
            if (diff < 0)
                throw new NotEnoughMvolaException(current_mvola);

            MvolaJSON mv_input = new MvolaJSON();
            mv_input.setIdclient(input.getIdclient());
            mv_input.setMontant(input.getMontant());
            mv_input.validateOrThrows();
            MvolaService.operer(con, mv_input, "retrait", CustomConfig.IGNORE_VALIDATION);
        } else if ("retrait".equals(action)) {
            credit.setSens(CustomConfig.RETRAIT);
        } else {
            throw new Exception("L'action doit etre soit retrait soit depot");
        }
        Timestamp now = TimeManipulation.getNow();
        credit.setDate_transac(now);
        
        credit.save(con);
    }
}