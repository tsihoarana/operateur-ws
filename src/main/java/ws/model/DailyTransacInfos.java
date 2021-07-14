package ws.model;

import java.math.BigDecimal;
import java.sql.Date;

/**
 * Objet de meme structure que les vues : CreditSumGroupByDayView, MvolaSumGroupByDayView
 */
public class DailyTransacInfos {
    BigDecimal montant;
    int nb_transac;
    Date date;

    public BigDecimal getMontant() {
        return montant;
    }

    public void setMontant(BigDecimal montant) {
        this.montant = montant;
    }

    public int getNb_transac() {
        return nb_transac;
    }

    public void setNb_transac(int nb_transac) {
        this.nb_transac = nb_transac;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    @Override
    public String toString() {
        return "DailyTransacInfos [date=" + date + ", montant=" + montant + ", nb_transac=" + nb_transac + "]";
    }
}