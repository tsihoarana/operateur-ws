package ws.services;

import java.sql.Connection;
import java.sql.Timestamp;
import java.util.ArrayList;

import core.afQuery;
import ws.model.*;
import ws.template_json.OffreClientJSON;
import ws.tools.TimeManipulation;
import ws.tools.Tools;

public class OffreClientService {
    /**
     * Ajout d'un offreclient en fonction d'un JSON de meme structure que OffreClientJSON
     * Les details "OffreClientDetailJSON" devront etre aussi mentionnee
     * @param con
     * @param input donnees formulaire
     * @throws Exception
     */
    public static void ajouter(Connection con, Offre offre, OffreClientJSON input) throws Exception {
        input.validateOrThrows();

        Timestamp date_achat = Tools.str_to_Date(input.getDate_achat());
        
        Timestamp date_exp = null;
        String code_mora = CustomConfig.CODE_ID_OFFRE_MORA;
        if (code_mora.equals(offre.getIdtypeoffre())) {
            // offre mora (only lasts on the buy date)
            String date_part = date_achat.toString().split(" ")[0];
            date_part += " 23:59:59";
            date_exp = Tools.str_to_Date(date_part);
        } else {
            int delta_minute = offre.getDuree().intValue() * 60;
            date_exp = TimeManipulation.addMinutes(date_achat, delta_minute);
        }

        OffreClient offreclient = new OffreClient();
        int next = afQuery.use(con).sequence("OffreClientSeq").nextValue();
        offreclient.setIdoffreclient("OFFRCLI" + next);

        offreclient.setIdoffre(input.getIdoffre());
        offreclient.setIdclient(input.getIdclient());
        offreclient.setDate_achat(date_achat);
        offreclient.setDate_expiration(date_exp);
        
        offreclient.save(con);

        ArrayList<DetailOffre> details = offre.getAllDetailsOffre(con);
        for (DetailOffre detail : details) {
            DetailOffreClient dtof_client = new DetailOffreClient();
            dtof_client.setIddetailoffreclient(afQuery.PG_DEFAULT);

            dtof_client.setIdoffreclient(offreclient.getIdoffreclient()); // root
            dtof_client.setIddetailoffre(detail.getIddetailoffre()); // linked detailoffre
            dtof_client.setValeur_actuel(detail.getValeur()); // initial value

            dtof_client.save(con);
        }
    }

    // offreclient actif
    public static ArrayList<OffreActif> getOffreActif(Connection con, String numero) throws Exception {
        afQuery query = afQuery.use(con);
        String where = "numero = ?";
        ArrayList<OffreActif> offres = query.of(new OffreActif(), "v_OffreActifs")
                                            .select()
                                            .where(where, new Object[]{numero})
                                            .<OffreActif>get();
        for (OffreActif offre : offres)
            offre.getDetailsInDb(con); 
        

        return offres;
    }

    // get offre client by id
    public static OffreClient getOffreClientById(Connection con, String id) throws Exception {
        afQuery query = afQuery.use(con);
        String sql = "idoffreclient = ?";
        ArrayList<OffreClient> offre = query.of(new OffreClient())
                                                    .select()
                                                    .where(sql, new Object[]{id})
                                                    .<OffreClient>get();

        return offre.get(0);
    }

    public static ArrayList<DetailOffreClient> getDetailOffreClient (Connection con, String idoffreclient) throws Exception {
        afQuery query = afQuery.use(con);
        String sql = "idoffreclient = ?";
        ArrayList<DetailOffreClient> details = query.of(new DetailOffreClient())
                                                    .select()
                                                    .where(sql, new Object[]{idoffreclient})
                                                    .<DetailOffreClient>get();

        return details;
    }
}