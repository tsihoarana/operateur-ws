package ws.model;

import java.util.ArrayList;

/**
 * Objet qui permet de retenir la liste sommee groupee journaliere credit et mvola
 */
public class DataTransacList {
    ArrayList<DailyTransacInfos> credit;   
    ArrayList<DailyTransacInfos> mvola;

    public ArrayList<DailyTransacInfos> getCredit() {
        return credit;
    }

    public void setCredit(ArrayList<DailyTransacInfos> credit) {
        this.credit = credit;
    }

    public ArrayList<DailyTransacInfos> getMvola() {
        return mvola;
    }

    public void setMvola(ArrayList<DailyTransacInfos> mvola) {
        this.mvola = mvola;
    }

    @Override
    public String toString() {
        return "DataTransacList [credit=" + credit + ", mvola=" + mvola + "]";
    }
}