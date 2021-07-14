package ws.services;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.sql.Connection;

import core.afQuery;
import operation.afReadOperation;
import ws.tools.TimeManipulation;
import ws.model.BasicStats;
import ws.model.DailyTransacInfos;
import ws.model.DataTransacList;
import ws.model.TotalAge;
import ws.model.TotalAmount;

/**
 * Service for advanced stats
 */
public class StatsService {
    /***
     * Verifie ou jette une exception si la table est != de Credit / Mvola
     * @param table doit avoir Credit ou Mvola comme valeur (case sensitive)
     * @throws Exception Si la verification echoue
     */
    public static void checkIfCreditOrMvola (String table) throws Exception {
        if (table == null || (!table.equals("Credit") && !table.equals("Mvola")))
            throw new Exception("La table pour la totalisation doit etre Credit ou Mvola et non " + table);
    }

    /***
     * Recupere la somme de toutes les lignes de transaction en fonction de la table.
     * Si la table est "Mvola" alors la somme se fera sur tous les Mvola avec validation
     * @param con
     * @param table Credit ou Mvola
     * @return
     * @throws Exception
     */
    public static BigDecimal getTotalFor (Connection con, String table) throws Exception {
        afQuery query = afQuery.use(con);
        // in order to avoid sql injections
        // we need to do some checks
        checkIfCreditOrMvola (table); // check or throw

        String sql = "SELECT COALESCE(sum(sens * montant), 0) amount FROM " + table + " WHERE date_transac <= now()";
        if (table.equals("Mvola"))
            sql += " AND etat = 1";

        ArrayList<TotalAmount> outputs = query.run(sql)
                                            .<TotalAmount>get(new TotalAmount());
        
        return outputs.get(0).getAmount();
    }

    /**
     * Recupere l'age moyenne des clients par rapport a NOW()
     * @param con
     * @return
     * @throws Exception
     */
    public static BigDecimal getCurrentAverageAgeAllClients (Connection con) throws Exception {
        afQuery query = afQuery.use(con);
        ArrayList<TotalAge> outputs = query.of(new TotalAge(), "AverageAgeClientsView")
                                            .select()
                                            .<TotalAge>get();
        return outputs.get(0).getAge();
    }

    /***
     * Recupere un objet stat de base (total mvola, credit et age moyenne)
     * @param con
     * @return
     * @throws Exception
     */
    public static BasicStats getBasicStats (Connection con) throws Exception {
        BasicStats stat = new BasicStats();
        stat.setAverage_age(getCurrentAverageAgeAllClients(con));
        stat.setTotal_mvola(getTotalFor(con, "Mvola"));
        stat.setTotal_credit(getTotalFor(con, "Credit"));
        return stat;
    }

    /***
     * Recupere la somme groupee journaliere de toutes les transactions
     * @param con
     * @param table Mvola (valide) ou Credit
     * @param time Si != null alors le filtre se fera avec les dates inferieurs ou egal
     * @return
     * @throws Exception
     */
    public static ArrayList<DailyTransacInfos> getDailyTransacFor (Connection con, String table, Timestamp time) throws Exception {
        // in order to avoid sql injections
        // we need to do some checks
        checkIfCreditOrMvola (table); // check or throw
        afQuery query = afQuery.use(con);
        String view_name = table + "SumGroupByDayView";

        afReadOperation read_op = query.of(new DailyTransacInfos(), view_name)
                                        .select();
        if (time != null)
            read_op.where("date <= ?", new Object[] { time });
        ArrayList<DailyTransacInfos> out = read_op.<DailyTransacInfos>get();
        return out;
    }

    /**
     * Recupere un objet permettant de retenir les transacs groupees journaliere credit et mvola
     * @param con
     * @param time Si != null alors le filtre se fera avec les dates inferieurs ou egal
     * @return
     * @throws Exception
     */
    public static DataTransacList getBothTranscList (Connection con, Timestamp time) throws Exception {
        DataTransacList data = new DataTransacList();
        data.setCredit(getDailyTransacFor(con, "Credit", time));
        data.setMvola(getDailyTransacFor(con, "Mvola", time));
        return data;
    }
}