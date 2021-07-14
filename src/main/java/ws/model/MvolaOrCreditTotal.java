package ws.model;

import java.math.BigDecimal;

public class MvolaOrCreditTotal {
    String numero;
    String idclient;
    BigDecimal total;

    public String getNumero() {
        return numero;
    }

    public void setNumero(String numero) {
        this.numero = numero;
    }

    public String getIdclient() {
        return idclient;
    }

    public void setIdclient(String idclient) {
        this.idclient = idclient;
    }

    public BigDecimal getTotal() {
        return total;
    }

    public void setTotal(BigDecimal total) {
        this.total = total;
    }

    @Override
    public String toString() {
        return "MvolaTotal [idclient=" + idclient + ", numero=" + numero + ", total=" + total + "]";
    }
}