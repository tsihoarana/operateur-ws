package ws.services;

import java.math.BigDecimal;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.Map;

import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;

import org.bson.Document;

import ws.model.Appel;
import ws.model.Client;
import ws.model.CustomConfig;
import ws.model.DetailOffreClient;
import ws.model.DetailOffreClientComplet;
import ws.model.OffreActif;
import ws.model.OffreClient;
import ws.model.StatusClient;
import ws.template_json.AppelJSON;
import ws.template_json.CreditJSON;
import ws.template_json.InternetJSON;
import ws.template_json.SmsJSON;
import ws.tools.Tools;

public class SimulerService {

    public static double simulerAutre(Connection con, InternetJSON input, String type) throws Exception  {
        String numero = input.getNumero();
        double valeur = input.getValeur();
        ArrayList<DetailOffreClient> docspec = getOffreDeductible(con, numero, type);
        ArrayList<DetailOffreClient> docNet = getOffreDeductible(con, numero, "Internet");
        double ins = deductionCreditEtOffreAutre(con, numero, valeur, docspec, docNet);
        return ins;
    }


    public static double simulerAppel(MongoClient client, Connection con, AppelJSON appelinput) throws Exception  {
        String numero = appelinput.getNumeroAppelant();
        boolean IsInterne = isInterne(appelinput.getNumeroCible());
        ArrayList<DetailOffreClient> docAppel = IsInterne ? getOffreDeductible(con, numero, "APPEL_INTERNE") : getOffreDeductible(con, numero, "APPEL_EXTERNE");
        double dureeAppel = deductionCreditEtOffre(con, appelinput, docAppel);
        Appel appel = new Appel();
        appel.setNumeroAppelant(appelinput.getNumeroAppelant());
        appel.setNumeroCible(appelinput.getNumeroCible());
        appel.setDuree(dureeAppel);
        appel.setDate(Tools.getNow());
        AppelService.insert(client, appel);
        
        return dureeAppel;
    }

    public static double simulerInternet(Connection con, InternetJSON input) throws Exception  {
        String numero = input.getNumero();
        double valeur = input.getValeur();
        ArrayList<DetailOffreClient> docAppel = getOffreDeductible(con, numero, "Internet");
        double consommer = deductionCreditEtOffreInternet(con, numero, valeur, docAppel);
        return consommer;
    }

    public static void simulerSms(MongoClient client, Connection con, SmsJSON input) throws Exception  {
        String numero = input.getNumeroEnvoyeur();
        ArrayList<DetailOffreClient> docAppel = getOffreDeductible(con, numero, "sms");
        deductionCreditEtOffreSms(con, input, docAppel);
        insertSms(client, input);
    }

    public static void insertSms (MongoClient client, SmsJSON sms) throws Exception {
        MongoDatabase database = client.getDatabase("operateur");
        MongoCollection<Document> collection = database.getCollection("sms");

        Document document = new Document();
        document.append("numeroEnvoyeur", sms.getNumeroEnvoyeur());
        document.append("numeroCible", sms.getNumeroCible());
        document.append("objet", sms.getObjet());
        collection.insertOne(document);
    }

    private static ArrayList<DetailOffreClient> getOffreDeductible(Connection con, String numero, String type) throws Exception {
        ArrayList<OffreActif> offre_actif = OffreClientService.getOffreActif(con, numero);
        ArrayList<OffreClient> offreclients = new ArrayList<>();
        for (OffreActif offreactif : offre_actif) {
            offreclients.add(OffreClientService.getOffreClientById(con, offreactif.getIdoffreclient()));
        }
        ArrayList<DetailOffreClient> doc = new ArrayList<>();
        for (OffreClient offreclient : offreclients) {
            ArrayList<DetailOffreClientComplet> docomplets = offreclient.getAllDetailOffreClientComplet(con);
            for (DetailOffreClientComplet docomplet : docomplets) {
                if (docomplet.getType_detail().equalsIgnoreCase(type) && ( docomplet.getValeur_actuel().doubleValue() > 0 || docomplet.getValeur_actuel().intValue() == -1 ))
                    doc.add(docomplet.toDetailOffreClient());
            }
        }
        return doc;
    }

	// special mobile
    public static double deductionCreditEtOffreAutre(Connection con, String numero, double valeur, ArrayList<DetailOffreClient> offredetails, ArrayList<DetailOffreClient> offreNet) throws Exception {
        Map<String, BigDecimal> customConfig = PGService.getAllConfig(con);
        double PRIX_INTERNET = customConfig.get("PRIX_INTERNET_CREDIT").doubleValue();
        double prix_internet = valeur * PRIX_INTERNET;
        StatusClient status = ClientService.getStatusClientCompletByNumero(con, numero);
        double credit_actuel = status.getTotal_credit().doubleValue();
        Client client_appelant = ClientService.getClientByNumero(con, numero);
        if(offredetails.size() == 0) {
            return deductionCreditEtOffreInternet(con, numero, valeur, offreNet);

        } else {
            double reste = -prix_internet / PRIX_INTERNET;
            double total_offre = 0;
            for(DetailOffreClient offre_actif : offredetails) {
                double off_dvalue = offre_actif.getValeur_actuel().doubleValue();
                if (off_dvalue < 0)
                    return valeur;
                double update = off_dvalue - Math.min(off_dvalue, Math.abs(reste));
                offre_actif.updateValeur(con, BigDecimal.valueOf(update));
                total_offre += off_dvalue;
                reste = off_dvalue - Math.abs(reste);

                if(reste > 0 || reste == 0)
                    return prix_internet / PRIX_INTERNET;
            }
            if (reste < 0) {
                if (offreNet.size() != 0) {
                    double ins = deductionCreditEtOffreInternet(con, numero, Math.abs(reste), offreNet);
                    return Math.abs(ins) + (prix_internet / PRIX_INTERNET) - Math.abs(reste);
                }
            }
            double total = total_offre * PRIX_INTERNET;
            double resteTotal = Math.abs(reste) * PRIX_INTERNET;
            return dureeAppel(total + credit_actuel, total + resteTotal) / PRIX_INTERNET;
        }
    }

    public static double deductionCreditEtOffreInternet(Connection con, String numero, double valeur, ArrayList<DetailOffreClient> offredetails) throws Exception {
        Map<String, BigDecimal> customConfig = PGService.getAllConfig(con);
        double PRIX_INTERNET = customConfig.get("PRIX_INTERNET_CREDIT").doubleValue();
        double prix_internet = valeur * PRIX_INTERNET;
        StatusClient status = ClientService.getStatusClientCompletByNumero(con, numero);
        double credit_actuel = status.getTotal_credit().doubleValue();
        Client client_appelant = ClientService.getClientByNumero(con, numero);
        if(offredetails.size() == 0) {
            if(credit_actuel < PRIX_INTERNET) {
                throw new Exception("credit insuffisant pour utiliser internet");
            }

            CreditJSON inputcredit = new CreditJSON();
            inputcredit.setIdclient(client_appelant.getIdclient());
            inputcredit.setMontant(Math.min(credit_actuel, prix_internet));
            CreditService.operer(con, inputcredit, "retrait");
            return dureeAppel(credit_actuel, prix_internet) / PRIX_INTERNET;
        } else {
            double reste = -prix_internet / PRIX_INTERNET;
            double total_offre = 0;
            for(DetailOffreClient offre_actif : offredetails) {
                double off_dvalue = offre_actif.getValeur_actuel().doubleValue();
                if (off_dvalue < 0)
                    return valeur;
                double update = off_dvalue - Math.min(off_dvalue, Math.abs(reste));
                offre_actif.updateValeur(con, BigDecimal.valueOf(update));
                total_offre += off_dvalue;
                reste = off_dvalue - Math.abs(reste);

                if(reste > 0 || reste == 0)
                    return prix_internet / PRIX_INTERNET;
            }
            if (reste < 0 && credit_actuel > 0) {
                CreditJSON inputcredit = new CreditJSON();
                inputcredit.setIdclient(client_appelant.getIdclient());
                inputcredit.setMontant(Math.min(credit_actuel, Math.abs(reste * PRIX_INTERNET)));
                CreditService.operer(con, inputcredit, "retrait");
            }
            double total = total_offre * PRIX_INTERNET;
            double resteTotal = Math.abs(reste) * PRIX_INTERNET;
            return dureeAppel(total + credit_actuel, total + resteTotal) / PRIX_INTERNET;
        }
    }

    private static double deductionCreditEtOffreSms(Connection con, SmsJSON input, ArrayList<DetailOffreClient> offredetails) throws Exception {
        Map<String, BigDecimal> customConfig = PGService.getAllConfig(con);
        double PRIX_SMS = customConfig.get("PRIX_SMS_CREDIT").doubleValue();
        StatusClient status = ClientService.getStatusClientCompletByNumero(con, input.getNumeroEnvoyeur());
        double credit_actuel = status.getTotal_credit().doubleValue();
        Client client_appelant = ClientService.getClientByNumero(con, input.getNumeroEnvoyeur());
        if(offredetails.size() == 0) {
            if(credit_actuel < PRIX_SMS) {
                throw new Exception("credit insuffisant pour envoyer un sms");
            }

            CreditJSON inputcredit = new CreditJSON();
            inputcredit.setIdclient(client_appelant.getIdclient());
            inputcredit.setMontant(Math.min(credit_actuel, PRIX_SMS));
            CreditService.operer(con, inputcredit, "retrait");
            return 1;
        } else {
            double reste = -PRIX_SMS;
            double total_offre = 0;
            for(DetailOffreClient offre_actif : offredetails) {
                double off_dvalue = offre_actif.getValeur_actuel().doubleValue();
                if (off_dvalue < 0)
                    return 1;
                double update = off_dvalue - 1;
                offre_actif.updateValeur(con, BigDecimal.valueOf(update));
                total_offre += off_dvalue;
                reste = off_dvalue - Math.abs(reste);

                    return 1;
            }
        }
        return -100;
    }
	
	// appel
    public static double deductionCreditEtOffre(Connection con, AppelJSON input, ArrayList<DetailOffreClient> offredetails) throws Exception {
        String numeroAppelant = input.getNumeroAppelant();
        double prixRawAppel = getPrixRawAppel(con, input.getNumeroCible());
        double prix_appel = input.getDuree() * prixRawAppel;
        StatusClient status = ClientService.getStatusClientCompletByNumero(con, input.getNumeroAppelant());
        double credit_actuel = status.getTotal_credit().doubleValue();
        Client client_appelant = ClientService.getClientByNumero(con, input.getNumeroAppelant());
        if(offredetails.size() == 0) {
            if(credit_actuel < prixRawAppel) {
                throw new Exception("credit insuffisant pour effectuer un appel");
            }

            CreditJSON inputcredit = new CreditJSON();
            inputcredit.setIdclient(client_appelant.getIdclient());
            inputcredit.setMontant(Math.min(credit_actuel, prix_appel));
            CreditService.operer(con, inputcredit, "retrait");
            return dureeAppel(credit_actuel, prix_appel) / prixRawAppel;
        } else {
            prix_appel = input.getDuree();
            double reste = -prix_appel;
            double total_offre = 0;
            for(DetailOffreClient offre_actif : offredetails) {
                double off_dvalue = offre_actif.getValeur_actuel().doubleValue();
                if (off_dvalue < 0)
                    return prix_appel;
                double update = off_dvalue - Math.min(off_dvalue, Math.abs(reste));
                offre_actif.updateValeur(con, BigDecimal.valueOf(update));
                total_offre += off_dvalue;
                reste = off_dvalue - Math.abs(reste);

                if(reste > 0 || reste == 0)
                    return prix_appel;
            }
            if (reste < 0 && credit_actuel > 0) {
                CreditJSON inputcredit = new CreditJSON();
                inputcredit.setIdclient(client_appelant.getIdclient());
                inputcredit.setMontant(Math.min(credit_actuel, Math.abs(reste * prixRawAppel)));
                CreditService.operer(con, inputcredit, "retrait");
            }
            double t_offre_ar = total_offre * prixRawAppel;
            return dureeAppel(t_offre_ar + credit_actuel, t_offre_ar + Math.abs(reste * prixRawAppel)) / prixRawAppel;
        }
    }
    private static double dureeAppel(double credit, double prix_appel) {
        double test = credit - prix_appel;
        if ( test < 0 || test == 0 ) {
            return credit;
        }
        return prix_appel;
    }

    private static boolean isInterne (String numero) {
        if (numero.startsWith("034"))
            return true;
        return false;
    }

    private static double getPrixRawAppel (Connection con, String numeroCible) throws Exception {
        Map<String, BigDecimal> customConfig = PGService.getAllConfig(con);
        boolean IsInterne = isInterne(numeroCible);
        if (IsInterne)
            return customConfig.get("PRIX_APPEL_INTERNE").doubleValue();
        return customConfig.get("PRIX_APPEL_EXTERNE").doubleValue();
    }
    
}