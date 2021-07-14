package ws.services;

import java.math.BigDecimal;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import core.afQuery;
import ws.model.*;
import ws.template_json.DetailOffreJSON;
import ws.template_json.OffreJSON;

public class OffreService {
    /**
     * Recupere toutes les textes associees aux offres special mobile
     * c.a.d FACEBOOK, WHATSAPP, INSTAGRAM, ... etc
     * @param con
     * @return
     * @throws Exception
     */
    public static String [] getAllSpecialMobile (Connection con) throws Exception {
        ArrayList<SpecialMobile> list = afQuery.use(con)
                                                .of(new SpecialMobile())
                                                .select()
                                                .<SpecialMobile>get();
        String [] output = list.stream()
                                .map(x -> x.getNom())
                                .toArray(String[]::new);
        return output;
    }

    /**
     * Recupere tous les type d'offre possible avec les durees d'expiration par defaut
     * @param con
     * @return
     * @throws Exception
     */
    public static ArrayList<TypeOffre> getAllTypeOffre (Connection con) throws Exception {
        afQuery query = afQuery.use(con);
        ArrayList<TypeOffre> type_offres = query.of(new TypeOffre())
                                                .select()
                                                .<TypeOffre>get();

        Map<String, List<Integer>> default_duree = new HashMap<>();
        // suggestion des durees
        default_duree.put("TYPEOF1", Arrays.asList(24)); // MORA (24h)
        default_duree.put("TYPEOF2", Arrays.asList()); // SPECIAL_MOBILE (None : indeterminee)
        default_duree.put("TYPEOF3", Arrays.asList(24 * 7, 24 * 30)); // INTERNET (volume defini 1 semaine, mensuel)
        default_duree.put("TYPEOF4", Arrays.asList(24 * 30)); // FIRST (30j)

        for (TypeOffre type : type_offres) {
            String key = type.getIdtypeoffre();
            if (default_duree.containsKey(key)) {
                ArrayList<Integer> temp = new ArrayList<>();
                temp.addAll(default_duree.get(key));
                type.setDefault_duree(temp);
            }
        }

        return type_offres;
    }

    /**
     * Ajouter offre en fonction d'un JSON de meme structure que OffreJSON
     * DOIT ETRE APPELE DANS UN CONTEXTE TRANSACTIONNEL
     * @param con
     * @param input donnees formulaire
     * @throws Exception
     */
    public static void ajouterOffre (Connection con, OffreJSON input) throws Exception {
        input.validateOrThrows(); // valide les donnees si correct
        checkIfTypeDetailCorrectByDefinition (con, input);

        Offre offre = new Offre();
        int next = afQuery.use(con).sequence("OffreSeq").nextValue();
        offre.setIdoffre("OF" + next);
        offre.setIdtypeoffre(input.getIdtypeoffre());
        offre.setNom(input.getNom());
        offre.setDuree(new BigDecimal(input.getDuree()));
        offre.setPrix(new BigDecimal(input.getPrix()));
        offre.setPriorite(input.getPriorite());
        
        offre.save(con);
        ArrayList<DetailOffreJSON> details = input.getDetails();
        for (DetailOffreJSON detail : details) {
            DetailOffre dt_offre = new DetailOffre();
            dt_offre.setIddetailoffre(afQuery.PG_DEFAULT);
            dt_offre.setIdoffre(offre.getIdoffre());
            dt_offre.setType_detail(detail.getType_detail());
            dt_offre.setValeur(detail.getValeur());
            dt_offre.setPrix_unitaire(new BigDecimal(-1));

            dt_offre.selfValidate(con);

            dt_offre.save(con);
        }
    }

    /**
     * Verifie si l'input respecte la definition selon les specifications 
     * * MORA = INTERNET + SMS + APPEL (valeur_offre) 
     * * SPECIAL_MOBILE = SPECIAL_MOBILE (fb, whatsapp) 
     * * OFFRE INTERNET = INTERNET FIRST = INTERNET + SMS + APPEL (valeur offre)
     * TYPEOF1 | MORA
     * TYPEOF2 | SPECIAL_MOBILE
     * TYPEOF3 | INTERNET
     * TYPEOF4 | FIRST
     * 
     * type_detail = "INTERNET", "SMS", "APPEL", "SPECIAL_MOBILE"
     * @throws Exception
     */
    public static void checkIfTypeDetailCorrectByDefinition(Connection con, OffreJSON input) throws Exception {
        Map<String, List<String>> defs = PGService.getAllConfigHavingValueList(con);

        Map<String, List<String>> rules = new HashMap<> ();
        // ex MORA = INTERNET, APPEL_INTERNE, APPEL_EXTERNE, SMS
        rules.put("TYPEOF1", defs.get("MORA"));
        rules.put("TYPEOF2", Arrays.asList(OffreService.getAllSpecialMobile(con)));
        rules.put("TYPEOF3", defs.get("INTERNET"));
        rules.put("TYPEOF4", defs.get("FIRST"));

        List<String> definition = rules.getOrDefault(input.getIdtypeoffre(), null);
        if (definition != null) { // we got a rule for this particular TypeOffre
            ArrayList<DetailOffreJSON> details = input.getDetails();
            String should_be_infos = definition.stream()
                                                .reduce("", (acc, s) -> acc + " " + s);

            should_be_infos = "( Doit inclure " + should_be_infos + ")";

            // check if the input contains an invalid type offre
            for (DetailOffreJSON detail : details) {
                String type_detail = detail.getType_detail();
                int count = (int) definition.stream()
                                        .filter(type -> type.equals(type_detail))
                                        .count();

                if (type_detail.startsWith("APPEL")) {
                    // double detail_valeur = detail.getValeur().doubleValue();
                    // double offre_prix = input.getPrix();
                    // if (Double.compare(detail_valeur, offre_prix) != 0)
                    //     throw new Exception ("La valeur du type 'APPEL' doit etre " + offre_prix + " Ar");
                }

                if (count == 0)
                    throw new Exception (type_detail + " invalide pour ce type d'offre. " + should_be_infos);
            }

            // check if the input has a forgotten type offre
            int count = 0;
            for (String spec : definition) {
                count += (int) details.stream()
                                    .filter(detail -> detail.getType_detail().equals(spec))
                                    .count();
            }
            boolean isSpecialMobile = "TYPEOF2".equals(input.getIdtypeoffre());
            if (count == 0 && isSpecialMobile || count != definition.size() && !isSpecialMobile)
                throw new Exception ("Element manquant pour ce type d'offre. " + should_be_infos);
            
        }
    }

    public static ArrayList<Offre> getOffreByType (Connection con, String idtypeoffre) throws Exception {
        afQuery query = afQuery.use(con);
        ArrayList<Offre> offres = query.of(new Offre())
                                    .select()
                                    .where("idtypeoffre = ?", new String[] { idtypeoffre })
                                    .<Offre>get();
        for (Offre offre : offres) {
            ArrayList<DetailOffre> details = offre.getAllDetailsOffre(con);
            offre.setDetails(details);
        }
        return offres;
    }


    public static ArrayList<Offre> getAllOffreWithDetails (Connection con) throws Exception {
        afQuery query = afQuery.use(con);
        ArrayList<Offre> offres = query.of(new Offre())
                                        .select()
                                        .<Offre>get();
        for (Offre offre : offres) {
            ArrayList<DetailOffre> details = offre.getAllDetailsOffre(con);
            offre.setDetails(details);
        }

        return offres;
    }

    public static Offre getOffreById(Connection con, String id) throws Exception  {
        afQuery query = afQuery.use(con);
        String where = "idoffre = ?";
        
        String[] values = new String[] {id};
        
        ArrayList<Offre> offre = query.of(new Offre())
                                        .select().where(where, values)
                                        .<Offre>get();
        
        return offre.get(0);
    }
}