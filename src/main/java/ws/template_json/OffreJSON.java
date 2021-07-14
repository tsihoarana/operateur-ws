package ws.template_json;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import ws.tools.Tools;


public class OffreJSON implements InputValidateInterf {
	String idtypeoffre = null;
    String nom = null;
    int duree = 0;
    double prix = 0;
    int priorite = 0;

    ArrayList<DetailOffreJSON> details = new ArrayList<>();

    @Override
    public void validateOrThrows() throws Exception {
        Tools.emptyOrNullChecker(getIdtypeoffre(), "idtypeoffre");
        Tools.emptyOrNullChecker(getNom(), "nom");
        Tools.negatifOrZeroChecker(getDuree(), "duree");
        Tools.negatifOrZeroChecker(getPrix(), "prix");
        Tools.nullChecker(getDetails(), "details (liste)");
        // Tools.negatifOrZeroChecker(getPriorite(), "priorite");
        if (getDetails().isEmpty())
            throw new Exception("Les details de l'offre ne doit pas etre vide");
        for (DetailOffreJSON detail : details)
            detail.validateOrThrows();
    }

   public String getIdtypeoffre() {
       return idtypeoffre;
   }

   public void setIdtypeoffre(String idtypeoffre) {
       this.idtypeoffre = idtypeoffre;
   }

   public String getNom() {
       return nom;
   }

   public void setNom(String nom) {
       this.nom = nom;
   }

   public int getDuree() {
       return duree;
   }

   public void setDuree(int duree) {
       this.duree = duree;
   }

   public double getPrix() {
       return prix;
   }

   public void setPrix(double prix) {
       this.prix = prix;
   }

   public int getPriorite() {
       return priorite;
   }

   public void setPriorite(int priorite) {
       this.priorite = priorite;
   }

   public ArrayList<DetailOffreJSON> getDetails() {
       return details;
   }

   public void setDetails(ArrayList<DetailOffreJSON> details) {
       this.details = details;
   }
}