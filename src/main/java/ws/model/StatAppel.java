package ws.model;

import java.util.ArrayList;

import org.bson.Document;

public class StatAppel {
    String numeroCible;
    double duree;

    public static ArrayList<StatAppel> format(Document document) {
        ArrayList<StatAppel> res = new ArrayList<>();
        ArrayList unkown = (ArrayList) document.get("cursor", Document.class).get("firstBatch");

        for(Object doc : unkown) {
            String num = ((Document)doc).get("_id", Document.class).getString("numeroCible");
            double d = ((Document)doc).getDouble("total_duree").doubleValue();
            res.add(new StatAppel(num, d));
        }
        return res;
    }

    public String getNumeroCible() {
        return numeroCible;
    }

    public void setNumeroCible(String numeroCible) {
        this.numeroCible = numeroCible;
    }

    public double getDuree() {
        return duree;
    }

    public void setDuree(double duree) {
        this.duree = duree;
    }

    public StatAppel(String numeroCible, double duree) {
        setNumeroCible(numeroCible);
        setDuree(duree);
    }
    
}