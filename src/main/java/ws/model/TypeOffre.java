package ws.model;

import java.util.ArrayList;

public class TypeOffre {
    String idtypeoffre = null;
    String nom = null;

    private ArrayList<Integer> possible_duree = new ArrayList<>();

    public TypeOffre () {}
    public TypeOffre (String idtypeoffre, String nom) {
        setIdtypeoffre (idtypeoffre);
        setNom (nom);
    }

    public void setIdtypeoffre (String idtypeoffre) {
        this.idtypeoffre = idtypeoffre;
    }
    public void setNom (String nom) {
        this.nom = nom;
    }

    public String getIdtypeoffre () {
        return this.idtypeoffre;
    }
    public String getNom () {
        return this.nom;
    }

    public ArrayList<Integer> getDefault_duree() {
        return possible_duree;
    }

    public void setDefault_duree(ArrayList<Integer> possible_duree) {
        this.possible_duree = possible_duree;
    }
    
    @Override
    public String toString () {
        return "[ idtypeoffre = " + idtypeoffre + " nom = " + nom + " ]";
    }

}